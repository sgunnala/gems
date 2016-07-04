/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.Tibjms
 *  com.tibco.tibjms.TibjmsTopicConnectionFactory
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.MessageListener
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
import java.util.Hashtable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import com.tibco.tibjms.Tibjms;
import com.tibco.tibjms.TibjmsTopicConnectionFactory;
import com.tibco.tibjms.admin.TibjmsAdmin;

public class GemsReqReplyMonitor
        extends JFrame {
    public Hashtable m_pending;
    protected JTextField m_conn;
    protected JTextField m_reqDest;
    protected JTextField m_msgsRead;
    protected JTextField m_msgsDisplay;
    protected JTextField m_replyDest;
    protected JButton m_destwiz;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JCheckBox m_noLimit;
    protected GemsMessageTableModel m_tableModel;
    protected boolean m_viewoldestFirst;
    protected long m_failCount;
    protected JMenuItem m_optMenuItem;
    protected JMenuItem m_dumpMenuItem;
    JFrame m_frame;
    JPanel m_panel;
    boolean m_running = false;
    boolean m_useMonitorMsgTimestamp = Gems.getGems().getUseServerTimestamps();
    int m_msgs = 0;
    int m_maxMsgs = 10;
    Message m_msg = null;
    TibjmsAdmin m_admin = null;
    TopicSession m_session = null;
    TopicSession m_replysession = null;
    GemsConnectionNode m_cn;
    TopicConnection m_connection = null;
    TopicSubscriber m_subscriber = null;
    TopicSubscriber m_replySubscriber = null;
    Timer m_timer;
    JTable m_table;
    TableSorter m_sorter;
    boolean m_isQueue;
    Mutex m_mutex;

    public GemsReqReplyMonitor(GemsConnectionNode gemsConnectionNode, String string, boolean bl, String string2) {
        super(Gems.getGems().getTitlePrefix() + string2);
        this.m_timer = new Timer(Gems.getGems().getMsgReadDelay(), new RefreshTimerAction());
        this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
        this.m_failCount = Gems.getGems().getRequestReplyTimeout() * 1000 / Gems.getGems().getMsgReadDelay();
        this.m_isQueue = true;
        this.m_mutex = new Mutex();
        this.m_pending = new Hashtable();
        this.setLocation(400, 175);
        this.setDefaultCloseOperation(2);
        this.m_frame = this;
        this.m_cn = gemsConnectionNode;
        this.m_isQueue = bl;
        String string3 = bl ? "Queue" : "Topic";
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
        JLabel jLabel2 = new JLabel("Request " + string3 + " Name:", 11);
        this.m_reqDest = new JTextField(string, 20);
        jLabel2.setLabelFor(this.m_reqDest);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_reqDest);
        this.m_destwiz = new JButton("...");
        this.m_destwiz.setPreferredSize(new Dimension(18, 16));
        this.m_destwiz.addActionListener(new DestinationWizardAction());
        jPanel3.add(this.m_destwiz);
        jPanel2.add(jPanel3);
        JLabel jLabel3 = new JLabel("Reply " + string3 + " Name:", 11);
        this.m_replyDest = new JTextField("$TMP$.>", 20);
        this.m_replyDest.setMaximumSize(new Dimension(0, 24));
        jLabel3.setLabelFor(this.m_replyDest);
        jPanel2.add(jLabel3);
        jPanel2.add(this.m_replyDest);
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        JLabel jLabel4 = new JLabel("Requests to Read:", 11);
        this.m_msgsRead = new JTextField("10", 20);
        this.m_msgsRead.setMinimumSize(new Dimension(40, 24));
        jPanel4.setPreferredSize(new Dimension(100, 24));
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
        jScrollPane.setPreferredSize(new Dimension(480, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel5 = new JPanel(true);
        jPanel5.setLayout(new BoxLayout(jPanel5, 0));
        Component component = Box.createRigidArea(new Dimension(230, 10));
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
                GemsReqReplyMonitor.this.dispose();
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
        jMenu = new JMenu("View");
        jMenu.setMnemonic(86);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("View Selected Request Message..."));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsReqReplyMonitor.this.showRequestMessageFrame();
            }
        });
        jMenuItem = jMenu.add(new JMenuItem("View Selected Reply Message..."));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsReqReplyMonitor.this.showReplyMessageFrame();
            }
        });
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsReqReplyMonitor.this.showRequestMessageFrame();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void dispose() {
        this.stop();
        super.dispose();
    }

    public void showRequestMessageFrame() {
        Message message = this.m_tableModel.getSelectedMessage();
        if (message != null) {
            GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(this.m_cn, false, null, true, null, false);
            gemsMessageFrame.populate(message);
        } else {
            JOptionPane.showMessageDialog(this.m_frame, "Select a Message to view!", "View Request Message", 1);
        }
    }

    public void showReplyMessageFrame() {
        Message message = this.m_tableModel.getSelectedReplyMessage();
        if (message != null) {
            GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(this.m_cn, false, null, true, null, false);
            gemsMessageFrame.populate(message);
        } else {
            JOptionPane.showMessageDialog(this.m_frame, "There is no reply Message to view!", "View Reply Message", 1);
        }
    }

    public void stop() {
        this.m_timer.stop();
        this.m_running = false;
        try {
            if (this.m_replySubscriber != null) {
                this.m_replySubscriber.close();
                this.m_replySubscriber = null;
            }
            if (this.m_subscriber != null) {
                this.m_subscriber.close();
                this.m_subscriber = null;
            }
            if (this.m_replysession != null) {
                this.m_replysession.close();
                this.m_replysession = null;
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
        this.m_reqDest.setEnabled(true);
        this.m_msgsRead.setEnabled(true);
        this.m_startButton.setEnabled(true);
        this.m_stopButton.setEnabled(false);
        this.m_noLimit.setEnabled(true);
        this.m_replyDest.setEnabled(true);
        this.m_destwiz.setEnabled(true);
        this.m_optMenuItem.setEnabled(true);
        this.m_dumpMenuItem.setEnabled(true);
        this.m_pending.clear();
    }

    public void start() {
        this.m_running = true;
        this.m_msgs = 0;
        this.m_reqDest.setEnabled(false);
        this.m_msgsRead.setEnabled(false);
        this.m_startButton.setEnabled(false);
        this.m_stopButton.setEnabled(true);
        this.m_noLimit.setEnabled(false);
        this.m_replyDest.setEnabled(false);
        this.m_destwiz.setEnabled(false);
        this.m_optMenuItem.setEnabled(false);
        this.m_dumpMenuItem.setEnabled(false);
        if (this.m_failCount < 2) {
            this.m_failCount = 2;
        }
        try {
            this.m_maxMsgs = Integer.parseInt(this.m_msgsRead.getText());
        } catch (Exception var1_1) {
            this.m_maxMsgs = 10;
        }
        this.m_tableModel.buildRequestReplyColumnHeaders();
        try {
            Topic topic;
            Topic topic2;
            TibjmsTopicConnectionFactory tibjmsTopicConnectionFactory = new TibjmsTopicConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
            this.m_connection = tibjmsTopicConnectionFactory.createTopicConnection(this.m_cn.m_user, this.m_cn.m_password);
            this.m_session = this.m_connection.createTopicSession(false, 22);
            this.m_replysession = this.m_connection.createTopicSession(false, 22);
            if (this.m_isQueue) {
                topic2 = this.m_session.createTopic("$sys.monitor.Q.r." + this.m_reqDest.getText());
                topic = this.m_replysession.createTopic("$sys.monitor.Q.r." + this.m_replyDest.getText());
            } else {
                topic2 = this.m_session.createTopic("$sys.monitor.T.r." + this.m_reqDest.getText());
                topic = this.m_replysession.createTopic("$sys.monitor.T.r." + this.m_replyDest.getText());
            }
            this.m_replySubscriber = this.m_replysession.createSubscriber(topic);
            this.m_replySubscriber.setMessageListener((MessageListener) new OnSyncReplyMessage());
            this.m_subscriber = this.m_session.createSubscriber(topic2);
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
                int n = jFileChooser.showOpenDialog(GemsReqReplyMonitor.this.m_frame);
                if (n == 0) {
                    File file = jFileChooser.getSelectedFile();
                    GemsReqReplyMonitor.this.m_tableModel.dumpMsgsToFile(file);
                }
            } catch (IOException var2_3) {
                JOptionPane.showMessageDialog(GemsReqReplyMonitor.this.m_frame, var2_3.getMessage(), "Error", 1);
                return;
            }
        }
    }

    class OnSyncReplyMessage
            implements MessageListener {
        OnSyncReplyMessage() {
        }

        public void onMessage(Message message) {
            try {
                if (message == null) {
                    return;
                }
                MapMessage mapMessage = (MapMessage) message;
                if (mapMessage.itemExists("message_bytes")) {
                    String string;
                    Message message2 = Tibjms.createFromBytes((byte[]) mapMessage.getBytes("message_bytes"));
                    if (GemsReqReplyMonitor.this.m_useMonitorMsgTimestamp) {
                        message2.setJMSTimestamp(mapMessage.getJMSTimestamp());
                    }
                    if ((string = message2.getJMSCorrelationID()) != null && string.length() > 0) {
                        GemsReqReplyMonitor.this.m_mutex.acquire();
                        GemsReqReplyMonitor.this.m_pending.put(string, message2);
                        GemsReqReplyMonitor.this.m_mutex.release();
                        Gems.debug("GemsReqReplyMonitor.onRespMessage: CID: " + string);
                    } else {
                        String string2 = message2.getJMSDestination().toString();
                        if (string2 != null && string2.length() > 0) {
                            if (string2.indexOf("$TMP$") >= 0) {
                                GemsReqReplyMonitor.this.m_mutex.acquire();
                                GemsReqReplyMonitor.this.m_pending.put(string2, message2);
                                GemsReqReplyMonitor.this.m_mutex.release();
                            } else {
                                System.err.println("GemsReqReplyMonitor.onRespMessage: invalid response message, non-temporary replies must set JMSCorrelationID");
                            }
                        }
                    }
                }
            } catch (JMSException var2_3) {
                Gems.debug("GemsReqReplyMonitor.onRespMessage: Exception: " + var2_3.toString());
            } catch (InterruptedException var2_4) {
                Gems.debug("GemsReqReplyMonitor.onRespMessage: Exception: " + var2_4.toString());
            }
            GemsReqReplyMonitor.this.m_mutex.release();
        }
    }

    class DestinationWizardAction
            implements ActionListener {
        DestinationWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsReqReplyMonitor.this.m_frame, GemsReqReplyMonitor.this.m_cn, GemsReqReplyMonitor.this.m_isQueue ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsReqReplyMonitor.this.m_reqDest.setText(gemsDestinationPicker.m_retDest.m_destName);
            }
        }
    }

    class EditOptionsAction
            implements ActionListener {
        EditOptionsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            new GemsBrowserOptionsDialog(GemsReqReplyMonitor.this.m_frame, "Edit Options", true);
            GemsReqReplyMonitor.this.m_timer.setDelay(Gems.getGems().getMsgReadDelay());
            GemsReqReplyMonitor.this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
            GemsReqReplyMonitor.this.m_useMonitorMsgTimestamp = Gems.getGems().getUseServerTimestamps();
            GemsReqReplyMonitor.this.m_failCount = Gems.getGems().getRequestReplyTimeout() * 1000 / Gems.getGems().getMsgReadDelay();
            if (GemsReqReplyMonitor.this.m_failCount < 2) {
                GemsReqReplyMonitor.this.m_failCount = 2;
            }
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsReqReplyMonitor.this.stop();
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsReqReplyMonitor.this.start();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        String m_lastmid;
        int m_repeatcount;

        RefreshTimerAction() {
            this.m_lastmid = null;
            this.m_repeatcount = 0;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsReqReplyMonitor.this.m_running) {
                try {
                    MapMessage mapMessage;
                    String string;
                    String string2;
                    Message message;
                    Message message2 = GemsReqReplyMonitor.this.m_subscriber.receiveNoWait();
                    if (message2 != null && (GemsReqReplyMonitor.this.m_noLimit.isSelected() || GemsReqReplyMonitor.this.m_msgs < GemsReqReplyMonitor.this.m_maxMsgs) && (mapMessage = (MapMessage) message2).itemExists("message_bytes")) {
                        message = Tibjms.createFromBytes((byte[]) mapMessage.getBytes("message_bytes"));
                        if (GemsReqReplyMonitor.this.m_useMonitorMsgTimestamp) {
                            message.setJMSTimestamp(mapMessage.getJMSTimestamp());
                        }
                        boolean bl = true;
                        string2 = message.getJMSCorrelationID();
                        string = null;
                        Destination destination = message.getJMSReplyTo();
                        if (destination != null) {
                            string = destination.toString();
                        }
                        Message message3 = null;
                        if (string2 != null && string2.length() > 0) {
                            GemsReqReplyMonitor.this.m_mutex.acquire();
                            message3 = (Message) GemsReqReplyMonitor.this.m_pending.remove(string2);
                            GemsReqReplyMonitor.this.m_mutex.release();
                        } else {
                            if (string != null && string.length() > 0 && string.indexOf("$TMP$") >= 0) {
                                GemsReqReplyMonitor.this.m_mutex.acquire();
                                message3 = (Message) GemsReqReplyMonitor.this.m_pending.remove(string);
                                GemsReqReplyMonitor.this.m_mutex.release();
                            }
                            String string3 = message.getJMSMessageID();
                            if (message3 == null && string3 != null) {
                                Gems.debug("GemsReqReplyMonitor.onReqMessage: checking pending replies for request MsgID:" + string3);
                                GemsReqReplyMonitor.this.m_mutex.acquire();
                                message3 = (Message) GemsReqReplyMonitor.this.m_pending.remove(string3);
                                GemsReqReplyMonitor.this.m_mutex.release();
                            }
                        }
                        if (string == null || string.length() == 0) {
                            Gems.debug("GemsReqReplyMonitor.onReqMessage: request message: " + message.getJMSMessageID() + "  JMSReplyTo property not set");
                            bl = false;
                        }
                        GemsReqReplyMonitor.this.m_tableModel.addRequestMessage(message, message3, GemsReqReplyMonitor.this.m_viewoldestFirst);
                        ++GemsReqReplyMonitor.this.m_msgs;
                    }
                    mapMessage = null;
                    message = null;
                    mapMessage = (MapMessage) GemsReqReplyMonitor.this.m_tableModel.getNextPendingRequest(GemsReqReplyMonitor.this.m_viewoldestFirst);
                    if (mapMessage != null) {
                        String string4 = mapMessage.getJMSMessageID();
                        if (this.m_lastmid != null && string4 != null && this.m_lastmid.equals(string4)) {
                            if ((long) (++this.m_repeatcount) > GemsReqReplyMonitor.this.m_failCount) {
                                GemsReqReplyMonitor.this.m_tableModel.timeoutRequestMessage((Message) mapMessage);
                                this.m_repeatcount = 0;
                                GemsReqReplyMonitor.this.m_timer.restart();
                                return;
                            }
                        } else {
                            this.m_lastmid = mapMessage.getJMSMessageID();
                            this.m_repeatcount = 0;
                        }
                        if ((string2 = mapMessage.getJMSCorrelationID()) != null && string2.length() > 0) {
                            GemsReqReplyMonitor.this.m_mutex.acquire();
                            message = (Message) GemsReqReplyMonitor.this.m_pending.remove(string2);
                            GemsReqReplyMonitor.this.m_mutex.release();
                            if (message != null) {
                                GemsReqReplyMonitor.this.m_tableModel.updateRequestMessage((Message) mapMessage, message);
                            }
                        } else {
                            string = null;
                            if (mapMessage.getJMSReplyTo() != null) {
                                string = mapMessage.getJMSReplyTo().toString();
                            }
                            if (string != null && string.length() > 0 && string.indexOf("$TMP$") >= 0) {
                                GemsReqReplyMonitor.this.m_mutex.acquire();
                                message = (Message) GemsReqReplyMonitor.this.m_pending.remove(string);
                                GemsReqReplyMonitor.this.m_mutex.release();
                                if (message != null) {
                                    GemsReqReplyMonitor.this.m_tableModel.updateRequestMessage((Message) mapMessage, message);
                                }
                            }
                            if (message == null && string4 != null) {
                                Gems.debug("GemsReqReplyMonitor.onReqMessage: checking pending request MsgID: " + string4);
                                GemsReqReplyMonitor.this.m_mutex.acquire();
                                message = (Message) GemsReqReplyMonitor.this.m_pending.remove(string4);
                                GemsReqReplyMonitor.this.m_mutex.release();
                                if (message != null) {
                                    GemsReqReplyMonitor.this.m_tableModel.updateRequestMessage((Message) mapMessage, message);
                                }
                            }
                        }
                    }
                    if (!GemsReqReplyMonitor.this.m_noLimit.isSelected() && GemsReqReplyMonitor.this.m_msgs >= GemsReqReplyMonitor.this.m_maxMsgs && mapMessage == null) {
                        GemsReqReplyMonitor.this.stop();
                    }
                } catch (JMSException var2_3) {
                    System.err.println("Exception: " + var2_3.getMessage());
                    GemsReqReplyMonitor.this.stop();
                } catch (InterruptedException var2_4) {
                    Gems.debug("GemsReqReplyMonitor.Exception: " + var2_4.toString());
                }
            }
        }
    }

}

