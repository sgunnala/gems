/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.MessageListener
 *  javax.jms.Topic
 *  javax.jms.TopicSession
 *  javax.jms.TopicSubscriber
 */
package com.tibco.gems;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class GemsService {
    public String m_name = null;
    public String m_reqDest = null;
    public String m_respDest = null;
    public long m_hits = 0;
    public long m_responses = 0;
    public long m_totalLatency = 0;
    public long m_minLatency = 0;
    public long m_maxLatency = 0;
    public long m_faults = 0;
    public long m_started = 0;
    public long m_totalReqSize = 0;
    public long m_minReqSize = 0;
    public long m_maxReqSize = 0;
    public long m_totalRespSize = 0;
    public long m_minRespSize = 0;
    public long m_maxRespSize = 0;
    public long m_respLimit = 0;
    public long m_overLimitCount = 0;
    public long m_lastRequest = 0;
    public boolean m_reqIsQueue = true;
    public boolean m_respIsQueue = true;
    public Hashtable m_requests = new Hashtable();
    public Hashtable m_cacheResp = null;
    public CacheResp[] m_cacheRespArray = null;
    protected TopicSubscriber m_reqsub = null;
    protected TopicSubscriber m_respsub = null;
    protected GemsServiceTable m_table = null;
    protected boolean m_useCache = true;
    protected int m_cacheIndex = 0;

    public GemsService(String name, String reqDest, boolean reqIsQueue, String respDest, boolean respIsQueue, long respLimit, GemsServiceTable gemsServiceTable) {
        this.m_reqIsQueue = reqIsQueue;
        this.m_respIsQueue = respIsQueue;
        this.m_respLimit = respLimit;
        if (this.m_useCache) {
            this.m_cacheResp = new Hashtable();
            this.m_cacheRespArray = new CacheResp[500];
            this.m_cacheIndex = 0;
        }
        this.m_name = name;
        this.m_reqDest = reqIsQueue ? "$sys.monitor.q.r." + reqDest : "$sys.monitor.t.r." + reqDest;
        if (respDest != null && respDest.length() > 0) {
            this.m_respDest = respIsQueue ? "$sys.monitor.q.r." + respDest : "$sys.monitor.t.r." + respDest;
        }
        this.m_started = System.currentTimeMillis();
        this.m_table = gemsServiceTable;
        Gems.debug("GemsService: " + name + " " + this.m_reqDest + " " + this.m_respDest);
    }

    public synchronized void reset() {
        this.m_hits = 0;
        this.m_responses = 0;
        this.m_totalLatency = 0;
        this.m_minLatency = 0;
        this.m_maxLatency = 0;
        this.m_faults = 0;
        this.m_totalReqSize = 0;
        this.m_minReqSize = 0;
        this.m_maxReqSize = 0;
        this.m_totalRespSize = 0;
        this.m_minRespSize = 0;
        this.m_maxRespSize = 0;
        this.m_overLimitCount = 0;
        this.m_lastRequest = 0;
        this.m_started = System.currentTimeMillis();
        this.m_requests.clear();
        this.m_cacheResp.clear();
    }

    public synchronized void addToCache(String string, long l) {
        CacheResp cacheResp2 = this.m_cacheRespArray[this.m_cacheIndex];
        if (cacheResp2 != null) {
            CacheResp cacheResp3 = null;
            if (cacheResp2.m_cid != null && cacheResp2.m_cid.length() > 0) {
                cacheResp3 = (CacheResp) this.m_cacheResp.remove(cacheResp2.m_cid);
            }
            if (cacheResp3 != null) {
                Gems.debug("GemsService.addToCache: Warning cache full, removed:  " + cacheResp3.m_cid + " " + cacheResp3.m_timestamp + " " + this.m_cacheIndex);
            }
        }
        this.m_cacheRespArray[this.m_cacheIndex] = new CacheResp(string, l);
        this.m_cacheResp.put(string, this.m_cacheRespArray[this.m_cacheIndex]);
        if (++this.m_cacheIndex >= this.m_cacheRespArray.length) {
            this.m_cacheIndex = 0;
        }
    }

    public synchronized void newRequest(long l, long l2) {
        ++this.m_hits;
        if (l2 < this.m_minReqSize) {
            this.m_minReqSize = l2;
        }
        if (l2 > this.m_maxReqSize) {
            this.m_maxReqSize = l2;
        }
        this.m_totalReqSize += l2;
        this.m_lastRequest = l;
    }

    public synchronized void newResponse(long l, long l2, long l3) {
        if (l2 < l) {
            Gems.debug("GemsService:newResponse: Error response timestamp before request");
            return;
        }
        long l4 = l2 - l;
        ++this.m_responses;
        if (l4 < this.m_minLatency) {
            this.m_minLatency = l4;
        }
        if (l4 > this.m_maxLatency) {
            this.m_maxLatency = l4;
        }
        this.m_totalLatency += l4;
        if (l3 < this.m_minRespSize) {
            this.m_minRespSize = l3;
        }
        if (l3 > this.m_maxRespSize) {
            this.m_maxRespSize = l3;
        }
        this.m_totalRespSize += l3;
        if (l4 > this.m_respLimit) {
            ++this.m_overLimitCount;
        }
    }

    public synchronized void start(TopicSession topicSession, TopicSession topicSession2) {
        try {
            Gems.debug("GemsService:start " + this.m_name);
            Topic topic = topicSession.createTopic(this.m_reqDest);
            this.m_reqsub = topicSession.createSubscriber(topic);
            this.m_reqsub.setMessageListener((MessageListener) new OnReqMessage());
            if (this.m_respDest != null && this.m_respDest.length() > 0) {
                topic = topicSession.createTopic(this.m_respDest);
                this.m_respsub = topicSession.createSubscriber(topic);
                this.m_respsub.setMessageListener((MessageListener) new OnRespMessage());
            }
        } catch (Exception var3_4) {
            Gems.debug("GemsService.start: Exception: " + var3_4.getMessage());
        }
    }

    public synchronized void stop() {
        try {
            if (this.m_reqsub != null) {
                this.m_reqsub.close();
                this.m_reqsub = null;
            }
            if (this.m_respsub != null) {
                this.m_respsub.close();
                this.m_respsub = null;
            }
        } catch (Exception var1_1) {
            Gems.debug("GemsService.stop: Exception: " + var1_1.getMessage());
        }
    }

    class OnRespMessage
            implements MessageListener {
        OnRespMessage() {
        }

        public void onMessage(Message message) {
            try {
                String string = message.getStringProperty("msg_correlation_id");
                if (string == null || string.length() == 0) {
                    Gems.debug("GemsService.onRespMessage: No correlationID ");
                    return;
                }
                long l = message.getLongProperty("msg_timestamp");
                Long l2 = (Long) GemsService.this.m_requests.remove(string);
                if (l2 == null) {
                    if (GemsService.this.m_useCache) {
                        GemsService.this.addToCache(string, l);
                        return;
                    }
                    Gems.debug("GemsService.onRespMessage: No request found for CID: " + string);
                }
                GemsService.this.newResponse(l2, l, 0);
            } catch (JMSException var2_3) {
            } catch (NumberFormatException var2_4) {
                // empty catch block
            }
        }
    }

    class OnReqMessage
            implements MessageListener {
        OnReqMessage() {
        }

        public void onMessage(Message message) {
            try {
                if (!message.propertyExists("msg_timestamp")) {
                    Gems.debug("GemsService.onReqMessage: No msg_timestamp!");
                    return;
                }
                long l = message.getLongProperty("msg_timestamp");
                String string = message.getStringProperty("msg_correlation_id");
                String string2 = message.getStringProperty("replyTo");
                if (string2 == null || string2.length() == 0) {
                    Gems.debug("GemsServiceTable.onReqMessage: No replyTo");
                    return;
                }
                if (string2.startsWith("$TMP$")) {
                    if (string == null || string.length() == 0) {
                        string = string2;
                    }
                    GemsService.this.m_table.m_mutex.acquire();
                    if (GemsService.this.m_table.m_useCache) {
                        GemsServiceTable.cacheResp cacheResp2 = (GemsServiceTable.cacheResp) GemsService.this.m_table.m_cacheResp.remove(string);
                        if (cacheResp2 == null && string != string2) {
                            cacheResp2 = (GemsServiceTable.cacheResp) GemsService.this.m_table.m_cacheResp.remove(string2);
                        }
                        if (cacheResp2 != null) {
                            GemsService.this.newRequest(l, 0);
                            GemsService.this.newResponse(l, cacheResp2.m_timestamp, 0);
                            GemsService.this.m_table.m_mutex.release();
                            return;
                        }
                    }
                    GemsService.this.m_table.newSyncRequest(GemsService.this, string, l);
                    GemsService.this.newRequest(l, 0);
                    GemsService.this.m_table.m_mutex.release();
                } else {
                    if (string == null || string.length() == 0) {
                        String object = message.getStringProperty("msg_id");
                        Gems.debug("GemsServiceTable.onReqMessage: No correlationId using MsgId");
                        if (object != null && object.length() > 0) {
                            string = object;
                        } else {
                            Gems.debug("GemsServiceTable.onReqMessage: No correlationId or MsgId");
                            return;
                        }
                    }
                    if (GemsService.this.m_useCache) {
                        CacheResp cacheResp = (CacheResp) GemsService.this.m_cacheResp.remove(string);

                        if (cacheResp == null) {
                            cacheResp = (CacheResp) GemsService.this.m_cacheResp.remove(string);
                        }

                        if (cacheResp != null) {
                            GemsService.this.newRequest(l, 0);
                            GemsService.this.newResponse(l, cacheResp.m_timestamp, 0);
                            return;
                        }
                    }
                    GemsService.this.m_requests.put(string, new Long(l));
                    GemsService.this.newRequest(l, 0);
                }
            } catch (JMSException var2_3) {
                Gems.debug("GemsService.onReqMessage: Exception: " + var2_3.toString());
            } catch (NumberFormatException var2_4) {
                Gems.debug("GemsService.onReqMessage: Exception: " + var2_4.toString());
            } catch (InterruptedException var2_5) {
                Gems.debug("GemsService.onReqMessage: Exception: " + var2_5.toString());
            }
            GemsService.this.m_table.m_mutex.release();
        }
    }

    class CacheResp {
        String m_cid;
        long m_timestamp;

        public CacheResp(String string, long l) {
            this.m_timestamp = l;
            this.m_cid = string;
        }
    }

    class request {
        long m_timestamp;
        String m_cid;

        public request(long l, String string) {
            this.m_timestamp = l;
            this.m_cid = string;
        }
    }

}

