/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

public class GemsTableListPanel
        extends JPanel {
    protected int iLastSelected = -1;
    protected JLabel _headerLabel;
    protected Vector _listElements;
    protected GemsTableListModelBase _tableListModel;
    protected TableListControl _tableListControl;
    private ChangeListener _changetListener;

    public GemsTableListPanel(String string, GemsTableListModelBase gemsTableListModelBase) {
        this(null, string, gemsTableListModelBase);
    }

    public GemsTableListPanel(String string, String string2, GemsTableListModelBase gemsTableListModelBase) {
        this(gemsTableListModelBase);
        JPanel jPanel = this.constructHeaderPanel(string2);
        this.setPanels(jPanel, this.constructListPanel());
    }

    public GemsTableListPanel(GemsTableListModelBase gemsTableListModelBase) {
        this();
        this.setTableModel(gemsTableListModelBase);
    }

    public JPanel constructHeaderPanel(String string) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBackground(Color.lightGray);
        jPanel.setPreferredSize(new Dimension(250, 20));
        jPanel.setMinimumSize(new Dimension(250, 20));
        this._headerLabel = new JLabel(string, 2);
        jPanel.add("Center", this._headerLabel);
        return jPanel;
    }

    public void setPanels(JComponent jComponent, JComponent jComponent2) {
        if (jComponent != null) {
            this.add((Component) jComponent, "North");
        }
        this.add((Component) jComponent2, "Center");
    }

    public JComponent constructListPanel() {
        return this.constructListPanel(new TableListControl(this, this._tableListModel));
    }

    public GemsTableListPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.lightGray);
    }

    public void setTableModel(GemsTableListModelBase gemsTableListModelBase) {
        this._tableListModel = gemsTableListModelBase;
        this._listElements = this._tableListModel.getDataVector();
        if (this._listElements == null) {
            this._listElements = new Vector();
        }
    }

    public JComponent constructListPanel(TableListControl tableListControl) {
        this._tableListControl = tableListControl;
        this._tableListControl.setBackground(Color.white);
        this._tableListControl.clearSelection();
        this._tableListControl.getSelectionModel().setSelectionMode(0);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(new SoftBevelBorder(1));
        jScrollPane.getViewport().add(this._tableListControl);
        jScrollPane.setBackground(Color.white);
        return jScrollPane;
    }

    public void onMouseClick() {
        this.iLastSelected = this.getSelectedIndex();
    }

    public int getSelectedIndex() {
        return this._tableListControl.getSelectedRow();
    }

    public void setSelectedIndex(int n) {
        this.iLastSelected = n;
        if (n == -1) {
            this._tableListControl.clearSelection();
            return;
        }
        this._tableListControl.setRowSelectionInterval(n, n);
    }

    public void onDoubleClick() {
        this.iLastSelected = this.getSelectedIndex();
    }

    public void setAutoResizeMode(int n) {
        if (this._tableListControl == null) {
            return;
        }
        this._tableListControl.setAutoResizeMode(n);
    }

    public int getSelectedColumn() {
        return this._tableListControl.getSelectedColumn();
    }

    public void addChangetListener(ChangeListener changeListener) {
        this._changetListener = changeListener;
    }

    public boolean onSelectionChanged(Object object) {
        if (this._changetListener != null) {
            this._changetListener.stateChanged(new ChangeEvent(object));
        }
        return true;
    }

    public void onBottom(int n) {
        this.stopEditing();
        if (n == this._listElements.size() - 1) {
            return;
        }
        Object e = this._listElements.elementAt(n);
        this._listElements.removeElementAt(n);
        this._listElements.addElement(e);
        this.setSelectedIndex(this._listElements.size() - 1);
    }

    public void stopEditing() {
        TableCellEditor tableCellEditor = this._tableListControl.getCellEditor();
        if (tableCellEditor != null) {
            tableCellEditor.stopCellEditing();
        }
        this._tableListControl.requestFocus();
    }

    public void onUp(int n) {
        if (n < 1 || n >= this._listElements.size()) {
            return;
        }
        Object e = this._listElements.elementAt(n);
        this._listElements.removeElementAt(n);
        this._listElements.insertElementAt(e, n - 1);
        this.setSelectedIndex(n - 1);
    }

    public void onDown(int n) {
        this.stopEditing();
        if (n == this._listElements.size() - 1) {
            return;
        }
        Object e = this._listElements.elementAt(n);
        this._listElements.removeElementAt(n);
        this._listElements.insertElementAt(e, n + 1);
        this.setSelectedIndex(n + 1);
    }

    public void onTop(int n) {
        this.stopEditing();
        if (n < 0 || n >= this._listElements.size()) {
            return;
        }
        Object e = this._listElements.elementAt(n);
        this._listElements.removeElementAt(n);
        this._listElements.insertElementAt(e, 0);
        this.setSelectedIndex(0);
    }

    public void addExtraButtons(JPanel jPanel) {
    }

    public Object getSelectedObject() {
        int n = this.getSelectedIndex();
        if (n < 0) {
            return null;
        }
        return this._listElements.elementAt(n);
    }

    public void resetSelection() {
        if (this._listElements.size() != 0) {
            int n = this.getSelectedIndex();
            this._tableListControl.clearSelection();
            this._tableListControl.validate();
            this._tableListControl.repaint();
            if (n >= 0) {
                this.setSelectedIndex(n);
            }
        }
    }

    public Vector getListElements() {
        return this._listElements;
    }

    public void setListElements(Vector vector) {
        this._listElements = vector;
        if (this._listElements.size() != 0) {
            this.iLastSelected = 0;
        }
        this.resizeAndRepaint();
    }

    public Object elementAt(int n) {
        return this._listElements.elementAt(n);
    }

    public void insertElementAt(Object object, int n) {
        this._listElements.insertElementAt(object, n);
    }

    public void addElement(Object object) {
        this._listElements.addElement(object);
        this.resizeAndRepaint();
    }

    public void resizeAndRepaint() {
        if (this._tableListControl == null) {
            return;
        }
        this._tableListControl.resizeAndRepaint();
    }

    public void setDescription(String string) {
        this._headerLabel.setText(string);
        this.doLayout();
        this.repaint();
    }

    private class TableListControl
            extends GemsTableListTable {
        final GemsTableListPanel tableListPanel;
        boolean resizing;

        public TableListControl(GemsTableListPanel gemsTableListPanel, GemsTableListModel gemsTableListModel) {
            super(gemsTableListModel);
            this.tableListPanel = gemsTableListPanel;
            this.resizing = false;
        }

        public TableListControl(GemsTableListPanel gemsTableListPanel, GemsTableListModelBase gemsTableListModelBase) {
            super(gemsTableListModelBase);
            this.tableListPanel = gemsTableListPanel;
            this.resizing = false;
        }

        public void onMouseClick() {
            this.tableListPanel.onMouseClick();
        }

        public void onDoubleClick() {
            this.tableListPanel.onDoubleClick();
        }

        public void onSelectionChanged() {
            if (this.resizing) {
                return;
            }
            this.tableListPanel.iLastSelected = this.tableListPanel.getSelectedIndex();
        }

        protected void resizeAndRepaint() {
            this.resizing = true;
            super.resizeAndRepaint();
            this.resizing = false;
        }
    }

    protected void editObject(Object object, int n) {
        if (object != null) {
            this.editObject(this._listElements.indexOf(object), n);
        }
    }

    protected void editObject(int n, int n2) {
        if (n != -1) {
            this.setSelectedIndex(n);
            if (n2 < 0) {
                n2 = 0;
            }
            this.getTable().editCellAt(this.getSelectedIndex(), n2);
            this._tableListControl.editCellAt(n, 0);
        }
    }

    public JTable getTable() {
        return this._tableListControl;
    }

}

