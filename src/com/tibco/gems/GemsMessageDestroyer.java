/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.TibjmsQueueConnectionFactory
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.Queue
 *  javax.jms.QueueConnection
 *  javax.jms.QueueReceiver
 *  javax.jms.QueueSession
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.swing.*;

import com.tibco.tibjms.TibjmsQueueConnectionFactory;

public class GemsMessageDestroyer
        extends JDialog {
    protected JTextField m_conn;
    protected JTextArea m_results;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JRadioButton m_adminAPIButton;
    protected JRadioButton m_JMSReadButton;
    protected Vector m_msgs;
    protected String m_queue;
    protected boolean m_adminAPI;
    Frame m_frame;
    JPanel m_panel;
    boolean m_running = false;
    boolean m_started = false;
    GemsConnectionNode m_cn;
    Timer m_timer;
    int m_msgsNum;
    QueueSession m_session;
    QueueConnection m_connection;

    public GemsMessageDestroyer(Frame frame, GemsConnectionNode gemsConnectionNode, Vector vector, String string) {
        super(frame, "Destroy Selected Messages:", true);
        this.m_timer = new Timer(100, new RefreshTimerAction());
        this.m_adminAPI = true;
        this.m_msgsNum = 0;
        this.m_session = null;
        this.m_connection = null;
        this.m_queue = string;
        this.setLocationRelativeTo(frame);
        this.setDefaultCloseOperation(2);
        this.m_frame = frame;
        this.m_cn = gemsConnectionNode;
        this.m_msgs = vector;
        JMenuBar jMenuBar = this.constructMenuBar();
        this.setJMenuBar(jMenuBar);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add((Component) jPanel2, "North");
        JLabel jLabel = new JLabel("EMS Server:", 11);
        this.m_conn = new JTextField(this.m_cn.getName(), 32);
        this.m_conn.setEditable(false);
        jLabel.setLabelFor(this.m_conn);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_conn);
        this.m_adminAPIButton = new JRadioButton("Use Admin API (requires track msg ids)");
        this.m_adminAPIButton.addActionListener(new adminAPIPressed());
        this.m_adminAPIButton.setSelected(true);
        this.m_JMSReadButton = new JRadioButton("Use JMS Read");
        this.m_JMSReadButton.addActionListener(new JMSReadPressed());
        jPanel2.add(this.m_JMSReadButton);
        jPanel2.add(this.m_adminAPIButton);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.m_JMSReadButton);
        buttonGroup.add(this.m_adminAPIButton);
        this.m_results = new JTextArea("");
        this.m_results.append("Press Start to destroy " + this.m_msgs.size() + " message(s) on EMS: " + this.m_cn.getName() + "\n");
        this.m_results.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(this.m_results);
        jScrollPane.setPreferredSize(new Dimension(430, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        Component component = Box.createRigidArea(new Dimension(175, 10));
        jPanel3.add(component);
        this.m_startButton = new JButton("Start");
        this.m_startButton.addActionListener(new StartPressed());
        this.m_stopButton = new JButton("Close");
        this.m_stopButton.addActionListener(new StopPressed());
        jPanel3.add(this.m_startButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel3.add(component);
        jPanel3.add(this.m_stopButton);
        jPanel.add((Component) jPanel3, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 2, 2, 5, 5, 5, 5);
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
                GemsMessageDestroyer.this.dispose();
            }
        });
        return jMenuBar;
    }

    public void dispose() {
        this.stop(false);
        super.dispose();
    }

    public void stop(boolean bl) {
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
        } catch (JMSException var2_2) {
            System.err.println("Exception: " + var2_2.getMessage());
        }
        if (bl) {
            this.m_startButton.setEnabled(true);
            this.m_startButton.setText("Continue");
        }
        this.m_stopButton.setText("Close");
    }

    public void start() {
        this.m_running = true;
        this.m_startButton.setEnabled(false);
        this.m_adminAPIButton.setEnabled(false);
        this.m_JMSReadButton.setEnabled(false);
        this.m_stopButton.setText("Stop");
        if (!this.m_started) {
            this.m_msgsNum = this.m_msgs.size();
            this.m_started = true;
        }
        if (this.m_adminAPI) {
            this.m_timer.start();
        } else {
            try {
                TibjmsQueueConnectionFactory tibjmsQueueConnectionFactory = new TibjmsQueueConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
                this.m_connection = tibjmsQueueConnectionFactory.createQueueConnection(this.m_cn.m_user, this.m_cn.m_password);
                this.m_session = this.m_connection.createQueueSession(false, 1);
                this.m_connection.start();
                this.m_timer.start();
            } catch (JMSException var1_2) {
                JOptionPane.showMessageDialog(this.m_frame, var1_2.getMessage(), "Error", 1);
                this.stop(false);
            }
        }
    }

    public void reset() {
        this.m_started = false;
        this.m_startButton.setText("Start");
    }

    class adminAPIPressed
            implements ActionListener {
        adminAPIPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageDestroyer.this.m_adminAPI = true;
        }
    }

    class JMSReadPressed
            implements ActionListener {
        JMSReadPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageDestroyer.this.m_adminAPI = false;
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsMessageDestroyer.this.m_stopButton.getText().equals("Close")) {
                GemsMessageDestroyer.this.dispose();
            } else {
                GemsMessageDestroyer.this.stop(true);
            }
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageDestroyer.this.start();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsMessageDestroyer.this.m_running) {
                try {
                    Message message;
                    if (GemsMessageDestroyer.this.m_msgsNum <= 0) {
                        GemsMessageDestroyer.this.m_results.append("Done\n");
                        GemsMessageDestroyer.this.stop(false);
                    } else if ((message = (Message) GemsMessageDestroyer.this.m_msgs.elementAt(--GemsMessageDestroyer.this.m_msgsNum)) != null) {
                        GemsMessageDestroyer.this.m_results.append("Destroying message: " + message.getJMSMessageID() + "\n");
                        String string = message.getJMSMessageID();
                        if (string == null || string.length() == 0 || string.startsWith("tmpId")) {
                            GemsMessageDestroyer.this.m_results.append("Error: Cannot destroy message. Message has no JMSMessageID\n");
                            GemsMessageDestroyer.this.stop(true);
                        } else if (GemsMessageDestroyer.this.m_adminAPI) {
                            GemsMessageDestroyer.this.m_cn.removeMessage(message);
                        } else {
                            Queue queue = GemsMessageDestroyer.this.m_session.createQueue(GemsMessageDestroyer.this.m_queue);
                            QueueReceiver queueReceiver = GemsMessageDestroyer.this.m_session.createReceiver(queue, "JMSMessageID='" + string + "'");
                            Message message2 = queueReceiver.receive(2000);
                            if (message2 != null) {
                                GemsMessageDestroyer.this.m_results.append(string + " successfully read from queue " + GemsMessageDestroyer.this.m_queue + "\n");
                            } else {
                                GemsMessageDestroyer.this.m_results.append(string + " time out from read after 2 seconds\n");
                            }
                            queueReceiver.close();
                        }
                    } else {
                        GemsMessageDestroyer.this.m_results.append("Error: null message\n");
                        GemsMessageDestroyer.this.stop(true);
                    }
                } catch (Exception var2_3) {
                    GemsMessageDestroyer.this.m_results.append("Exception: " + var2_3.getMessage() + "\n");
                    GemsMessageDestroyer.this.stop(true);
                }
            }
        }
    }

}

