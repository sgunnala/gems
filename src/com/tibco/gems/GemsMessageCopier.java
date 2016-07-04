/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.Tibjms
 *  com.tibco.tibjms.TibjmsQueueConnectionFactory
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.MessageProducer
 *  javax.jms.Queue
 *  javax.jms.QueueConnection
 *  javax.jms.Session
 *  javax.jms.Topic
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.swing.*;

import com.tibco.tibjms.Tibjms;
import com.tibco.tibjms.TibjmsQueueConnectionFactory;

public class GemsMessageCopier
        extends JDialog {
    protected JComboBox m_conn;
    protected JComboBox m_type;
    protected JTextField m_dest;
    protected JTextArea m_results;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JButton m_lookup;
    protected Hashtable m_connTable;
    protected Vector m_msgs;
    JFrame m_frame;
    JPanel m_panel;
    boolean m_running = false;
    Session m_session = null;
    GemsConnectionNode m_cn;
    QueueConnection m_connection = null;
    Timer m_timer;
    int m_msgsNum;
    Destination m_jmsdest;
    MessageProducer m_msgProducer;

    public GemsMessageCopier(JFrame jFrame, GemsConnectionNode gemsConnectionNode, Vector vector) {
        super(jFrame, "Copy Selected Messages To:", true);

        this.m_timer = new Timer(100, new RefreshTimerAction());
        this.m_msgsNum = 0;
        this.m_jmsdest = null;
        this.m_msgProducer = null;
        this.setLocationRelativeTo(jFrame);
        this.setDefaultCloseOperation(2);
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_msgs = vector;
        JMenuBar jMenuBar = this.constructMenuBar();
        this.setJMenuBar(jMenuBar);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add((Component) jPanel2, "North");
        this.m_connTable = GemsConnectionNode.getConnections();
        JLabel jLabel = new JLabel("EMS Server:", 11);
        this.m_conn = new JComboBox();
        Enumeration enumeration = this.m_connTable.keys();
        ArrayList<String> arrayList = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            String s = (String) enumeration.nextElement();
            arrayList.add((String) s);
            Collections.sort(arrayList);
            this.m_conn.insertItemAt(s, arrayList.indexOf(s));
            if (!s.startsWith(gemsConnectionNode.getName())) continue;
            this.m_conn.setSelectedItem(s);
        }
        if (this.m_conn.getSelectedItem() == null) {
            this.m_conn.setSelectedIndex(0);
        }
        this.m_conn.setMaximumSize(new Dimension(0, 24));
        jLabel.setLabelFor(this.m_conn);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_conn);
        JLabel jLabel1 = new JLabel("Destination Type:", 11);
        this.m_type = new JComboBox();
        this.m_type.addItem("Queue");
        this.m_type.addItem("Topic");
        this.m_type.setMaximumSize(new Dimension(0, 24));
        jLabel1.setLabelFor(this.m_type);
        jPanel2.add((Component) jLabel1);
        jPanel2.add(this.m_type);
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        jPanel3.setMinimumSize(new Dimension(300, 24));
        JLabel jLabel2 = new JLabel("Destination Name:", 11);
        this.m_dest = new JTextField("", 32);
        jLabel2.setLabelFor(this.m_dest);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_dest);
        this.m_lookup = new JButton("LookUp...");
        this.m_lookup.addActionListener(new LookupAction());
        jPanel3.add(this.m_lookup);
        jPanel2.add(jPanel3);
        this.m_results = new JTextArea("");
        this.m_results.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(this.m_results);
        jScrollPane.setPreferredSize(new Dimension(430, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        Component component = Box.createRigidArea(new Dimension(175, 10));
        jPanel4.add(component);
        this.m_startButton = new JButton("Start");
        this.m_startButton.addActionListener(new StartPressed());
        this.m_stopButton = new JButton("Stop");
        this.m_stopButton.setEnabled(false);
        this.m_stopButton.addActionListener(new StopPressed());
        jPanel4.add(this.m_startButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel4.add(component);
        jPanel4.add(this.m_stopButton);
        jPanel.add((Component) jPanel4, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 3, 2, 5, 5, 5, 5);
        this.pack();
        this.show();
    }

    private JMenuBar constructMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenu.setMnemonic(70);
        jMenuBar.add(jMenu);
        JMenuItem jMenuItem = jMenu.add(new JMenuItem("Exit"));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsMessageCopier.this.dispose();
            }
        });
        return jMenuBar;
    }

    public void dispose() {
        this.stop();
        super.dispose();
    }

    public void stop() {
        this.m_timer.stop();
        this.m_running = false;
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
        this.m_dest.setEnabled(true);
        this.m_startButton.setEnabled(true);
        this.m_stopButton.setEnabled(false);
        this.m_conn.setEnabled(true);
        this.m_type.setEnabled(true);
    }

    public void start() {
        if (this.m_dest.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Enter a destination to copy to", "Error", 1);
            return;
        }
        this.m_running = true;
        this.m_conn.setEnabled(false);
        this.m_type.setEnabled(false);
        this.m_startButton.setEnabled(false);
        this.m_stopButton.setEnabled(true);
        this.m_dest.setEnabled(false);
        this.m_results.setText("");
        try {
            GemsConnectionNode gemsConnectionNode = (GemsConnectionNode) this.m_connTable.get(this.m_conn.getSelectedItem());
            this.m_results.append("Connecting to: " + this.m_conn.getSelectedItem() + "\n");
            TibjmsQueueConnectionFactory tibjmsQueueConnectionFactory = new TibjmsQueueConnectionFactory(gemsConnectionNode.m_url, null, gemsConnectionNode.m_sslParams);
            this.m_connection = tibjmsQueueConnectionFactory.createQueueConnection(gemsConnectionNode.m_user, gemsConnectionNode.m_password);
            this.m_results.append("Creating transacted session\n");
            this.m_session = this.m_connection.createSession(true, 1);
            if (this.m_type.getSelectedItem().equals("Queue")) {
                this.m_results.append("Creating producer to queue: " + this.m_dest.getText() + "\n");
                this.m_jmsdest = this.m_session.createQueue(this.m_dest.getText());
            } else {
                this.m_results.append("Creating producer to topic: " + this.m_dest.getText() + "\n");
                this.m_jmsdest = this.m_session.createTopic(this.m_dest.getText());
            }
            this.m_msgProducer = this.m_session.createProducer(null);
            this.m_connection.start();
            this.m_msgsNum = this.m_msgs.size();
            this.m_timer.start();
        } catch (Exception var1_2) {
            this.m_results.append("Exception: " + var1_2.getMessage() + "\n");
            this.stop();
        }
    }

    class LookupAction
            implements ActionListener {
        LookupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = (GemsConnectionNode) GemsMessageCopier.this.m_connTable.get(GemsMessageCopier.this.m_conn.getSelectedItem());
            if (gemsConnectionNode == null) {
                return;
            }
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsMessageCopier.this.m_frame, gemsConnectionNode, GemsMessageCopier.this.m_type.getSelectedItem().equals("Queue") ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsMessageCopier.this.m_dest.setText(gemsDestinationPicker.m_retDest.m_destName);
            }
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageCopier.this.stop();
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageCopier.this.start();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsMessageCopier.this.m_running) {
                try {
                    if (GemsMessageCopier.this.m_msgsNum <= 0) {
                        GemsMessageCopier.this.m_results.append("Commiting messages \n");
                        GemsMessageCopier.this.m_session.commit();
                        GemsMessageCopier.this.m_results.append("Done\n");
                        GemsMessageCopier.this.stop();
                    } else {
                        Message message = (Message) GemsMessageCopier.this.m_msgs.elementAt(--GemsMessageCopier.this.m_msgsNum);
                        GemsMessageCopier.this.m_results.append("Copying message: " + message.getJMSMessageID() + "\n");
                        Message message2 = Tibjms.createFromBytes((byte[]) Tibjms.getAsBytes((Message) message));
                        if (message != null) {
                            GemsMessageCopier.this.m_msgProducer.send(GemsMessageCopier.this.m_jmsdest, message2, message.getJMSDeliveryMode(), message.getJMSPriority(), message.getJMSExpiration());
                            GemsMessageCopier.this.m_results.append("Message sent as: " + message2.getJMSMessageID() + "\n");
                        }
                    }
                } catch (JMSException var2_3) {
                    GemsMessageCopier.this.m_results.append("Exception: " + var2_3.getMessage() + "\n");
                    GemsMessageCopier.this.m_results.append("Rolling back");
                    try {
                        GemsMessageCopier.this.m_session.rollback();
                    } catch (JMSException var3_5) {
                        GemsMessageCopier.this.m_results.append("Exception: " + var3_5.getMessage() + "\n");
                    }
                    GemsMessageCopier.this.stop();
                }
            }
        }
    }

}

