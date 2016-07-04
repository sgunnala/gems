/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.TibjmsQueueConnectionFactory
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  javax.jms.JMSException
 *  javax.jms.Message
 *  javax.jms.Queue
 *  javax.jms.QueueBrowser
 *  javax.jms.QueueConnection
 *  javax.jms.QueueSession
 *  javax.jms.TextMessage
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import com.tibco.tibjms.TibjmsQueueConnectionFactory;
import com.tibco.tibjms.admin.TibjmsAdmin;

public class GemsQueueBrowser
        extends JFrame {
    protected JTextField m_conn;
    protected JTextField m_queue;
    protected JTextField m_msgsRead;
    protected JTextField m_msgsDisplay;
    protected JTextField m_selector;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JButton m_selwiz;
    protected JButton m_destwiz;
    protected JCheckBox m_noLimit;
    protected GemsMessageTableModel m_tableModel;
    protected boolean m_viewoldestFirst;
    protected JMenuItem m_optMenuItem;
    protected JMenuItem m_filterMenuItem;
    protected JMenuItem m_selectorMenuItem;
    protected JMenuItem m_dumpMenuItem;
    JFrame m_frame;
    JPanel m_panel;
    boolean m_running = false;
    int m_msgs = 0;
    int m_maxMsgs = 10;
    Message m_msg = null;
    TibjmsAdmin m_admin = null;
    QueueSession m_session = null;
    GemsConnectionNode m_cn;
    QueueConnection m_connection = null;
    QueueBrowser m_browser = null;
    Enumeration m_msgsEnum = null;
    Pattern m_pattern = null;
    Timer m_timer;
    JTable m_table;
    TableSorter m_sorter;

    public GemsQueueBrowser(GemsConnectionNode gemsConnectionNode, String string) {
        super(Gems.getGems().getTitlePrefix() + "Queue Browser");
        this.m_timer = new Timer(Gems.getGems().getMsgReadDelay(), new RefreshTimerAction());
        this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
        this.setLocation(400, 175);
        this.setDefaultCloseOperation(2);
        this.m_frame = this;
        this.m_cn = gemsConnectionNode;
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
        JLabel jLabel2 = new JLabel("Queue Name:", 11);
        this.m_queue = new JTextField(string, 20);
        jLabel2.setLabelFor(this.m_queue);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_queue);
        this.m_destwiz = new JButton("...");
        this.m_destwiz.setPreferredSize(new Dimension(18, 16));
        this.m_destwiz.addActionListener(new DestinationWizardAction());
        jPanel3.add(this.m_destwiz);
        jPanel2.add(jPanel3);
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        jPanel4.setMinimumSize(new Dimension(140, 24));
        JLabel jLabel3 = new JLabel("Selector:", 11);
        this.m_selector = new JTextField("", 20);
        jLabel3.setLabelFor(this.m_selector);
        jPanel2.add(jLabel3);
        jPanel4.add(this.m_selector);
        this.m_selwiz = new JButton("...");
        this.m_selwiz.setPreferredSize(new Dimension(18, 16));
        this.m_selwiz.addActionListener(new SelectorWizardAction());
        jPanel4.add(this.m_selwiz);
        jPanel2.add(jPanel4);
        JPanel jPanel5 = new JPanel(true);
        jPanel5.setLayout(new BoxLayout(jPanel5, 0));
        JLabel jLabel4 = new JLabel("Msgs to Read:", 11);
        this.m_msgsRead = new JTextField("10", 20);
        this.m_msgsRead.setMinimumSize(new Dimension(40, 24));
        jPanel4.setMinimumSize(new Dimension(140, 24));
        jLabel4.setLabelFor(this.m_msgsRead);
        jPanel2.add(jLabel4);
        jPanel5.add(this.m_msgsRead);
        this.m_noLimit = new JCheckBox("No Limit", false);
        jPanel5.add(this.m_noLimit);
        jPanel2.add(jPanel5);
        this.m_tableModel = new GemsMessageTableModel(false, true);
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
        JPanel jPanel6 = new JPanel(true);
        jPanel6.setLayout(new BoxLayout(jPanel6, 0));
        Component component = Box.createRigidArea(new Dimension(250, 10));
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
                GemsQueueBrowser.this.dispose();
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
        jMenuItem = new JMenuItem("Select All");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2));
        jMenuItem.addActionListener(new SelectAllAction());
        jMenu.add(jMenuItem);
        jMenu.addSeparator();
        this.m_selectorMenuItem = jMenu.add(new JMenuItem("Selector..."));
        this.m_selectorMenuItem.addActionListener(new SelectorWizardAction());
        this.m_filterMenuItem = jMenu.add(new JMenuItem("TextMessage Filter..."));
        this.m_filterMenuItem.addActionListener(new FilterWizardAction());
        this.m_optMenuItem = jMenu.add(new JMenuItem("Options..."));
        this.m_optMenuItem.addActionListener(new EditOptionsAction());
        jMenuBar.add(jMenu);
        jMenu = new JMenu("Message");
        jMenu.setMnemonic(77);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("View Message..."));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsQueueBrowser.this.showMessageFrame();
            }
        });
        if (!Gems.getGems().getViewOnlyMode()) {
            jMenuItem = jMenu.add(new JMenuItem("Copy Checked Messages..."));
            jMenuItem.addActionListener(new CopyMessageAction());
            jMenuItem = jMenu.add(new JMenuItem("Destroy Checked Messages..."));
            jMenuItem.addActionListener(new DestroyMessageAction());
        }
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsQueueBrowser.this.showMessageFrame();
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
            this.m_msgsEnum = null;
            if (this.m_browser != null) {
                this.m_browser.close();
                this.m_browser = null;
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
        this.m_queue.setEnabled(true);
        this.m_msgsRead.setEnabled(true);
        this.m_startButton.setEnabled(true);
        this.m_stopButton.setEnabled(false);
        this.m_noLimit.setEnabled(true);
        this.m_selector.setEnabled(true);
        this.m_selwiz.setEnabled(true);
        this.m_destwiz.setEnabled(true);
        this.m_optMenuItem.setEnabled(true);
        this.m_filterMenuItem.setEnabled(true);
        this.m_selectorMenuItem.setEnabled(true);
        this.m_dumpMenuItem.setEnabled(true);
    }

    public void start() {
        this.m_running = true;
        this.m_msgs = 0;
        this.m_queue.setEnabled(false);
        this.m_msgsRead.setEnabled(false);
        this.m_startButton.setEnabled(false);
        this.m_stopButton.setEnabled(true);
        this.m_noLimit.setEnabled(false);
        this.m_selector.setEnabled(false);
        this.m_filterMenuItem.setEnabled(false);
        this.m_selwiz.setEnabled(false);
        this.m_destwiz.setEnabled(false);
        this.m_optMenuItem.setEnabled(false);
        this.m_selectorMenuItem.setEnabled(false);
        this.m_dumpMenuItem.setEnabled(false);
        try {
            this.m_maxMsgs = Integer.parseInt(this.m_msgsRead.getText());
        } catch (Exception var1_1) {
            this.m_maxMsgs = 10;
        }
        this.m_tableModel.buildColumnHeaders();
        try {
            TibjmsQueueConnectionFactory tibjmsQueueConnectionFactory = new TibjmsQueueConnectionFactory(this.m_cn.m_url, null, this.m_cn.m_sslParams);
            this.m_connection = tibjmsQueueConnectionFactory.createQueueConnection(this.m_cn.m_user, this.m_cn.m_password);
            this.m_session = this.m_connection.createQueueSession(false, 1);
            Queue queue = this.m_session.createQueue(this.m_queue.getText());
            this.m_connection.start();
            this.m_browser = this.m_selector != null && this.m_selector.getText().length() > 0 ? this.m_session.createBrowser(queue, this.m_selector.getText()) : this.m_session.createBrowser(queue);
            this.m_msgsEnum = this.m_browser.getEnumeration();
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
                int n = jFileChooser.showOpenDialog(GemsQueueBrowser.this.m_frame);
                if (n == 0) {
                    File file = jFileChooser.getSelectedFile();
                    GemsQueueBrowser.this.m_tableModel.dumpMsgsToFile(file);
                }
            } catch (IOException var2_3) {
                JOptionPane.showMessageDialog(GemsQueueBrowser.this.m_frame, var2_3.getMessage(), "Error", 1);
                return;
            }
        }
    }

    class SelectAllAction
            implements ActionListener {
        SelectAllAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsQueueBrowser.this.m_tableModel.selectAllRows();
        }
    }

    class DestroyMessageAction
            implements ActionListener {
        DestroyMessageAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Vector vector = GemsQueueBrowser.this.m_tableModel.getSelectedMessages();
            if (vector.size() == 0) {
                JOptionPane.showMessageDialog(GemsQueueBrowser.this.m_frame, "Select Messages to destroy using checkbox!", "Destroy Messages", 1);
            }
            if (vector.size() > 0) {
                GemsMessageDestroyer gemsMessageDestroyer = new GemsMessageDestroyer(GemsQueueBrowser.this.m_frame, GemsQueueBrowser.this.m_cn, vector, GemsQueueBrowser.this.m_queue.getText());
            }
        }
    }

    class CopyMessageAction
            implements ActionListener {
        CopyMessageAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Vector vector = GemsQueueBrowser.this.m_tableModel.getSelectedMessages();
            if (vector.size() == 0) {
                JOptionPane.showMessageDialog(GemsQueueBrowser.this.m_frame, "Select Messages to copy using checkbox!", "Copy Messages", 1);
            }
            if (vector.size() > 0) {
                GemsMessageCopier gemsMessageCopier = new GemsMessageCopier(GemsQueueBrowser.this.m_frame, GemsQueueBrowser.this.m_cn, vector);
            }
        }
    }

    class FilterWizardAction
            implements ActionListener {
        FilterWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsBrowserFilterDialog gemsBrowserFilterDialog = new GemsBrowserFilterDialog(GemsQueueBrowser.this.m_frame, "TextMessage Filter Editor:");
            Pattern pattern = gemsBrowserFilterDialog.getFilter(GemsQueueBrowser.this.m_pattern, "messages");
            if (!gemsBrowserFilterDialog.m_cancelled) {
                GemsQueueBrowser.this.m_pattern = pattern;
            }
            if (GemsQueueBrowser.this.m_pattern != null && GemsQueueBrowser.this.m_pattern.pattern().length() == 0) {
                GemsQueueBrowser.this.m_pattern = null;
            }
        }
    }

    class DestinationWizardAction
            implements ActionListener {
        DestinationWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsQueueBrowser.this.m_frame, GemsQueueBrowser.this.m_cn, GemsDestination.DEST_TYPE.Queue);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsQueueBrowser.this.m_queue.setText(gemsDestinationPicker.m_retDest.m_destName);
            }
        }
    }

    class SelectorWizardAction
            implements ActionListener {
        SelectorWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsSelectorWizardDialog gemsSelectorWizardDialog = new GemsSelectorWizardDialog(GemsQueueBrowser.this.m_frame, "Selector Editor:", "Browse");
            if (!gemsSelectorWizardDialog.m_cancelled && gemsSelectorWizardDialog.m_selector.length() > 0) {
                GemsQueueBrowser.this.m_selector.setText(gemsSelectorWizardDialog.m_selector);
            }
        }
    }

    class EditOptionsAction
            implements ActionListener {
        EditOptionsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            new GemsBrowserOptionsDialog(GemsQueueBrowser.this.m_frame, "Edit Queue Browser Options");
            GemsQueueBrowser.this.m_timer.setDelay(Gems.getGems().getMsgReadDelay());
            GemsQueueBrowser.this.m_viewoldestFirst = Gems.getGems().getViewOldMessagesFirst();
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsQueueBrowser.this.stop();
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsQueueBrowser.this.start();
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsQueueBrowser.this.m_running) {
                if (!GemsQueueBrowser.this.m_msgsEnum.hasMoreElements()) {
                    GemsQueueBrowser.this.stop();
                } else {
                    Message message = (Message) GemsQueueBrowser.this.m_msgsEnum.nextElement();
                    if (message != null) {
                        try {
                            if (GemsQueueBrowser.this.m_pattern != null && message instanceof TextMessage && !GemsQueueBrowser.this.m_pattern.matcher(((TextMessage) message).getText()).matches()) {
                                return;
                            }
                        } catch (JMSException var3_3) {
                            System.err.println("Exception: " + var3_3.getMessage());
                            return;
                        }
                        GemsQueueBrowser.this.m_tableModel.addMessage(message, GemsQueueBrowser.this.m_viewoldestFirst);
                        if (!GemsQueueBrowser.this.m_noLimit.isSelected() && ++GemsQueueBrowser.this.m_msgs >= GemsQueueBrowser.this.m_maxMsgs) {
                            GemsQueueBrowser.this.stop();
                        }
                    }
                }
            }
        }
    }

}

