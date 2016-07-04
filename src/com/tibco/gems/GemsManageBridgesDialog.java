/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.BridgeInfo
 *  com.tibco.tibjms.admin.DestinationBridgeInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.*;

import com.tibco.tibjms.admin.BridgeInfo;
import com.tibco.tibjms.admin.DestinationBridgeInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;

public class GemsManageBridgesDialog
        extends JDialog {
    protected String m_destType;
    protected String m_bridgeType;
    protected JTextField m_conn;
    protected JTextField m_pattern;
    protected JTextField m_filter;
    protected JButton m_okButton;
    protected JButton m_cancelButton;
    protected JComboBox m_destTypeCombo;
    protected GemsBridgeTableModel m_tableModel;
    JFrame m_frame;
    JPanel m_panel;
    GemsConnectionNode m_cn;
    Pattern m_filterPattern = null;
    JTable m_table;
    TableSorter m_sorter;

    public GemsManageBridgesDialog(JFrame jFrame, GemsConnectionNode gemsConnectionNode, String string, String string2, String string3) {
        super(jFrame, Gems.getGems().getTitlePrefix() + "Manage Bridges", true);
        this.setDefaultCloseOperation(2);
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_destType = string;
        this.m_bridgeType = string3;
        String string4 = "Target";
        if (string3.equals("Target")) {
            string4 = "Source";
        }
        JMenuBar jMenuBar = this.constructMenuBar();
        this.setJMenuBar(jMenuBar);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add((Component) jPanel2, "North");
        JLabel jLabel = new JLabel("EMS Server:", 11);
        this.m_conn = new JTextField(gemsConnectionNode.getName(), 20);
        this.m_conn.setEditable(false);
        this.m_conn.setMaximumSize(new Dimension(0, 24));
        jLabel.setLabelFor(this.m_conn);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_conn);
        jPanel2.add(new JLabel(string4 + " Destination Type:"));
        this.m_destTypeCombo = new JComboBox();
        this.m_destTypeCombo.addItem("Queue");
        this.m_destTypeCombo.addItem("Topic");
        this.m_destTypeCombo.setSelectedItem(this.m_destType);
        jPanel2.add(this.m_destTypeCombo);
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        JLabel jLabel2 = new JLabel(string4 + " Destination Pattern:", 11);
        if (string2 == null || string2.length() == 0) {
            string2 = ">";
        }
        this.m_pattern = new JTextField(string2, 32);
        jLabel2.setLabelFor(this.m_pattern);
        jPanel2.add(jLabel2);
        JButton jButton = new JButton("  Lookup...");
        jButton.addActionListener(new DestinationWizardAction());
        jPanel3.add(this.m_pattern);
        jPanel3.add(jButton);
        jPanel2.add(jPanel3);
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        JLabel jLabel3 = new JLabel("Target Destination Filter:", 11);
        this.m_filter = new JTextField("", 32);
        this.m_filter.setEnabled(false);
        jLabel3.setLabelFor(this.m_filter);
        jPanel2.add(jLabel3);
        JButton jButton2 = new JButton("Set Filter...");
        jButton2.addActionListener(new FilterWizardAction());
        jPanel4.add(this.m_filter);
        jPanel4.add(jButton2);
        jPanel2.add(jPanel4);
        this.m_tableModel = new GemsBridgeTableModel(false, true, this);
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_table.setRowSelectionAllowed(false);
        this.m_tableModel.m_table = this.m_table;
        this.addMouseListenerToTable(this.m_table);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(735, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel5 = new JPanel(true);
        jPanel5.setLayout(new BoxLayout(jPanel5, 0));
        Component component = Box.createRigidArea(new Dimension(275, 10));
        jPanel5.add(component);
        this.m_okButton = new JButton("Find Bridges");
        this.m_okButton.addActionListener(new OkPressed());
        this.m_cancelButton = new JButton("Close");
        this.m_cancelButton.addActionListener(new CancelPressed());
        jPanel5.add(this.m_okButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel5.add(component);
        jPanel5.add(this.m_cancelButton);
        jPanel.add((Component) jPanel5, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 4, 2, 5, 5, 5, 5);
        this.m_frame.setIconImage(Gems.getGems().m_icon.getImage());
        this.pack();
        this.setLocationRelativeTo(jFrame);
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
                GemsManageBridgesDialog.this.dispose();
            }
        });
        jMenu = new JMenu("Edit");
        jMenu.setMnemonic(69);
        jMenuBar.add(jMenu);
        jMenuItem = new JMenuItem("Select All");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2));
        jMenuItem.addActionListener(new SelectAllAction());
        jMenu.add(jMenuItem);
        if (!Gems.getGems().getViewOnlyMode()) {
            jMenu.addSeparator();
            jMenuItem = new JMenuItem("Create New Target...");
            jMenuItem.addActionListener(new CreateBridgeAction());
            jMenu.add(jMenuItem);
            jMenuItem = new JMenuItem("Destroy Selected Bridges");
            jMenuItem.addActionListener(new DeleteBridgesAction());
            jMenu.add(jMenuItem);
        }
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && GemsManageBridgesDialog.this.m_table.getSelectedColumn() > 0) {
                    GemsManageBridgesDialog.this.m_tableModel.toggleSelectedRow();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void dispose() {
        super.dispose();
    }

    public void stop() {
        this.dispose();
    }

    public void createBridge(String string) {
        if (!Gems.getGems().isStandbyOpsAllowed(this.m_cn)) {
            return;
        }
        GemsCreateBridgeDialog gemsCreateBridgeDialog = new GemsCreateBridgeDialog(null, this.m_cn, this.m_destTypeCombo.getSelectedItem().equals("Queue"), string);
        if (gemsCreateBridgeDialog.createBridge()) {
            this.start();
        }
    }

    public void start() {
        this.m_tableModel.buildColumnHeaders();
        this.m_sorter.setSortingStatus(1, 1);
        this.m_tableModel.populateBridgeInfo(this.getBridgeInfo(), this.m_filterPattern);
    }

    public BridgeInfo[] getBridgeInfo() {
        try {
            return this.m_cn.getJmsAdmin().getBridges(this.m_destTypeCombo.getSelectedItem().equals("Queue") ? 1 : 2, this.m_pattern.getText());
        } catch (TibjmsAdminException var1_1) {
            JOptionPane.showMessageDialog(this.m_frame, var1_1.getMessage(), "Error", 1);
            return null;
        }
    }

    public void deleteSelectedBridges() {
        if (!Gems.getGems().isStandbyOpsAllowed(this.m_cn)) {
            return;
        }
        Vector vector = this.m_tableModel.getSelectedBridges();
        if (vector.size() <= 0) {
            return;
        }
        int n = JOptionPane.showConfirmDialog(null, "Destroy All Selected Bridges?", "Destroy Bridge", 0);
        if (n == 0 && this.m_cn != null && this.m_cn.m_adminConn != null) {
            for (int i = 0; i < vector.size(); ++i) {
                try {
                    DestinationBridgeInfo destinationBridgeInfo = (DestinationBridgeInfo) vector.get(i);
                    this.m_cn.m_adminConn.destroyDestinationBridge(destinationBridgeInfo.getSourceType(), destinationBridgeInfo.getSourceName(), destinationBridgeInfo.getTargetType(), destinationBridgeInfo.getTargetName());
                    continue;
                } catch (TibjmsAdminException var4_5) {
                    JOptionPane.showMessageDialog(this.m_frame, var4_5.getMessage(), "Error", 1);
                    return;
                }
            }
            this.start();
        }
    }

    public void deleteBridge(String string, String string2, String string3, String string4) {
        if (!Gems.getGems().isStandbyOpsAllowed(this.m_cn)) {
            return;
        }
        int n = JOptionPane.showConfirmDialog(null, "<html>Destroy Bridge:<p>Source " + string2 + ":" + string + "<p>Target " + string4 + ": " + string3 + "</html>", "Destroy Bridge", 0);
        if (n == 0 && this.m_cn != null && this.m_cn.m_adminConn != null) {
            try {
                this.m_cn.m_adminConn.destroyDestinationBridge(string2.equals("Queue") ? 1 : 2, string, string4.equals("Queue") ? 1 : 2, string3);
            } catch (TibjmsAdminException var6_6) {
                JOptionPane.showMessageDialog(this.m_frame, var6_6.getMessage(), "Error", 1);
                return;
            }
            this.start();
        }
    }

    class FilterWizardAction
            implements ActionListener {
        FilterWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsBrowserFilterDialog gemsBrowserFilterDialog = new GemsBrowserFilterDialog(GemsManageBridgesDialog.this.m_frame, "Target Filter Editor:");
            Pattern pattern = gemsBrowserFilterDialog.getFilter(GemsManageBridgesDialog.this.m_filterPattern, "destinations");
            if (!gemsBrowserFilterDialog.m_cancelled) {
                GemsManageBridgesDialog.this.m_filterPattern = pattern;
            }
            if (GemsManageBridgesDialog.this.m_filterPattern != null && GemsManageBridgesDialog.this.m_filterPattern.pattern().length() > 0) {
                GemsManageBridgesDialog.this.m_filter.setText(GemsManageBridgesDialog.this.m_filterPattern.pattern());
            } else {
                GemsManageBridgesDialog.this.m_filter.setText("");
                GemsManageBridgesDialog.this.m_filterPattern = null;
            }
        }
    }

    class DestinationWizardAction
            implements ActionListener {
        DestinationWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsManageBridgesDialog.this.m_frame, GemsManageBridgesDialog.this.m_cn, GemsManageBridgesDialog.this.m_destTypeCombo.getSelectedItem().equals("Queue") ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsManageBridgesDialog.this.m_pattern.setText(gemsDestinationPicker.m_retDest.m_destName);
            }
        }
    }

    class DeleteBridgesAction
            implements ActionListener {
        DeleteBridgesAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsManageBridgesDialog.this.deleteSelectedBridges();
        }
    }

    class CreateBridgeAction
            implements ActionListener {
        CreateBridgeAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsManageBridgesDialog.this.createBridge(GemsManageBridgesDialog.this.m_pattern.getText());
        }
    }

    class SelectAllAction
            implements ActionListener {
        SelectAllAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsManageBridgesDialog.this.m_tableModel.selectAllRows();
        }
    }

    class LookupAction
            implements ActionListener {
        LookupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsManageBridgesDialog.this.start();
        }
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsManageBridgesDialog.this.dispose();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsManageBridgesDialog.this.start();
        }
    }

}

