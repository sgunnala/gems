/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.TibjmsTopicConnectionFactory
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.MessageListener
 *  javax.jms.Topic
 *  javax.jms.TopicConnection
 *  javax.jms.TopicSession
 *  javax.jms.TopicSubscriber
 */
package com.tibco.gems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import com.tibco.tibjms.TibjmsTopicConnectionFactory;

public class GemsServiceTable
        implements ActionListener {
    public Hashtable m_services = new Hashtable();
    public boolean m_running = false;
    public boolean m_enabled = false;
    public Mutex m_mutex = new Mutex();
    public Hashtable m_syncRequests = new Hashtable();
    public Hashtable m_cacheResp = null;
    public cacheResp[] m_cacheRespArray = null;
    protected long m_period;
    protected boolean m_useQTemps;
    protected PeriodThread m_timer = null;
    protected GemsConnectionNode m_cn;
    protected TopicSession m_reqSess = null;
    protected TopicSession m_respSess = null;
    protected TopicConnection m_connection = null;
    protected TopicSubscriber m_tempQueueSubscriber = null;
    protected TopicSubscriber m_tempTopicSubscriber = null;
    protected Random m_random = new Random();
    protected boolean m_useCache = true;
    protected int m_cacheIndex = 0;
    boolean m_useTTemps;

    public GemsServiceTable(GemsConnectionNode gemsConnectionNode, long period, boolean useQTemps, boolean useTTemps, boolean enabled) {
        this.m_cn = gemsConnectionNode;
        this.m_period = period;
        this.m_useQTemps = useQTemps;
        this.m_useTTemps = useTTemps;
        this.m_enabled = enabled;
        if (this.m_useCache) {
            this.m_cacheResp = new Hashtable();
            this.m_cacheRespArray = new cacheResp[1000];
            this.m_cacheIndex = 0;
        }
    }

    public synchronized void addToCache(String string, String string2, long l) {
        cacheResp cacheResp2 = this.m_cacheRespArray[this.m_cacheIndex];
        if (cacheResp2 != null) {
            cacheResp cacheResp3 = null;
            if (cacheResp2.m_cid != null && cacheResp2.m_cid.length() > 0) {
                cacheResp3 = (cacheResp) this.m_cacheResp.remove(cacheResp2.m_cid);
            }
            if (cacheResp3 == null && cacheResp2.m_target != null && cacheResp2.m_target.length() > 0) {
                cacheResp3 = (cacheResp) this.m_cacheResp.remove(cacheResp2.m_target);
            }
            if (cacheResp3 != null) {
                Gems.debug("GemsService.addToCache: Warning cache full, removed: " + cacheResp3.m_target + " " + cacheResp3.m_cid + " " + cacheResp3.m_timestamp + " " + this.m_cacheIndex);
            }
        }
        this.m_cacheRespArray[this.m_cacheIndex] = new cacheResp(string, string2, l);
        if (string2 != null && string2.length() > 0) {
            this.m_cacheResp.put(string2, this.m_cacheRespArray[this.m_cacheIndex]);
        } else {
            this.m_cacheResp.put(string, this.m_cacheRespArray[this.m_cacheIndex]);
        }
        if (++this.m_cacheIndex >= this.m_cacheRespArray.length) {
            this.m_cacheIndex = 0;
        }
    }

    public synchronized void systemStart() {
        if (this.m_enabled) {
            this.userStart();
        }
    }

    public synchronized void userStart() {
        this.stopServices();
        this.reset();
        if (this.m_timer != null) {
            this.m_timer.interrupt();
        }
        this.m_timer = new PeriodThread(this.m_period * 60000);
        this.m_timer.start();
    }

    public synchronized void stopServices() {
        try {
            if (this.m_connection == null) {
                return;
            }
            this.m_connection.stop();
            Enumeration enumeration = this.m_services.keys();
            while (enumeration.hasMoreElements()) {
                GemsService gemsService = (GemsService) this.m_services.get(enumeration.nextElement());
                gemsService.stop();
            }
            if (this.m_reqSess != null) {
                this.m_reqSess.close();
                this.m_reqSess = null;
            }
            if (this.m_respSess != null) {
                this.m_respSess.close();
                this.m_respSess = null;
            }
            this.m_connection.close();
            this.m_connection = null;
        } catch (Exception var1_2) {
            Gems.debug("GemsServiceTable:stop: Exception: " + var1_2.toString());
            return;
        }
    }

    public synchronized void reset() {
        Enumeration enumeration = this.m_services.keys();
        while (enumeration.hasMoreElements()) {
            GemsService gemsService = (GemsService) this.m_services.get(enumeration.nextElement());
            gemsService.reset();
        }
        this.m_syncRequests.clear();
        this.m_cacheResp.clear();
    }

    public synchronized void systemStop() {
        this.userStop();
    }

    public synchronized void userStop() {
        this.m_running = false;
        this.stopServices();
        this.reset();
        if (this.m_timer != null) {
            this.m_timer.interrupt();
        }
        this.m_timer = null;
    }

    public synchronized void startServices() {
        if (this.m_services.size() == 0) {
            Gems.debug("GemsServiceTable:startServices: No services found");
            return;
        }
        try {
            GemsService gemsService;
            Gems.debug("GemsServiceTable:startServices: Connecting to: " + this.m_cn.m_url);
            TibjmsTopicConnectionFactory tibjmsTopicConnectionFactory = new TibjmsTopicConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
            this.m_connection = tibjmsTopicConnectionFactory.createTopicConnection(this.m_cn.m_user, this.m_cn.m_password);
            this.m_reqSess = this.m_connection.createTopicSession(false, 22);
            this.m_respSess = this.m_connection.createTopicSession(false, 22);
            boolean bl = false;
            boolean bl2 = false;
            Enumeration enumeration = this.m_services.keys();
            while (enumeration.hasMoreElements()) {
                gemsService = (GemsService) this.m_services.get(enumeration.nextElement());
                gemsService.start(this.m_reqSess, this.m_respSess);
            }
            if (this.m_useQTemps) {
                Topic topic = this.m_reqSess.createTopic("$sys.monitor.q.r.$TMP$.>");
                this.m_tempQueueSubscriber = this.m_respSess.createSubscriber((Topic) topic);
                this.m_tempQueueSubscriber.setMessageListener((MessageListener) new OnSyncRespMessage());
                Gems.debug("GemsServiceTable:startServices: Adding queue $TMP$.>");
            }
            if (this.m_useTTemps) {
                Topic topic = this.m_reqSess.createTopic("$sys.monitor.t.r.$TMP$.>");
                this.m_tempTopicSubscriber = this.m_respSess.createSubscriber(topic);
                this.m_tempTopicSubscriber.setMessageListener((MessageListener) new OnSyncRespMessage());
                Gems.debug("GemsServiceTable:startServices: Adding topic $TMP$.>");
            }
            this.m_connection.start();
        } catch (JMSException var1_2) {
            Gems.debug("GemsServiceTable:startServices: Exception: " + var1_2.getMessage());
            return;
        }
    }

    public synchronized void addService(String name, String reqDest, boolean reqIsQueue, String respDest, boolean respIsQueue, long respLimit) {
        this.m_services.put(name, new GemsService(name, reqDest, reqIsQueue, respDest, respIsQueue, respLimit, this));
    }

    public void actionPerformed(ActionEvent actionEvent) {
        this.reset();
    }

    public synchronized void newSyncRequest(GemsService gemsService, String string, long l) {
        this.m_syncRequests.put(string, new syncRequest(gemsService, l));
    }

    class OnSyncRespMessage
            implements MessageListener {
        OnSyncRespMessage() {
        }

        public void onMessage(Message message) {
            try {
                String string = message.getStringProperty("msg_correlation_id");
                String string2 = message.getStringProperty("target_dest_name");
                long l = message.getLongProperty("msg_timestamp");
                if (l == 0) {
                    return;
                }
                if (string2 == null || !string2.startsWith("$TMP$")) {
                    Gems.debug("GemsServiceTable.onSyncRespMessage: No $TMP target");
                    return;
                }
                GemsServiceTable.this.m_mutex.acquire();
                syncRequest syncRequest2 = null;
                if (string != null && string.length() > 0) {
                    syncRequest2 = (syncRequest) GemsServiceTable.this.m_syncRequests.remove(string);
                }
                if (syncRequest2 == null && string2 != null && string2.length() > 0) {
                    syncRequest2 = (syncRequest) GemsServiceTable.this.m_syncRequests.remove(string2);
                }
                if (syncRequest2 != null) {
                    syncRequest2.m_service.newResponse(syncRequest2.m_timestamp, l, 0);
                } else if (GemsServiceTable.this.m_useCache) {
                    GemsServiceTable.this.addToCache(string2, string, l);
                }
            } catch (JMSException var2_3) {
            } catch (NumberFormatException var2_4) {
            } catch (InterruptedException var2_5) {
                Gems.debug("GemsServiceTable.onRespMessage: Exception: " + var2_5.toString());
            }
            GemsServiceTable.this.m_mutex.release();
        }
    }

    class PeriodThread
            extends Thread {
        long m_delay;

        PeriodThread(long l) {
            this.m_delay = l;
        }

        public void run() {
            GemsServiceTable.this.m_running = true;
            try {
                PeriodThread.sleep(2000 + GemsServiceTable.this.m_random.nextInt(5000));
            } catch (Exception var1_1) {
                Gems.debug("PeriodThread.Exception: " + var1_1.getMessage());
                return;
            }
            GemsServiceTable.this.startServices();
            while (this.m_delay > 0 && GemsServiceTable.this.m_running) {
                try {
                    PeriodThread.sleep(this.m_delay);
                    if (!GemsServiceTable.this.m_running) continue;
                    GemsServiceTable.this.reset();
                    continue;
                } catch (Exception var1_2) {
                    Gems.debug("PeriodThread.Exception: " + var1_2.getMessage());
                    return;
                }
            }
        }
    }

    class cacheResp {
        String m_cid;
        String m_target;
        long m_timestamp;

        public cacheResp(String string, String string2, long l) {
            this.m_target = string;
            this.m_timestamp = l;
            this.m_cid = string2;
        }
    }

    class syncRequest {
        long m_timestamp;
        GemsService m_service;

        public syncRequest(GemsService gemsService, long l) {
            this.m_timestamp = l;
            this.m_service = gemsService;
        }
    }

}

