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

import java.util.Random;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import com.tibco.tibjms.TibjmsTopicConnectionFactory;

public class GemsEventMonitor
        implements MessageListener {
    public Vector m_subscriptions = new Vector();
    public Vector m_subscribers = new Vector();
    public Vector m_messages = new Vector();
    public boolean m_running = false;
    public Mutex m_mutex = new Mutex();
    public boolean m_enabled = false;
    protected long m_maxEvents;
    protected GemsConnectionNode m_cn;
    protected TopicSession m_sess = null;
    protected TopicConnection m_connection = null;
    protected StartThread m_timer = null;
    protected Random m_random = new Random();

    public GemsEventMonitor(GemsConnectionNode gemsConnectionNode, long l, boolean bl) {
        this.m_cn = gemsConnectionNode;
        this.m_maxEvents = l;
        this.m_enabled = bl;
    }

    public synchronized void systemStart() {
        if (this.m_enabled) {
            this.userStart();
        }
    }

    public synchronized void userStart() {
        this.stopMonitor();
        this.reset();
        this.m_timer = new StartThread();
        this.m_timer.start();
    }

    public synchronized void stopMonitor() {
        try {
            if (this.m_connection == null) {
                return;
            }
            this.m_connection.stop();
            for (int i = 0; i < this.m_subscribers.size(); ++i) {
                TopicSubscriber topicSubscriber = (TopicSubscriber) this.m_subscribers.get(i);
                topicSubscriber.close();
            }
            this.m_subscribers.removeAllElements();
            if (this.m_sess != null) {
                this.m_sess.close();
                this.m_sess = null;
            }
            this.m_connection.close();
            this.m_connection = null;
            this.m_messages.removeAllElements();
        } catch (Exception var1_2) {
            Gems.debug("GemsEventMonitor:stop: Exception: " + var1_2.toString());
            return;
        }
    }

    public synchronized void reset() {
        try {
            this.m_mutex.acquire();
            this.m_messages.removeAllElements();
        } catch (InterruptedException var1_1) {
            Gems.debug("GemsEventMonitor.reset: Exception: " + var1_1.toString());
        }
        this.m_mutex.release();
        Gems.getGems().treeRepaint();
    }

    public synchronized void systemStop() {
        this.userStop();
    }

    public synchronized void userStop() {
        this.m_running = false;
        if (this.m_timer != null) {
            this.m_timer.interrupt();
        }
        this.stopMonitor();
        this.reset();
    }

    public synchronized Vector getMessages() {
        return this.m_messages;
    }

    public synchronized void startMonitor() {
        if (this.m_subscriptions.size() == 0) {
            System.err.println("GemsEventMonitor:start: No subscriptions found");
            return;
        }
        try {
            TibjmsTopicConnectionFactory tibjmsTopicConnectionFactory = new TibjmsTopicConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
            this.m_connection = tibjmsTopicConnectionFactory.createTopicConnection(this.m_cn.m_user, this.m_cn.m_password);
            this.m_sess = this.m_connection.createTopicSession(false, 22);
            for (int i = 0; i < this.m_subscriptions.size(); ++i) {
                subscription subscription2 = (subscription) this.m_subscriptions.get(i);
                Topic topic = this.m_sess.createTopic(subscription2.m_dest);
                TopicSubscriber topicSubscriber = this.m_sess.createSubscriber(topic, subscription2.m_sel, false);
                topicSubscriber.setMessageListener((MessageListener) this);
                this.m_subscribers.add(topicSubscriber);
                Gems.debug("GemsEventMonitor:start: Adding subscription: " + subscription2.m_dest);
            }
            this.m_connection.start();
            this.m_running = true;
        } catch (JMSException var1_2) {
            System.err.println("GemsEventMonitor:start: Exception: " + var1_2.getMessage());
            return;
        }
    }

    public synchronized void addSubscription(String string, String string2) {
        this.m_subscriptions.add(new subscription(string, string2));
    }

    public int getMessageCount() {
        return this.m_messages.size();
    }

    public void onMessage(Message message) {
        try {
            this.m_mutex.acquire();
            this.m_messages.add(message);
            if ((long) this.m_messages.size() > this.m_maxEvents) {
                this.m_messages.removeElementAt(0);
            }
            if (this.m_messages.size() == 1) {
                Gems.getGems().treeRepaint();
            }
        } catch (InterruptedException var2_2) {
            Gems.debug("GemsServiceTable.onRespMessage: Exception: " + var2_2.toString());
        }
        this.m_mutex.release();
    }

    class StartThread
            extends Thread {
        StartThread() {
        }

        public void run() {
            try {
                StartThread.sleep(3000 + GemsEventMonitor.this.m_random.nextInt(5000));
            } catch (Exception var1_1) {
                Gems.debug("StartThread.Exception: " + var1_1.getMessage());
                return;
            }
            GemsEventMonitor.this.startMonitor();
        }
    }

    class subscription {
        String m_dest;
        String m_sel;

        public subscription(String string, String string2) {
            this.m_dest = string;
            this.m_sel = string2;
        }
    }

}

