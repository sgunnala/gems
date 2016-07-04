/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.Tibjms
 *  com.tibco.tibjms.TibjmsTopicConnectionFactory
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  javax.jms.JMSException
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.Topic
 *  javax.jms.TopicConnection
 *  javax.jms.TopicSession
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
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import com.tibco.tibjms.Tibjms;
import com.tibco.tibjms.TibjmsTopicConnectionFactory;
import com.tibco.tibjms.admin.TibjmsAdmin;

public class GemsTopicSubscriber
        extends JFrame {
    protected JTextField m_conn;
    protected JTextField m_topic;
    protected JButton m_destwiz;
    protected JTextField m_msgsRead;
    protected JTextField m_msgsDisplay;
    protected JTextField m_selector;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JCheckBox m_noLimit;
    protected GemsMessageTableModel m_tableModel;
    protected boolean m_viewoldestFirst;
    protected JMenuItem m_optMenuItem;
    protected JMenuItem m_dumpMenuItem;
    protected boolean m_isMonitorTopic;
    protected boolean m_isMonitorQueue;
    protected boolean m_isMonitor;
    JFrame m_frame;
    JPanel m_panel;
    boolean m_running = false;
    int m_msgs = 0;
    int m_maxMsgs = 10;
    Message m_msg = null;
    TibjmsAdmin m_admin = null;
    TopicSession m_session = null;
    GemsConnectionNode m_cn;
    TopicConnection m_connection = null;
    TopicSubscriber m_subscriber = null;
    Timer m_timer;
    JTable m_table;
    TableSorter m_sorter;

    public GemsTopicSubscriber(GemsConnectionNode gemsConnectionNode, String string, String string2) {
        super(Gems.getGems().getTitlePrefix() + string2);
        this.m_timer = new Timer(Gems.getGems().getMsgReadDelay(), new RefreshTimerAction());
        this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
        this.m_isMonitorTopic = false;
        this.m_isMonitorQueue = false;
        this.m_isMonitor = false;
        this.setLocation(400, 175);
        this.setDefaultCloseOperation(2);
        this.m_frame = this;
        this.m_cn = gemsConnectionNode;
        if (string == null) {
            string = new String();
        }
        if (string.startsWith("$sys.monitor.Q")) {
            this.m_isMonitorQueue = true;
        } else if (string.startsWith("$sys.monitor.T")) {
            this.m_isMonitorTopic = true;
        } else if (string.startsWith("$sys.monitor")) {
            this.m_isMonitor = true;
        }
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
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        JLabel jLabel2 = new JLabel("Topic Name:", 11);
        this.m_topic = new JTextField(string, 20);
        if (this.m_isMonitor) {
            this.m_topic.setEnabled(false);
        }
        jLabel2.setLabelFor(this.m_topic);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_topic);
        this.m_destwiz = new JButton("...");
        this.m_destwiz.setPreferredSize(new Dimension(18, 16));
        this.m_destwiz.addActionListener(new DestinationWizardAction());
        if (this.m_isMonitor) {
            this.m_destwiz.setEnabled(false);
        }
        jPanel3.add(this.m_destwiz);
        jPanel2.add(jPanel3);
        JLabel jLabel3 = new JLabel("Selector:", 11);
        this.m_selector = new JTextField("", 20);
        jLabel3.setLabelFor(this.m_selector);
        jPanel2.add(jLabel3);
        jPanel2.add(this.m_selector);
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        JLabel jLabel4 = new JLabel("Msgs to Read:", 11);
        this.m_msgsRead = new JTextField("10", 20);
        this.m_msgsRead.setMinimumSize(new Dimension(40, 24));
        jLabel4.setLabelFor(this.m_msgsRead);
        jPanel2.add(jLabel4);
        jPanel4.add(this.m_msgsRead);
        this.m_noLimit = new JCheckBox("No Limit", false);
        jPanel4.add(this.m_noLimit);
        jPanel2.add(jPanel4);
        this.m_tableModel = new GemsMessageTableModel(false, false);
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_table.setSelectionMode(0);
        this.m_tableModel.m_table = this.m_table;
        this.addMouseListenerToTable(this.m_table);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(650, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel5 = new JPanel(true);
        jPanel5.setLayout(new BoxLayout(jPanel5, 0));
        Component component = Box.createRigidArea(new Dimension(250, 10));
        jPanel5.add(component);
        this.m_startButton = new JButton("Start");
        this.m_startButton.addActionListener(new StartPressed());
        this.m_stopButton = new JButton("Stop");
        this.m_stopButton.addActionListener(new StopPressed());
        this.m_stopButton.setEnabled(false);
        jPanel5.add(this.m_startButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel5.add(component);
        jPanel5.add(this.m_stopButton);
        jPanel.add((Component) jPanel5, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 2, 4, 5, 5, 5, 5);
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
                GemsTopicSubscriber.this.dispose();
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
        jMenuItem = this.m_isMonitorQueue || this.m_isMonitorTopic || this.m_isMonitor ? jMenu.add(new JMenuItem("View Monitor Message...")) : jMenu.add(new JMenuItem("View Message..."));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsTopicSubscriber.this.showMessageFrame();
            }
        });
        if (this.m_isMonitorQueue || this.m_isMonitorTopic) {
            jMenuItem = jMenu.add(new JMenuItem("View Original Message..."));
            jMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionEvent) {
                    GemsTopicSubscriber.this.showOrigMessageFrame();
                }
            });
        }
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsTopicSubscriber.this.showMessageFrame();
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
            GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(this.m_cn, false, null, true, null, this.m_topic.getText().startsWith("$sys.monitor."));
            gemsMessageFrame.populate(message);
        } else {
            JOptionPane.showMessageDialog(this.m_frame, "Select a Message to view!", "View Message", 1);
        }
    }

    public void showOrigMessageFrame() {
        Message message = this.m_tableModel.getSelectedMessage();
        if (message != null && message instanceof MapMessage) {
            try {
                MapMessage mapMessage = (MapMessage) message;
                if (mapMessage.itemExists("message_bytes")) {
                    Message message2 = Tibjms.createFromBytes((byte[]) mapMessage.getBytes("message_bytes"));
                    GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(this.m_cn, false, null, false, null, false);
                    gemsMessageFrame.populate(message2);
                    return;
                }
            } catch (Exception var2_3) {
                // empty catch block
            }
            JOptionPane.showMessageDialog(null, "There is no original message associated with this monitor message", "Error", 1);
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
        if (!this.m_isMonitor) {
            this.m_topic.setEnabled(true);
        }
        this.m_msgsRead.setEnabled(true);
        this.m_startButton.setEnabled(true);
        this.m_stopButton.setEnabled(false);
        this.m_noLimit.setEnabled(true);
        this.m_selector.setEnabled(true);
        if (!this.m_isMonitor) {
            this.m_destwiz.setEnabled(true);
        }
        this.m_optMenuItem.setEnabled(true);
        this.m_dumpMenuItem.setEnabled(true);
    }

    public void start() {
        this.m_running = true;
        this.m_msgs = 0;
        this.m_topic.setEnabled(false);
        this.m_msgsRead.setEnabled(false);
        this.m_startButton.setEnabled(false);
        this.m_stopButton.setEnabled(true);
        this.m_noLimit.setEnabled(false);
        this.m_selector.setEnabled(false);
        this.m_destwiz.setEnabled(false);
        this.m_optMenuItem.setEnabled(false);
        this.m_dumpMenuItem.setEnabled(false);
        try {
            this.m_maxMsgs = Integer.parseInt(this.m_msgsRead.getText());
        } catch (Exception var1_1) {
            this.m_maxMsgs = 10;
        }
        if (this.m_topic.getText().startsWith("$sys.monitor.")) {
            this.m_tableModel.buildMonitorColumnHeaders(!this.m_isMonitor);
        } else {
            this.m_tableModel.buildColumnHeaders();
        }
        try {
            TibjmsTopicConnectionFactory tibjmsTopicConnectionFactory = new TibjmsTopicConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
            this.m_connection = tibjmsTopicConnectionFactory.createTopicConnection(this.m_cn.m_user, this.m_cn.m_password);
            this.m_session = this.m_connection.createTopicSession(false, 1);
            Topic topic = this.m_session.createTopic(this.m_topic.getText());
            this.m_subscriber = this.m_selector != null && this.m_selector.getText().length() > 0 ? this.m_session.createSubscriber(topic, this.m_selector.getText(), false) : this.m_session.createSubscriber(topic);
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
                int n = jFileChooser.showOpenDialog(GemsTopicSubscriber.this.m_frame);
                if (n == 0) {
                    File file = jFileChooser.getSelectedFile();
                    GemsTopicSubscriber.this.m_tableModel.dumpMsgsToFile(file);
                }
            } catch (IOException var2_3) {
                JOptionPane.showMessageDialog(GemsTopicSubscriber.this.m_frame, var2_3.getMessage(), "Error", 1);
                return;
            }
        }
    }

    class DestinationWizardAction
            implements ActionListener {
        DestinationWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsTopicSubscriber.this.m_frame, GemsTopicSubscriber.this.m_cn, GemsTopicSubscriber.this.m_isMonitorQueue ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                if (GemsTopicSubscriber.this.m_isMonitorQueue) {
                    GemsTopicSubscriber.this.m_topic.setText("$sys.monitor.Q.*." + gemsDestinationPicker.m_retDest.m_destName);
                } else if (GemsTopicSubscriber.this.m_isMonitorTopic) {
                    GemsTopicSubscriber.this.m_topic.setText("$sys.monitor.T.*." + gemsDestinationPicker.m_retDest.m_destName);
                } else {
                    GemsTopicSubscriber.this.m_topic.setText(gemsDestinationPicker.m_retDest.m_destName);
                }
            }
        }
    }

    class EditOptionsAction
            implements ActionListener {
        EditOptionsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            new GemsBrowserOptionsDialog(GemsTopicSubscriber.this.m_frame, "Edit Options");
            GemsTopicSubscriber.this.m_timer.setDelay(Gems.getGems().getMsgReadDelay());
            GemsTopicSubscriber.this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTopicSubscriber.this.stop();
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTopicSubscriber.this.start();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsTopicSubscriber.this.m_running) {
                try {
                    Message message = GemsTopicSubscriber.this.m_subscriber.receiveNoWait();
                    if (message != null) {
                        if (GemsTopicSubscriber.this.m_topic.getText().startsWith("$sys.monitor.")) {
                            GemsTopicSubscriber.this.m_tableModel.addMonitorMessage(message, GemsTopicSubscriber.this.m_viewoldestFirst, !GemsTopicSubscriber.this.m_isMonitor);
                        } else {
                            GemsTopicSubscriber.this.m_tableModel.addMessage(message, GemsTopicSubscriber.this.m_viewoldestFirst);
                        }
                        if (!GemsTopicSubscriber.this.m_noLimit.isSelected() && ++GemsTopicSubscriber.this.m_msgs >= GemsTopicSubscriber.this.m_maxMsgs) {
                            GemsTopicSubscriber.this.stop();
                        }
                    }
                } catch (JMSException var2_3) {
                    System.err.println("Exception: " + var2_3.getMessage());
                    GemsTopicSubscriber.this.stop();
                }
            }
        }
    }

}

