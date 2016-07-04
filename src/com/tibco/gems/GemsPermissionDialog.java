/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.ACLEntry
 *  com.tibco.tibjms.admin.DestinationInfo
 *  com.tibco.tibjms.admin.GroupInfo
 *  com.tibco.tibjms.admin.Permissions
 *  com.tibco.tibjms.admin.PrincipalInfo
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 *  com.tibco.tibjms.admin.TopicInfo
 *  com.tibco.tibjms.admin.UserInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.tibco.tibjms.admin.ACLEntry;
import com.tibco.tibjms.admin.DestinationInfo;
import com.tibco.tibjms.admin.GroupInfo;
import com.tibco.tibjms.admin.Permissions;
import com.tibco.tibjms.admin.PrincipalInfo;
import com.tibco.tibjms.admin.QueueInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;
import com.tibco.tibjms.admin.TopicInfo;
import com.tibco.tibjms.admin.UserInfo;

public class GemsPermissionDialog
        extends JDialog {
    protected JTree m_perms;
    protected JFrame m_frame;
    protected JCheckBox m_isAdmin;
    protected JTextField m_dest;
    protected JComboBox m_principal;
    protected boolean m_cancelled = false;
    protected String m_selected;
    protected CheckNode[] m_px;
    protected GemsConnectionNode m_cn;
    protected boolean m_isQueue = false;
    protected JButton m_destwiz;

    public GemsPermissionDialog(JFrame jFrame, String string, boolean bl, GemsConnectionNode gemsConnectionNode, String string2) {
        super(jFrame, string, true);
        this.m_cn = gemsConnectionNode;
        this.m_frame = jFrame;
        this.m_isQueue = bl;
        this.buildFrame(jFrame, string2);
        this.updatePermissions(true);
        this.pack();
        this.show();
    }

    public void buildFrame(Frame frame, String string) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel(new SpringLayout());
        jPanel.add(jPanel2);
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        JLabel jLabel = new JLabel(this.m_isQueue ? "Queue:" : "Topic:", 11);
        this.m_dest = new JTextField(20);
        this.m_dest.setText(string);
        jPanel2.add(jLabel);
        jPanel3.add(this.m_dest);
        this.m_destwiz = new JButton("...");
        this.m_destwiz.setPreferredSize(new Dimension(18, 16));
        this.m_destwiz.addActionListener(new DestinationWizardAction());
        jPanel3.add(this.m_destwiz);
        jPanel2.add(jPanel3);
        JLabel jLabel2 = new JLabel("Principal:", 11);
        this.m_principal = new JComboBox();
        this.populatePrincipals();
        this.m_principal.addActionListener(new PrincipalSelected());
        jPanel2.add(jLabel2);
        jPanel2.add(this.m_principal);
        SpringUtilities.makeCompactGrid(jPanel2, 2, 2, 5, 5, 5, 5);
        this.m_px = this.m_isQueue ? new CheckNode[]{new CheckNode("All Permissions:"), new CheckNode("JMS Permissions:"), new CheckNode("Send To Queue"), new CheckNode("Receive From Queue"), new CheckNode("Browse Queue"), new CheckNode("Admin Permissions:"), new CheckNode("View Queue"), new CheckNode("Create Queue"), new CheckNode("Modify Queue"), new CheckNode("Delete Queue"), new CheckNode("Purge Queue")} : new CheckNode[]{new CheckNode("All Permissions:"), new CheckNode("JMS Permissions:"), new CheckNode("Subscribe To Topic"), new CheckNode("Publish To Topic"), new CheckNode("Create Durable"), new CheckNode("Use Durable"), new CheckNode("Admin Permissions:"), new CheckNode("View Topic"), new CheckNode("Create Topic"), new CheckNode("Modify Topic"), new CheckNode("Delete Topic"), new CheckNode("Purge Topic")};
        this.m_perms = new JTree(this.m_px[0]);
        this.m_perms.setBorder(new EtchedBorder());
        this.m_perms.setCellRenderer(new CheckRenderer());
        this.m_perms.getSelectionModel().setSelectionMode(1);
        this.m_perms.putClientProperty("JTree.lineStyle", "Angled");
        this.m_perms.addMouseListener(new NodeSelectionListener(this.m_perms));
        int n = 0;
        for (int i = 1; i < this.m_px.length; ++i) {
            if (((String) this.m_px[i].getUserObject()).endsWith(":") && n > 0) {
                n = 0;
            }
            this.m_px[n].add(this.m_px[i]);
            if (!((String) this.m_px[i].getUserObject()).endsWith(":") || n != 0) continue;
            n = i;
        }
        this.m_perms.expandRow(0);
        this.m_perms.expandRow(1);
        this.m_perms.expandRow(5);
        this.m_perms.expandRow(6);
        JScrollPane jScrollPane = new JScrollPane(this.m_perms);
        jScrollPane.setPreferredSize(new Dimension(350, 300));
        JPanel jPanel4 = new JPanel();
        jPanel4.setBorder(new TitledBorder("Permissions"));
        jPanel4.setLayout(new BoxLayout(jPanel4, 1));
        JPanel jPanel5 = new JPanel(new FlowLayout());
        JButton jButton = new JButton("Apply");
        jButton.addActionListener(new ApplyPressed());
        jPanel5.add(jButton);
        JButton jButton2 = new JButton("Reset");
        jButton2.addActionListener(new ResetPressed());
        jPanel5.add(jButton2);
        jPanel4.add(jScrollPane);
        JPanel jPanel6 = new JPanel(new FlowLayout());
        JButton jButton3 = new JButton("Permit All");
        JButton jButton4 = new JButton("Restrict All");
        jPanel6.add(jButton3);
        jPanel6.add(jButton4);
        jPanel4.add(jPanel5);
        jPanel.add(jPanel4);
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new FlowLayout());
        JButton jButton5 = new JButton("OK");
        JButton jButton6 = new JButton("Cancel");
        jPanel7.add(jButton5);
        jPanel7.add(jButton6);
        jButton5.addActionListener(new OkPressed());
        jButton6.addActionListener(new CancelPressed());
        jPanel.add(jPanel7);
        this.setContentPane(jPanel);
        this.setLocationRelativeTo(frame);
    }

    public void updatePermissions(boolean bl) {
        if (bl && this.m_dest.getText().length() == 0) {
            return;
        }
        try {
            for (int i = 0; i < this.m_px.length; ++i) {
                this.m_px[i].setSelected(false);
            }
            ACLEntry[] arraCLEntry = this.m_isQueue ? this.m_cn.m_adminConn.getQueueACLEntries(this.m_dest.getText()) : this.m_cn.m_adminConn.getTopicACLEntries(this.m_dest.getText());
            for (int j = 0; j < arraCLEntry.length; ++j) {
                String string = arraCLEntry[j].getPrincipal().getName();
                string = arraCLEntry[j].getPrincipal() instanceof UserInfo ? string + " (user" : string + " (group";
                if (!this.m_selected.startsWith(string)) continue;
                Permissions permissions = arraCLEntry[j].getPermissions();
                if (this.m_isQueue) {
                    if (permissions.hasPermission(1)) {
                        this.m_px[2].setSelected(true);
                    }
                    if (permissions.hasPermission(2)) {
                        this.m_px[3].setSelected(true);
                    }
                    if (permissions.hasPermission(4)) {
                        this.m_px[4].setSelected(true);
                    }
                    if (permissions.hasPermission(65536)) {
                        this.m_px[6].setSelected(true);
                    }
                    if (permissions.hasPermission(131072)) {
                        this.m_px[7].setSelected(true);
                    }
                    if (permissions.hasPermission(524288)) {
                        this.m_px[8].setSelected(true);
                    }
                    if (permissions.hasPermission(262144)) {
                        this.m_px[9].setSelected(true);
                    }
                    if (permissions.hasPermission(0x100000)) {
                        this.m_px[10].setSelected(true);
                    }
                } else {
                    if (permissions.hasPermission(32)) {
                        this.m_px[2].setSelected(true);
                    }
                    if (permissions.hasPermission(16)) {
                        this.m_px[3].setSelected(true);
                    }
                    if (permissions.hasPermission(64)) {
                        this.m_px[4].setSelected(true);
                    }
                    if (permissions.hasPermission(128)) {
                        this.m_px[5].setSelected(true);
                    }
                    if (permissions.hasPermission(65536)) {
                        this.m_px[7].setSelected(true);
                    }
                    if (permissions.hasPermission(131072)) {
                        this.m_px[8].setSelected(true);
                    }
                    if (permissions.hasPermission(524288)) {
                        this.m_px[9].setSelected(true);
                    }
                    if (permissions.hasPermission(262144)) {
                        this.m_px[10].setSelected(true);
                    }
                    if (permissions.hasPermission(0x100000)) {
                        this.m_px[11].setSelected(true);
                    }
                }
                break;
            }
        } catch (TibjmsAdminException var2_4) {
            JOptionPane.showMessageDialog(this, var2_4.getMessage(), "Error", 1);
        }
        this.m_perms.repaint();
    }

    public void populatePrincipals() {
        try {
            UserInfo[] arruserInfo = this.m_cn.m_adminConn.getUsers();
            for (int i = 0; i < arruserInfo.length; ++i) {
                if (arruserInfo[i].getName().equals("admin")) continue;
                String string = arruserInfo[i].getName() + " (user";
                string = arruserInfo[i].isExternal() ? string + ", external)" : string + ")";
                this.m_principal.addItem(string);
            }
            GroupInfo[] arrgroupInfo = this.m_cn.m_adminConn.getGroups();
            for (int j = 0; j < arrgroupInfo.length; ++j) {
                if (arrgroupInfo[j].getName().equals("$admin")) continue;
                String string = arrgroupInfo[j].getName() + " (group";
                string = arrgroupInfo[j].isExternal() ? string + ", external)" : string + ")";
                this.m_principal.addItem(string);
            }
            this.m_selected = (String) this.m_principal.getSelectedItem();
        } catch (TibjmsAdminException var1_2) {
            System.err.println("JMSException: " + var1_2.getMessage());
        }
    }

    public GemsPermissionDialog(JFrame jFrame, GemsConnectionNode gemsConnectionNode, String string, String string2, String string3, String string4, String string5) {
        super(jFrame, "Set " + string2 + " Permissions", true);
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_isQueue = string2.equals("Queue");
        String string6 = string3;
        string6 = string4.equals("User") ? string6 + " (user" : string6 + " (group";
        string6 = string5.equals("true") ? string6 + ", external)" : string6 + ")";
        this.buildFrame(jFrame, string);
        this.m_principal.setSelectedItem(string6);
        this.m_selected = (String) this.m_principal.getSelectedItem();
        this.updatePermissions(true);
        this.pack();
        this.show();
    }

    public String getSelectedProp() {
        return this.m_selected;
    }

    public void setPermissions() {
        try {
            DestinationInfo queueInfo;
            PrincipalInfo userInfo;
            Permissions permissions = new Permissions();
            Permissions permissions2 = new Permissions();


            if (this.m_isQueue) {
                queueInfo = new QueueInfo(this.m_dest.getText());
                if (this.m_px[2].isSelected()) {
                    permissions.setPermission(1, true);
                } else {
                    permissions2.setPermission(1, true);
                }
                if (this.m_px[3].isSelected()) {
                    permissions.setPermission(2, true);
                } else {
                    permissions2.setPermission(2, true);
                }
                if (this.m_px[4].isSelected()) {
                    permissions.setPermission(4, true);
                } else {
                    permissions2.setPermission(4, true);
                }
                if (this.m_px[6].isSelected()) {
                    permissions.setPermission(65536, true);
                } else {
                    permissions2.setPermission(65536, true);
                }
                if (this.m_px[7].isSelected()) {
                    permissions.setPermission(131072, true);
                } else {
                    permissions2.setPermission(131072, true);
                }
                if (this.m_px[8].isSelected()) {
                    permissions.setPermission(524288, true);
                } else {
                    permissions2.setPermission(524288, true);
                }
                if (this.m_px[9].isSelected()) {
                    permissions.setPermission(262144, true);
                } else {
                    permissions2.setPermission(262144, true);
                }
                if (this.m_px[10].isSelected()) {
                    permissions.setPermission(0x100000, true);
                } else {
                    permissions2.setPermission(0x100000, true);
                }
            } else {
                queueInfo = new TopicInfo(this.m_dest.getText());
                if (this.m_px[2].isSelected()) {
                    permissions.setPermission(32, true);
                } else {
                    permissions2.setPermission(32, true);
                }
                if (this.m_px[3].isSelected()) {
                    permissions.setPermission(16, true);
                } else {
                    permissions2.setPermission(16, true);
                }
                if (this.m_px[4].isSelected()) {
                    permissions.setPermission(64, true);
                } else {
                    permissions2.setPermission(64, true);
                }
                if (this.m_px[5].isSelected()) {
                    permissions.setPermission(128, true);
                } else {
                    permissions2.setPermission(128, true);
                }
                if (this.m_px[7].isSelected()) {
                    permissions.setPermission(65536, true);
                } else {
                    permissions2.setPermission(65536, true);
                }
                if (this.m_px[8].isSelected()) {
                    permissions.setPermission(131072, true);
                } else {
                    permissions2.setPermission(131072, true);
                }
                if (this.m_px[9].isSelected()) {
                    permissions.setPermission(524288, true);
                } else {
                    permissions2.setPermission(524288, true);
                }
                if (this.m_px[10].isSelected()) {
                    permissions.setPermission(262144, true);
                } else {
                    permissions2.setPermission(262144, true);
                }
                if (this.m_px[11].isSelected()) {
                    permissions.setPermission(0x100000, true);
                } else {
                    permissions2.setPermission(0x100000, true);
                }
            }

            int n = this.m_selected.lastIndexOf(" (user");
            if (n > 0) {
                userInfo = new UserInfo(this.m_selected.substring(0, n));
            } else {
                n = this.m_selected.lastIndexOf(" (group");
                userInfo = new GroupInfo(this.m_selected.substring(0, n));
            }

            if (!permissions.isEmpty()) {
                this.m_cn.m_adminConn.grant(new ACLEntry((DestinationInfo) queueInfo, (PrincipalInfo) userInfo, permissions));
            }
            if (!permissions2.isEmpty()) {
                this.m_cn.m_adminConn.revoke(new ACLEntry((DestinationInfo) queueInfo, (PrincipalInfo) userInfo, permissions2));
            }
            Gems.getGems().scheduleRepaint();
        } catch (TibjmsAdminException var1_4) {
            JOptionPane.showMessageDialog(this, var1_4.getMessage(), "Error", 1);
            this.updatePermissions(false);
        }
    }

    class NodeSelectionListener
            extends MouseAdapter {
        JTree tree;

        NodeSelectionListener(JTree jTree) {
            this.tree = jTree;
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            int n;
            int n2 = mouseEvent.getX();
            int n3 = this.tree.getRowForLocation(n2, n = mouseEvent.getY());
            TreePath treePath = this.tree.getPathForRow(n3);
            if (treePath != null) {
                CheckNode checkNode = (CheckNode) treePath.getLastPathComponent();
                boolean bl = !checkNode.isSelected();
                checkNode.setSelected(bl);
                if (checkNode.getSelectionMode() == 4) {
                    this.tree.expandPath(treePath);
                }
                ((DefaultTreeModel) this.tree.getModel()).nodeChanged(checkNode);
                if (n3 >= 0) {
                    this.tree.revalidate();
                    this.tree.repaint();
                }
            }
        }
    }

    class DestinationWizardAction
            implements ActionListener {
        DestinationWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsPermissionDialog.this.m_frame, GemsPermissionDialog.this.m_cn, GemsPermissionDialog.this.m_isQueue ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsPermissionDialog.this.m_dest.setText(gemsDestinationPicker.m_retDest.m_destName);
            }
        }
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPermissionDialog.this.m_cancelled = true;
            GemsPermissionDialog.this.dispose();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPermissionDialog.this.setPermissions();
            GemsPermissionDialog.this.dispose();
        }
    }

    class ApplyPressed
            implements ActionListener {
        ApplyPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPermissionDialog.this.setPermissions();
        }
    }

    class PrincipalSelected
            implements ActionListener {
        PrincipalSelected() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPermissionDialog.this.m_selected = (String) GemsPermissionDialog.this.m_principal.getSelectedItem();
            GemsPermissionDialog.this.updatePermissions(false);
        }
    }

    class ResetPressed
            implements ActionListener {
        ResetPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPermissionDialog.this.updatePermissions(false);
        }
    }

}

