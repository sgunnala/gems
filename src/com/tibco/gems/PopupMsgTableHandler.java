/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.Tibjms
 *  javax.jms.MapMessage
 *  javax.jms.Message
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.swing.*;

import com.tibco.tibjms.Tibjms;

public class PopupMsgTableHandler
        extends PopupTableHandler {
    public AbstractAction openReqMsg = null;
    public AbstractAction openRepMsg = null;
    public AbstractAction openOrigMsg = null;
    public AbstractAction openMsg = null;
    public AbstractAction addProp = null;
    public AbstractAction selAll = null;
    public AbstractAction clrEvents = null;
    public AbstractAction origEvent = null;
    public GemsMessageTableModel m_model;

    public PopupMsgTableHandler(JTable jTable, GemsMessageTableModel gemsMessageTableModel) {
        super(jTable);
        this.m_model = gemsMessageTableModel;
    }

    public JPopupMenu createPopup(Point point) {
        JPopupMenu jPopupMenu = super.createPopup(point);
        if (this.m_model.m_showCheckbox) {
            if (this.selAll == null) {
                this.selAll = new TableSelectAllAction("Select All", null);
            }
            jPopupMenu.insert(this.selAll, 1);
        }
        if (this.m_model.getColumnName(0).equals("RequestMessageID")) {
            if (this.openReqMsg == null) {
                this.openReqMsg = new OpenMsgAction("Open Request Message...", null);
            }
            if (this.openRepMsg == null) {
                this.openRepMsg = new OpenReplyMsgAction("Open Reply Message...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.openReqMsg);
            jPopupMenu.add(this.openRepMsg);
        } else if (this.m_model.m_monitor) {
            if (this.openMsg == null) {
                this.openMsg = new OpenMsgAction("Open Monitor Message...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.openMsg);
            if (this.openOrigMsg == null) {
                this.openOrigMsg = new OpenOrigMsgAction("Open Original Message...", null);
            }
            jPopupMenu.add(this.openOrigMsg);
        } else {
            if (this.openMsg == null) {
                this.openMsg = new OpenMsgAction("Open Message...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.openMsg);
        }
        if (this.m_model.m_showEvents) {
            if (this.clrEvents == null) {
                this.clrEvents = new TableClearEventsAction("Clear Events", null);
            }
            if (this.origEvent == null) {
                this.origEvent = new OriginalEventAction("Open Original Message...", null);
            }
            jPopupMenu.add(this.origEvent);
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.clrEvents);
        }
        return jPopupMenu;
    }

    public class TableClearEventsAction
            extends AbstractAction {
        public TableClearEventsAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Gems.getGems().clearCurrentEventsDisplay();
        }
    }

    public class TableSelectAllAction
            extends AbstractAction {
        public TableSelectAllAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgTableHandler.this.m_model.selectAllRows();
        }
    }

    public class OpenReplyMsgAction
            extends AbstractAction {
        public OpenReplyMsgAction(String string, Icon icon) {
            super(string, icon);
        }

        public boolean isEnabled() {
            return true;
        }        public void actionPerformed(ActionEvent actionEvent) {
            Message message = PopupMsgTableHandler.this.m_model.getReplyMessageAt(PopupMsgTableHandler.this.m_row);
            if (message != null) {
                GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(null, false, null, false, null, false);
                gemsMessageFrame.populate(message);
            } else {
                JOptionPane.showMessageDialog(null, "There is no reply Message to view!", "View Reply Message", 1);
            }
        }


    }

    public class OpenMsgAction
            extends AbstractAction {
        boolean m_isMonitor;

        public OpenMsgAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Message message;
            String string = PopupMsgTableHandler.this.m_model.getColumnName(3);
            boolean bl = false;
            if (string != null && string.equals("EventReason")) {
                bl = true;
            }
            if ((message = PopupMsgTableHandler.this.m_model.getMessageAt(PopupMsgTableHandler.this.m_row)) != null) {
                GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(null, false, null, false, null, bl);
                gemsMessageFrame.populate(message);
            }
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class OpenOrigMsgAction
            extends AbstractAction {
        public OpenOrigMsgAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Message message = PopupMsgTableHandler.this.m_model.getMessageAt(PopupMsgTableHandler.this.m_row);
            if (message != null && message instanceof MapMessage) {
                try {
                    MapMessage mapMessage = (MapMessage) message;
                    if (mapMessage.itemExists("message_bytes")) {
                        Message message2 = Tibjms.createFromBytes((byte[]) mapMessage.getBytes("message_bytes"));
                        GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(null, false, null, false, null, false);
                        gemsMessageFrame.populate(message2);
                        return;
                    }
                } catch (Exception var3_4) {
                    // empty catch block
                }
            }
            JOptionPane.showMessageDialog(null, "There is no original message associated with this monitor message", "Error", 1);
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class OriginalEventAction
            extends AbstractAction {
        public OriginalEventAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Message message = PopupMsgTableHandler.this.m_model.getMessageAt(PopupMsgTableHandler.this.m_row);
            if (message != null && message instanceof MapMessage) {
                try {
                    MapMessage mapMessage = (MapMessage) message;
                    if (mapMessage.itemExists("message_bytes")) {
                        Message message2 = Tibjms.createFromBytes((byte[]) mapMessage.getBytes("message_bytes"));
                        GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(null, false, null, false, null, false);
                        gemsMessageFrame.populate(message2);
                        return;
                    }
                } catch (Exception var3_4) {
                    // empty catch block
                }
            }
            JOptionPane.showMessageDialog(null, "There is no original message associated with this monitor message", "Error", 1);
        }

        public boolean isEnabled() {
            String string = PopupMsgTableHandler.this.m_model.getValueAt(PopupMsgTableHandler.this.m_row, 2).toString();
            return string.equals("message");
        }
    }

}

