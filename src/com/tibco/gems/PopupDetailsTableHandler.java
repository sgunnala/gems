/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class PopupDetailsTableHandler
        extends PopupTableHandler {
    public AbstractAction bq = null;
    public AbstractAction qp = null;
    public AbstractAction stm = null;
    public AbstractAction smm = null;
    public AbstractAction ts = null;
    public AbstractAction tp = null;
    public AbstractAction ptm = null;
    public AbstractAction pmm = null;
    public AbstractAction sp = null;
    public AbstractAction rss = null;
    public AbstractAction dss = null;
    public AbstractAction ess = null;
    public GemsDetailsTableModel m_model;

    public PopupDetailsTableHandler(JTable jTable, GemsDetailsTableModel gemsDetailsTableModel) {
        super(jTable);
        this.m_model = gemsDetailsTableModel;
    }

    public JPopupMenu createPopup(Point point) {
        JPopupMenu jPopupMenu = super.createPopup(point);
        if (this.m_model.getColumnName(0).equals("QueueName")) {
            if (this.bq == null) {
                this.bq = new BrowseQueueAction("Browse Queue...", null);
            }
            if (this.qp == null) {
                this.qp = new SetPropertyAction("Queue Properties...", null);
            }
            if (this.stm == null) {
                this.stm = new SendTextMessageAction("Send TextMessage...", null);
            }
            if (this.smm == null) {
                this.smm = new SendMapMessageAction("Send MapMessage...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.bq);
            jPopupMenu.add(this.qp);
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.stm);
            jPopupMenu.add(this.smm);
        } else if (this.m_model.getColumnName(0).equals("TopicName")) {
            if (this.ts == null) {
                this.ts = new TopicSubscriberAction("Topic Subscriber...", null);
            }
            if (this.tp == null) {
                this.tp = new SetPropertyAction("Topic Properties...", null);
            }
            if (this.ptm == null) {
                this.ptm = new PublishTextMessageAction("Publish TextMessage...", null);
            }
            if (this.pmm == null) {
                this.pmm = new PublishMapMessageAction("Publish MapMessage...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.ts);
            jPopupMenu.add(this.tp);
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.ptm);
            jPopupMenu.add(this.pmm);
        } else if (this.m_model.getColumnName(0).equals("Identifier")) {
            if (this.rss == null) {
                this.rss = new SSRefreshAction("Refresh", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.rss);
            jPopupMenu.addSeparator();
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.getGems().getSelectedNode();
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Disabled")) {
                if (this.ess == null) {
                    this.ess = new SSEnableAction("Enable", null);
                }
                jPopupMenu.add(this.ess);
            } else {
                if (this.dss == null) {
                    this.dss = new SSDisableAction("Disable", null);
                }
                jPopupMenu.add(this.dss);
            }
        } else if (this.m_model.getColumnCount() == 2 && (this.m_model.getColumnName(0).equals("QueueProperty") || this.m_model.getColumnName(0).equals("TopicProperty"))) {
            if (this.sp == null) {
                this.sp = new SetPropertyAction("Set Property...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.sp);
        }
        return jPopupMenu;
    }

    public class SSDisableAction
            extends AbstractAction {
        public SSDisableAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            GemsSSNode gemsSSNode = Gems.getGems().getSSConnectionNode();
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.getGems().getSelectedNode();
            if (gemsConnectionNode != null) {
                String string = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                String string2 = ((String) ((DefaultMutableTreeNode) defaultMutableTreeNode.getParent()).getUserObject()).substring(5);
                String string3 = "";
                if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active R")) {
                    string3 = gemsSSNode.RunCommand("DISABLE,RID=" + string + ",INTF=" + string2);
                }
                if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active T")) {
                    string3 = gemsSSNode.RunCommand("DISABLE,TID=" + string + ",INTF=" + string2);
                }
                JOptionPane.showMessageDialog(Gems.getGems().m_frame, string3, "Disable SS Object", 1);
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class SSEnableAction
            extends AbstractAction {
        public SSEnableAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            GemsSSNode gemsSSNode = Gems.getGems().getSSConnectionNode();
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.getGems().getSelectedNode();
            if (gemsConnectionNode != null) {
                String string = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                String string2 = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 1);
                String string3 = ((String) ((DefaultMutableTreeNode) defaultMutableTreeNode.getParent()).getUserObject()).substring(5);
                String string4 = "";
                string4 = string2.equals("Recipe") ? gemsSSNode.RunCommand("ENABLE,RID=" + string + ",INTF=" + string3) : gemsSSNode.RunCommand("ENABLE,TID=" + string + ",INTF=" + string3);
                JOptionPane.showMessageDialog(Gems.getGems().m_frame, string4, "Enable SS Object", 1);
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class SSRefreshAction
            extends AbstractAction {
        public SSRefreshAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            GemsSSNode gemsSSNode = Gems.getGems().getSSConnectionNode();
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.getGems().getSelectedNode();
            if (gemsConnectionNode != null) {
                String string = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                String string2 = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 1);
                String string3 = ((String) ((DefaultMutableTreeNode) defaultMutableTreeNode.getParent()).getUserObject()).substring(5);
                String string4 = "";
                if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active R")) {
                    string4 = gemsSSNode.RunCommand("REFRESH,RID=" + string + ",INTF=" + string3);
                }
                if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active T")) {
                    string4 = gemsSSNode.RunCommand("REFRESH,TID=" + string + ",INTF=" + string3);
                }
                if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Disabled")) {
                    string4 = string2.equals("Recipe") ? gemsSSNode.RunCommand("REFRESH,RID=" + string + ",INTF=" + string3) : gemsSSNode.RunCommand("REFRESH,TID=" + string + ",INTF=" + string3);
                }
                JOptionPane.showMessageDialog(Gems.getGems().m_frame, string4, "Refresh SS Object", 1);
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class BrowseQueueAction
            extends AbstractAction {
        public BrowseQueueAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsQueueBrowser(gemsConnectionNode, (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0));
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class SendTextMessageAction
            extends AbstractAction {
        public SendTextMessageAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsMessageFrame(gemsConnectionNode, true, (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0), true, Gems.getGems().m_frame, false);
            }
        }

        public boolean isEnabled() {
            return !Gems.getGems().getViewOnlyMode();
        }
    }

    public class SendMapMessageAction
            extends AbstractAction {
        public SendMapMessageAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsMessageFrame(gemsConnectionNode, true, (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0), true, Gems.getGems().m_frame, false, true);
            }
        }

        public boolean isEnabled() {
            return !Gems.getGems().getViewOnlyMode();
        }
    }

    public class TopicSubscriberAction
            extends AbstractAction {
        public TopicSubscriberAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsTopicSubscriber(gemsConnectionNode, (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0), "Topic Subscriber");
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class PublishMapMessageAction
            extends AbstractAction {
        public PublishMapMessageAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsMessageFrame(gemsConnectionNode, true, (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0), false, Gems.getGems().m_frame, false, true);
            }
        }

        public boolean isEnabled() {
            return !Gems.getGems().getViewOnlyMode();
        }
    }

    public class PublishTextMessageAction
            extends AbstractAction {
        public PublishTextMessageAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsMessageFrame(gemsConnectionNode, true, (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0), false, Gems.getGems().m_frame, false);
            }
        }

        public boolean isEnabled() {
            return !Gems.getGems().getViewOnlyMode();
        }
    }

    public class SetPropertyAction
            extends AbstractAction {
        public SetPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformedNEW(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            String string = null;
            String string2 = null;
            if (gemsConnectionNode != null) {
                if (PopupDetailsTableHandler.this.m_model.getColumnName(0).equals("TopicName")) {
                    string = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                    GemsDestPropEditor gemsDestPropEditor = new GemsDestPropEditor(Gems.getGems().m_frame, gemsConnectionNode, "Topic", string);
                } else if (PopupDetailsTableHandler.this.m_model.getColumnName(0).equals("QueueName")) {
                    string = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                    GemsDestPropEditor gemsDestPropEditor = new GemsDestPropEditor(Gems.getGems().m_frame, gemsConnectionNode, "Queue", string);
                } else {
                    DefaultMutableTreeNode defaultMutableTreeNode;
                    DefaultMutableTreeNode defaultMutableTreeNode2 = Gems.getGems().getSelectedNode();
                    if (defaultMutableTreeNode2 != null && (defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode2.getParent()) != null) {
                        string2 = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                        if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                            ((GemsTopicNode) defaultMutableTreeNode2).setProperty(gemsConnectionNode, string2);
                        } else if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Queues")) {
                            ((GemsQueueNode) defaultMutableTreeNode2).setProperty(gemsConnectionNode, string2);
                        }
                    }
                }
            }
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.getGems().getConnectionNode();
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.getGems().getSelectedNode();
            String string = null;
            String string2 = null;
            if (gemsConnectionNode != null && defaultMutableTreeNode != null) {
                DefaultMutableTreeNode defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
                String string3 = (String) defaultMutableTreeNode.getUserObject();
                if (string3.startsWith("Topics") || string3.startsWith("Queues")) {
                    string = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                    if (string != null) {
                        Enumeration enumeration = defaultMutableTreeNode.children();
                        while (enumeration.hasMoreElements()) {
                            DefaultMutableTreeNode defaultMutableTreeNode3 = (DefaultMutableTreeNode) enumeration.nextElement();
                            if (defaultMutableTreeNode3 == null || !string.equals((String) defaultMutableTreeNode3.getUserObject()))
                                continue;
                            String string4 = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                            if (PopupDetailsTableHandler.this.m_model.getColumnName(0).equals("TopicName")) {
                                GemsDestPropEditor gemsDestPropEditor = new GemsDestPropEditor(Gems.getGems().m_frame, gemsConnectionNode, "Topic", string4);
                            } else {
                                GemsDestPropEditor gemsDestPropEditor = new GemsDestPropEditor(Gems.getGems().m_frame, gemsConnectionNode, "Queue", string4);
                            }
                            return;
                        }
                    }
                } else if (defaultMutableTreeNode2 != null) {
                    string2 = (String) PopupDetailsTableHandler.this.m_table.getModel().getValueAt(PopupDetailsTableHandler.this.m_row, 0);
                    if (((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                        ((GemsTopicNode) defaultMutableTreeNode).setProperty(gemsConnectionNode, string2);
                    } else {
                        ((GemsQueueNode) defaultMutableTreeNode).setProperty(gemsConnectionNode, string2);
                    }
                }
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

}

