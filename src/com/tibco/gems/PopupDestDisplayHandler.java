/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class PopupDestDisplayHandler
        extends PopupTableHandler {
    public AbstractAction bq = null;
    public AbstractAction qp = null;
    public AbstractAction stm = null;
    public AbstractAction smm = null;
    public AbstractAction ts = null;
    public AbstractAction tp = null;
    public AbstractAction ptm = null;
    public AbstractAction pmm = null;
    public GemsDetailsTableModel m_model;
    public GemsConnectionNode m_cn;
    public JFrame m_frame;

    public PopupDestDisplayHandler(JTable jTable, GemsDetailsTableModel gemsDetailsTableModel, GemsConnectionNode gemsConnectionNode, JFrame jFrame) {
        super(jTable);
        this.m_model = gemsDetailsTableModel;
        this.m_cn = gemsConnectionNode;
        this.m_frame = jFrame;
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
        }
        return jPopupMenu;
    }

    public class BrowseQueueAction
            extends AbstractAction {
        public BrowseQueueAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (PopupDestDisplayHandler.this.m_cn != null) {
                new GemsQueueBrowser(PopupDestDisplayHandler.this.m_cn, (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0));
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
            if (PopupDestDisplayHandler.this.m_cn != null) {
                new GemsMessageFrame(PopupDestDisplayHandler.this.m_cn, true, (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0), true, PopupDestDisplayHandler.this.m_frame, false);
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
            if (PopupDestDisplayHandler.this.m_cn != null) {
                new GemsMessageFrame(PopupDestDisplayHandler.this.m_cn, true, (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0), true, PopupDestDisplayHandler.this.m_frame, false, true);
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
            if (PopupDestDisplayHandler.this.m_cn != null) {
                new GemsTopicSubscriber(PopupDestDisplayHandler.this.m_cn, (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0), "Topic Subscriber");
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
            if (PopupDestDisplayHandler.this.m_cn != null) {
                new GemsMessageFrame(PopupDestDisplayHandler.this.m_cn, true, (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0), false, PopupDestDisplayHandler.this.m_frame, false, true);
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
            if (PopupDestDisplayHandler.this.m_cn != null) {
                new GemsMessageFrame(PopupDestDisplayHandler.this.m_cn, true, (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0), false, PopupDestDisplayHandler.this.m_frame, false);
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

        public void actionPerformed(ActionEvent actionEvent) {
            String string = null;
            Object var3_3 = null;
            if (PopupDestDisplayHandler.this.m_cn != null) {
                if (PopupDestDisplayHandler.this.m_model.getColumnName(0).equals("TopicName")) {
                    string = (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0);
                    GemsDestPropEditor gemsDestPropEditor = new GemsDestPropEditor(PopupDestDisplayHandler.this.m_frame, PopupDestDisplayHandler.this.m_cn, "Topic", string);
                } else if (PopupDestDisplayHandler.this.m_model.getColumnName(0).equals("QueueName")) {
                    string = (String) PopupDestDisplayHandler.this.m_table.getModel().getValueAt(PopupDestDisplayHandler.this.m_row, 0);
                    GemsDestPropEditor gemsDestPropEditor = new GemsDestPropEditor(PopupDestDisplayHandler.this.m_frame, PopupDestDisplayHandler.this.m_cn, "Queue", string);
                }
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

}

