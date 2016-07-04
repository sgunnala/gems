/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.Tibjms
 *  com.tibco.tibjms.TibjmsConnectionFactory
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 *  com.tibco.tibjms.admin.TopicInfo
 *  javax.jms.BytesMessage
 *  javax.jms.Connection
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.MessageProducer
 *  javax.jms.ObjectMessage
 *  javax.jms.Queue
 *  javax.jms.Session
 *  javax.jms.StreamMessage
 *  javax.jms.TextMessage
 *  javax.jms.Topic
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.swing.*;

import com.tibco.tibjms.Tibjms;
import com.tibco.tibjms.TibjmsConnectionFactory;
import com.tibco.tibjms.admin.TibjmsAdmin;
import com.tibco.tibjms.admin.TibjmsAdminException;

public class GemsMessageFrame extends JDialog {
    GemsMsgPropTableModel m_tableModel = null;
    GemsMsgPropTableModel m_mapTableModel = null;
    JMenuBar m_menuBar = new JMenuBar();
    JMenu m_mapmenu = null;
    JFrame m_frame;
    JTable m_table;
    JPanel m_panel;
    JPanel m_headPanel;
    JPanel m_textPanel;
    JPanel m_mapPanel = null;
    JPanel m_bytesPanel = null;
    JTextArea m_bytesTextArea;
    GemsMessageText m_text;
    JButton m_sendButt;
    boolean m_send;
    JTabbedPane m_tabPane;
    String m_destination;
    String m_destprop;
    boolean m_isQueue;
    Message m_msg = null;
    TibjmsAdmin m_admin = null;
    Connection m_connection = null;
    Session m_session = null;
    GemsConnectionNode m_cn = null;
    SimpleDateFormat dateFormatMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss SSS zzz yyyy");
    boolean m_isMonitorMsg;
    boolean m_isEditor = false;
    JDialog m_this;

    public GemsMessageFrame(GemsConnectionNode gemsConnectionNode, boolean bl, String string, boolean bl2, JFrame jFrame, boolean bl3) {
        super(jFrame, Gems.getGems().getTitlePrefix() + "Message: ", false);
        this.m_isMonitorMsg = bl3;
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_send = bl;
        this.m_isQueue = bl2;
        this.m_destination = string;
        this.m_this = this;
        this.init();
        this.setPreferredSize(new Dimension(900, 700));
        this.setLocationRelativeTo(jFrame);
        this.setLocation(this.getX(), this.getY() - 175);
        this.pack();
        this.show();
    }

    public void init() {
        this.setDefaultCloseOperation(2);
        if (this.m_cn != null) {
            this.m_admin = this.m_cn.m_adminConn;
        }
        if (this.m_send) {
            if (this.m_isQueue) {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Send TextMessage To Queue");
            } else {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Publish TextMessage To Topic");
            }
            this.m_destprop = this.m_destination;
        } else {
            this.m_destprop = this.m_isQueue ? new String("Queue[") : new String("Topic[");
            this.m_destprop = this.m_destprop + this.m_destination + ']';
        }
        JMenuBar jMenuBar = this.constructMenuBar(this.m_send);
        this.setJMenuBar(jMenuBar);
        this.m_panel = new JPanel(true);
        this.m_panel.setLayout(new BorderLayout());
        this.m_tabPane = new JTabbedPane();
        this.m_panel.add((Component) this.m_tabPane, "Center");
        this.getContentPane().add("Center", this.m_panel);
        this.m_headPanel = new JPanel(new BorderLayout());
        this.m_tableModel = new GemsMsgPropTableModel(this.m_send);
        this.m_table = new JTable(this.m_tableModel);
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_tableModel.m_table = this.m_table;
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        this.m_headPanel.add((Component) jScrollPane, "Center");
        this.m_tabPane.addTab("Header", this.m_headPanel);
        this.m_textPanel = new JPanel(new BorderLayout());
        this.m_text = new GemsMessageText();
        this.m_text.setLineWrap(true);
        if (!this.m_send) {
            this.m_text.setEditable(false);
        }
        JScrollPane jScrollPane2 = new JScrollPane(this.m_text);
        this.m_textPanel.add((Component) jScrollPane2, "Center");
        this.m_tabPane.addTab("Text Body", this.m_textPanel);
        if (this.m_send) {
            this.m_sendButt = this.m_isEditor ? new JButton("OK") : (this.m_isQueue ? new JButton("Send Message") : new JButton("Publish Message"));
            this.m_sendButt.addActionListener(new SendPressed());
            this.m_panel.add((Component) this.m_sendButt, "South");
            if (this.m_isEditor) {
                this.m_tableModel.emptyEditorPropertyInfo(this.m_destprop);
            } else {
                this.m_tableModel.emptyPropertyInfo(this.m_destprop);
            }
            this.m_text.setText("<Message text>");
        }
    }

    private JMenuBar constructMenuBar(boolean bl) {
        JMenuItem jMenuItem;
        JMenu jMenu = new JMenu("File");
        jMenu.setMnemonic(70);
        this.m_menuBar.add(jMenu);
        if (bl) {
            JMenu jMenu2;
            jMenuItem = jMenu.add(new JMenuItem("Load..."));
            jMenuItem.addActionListener(new LoadMessage());
            JMenu jMenu3 = new JMenu("Property");
            jMenu3.setMnemonic(80);
            this.m_menuBar.add(jMenu3);
            if (!this.m_isEditor) {
                jMenu2 = new JMenu("Add JMS Property");
                jMenu3.add(jMenu2);
                jMenuItem = jMenu2.add(new JMenuItem("JMSReplyTo..."));
                jMenuItem.addActionListener(new AddJMSReplyToProperty());
            }
            jMenu2 = new JMenu("Add Tibco Property");
            jMenu3.add(jMenu2);
            jMenuItem = jMenu2.add(new JMenuItem("JMS_TIBCO_COMPRESS"));
            jMenuItem.addActionListener(new AddCompressProperty());
            jMenuItem = jMenu2.add(new JMenuItem("JMS_TIBCO_PRESERVE_UNDELIVERED"));
            jMenuItem.addActionListener(new AddPreserveProperty());
            jMenu2 = new JMenu("Add Custom Property");
            jMenu3.add(jMenu2);
            jMenuItem = jMenu2.add(new JMenuItem("String"));
            jMenuItem.addActionListener(new AddStringProperty());
            jMenuItem = jMenu2.add(new JMenuItem("Boolean"));
            jMenuItem.addActionListener(new AddBooleanProperty());
            jMenuItem = jMenu2.add(new JMenuItem("Integer"));
            jMenuItem.addActionListener(new AddIntegerProperty());
            jMenuItem = jMenu2.add(new JMenuItem("Byte"));
            jMenuItem.addActionListener(new AddByteProperty());
            jMenuItem = jMenu2.add(new JMenuItem("Short"));
            jMenuItem.addActionListener(new AddShortProperty());
            jMenuItem = jMenu2.add(new JMenuItem("Long"));
            jMenuItem.addActionListener(new AddLongProperty());
            jMenuItem = jMenu2.add(new JMenuItem("Float"));
            jMenuItem.addActionListener(new AddFloatProperty());
            jMenuItem = jMenu2.add(new JMenuItem("Double"));
            jMenuItem.addActionListener(new AddDoubleProperty());
        } else {
            jMenuItem = jMenu.add(new JMenuItem("Save..."));
            jMenuItem.addActionListener(new SaveMessage());
        }
        jMenuItem = jMenu.add(new JMenuItem("Exit"));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsMessageFrame.this.dispose();
            }
        });
        if (this.m_isMonitorMsg) {
            jMenu = new JMenu("Message");
            jMenu.setMnemonic(77);
            this.m_menuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("View Original Message..."));
            jMenuItem.addActionListener(new ViewOrigMsg(this));
        }
        return this.m_menuBar;
    }

    public void dispose() {
        this.close();
        super.dispose();
    }

    public void close() {
        try {
            if (this.m_session != null) {
                this.m_session.close();
                this.m_session = null;
            }
            if (this.m_connection != null) {
                this.m_connection.close();
                this.m_connection = null;
            }
        } catch (JMSException var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
        }
    }

    public GemsMessageFrame(GemsConnectionNode gemsConnectionNode, boolean bl, String string, boolean bl2, JFrame jFrame, boolean bl3, boolean bl4) {
        super(jFrame, Gems.getGems().getTitlePrefix() + "Message: ", false);
        this.m_isMonitorMsg = bl3;
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_send = bl;
        this.m_isQueue = bl2;
        this.m_destination = string;
        this.m_this = this;
        this.init();
        if (bl4) {
            this.createMapUI();
            this.m_mapTableModel.populateEmptyPropertyInfo(0);
        }
        this.setLocationRelativeTo(jFrame);
        this.setLocation(this.getX(), this.getY() - 165);
        this.pack();
        this.show();
    }

    public void createMapUI() {
        if (this.m_mapPanel == null) {
            this.m_mapPanel = new JPanel(new BorderLayout());
            this.m_mapTableModel = new GemsMsgPropTableModel(this.m_send, true);
            JTable jTable = new JTable(this.m_mapTableModel);
            jTable.getTableHeader().setReorderingAllowed(false);
            this.m_mapTableModel.m_table = jTable;
            JScrollPane jScrollPane = new JScrollPane(jTable);
            this.m_mapPanel.add((Component) jScrollPane, "Center");
            this.m_tabPane.addTab("Map Body", this.m_mapPanel);
            this.m_tabPane.remove(this.m_textPanel);
        }
        this.m_mapTableModel.setRowCount(0);
        if (this.m_send) {
            if (this.m_isQueue) {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Send MapMessage To Queue");
            } else {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Publish MapMessage To Topic");
            }
            if (this.m_mapmenu == null) {
                this.m_mapmenu = new JMenu("Map");
                this.m_mapmenu.setMnemonic(77);
                this.m_menuBar.add(this.m_mapmenu);
                JMenu jMenu = new JMenu("Add Field");
                this.m_mapmenu.add(jMenu);
                JMenuItem jMenuItem = jMenu.add(new JMenuItem("String"));
                jMenuItem.addActionListener(new AddStringField());
                JMenuItem jMenuItem2 = jMenu.add(new JMenuItem("Boolean"));
                jMenuItem2.addActionListener(new AddBooleanField());
                JMenuItem jMenuItem3 = jMenu.add(new JMenuItem("Integer"));
                jMenuItem3.addActionListener(new AddIntegerField());
                JMenuItem jMenuItem4 = jMenu.add(new JMenuItem("Byte"));
                jMenuItem4.addActionListener(new AddByteField());
                JMenuItem jMenuItem5 = jMenu.add(new JMenuItem("Short"));
                jMenuItem5.addActionListener(new AddShortField());
                JMenuItem jMenuItem6 = jMenu.add(new JMenuItem("Long"));
                jMenuItem6.addActionListener(new AddLongField());
                JMenuItem jMenuItem7 = jMenu.add(new JMenuItem("Float"));
                jMenuItem7.addActionListener(new AddFloatField());
                JMenuItem jMenuItem8 = jMenu.add(new JMenuItem("Double"));
                jMenuItem8.addActionListener(new AddDoubleField());
            }
        }
    }

    public GemsMessageFrame(GemsConnectionNode gemsConnectionNode, boolean bl, String string, boolean bl2, JFrame jFrame, boolean bl3, boolean bl4, boolean bl5) {
        super(jFrame, Gems.getGems().getTitlePrefix() + "Message: ", true);
        this.m_isMonitorMsg = bl3;
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_send = bl;
        this.m_isQueue = bl2;
        this.m_destination = string;
        this.m_isEditor = bl5;
        this.m_this = this;
        this.init();
        if (bl4) {
            this.createMapUI();
            this.m_mapTableModel.populateEmptyPropertyInfo(0);
        }
        this.setLocationRelativeTo(jFrame);
        this.setLocation(this.getX(), this.getY() - 165);
        this.pack();
    }

    public void createBytesFieldUI(String string, boolean bl) {
        if (this.m_bytesPanel == null) {
            this.m_bytesPanel = new JPanel(new BorderLayout());
            this.m_bytesTextArea = new JTextArea();
            this.m_bytesTextArea.setLineWrap(true);
            this.m_bytesTextArea.setEditable(false);
            JScrollPane jScrollPane = new JScrollPane(this.m_bytesTextArea);
            this.m_bytesPanel.add((Component) jScrollPane, "Center");
            this.m_tabPane.addTab(string + " Field", this.m_bytesPanel);
        } else {
            this.m_tabPane.setTitleAt(2, string + " Field");
        }
        if (this.m_msg != null && this.m_msg instanceof MapMessage) {
            try {
                if (bl) {
                    this.m_bytesTextArea.setText(new String(((MapMessage) this.m_msg).getBytes(string)));
                } else {
                    this.m_bytesTextArea.setText(StringUtilities.dumpBytes(((MapMessage) this.m_msg).getBytes(string)));
                }
                this.m_tabPane.setSelectedIndex(2);
            } catch (Exception var3_4) {
                System.err.println("Exception: " + var3_4.getMessage());
                return;
            }
        }
    }

    public void createTextUI() {
        if (this.m_mapPanel != null) {
            this.m_tabPane.remove(this.m_mapPanel);
            this.m_mapPanel = null;
            this.m_mapTableModel = null;
            this.m_tabPane.addTab("Text Body", this.m_textPanel);
        }

        if (this.m_send) {
            if (this.m_isQueue) {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Send TextMessage To Queue");
            } else {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Publish TextMessage To Topic");
            }
        }
    }

    public void openSubMapMsg(String string) {
        if (this.m_msg != null && this.m_msg instanceof MapMessage) {
            try {
                MapMessage mapMessage = (MapMessage) this.m_msg;
                Object object = mapMessage.getObject(string);
                if (object != null && object instanceof MapMessage) {
                    GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(this.m_cn, false, this.m_destination, this.m_isQueue, this.m_frame, false);
                    gemsMessageFrame.populate((Message) ((MapMessage) object));
                } else if (mapMessage.itemExists("message_bytes")) {
                    Message message = Tibjms.createFromBytes((byte[]) mapMessage.getBytes("message_bytes"));
                    GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(this.m_cn, false, this.m_destination, this.m_isQueue, this.m_frame, false);
                    gemsMessageFrame.populate(message);
                }
            } catch (Exception var2_3) {
                Gems.debug(var2_3.getMessage());
                return;
            }
        }
    }

    public void populate(Message message) {
        this.m_msg = message;
        try {
            String string = message.getClass().getName();
            int n = string.lastIndexOf("Tibjms");
            String string2 = new String();
            if (message.getJMSMessageID() != null) {
                string2 = message.getJMSMessageID();
            }

            if (n > 0) {
                this.setTitle(Gems.getGems().getTitlePrefix() + string.substring(n + 6) + " " + string2);
            }

            this.m_tableModel.populatePropertyInfo(message);
            this.m_text.setMessageText(message);
            if (message instanceof TextMessage) {
                if (this.m_tabPane.getTabCount() > 1) {
                    this.m_tabPane.setTitleAt(1, "Text Body");
                }
            } else if (message instanceof MapMessage) {
                this.createMapUI();
                MapMessage mapMessage = (MapMessage) message;
                this.m_mapTableModel.populateMapMsg(mapMessage);
            } else if (message instanceof BytesMessage) {
                if (this.m_tabPane.getTabCount() > 1) {
                    this.m_tabPane.setTitleAt(1, "Bytes Body (" + ((BytesMessage) message).getBodyLength() + " bytes)");
                }
            } else if (message instanceof StreamMessage) {
                if (this.m_tabPane.getTabCount() > 1) {
                    this.m_tabPane.setTitleAt(1, "Body");
                }
            } else if (message instanceof ObjectMessage) {
                if (this.m_tabPane.getTabCount() > 1) {
                    this.m_tabPane.setTitleAt(1, "Body");
                }
            } else {
                this.createSimpleUI();
            }
        } catch (Exception var2_3) {
            System.err.println("Exception: " + var2_3.getMessage());
            return;
        }
    }

    public void createSimpleUI() {
        if (this.m_tabPane.getTabCount() > 1) {
            this.m_tabPane.remove(this.m_textPanel);
        }
        if (this.m_send) {
            if (this.m_isQueue) {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Send SimpleMessage To Queue");
            } else {
                this.setTitle(Gems.getGems().getTitlePrefix() + "Publish SimpleMessage To Topic");
            }
        }
    }

    public GemsMsgPropTableModel getModel() {
        return this.m_tableModel;
    }

    Message createMessage(Session session) throws JMSException {
        Object object;
        MapMessage mapMessage = null;
        mapMessage = this.m_mapPanel != null ? session.createMapMessage() : (MapMessage) session.createTextMessage();
        mapMessage.setJMSCorrelationID(this.m_tableModel.getJMSCorrelationID());
        mapMessage.setJMSDeliveryMode(this.m_tableModel.getJMSDeliveryMode());
        mapMessage.setJMSExpiration((long) this.m_tableModel.getJMSExpiration());
        mapMessage.setJMSPriority(this.m_tableModel.getJMSPriority());
        mapMessage.setJMSType(this.m_tableModel.getJMSType());
        GemsDestination gemsDestination = this.m_tableModel.getJMSReplyTo();
        if (gemsDestination != null) {
            if (gemsDestination.m_destType == GemsDestination.DEST_TYPE.Queue) {
                mapMessage.setJMSReplyTo((Destination) session.createQueue(gemsDestination.m_destName));
            } else {
                mapMessage.setJMSReplyTo((Destination) session.createTopic(gemsDestination.m_destName));
            }
        }
        for (int i = this.m_tableModel.getHeaderRows(); i < this.m_tableModel.getRowCount(); ++i) {
            String string = (String) this.m_tableModel.getValueAt(i, 0);
            if (string == null || string.length() <= 0) continue;
            object = this.m_tableModel.getValueAt(i, 1);
            if (object instanceof String) {
                mapMessage.setStringProperty(string, (String) object);
                continue;
            }
            if (object instanceof Boolean) {
                mapMessage.setBooleanProperty(string, ((Boolean) object).booleanValue());
                continue;
            }
            if (object instanceof Integer) {
                mapMessage.setIntProperty(string, ((Integer) object).intValue());
                continue;
            }
            if (object instanceof Long) {
                mapMessage.setLongProperty(string, ((Long) object).longValue());
                continue;
            }
            if (object instanceof Short) {
                mapMessage.setShortProperty(string, ((Short) object).shortValue());
                continue;
            }
            if (object instanceof Byte) {
                mapMessage.setByteProperty(string, ((Byte) object).byteValue());
                continue;
            }
            if (object instanceof Float) {
                mapMessage.setFloatProperty(string, ((Float) object).floatValue());
                continue;
            }
            if (!(object instanceof Double)) continue;
            mapMessage.setDoubleProperty(string, ((Double) object).doubleValue());
        }
        if (mapMessage instanceof TextMessage) {
            ((TextMessage) mapMessage).setText(this.m_text.getText());
        } else if (mapMessage instanceof MapMessage) {
            MapMessage mapMessage2 = mapMessage;
            for (int j = this.m_mapTableModel.getHeaderRows(); j < this.m_mapTableModel.getRowCount(); ++j) {
                String valueAt = (String) this.m_mapTableModel.getValueAt(j, 0);
                if (valueAt == null || valueAt.length() <= 0) continue;
                Object object2 = this.m_mapTableModel.getValueAt(j, 1);
                if (object2 instanceof String) {
                    mapMessage2.setString((String) valueAt, (String) object2);
                    continue;
                }
                if (object2 instanceof Boolean) {
                    mapMessage2.setBoolean((String) valueAt, ((Boolean) object2).booleanValue());
                    continue;
                }
                if (object2 instanceof Integer) {
                    mapMessage2.setInt((String) valueAt, ((Integer) object2).intValue());
                    continue;
                }
                if (object2 instanceof Long) {
                    mapMessage2.setLong((String) valueAt, ((Long) object2).longValue());
                    continue;
                }
                if (object2 instanceof Short) {
                    mapMessage2.setShort((String) valueAt, ((Short) object2).shortValue());
                    continue;
                }
                if (object2 instanceof Byte) {
                    mapMessage2.setByte((String) valueAt, ((Byte) object2).byteValue());
                    continue;
                }
                if (object2 instanceof Float) {
                    mapMessage2.setFloat((String) valueAt, ((Float) object2).floatValue());
                    continue;
                }
                if (!(object2 instanceof Double)) continue;
                mapMessage2.setDouble((String) valueAt, ((Double) object2).doubleValue());
            }
        }
        return mapMessage;
    }

    class ViewOrigMsg implements ActionListener {
        Component parent;

        ViewOrigMsg(Component component) {
            this.parent = component;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsMessageFrame.this.m_isMonitorMsg && GemsMessageFrame.this.m_msg != null) {
                try {
                    MapMessage mapMessage = (MapMessage) GemsMessageFrame.this.m_msg;
                    if (mapMessage.itemExists("message_bytes")) {
                        Message message = Tibjms.createFromBytes((byte[]) mapMessage.getBytes("message_bytes"));
                        GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(GemsMessageFrame.this.m_cn, false, GemsMessageFrame.this.m_destination, GemsMessageFrame.this.m_isQueue, GemsMessageFrame.this.m_frame, false);
                        gemsMessageFrame.populate(message);
                    }
                } catch (Exception var2_3) {
                    Gems.debug(var2_3.getMessage());
                    return;
                }
            }
        }
    }

    class AddJMSReplyToProperty implements ActionListener {
        AddJMSReplyToProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsMessageFrame.this.m_frame, GemsMessageFrame.this.m_cn);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
                GemsMessageFrame.this.m_tableModel.addJMSReplyToProperty(gemsDestinationPicker.m_retDest);
            }
        }
    }

    class AddDoubleField
            implements ActionListener {
        AddDoubleField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addDoubleProperty();
        }
    }

    class AddFloatField
            implements ActionListener {
        AddFloatField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addFloatProperty();
        }
    }

    class AddBooleanField
            implements ActionListener {
        AddBooleanField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addBooleanProperty();
        }
    }

    class AddByteField
            implements ActionListener {
        AddByteField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addByteProperty();
        }
    }

    class AddShortField
            implements ActionListener {
        AddShortField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addShortProperty();
        }
    }

    class AddLongField
            implements ActionListener {
        AddLongField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addLongProperty();
        }
    }

    class AddIntegerField
            implements ActionListener {
        AddIntegerField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addIntProperty();
        }
    }

    class AddStringField
            implements ActionListener {
        AddStringField() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(1);
            GemsMessageFrame.this.m_mapTableModel.addStringProperty();
        }
    }

    class AddPreserveProperty
            implements ActionListener {
        AddPreserveProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addPreserveProperty();
        }
    }

    class AddCompressProperty
            implements ActionListener {
        AddCompressProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addCompressProperty();
        }
    }

    class AddDoubleProperty
            implements ActionListener {
        AddDoubleProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addDoubleProperty();
        }
    }

    class AddFloatProperty
            implements ActionListener {
        AddFloatProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addFloatProperty();
        }
    }

    class AddBooleanProperty
            implements ActionListener {
        AddBooleanProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addBooleanProperty();
        }
    }

    class AddByteProperty
            implements ActionListener {
        AddByteProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addByteProperty();
        }
    }

    class AddShortProperty
            implements ActionListener {
        AddShortProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addShortProperty();
        }
    }

    class AddLongProperty
            implements ActionListener {
        AddLongProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addLongProperty();
        }
    }

    class AddIntegerProperty
            implements ActionListener {
        AddIntegerProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addIntProperty();
        }
    }

    class AddStringProperty
            implements ActionListener {
        AddStringProperty() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame.this.m_tabPane.setSelectedIndex(0);
            GemsMessageFrame.this.m_tableModel.addStringProperty();
        }
    }

    class LoadMessage
            implements ActionListener {
        LoadMessage() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            block11:
            {
                try {
                    String string;
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setDialogTitle("Load Message From File");
                    int n = jFileChooser.showOpenDialog(GemsMessageFrame.this.m_frame);
                    if (n != 0) break block11;
                    File file = jFileChooser.getSelectedFile();
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    GemsMessageFrame.this.m_text.setText("");
                    GemsMessageFrame.this.m_tableModel.emptyPropertyInfo(GemsMessageFrame.this.m_destprop);
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer stringBuffer2 = new StringBuffer();
                    boolean bl = true;
                    boolean bl2 = true;
                    boolean bl3 = false;
                    block2:
                    while ((string = bufferedReader.readLine()) != null) {
                        if (!bl2) {
                            stringBuffer2.append('\n');
                        }
                        stringBuffer2.append(string);
                        bl2 = false;
                        if (!string.startsWith("$Properties:")) continue;
                        bl3 = true;
                        while ((string = bufferedReader.readLine()) != null) {
                            if (string.startsWith("$Body:") || string.startsWith("$TextBody:")) {
                                while ((string = bufferedReader.readLine()) != null) {
                                    if (!bl) {
                                        stringBuffer.append('\n');
                                    }
                                    stringBuffer.append(string);
                                    bl = false;
                                }
                                GemsMessageFrame.this.createTextUI();
                                GemsMessageFrame.this.m_text.setText(stringBuffer.toString());
                                continue block2;
                            }
                            if (string.startsWith("$MapBody:")) {
                                GemsMessageFrame.this.createMapUI();
                                GemsMessageFrame.this.m_mapTableModel.populateEmptyPropertyInfo(0);
                                while ((string = bufferedReader.readLine()) != null) {
                                    if (GemsMessageFrame.this.m_mapTableModel == null) continue;
                                    GemsMessageFrame.this.m_mapTableModel.addPropertyInfo(string);
                                }
                                continue block2;
                            }
                            if (!string.startsWith("JMS_") && string.startsWith("JMS")) continue;
                            GemsMessageFrame.this.m_tableModel.addPropertyInfo(string);
                        }
                    }
                    if (!bl3) {
                        GemsMessageFrame.this.m_text.setText(stringBuffer2.toString());
                    }
                    bufferedReader.close();
                } catch (IOException var2_3) {
                    System.err.println("JavaIOException: " + var2_3.getMessage());
                    return;
                }
            }
        }
    }

    class SaveMessage
            implements ActionListener {
        SaveMessage() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsMessageFrame.this.m_msg == null) {
                return;
            }
            try {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setApproveButtonText("Save");
                jFileChooser.setDialogTitle("Save Message To File");
                int n = jFileChooser.showOpenDialog(GemsMessageFrame.this.m_frame);
                if (n == 0) {
                    File file = jFileChooser.getSelectedFile();
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    PrintStream printStream = new PrintStream(fileOutputStream);
                    printStream.println("$Header:");
                    printStream.println("JMSMessageID=" + GemsMessageFrame.this.m_msg.getJMSMessageID());
                    Date date = new Date();
                    date.setTime(GemsMessageFrame.this.m_msg.getJMSTimestamp());
                    printStream.println("JMSTimestamp=" + GemsMessageFrame.this.dateFormatMillis.format(date).toString());
                    printStream.println("JMSDestination=" + (Object) GemsMessageFrame.this.m_msg.getJMSDestination());
                    String string = GemsMessageFrame.this.m_msg.getJMSDeliveryMode() == 2 ? "PERSISTENT" : (GemsMessageFrame.this.m_msg.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "RELIABLE");
                    printStream.println("JMSDeliveryMode=" + string);
                    if (GemsMessageFrame.this.m_msg.getJMSCorrelationID() != null) {
                        printStream.println("JMSCorrelationID=" + GemsMessageFrame.this.m_msg.getJMSCorrelationID());
                    }
                    if (GemsMessageFrame.this.m_msg.getJMSType() != null) {
                        printStream.println("JMSType=" + GemsMessageFrame.this.m_msg.getJMSType());
                    }
                    if (GemsMessageFrame.this.m_msg.getJMSReplyTo() != null) {
                        printStream.println("JMSReplyTo=" + (Object) GemsMessageFrame.this.m_msg.getJMSReplyTo());
                    }
                    if (GemsMessageFrame.this.m_msg.getJMSExpiration() != 0) {
                        date.setTime(GemsMessageFrame.this.m_msg.getJMSExpiration());
                        printStream.println("JMSExpiration=" + GemsMessageFrame.this.dateFormatMillis.format(date).toString());
                    }
                    printStream.println("JMSPriority=" + String.valueOf(GemsMessageFrame.this.m_msg.getJMSPriority()));
                    printStream.println("$Properties:");
                    Enumeration enumeration = GemsMessageFrame.this.m_msg.getPropertyNames();
                    while (enumeration.hasMoreElements()) {
                        String string2 = (String) enumeration.nextElement();
                        printStream.println(string2 + "=" + GemsMsgPropTableModel.toTypedString(null, GemsMessageFrame.this.m_msg.getObjectProperty(string2)));
                    }
                    if (GemsMessageFrame.this.m_msg instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) GemsMessageFrame.this.m_msg;
                        printStream.println("$TextBody:");
                        printStream.println(textMessage.getText());
                    } else if (GemsMessageFrame.this.m_msg instanceof MapMessage) {
                        MapMessage mapMessage = (MapMessage) GemsMessageFrame.this.m_msg;
                        printStream.println("$MapBody:");
                        Enumeration enumeration2 = mapMessage.getMapNames();
                        while (enumeration2.hasMoreElements()) {
                            String string3 = (String) enumeration2.nextElement();
                            printStream.println(string3 + "=" + GemsMsgPropTableModel.toTypedString(null, mapMessage.getObject(string3)));
                        }
                    } else if (GemsMessageFrame.this.m_msg instanceof BytesMessage) {
                        BytesMessage bytesMessage = (BytesMessage) GemsMessageFrame.this.m_msg;
                        printStream.println("$BytesBody:");
                        bytesMessage.reset();
                        long l = bytesMessage.getBodyLength();
                        byte[] arrby = new byte[(int) l];
                        bytesMessage.readBytes(arrby, (int) l);
                        printStream.println(StringUtilities.dumpBytes(arrby));
                    } else if (GemsMessageFrame.this.m_msg instanceof StreamMessage) {
                        StreamMessage streamMessage = (StreamMessage) GemsMessageFrame.this.m_msg;
                        printStream.println("$StreamBody:");
                        printStream.println(streamMessage.toString());
                    } else if (GemsMessageFrame.this.m_msg instanceof ObjectMessage) {
                        ObjectMessage objectMessage = (ObjectMessage) GemsMessageFrame.this.m_msg;
                        printStream.println("$ObjectBody:");
                        printStream.println(objectMessage.toString());
                    }
                    printStream.close();
                }
            } catch (JMSException var2_3) {
                System.err.println("JMSException: " + var2_3.getMessage());
                return;
            } catch (IOException var2_4) {
                System.err.println("JavaIOException: " + var2_4.getMessage());
                return;
            }
        }
    }

    class SendPressed
            implements ActionListener {
        SendPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsMessageFrame.this.m_table.getCellEditor() != null) {
                GemsMessageFrame.this.m_table.getCellEditor().stopCellEditing();
            }
            if (GemsMessageFrame.this.m_mapTableModel != null && GemsMessageFrame.this.m_mapTableModel.m_table != null && GemsMessageFrame.this.m_mapTableModel.m_table.getCellEditor() != null) {
                GemsMessageFrame.this.m_mapTableModel.m_table.getCellEditor().stopCellEditing();
            }
            if (GemsMessageFrame.this.m_isEditor) {
                GemsMessageFrame.this.dispose();
                return;
            }
            String string = GemsMessageFrame.this.m_tableModel.getJMSDestination();
            try {
                if (GemsMessageFrame.this.m_isQueue) {
                    if (GemsMessageFrame.this.m_admin.getQueue(string) == null) {
                        JOptionPane.showMessageDialog(GemsMessageFrame.this.m_this, "The destination queue does not exist!", "Error", 1);
                        return;
                    }
                } else if (GemsMessageFrame.this.m_admin.getTopic(string) == null) {
                    JOptionPane.showMessageDialog(GemsMessageFrame.this.m_this, "The destination topic does not exist!", "Error", 1);
                    return;
                }
            } catch (TibjmsAdminException var3_3) {
                JOptionPane.showMessageDialog(GemsMessageFrame.this.m_this, var3_3.getMessage(), "Error", 1);
                return;
            } catch (Exception var3_4) {
                JOptionPane.showMessageDialog(GemsMessageFrame.this.m_this, "Connection Error, please close send message window and retry", "Error", 1);
                return;
            }
            MessageProducer messageProducer = null;
            try {
                TibjmsConnectionFactory tibjmsConnectionFactory;
                if (GemsMessageFrame.this.m_connection == null) {
                    tibjmsConnectionFactory = new TibjmsConnectionFactory(GemsMessageFrame.this.m_cn.m_url, null, GemsMessageFrame.this.m_cn.m_sslParams);
                    GemsMessageFrame.this.m_connection = tibjmsConnectionFactory.createConnection(GemsMessageFrame.this.m_cn.m_user, GemsMessageFrame.this.m_cn.m_password);
                    GemsMessageFrame.this.m_connection.start();
                }
                if (GemsMessageFrame.this.m_session == null) {
                    GemsMessageFrame.this.m_session = GemsMessageFrame.this.m_connection.createSession(false, 1);
                }
                tibjmsConnectionFactory = (TibjmsConnectionFactory) (GemsMessageFrame.this.m_isQueue ? GemsMessageFrame.this.m_session.createQueue(string) : GemsMessageFrame.this.m_session.createTopic(string));
                messageProducer = GemsMessageFrame.this.m_session.createProducer(null);
                Message message = GemsMessageFrame.this.createMessage(GemsMessageFrame.this.m_session);
                messageProducer.send((Destination) tibjmsConnectionFactory, message, GemsMessageFrame.this.m_tableModel.getJMSDeliveryMode(), GemsMessageFrame.this.m_tableModel.getJMSPriority(), (long) GemsMessageFrame.this.m_tableModel.getJMSExpiration());
                if (GemsMessageFrame.this.m_isQueue) {
                    GemsMessageFrame.this.m_sendButt.setText("Send Again");
                } else {
                    GemsMessageFrame.this.m_sendButt.setText("Publish Again");
                }
                messageProducer.close();
            } catch (JMSException var4_7) {
                JOptionPane.showMessageDialog(GemsMessageFrame.this.m_this, var4_7.getMessage(), "Error", 1);
                System.err.println("Exception: " + var4_7.getMessage());
                GemsMessageFrame.this.close();
                return;
            } catch (Exception var4_8) {
                JOptionPane.showMessageDialog(GemsMessageFrame.this.m_this, var4_8.getMessage(), "Error", 1);
                System.err.println("Exception: " + var4_8.getMessage());
                return;
            }
        }
    }

}

