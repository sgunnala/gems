/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class GemsTableListModel
        extends DefaultTableModel
        implements GemsTableListModelBase {
    GemsTableListModelBase _tableListModel;
    String[] m_arrColumnIdentifiers;
    JTable _table;

    public GemsTableListModel(GemsTableListModelBase gemsTableListModelBase) {
        this._tableListModel = gemsTableListModelBase;
    }

    public void removeAll() {
        int n = this.getRowCount();
        for (int i = 0; i < n; ++i) {
            this.removeRow(0);
        }
    }

    public JTable getTable() {
        return this._table;
    }

    public void setTable(JTable jTable) {
        this._table = jTable;
    }

    public void setColumnRenderer(int n, TableCellRenderer tableCellRenderer) {
        TableColumn tableColumn = this._table.getColumn(this.m_arrColumnIdentifiers[n]);
        if (tableColumn != null) {
            tableColumn.setCellRenderer(tableCellRenderer);
        }
    }

    public void setColumnSizes() {
        int[] arrn = this.getColumnSizes();
        this.setColumnSizes(arrn);
    }

    public int[] getColumnSizes() {
        return this._tableListModel.getColumnSizes();
    }

    public void setColumnSizes(int[] arrn) {
        int n = arrn.length <= this.getColumnCount() ? arrn.length : this.getColumnCount();
        String[] arrstring = this.m_arrColumnIdentifiers != null ? this.m_arrColumnIdentifiers : this.getColumnHeadings();
        for (int i = 0; i < n; ++i) {
            TableColumn tableColumn = this._table.getColumn(arrstring[i]);
            if (tableColumn != null) {
                tableColumn.setWidth(arrn[i]);
                tableColumn.setPreferredWidth(arrn[i]);
                continue;
            }
            System.out.println(" >>> TableListModelAdapter:setColumnSizes - column[" + i + "]=[" + arrstring[i] + "] is null, columnCount=" + this.getColumnCount());
        }
    }

    public String[] getColumnHeadings() {
        return this._tableListModel.getColumnHeadings();
    }

    public void setColumnHeadings(String[] arrstring) {
        this.m_arrColumnIdentifiers = arrstring;
        this.setColumnIdentifiers(this.m_arrColumnIdentifiers);
    }

    public void setColumnHeadings() {
        this.m_arrColumnIdentifiers = this.getColumnHeadings();
        this.setColumnIdentifiers(this.m_arrColumnIdentifiers);
    }

    public void setHeadings(JTable jTable) {
        Object[] arrobject = this.m_arrColumnIdentifiers != null ? this.m_arrColumnIdentifiers : this.getColumnHeadings();
        this.setColumnIdentifiers(arrobject);
        int[] arrn = this.getColumnSizes();
        for (int i = 0; i < this.getColumnCount(); ++i) {
            TableColumn tableColumn = jTable.getColumn(new Integer(i));
            if (tableColumn == null) continue;
            tableColumn.setWidth(arrn[i]);
        }
    }

    public int getRowIndex(Object object) {
        if (object instanceof Integer) {
            return (Integer) object;
        }
        for (int i = 0; i < this.getRowCount(); ++i) {
            String string = (String) super.getValueAt(i, 0);
            if (string == null || !string.equalsIgnoreCase((String) object)) continue;
            return i;
        }
        return -1;
    }

    public int getColumnIndex(Object object) {
        String[] arrstring = this.m_arrColumnIdentifiers != null ? this.m_arrColumnIdentifiers : this.getColumnHeadings();
        for (int i = 0; i < arrstring.length; ++i) {
            if (arrstring[i] == null || !arrstring[i].equalsIgnoreCase((String) object)) continue;
            return i;
        }
        return -1;
    }

    public void setColumnID(String[] arrstring) {
        this.setColumnIdentifiers(arrstring);
    }

    public Vector getDataVector() {
        return this._tableListModel.getDataVector();
    }

    public int getRowCount() {
        if (this._tableListModel == null) {
            return 0;
        }
        return this._tableListModel.getRowCount();
    }

    public int getColumnCount() {
        if (this._tableListModel == null) {
            return 0;
        }
        return this._tableListModel.getColumnCount();
    }

    public boolean isCellEditable(int n, int n2) {
        return false;
    }

    public Object getValueAt(int n, int n2) {
        return this._tableListModel.getValueAt(n, n2);
    }

    public void setValueAt(Object object, int n, int n2) {
    }

    public Class getColumnClass(int n) {
        return this._tableListModel.getColumnClass(n);
    }
}

