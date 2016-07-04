/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.ServerInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 */
package com.tibco.gems;

import java.util.Enumeration;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.tibco.tibjms.admin.ServerInfo;
import com.tibco.tibjms.admin.TibjmsAdmin;

public class GemsTreeModel
        extends DefaultTreeModel
        implements TreeSelectionListener {
    DefaultMutableTreeNode m_root;
    JTree m_tree;
    Gems m_gems;

    public GemsTreeModel(DefaultMutableTreeNode defaultMutableTreeNode) {
        super(defaultMutableTreeNode);
        this.m_root = defaultMutableTreeNode;
    }

    public void valueForPathChanged(TreePath treePath, Object object) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        this.nodeChanged(defaultMutableTreeNode);
    }

    public GemsConnectionNode newJMSConnection(DefaultMutableTreeNode defaultMutableTreeNode, String string) {
        GemsConnectionNode gemsConnectionNode = new GemsConnectionNode(string);
        defaultMutableTreeNode.add(gemsConnectionNode);
        return gemsConnectionNode;
    }

    public void reloadTree() {
        this.reload();
        this.m_tree.expandPath(new TreePath(this.m_root.getPath()));
        this.m_tree.setSelectionPath(new TreePath(this.m_root.getPath()));
    }

    public GemsConnectionNode newJMSConnection(String string) {
        GemsConnectionNode gemsConnectionNode = new GemsConnectionNode(string);
        this.m_root.add(gemsConnectionNode);
        this.reload();
        this.m_tree.expandPath(new TreePath(gemsConnectionNode.getPath()));
        this.m_tree.setSelectionPath(new TreePath(gemsConnectionNode.getPath()));
        return gemsConnectionNode;
    }

    public boolean connectCurrentNode(String string, String string2, String string3, String string4) {
        TreePath treePath = this.m_tree.getSelectionPath();
        if (treePath == null) {
            return false;
        }
        if (!(treePath.getLastPathComponent() instanceof GemsConnectionNode)) {
            return false;
        }
        GemsConnectionNode gemsConnectionNode = (GemsConnectionNode) treePath.getLastPathComponent();
        if (gemsConnectionNode == null) {
            return false;
        }
        gemsConnectionNode.connect(string, string2, string3, string4);
        this.reload();
        this.m_tree.expandPath(new TreePath(gemsConnectionNode.getPath()));
        this.m_tree.setSelectionPath(new TreePath(gemsConnectionNode.getPath()));
        if (gemsConnectionNode.isConnected()) {
            return true;
        }
        return false;
    }

    public void disconnectCurrentNode() {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        GemsConnectionNode gemsConnectionNode = null;
        while (defaultMutableTreeNode.getLevel() > 0) {
            if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                break;
            }
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        }
        if (gemsConnectionNode != null) {
            gemsConnectionNode.disconnect();
            this.reload();
            this.m_tree.expandPath(new TreePath(defaultMutableTreeNode.getPath()));
            this.m_tree.setSelectionPath(new TreePath(defaultMutableTreeNode.getPath()));
        }
    }

    public void detailsPanelDoubleClick(String string) {
        if (string == null) {
            return;
        }
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        if (defaultMutableTreeNode == null) {
            return;
        }
        this.m_tree.expandPath(new TreePath(defaultMutableTreeNode.getPath()));
        String string2 = (String) defaultMutableTreeNode.getUserObject();
        DefaultMutableTreeNode defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        String string3 = null;
        if (defaultMutableTreeNode2 != null) {
            string3 = (String) defaultMutableTreeNode2.getUserObject();
        }
        TreePath treePath = this.m_tree.getNextMatch(string, this.m_tree.getRowForPath(new TreePath(defaultMutableTreeNode.getPath())), Position.Bias.Forward);
        if (string2.startsWith("Topics")) {
            if (treePath == null) {
                this.createTopic(string, true);
            } else {
                this.m_tree.setSelectionPath(treePath);
            }
        } else if (string2.startsWith("Queues")) {
            if (treePath == null) {
                this.createQueue(string, true);
            } else {
                this.m_tree.setSelectionPath(treePath);
            }
        } else if (!Gems.getGems().getViewOnlyMode() && defaultMutableTreeNode2 != null && string3.startsWith("Topics")) {
            ((GemsTopicNode) defaultMutableTreeNode).setProperty((GemsConnectionNode) defaultMutableTreeNode2.getParent(), string);
        } else if (!Gems.getGems().getViewOnlyMode() && defaultMutableTreeNode2 != null && string3.startsWith("Queues")) {
            ((GemsQueueNode) defaultMutableTreeNode).setProperty((GemsConnectionNode) defaultMutableTreeNode2.getParent(), string);
        } else if (!Gems.getGems().getViewOnlyMode() && string2.equals("ACLs")) {
            GemsPermissionDialog gemsPermissionDialog = new GemsPermissionDialog(Gems.getGems().m_frame, (GemsConnectionNode) defaultMutableTreeNode.getParent(), string, Gems.getGems().getDetailsPanel().getModel().getSelectedCol(2), Gems.getGems().getDetailsPanel().getModel().getSelectedCol(3), Gems.getGems().getDetailsPanel().getModel().getSelectedCol(4), Gems.getGems().getDetailsPanel().getModel().getSelectedCol(5));
        } else if (!Gems.getGems().getViewOnlyMode() && string2.equals("AdminACLs")) {
            GemsAdminPermissionDialog gemsAdminPermissionDialog = new GemsAdminPermissionDialog(Gems.getGems().m_frame, (GemsConnectionNode) defaultMutableTreeNode.getParent(), string, Gems.getGems().getDetailsPanel().getModel().getSelectedCol(2), Gems.getGems().getDetailsPanel().getModel().getSelectedCol(3));
        } else if (treePath != null) {
            this.m_tree.setSelectionPath(treePath);
        }
    }

    public void createTopic(String string, boolean bl) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        GemsConnectionNode gemsConnectionNode = null;
        while (defaultMutableTreeNode.getLevel() > 0) {
            if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                break;
            }
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        }
        if (gemsConnectionNode != null) {
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode2 = gemsConnectionNode.createTopic(string, bl);
            if (defaultMutableTreeNode2 == null) {
                return;
            }
            this.reload();
            this.m_tree.expandPath(new TreePath(((DefaultMutableTreeNode) defaultMutableTreeNode2.getParent()).getPath()));
            this.m_tree.setSelectionPath(new TreePath(defaultMutableTreeNode2.getPath()));
        }
    }

    public void createQueue(String string, boolean bl) {
        GemsConnectionNode gemsConnectionNode = null;
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();

        while (selectedNode.getLevel() > 0) {
            if (selectedNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) selectedNode;
                break;
            }

            selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
        }

        if (gemsConnectionNode != null) {
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }

            DefaultMutableTreeNode queueNode = gemsConnectionNode.createQueue(string, bl);
            if (queueNode == null) {
                return;
            }

            reload();
            m_tree.expandPath(new TreePath(((DefaultMutableTreeNode) queueNode.getParent()).getPath()));
            m_tree.setSelectionPath(new TreePath(queueNode.getPath()));
        }
    }

    public void setServerTrace() {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        if (defaultMutableTreeNode == null) {
            return;
        }
        GemsConnectionNode gemsConnectionNode = null;
        while (defaultMutableTreeNode.getLevel() > 0) {
            if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                break;
            }
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        }
        if (gemsConnectionNode != null) {
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            GemsTraceDialog gemsTraceDialog = new GemsTraceDialog(Gems.getGems().m_frame, gemsConnectionNode);
        }
    }

    public void serverPanelDoubleClick(String string) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        if (defaultMutableTreeNode == null) {
            return;
        }
        GemsConnectionNode gemsConnectionNode = null;
        while (defaultMutableTreeNode.getLevel() > 0) {
            if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                break;
            }
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        }
        if (gemsConnectionNode != null) {
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            gemsConnectionNode.setServerProperty(string);
        }
    }

    public void serverMonitorDoubleClick(String string) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        if (defaultMutableTreeNode == null) {
            return;
        }
        this.m_tree.expandPath(new TreePath(defaultMutableTreeNode.getPath()));
        TreePath treePath = this.find2(this.m_tree, new TreePath(defaultMutableTreeNode.getPath()), string, defaultMutableTreeNode.getLevel(), true);
        if (treePath != null) {
            this.m_tree.setSelectionPath(treePath);
        }
    }

    public TreePath findByName(JTree jTree, String string) {
        TreeNode treeNode = (TreeNode) jTree.getModel().getRoot();
        return this.find2(jTree, new TreePath(treeNode), string, 0, true);
    }

    public void removeQueue(String string) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        while (!(defaultMutableTreeNode instanceof GemsConnectionNode)) {
            if ((defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent()) != null) continue;
            return;
        }
        if (!Gems.getGems().isStandbyOpsAllowed((GemsConnectionNode) defaultMutableTreeNode)) {
            return;
        }
        defaultMutableTreeNode = ((GemsConnectionNode) defaultMutableTreeNode).removeQueue(string);
        this.m_tree.updateUI();
        this.reload();
        this.m_tree.expandPath(new TreePath(defaultMutableTreeNode.getPath()));
        this.m_tree.setSelectionPath(new TreePath(defaultMutableTreeNode.getPath()));
    }

    public ServerInfo getSelectedJmsServerInfo() {
        TreePath treePath = this.m_tree.getSelectionPath();
        if (treePath != null) {
            DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (defaultMutableTreeNode == null) {
                return null;
            }
            if (defaultMutableTreeNode.getLevel() < 0) {
                return null;
            }
            GemsConnectionNode gemsConnectionNode = null;
            while (defaultMutableTreeNode.getLevel() > 0) {
                if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                    gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                    break;
                }
                defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }
            System.out.println("In getSelectedJmsServerInfo!");
            if (gemsConnectionNode != null) {
                return gemsConnectionNode.getJmsServerInfo(false);
            }
        }
        return null;
    }

    public TibjmsAdmin getSelectedJmsAdmin() {
        TreePath treePath = this.m_tree.getSelectionPath();
        if (treePath != null) {
            DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (defaultMutableTreeNode == null) {
                return null;
            }
            if (defaultMutableTreeNode.getLevel() < 0) {
                return null;
            }
            GemsConnectionNode gemsConnectionNode = null;
            while (defaultMutableTreeNode.getLevel() > 0) {
                if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                    gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                    break;
                }
                defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }
            if (gemsConnectionNode != null) {
                return gemsConnectionNode.getJmsAdmin();
            }
        }
        return null;
    }

    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getLastSelectedPathComponent();
        TreePath treePath = this.m_tree.getSelectionPath();
        if (treePath != null) {
            defaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        }
        if (defaultMutableTreeNode == null) {
            return;
        }
        this.m_gems.treeSelectionChange(defaultMutableTreeNode, false);
    }

    public void removeTopic(String string) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        GemsConnectionNode gemsConnectionNode = null;
        while (defaultMutableTreeNode.getLevel() > 0) {
            if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                break;
            }
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        }
        if (gemsConnectionNode != null) {
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            defaultMutableTreeNode = gemsConnectionNode.removeTopic(string);
            if (defaultMutableTreeNode == null) {
                return;
            }
            this.m_tree.updateUI();
            this.reload();
            this.m_tree.expandPath(new TreePath(defaultMutableTreeNode.getPath()));
            this.m_tree.setSelectionPath(new TreePath(defaultMutableTreeNode.getPath()));
        }
    }

    public void purgeTopic(String string) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        GemsConnectionNode gemsConnectionNode = null;
        while (defaultMutableTreeNode.getLevel() > 0) {
            if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                break;
            }
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        }
        if (gemsConnectionNode != null) {
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            gemsConnectionNode.purgeTopic(string);
        }
    }

    public void purgeQueue(String string) {
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) this.m_tree.getSelectionPath().getLastPathComponent();
        GemsConnectionNode gemsConnectionNode = null;
        while (defaultMutableTreeNode.getLevel() > 0) {
            if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode;
                break;
            }
            defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
        }
        if (gemsConnectionNode != null) {
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            gemsConnectionNode.purgeQueue(string);
        }
    }

    private TreePath find2(JTree jTree, TreePath treePath, Object object, int n, boolean bl) {
        TreeNode treeNode;
        Object object2 = treeNode = (TreeNode) treePath.getLastPathComponent();
        if (bl) {
            object2 = object2.toString();
        }
        if (object2.equals(object)) {
            return treePath;
        }
        if (treeNode.getChildCount() >= 0) {
            Enumeration enumeration = treeNode.children();
            while (enumeration.hasMoreElements()) {
                TreeNode treeNode2 = (TreeNode) enumeration.nextElement();
                TreePath treePath2 = treePath.pathByAddingChild(treeNode2);
                TreePath treePath3 = this.find2(jTree, treePath2, object, n + 1, bl);
                if (treePath3 == null) continue;
                return treePath3;
            }
        }
        return null;
    }
}

