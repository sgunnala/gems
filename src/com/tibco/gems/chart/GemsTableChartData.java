/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class GemsTableChartData
        extends JTable {
    public GemsTableChartData(TableModel tableModel, Vector vector) {
        super(tableModel);
        this.setBackground(Color.white);
        for (int i = 0; i < vector.size(); ++i) {
            TableColumn tableColumn = this.getColumn((String) vector.elementAt(i));
            tableColumn.setPreferredWidth(150);
        }
        if (vector.size() > 4) {
            this.setAutoResizeMode(0);
        }
    }

    public JPanel createView(Dimension dimension) {
        JScrollPane jScrollPane = new JScrollPane(this);
        jScrollPane.setPreferredSize(dimension);
        jScrollPane.setPreferredSize(new Dimension(dimension.width, dimension.height * 7 / 8));
        jScrollPane.setMinimumSize(new Dimension(dimension.width, dimension.height / 3));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("Center", jScrollPane);
        return jPanel;
    }
}

