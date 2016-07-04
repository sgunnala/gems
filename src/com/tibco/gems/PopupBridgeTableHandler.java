/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class PopupBridgeTableHandler
        extends PopupTableHandler {
    public AbstractAction selAll = null;
    public AbstractAction createTar = null;
    public AbstractAction delBridge = null;
    public AbstractAction delSelBridges = null;
    public GemsBridgeTableModel m_model;
    GemsManageBridgesDialog m_manager = null;

    public PopupBridgeTableHandler(JTable jTable, GemsBridgeTableModel gemsBridgeTableModel, GemsManageBridgesDialog gemsManageBridgesDialog) {
        super(jTable);
        this.m_model = gemsBridgeTableModel;
        this.m_manager = gemsManageBridgesDialog;
    }

    public JPopupMenu createPopup(Point point) {
        JPopupMenu jPopupMenu = super.createPopup(point);
        if (this.m_model.m_showCheckbox) {
            if (this.selAll == null) {
                this.selAll = new TableSelectAllAction("Select All", null);
            }
            jPopupMenu.insert(this.selAll, 1);
        }
        if (!Gems.getGems().getViewOnlyMode()) {
            if (this.createTar == null) {
                this.createTar = new CreateTargetAction("Create New Target...", null);
            }
            if (this.delBridge == null) {
                this.delBridge = new DeleteBridgeAction("Destroy Bridge", null);
            }
            if (this.delSelBridges == null) {
                this.delSelBridges = new DeleteSelectedBridgesAction("Destroy Selected Bridges", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.createTar);
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.delBridge);
            jPopupMenu.add(this.delSelBridges);
        }
        return jPopupMenu;
    }

    public class DeleteSelectedBridgesAction
            extends AbstractAction {
        public DeleteSelectedBridgesAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupBridgeTableHandler.this.m_manager.deleteSelectedBridges();
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class DeleteBridgeAction
            extends AbstractAction {
        public DeleteBridgeAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String string = PopupBridgeTableHandler.this.m_table.getModel().getValueAt(PopupBridgeTableHandler.this.m_row, 1).toString();
            String string2 = PopupBridgeTableHandler.this.m_table.getModel().getValueAt(PopupBridgeTableHandler.this.m_row, 2).toString();
            String string3 = PopupBridgeTableHandler.this.m_table.getModel().getValueAt(PopupBridgeTableHandler.this.m_row, 3).toString();
            String string4 = PopupBridgeTableHandler.this.m_table.getModel().getValueAt(PopupBridgeTableHandler.this.m_row, 4).toString();
            PopupBridgeTableHandler.this.m_manager.deleteBridge(string, string2, string3, string4);
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class CreateTargetAction
            extends AbstractAction {
        public CreateTargetAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String string = PopupBridgeTableHandler.this.m_table.getModel().getValueAt(PopupBridgeTableHandler.this.m_row, 1).toString();
            PopupBridgeTableHandler.this.m_manager.createBridge(string);
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class TableSelectAllAction
            extends AbstractAction {
        public TableSelectAllAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupBridgeTableHandler.this.m_model.selectAllRows();
        }
    }

}

