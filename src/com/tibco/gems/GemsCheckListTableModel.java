/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GemsCheckListTableModel
        extends DefaultTableModel {
    JTable m_table;
    String m_header;
    Object m_obj = new Object();
    MyCheckboxRenderer m_checkRenderer = new MyCheckboxRenderer();

    public GemsCheckListTableModel(String string) {
        this.m_header = string;
    }

    public Class getColumnClass(int n) {
        Object object = this.getValueAt(0, n);
        if (object != null) {
            return object.getClass();
        }
        return String.class;
    }

    public boolean isCellEditable(int n, int n2) {
        if (n2 < 1) {
            return true;
        }
        return false;
    }

    public Vector getSelectedObjects() {
        Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i < this.getRowCount(); ++i) {
            if (!((Boolean) this.m_table.getValueAt(i, 0)).booleanValue()) continue;
            vector.add(this.m_table.getValueAt(i, 1));
        }
        return vector;
    }

    public void selectAllRows() {
        for (int i = 0; i < this.getRowCount(); ++i) {
            this.m_table.setValueAt(new Boolean(true), i, 0);
        }
    }

    public void toggleSelectedRow() {
        this.m_table.setValueAt(new Boolean((Boolean) this.m_table.getValueAt(this.m_table.getSelectedRow(), 0) == false), this.m_table.getSelectedRow(), 0);
    }

    public void buildColumnHeaders() {
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_table.setDefaultRenderer(Boolean.class, this.m_checkRenderer);
        Object[] arrobject = new String[]{"", this.m_header};
        this.setColumnIdentifiers(arrobject);
        this.m_table.getColumn("").setMaxWidth(30);
    }

    public void addObject(boolean bl, Object object) {
        if (object != null) {
            Object[] arrobject = new Object[]{new Boolean(bl), object};
            this.addRow(arrobject);
        }
    }
}

