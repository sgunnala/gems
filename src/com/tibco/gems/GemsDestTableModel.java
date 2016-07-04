/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.DestinationInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.tibco.tibjms.admin.DestinationInfo;

public class GemsDestTableModel
        extends DefaultTableModel {
    JTable m_table;
    boolean m_isEditable;
    boolean m_showCheckbox = false;
    Object m_obj = new Object();
    MyRenderer m_renderer;
    MyCheckboxRenderer m_checkRenderer;
    SimpleDateFormat dateFormatMillis;

    public GemsDestTableModel(boolean bl, boolean bl2) {
        this.m_renderer = new MyRenderer();
        this.m_checkRenderer = new MyCheckboxRenderer();
        this.dateFormatMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss SSS zzz yyyy");
        this.m_isEditable = bl;
        this.m_showCheckbox = bl2;
    }

    public Class getColumnClass(int n) {
        Object object = this.getValueAt(0, n);
        if (object != null) {
            return object.getClass();
        }
        return String.class;
    }

    public boolean isCellEditable(int n, int n2) {
        if (this.m_showCheckbox && n2 < 1) {
            return true;
        }
        return this.m_isEditable;
    }

    public Vector getSelectedDestinations() {
        Vector<String> vector = new Vector<String>();
        if (this.m_showCheckbox) {
            for (int i = 0; i < this.getRowCount(); ++i) {
                if (!((Boolean) this.m_table.getValueAt(i, 0)).booleanValue()) continue;
                vector.add((String) this.m_table.getValueAt(i, 1));
            }
        }
        return vector;
    }

    public void selectAllRows() {
        if (this.m_showCheckbox) {
            for (int i = 0; i < this.getRowCount(); ++i) {
                this.m_table.setValueAt(new Boolean(true), i, 0);
            }
        }
    }

    public void toggleSelectedRow() {
        this.m_table.setValueAt(new Boolean((Boolean) this.m_table.getValueAt(this.m_table.getSelectedRow(), 0) == false), this.m_table.getSelectedRow(), 0);
    }

    public void buildColumnHeaders() {
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_table.setAutoResizeMode(0);
        if (Gems.getGems().getColourPendingMsgs()) {
            this.m_table.setDefaultRenderer(Long.class, this.m_renderer);
        }
        this.m_table.setDefaultRenderer(String.class, this.m_renderer);
        this.m_table.setDefaultRenderer(Boolean.class, this.m_checkRenderer);
        Object[] arrobject = this.m_showCheckbox ? new String[]{"Sel", "Destination", "ConsumerCount", "PendingMsgCount", "PendingMsgSize"} : new String[]{"Destination", "ConsumerCount", "PendingMsgCount", "PendingMsgSize"};
        this.setColumnIdentifiers(arrobject);
        if (this.m_showCheckbox) {
            this.m_table.getColumn("Sel").setPreferredWidth(30);
        }
        this.m_table.getColumn("Destination").setPreferredWidth(250);
        this.m_table.getColumn("ConsumerCount").setPreferredWidth(110);
        this.m_table.getColumn("PendingMsgCount").setPreferredWidth(120);
        this.m_table.getColumn("PendingMsgSize").setPreferredWidth(120);
    }

    public void populateDestinationInfo(DestinationInfo[] arrdestinationInfo) {
        this.setRowCount(0);
        if (arrdestinationInfo == null) {
            return;
        }
        for (int i = 0; i < arrdestinationInfo.length; ++i) {
            this.addDestination(arrdestinationInfo[i]);
        }
    }

    public void addDestination(DestinationInfo destinationInfo) {
        if (destinationInfo != null) {
            if (destinationInfo.getName().equals(">")) {
                return;
            }
            if (destinationInfo.getName().startsWith("$sys")) {
                return;
            }
            Object[] arrobject = this.m_showCheckbox ? new Object[]{new Boolean(false), destinationInfo.getName(), new Long(destinationInfo.getConsumerCount()), new Long(destinationInfo.getPendingMessageCount()), new Long(destinationInfo.getPendingMessageSize())} : new Object[]{destinationInfo.getName(), new Long(destinationInfo.getConsumerCount()), new Long(destinationInfo.getPendingMessageCount()), new Long(destinationInfo.getPendingMessageSize())};
            this.addRow(arrobject);
        }
    }

    class MyRenderer
            extends DefaultTableCellRenderer {
        public MyRenderer() {
            this.setToolTipText("Select checkboxes of destinations to purge");
        }

        public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
            Component component = super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
            if (object instanceof Long) {
                this.setHorizontalAlignment(4);
                if ((Long) object > 0 && jTable.getColumnName(n2).startsWith("Pending")) {
                    component.setBackground(Color.orange);
                } else if (bl) {
                    component.setBackground(jTable.getSelectionBackground());
                } else {
                    component.setBackground(Color.white);
                }
            } else {
                this.setHorizontalAlignment(2);
                if (bl) {
                    component.setBackground(jTable.getSelectionBackground());
                } else {
                    component.setBackground(Color.white);
                }
            }
            return component;
        }
    }

}

