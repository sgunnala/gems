/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.TibjmsConnectionFactory
 *  javax.jms.Connection
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.MessageConsumer
 *  javax.jms.MessageListener
 *  javax.jms.MessageProducer
 *  javax.jms.Queue
 *  javax.jms.Session
 *  javax.jms.TemporaryQueue
 *  javax.jms.TemporaryTopic
 *  javax.jms.Topic
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

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import com.tibco.tibjms.TibjmsConnectionFactory;

public class GemsReqReplyTester
        extends JFrame {
    public Hashtable m_pending = new Hashtable();
    protected JTextField m_conn;
    protected JTextField m_reqDest;
    protected JTextField m_msgsSend;
    protected JTextField m_delay;
    protected JComboBox m_mode;
    protected JComboBox m_msgType;
    protected JButton m_destwiz;
    protected JButton m_msgwiz;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JCheckBox m_noDelay;
    protected GemsMessageFrame m_textMsgFrame;
    protected GemsMessageFrame m_mapMsgFrame;
    protected GemsMessageTableModel m_tableModel;
    protected boolean m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
    protected long m_RRTimeout = Gems.getGems().getRequestReplyTimeout() * 1000;
    protected JMenuItem m_optMenuItem;
    protected JMenuItem m_dumpMenuItem;
    GemsReqReplyTester m_frame;
    JPanel m_panel;
    boolean m_running = false;
    int m_msgCount = 0;
    int m_msgSeq = 0;
    int m_maxMsgs = 10;
    Session m_session = null;
    Session m_replysession = null;
    GemsConnectionNode m_cn;
    Connection m_connection = null;
    MessageProducer m_producer = null;
    MessageConsumer m_consumer = null;
    Destination m_requestDestination = null;
    Destination m_replyDestination = null;
    String m_destination = null;
    Timer m_timer = null;
    Timer m_stoptimer = null;
    SyncThread m_thread = null;
    JTable m_table;
    TableSorter m_sorter;
    boolean m_isQueue = true;
    Mutex m_mutex = new Mutex();

    public GemsReqReplyTester(GemsConnectionNode gemsConnectionNode, String string, boolean bl, String string2) {
        super(Gems.getGems().getTitlePrefix() + string2);
        this.setLocation(400, 175);
        this.setDefaultCloseOperation(2);
        this.m_frame = this;
        this.m_cn = gemsConnectionNode;
        this.m_isQueue = bl;
        this.m_destination = string;
        String string3 = bl ? "Queue" : "Topic";
        this.m_mapMsgFrame = new GemsMessageFrame(this.m_cn, true, string, this.m_isQueue, this.m_frame, false, true, true);
        this.m_textMsgFrame = new GemsMessageFrame(this.m_cn, true, string, this.m_isQueue, this.m_frame, false, false, true);
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
        JLabel jLabel3 = new JLabel("Mode:", 11);
        this.m_mode = new JComboBox();
        for (MODE_TYPE mODE_TYPE : MODE_TYPE.values()) {
            this.m_mode.addItem(mODE_TYPE);
        }
        jLabel3.setLabelFor(this.m_mode);
        jPanel2.add(jLabel3);
        jPanel2.add(this.m_mode);
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        JLabel jLabel4 = new JLabel("Request Message:", 11);
        this.m_msgType = new JComboBox();
        for (MSG_TYPE object2 : MSG_TYPE.values()) {
            this.m_msgType.addItem(object2);
        }
        jLabel4.setLabelFor(this.m_msgType);
        jPanel2.add(jLabel4);
        this.m_msgwiz = new JButton("...");
        this.m_msgwiz.setPreferredSize(new Dimension(18, 16));
        this.m_msgwiz.addActionListener(new MessageWizardAction());
        jPanel4.add(this.m_msgType);
        jPanel4.add(this.m_msgwiz);
        jPanel2.add(jPanel4);
        JLabel jLabel5 = new JLabel("Requests to Send:", 11);
        this.m_msgsSend = new JTextField("10", 20);
        this.m_msgsSend.setMinimumSize(new Dimension(40, 24));
        jLabel5.setLabelFor(this.m_msgsSend);
        jPanel2.add(jLabel5);
        jPanel2.add(this.m_msgsSend);
        JPanel jPanel5 = new JPanel(true);
        jPanel5.setLayout(new BoxLayout(jPanel5, 0));
        JLabel jLabel6 = new JLabel("Delay Between Sends(ms):", 11);
        this.m_delay = new JTextField("200", 20);
        this.m_delay.setMinimumSize(new Dimension(40, 24));
        jPanel5.setPreferredSize(new Dimension(100, 24));
        jLabel6.setLabelFor(this.m_delay);
        jPanel2.add(jLabel6);
        jPanel5.add(this.m_delay);
        this.m_noDelay = new JCheckBox("None", false);
        jPanel5.add(this.m_noDelay);
        jPanel2.add(jPanel5);
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
        JPanel jPanel6 = new JPanel(true);
        jPanel6.setLayout(new BoxLayout(jPanel6, 0));
        Component component = Box.createRigidArea(new Dimension(280, 10));
        jPanel6.add(component);
        this.m_startButton = new JButton("Start");
        this.m_startButton.addActionListener(new StartPressed());
        this.m_stopButton = new JButton("Stop");
        this.m_stopButton.addActionListener(new StopPressed());
        this.m_stopButton.setEnabled(false);
        jPanel6.add(this.m_startButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel6.add(component);
        jPanel6.add(this.m_stopButton);
        jPanel.add((Component) jPanel6, "South");
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
                GemsReqReplyTester.this.dispose();
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
                GemsReqReplyTester.this.showRequestMessageFrame();
            }
        });
        jMenuItem = jMenu.add(new JMenuItem("View Selected Reply Message..."));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsReqReplyTester.this.showReplyMessageFrame();
            }
        });
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsReqReplyTester.this.showReplyMessageFrame();
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
        if (!this.m_running) {
            return;
        }
        try {
            this.m_thread = null;
            if (this.m_timer != null) {
                this.m_timer.stop();
            }
            this.m_running = false;
            if (this.m_consumer != null) {
                this.m_consumer.close();
                this.m_consumer = null;
            }
            if (this.m_replyDestination != null) {
                if (this.m_isQueue) {
                    ((TemporaryQueue) this.m_replyDestination).delete();
                } else {
                    ((TemporaryTopic) this.m_replyDestination).delete();
                }
                this.m_replyDestination = null;
            }
            if (this.m_producer != null) {
                this.m_producer.close();
                this.m_producer = null;
            }
            if (this.m_session != null) {
                this.m_session.close();
                this.m_session = null;
            }
            if (this.m_connection != null) {
                this.m_connection.close();
                this.m_connection = null;
            }
            this.m_reqDest.setEnabled(true);
            this.m_msgsSend.setEnabled(true);
            this.m_startButton.setEnabled(true);
            this.m_stopButton.setEnabled(false);
            this.m_noDelay.setEnabled(true);
            this.m_delay.setEnabled(true);
            this.m_mode.setEnabled(true);
            this.m_destwiz.setEnabled(true);
            this.m_msgwiz.setEnabled(true);
            this.m_msgType.setEnabled(true);
            this.m_optMenuItem.setEnabled(true);
            this.m_dumpMenuItem.setEnabled(true);
            this.m_mutex.acquire();
            this.m_pending.clear();
            this.m_mutex.release();
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
        }
    }

    public void start() {
        this.m_running = true;
        this.m_msgCount = 0;
        this.m_reqDest.setEnabled(false);
        this.m_msgsSend.setEnabled(false);
        this.m_startButton.setEnabled(false);
        this.m_stopButton.setEnabled(true);
        this.m_noDelay.setEnabled(false);
        this.m_delay.setEnabled(false);
        this.m_mode.setEnabled(false);
        this.m_msgType.setEnabled(false);
        this.m_destwiz.setEnabled(false);
        this.m_msgwiz.setEnabled(false);
        this.m_optMenuItem.setEnabled(false);
        this.m_dumpMenuItem.setEnabled(false);
        int n = 0;
        try {
            this.m_maxMsgs = Integer.parseInt(this.m_msgsSend.getText());
            n = Integer.parseInt(this.m_delay.getText());
        } catch (Exception var2_2) {
            this.m_maxMsgs = 10;
            n = 200;
        }
        if (this.m_noDelay.isSelected()) {
            n = 0;
        }
        this.m_tableModel.buildRequestReplyColumnHeaders();
        if (this.m_reqDest.getText() == null || this.m_reqDest.getText().length() == 0) {
            this.stop();
            return;
        }
        try {
            TibjmsConnectionFactory tibjmsConnectionFactory = new TibjmsConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
            this.m_connection = tibjmsConnectionFactory.createConnection(this.m_cn.m_user, this.m_cn.m_password);
            this.m_session = this.m_connection.createSession(false, 22);
            if (this.m_isQueue) {
                this.m_requestDestination = this.m_session.createQueue(this.m_reqDest.getText());
                if (this.m_mode.getSelectedItem() == MODE_TYPE.Asynchronous) {
                    this.m_replyDestination = this.m_session.createTemporaryQueue();
                }
            } else {
                this.m_requestDestination = this.m_session.createTopic(this.m_reqDest.getText());
                if (this.m_mode.getSelectedItem() == MODE_TYPE.Asynchronous) {
                    this.m_replyDestination = this.m_session.createTemporaryTopic();
                }
            }
            if (this.m_mode.getSelectedItem() == MODE_TYPE.Asynchronous) {
                this.m_consumer = this.m_session.createConsumer(this.m_replyDestination);
                this.m_consumer.setMessageListener((MessageListener) new OnAsyncReplyMessage());
            }
            this.m_producer = this.m_session.createProducer(null);
            this.m_connection.start();
            if (this.m_mode.getSelectedItem() == MODE_TYPE.Asynchronous) {
                if (this.m_timer == null) {
                    this.m_timer = new Timer(n, new RefreshTimerAction());
                } else {
                    this.m_timer.setDelay(n);
                }
                this.m_timer.setInitialDelay(200);
                this.m_timer.start();
            } else {
                this.m_thread = new SyncThread(n);
                this.m_thread.start();
            }
        } catch (Exception var2_4) {
            JOptionPane.showMessageDialog(this.m_frame, var2_4.getMessage(), "Error", 1);
            this.stop();
        }
    }

    public void updateByTimer(Message message, Message message2) {
        Timer timer = new Timer(0, new UpdateTimerAction(message, message2));
        timer.setRepeats(false);
        timer.start();
    }

    public void stopByTimer() {
        if (this.m_stoptimer == null) {
            this.m_stoptimer = new Timer(0, new StopTimerAction());
        }
        this.m_stoptimer.setRepeats(false);
        this.m_stoptimer.start();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum MSG_TYPE {
        TextMessage,
        MapMessage;


        private MSG_TYPE() {
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum MODE_TYPE {
        Synchronous,
        Asynchronous;


        private MODE_TYPE() {
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
                int n = jFileChooser.showOpenDialog(GemsReqReplyTester.this.m_frame);
                if (n == 0) {
                    File file = jFileChooser.getSelectedFile();
                    GemsReqReplyTester.this.m_tableModel.dumpMsgsToFile(file);
                }
            } catch (IOException var2_3) {
                JOptionPane.showMessageDialog(GemsReqReplyTester.this.m_frame, var2_3.getMessage(), "Error", 1);
                return;
            }
        }
    }

    class OnAsyncReplyMessage
            implements MessageListener {
        OnAsyncReplyMessage() {
        }

        public void onMessage(Message message) {
            try {
                if (message == null) {
                    return;
                }
                String string = message.getJMSCorrelationID();
                if (string == null || string.length() == 0) {
                    System.err.println("GemsReqReplyTester.onAsyncReplyMessage: JMSCorrelationID not returned by receiver, try using Synch mode");
                    return;
                }
                GemsReqReplyTester.this.m_mutex.acquire();
                Message message2 = (Message) GemsReqReplyTester.this.m_pending.remove(string);
                GemsReqReplyTester.this.m_mutex.release();
                if (message2 == null) {
                    System.err.println("GemsReqReplyTester.onAsyncReplyMessage: No pending message for CID: " + string);
                    return;
                }
                GemsReqReplyTester.this.m_tableModel.updateRequestMessage(message2, message);
            } catch (JMSException var2_3) {
                Gems.debug("GemsReqReplyTester.onAsyncReplyMessage: Exception: " + var2_3.toString());
            } catch (InterruptedException var2_4) {
                Gems.debug("GemsReqReplyTester.onRespMessage: Exception: " + var2_4.toString());
            }
        }
    }

    class MessageWizardAction
            implements ActionListener {
        MessageWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsReqReplyTester.this.m_msgType.getSelectedItem() == MSG_TYPE.TextMessage) {
                if (GemsReqReplyTester.this.m_textMsgFrame == null) {
                    GemsReqReplyTester.this.m_textMsgFrame = new GemsMessageFrame(GemsReqReplyTester.this.m_cn, true, GemsReqReplyTester.this.m_destination, GemsReqReplyTester.this.m_isQueue, GemsReqReplyTester.this.m_frame, false, false, true);
                }
                GemsReqReplyTester.this.m_textMsgFrame.show();
            } else {
                if (GemsReqReplyTester.this.m_mapMsgFrame == null) {
                    GemsReqReplyTester.this.m_mapMsgFrame = new GemsMessageFrame(GemsReqReplyTester.this.m_cn, true, GemsReqReplyTester.this.m_destination, GemsReqReplyTester.this.m_isQueue, GemsReqReplyTester.this.m_frame, false, true, true);
                }
                GemsReqReplyTester.this.m_mapMsgFrame.show();
            }
        }
    }

    class DestinationWizardAction
            implements ActionListener {
        DestinationWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsReqReplyTester.this.m_frame, GemsReqReplyTester.this.m_cn, GemsReqReplyTester.this.m_isQueue ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsReqReplyTester.this.m_destination = gemsDestinationPicker.m_retDest.m_destName;
                GemsReqReplyTester.this.m_reqDest.setText(GemsReqReplyTester.this.m_destination);
            }
        }
    }

    class EditOptionsAction
            implements ActionListener {
        EditOptionsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            new GemsBrowserOptionsDialog(GemsReqReplyTester.this.m_frame, "Edit Options", false, true, false);
            GemsReqReplyTester.this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
            GemsReqReplyTester.this.m_RRTimeout = Gems.getGems().getRequestReplyTimeout() * 1000;
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsReqReplyTester.this.stop();
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsReqReplyTester.this.start();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsReqReplyTester.this.m_running && GemsReqReplyTester.this.m_msgCount < GemsReqReplyTester.this.m_maxMsgs) {
                try {
                    Message message = GemsReqReplyTester.this.m_msgType.getSelectedItem() == MSG_TYPE.TextMessage ? GemsReqReplyTester.this.m_textMsgFrame.createMessage(GemsReqReplyTester.this.m_session) : GemsReqReplyTester.this.m_mapMsgFrame.createMessage(GemsReqReplyTester.this.m_session);
                    message.setJMSReplyTo(GemsReqReplyTester.this.m_replyDestination);
                    if (GemsReqReplyTester.this.m_mode.getSelectedItem() == MODE_TYPE.Synchronous) {
                        GemsReqReplyTester.this.m_replyDestination = GemsReqReplyTester.this.m_isQueue ? GemsReqReplyTester.this.m_session.createTemporaryQueue() : GemsReqReplyTester.this.m_session.createTemporaryTopic();
                        GemsReqReplyTester.this.m_consumer = GemsReqReplyTester.this.m_session.createConsumer(GemsReqReplyTester.this.m_replyDestination);
                    } else {
                        message.setJMSCorrelationID(String.valueOf(++GemsReqReplyTester.this.m_msgSeq));
                    }
                    GemsReqReplyTester.this.m_producer.send(GemsReqReplyTester.this.m_requestDestination, message, message.getJMSDeliveryMode(), message.getJMSPriority(), message.getJMSExpiration());
                    ++GemsReqReplyTester.this.m_msgCount;
                    if (GemsReqReplyTester.this.m_mode.getSelectedItem() == MODE_TYPE.Asynchronous) {
                        GemsReqReplyTester.this.m_mutex.acquire();
                        GemsReqReplyTester.this.m_pending.put(message.getJMSCorrelationID(), message);
                        GemsReqReplyTester.this.m_mutex.release();
                    }
                    GemsReqReplyTester.this.m_tableModel.addRequestMessage(message, null, GemsReqReplyTester.this.m_viewoldestFirst);
                    if (GemsReqReplyTester.this.m_mode.getSelectedItem() == MODE_TYPE.Synchronous) {
                        Message message2 = GemsReqReplyTester.this.m_consumer.receive(GemsReqReplyTester.this.m_RRTimeout);
                        if (message2 != null) {
                            GemsReqReplyTester.this.m_tableModel.updateRequestMessage(message, message2);
                        } else {
                            GemsReqReplyTester.this.m_tableModel.timeoutRequestMessage(message);
                        }
                        GemsReqReplyTester.this.m_consumer.close();
                        if (GemsReqReplyTester.this.m_isQueue) {
                            ((TemporaryQueue) GemsReqReplyTester.this.m_replyDestination).delete();
                        } else {
                            ((TemporaryTopic) GemsReqReplyTester.this.m_replyDestination).delete();
                        }
                        GemsReqReplyTester.this.m_replyDestination = null;
                        GemsReqReplyTester.this.m_consumer = null;
                    }
                } catch (JMSException var2_3) {
                    System.err.println("Exception: " + var2_3.getMessage());
                    GemsReqReplyTester.this.m_frame.stop();
                } catch (InterruptedException var2_4) {
                    Gems.debug("GemsReqReplyTester.Exception: " + var2_4.toString());
                }
            } else {
                GemsReqReplyTester.this.m_frame.stop();
            }
        }
    }

    class StopTimerAction
            implements ActionListener {
        StopTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsReqReplyTester.this.m_frame.stop();
        }
    }

    class UpdateTimerAction
            implements ActionListener {
        Message msg;
        Message rep;

        public UpdateTimerAction(Message message, Message message2) {
            this.msg = message;
            this.rep = message2;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                GemsReqReplyTester.this.m_frame.m_mutex.acquire();
                if (this.rep != null) {
                    GemsReqReplyTester.this.m_frame.m_tableModel.updateRequestMessage(this.msg, this.rep);
                } else {
                    GemsReqReplyTester.this.m_frame.m_tableModel.timeoutRequestMessage(this.msg);
                }
                GemsReqReplyTester.this.m_frame.m_mutex.release();
            } catch (Exception var2_2) {
                GemsReqReplyTester.this.m_frame.m_mutex.release();
            }
        }
    }

    class SyncThread
            extends Thread {
        long m_delay;

        SyncThread(long l) {
            this.m_delay = l;
        }

        public void run() {
            while (GemsReqReplyTester.this.m_running && GemsReqReplyTester.this.m_msgCount < GemsReqReplyTester.this.m_maxMsgs) {
                try {
                    Message message = GemsReqReplyTester.this.m_msgType.getSelectedItem() == MSG_TYPE.TextMessage ? GemsReqReplyTester.this.m_textMsgFrame.createMessage(GemsReqReplyTester.this.m_session) : GemsReqReplyTester.this.m_mapMsgFrame.createMessage(GemsReqReplyTester.this.m_session);
                    if (GemsReqReplyTester.this.m_mode.getSelectedItem() == MODE_TYPE.Synchronous) {
                        GemsReqReplyTester.this.m_replyDestination = GemsReqReplyTester.this.m_isQueue ? GemsReqReplyTester.this.m_session.createTemporaryQueue() : GemsReqReplyTester.this.m_session.createTemporaryTopic();
                        GemsReqReplyTester.this.m_consumer = GemsReqReplyTester.this.m_session.createConsumer(GemsReqReplyTester.this.m_replyDestination);
                    } else {
                        message.setJMSCorrelationID(String.valueOf(++GemsReqReplyTester.this.m_msgSeq));
                    }
                    message.setJMSReplyTo(GemsReqReplyTester.this.m_replyDestination);
                    GemsReqReplyTester.this.m_producer.send(GemsReqReplyTester.this.m_requestDestination, message, message.getJMSDeliveryMode(), message.getJMSPriority(), message.getJMSExpiration());
                    ++GemsReqReplyTester.this.m_msgCount;
                    if (GemsReqReplyTester.this.m_mode.getSelectedItem() == MODE_TYPE.Asynchronous) {
                        GemsReqReplyTester.this.m_mutex.acquire();
                        GemsReqReplyTester.this.m_pending.put(message.getJMSCorrelationID(), message);
                        GemsReqReplyTester.this.m_mutex.release();
                    }
                    GemsReqReplyTester.this.m_tableModel.addRequestMessage(message, null, GemsReqReplyTester.this.m_viewoldestFirst);
                    if (GemsReqReplyTester.this.m_mode.getSelectedItem() == MODE_TYPE.Synchronous) {
                        Message message2 = GemsReqReplyTester.this.m_consumer.receive(GemsReqReplyTester.this.m_RRTimeout);
                        GemsReqReplyTester.this.updateByTimer(message, message2);
                        if (GemsReqReplyTester.this.m_consumer != null) {
                            GemsReqReplyTester.this.m_consumer.close();
                            GemsReqReplyTester.this.m_consumer = null;
                        }
                        if (GemsReqReplyTester.this.m_replyDestination != null) {
                            if (GemsReqReplyTester.this.m_isQueue) {
                                ((TemporaryQueue) GemsReqReplyTester.this.m_replyDestination).delete();
                            } else {
                                ((TemporaryTopic) GemsReqReplyTester.this.m_replyDestination).delete();
                            }
                        }
                        GemsReqReplyTester.this.m_replyDestination = null;
                    }
                } catch (JMSException var1_2) {
                    System.err.println("Exception: " + var1_2.getMessage());
                    GemsReqReplyTester.this.m_frame.stop();
                    return;
                } catch (InterruptedException var1_3) {
                    Gems.debug("GemsReqReplyTester.Exception: " + var1_3.toString());
                }
                try {
                    SyncThread.sleep(this.m_delay);
                } catch (Exception var1_4) {
                    System.err.println("Exception: " + var1_4.getMessage());
                }
            }
            GemsReqReplyTester.this.m_frame.stop();
        }
    }

}

