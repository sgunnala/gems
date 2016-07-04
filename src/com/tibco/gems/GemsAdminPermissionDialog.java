/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.AdminACLEntry
 *  com.tibco.tibjms.admin.AdminPermissions
 *  com.tibco.tibjms.admin.GroupInfo
 *  com.tibco.tibjms.admin.PrincipalInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
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
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.tibco.tibjms.admin.AdminACLEntry;
import com.tibco.tibjms.admin.AdminPermissions;
import com.tibco.tibjms.admin.GroupInfo;
import com.tibco.tibjms.admin.PrincipalInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;
import com.tibco.tibjms.admin.UserInfo;

public class GemsAdminPermissionDialog
        extends JDialog {
    protected JTree m_perms;
    protected JCheckBox m_isAdmin;
    protected JComboBox m_principal;
    protected boolean m_cancelled = false;
    protected String m_selected;
    protected CheckNode[] m_px;
    protected GemsConnectionNode m_cn;

    public GemsAdminPermissionDialog(Frame frame, GemsConnectionNode gemsConnectionNode) {
        super(frame, "Set Global Administrator Permissions", true);
        this.m_cn = gemsConnectionNode;
        this.buildFrame(frame);
        this.updatePermissions(false);
        this.pack();
        this.setVisible(true);
    }

    public void buildFrame(Frame frame) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel(new SpringLayout());
        jPanel.add(jPanel2);
        JLabel jLabel = new JLabel("Principal:", 11);
        this.m_principal = new JComboBox();
        this.populatePrincipals();
        this.m_principal.addActionListener(new PrincipalSelected());
        jPanel2.add(jLabel);
        jPanel2.add(this.m_principal);
        SpringUtilities.makeCompactGrid(jPanel2, 1, 2, 5, 5, 5, 5);
        this.m_px = new CheckNode[]{new CheckNode("Global Permissions:"), new CheckNode("All Admin Permissions:"), new CheckNode("View Permissions:"), new CheckNode("View ACL"), new CheckNode("View Admin ACL"), new CheckNode("View Connection"), new CheckNode("View Destination"), new CheckNode("View Durable"), new CheckNode("View Factory"), new CheckNode("View Group"), new CheckNode("View Message"), new CheckNode("View Route"), new CheckNode("View Server"), new CheckNode("View User"), new CheckNode("Modify Permissions:"), new CheckNode("Change ACL"), new CheckNode("Change Admin ACL"), new CheckNode("Change Connection"), new CheckNode("Change Durable"), new CheckNode("Change Factory"), new CheckNode("Change Group"), new CheckNode("Change Message"), new CheckNode("Change Route"), new CheckNode("Change Server"), new CheckNode("Change User"), new CheckNode("Create Destination"), new CheckNode("Delete Destination"), new CheckNode("Modify Destination"), new CheckNode("Purge Permissions:"), new CheckNode("Purge Destination"), new CheckNode("Purge Durable"), new CheckNode("Shutdown Permissions:"), new CheckNode("Shutdown Server"), new CheckNode("Protect Permissions:"), new CheckNode("Protect1"), new CheckNode("Protect2"), new CheckNode("Protect3"), new CheckNode("Protect4")};
        this.m_perms = new JTree(this.m_px[0]);
        this.m_perms.setUI(new MetalTreeUI());
        this.m_perms.setBorder(new EtchedBorder());
        this.m_perms.setCellRenderer(new CheckRenderer());
        this.m_perms.getSelectionModel().setSelectionMode(1);
        this.m_perms.putClientProperty("JTree.lineStyle", "Angled");
        this.m_perms.addMouseListener(new NodeSelectionListener(this.m_perms));
        int n = 0;
        for (int i = 1; i < this.m_px.length; ++i) {
            if (((String) this.m_px[i].getUserObject()).equals("Protect Permissions:")) {
                n = 0;
            } else if (((String) this.m_px[i].getUserObject()).endsWith(":") && n > 1) {
                n = 1;
            }
            this.m_px[n].add(this.m_px[i]);
            if (!((String) this.m_px[i].getUserObject()).endsWith(":")) continue;
            n = i;
        }
        this.m_perms.expandRow(0);
        this.m_perms.expandRow(1);
        JScrollPane jScrollPane = new JScrollPane(this.m_perms);
        jScrollPane.setPreferredSize(new Dimension(375, 450));
        JPanel jPanel3 = new JPanel();
        jPanel3.setBorder(new TitledBorder("Global Admin Permissions"));
        jPanel3.setLayout(new BoxLayout(jPanel3, 1));
        JPanel jPanel4 = new JPanel(new FlowLayout());
        JButton jButton = new JButton("Apply");
        jButton.addActionListener(new ApplyPressed());
        jPanel4.add(jButton);
        JButton jButton2 = new JButton("Reset");
        jButton2.addActionListener(new ResetPressed());
        jPanel4.add(jButton2);
        jPanel3.add(jScrollPane);
        JPanel jPanel5 = new JPanel(new FlowLayout());
        JButton jButton3 = new JButton("Permit All");
        JButton jButton4 = new JButton("Restrict All");
        jPanel5.add(jButton3);
        jPanel5.add(jButton4);
        jPanel3.add(jPanel4);
        jPanel.add(jPanel3);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new FlowLayout());
        JButton jButton5 = new JButton("OK");
        JButton jButton6 = new JButton("Cancel");
        jPanel6.add(jButton5);
        jPanel6.add(jButton6);
        jButton5.addActionListener(new OkPressed());
        jButton6.addActionListener(new CancelPressed());
        jPanel.add(jPanel6);
        this.setContentPane(jPanel);
        this.setLocationRelativeTo(frame);
    }

    public void updatePermissions(boolean bl) {
        if (bl) {
            return;
        }
        try {
            for (int i = 0; i < this.m_px.length; ++i) {
                this.m_px[i].setSelected(false);
            }
            Object[] arrobject = this.m_cn.m_adminConn.getPermissions();
            for (int j = 0; j < arrobject.length; ++j) {
                if (!(arrobject[j] instanceof AdminACLEntry)) continue;
                AdminACLEntry adminACLEntry = (AdminACLEntry) arrobject[j];
                String string = adminACLEntry.getPrincipal().getName();
                string = adminACLEntry.getPrincipal() instanceof UserInfo ? string + " (user" : string + " (group";
                if (!this.m_selected.startsWith(string)) continue;
                AdminPermissions adminPermissions = adminACLEntry.getPermissions();
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(8192) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(8192)) {
                    this.m_px[3].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(0x4000000) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(0x4000000)) {
                    this.m_px[4].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(16) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(16)) {
                    this.m_px[5].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(65536) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(65536)) {
                    this.m_px[6].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(64) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(64)) {
                    this.m_px[7].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(1) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(1)) {
                    this.m_px[8].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(2048) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(2048)) {
                    this.m_px[9].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(0x200000) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(0x200000)) {
                    this.m_px[10].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(4) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(4)) {
                    this.m_px[11].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(0x800000) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(0x800000)) {
                    this.m_px[12].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x2000000000000000L) || adminPermissions.hasPositivePermission(512) || adminPermissions.hasPositivePermission(0x4000000000000000L)) && !adminPermissions.hasNegativePermission(512)) {
                    this.m_px[13].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(16384)) && !adminPermissions.hasNegativePermission(16384)) {
                    this.m_px[15].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(0x8000000)) && !adminPermissions.hasNegativePermission(0x8000000)) {
                    this.m_px[16].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(32)) && !adminPermissions.hasNegativePermission(32)) {
                    this.m_px[17].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(128)) && !adminPermissions.hasNegativePermission(128)) {
                    this.m_px[18].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(2)) && !adminPermissions.hasNegativePermission(2)) {
                    this.m_px[19].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(4096)) && !adminPermissions.hasNegativePermission(4096)) {
                    this.m_px[20].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(0x400000)) && !adminPermissions.hasNegativePermission(0x400000)) {
                    this.m_px[21].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(8)) && !adminPermissions.hasNegativePermission(8)) {
                    this.m_px[22].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(0x1000000)) && !adminPermissions.hasNegativePermission(0x1000000)) {
                    this.m_px[23].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(1024)) && !adminPermissions.hasNegativePermission(1024)) {
                    this.m_px[24].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(131072)) && !adminPermissions.hasNegativePermission(131072)) {
                    this.m_px[25].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(262144)) && !adminPermissions.hasNegativePermission(262144)) {
                    this.m_px[26].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(524288)) && !adminPermissions.hasNegativePermission(524288)) {
                    this.m_px[27].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(0x100000)) && !adminPermissions.hasNegativePermission(0x100000)) {
                    this.m_px[29].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(256)) && !adminPermissions.hasNegativePermission(256)) {
                    this.m_px[30].setSelected(true);
                }
                if ((adminPermissions.hasPositivePermission(0x4000000000000000L) || adminPermissions.hasPositivePermission(0x2000000)) && !adminPermissions.hasNegativePermission(0x2000000)) {
                    this.m_px[32].setSelected(true);
                }
                if (adminPermissions.hasPositivePermission(0x80000000000000L) && !adminPermissions.hasNegativePermission(0x80000000000000L)) {
                    this.m_px[34].setSelected(true);
                }
                if (adminPermissions.hasPositivePermission(0x100000000000000L) && !adminPermissions.hasNegativePermission(0x100000000000000L)) {
                    this.m_px[35].setSelected(true);
                }
                if (adminPermissions.hasPositivePermission(0x200000000000000L) && !adminPermissions.hasNegativePermission(0x200000000000000L)) {
                    this.m_px[36].setSelected(true);
                }
                if (adminPermissions.hasPositivePermission(0x400000000000000L) && !adminPermissions.hasNegativePermission(0x400000000000000L)) {
                    this.m_px[37].setSelected(true);
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

    public GemsAdminPermissionDialog(Frame frame, GemsConnectionNode gemsConnectionNode, String string, String string2, String string3) {
        super(frame, "Set Global Administrator Permissions", true);
        this.m_cn = gemsConnectionNode;
        String string4 = string;
        string4 = string2.equals("User") ? string4 + " (user" : string4 + " (group";
        string4 = string3.equals("true") ? string4 + ", external)" : string4 + ")";
        this.buildFrame(frame);
        this.m_principal.setSelectedItem(string4);
        this.m_selected = (String) this.m_principal.getSelectedItem();
        this.updatePermissions(true);
        this.pack();
        this.setVisible(true);
    }

    public String getSelectedProp() {
        return this.m_selected;
    }

    public void setPermissions() {
        try {
            AdminPermissions adminPermissions = new AdminPermissions();
            AdminPermissions adminPermissions2 = new AdminPermissions();
            if (this.m_px[1].areAllChildrenSelected()) {
                adminPermissions.setPermission(0x4000000000000000L, true);
            } else if (!this.m_px[1].isSelected()) {
                adminPermissions2.setPermission(0x4000000000000000L, true);
            } else {
                if (this.m_px[2].areAllChildrenSelected()) {
                    adminPermissions.setPermission(0x2000000000000000L, true);
                } else if (!this.m_px[2].isSelected()) {
                    adminPermissions2.setPermission(0x2000000000000000L, true);
                } else {
                    if (this.m_px[3].isSelected()) {
                        adminPermissions.setPermission(8192, true);
                    } else {
                        adminPermissions2.setPermission(8192, true);
                    }
                    if (this.m_px[4].isSelected()) {
                        adminPermissions.setPermission(0x4000000, true);
                    } else {
                        adminPermissions2.setPermission(0x4000000, true);
                    }
                    if (this.m_px[5].isSelected()) {
                        adminPermissions.setPermission(16, true);
                    } else {
                        adminPermissions2.setPermission(16, true);
                    }
                    if (this.m_px[6].isSelected()) {
                        adminPermissions.setPermission(65536, true);
                    } else {
                        adminPermissions2.setPermission(65536, true);
                    }
                    if (this.m_px[7].isSelected()) {
                        adminPermissions.setPermission(64, true);
                    } else {
                        adminPermissions2.setPermission(64, true);
                    }
                    if (this.m_px[8].isSelected()) {
                        adminPermissions.setPermission(1, true);
                    } else {
                        adminPermissions2.setPermission(1, true);
                    }
                    if (this.m_px[9].isSelected()) {
                        adminPermissions.setPermission(2048, true);
                    } else {
                        adminPermissions2.setPermission(2048, true);
                    }
                    if (this.m_px[10].isSelected()) {
                        adminPermissions.setPermission(0x200000, true);
                    } else {
                        adminPermissions2.setPermission(0x200000, true);
                    }
                    if (this.m_px[11].isSelected()) {
                        adminPermissions.setPermission(4, true);
                    } else {
                        adminPermissions2.setPermission(4, true);
                    }
                    if (this.m_px[12].isSelected()) {
                        adminPermissions.setPermission(0x800000, true);
                    } else {
                        adminPermissions2.setPermission(0x800000, true);
                    }
                    if (this.m_px[13].isSelected()) {
                        adminPermissions.setPermission(512, true);
                    } else {
                        adminPermissions2.setPermission(512, true);
                    }
                }
                if (this.m_px[15].isSelected()) {
                    adminPermissions.setPermission(16384, true);
                } else {
                    adminPermissions2.setPermission(16384, true);
                }
                if (this.m_px[16].isSelected()) {
                    adminPermissions.setPermission(0x8000000, true);
                } else {
                    adminPermissions2.setPermission(0x8000000, true);
                }
                if (this.m_px[17].isSelected()) {
                    adminPermissions.setPermission(32, true);
                } else {
                    adminPermissions2.setPermission(32, true);
                }
                if (this.m_px[18].isSelected()) {
                    adminPermissions.setPermission(128, true);
                } else {
                    adminPermissions2.setPermission(128, true);
                }
                if (this.m_px[19].isSelected()) {
                    adminPermissions.setPermission(2, true);
                } else {
                    adminPermissions2.setPermission(2, true);
                }
                if (this.m_px[20].isSelected()) {
                    adminPermissions.setPermission(4096, true);
                } else {
                    adminPermissions2.setPermission(4096, true);
                }
                if (this.m_px[21].isSelected()) {
                    adminPermissions.setPermission(0x400000, true);
                } else {
                    adminPermissions2.setPermission(0x400000, true);
                }
                if (this.m_px[22].isSelected()) {
                    adminPermissions.setPermission(8, true);
                } else {
                    adminPermissions2.setPermission(8, true);
                }
                if (this.m_px[23].isSelected()) {
                    adminPermissions.setPermission(0x1000000, true);
                } else {
                    adminPermissions2.setPermission(0x1000000, true);
                }
                if (this.m_px[24].isSelected()) {
                    adminPermissions.setPermission(1024, true);
                } else {
                    adminPermissions2.setPermission(1024, true);
                }
                if (this.m_px[25].isSelected()) {
                    adminPermissions.setPermission(131072, true);
                } else {
                    adminPermissions2.setPermission(131072, true);
                }
                if (this.m_px[26].isSelected()) {
                    adminPermissions.setPermission(262144, true);
                } else {
                    adminPermissions2.setPermission(262144, true);
                }
                if (this.m_px[27].isSelected()) {
                    adminPermissions.setPermission(524288, true);
                } else {
                    adminPermissions2.setPermission(524288, true);
                }
                if (this.m_px[29].isSelected()) {
                    adminPermissions.setPermission(0x100000, true);
                } else {
                    adminPermissions2.setPermission(0x100000, true);
                }
                if (this.m_px[30].isSelected()) {
                    adminPermissions.setPermission(256, true);
                } else {
                    adminPermissions2.setPermission(256, true);
                }
                if (this.m_px[32].isSelected()) {
                    adminPermissions.setPermission(0x2000000, true);
                } else {
                    adminPermissions2.setPermission(0x2000000, true);
                }
            }
            if (this.m_px[34].isSelected()) {
                adminPermissions.setPermission(0x80000000000000L, true);
            } else {
                adminPermissions2.setPermission(0x80000000000000L, true);
            }
            if (this.m_px[35].isSelected()) {
                adminPermissions.setPermission(0x100000000000000L, true);
            } else {
                adminPermissions2.setPermission(0x100000000000000L, true);
            }
            if (this.m_px[36].isSelected()) {
                adminPermissions.setPermission(0x200000000000000L, true);
            } else {
                adminPermissions2.setPermission(0x200000000000000L, true);
            }
            if (this.m_px[37].isSelected()) {
                adminPermissions.setPermission(0x400000000000000L, true);
            } else {
                adminPermissions2.setPermission(0x400000000000000L, true);
            }

            PrincipalInfo userInfo;
            int n = this.m_selected.lastIndexOf(" (user");
            if (n > 0) {
                userInfo = new UserInfo(this.m_selected.substring(0, n));
            } else {
                n = this.m_selected.lastIndexOf(" (group");
                userInfo = new GroupInfo(this.m_selected.substring(0, n));
            }

            if (!adminPermissions2.isEmpty()) {
                this.m_cn.m_adminConn.revoke(new AdminACLEntry(userInfo, adminPermissions2));
            }

            if (!adminPermissions.isEmpty()) {
                this.m_cn.m_adminConn.grant(new AdminACLEntry(userInfo, adminPermissions));
            }

            if (adminPermissions2.hasPositivePermission(0x4000000000000000L)) {
                this.updatePermissions(false);
            }

            Gems.getGems().scheduleRepaint();
        } catch (TibjmsAdminException var1_5) {
            JOptionPane.showMessageDialog(this, var1_5.getMessage(), "Error", 1);
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

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsAdminPermissionDialog.this.m_cancelled = true;
            GemsAdminPermissionDialog.this.dispose();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsAdminPermissionDialog.this.setPermissions();
            GemsAdminPermissionDialog.this.dispose();
        }
    }

    class ApplyPressed
            implements ActionListener {
        ApplyPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsAdminPermissionDialog.this.setPermissions();
        }
    }

    class PrincipalSelected
            implements ActionListener {
        PrincipalSelected() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsAdminPermissionDialog.this.m_selected = (String) GemsAdminPermissionDialog.this.m_principal.getSelectedItem();
            GemsAdminPermissionDialog.this.updatePermissions(false);
        }
    }

    class ResetPressed
            implements ActionListener {
        ResetPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsAdminPermissionDialog.this.updatePermissions(false);
        }
    }

}

