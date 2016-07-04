/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.TibjmsTopicConnectionFactory
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.Session
 *  javax.jms.Topic
 *  javax.jms.TopicConnection
 *  javax.jms.TopicSubscriber
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSubscriber;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import com.tibco.tibjms.TibjmsTopicConnectionFactory;
import com.tibco.tibjms.admin.TibjmsAdmin;

public class GemsDurableBrowser
        extends JFrame {
    protected JTextField m_conn;
    protected JTextField m_durable;
    protected JTextField m_topic;
    protected JTextField m_clientId;
    protected JTextField m_msgsRead;
    protected JTextField m_msgsDisplay;
    protected JTextField m_filter;
    protected String m_selector;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JCheckBox m_ackMsgs;
    protected GemsMessageTableModel m_tableModel;
    protected boolean m_viewoldestFirst;
    protected JMenuItem m_optMenuItem;
    protected JMenuItem m_dumpMenuItem;
    JFrame m_frame;
    JPanel m_panel;
    boolean m_running = false;
    boolean m_noLocal = false;
    int m_msgs = 0;
    int m_maxMsgs = 10;
    Message m_msg = null;
    TibjmsAdmin m_admin = null;
    Session m_session = null;
    GemsConnectionNode m_cn;
    TopicConnection m_connection = null;
    TopicSubscriber m_subscriber = null;
    Timer m_timer;
    JTable m_table;
    TableSorter m_sorter;

    public GemsDurableBrowser(GemsConnectionNode gemsConnectionNode, String string, String string2, String string3, String string4, boolean bl) {
        super(Gems.getGems().getTitlePrefix() + "Durable Browser");
        this.m_timer = new Timer(Gems.getGems().getMsgReadDelay(), new RefreshTimerAction());
        this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
        this.setLocation(400, 175);
        this.setDefaultCloseOperation(2);
        this.m_frame = this;
        this.m_cn = gemsConnectionNode;
        this.m_noLocal = bl;
        this.m_selector = string4;
        JMenuBar jMenuBar = this.constructMenuBar();
        this.setJMenuBar(jMenuBar);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add((Component) jPanel2, "North");
        JLabel jLabel = new JLabel("Server:", 11);
        this.m_conn = new JTextField(gemsConnectionNode.getName(), 20);
        this.m_conn.setEditable(false);
        this.m_conn.setMaximumSize(new Dimension(0, 24));
        jLabel.setLabelFor(this.m_conn);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_conn);
        JLabel jLabel2 = new JLabel("Topic Name:", 11);
        this.m_topic = new JTextField(string3, 20);
        this.m_topic.setEditable(false);
        this.m_topic.setMaximumSize(new Dimension(0, 24));
        jLabel2.setLabelFor(this.m_topic);
        jPanel2.add(jLabel2);
        jPanel2.add(this.m_topic);
        JLabel jLabel3 = new JLabel("Durable Name:", 11);
        this.m_durable = new JTextField(string, 20);
        this.m_durable.setEditable(false);
        this.m_durable.setMaximumSize(new Dimension(0, 24));
        jLabel3.setLabelFor(this.m_durable);
        jPanel2.add(jLabel3);
        jPanel2.add(this.m_durable);
        JLabel jLabel4 = new JLabel("Client Id:", 11);
        this.m_clientId = new JTextField(string2, 20);
        this.m_clientId.setMaximumSize(new Dimension(0, 24));
        this.m_clientId.setEditable(false);
        jLabel4.setLabelFor(this.m_clientId);
        jPanel2.add(jLabel4);
        jPanel2.add(this.m_clientId);
        JLabel jLabel5 = new JLabel("Destination Filter:", 11);
        this.m_filter = new JTextField("", 20);
        this.m_filter.setMaximumSize(new Dimension(0, 24));
        jLabel5.setLabelFor(this.m_filter);
        jPanel2.add(jLabel5);
        jPanel2.add(this.m_filter);
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        JLabel jLabel6 = new JLabel("Msgs to Read:", 11);
        this.m_msgsRead = new JTextField("10", 20);
        this.m_msgsRead.setMinimumSize(new Dimension(40, 24));
        jLabel6.setLabelFor(this.m_msgsRead);
        jPanel2.add(jLabel6);
        jPanel3.add(this.m_msgsRead);
        this.m_ackMsgs = new JCheckBox("Purge Msgs Read", false);
        if (!Gems.getGems().getViewOnlyMode()) {
            jPanel3.add(this.m_ackMsgs);
        }
        jPanel2.add(jPanel3);
        this.m_tableModel = new GemsMessageTableModel(false, false);
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_table.setSelectionMode(0);
        this.m_tableModel.m_table = this.m_table;
        this.addMouseListenerToTable(this.m_table);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(450, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        Component component = Box.createRigidArea(new Dimension(250, 10));
        jPanel4.add(component);
        this.m_startButton = new JButton("Start");
        this.m_startButton.addActionListener(new StartPressed());
        this.m_stopButton = new JButton("Stop");
        this.m_stopButton.addActionListener(new StopPressed());
        this.m_stopButton.setEnabled(false);
        jPanel4.add(this.m_startButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel4.add(component);
        jPanel4.add(this.m_stopButton);
        jPanel.add((Component) jPanel4, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 3, 4, 5, 5, 5, 5);
        this.m_frame.setIconImage(Gems.getGems().m_icon.getImage());
        this.pack();
        this.show();
    }

    private JMenuBar constructMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenu.setMnemonic(70);
        jMenuBar.add(jMenu);
        this.m_dumpMenuItem = new JMenuItem("Save Messages To File...");
        this.m_dumpMenuItem.addActionListener(new DumpToFile());
        jMenu.add(this.m_dumpMenuItem);
        JMenuItem jMenuItem = jMenu.add(new JMenuItem("Exit"));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsDurableBrowser.this.dispose();
            }
        });
        jMenu = new JMenu("Edit");
        jMenu.setMnemonic(69);
        jMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        jMenuItem.setText("Cut");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 2));
        jMenu.add(jMenuItem);
        jMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        jMenuItem.setText("Copy");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2));
        jMenu.add(jMenuItem);
        jMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        jMenuItem.setText("Paste");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 2));
        jMenu.add(jMenuItem);
        jMenu.addSeparator();
        this.m_optMenuItem = jMenu.add(new JMenuItem("Options..."));
        this.m_optMenuItem.addActionListener(new EditOptionsAction());
        jMenuBar.add(jMenu);
        jMenu = new JMenu("Message");
        jMenu.setMnemonic(77);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("View Message..."));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsDurableBrowser.this.showMessageFrame();
            }
        });
        if (!Gems.getGems().getViewOnlyMode()) {
            jMenuItem = jMenu.add(new JMenuItem("Destroy Server Message"));
            jMenuItem.addActionListener(new DestroyMessageAction());
        }
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsDurableBrowser.this.showMessageFrame();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void dispose() {
        this.stop();
        super.dispose();
    }

    public void showMessageFrame() {
        Message message = this.m_tableModel.getSelectedMessage();
        if (message != null) {
            GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(this.m_cn, false, null, true, null, false);
            gemsMessageFrame.populate(message);
        } else {
            JOptionPane.showMessageDialog(this.m_frame, "Select a Message to view!", "View Message", 1);
        }
    }

    public void stop() {
        this.m_timer.stop();
        this.m_running = false;
        try {
            if (this.m_subscriber != null) {
                this.m_subscriber.close();
                this.m_subscriber = null;
            }
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
        this.m_durable.setEnabled(true);
        this.m_topic.setEnabled(true);
        this.m_clientId.setEnabled(true);
        this.m_msgsRead.setEnabled(true);
        this.m_startButton.setEnabled(true);
        this.m_stopButton.setEnabled(false);
        this.m_ackMsgs.setEnabled(true);
        this.m_filter.setEnabled(true);
        this.m_optMenuItem.setEnabled(true);
        this.m_dumpMenuItem.setEnabled(true);
    }

    public void start() {
        this.m_running = true;
        this.m_msgs = 0;
        this.m_durable.setEnabled(false);
        this.m_topic.setEnabled(false);
        this.m_clientId.setEnabled(false);
        this.m_msgsRead.setEnabled(false);
        this.m_startButton.setEnabled(false);
        this.m_stopButton.setEnabled(true);
        this.m_ackMsgs.setEnabled(false);
        this.m_filter.setEnabled(false);
        this.m_optMenuItem.setEnabled(false);
        this.m_dumpMenuItem.setEnabled(false);
        try {
            this.m_maxMsgs = Integer.parseInt(this.m_msgsRead.getText());
        } catch (Exception var1_1) {
            this.m_maxMsgs = 10;
        }
        this.m_tableModel.buildColumnHeaders();
        try {
            TibjmsTopicConnectionFactory tibjmsTopicConnectionFactory = new TibjmsTopicConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
            this.m_connection = tibjmsTopicConnectionFactory.createTopicConnection(this.m_cn.m_user, this.m_cn.m_password);
            if (this.m_clientId.getText().length() > 0) {
                this.m_connection.setClientID(this.m_clientId.getText());
            }
            this.m_session = this.m_connection.createSession(false, 2);
            Topic topic = this.m_session.createTopic(this.m_topic.getText());
            String string = null;
            if (this.m_selector != null && this.m_selector.length() > 0) {
                string = this.m_selector;
            }
            this.m_subscriber = this.m_session.createDurableSubscriber(topic, this.m_durable.getText(), string, this.m_noLocal);
            this.m_connection.start();
            this.m_timer.start();
        } catch (JMSException var1_3) {
            JOptionPane.showMessageDialog(this.m_frame, var1_3.getMessage(), "Error", 1);
            this.stop();
        }
    }

    class DumpToFile
            implements ActionListener {
        DumpToFile() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setApproveButtonText("Save");
                jFileChooser.setDialogTitle("Save Messages To File (appends)");
                int n = jFileChooser.showOpenDialog(GemsDurableBrowser.this.m_frame);
                if (n == 0) {
                    File file = jFileChooser.getSelectedFile();
                    GemsDurableBrowser.this.m_tableModel.dumpMsgsToFile(file);
                }
            } catch (IOException var2_3) {
                JOptionPane.showMessageDialog(GemsDurableBrowser.this.m_frame, var2_3.getMessage(), "Error", 1);
                return;
            }
        }
    }

    class EditOptionsAction
            implements ActionListener {
        EditOptionsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            new GemsBrowserOptionsDialog(GemsDurableBrowser.this.m_frame, "Edit Durable Browser Options");
            GemsDurableBrowser.this.m_timer.setDelay(Gems.getGems().getMsgReadDelay());
            GemsDurableBrowser.this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
        }
    }

    class DestroyMessageAction
            implements ActionListener {
        DestroyMessageAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Message message = GemsDurableBrowser.this.m_tableModel.getSelectedMessage();
            if (message == null) {
                JOptionPane.showMessageDialog(GemsDurableBrowser.this.m_frame, "Select a Message to destroy!", "Destroy Message", 1);
                return;
            }
            try {
                int n = JOptionPane.showConfirmDialog(GemsDurableBrowser.this.m_frame, "Destroy Message: " + message.getJMSMessageID() + " on " + GemsDurableBrowser.this.m_cn.getName(), "Destroy Message", 0);
                if (n == 0) {
                    GemsDurableBrowser.this.m_cn.removeMessage(message);
                }
            } catch (Exception var3_4) {
                JOptionPane.showMessageDialog(GemsDurableBrowser.this.m_frame, var3_4.getMessage(), "Error", 1);
                return;
            }
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDurableBrowser.this.stop();
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDurableBrowser.this.start();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsDurableBrowser.this.m_running) {
                try {
                    boolean bl = false;
                    Message message = null;
                    while (!bl) {
                        message = GemsDurableBrowser.this.m_subscriber.receiveNoWait();
                        if (message != null) {
                            Topic topic;
                            bl = true;
                            if (GemsDurableBrowser.this.m_filter == null || GemsDurableBrowser.this.m_filter.getText().length() <= 0 || (topic = (Topic) message.getJMSDestination()).getTopicName().equals(GemsDurableBrowser.this.m_filter.getText()))
                                continue;
                            bl = false;
                            continue;
                        }
                        return;
                    }
                    GemsDurableBrowser.this.m_tableModel.addMessage(message, GemsDurableBrowser.this.m_viewoldestFirst);
                    if (GemsDurableBrowser.this.m_ackMsgs.isSelected()) {
                        message.acknowledge();
                    }
                    if (++GemsDurableBrowser.this.m_msgs >= GemsDurableBrowser.this.m_maxMsgs) {
                        GemsDurableBrowser.this.stop();
                    }
                } catch (JMSException var2_3) {
                    System.err.println("Exception: " + var2_3.getMessage());
                    GemsDurableBrowser.this.stop();
                }
            }
        }
    }

}

