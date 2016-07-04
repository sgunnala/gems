/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.BridgeInfo
 *  com.tibco.tibjms.admin.BridgeTarget
 *  com.tibco.tibjms.admin.DestinationBridgeInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.tibco.tibjms.admin.BridgeInfo;
import com.tibco.tibjms.admin.BridgeTarget;
import com.tibco.tibjms.admin.DestinationBridgeInfo;

public class GemsBridgeTableModel
        extends DefaultTableModel
        implements GetPopupHandler {
    JTable m_table;
    boolean m_isEditable;
    boolean m_showCheckbox = false;
    PopupHandler m_popup = null;
    Object m_obj = new Object();
    MyRenderer m_renderer;
    MyCheckboxRenderer m_checkRenderer;
    SimpleDateFormat dateFormatMillis;
    GemsManageBridgesDialog m_manager;

    public GemsBridgeTableModel(boolean bl, boolean bl2, GemsManageBridgesDialog gemsManageBridgesDialog) {
        this.m_renderer = new MyRenderer();
        this.m_checkRenderer = new MyCheckboxRenderer();
        this.dateFormatMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss SSS zzz yyyy");
        this.m_manager = null;
        this.m_isEditable = bl;
        this.m_showCheckbox = bl2;
        this.m_manager = gemsManageBridgesDialog;
    }

    public PopupHandler getPopupHandler() {
        if (this.m_popup == null) {
            this.m_popup = new PopupBridgeTableHandler(this.m_table, this, this.m_manager);
        }
        return this.m_popup;
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

    public Vector getSelectedBridges() {
        Vector<DestinationBridgeInfo> vector = new Vector<DestinationBridgeInfo>();
        if (this.m_showCheckbox) {
            for (int i = 0; i < this.getRowCount(); ++i) {
                if (!((Boolean) this.m_table.getValueAt(i, 0)).booleanValue()) continue;
                vector.add(new DestinationBridgeInfo(this.m_table.getValueAt(i, 2).equals("Queue") ? 1 : 2, (String) this.m_table.getValueAt(i, 1), this.m_table.getValueAt(i, 4).equals("Queue") ? 1 : 2, (String) this.m_table.getValueAt(i, 3), ""));
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
        this.m_table.setDefaultRenderer(Boolean.class, this.m_checkRenderer);
        Object[] arrobject = this.m_showCheckbox ? new String[]{"Sel", "Source Destination", "Source Type", "Target Destination", "Target Type", "Selector"} : new String[]{"Source Destination", "Source Type", "Target Destination", "Target Type", "Selector"};
        this.setColumnIdentifiers(arrobject);
        if (this.m_showCheckbox) {
            this.m_table.getColumn("Sel").setPreferredWidth(30);
        }
        this.m_table.getColumn("Source Destination").setPreferredWidth(250);
        this.m_table.getColumn("Target Destination").setPreferredWidth(250);
        this.m_table.getColumn("Source Type").setPreferredWidth(80);
        this.m_table.getColumn("Target Type").setPreferredWidth(80);
        this.m_table.getColumn("Selector").setPreferredWidth(250);
    }

    public void populateBridgeInfo(BridgeInfo[] arrbridgeInfo, Pattern pattern) {
        this.setRowCount(0);
        if (arrbridgeInfo == null) {
            return;
        }
        for (int i = 0; i < arrbridgeInfo.length; ++i) {
            this.addBridge(arrbridgeInfo[i], pattern);
        }
    }

    public void addBridge(BridgeInfo bridgeInfo, Pattern pattern) {
        if (bridgeInfo != null) {
            BridgeTarget[] arrbridgeTarget = bridgeInfo.getTargets();
            for (int i = 0; i < arrbridgeTarget.length; ++i) {
                Object[] arrobject;
                if (pattern != null && !pattern.matcher(arrbridgeTarget[i].getName()).matches()) continue;
                if (this.m_showCheckbox) {
                    Object[] arrobject2 = new Object[6];
                    arrobject2[0] = new Boolean(false);
                    arrobject2[1] = bridgeInfo.getName();
                    arrobject2[2] = new String(bridgeInfo.getType() == 1 ? "Queue" : "Topic");
                    arrobject2[3] = arrbridgeTarget[i].getName();
                    arrobject2[4] = new String(arrbridgeTarget[i].getType() == 1 ? "Queue" : "Topic");
                    arrobject2[5] = arrbridgeTarget[i].getSelector();
                    arrobject = arrobject2;
                } else {
                    Object[] arrobject3 = new Object[5];
                    arrobject3[0] = bridgeInfo.getName();
                    arrobject3[1] = new String(bridgeInfo.getType() == 1 ? "Queue" : "Topic");
                    arrobject3[2] = arrbridgeTarget[i].getName();
                    arrobject3[3] = new String(arrbridgeTarget[i].getType() == 1 ? "Queue" : "Topic");
                    arrobject3[4] = arrbridgeTarget[i].getSelector();
                    arrobject = arrobject3;
                }
                this.addRow(arrobject);
            }
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

