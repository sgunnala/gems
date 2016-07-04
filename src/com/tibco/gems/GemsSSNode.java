/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.TibjmsQueueConnectionFactory
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.Queue
 *  javax.jms.QueueConnection
 *  javax.jms.QueueReceiver
 *  javax.jms.QueueSender
 *  javax.jms.QueueSession
 *  javax.jms.TemporaryQueue
 */
package com.tibco.gems;

import java.util.Hashtable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

import com.tibco.tibjms.TibjmsQueueConnectionFactory;

public class GemsSSNode
        extends IconNode {
    static Hashtable props = null;
    public String m_name = null;
    public String m_queueName = null;
    public String m_ver = null;
    public QueueSession m_session = null;
    QueueConnection m_connection = null;
    Queue m_queue = null;
    Queue m_replyQueue = null;
    QueueSender m_sender = null;
    QueueReceiver m_receiver = null;
    int m_seqno = 0;
    long TIMEOUT = Gems.getGems().getSSTimeout();
    GemsConnectionNode m_cn = null;

    public GemsSSNode(String string, String string2, String string3, GemsConnectionNode gemsConnectionNode) {
        super(string, true);
        this.m_name = string;
        this.m_queueName = string2;
        this.m_cn = gemsConnectionNode;
        this.m_ver = string3;
        if (Gems.getGems().m_useMetalIcons) {
            this.setIconName("substation");
        }
    }

    public synchronized String RunCommand(String string) {
        if (this.m_connection == null) {
            this.init(this.m_cn);
        }
        if (this.m_connection == null) {
            return "Error: EMS server connection error";
        }
        try {
            MapMessage mapMessage = this.m_session.createMapMessage();
            String string2 = string;
            mapMessage.setString("SXS-COMMAND", string2);
            mapMessage.setJMSReplyTo((Destination) this.m_replyQueue);
            int n = ++this.m_seqno;
            mapMessage.setJMSCorrelationID(String.valueOf(n));
            this.m_sender.send((Message) mapMessage, 22, 4, 4 * this.TIMEOUT);
            MapMessage mapMessage2 = this.receiveReply(n);
            if (mapMessage2 != null) {
                Gems.debug("SubStation returned: " + mapMessage2.getString("SXS-RESULT"));
                return mapMessage2.getString("SXS-RESULT");
            }
            System.err.println("Error: Timeout waiting for SubStation response");
        } catch (JMSException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            this.destroy();
        }
        return "Error: Timeout waiting for SubStation reply (timeout value may be set in gems.props file)";
    }

    public synchronized void init(GemsConnectionNode gemsConnectionNode) {
        try {
            TibjmsQueueConnectionFactory tibjmsQueueConnectionFactory = new TibjmsQueueConnectionFactory(gemsConnectionNode.m_url, null, gemsConnectionNode.m_sslParams);
            this.m_connection = tibjmsQueueConnectionFactory.createQueueConnection(gemsConnectionNode.m_user, gemsConnectionNode.m_password);
            this.m_session = this.m_connection.createQueueSession(false, 22);
            this.m_queue = this.m_session.createQueue(this.m_queueName);
            this.m_sender = this.m_session.createSender(this.m_queue);
            this.m_sender.setDeliveryMode(22);
            this.m_replyQueue = this.m_session.createTemporaryQueue();
            this.m_receiver = this.m_session.createReceiver(this.m_replyQueue);
            this.m_connection.start();
        } catch (JMSException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            this.m_receiver = null;
            this.m_sender = null;
            this.m_session = null;
            this.m_connection = null;
            return;
        }
    }

    synchronized MapMessage receiveReply(int n) throws JMSException {
        MapMessage mapMessage = null;
        long l = this.TIMEOUT;
        do {
            long l2 = System.currentTimeMillis();
            mapMessage = (MapMessage) this.m_receiver.receive(l);
            if (mapMessage == null) {
                return mapMessage;
            }
            String string = mapMessage.getJMSCorrelationID();
            try {
                if (string != null && Integer.valueOf(string) == n) {
                    break;
                }
            } catch (NumberFormatException var8_7) {
                System.err.println("Error: " + var8_7.getMessage());
            }
            long l3 = System.currentTimeMillis();
            long l4 = l3 - l2;
            if (l4 <= 200) continue;
            l -= l4;
        } while (l > 10);
        return mapMessage;
    }

    public synchronized void destroy() {
        try {
            if (this.m_receiver != null) {
                this.m_receiver.close();
            }
            if (this.m_sender != null) {
                this.m_sender.close();
            }
            if (this.m_session != null) {
                this.m_session.close();
            }
            if (this.m_connection != null) {
                this.m_connection.close();
            }
            this.m_receiver = null;
            this.m_sender = null;
            this.m_session = null;
            this.m_connection = null;
        } catch (JMSException var1_1) {
            this.m_receiver = null;
            this.m_sender = null;
            this.m_session = null;
            this.m_connection = null;
            return;
        }
    }
}

