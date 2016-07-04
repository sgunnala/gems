/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.Tibjms
 *  javax.jms.BytesMessage
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.ObjectMessage
 *  javax.jms.QueueBrowser
 *  javax.jms.StreamMessage
 *  javax.jms.TextMessage
 */
package com.tibco.gems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.QueueBrowser;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.tibco.tibjms.Tibjms;

public class GemsMessageTableModel
        extends DefaultTableModel
        implements GetPopupHandler {
    static long s_msgId = 0;
    public boolean m_showCheckbox = false;
    public boolean m_showEvents = false;
    public boolean m_monitor = false;
    JTable m_table;
    Hashtable m_msgs = new Hashtable();
    Hashtable m_replymsgs = null;
    boolean m_isEditable;
    Object m_obj = new Object();
    PopupHandler m_popup = null;
    MyRenderer m_renderer = null;
    MyCheckboxRenderer m_checkRenderer = new MyCheckboxRenderer();
    SimpleDateFormat dateFormatMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss SSS zzz yyyy");

    public GemsMessageTableModel(boolean bl, boolean bl2) {
        this.m_isEditable = bl;
        this.m_showCheckbox = bl2;
        this.m_renderer = new MyRenderer();
    }

    public PopupHandler getPopupHandler() {
        if (this.m_popup == null) {
            this.m_popup = new PopupMsgTableHandler(this.m_table, this);
        }
        return this.m_popup;
    }

    public Class getColumnClass(int n) {
        Object object = this.getValueAt(0, n);
        if (object != null) {
            return object.getClass();
        }
        return String.class;
    }

    public boolean isCellEditable(int n, int n2) {
        if (this.m_showCheckbox && n2 < 1) {
            return true;
        }
        return this.m_isEditable;
    }

    public Message getSelectedMessage() {
        if (this.m_msgs == null) {
            return null;
        }
        if (this.m_table.getSelectedRow() < 0) {
            return null;
        }
        if (this.m_showCheckbox) {
            return (Message) this.m_msgs.get((String) this.m_table.getValueAt(this.m_table.getSelectedRow(), 1));
        }
        return (Message) this.m_msgs.get((String) this.m_table.getValueAt(this.m_table.getSelectedRow(), 0));
    }

    public Message getSelectedReplyMessage() {
        if (this.m_replymsgs == null) {
            return null;
        }
        if (this.m_table.getSelectedRow() < 0) {
            return null;
        }
        Message message = null;
        String string = (String) this.m_table.getValueAt(this.m_table.getSelectedRow(), 4);
        if (string != null) {
            message = (Message) this.m_replymsgs.get(string);
        }
        if (message == null && (string = (String) this.m_table.getValueAt(this.m_table.getSelectedRow(), 5)) != null) {
            message = (Message) this.m_replymsgs.get(string);
        }
        return message;
    }

    public Message getMessageAt(int n) {
        if (this.m_msgs == null) {
            return null;
        }
        if (this.m_showCheckbox) {
            return (Message) this.m_msgs.get((String) this.m_table.getValueAt(n, 1));
        }
        return (Message) this.m_msgs.get((String) this.m_table.getValueAt(n, 0));
    }

    public Message getReplyMessageFor(String string) {
        if (this.m_replymsgs == null) {
            return null;
        }
        Message message = null;
        if (string != null) {
            message = (Message) this.m_replymsgs.get(string);
        }
        return message;
    }

    public Message getNextPendingRequest(boolean bl) {
        if (this.m_msgs == null) {
            return null;
        }
        if (bl) {
            for (int i = 0; i < this.m_table.getRowCount(); ++i) {
                String string = (String) this.m_table.getValueAt(i, 2);
                if (!string.startsWith("pending")) continue;
                return (Message) this.m_msgs.get((String) this.m_table.getValueAt(i, 0));
            }
        } else {
            for (int i = this.m_table.getRowCount() - 1; i >= 0; --i) {
                String string = (String) this.m_table.getValueAt(i, 2);
                if (!string.startsWith("pending")) continue;
                return (Message) this.m_msgs.get((String) this.m_table.getValueAt(i, 0));
            }
        }
        return null;
    }

    public Vector getSelectedMessages() {
        Vector vector = new Vector();
        if (this.m_showCheckbox) {
            for (int i = 0; i < this.getRowCount(); ++i) {
                if (!((Boolean) this.m_table.getValueAt(i, 0)).booleanValue()) continue;
                vector.add(this.m_msgs.get((String) this.m_table.getValueAt(i, 1)));
            }
        }
        return vector;
    }

    public void selectAllRows() {
        if (this.m_showCheckbox) {
            for (int i = 0; i < this.getRowCount(); ++i) {
                this.m_table.setValueAt(new Boolean(true), i, 0);
            }
        }
    }

    public void toggleSelectedRow() {
        this.m_table.setValueAt(new Boolean((Boolean) this.m_table.getValueAt(this.m_table.getSelectedRow(), 0) == false), this.m_table.getSelectedRow(), 0);
    }

    public void buildRequestReplyColumnHeaders() {
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_msgs.clear();
        if (this.m_replymsgs == null) {
            this.m_replymsgs = new Hashtable();
        }
        this.m_replymsgs.clear();
        this.m_table.setAutoResizeMode(0);
        this.m_table.setDefaultRenderer(Object.class, this.m_renderer);
        this.m_table.setDefaultRenderer(String.class, this.m_renderer);
        this.m_table.setDefaultRenderer(Date.class, this.m_renderer);
        this.m_table.setDefaultRenderer(Boolean.class, this.m_checkRenderer);
        Object[] arrobject = new String[]{"RequestMessageID", "Timestamp", "ResponseTime(ms)", "Destination", "CorrelationID", "ReplyTo", "ReqMsgSize", "ReplyMsgSize"};
        this.setColumnIdentifiers(arrobject);
        if (this.m_showCheckbox) {
            this.m_table.getColumn("").setPreferredWidth(30);
        }
        this.m_table.getColumn("RequestMessageID").setPreferredWidth(220);
        this.m_table.getColumn("Destination").setPreferredWidth(150);
        this.m_table.getColumn("ReplyTo").setPreferredWidth(150);
        this.m_table.getColumn("ReplyMsgSize").setPreferredWidth(100);
        this.m_table.getColumn("ResponseTime(ms)").setPreferredWidth(120);
        this.m_table.getColumn("CorrelationID").setPreferredWidth(100);
        this.m_table.getColumn("Timestamp").setPreferredWidth(200);
    }

    public void buildMonitorColumnHeaders(boolean bl) {
        this.m_monitor = true;
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_msgs.clear();
        this.m_table.setAutoResizeMode(0);
        this.m_table.setDefaultRenderer(this.m_obj.getClass(), this.m_renderer);
        this.m_table.setDefaultRenderer(Date.class, this.m_renderer);
        Object[] arrobject = bl ? new String[]{"MessageID", "Timestamp", "EventAction", "EventReason", "ConnHostname", "ConnUsername", "TargetDestination"} : new String[]{"MessageID", "Timestamp", "EventAction", "EventReason", "ConnHostname", "ConnUsername"};
        this.setColumnIdentifiers(arrobject);
        this.m_table.getColumn("MessageID").setPreferredWidth(100);
        if (bl) {
            this.m_table.getColumn("TargetDestination").setPreferredWidth(200);
        }
        this.m_table.getColumn("ConnHostname").setPreferredWidth(120);
        this.m_table.getColumn("ConnUsername").setPreferredWidth(120);
        this.m_table.getColumn("EventAction").setPreferredWidth(90);
        this.m_table.getColumn("EventReason").setPreferredWidth(90);
        this.m_table.getColumn("Timestamp").setPreferredWidth(180);
    }

    public void updateRequestMessage(Message message, Message message2) {
        try {
            String string = message.getJMSMessageID();
            for (int i = 0; i < this.m_table.getRowCount(); ++i) {
                String string2 = (String) this.m_table.getValueAt(i, 0);
                String string3 = StringUtilities.getHumanReadableSize(Tibjms.getMessageSize((Message) message2));
                if (!string2.equals(string)) continue;
                long l = message2.getJMSTimestamp() - message.getJMSTimestamp();
                this.m_table.setValueAt(String.valueOf(l), i, 2);
                this.m_table.setValueAt(string3, i, 7);
                String string4 = message.getJMSCorrelationID();
                if (string4 != null && string4.length() > 0) {
                    this.m_replymsgs.put(string4, message2);
                    continue;
                }
                String string5 = message.getJMSReplyTo().toString();
                if (string5 != null && string5.length() > 0) {
                    this.m_replymsgs.put(string5, message2);
                    continue;
                }
                System.err.println("GemsReqReplyMonitor.updateRequestMessage: Could not update request " + message.getJMSMessageID());
            }
        } catch (JMSException var3_4) {
            System.err.println("JMSException: " + var3_4.getMessage());
            return;
        }
    }

    public void timeoutRequestMessage(Message message) {
        try {
            String string = message.getJMSMessageID();
            for (int i = 0; i < this.m_table.getRowCount(); ++i) {
                String string2 = (String) this.m_table.getValueAt(i, 0);
                if (!string2.equals(string)) continue;
                this.m_table.setValueAt("no reply", i, 2);
            }
        } catch (JMSException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void addRequestMessage(Message message, Message message2, boolean bl) {
        Date date = new Date();
        try {
            if (message != null) {
                String string;
                if (message.getJMSMessageID() == null) {
                    message.setJMSMessageID("tmpId" + String.valueOf(++s_msgId));
                }
                this.m_msgs.put(message.getJMSMessageID(), message);
                if (message2 != null) {
                    string = message.getJMSCorrelationID();
                    if (string != null && string.length() > 0) {
                        this.m_replymsgs.put(string, message2);
                    } else {
                        String string2 = message.getJMSReplyTo().toString();
                        if (string2 != null && string2.length() > 0) {
                            this.m_replymsgs.put(string2, message2);
                        }
                    }
                }
                string = message.getJMSDeliveryMode() == 2 ? "PERSISTENT" : (message.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "RELIABLE");
                long l = -99;
                String string3 = "";
                if (message2 != null) {
                    l = message2.getJMSTimestamp() - message.getJMSTimestamp();
                    string3 = StringUtilities.getHumanReadableSize(Tibjms.getMessageSize((Message) message2));
                }
                date.setTime(message.getJMSTimestamp());
                if (message.getJMSMessageID() == null) {
                    message.setJMSMessageID("tmpId" + String.valueOf(++s_msgId));
                }
                Object[] arrobject = new Object[8];
                arrobject[0] = message.getJMSMessageID();
                arrobject[1] = date;
                arrobject[2] = l == -99 ? "pending..." : String.valueOf(l);
                arrobject[3] = message.getJMSDestination().toString();
                arrobject[4] = message.getJMSCorrelationID();
                arrobject[5] = message.getJMSReplyTo() != null ? message.getJMSReplyTo().toString() : "";
                arrobject[6] = StringUtilities.getHumanReadableSize(Tibjms.getMessageSize((Message) message));
                arrobject[7] = string3;
                Object[] arrobject2 = arrobject;
                if (bl) {
                    this.addRow(arrobject2);
                } else {
                    this.insertRow(0, arrobject2);
                }
            } else {
                System.err.println("Empty message!");
            }
        } catch (JMSException var5_6) {
            System.err.println("JMSException: " + var5_6.getMessage());
            return;
        }
    }

    public void addMonitorMessage(Message message, boolean bl, boolean bl2) {
        Date date = new Date();
        try {
            if (message != null) {
                if (message.getJMSMessageID() == null) {
                    message.setJMSMessageID("tmpId" + String.valueOf(++s_msgId));
                }
                this.m_msgs.put(message.getJMSMessageID(), message);
                date.setTime(message.getJMSTimestamp());
                Object[] arrobject = bl2 ? new Object[]{message.getJMSMessageID(), date, message.getStringProperty("event_action"), message.getStringProperty("event_reason"), message.getStringProperty("conn_hostname"), message.getStringProperty("conn_username"), message.getStringProperty("target_dest_name")} : new Object[]{message.getJMSMessageID(), date, message.getStringProperty("event_action"), message.getStringProperty("event_reason"), message.getStringProperty("conn_hostname"), message.getStringProperty("conn_username")};
                if (bl) {
                    this.addRow(arrobject);
                } else {
                    this.insertRow(0, arrobject);
                }
            } else {
                System.err.println("Empty message!");
            }
        } catch (JMSException var5_6) {
            System.err.println("JMSException: " + var5_6.getMessage());
            return;
        }
    }

    public void populateEventMessageInfo(Vector vector) {
        if (vector == null) {
            this.setRowCount(0);
            this.setColumnCount(0);
            this.m_msgs.clear();
            return;
        }
        this.buildEventColumnHeaders();
        for (int i = 0; i < vector.size(); ++i) {
            Message message = (Message) vector.elementAt(i);
            this.addEventMessage(message, false);
        }
    }

    public void buildEventColumnHeaders() {
        this.m_showEvents = true;
        this.setRowCount(0);
        this.m_msgs.clear();
        this.m_table.setAutoResizeMode(0);
        this.m_table.setDefaultRenderer(this.m_obj.getClass(), this.m_renderer);
        this.m_table.setDefaultRenderer(Date.class, this.m_renderer);
        Object[] arrobject = new String[]{"MessageID", "Timestamp", "EventType", "EventReason", "ConnHostname", "ConnUsername", "TargetDestination"};
        if (this.getColumnCount() != 7 || !this.getColumnName(2).equals("EventType")) {
            this.setColumnCount(0);
            this.setColumnIdentifiers(arrobject);
            this.m_table.getColumn("MessageID").setPreferredWidth(100);
            this.m_table.getColumn("TargetDestination").setPreferredWidth(200);
            this.m_table.getColumn("ConnHostname").setPreferredWidth(120);
            this.m_table.getColumn("ConnUsername").setPreferredWidth(120);
            this.m_table.getColumn("EventType").setPreferredWidth(100);
            this.m_table.getColumn("EventReason").setPreferredWidth(120);
            this.m_table.getColumn("Timestamp").setPreferredWidth(180);
        }
    }

    public void addEventMessage(Message message, boolean bl) {
        Date date = new Date();
        try {
            if (message != null) {
                if (message.getJMSMessageID() == null) {
                    message.setJMSMessageID("tmpId" + String.valueOf(++s_msgId));
                }
                this.m_msgs.put(message.getJMSMessageID(), message);
                String string = message.getStringProperty("event_class");
                if (string == null) {
                    message.getStringProperty("event_action");
                }
                date.setTime(message.getJMSTimestamp());
                Object[] arrobject = new Object[]{message.getJMSMessageID(), date, string, message.getStringProperty("event_reason"), message.getStringProperty("conn_hostname"), message.getStringProperty("conn_username"), message.getStringProperty("target_dest_name")};
                if (bl) {
                    this.addRow(arrobject);
                } else {
                    this.insertRow(0, arrobject);
                }
            } else {
                System.err.println("Empty message!");
            }
        } catch (JMSException var4_5) {
            System.err.println("JMSException: " + var4_5.getMessage());
            return;
        }
    }

    public void populateMessageInfo(Vector vector) {
        if (vector == null) {
            return;
        }
        boolean bl = Gems.getGems().getViewOldMessagesFirst();
        this.buildColumnHeaders();
        for (int i = 0; i < vector.size(); ++i) {
            Message message = (Message) vector.elementAt(i);
            this.addMessage(message, bl);
        }
    }

    public void buildColumnHeaders() {
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_msgs.clear();
        this.m_table.setAutoResizeMode(0);
        this.m_table.setDefaultRenderer(Object.class, this.m_renderer);
        this.m_table.setDefaultRenderer(String.class, this.m_renderer);
        this.m_table.setDefaultRenderer(Date.class, this.m_renderer);
        this.m_table.setDefaultRenderer(Boolean.class, this.m_checkRenderer);
        Object[] arrobject = this.m_showCheckbox ? new String[]{"Sel", "MessageID", "Timestamp", "Type", "MsgSize", "Destination", "CorrelationID", "DeliveryMode"} : new String[]{"MessageID", "Timestamp", "Type", "MsgSize", "Destination", "CorrelationID", "DeliveryMode"};
        this.setColumnIdentifiers(arrobject);
        if (this.m_showCheckbox) {
            this.m_table.getColumn("Sel").setPreferredWidth(30);
        }
        this.m_table.getColumn("MessageID").setPreferredWidth(220);
        this.m_table.getColumn("Destination").setPreferredWidth(150);
        this.m_table.getColumn("DeliveryMode").setPreferredWidth(100);
        this.m_table.getColumn("CorrelationID").setPreferredWidth(100);
        this.m_table.getColumn("Timestamp").setPreferredWidth(200);
    }

    public void addMessage(Message message, boolean bl) {
        Date date = new Date();
        try {
            if (message != null) {
                if (message.getJMSMessageID() == null) {
                    message.setJMSMessageID("tmpId" + String.valueOf(++s_msgId));
                }
                this.m_msgs.put(message.getJMSMessageID(), message);
                String string = message.getJMSDeliveryMode() == 2 ? "PERSISTENT" : (message.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "RELIABLE");
                String string2 = message.getJMSType();
                if (string2 == null || string2.length() == 0) {
                    string2 = message instanceof TextMessage ? "[Text]" : (message instanceof MapMessage ? "[Map]" : (message instanceof BytesMessage ? "[Bytes]" : (message instanceof StreamMessage ? "[Stream]" : (message instanceof ObjectMessage ? "[Object]" : ""))));
                }
                date.setTime(message.getJMSTimestamp());
                Object[] arrobject = this.m_showCheckbox ? new Object[]{new Boolean(false), message.getJMSMessageID(), date, string2, StringUtilities.getHumanReadableSize(Tibjms.getMessageSize((Message) message)), message.getJMSDestination().toString(), message.getJMSCorrelationID(), string} : new Object[]{message.getJMSMessageID(), date, string2, StringUtilities.getHumanReadableSize(Tibjms.getMessageSize((Message) message)), message.getJMSDestination().toString(), message.getJMSCorrelationID(), string};
                if (bl) {
                    this.addRow(arrobject);
                } else {
                    this.insertRow(0, arrobject);
                }
            } else {
                System.err.println("Empty message!");
            }
        } catch (JMSException var4_5) {
            System.err.println("JMSException: " + var4_5.getMessage());
            return;
        }
    }

    public void populateMessageInfoOld(Vector vector) {
        boolean bl = Gems.getGems().getViewOldMessagesFirst();
        this.buildColumnHeaders();
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_msgs.clear();
        if (vector == null) {
            return;
        }
        this.m_table.setAutoResizeMode(0);
        Object[] arrobject = new String[]{"MessageID", "Timestamp", "DeliveryMode", "Destination", "CorrelationID", "Expiration", "Priority", "ReplyTo", "Type"};
        this.setColumnIdentifiers(arrobject);
        this.m_table.getColumn("MessageID").setPreferredWidth(200);
        this.m_table.getColumn("Destination").setPreferredWidth(100);
        this.m_table.getColumn("DeliveryMode").setPreferredWidth(100);
        this.m_table.getColumn("CorrelationID").setPreferredWidth(100);
        this.m_table.getColumn("Expiration").setPreferredWidth(100);
        this.m_table.getColumn("Priority").setPreferredWidth(100);
        this.m_table.getColumn("Timestamp").setPreferredWidth(200);
        try {
            Date date = new Date();
            for (int i = 0; i < vector.size(); ++i) {
                Message message = (Message) vector.elementAt(i);
                if (message != null) {
                    if (message.getJMSMessageID() == null) {
                        message.setJMSMessageID("tmpId" + String.valueOf(++s_msgId));
                    }
                    this.m_msgs.put(message.getJMSMessageID(), message);
                    String string = message.getJMSDeliveryMode() == 2 ? "PERSISTENT" : (message.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "RELIABLE");
                    date.setTime(message.getJMSTimestamp());
                    Object[] arrobject2 = new String[9];
                    arrobject2[0] = message.getJMSMessageID();
                    arrobject2[1] = date.toString();
                    arrobject2[2] = string;
                    arrobject2[3] = message.getJMSDestination().toString();
                    arrobject2[4] = message.getJMSCorrelationID();
                    arrobject2[5] = String.valueOf(message.getJMSExpiration());
                    arrobject2[6] = String.valueOf(message.getJMSPriority());
                    arrobject2[7] = message.getJMSReplyTo() != null ? message.getJMSReplyTo().toString() : new String();
                    arrobject2[8] = message.getJMSType();
                    Object[] arrobject3 = arrobject2;
                    if (bl) {
                        this.addRow(arrobject3);
                        continue;
                    }
                    this.insertRow(0, arrobject3);
                    continue;
                }
                System.err.println("Empty message!");
            }
        } catch (JMSException var4_7) {
            System.err.println("JMSException: " + var4_7.getMessage());
            return;
        }
    }

    public void populateMessageInfo(QueueBrowser queueBrowser) {
        System.err.println("NOT USED");
        boolean bl = Gems.getGems().getViewOldMessagesFirst();
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_msgs.clear();
        if (queueBrowser == null) {
            return;
        }
        this.m_table.setAutoResizeMode(0);
        Object[] arrobject = new String[]{"ID", "Timestamp", "DeliveryMode", "CorrelationID", "Expiration", "Priority", "ReplyTo", "Type"};
        this.setColumnIdentifiers(arrobject);
        this.m_table.getColumn("ID").setPreferredWidth(200);
        this.m_table.getColumn("DeliveryMode").setPreferredWidth(100);
        this.m_table.getColumn("CorrelationID").setPreferredWidth(100);
        this.m_table.getColumn("Expiration").setPreferredWidth(100);
        this.m_table.getColumn("Priority").setPreferredWidth(100);
        this.m_table.getColumn("Timestamp").setPreferredWidth(200);
        try {
            Enumeration enumeration = queueBrowser.getEnumeration();
            Date date = new Date();
            int n = 0;
            int n2 = Gems.getGems().getMaxMessageView();
            while (enumeration.hasMoreElements() && n < n2) {
                Message message = (Message) enumeration.nextElement();
                if (message != null) {
                    this.m_msgs.put(message.getJMSMessageID(), message);
                    String string = message.getJMSDeliveryMode() == 2 ? "PERSISTENT" : (message.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "RELIABLE");
                    date.setTime(message.getJMSTimestamp());
                    Object[] arrobject2 = new String[8];
                    arrobject2[0] = message.getJMSMessageID();
                    arrobject2[1] = date.toString();
                    arrobject2[2] = string;
                    arrobject2[3] = message.getJMSCorrelationID();
                    arrobject2[4] = String.valueOf(message.getJMSExpiration());
                    arrobject2[5] = String.valueOf(message.getJMSPriority());
                    arrobject2[6] = message.getJMSReplyTo() != null ? message.getJMSReplyTo().toString() : new String();
                    arrobject2[7] = message.getJMSType();
                    Object[] arrobject3 = arrobject2;
                    if (bl) {
                        this.addRow(arrobject3);
                    } else {
                        this.insertRow(0, arrobject3);
                    }
                    ++n;
                    continue;
                }
                System.err.println("Empty message!");
            }
            if (enumeration.hasMoreElements()) {
                System.err.println("Warning: Display Limited to" + n2 + " messages");
            }
        } catch (JMSException var4_5) {
            System.err.println("JMSException: " + var4_5.getMessage());
            return;
        }
    }

    void dumpMsgsToFile(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        PrintStream printStream = new PrintStream(fileOutputStream);
        for (int i = 0; i < this.m_table.getRowCount(); ++i) {
            String string = this.m_showCheckbox ? (String) this.m_table.getValueAt(i, 1) : (String) this.m_table.getValueAt(i, 0);
            this.writeMessage((Message) this.m_msgs.get(string), printStream);
            if (this.m_replymsgs == null) continue;
            this.writeMessage(this.getReplyMessageAt(i), printStream);
        }
        printStream.close();
    }

    void writeMessage(Message message, PrintStream printStream) {
        if (message == null) {
            return;
        }
        try {
            printStream.println("$MsgStart$");
            printStream.println("$MsgHeader$");
            printStream.println("JMSMessageID=" + message.getJMSMessageID());
            Date date = new Date();
            date.setTime(message.getJMSTimestamp());
            printStream.println("JMSTimestamp=" + this.dateFormatMillis.format(date).toString());
            printStream.println("JMSDestination=" + (Object) message.getJMSDestination());
            String string = message.getJMSDeliveryMode() == 2 ? "PERSISTENT" : (message.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "RELIABLE");
            printStream.println("JMSDeliveryMode=" + string);
            if (message.getJMSCorrelationID() != null) {
                printStream.println("JMSCorrelationID=" + message.getJMSCorrelationID());
            }
            if (message.getJMSType() != null) {
                printStream.println("JMSType=" + message.getJMSType());
            }
            if (message.getJMSReplyTo() != null) {
                printStream.println("JMSReplyTo=" + (Object) message.getJMSReplyTo());
            }
            if (message.getJMSExpiration() > 0) {
                date.setTime(message.getJMSExpiration());
                printStream.println("JMSExpiration=" + date.toString());
            }
            printStream.println("$MsgProperties$");
            Enumeration enumeration = message.getPropertyNames();
            while (enumeration.hasMoreElements()) {
                String string2 = (String) enumeration.nextElement();
                printStream.println(string2 + "=" + GemsMsgPropTableModel.toTypedString(null, message.getObjectProperty(string2)));
            }
            if (message instanceof TextMessage) {
                printStream.println("$MsgTextBody$");
                TextMessage textMessage = (TextMessage) message;
                printStream.println(textMessage.getText());
            } else if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                printStream.println("$MsgMapBody$");
                Enumeration enumeration2 = mapMessage.getMapNames();
                while (enumeration2.hasMoreElements()) {
                    String string3 = (String) enumeration2.nextElement();
                    printStream.println(string3 + "=" + GemsMsgPropTableModel.toTypedString(null, mapMessage.getObject(string3)));
                }
            } else if (message instanceof BytesMessage) {
                printStream.println("$MsgBytesBody$");
                BytesMessage bytesMessage = (BytesMessage) message;
                bytesMessage.reset();
                long l = bytesMessage.getBodyLength();
                byte[] arrby = new byte[(int) l];
                bytesMessage.readBytes(arrby, (int) l);
                printStream.println(StringUtilities.dumpBytes(arrby));
            } else if (message instanceof StreamMessage) {
                printStream.println("$MsgStreamBody$");
                StreamMessage streamMessage = (StreamMessage) message;
                printStream.println(streamMessage.toString());
            } else if (message instanceof ObjectMessage) {
                printStream.println("$MsgObjectBody$");
                ObjectMessage objectMessage = (ObjectMessage) message;
                printStream.println(objectMessage.toString());
            }
            printStream.println("$MsgEnd$");
            printStream.println("");
        } catch (JMSException var3_4) {
            System.err.println("JMSException: " + var3_4.getMessage());
            return;
        }
    }

    public Message getReplyMessageAt(int n) {
        if (this.m_replymsgs == null) {
            return null;
        }
        Message message = null;
        String string = (String) this.m_table.getValueAt(n, 4);
        if (string != null) {
            message = (Message) this.m_replymsgs.get(string);
        }
        if (message == null && (string = (String) this.m_table.getValueAt(n, 5)) != null) {
            message = (Message) this.m_replymsgs.get(string);
        }
        return message;
    }

    class MyRenderer
            extends DefaultTableCellRenderer {
        public MyRenderer() {
            if (GemsMessageTableModel.this.m_showCheckbox) {
                this.setToolTipText("Double-click to display message, select checkbox for messages to copy or destroy");
            } else {
                this.setToolTipText("Double-click to display message");
            }
        }

        public void setValue(Object object) {
            if (object instanceof Date) {
                this.setText(object == null ? "" : GemsMessageTableModel.this.dateFormatMillis.format(object).toString());
            } else {
                super.setValue(object);
            }
        }
    }

}

