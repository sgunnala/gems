/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  javax.jms.Message
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.jms.Message;
import javax.swing.*;

import com.tibco.tibjms.admin.TibjmsAdmin;

public class GemsMessagePanel
        extends JPanel {
    protected GemsMessageTableModel m_tableModel;
    JTable m_table;
    TibjmsAdmin m_admin = null;
    TableSorter m_sorter;

    public GemsMessagePanel() {
        super(true);
        this.setLayout(new BorderLayout());
        this.m_tableModel = new GemsMessageTableModel(false, false);
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_table.setSelectionMode(0);
        this.m_tableModel.m_table = this.m_table;
        this.addMouseListenerToTable(this.m_table);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        this.add((Component) jScrollPane, "Center");
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                Message message;
                if (mouseEvent.getClickCount() == 2 && (message = GemsMessagePanel.this.m_tableModel.getSelectedMessage()) != null) {
                    GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(null, false, null, false, null, true);
                    gemsMessageFrame.populate(message);
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public GemsMessageTableModel getModel() {
        return this.m_tableModel;
    }

}

