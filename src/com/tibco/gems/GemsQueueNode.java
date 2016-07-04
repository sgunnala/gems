/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 */
package com.tibco.gems;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import com.tibco.tibjms.admin.QueueInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;

public class GemsQueueNode
        extends IconNode {
    static Hashtable props = null;

    public GemsQueueNode(String string) {
        super(string, false);
        GemsQueueNode.init();
        if (Gems.getGems().m_useMetalIcons) {
            this.setIconName("queue");
        }
    }

    public static synchronized void init() {
        if (props == null) {
            try {
                props = new Hashtable();
                props.put("isGlobal", new GemsProperty("Global", Boolean.TYPE, "<html>Enables or disables global routing of messages<br>on this destination</html>"));
                props.put("isFailsafe", new GemsProperty("Failsafe", Boolean.TYPE, "<html>Whether the server writes persistent messages<br>to disk synchronously (pre EMS5.0 only)</html>"));
                props.put("isSecure", new GemsProperty("Secure", Boolean.TYPE, "<html>Whether the server checks user permissions<br> on the destination</html>"));
                props.put("FlowControl", new GemsProperty("FlowControlMaxBytes", Long.TYPE, "<html>The target maximum size (bytes) the server<br>can use to store pending messages</html>"));
                props.put("MaxBytes", new GemsProperty("MaxBytes", Long.TYPE, "<html>The maximum size (in bytes) that the destination<br>can store, summed over all messages</html>"));
                props.put("isSenderName", new GemsProperty("SenderName", Boolean.TYPE, "<html>Whether the server may include the sender\u00eds<br>username for messages sent to this destination</html>"));
                props.put("isSenderNameEnforced", new GemsProperty("SenderNameEnforced", Boolean.TYPE, "<html>Whether the server must include the sender\u00eds<br>username for messages sent to this destination</html>"));
                props.put("MsgTrace", new GemsProperty("MsgTrace", Byte.TYPE, "<html>Set the level of message tracing<br>0 = none, 1 = basic, 3 = detail</html>"));
                props.put("Prefetch", new GemsProperty("Prefetch", Integer.TYPE, "<html>Number of messages prefetched by consumers<br>of this destination (0 = default, -1 = none)</html>"));
                props.put("MaxRedelivery", new GemsProperty("MaxRedelivery", Integer.TYPE, "<html>The number of attempts the server should make<br>to redeliver a message (0 = no limit)</html>"));
                props.put("isExclusive", new GemsProperty("Exclusive", Boolean.TYPE, "<html>Whether the server sends all messages on<br>the queue to one consumer</html>"));
                props.put("ExpiryOverride", new GemsProperty("ExpiryOverride", Long.TYPE, "<html>The expiry override for this destination<br>in millisecs (0 = disable override)</html>"));
                props.put("OverflowPolicy", new GemsProperty("OverflowPolicy", Integer.TYPE, "<html>Behaviour when message capacity is exceeded<br>0 = default, 1 = discardOld, 2 = rejectIncoming</html>"));
                props.put("MaxMsgs", new GemsProperty("MaxMsgs", Long.TYPE, "<html>The maximum number of messages that can be<br>waiting on a destination</html>"));
                props.put("Store", new GemsProperty("Store", Class.forName("java.lang.String"), "<html>Where messages on this destination are stored<br>First stop the flow of incoming messages!</html>"));
                props.put("RedeliveryDelay", new GemsProperty("RedeliveryDelay", Long.TYPE, "<html>The interval (msecs) before returning an unacknowledged<br>message to the queue. 0 = no redelivery delay</html>"));
            } catch (Throwable var0) {
                System.err.println(var0);
            }
        }
    }

    public GemsQueueNode(String string, boolean bl) {
        super(string, false);
        GemsQueueNode.init();
        if (Gems.getGems().m_useMetalIcons) {
            if (bl) {
                this.setIconName("routedqueue");
            } else {
                this.setIconName("queue");
            }
        }
    }

    public static void setProperty(JFrame jFrame, GemsConnectionNode gemsConnectionNode, String string, String string2) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        GemsQueueNode.init();
        try {
            String string3;
            GemsSetPropDialog gemsSetPropDialog;
            GemsProperty gemsProperty = null;
            String string4 = null;
            QueueInfo queueInfo = gemsConnectionNode.m_adminConn.getQueue(string2);
            if (queueInfo == null) {
                return;
            }
            if (string != null && (gemsProperty = (GemsProperty) props.get(string)) != null) {
                string4 = gemsProperty.getValue((Object) queueInfo);
            }
            if ((string3 = (gemsSetPropDialog = new GemsSetPropDialog(jFrame, "Set Property on Queue " + string2)).getValue(props, string, string4)) != null) {
                gemsProperty = (GemsProperty) props.get(gemsSetPropDialog.getSelectedProp());
                gemsProperty.setValue((Object) queueInfo, string3);
                gemsConnectionNode.m_adminConn.updateQueue(queueInfo);
                Gems.getGems().scheduleRepaint();
            }
        } catch (TibjmsAdminException var4_5) {
            JOptionPane.showMessageDialog(jFrame, var4_5.getMessage(), "Set Queue Property", 0);
            return;
        }
    }

    public boolean hasMessages() {
        return false;
    }

    public Vector getMessages() {
        return null;
    }

    public void setProperty(GemsConnectionNode gemsConnectionNode, String string) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        try {
            GemsSetPropDialog gemsSetPropDialog;
            String string2;
            GemsProperty gemsProperty = null;
            String string3 = null;
            QueueInfo queueInfo = gemsConnectionNode.m_adminConn.getQueue((String) this.getUserObject());
            if (queueInfo == null) {
                return;
            }
            if (string != null && (gemsProperty = (GemsProperty) props.get(string)) != null) {
                string3 = gemsProperty.getValue((Object) queueInfo);
            }
            if ((string2 = (gemsSetPropDialog = new GemsSetPropDialog(Gems.getGems().m_frame, "Set Property on Queue " + (String) this.getUserObject())).getValue(props, string, string3)) != null) {
                gemsProperty = (GemsProperty) props.get(gemsSetPropDialog.getSelectedProp());
                gemsProperty.setValue((Object) queueInfo, string2);
                gemsConnectionNode.m_adminConn.updateQueue(queueInfo);
                Gems.getGems().scheduleRepaint();
            }
        } catch (TibjmsAdminException var3_4) {
            JOptionPane.showMessageDialog(Gems.getGems().m_frame, var3_4.getMessage(), "Set Queue Property", 0);
            return;
        }
    }
}

