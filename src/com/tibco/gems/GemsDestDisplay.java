/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.ServerInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 *  com.tibco.tibjms.admin.TopicInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.tibco.tibjms.admin.ServerInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;

public class GemsDestDisplay
        extends JFrame {
    protected String m_type;
    protected JTextField m_conn;
    protected JTextField m_pattern;
    protected JButton m_closeButton;
    protected JButton m_lookup;
    protected boolean m_isQueue;
    protected boolean m_isFirstUpdate = false;
    protected JCheckBoxMenuItem m_autoRefresh = null;
    protected GemsDetailsTableModel m_tableModel;
    protected Timer m_timer = null;
    JFrame m_frame;
    JPanel m_panel;
    GemsConnectionNode m_cn;
    JTable m_table;
    TableSorter m_sorter;

    public GemsDestDisplay(GemsConnectionNode gemsConnectionNode, String string) {
        super(Gems.getGems().getTitlePrefix() + string + "s Display");
        this.setLocation(300, 155);
        this.setDefaultCloseOperation(2);
        this.m_frame = this;
        this.m_cn = gemsConnectionNode;
        this.m_type = string;
        this.m_isQueue = string.equals("Queue");
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
        jPanel3.setMinimumSize(new Dimension(600, 24));
        JLabel jLabel2 = new JLabel(string + " Pattern:", 11);
        this.m_pattern = new JTextField(this.m_isQueue ? Gems.getGems().getQueueNamePattern() : Gems.getGems().getTopicNamePattern(), 32);
        this.m_pattern.addKeyListener(new SubmitListener());
        jLabel2.setLabelFor(this.m_pattern);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_pattern);
        this.m_lookup = new JButton("Lookup");
        this.m_lookup.addActionListener(new LookupAction());
        jPanel3.add(this.m_lookup);
        jPanel2.add(jPanel3);
        this.m_tableModel = new GemsDetailsTableModel();
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_tableModel.setPopupHandler(new PopupDestDisplayHandler(this.m_table, this.m_tableModel, this.m_cn, this));
        this.m_tableModel.setTable(this.m_table);
        this.m_table.setSelectionMode(0);
        if (!Gems.getGems().getViewOnlyMode()) {
            this.addMouseListenerToTable(this.m_table);
        }
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(835, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        Component component = Box.createRigidArea(new Dimension(350, 10));
        jPanel4.add(component);
        this.m_closeButton = new JButton("Close");
        this.m_closeButton.addActionListener(new ClosePressed());
        jPanel4.add(this.m_closeButton);
        jPanel.add((Component) jPanel4, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 2, 2, 5, 5, 5, 5);
        this.m_frame.setIconImage(Gems.getGems().m_icon.getImage());
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
                GemsDestDisplay.this.dispose();
            }
        });
        if (!Gems.getGems().getViewOnlyMode()) {
            jMenu = new JMenu("Edit");
            jMenu.setMnemonic(69);
            jMenuBar.add(jMenu);
            jMenuItem = new JMenuItem(this.m_type + " Properties...");
            jMenuItem.addActionListener(new EditPropAction());
            jMenu.add(jMenuItem);
        }
        jMenu = new JMenu("View");
        jMenu.setMnemonic(86);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("Temporaries"));
        jMenuItem.addActionListener(new ViewTempsAction());
        jMenuItem = jMenu.add(new JMenuItem("Refresh"));
        jMenuItem.addActionListener(new RefreshAction());
        this.m_autoRefresh = new JCheckBoxMenuItem("Auto Refresh");
        if (Gems.getGems().getDisplayRefresh() > 0) {
            jMenuItem = jMenu.add(this.m_autoRefresh);
            this.m_autoRefresh.setState(true);
        } else {
            this.m_autoRefresh.setState(false);
        }
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsDestDisplay.this.editDestProperties();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void dispose() {
        super.dispose();
    }

    public void editDestProperties() {
        String string = this.m_tableModel.getSelectedCol1();
        GemsDestPropEditor gemsDestPropEditor = new GemsDestPropEditor(this.m_frame, this.m_cn, this.m_type, string);
    }

    public void lookupDestinations() {
        if (this.m_timer != null) {
            this.m_timer.stop();
        }
        this.m_tableModel.setRowCount(0);
        this.m_tableModel.setColumnCount(0);
        ServerInfo serverInfo = this.m_cn.getJmsServerInfo(false);
        if (serverInfo != null) {
            if (this.m_isQueue) {
                if (serverInfo.getQueueCount() > Gems.getGems().getMaxQueues() && this.m_pattern.getText().equals(">")) {
                    int n = JOptionPane.showConfirmDialog(this, "There are " + serverInfo.getQueueCount() + " queues, set a pattern to filter the number of queues. \nAre you sure you want to continue?", "Lookup Queues", 0);
                    if (n != 0) {
                        return;
                    }
                    this.m_autoRefresh.setState(false);
                } else {
                    this.m_autoRefresh.setState(true);
                }
            } else if (serverInfo.getTopicCount() > Gems.getGems().getMaxTopics() && this.m_pattern.getText().equals(">")) {
                int n = JOptionPane.showConfirmDialog(this, "There are " + serverInfo.getTopicCount() + " topics, set a pattern to filter the number of queues. \nAre you sure you want to continue?", "Lookup Topics", 0);
                if (n != 0) {
                    return;
                }
                this.m_autoRefresh.setState(false);
            } else {
                this.m_autoRefresh.setState(true);
            }
            this.m_isFirstUpdate = true;
            if (Gems.getGems().getDisplayRefresh() == 0) {
                this.m_autoRefresh.setState(false);
            }
            if (this.m_timer == null) {
                this.m_timer = new Timer(Gems.getGems().getDisplayRefresh() * 1000 + 1000, new RefreshTimerAction());
                this.m_timer.setInitialDelay(50);
                this.m_timer.start();
            } else {
                this.m_timer.restart();
            }
        }
    }

    public void getDestinations() {
        if (this.m_pattern.getText().length() == 0) {
            this.m_tableModel.populateErrorInfo("No " + this.m_type + " name pattern configured");
            return;
        }
        if (!this.m_cn.isConnected()) {
            this.m_tableModel.populateErrorInfo("Not connected to EMS server");
            return;
        }
        try {
            int n = 3;
            if (this.m_pattern.getText().startsWith("$TMP$.")) {
                n = 4;
            }
            if (this.m_isQueue) {
                try {
                    this.m_tableModel.populateQueuesInfo(this.m_cn.getJmsAdmin().getQueues(this.m_pattern.getText(), n));
                } catch (Throwable var2_3) {
                    this.m_tableModel.populateQueuesInfo(this.m_cn.getJmsAdmin().getQueues(this.m_pattern.getText()));
                }
            } else {
                try {
                    this.m_tableModel.populateTopicsInfo(this.m_cn.getJmsAdmin().getTopics(this.m_pattern.getText(), n));
                } catch (Throwable var2_4) {
                    this.m_tableModel.populateTopicsInfo(this.m_cn.getJmsAdmin().getTopics(this.m_pattern.getText()));
                }
            }
        } catch (TibjmsAdminException var1_2) {
            System.err.println("JMSException: " + var1_2.getMessage());
            this.m_autoRefresh.setState(false);
            return;
        }
    }

    class ViewTempsAction
            implements ActionListener {
        ViewTempsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestDisplay.this.m_pattern.setText("$TMP$.>");
            GemsDestDisplay.this.lookupDestinations();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsDestDisplay.this.m_isFirstUpdate || GemsDestDisplay.this.m_autoRefresh.getState()) {
                GemsDestDisplay.this.getDestinations();
            }
            GemsDestDisplay.this.m_isFirstUpdate = false;
        }
    }

    class RefreshAction
            implements ActionListener {
        RefreshAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsDestDisplay.this.m_timer != null) {
                GemsDestDisplay.this.m_isFirstUpdate = true;
                GemsDestDisplay.this.m_timer.restart();
            }
        }
    }

    class LookupAction
            implements ActionListener {
        LookupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestDisplay.this.lookupDestinations();
        }
    }

    class EditPropAction
            implements ActionListener {
        EditPropAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestDisplay.this.editDestProperties();
        }
    }

    public class SubmitListener
            implements KeyListener {
        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 10) {
                GemsDestDisplay.this.lookupDestinations();
            }
        }

        public void keyReleased(KeyEvent keyEvent) {
        }
    }

    class ClosePressed
            implements ActionListener {
        ClosePressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestDisplay.this.dispose();
        }
    }

}

