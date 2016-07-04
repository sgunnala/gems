/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

public abstract class GemsTableListTable
        extends JTable {
    final int m_nCols = 4;
    GemsTableListModel m_dataModel;

    public GemsTableListTable(GemsTableListModelBase gemsTableListModelBase) {
        this(new GemsTableListModel(gemsTableListModelBase));
    }

    public GemsTableListTable(GemsTableListModel gemsTableListModel) {
        super(gemsTableListModel);
        this.m_dataModel = gemsTableListModel;
        this.m_dataModel.setTable(this);
        ListSelectionModel listSelectionModel = this.getSelectionModel();
        listSelectionModel.addListSelectionListener(new TableListTableSelectionListener());
        this.setRequestFocusEnabled(true);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(10, 0);
        this.registerKeyboardAction(new TableListTableAction(), keyStroke, 2);
        this.addMouseListener(new TableListTableMouseListener());
        this.setRowSelectionAllowed(true);
        this.setColumnSelectionAllowed(false);
        this.setAutoResizeMode(0);
        this.m_dataModel.setColumnHeadings();
        this.m_dataModel.setColumnSizes();
    }

    public void setColumns() {
        String[] arrstring = this.m_dataModel.getColumnHeadings();
        int[] arrn = this.m_dataModel.getColumnSizes();
        int n = arrstring.length <= this.getColumnCount() ? arrn.length : this.getColumnCount();
        for (int i = 0; i < n; ++i) {
            TableColumn tableColumn = new TableColumn(i);
            tableColumn.setHeaderValue(arrstring[i]);
            this.addColumn(tableColumn);
        }
    }

    public abstract void onMouseClick();

    public abstract void onDoubleClick();

    public abstract void onSelectionChanged();

    private class TableListTableMouseListener
            extends MouseAdapter {
        TableListTableMouseListener() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            GemsTableListTable.this.onMouseClick();
            if (mouseEvent.getClickCount() == 2) {
                GemsTableListTable.this.onDoubleClick();
            }
        }
    }

    private class TableListTableAction
            extends AbstractAction {
        TableListTableAction() {
        }

        public boolean isEnabled() {
            return true;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("RowSelectionAction:actionPerformed - " + actionEvent);
            GemsTableListTable.this.onDoubleClick();
        }
    }

    private class TableListTableSelectionListener
            implements ListSelectionListener {
        TableListTableSelectionListener() {
        }

        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            GemsTableListTable.this.onSelectionChanged();
        }
    }

}

