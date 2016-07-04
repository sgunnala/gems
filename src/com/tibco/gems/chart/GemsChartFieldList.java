/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class GemsChartFieldList
        extends JPanel
        implements GemsTableListModelBase {
    private static final int[] _columnSizes = new int[]{10, 125};
    private static final String[] _colunnHeaders = new String[]{" ", "Fields"};
    protected Timer m_timer = null;
    private GemsSubscriptionData _subscriptionData;
    private Vector _listElements;
    private GemsTableListPanel _tableListPanel;
    private GemsChartFrame m_chart;

    public GemsChartFieldList(GemsChartFrame gemsChartFrame, String string, Vector vector, GemsSubscriptionData gemsSubscriptionData) {
        this.m_chart = gemsChartFrame;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(180, 100));
        this.setBackground(Color.lightGray);
        this._subscriptionData = gemsSubscriptionData;
        this._listElements = this.createChartableFields(vector);
        this._tableListPanel = new GemsTableListPanel(this);
        this._tableListPanel.add((Component) this._tableListPanel.constructListPanel(), "Center");
        JTable jTable = this._tableListPanel.getTable();
        jTable.setDefaultRenderer(String.class, new ChartableFieldRenderer(null));
        jTable.setShowGrid(false);
        jTable.getColumn("Fields").setPreferredWidth(140);
        JTableHeader jTableHeader = jTable.getTableHeader();
        jTableHeader.setReorderingAllowed(false);
        _cls1 _cls12 = new _cls1();
        jTable.addMouseListener(_cls12);
        this.add("Center", this._tableListPanel);
    }

    public Vector createChartableFields(Vector vector) {
        Vector<ChartableField> vector2 = new Vector<ChartableField>();
        for (int i = 0; i < vector.size(); ++i) {
            vector2.addElement(new ChartableField((String) vector.elementAt(i), i));
        }
        return vector2;
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        if (this._listElements == null) {
            return 0;
        }
        return this._listElements.size();
    }

    public Vector getDataVector() {
        return this._listElements;
    }

    public Object getValueAt(int n, int n2) {
        ChartableField chartableField = (ChartableField) this._listElements.elementAt(n);
        if (chartableField != null) {
            if (n2 == 0) {
                return new Boolean(chartableField.show);
            }
            return chartableField.name;
        }
        return "";
    }

    public Class getColumnClass(int n) {
        if (n == 0) {
            return Boolean.class;
        }
        return String.class;
    }

    public int[] getColumnSizes() {
        return _columnSizes;
    }

    public String[] getColumnHeadings() {
        return _colunnHeaders;
    }

    public void SelectChartableField() {
        int n = this._tableListPanel.getSelectedIndex();
        if (n == -1) {
            return;
        }
        ChartableField chartableField = (ChartableField) this._listElements.elementAt(n);
        boolean bl = chartableField.show = !chartableField.show;
        if (!chartableField.show) {
            this.m_chart.removeChartableField(chartableField.series);
            chartableField.color = null;
        } else {
            if (chartableField.series == -1) {
                if (this.m_timer == null) {
                    this.m_timer = new Timer(500, new RefreshColorsTimerAction());
                    this.m_timer.setInitialDelay(500);
                    this.m_timer.start();
                } else {
                    this.m_timer.restart();
                }
            }
            chartableField.series = n;
            this.m_chart.showChartableField(chartableField.series);
            chartableField.color = this.m_chart.getSeriesColor(chartableField.series);
        }
        this._tableListPanel.getTable().repaint();
    }

    public void refreshChartableFieldColors() {
        for (int i = 0; i < this._listElements.size(); ++i) {
            ChartableField chartableField = (ChartableField) this._listElements.elementAt(i);
            if (!chartableField.show || chartableField.color != null) continue;
            chartableField.color = this.m_chart.getSeriesColor(chartableField.series);
            if (chartableField.color != null || this.m_timer != null) {
                // empty if block
            }
            this.m_timer.restart();
        }
        this._tableListPanel.resizeAndRepaint();
    }

    public void setListData(Vector vector) {
        this._listElements = vector;
        this._tableListPanel.setListElements(vector);
        this.resetSelection();
    }

    public void resetSelection() {
        this._tableListPanel.resetSelection();
    }

    private class _cls1
            extends MouseAdapter {
        _cls1() {
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            GemsChartFieldList.this.SelectChartableField();
        }
    }

    private class ChartableFieldRenderer
            extends DefaultTableCellRenderer {
        ChartableFieldRenderer(_cls1 _cls12) {
            this();
        }

        private ChartableFieldRenderer() {
        }

        public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
            ChartableField chartableField = (ChartableField) GemsChartFieldList.this._listElements.elementAt(n);
            if (chartableField.show) {
                this.setBackground(chartableField.color);
            } else {
                this.setBackground(Color.white);
            }
            if (n2 > 0) {
                bl = false;
                bl2 = false;
            }
            return super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
        }
    }

    class RefreshColorsTimerAction
            implements ActionListener {
        RefreshColorsTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsChartFieldList.this.m_timer.stop();
            GemsChartFieldList.this.refreshChartableFieldColors();
        }
    }

    private class ChartableField {
        boolean show;
        int series;
        String name;
        Color color;

        ChartableField(String string, int n) {
            this.color = null;
            this.name = string;
            this.series = -1;
            this.show = false;
        }
    }

}

