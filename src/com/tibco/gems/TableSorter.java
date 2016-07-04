/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TableSorter
        extends AbstractTableModel {
    public static final int DESCENDING = -1;
    public static final int NOT_SORTED = 0;
    public static final int ASCENDING = 1;
    public static final Comparator COMPARABLE_COMAPRATOR = new Comparator() {

        public int compare(Object object, Object object2) {
            return ((Comparable) object).compareTo(object2);
        }
    };
    public static final Comparator LEXICAL_COMPARATOR = new Comparator() {

        public int compare(Object object, Object object2) {
            return object.toString().compareTo(object2.toString());
        }
    };
    private static Directive EMPTY_DIRECTIVE = new Directive(-1, 0);
    protected TableModel tableModel;
    private Row[] viewToModel;
    private int[] modelToView;
    private JTableHeader tableHeader;
    private MouseListener mouseListener;
    private TableModelListener tableModelListener;
    private Map columnComparators = new HashMap();
    private List<Directive> sortingColumns = new ArrayList<>();

    public TableSorter(TableModel tableModel) {
        this();
        this.setTableModel(tableModel);
    }

    public TableSorter() {
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
    }

    private void clearSortingState() {
        this.viewToModel = null;
        this.modelToView = null;
    }

    public TableSorter(TableModel tableModel, JTableHeader jTableHeader) {
        this();
        this.setTableHeader(jTableHeader);
        this.setTableModel(tableModel);
    }

    public TableModel getTableModel() {
        return this.tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        if (this.tableModel != null) {
            this.tableModel.removeTableModelListener(this.tableModelListener);
        }
        this.tableModel = tableModel;
        if (this.tableModel != null) {
            this.tableModel.addTableModelListener(this.tableModelListener);
        }
        this.clearSortingState();
        this.fireTableStructureChanged();
    }

    public JTableHeader getTableHeader() {
        return this.tableHeader;
    }

    public void setTableHeader(JTableHeader jTableHeader) {
        if (this.tableHeader != null) {
            this.tableHeader.removeMouseListener(this.mouseListener);
            TableCellRenderer tableCellRenderer = this.tableHeader.getDefaultRenderer();
            if (tableCellRenderer instanceof SortableHeaderRenderer) {
                this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer) tableCellRenderer).tableCellRenderer);
            }
        }
        this.tableHeader = jTableHeader;
        if (this.tableHeader != null) {
            this.tableHeader.addMouseListener(this.mouseListener);
            this.tableHeader.setDefaultRenderer(new SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
        }
    }

    public int getSortingStatus(int n) {
        return this.getDirective(n).direction;
    }

    private Directive getDirective(int n) {
        for (int i = 0; i < this.sortingColumns.size(); ++i) {
            Directive directive = (Directive) this.sortingColumns.get(i);
            if (directive.column != n) continue;
            return directive;
        }
        return EMPTY_DIRECTIVE;
    }

    public void setSortingStatus(int n, int n2) {
        Directive directive = this.getDirective(n);
        if (directive != EMPTY_DIRECTIVE) {
            this.sortingColumns.remove(directive);
        }
        if (n2 != 0) {
            this.sortingColumns.add(new Directive(n, n2));
        }
        this.sortingStatusChanged();
    }

    private void sortingStatusChanged() {
        this.clearSortingState();
        this.fireTableDataChanged();
        if (this.tableHeader != null) {
            this.tableHeader.repaint();
        }
    }

    public void setColumnComparator(Class class_, Comparator comparator) {
        if (comparator == null) {
            this.columnComparators.remove(class_);
        } else {
            this.columnComparators.put(class_, comparator);
        }
    }

    public int getRowCount() {
        return this.tableModel == null ? 0 : this.tableModel.getRowCount();
    }

    public int getColumnCount() {
        return this.tableModel == null ? 0 : this.tableModel.getColumnCount();
    }

    public Object getValueAt(int n, int n2) {
        return this.tableModel.getValueAt(this.modelIndex(n), n2);
    }

    public String getColumnName(int n) {
        return this.tableModel.getColumnName(n);
    }

    public Class getColumnClass(int n) {
        return this.tableModel.getColumnClass(n);
    }

    public boolean isCellEditable(int n, int n2) {
        return this.tableModel.isCellEditable(this.modelIndex(n), n2);
    }

    public void setValueAt(Object object, int n, int n2) {
        this.tableModel.setValueAt(object, this.modelIndex(n), n2);
    }

    private void cancelSorting() {
        this.sortingColumns.clear();
        this.sortingStatusChanged();
    }

    private int[] getModelToView() {
        if (this.modelToView == null) {
            int n = this.getViewToModel().length;
            this.modelToView = new int[n];
            int n2 = 0;
            while (n2 < n) {
                this.modelToView[this.modelIndex((int) n2)] = n2++;
            }
        }
        return this.modelToView;
    }

    private Row[] getViewToModel() {
        if (this.viewToModel == null) {
            int n = this.tableModel.getRowCount();
            this.viewToModel = new Row[n];
            for (int i = 0; i < n; ++i) {
                this.viewToModel[i] = new Row(i);
            }
            if (this.isSorting()) {
                Arrays.sort(this.viewToModel);
            }
        }
        return this.viewToModel;
    }

    public int modelIndex(int n) {
        return this.getViewToModel()[n].modelIndex;
    }

    public boolean isSorting() {
        return this.sortingColumns.size() != 0;
    }

    private static class Directive {
        private int column;
        private int direction;

        public Directive(int n, int n2) {
            this.column = n;
            this.direction = n2;
        }
    }

    private static class Arrow
            implements Icon {
        private boolean descending;
        private int size;
        private int priority;

        public Arrow(boolean bl, int n, int n2) {
            this.descending = bl;
            this.size = n;
            this.priority = n2;
        }

        public void paintIcon(Component component, Graphics graphics, int n, int n2) {
            Color color = component == null ? Color.GRAY : component.getBackground();
            int n3 = (int) ((double) (this.size / 2) * Math.pow(0.8, this.priority));
            int n4 = this.descending ? n3 : -n3;
            n2 = n2 + 5 * this.size / 6 + (this.descending ? -n4 : 0);
            int n5 = this.descending ? 1 : -1;
            graphics.translate(n, n2);
            graphics.setColor(color.darker());
            graphics.drawLine(n3 / 2, n4, 0, 0);
            graphics.drawLine(n3 / 2, n4 + n5, 0, n5);
            graphics.setColor(color.brighter());
            graphics.drawLine(n3 / 2, n4, n3, 0);
            graphics.drawLine(n3 / 2, n4 + n5, n3, n5);
            if (this.descending) {
                graphics.setColor(color.darker().darker());
            } else {
                graphics.setColor(color.brighter().brighter());
            }
            graphics.drawLine(n3, 0, 0, 0);
            graphics.setColor(color);
            graphics.translate(-n, -n2);
        }

        public int getIconWidth() {
            return this.size;
        }

        public int getIconHeight() {
            return this.size;
        }
    }

    private class SortableHeaderRenderer
            implements TableCellRenderer {
        private TableCellRenderer tableCellRenderer;

        public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
            Component component = this.tableCellRenderer.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
            if (component instanceof JLabel) {
                JLabel jLabel = (JLabel) component;
                jLabel.setHorizontalTextPosition(2);
                int n3 = jTable.convertColumnIndexToModel(n2);
                jLabel.setIcon(TableSorter.this.getHeaderRendererIcon(n3, jLabel.getFont().getSize()));
            }
            return component;
        }
    }

    private class MouseHandler
            extends MouseAdapter {
        private MouseHandler() {
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            int n;
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            TableColumnModel tableColumnModel = jTableHeader.getColumnModel();
            int n2 = tableColumnModel.getColumn(n = tableColumnModel.getColumnIndexAtX(mouseEvent.getX())).getModelIndex();
            if (n2 != -1) {
                int n3 = TableSorter.this.getSortingStatus(n2);
                if (!mouseEvent.isControlDown()) {
                    TableSorter.this.cancelSorting();
                }
                n3 += mouseEvent.isShiftDown() ? -1 : 1;
                n3 = (n3 + 4) % 3 - 1;
                TableSorter.this.setSortingStatus(n2, n3);
            }
        }
    }

    private class TableModelHandler
            implements TableModelListener {
        private TableModelHandler() {
        }

        public void tableChanged(TableModelEvent tableModelEvent) {
            if (!TableSorter.this.isSorting()) {
                TableSorter.this.clearSortingState();
                TableSorter.this.fireTableChanged(tableModelEvent);
                return;
            }
            if (tableModelEvent.getFirstRow() == -1) {
                TableSorter.this.cancelSorting();
                TableSorter.this.fireTableChanged(tableModelEvent);
                return;
            }
            int n = tableModelEvent.getColumn();
            if (tableModelEvent.getFirstRow() == tableModelEvent.getLastRow() && n != -1 && TableSorter.this.getSortingStatus(n) == 0 && TableSorter.this.modelToView != null) {
                int n2 = TableSorter.this.getModelToView()[tableModelEvent.getFirstRow()];
                TableSorter.this.fireTableChanged(new TableModelEvent(TableSorter.this, n2, n2, n, tableModelEvent.getType()));
                return;
            }
            TableSorter.this.clearSortingState();
            TableSorter.this.fireTableDataChanged();
        }
    }

    private class Row
            implements Comparable {
        private int modelIndex;

        public Row(int n) {
            this.modelIndex = n;
        }

        public int compareTo(Object object) {
            int n = this.modelIndex;
            int n2 = ((Row) object).modelIndex;
            for (Directive directive : TableSorter.this.sortingColumns) {
                int n3 = directive.column;
                Object object2 = TableSorter.this.tableModel.getValueAt(n, n3);
                Object object3 = TableSorter.this.tableModel.getValueAt(n2, n3);
                int n4 = 0;
                n4 = object2 == null && object3 == null ? 0 : (object2 == null ? -1 : (object3 == null ? 1 : TableSorter.this.getComparator(n3).compare(object2, object3)));
                if (n4 == 0) continue;
                return directive.direction == -1 ? -n4 : n4;
            }
            return 0;
        }
    }

    protected Icon getHeaderRendererIcon(int n, int n2) {
        Directive directive = this.getDirective(n);
        if (directive == EMPTY_DIRECTIVE) {
            return null;
        }
        return new Arrow(directive.direction == -1, n2, this.sortingColumns.indexOf(directive));
    }

    protected Comparator getComparator(int n) {
        Class class_ = this.tableModel.getColumnClass(n);
        Comparator comparator = (Comparator) this.columnComparators.get(class_);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(class_)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }

}

