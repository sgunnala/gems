/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class PopupDestPropTableHandler
        extends PopupTableHandler {
    public AbstractAction sp = null;
    public GemsDestPropEditor m_editor;
    public GemsDetailsTableModel m_model;

    public PopupDestPropTableHandler(JTable jTable, GemsDetailsTableModel gemsDetailsTableModel, GemsDestPropEditor gemsDestPropEditor) {
        super(jTable);
        this.m_editor = gemsDestPropEditor;
        this.m_model = gemsDetailsTableModel;
    }

    public JPopupMenu createPopup(Point point) {
        JPopupMenu jPopupMenu = super.createPopup(point);
        if (this.m_model.getColumnCount() == 2 && (this.m_model.getColumnName(0).equals("QueueProperty") || this.m_model.getColumnName(0).equals("TopicProperty"))) {
            if (this.sp == null) {
                this.sp = new SetPropertyAction("Set Property...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.sp);
        }
        return jPopupMenu;
    }

    public class SetPropertyAction
            extends AbstractAction {
        public SetPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupDestPropTableHandler.this.m_editor.editRowProperty(PopupDestPropTableHandler.this.m_row);
        }

        public boolean isEnabled() {
            return !Gems.getGems().getViewOnlyMode();
        }
    }

}

