/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class GemsServerInfoPanel
        extends JPanel {
    protected GemsServerInfoTableModel m_tableModel;
    JTable m_table;
    TableSorter m_sorter;

    public GemsServerInfoPanel() {
        super(true);
        this.setLayout(new BorderLayout());
        this.m_tableModel = new GemsServerInfoTableModel();
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_tableModel.setTable(this.m_table);
        if (!Gems.getGems().getViewOnlyMode()) {
            this.addMouseListenerToTable(this.m_table);
        }
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        this.add((Component) jScrollPane, "Center");
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Gems.getGems().getTreeModel().serverPanelDoubleClick(GemsServerInfoPanel.this.m_tableModel.getSelectedCol1());
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    GemsServerInfoTableModel getModel() {
        return this.m_tableModel;
    }

}

