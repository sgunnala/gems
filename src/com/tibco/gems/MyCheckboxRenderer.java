/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class MyCheckboxRenderer
        extends JCheckBox
        implements TableCellRenderer {
    public MyCheckboxRenderer() {
        this.setHorizontalAlignment(0);
    }

    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
        Color color = jTable.getBackground();
        this.setBackground(color);
        this.setSelected(object != null && (Boolean) object != false);
        return this;
    }
}

