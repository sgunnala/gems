/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;

public class GemsTracePopup
        extends JDialog {
    protected GemsCheckListTableModel m_tableModel;
    protected JButton m_okButton;
    protected JButton m_cancelButton;
    protected boolean m_ok = true;
    JPanel m_panel;
    JTable m_table;
    TableSorter m_sorter;

    public GemsTracePopup(JDialog jDialog, Component component) {
        super(jDialog, "Select Trace Items", true);
        this.setLocationRelativeTo(component);
        this.setDefaultCloseOperation(2);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        this.m_tableModel = new GemsCheckListTableModel("TraceItem");
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_table.setRowSelectionAllowed(false);
        this.m_tableModel.m_table = this.m_table;
        this.addMouseListenerToTable(this.m_table);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(230, 300));
        jPanel.add((Component) jScrollPane, "North");
        JPanel jPanel2 = new JPanel(true);
        jPanel2.setLayout(new BoxLayout(jPanel2, 0));
        Component component2 = Box.createRigidArea(new Dimension(40, 10));
        jPanel2.add(component2);
        this.m_okButton = new JButton("Ok");
        this.m_okButton.addActionListener(new OkPressed());
        this.m_cancelButton = new JButton("Cancel");
        this.m_cancelButton.addActionListener(new CancelPressed());
        jPanel2.add(this.m_okButton);
        component2 = Box.createRigidArea(new Dimension(20, 10));
        jPanel2.add(component2);
        jPanel2.add(this.m_cancelButton);
        jPanel.add((Component) jPanel2, "South");
        this.pack();
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && GemsTracePopup.this.m_table.getSelectedColumn() > 0) {
                    GemsTracePopup.this.m_tableModel.toggleSelectedRow();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void ok() {
        this.dispose();
    }

    public void cancel() {
        this.m_ok = false;
        this.dispose();
    }

    Vector getSelectedTraceItems() {
        this.populate();
        this.show();
        if (this.m_ok) {
            return this.m_tableModel.getSelectedObjects();
        }
        return null;
    }

    public void populate() {
        this.m_tableModel.buildColumnHeaders();
        for (GemsTraceDialog.Trace trace : GemsTraceDialog.Trace.values()) {
            this.m_tableModel.addObject(false, (Object) trace);
        }
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTracePopup.this.cancel();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTracePopup.this.ok();
        }
    }

}

