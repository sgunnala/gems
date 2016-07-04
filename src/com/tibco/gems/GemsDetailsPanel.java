/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.JTableHeader;

class GemsDetailsPanel extends JPanel {
    protected GemsDetailsTableModel m_tableModel;

    GemsDetailsPanel() {
        super(true);

        m_tableModel = new GemsDetailsTableModel();
        TableSorter m_sorter = new TableSorter(this.m_tableModel);
        JTable m_table = new JTable(m_sorter);
        m_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_tableModel.setTable(m_table);

        JTableHeader tableHeader = m_table.getTableHeader();
        m_sorter.setTableHeader(tableHeader);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setToolTipText("Click to specify sorting; Control-Click to specify secondary sorting");

        addMouseListenerToTable(m_table);

        setLayout(new BorderLayout());
        add(new JScrollPane(m_table), BorderLayout.CENTER);
    }

    public void addMouseListenerToTable(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Gems.getGems().getTreeModel().detailsPanelDoubleClick(GemsDetailsPanel.this.m_tableModel.getSelectedCol1());
                }
            }
        });
    }

    public GemsDetailsTableModel getModel() {
        return m_tableModel;
    }
}

