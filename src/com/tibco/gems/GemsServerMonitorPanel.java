/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class GemsServerMonitorPanel
        extends JPanel {
    protected GemsServerMonitorTableModel m_tableModel;
    JTable m_table;

    public GemsServerMonitorPanel() {
        super(true);
        this.setLayout(new BorderLayout());
        this.m_tableModel = new GemsServerMonitorTableModel();
        this.m_tableModel.m_table = this.m_table = new JTable(this.m_tableModel);
        this.addMouseListenerToTable(this.m_table);
        this.m_table.setSelectionMode(0);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        this.add((Component) jScrollPane, "Center");
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Gems.getGems().getTreeModel().serverMonitorDoubleClick(GemsServerMonitorPanel.this.m_tableModel.getSelectedCol1());
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    GemsServerMonitorTableModel getModel() {
        return this.m_tableModel;
    }

}

