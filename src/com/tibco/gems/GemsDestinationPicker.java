/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.DestinationInfo
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.tibco.tibjms.admin.DestinationInfo;
import com.tibco.tibjms.admin.ServerInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;

public class GemsDestinationPicker
        extends JDialog {
    protected JComboBox m_type;
    protected JTextField m_pattern;
    protected JButton m_okButton;
    protected JButton m_cancelButton;
    protected JButton m_lookup;
    protected MyTableModel m_tableModel;
    JFrame m_frame;
    JPanel m_panel;
    GemsConnectionNode m_cn;
    GemsDestination m_retDest = null;
    JTable m_table;
    TableSorter m_sorter;

    public GemsDestinationPicker(JFrame jFrame, GemsConnectionNode gemsConnectionNode) {
        super(jFrame, "Destination Picker (" + gemsConnectionNode.getName() + ")", true);
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.init();
        this.pack();
        this.show();
    }

    public void init() {
        this.setLocationRelativeTo(this.m_frame);
        this.setDefaultCloseOperation(2);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add((Component) jPanel2, "North");
        JLabel jLabel = new JLabel("Destination Type:", 11);
        this.m_type = new JComboBox();
        for (GemsDestination.DEST_TYPE object2 : GemsDestination.DEST_TYPE.values()) {
            this.m_type.addItem(object2);
        }
        this.m_type.addItemListener(new DestTypeSelectAction());
        jLabel.setLabelFor(this.m_type);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_type);
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        jPanel3.setMinimumSize(new Dimension(200, 24));
        JLabel jLabel2 = new JLabel(" Pattern:", 11);
        this.m_pattern = new JTextField(">", 30);
        this.m_pattern.addKeyListener(new SubmitListener());
        jLabel2.setLabelFor(this.m_pattern);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_pattern);
        this.m_lookup = new JButton("LookUp");
        this.m_lookup.addActionListener(new LookupAction());
        jPanel3.add(this.m_lookup);
        jPanel2.add(jPanel3);
        this.m_tableModel = new MyTableModel();
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_table.setSelectionMode(0);
        this.addMouseListenerToTable(this.m_table);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(330, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        Component component = Box.createRigidArea(new Dimension(175, 10));
        jPanel4.add(component);
        this.m_okButton = new JButton("Ok");
        this.m_okButton.addActionListener(new OkPressed());
        this.m_cancelButton = new JButton("Cancel");
        this.m_cancelButton.addActionListener(new CancelPressed());
        jPanel4.add(this.m_okButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel4.add(component);
        jPanel4.add(this.m_cancelButton);
        jPanel.add((Component) jPanel4, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 2, 2, 5, 5, 5, 5);
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsDestinationPicker.this.ok();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void ok() {
        int n = this.m_table.getSelectedRow();
        if (n < 0) {
            JOptionPane.showMessageDialog(this, "No destination selected", "Error", 1);
        } else {
            String string = (String) this.m_table.getValueAt(n, 0);
            this.m_retDest = new GemsDestination(string, (GemsDestination.DEST_TYPE) ((Object) this.m_type.getSelectedItem()));
            this.dispose();
        }
    }

    public void dispose() {
        super.dispose();
    }

    public GemsDestinationPicker(JFrame jFrame, GemsConnectionNode gemsConnectionNode, GemsDestination.DEST_TYPE dEST_TYPE) {
        super(jFrame, "Destination Picker (" + gemsConnectionNode.getName() + ")", true);
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.init();
        this.m_type.setSelectedItem((Object) dEST_TYPE);
        this.m_type.setEnabled(false);
        this.pack();
        this.show();
    }

    public DestinationInfo[] getDestinationInfo() {
        try {
            int n;
            ServerInfo serverInfo = this.m_cn.getJmsServerInfo(false);
            if (this.m_type.getSelectedItem() == GemsDestination.DEST_TYPE.Queue) {
                int n2;
                if (serverInfo != null && serverInfo.getQueueCount() > Gems.getGems().getMaxQueues() && this.m_pattern.getText().equals(">") && (n2 = JOptionPane.showConfirmDialog(this, "There are " + serverInfo.getQueueCount() + " queues, set a pattern to filter the number of queues. \nAre you sure you want to continue?", "Lookup Queues", 0)) != 0) {
                    return null;
                }
                try {
                    return this.m_cn.getJmsAdmin().getQueues(this.m_pattern.getText(), this.m_pattern.getText().startsWith("$TMP$") ? 4 : 3);
                } catch (Throwable var2_4) {
                    return this.m_cn.getJmsAdmin().getQueues(this.m_pattern.getText());
                }
            }
            if (serverInfo != null && serverInfo.getTopicCount() > Gems.getGems().getMaxTopics() && this.m_pattern.getText().equals(">") && (n = JOptionPane.showConfirmDialog(this, "There are " + serverInfo.getTopicCount() + " topics, set a pattern to filter the number of queues. \nAre you sure you want to continue?", "Lookup Topics", 0)) != 0) {
                return null;
            }
            try {
                return this.m_cn.getJmsAdmin().getTopics(this.m_pattern.getText(), this.m_pattern.getText().startsWith("$TMP$") ? 4 : 3);
            } catch (Throwable var2_6) {
                return this.m_cn.getJmsAdmin().getTopics(this.m_pattern.getText());
            }
        } catch (TibjmsAdminException var1_2) {
            JOptionPane.showMessageDialog(this, var1_2.getMessage(), "Error", 1);
            return null;
        }
    }

    public void cancel() {
        this.dispose();
    }

    public void populateDestinationInfo(DestinationInfo[] arrdestinationInfo) {
        this.m_tableModel.setRowCount(0);
        if (arrdestinationInfo == null) {
            return;
        }
        for (int i = 0; i < arrdestinationInfo.length; ++i) {
            this.addDestination(arrdestinationInfo[i]);
        }
    }

    public void addDestination(DestinationInfo destinationInfo) {
        if (destinationInfo != null) {
            if (destinationInfo.getName().equals(">")) {
                return;
            }
            if (!this.m_pattern.getText().startsWith("$sys.") && destinationInfo.getName().startsWith("$sys.")) {
                return;
            }
            Object[] arrobject = new Object[]{destinationInfo.getName()};
            this.m_tableModel.addRow(arrobject);
        }
    }

    class MyTableModel
            extends DefaultTableModel {
        MyTableModel() {
        }

        public boolean isCellEditable(int n, int n2) {
            return false;
        }
    }

    class DestTypeSelectAction
            implements ItemListener {
        DestTypeSelectAction() {
        }

        public void itemStateChanged(ItemEvent itemEvent) {
            if (itemEvent.getStateChange() == 1) {
                // empty if block
            }
        }
    }

    class LookupAction
            implements ActionListener {
        LookupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker.this.m_tableModel.setRowCount(0);
            GemsDestinationPicker.this.m_tableModel.setColumnCount(0);
            Object[] arrobject = new String[]{"Destination Name"};
            GemsDestinationPicker.this.m_tableModel.setColumnIdentifiers(arrobject);
            GemsDestinationPicker.this.populateDestinationInfo(GemsDestinationPicker.this.getDestinationInfo());
        }
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker.this.cancel();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker.this.ok();
        }
    }

    public class SubmitListener
            implements KeyListener {
        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 10) {
                GemsDestinationPicker.this.m_tableModel.setRowCount(0);
                GemsDestinationPicker.this.m_tableModel.setColumnCount(0);
                Object[] arrobject = new String[]{"Destination Name"};
                GemsDestinationPicker.this.m_tableModel.setColumnIdentifiers(arrobject);
                GemsDestinationPicker.this.populateDestinationInfo(GemsDestinationPicker.this.getDestinationInfo());
            }
        }

        public void keyReleased(KeyEvent keyEvent) {
        }
    }

}

