/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.ServerInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.tibco.tibjms.admin.ServerInfo;

public class GemsServerMonitorTableModel
        extends DefaultTableModel {
    static String[] cols = new String[]{"Alias", "State", "ServerName", "BackupName", "Connections", "Sessions", "Queues", "Topics", "Durables", "PendingMsgs", "PendingMsgSize", "MsgMem", "InMsgCount", "InMsgRate", "OutMsgCount", "OutMsgRate", "DiskReadRate", "DiskWriteRate", "AsyncDBSize", "SyncDBSize"};
    static String[] cols1 = new String[]{"Alias", "State", "ServerName", "BackupName", "Events", "Connections", "Sessions", "Queues", "Topics", "Durables", "PendingMsgs", "PendingMsgSize", "MsgMem", "InMsgCount", "InMsgRate", "OutMsgCount", "OutMsgRate", "DiskReadRate", "DiskWriteRate", "AsyncDBSize", "SyncDBSize"};
    final int MAX_COLS = 21;
    JTable m_table;
    MyRenderer m_renderer;
    Object m_obj;
    long[] totals;
    String[] m_colPositions;

    public GemsServerMonitorTableModel() {
        this.m_renderer = new MyRenderer();
        this.m_obj = new Object();
        this.totals = new long[17];
        this.m_colPositions = null;
        this.initColumnPositions();
    }

    public void initColumnPositions() {
        String string = Gems.getGems().getServerInfoColPositions();
        if (string == null || string.length() == 0) {
            return;
        }
        this.m_colPositions = new String[21];
        for (int i = 0; i < 21; ++i) {
            this.m_colPositions[i] = null;
        }
        String[] arrstring = string.split(",");
        for (int j = 0; j < arrstring.length; ++j) {
            try {
                int n = arrstring[j].indexOf(58);
                if (n <= 0) continue;
                String string2 = arrstring[j].substring(0, n);
                String string3 = arrstring[j].substring(n + 1);
                int n2 = Integer.valueOf(string3);
                if (n2 == 0 || string2.equals("Alias")) continue;
                if (n2 >= 21) {
                    n2 = 20;
                }
                this.m_colPositions[n2] = string2;
                continue;
            } catch (NumberFormatException var4_6) {
                System.err.println("NumberFormatException: " + var4_6.getMessage());
                return;
            }
        }
    }

    public boolean isCellEditable(int n, int n2) {
        return false;
    }

    public String getSelectedCol1() {
        if (this.m_table.getSelectedRow() < 0) {
            return null;
        }
        Object object = this.m_table.getValueAt(this.m_table.getSelectedRow(), 0);
        if (object instanceof CellValue) {
            return ((CellValue) object).m_cellValue;
        }
        return null;
    }

    public boolean populateTable(DefaultMutableTreeNode defaultMutableTreeNode, boolean bl, boolean bl2) {
        int n;
        this.setRowCount(0);
        for (n = 0; n < 17; ++n) {
            this.totals[n] = 0;
        }
        n = cols.length;
        if (bl2) {
            ++n;
        }
        if (this.getColumnCount() != n) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.m_table.setDefaultRenderer(this.m_obj.getClass(), this.m_renderer);
            if (bl2) {
                this.setColumnIdentifiers(cols1);
            } else {
                this.setColumnIdentifiers(cols);
            }
            this.m_table.getColumn("Alias").setPreferredWidth(100);
            this.m_table.getColumn("ServerName").setPreferredWidth(100);
            this.m_table.getColumn("BackupName").setPreferredWidth(100);
            this.m_table.getColumn("State").setPreferredWidth(65);
            this.m_table.getColumn("PendingMsgs").setPreferredWidth(80);
            this.m_table.getColumn("PendingMsgSize").setPreferredWidth(100);
            this.m_table.getColumn("InMsgRate").setPreferredWidth(70);
            this.m_table.getColumn("OutMsgRate").setPreferredWidth(80);
            this.m_table.getColumn("InMsgCount").setPreferredWidth(80);
            this.m_table.getColumn("OutMsgCount").setPreferredWidth(80);
            this.m_table.getColumn("MsgMem").setPreferredWidth(70);
            this.m_table.getColumn("Topics").setPreferredWidth(50);
            this.m_table.getColumn("Queues").setPreferredWidth(55);
            this.m_table.getColumn("Durables").setPreferredWidth(60);
            this.m_table.getColumn("Sessions").setPreferredWidth(60);
            this.m_table.getColumn("Connections").setPreferredWidth(80);
            this.m_table.getColumn("DiskReadRate").setPreferredWidth(90);
            this.m_table.getColumn("DiskWriteRate").setPreferredWidth(90);
            if (bl2) {
                this.m_table.getColumn("Events").setPreferredWidth(55);
            }
        }
        if (defaultMutableTreeNode != null) {
            boolean bl3 = this.findServers(defaultMutableTreeNode, false, bl2);
            if (this.m_table.getRowCount() == 0) {
                return true;
            }
            if (Gems.getGems().getShowTotals()) {
                if (bl2) {
                    Object[] arrobject = new String[]{new String("Total"), new String(""), new String(""), new String(""), new String(String.valueOf(this.totals[0])), new String(String.valueOf(this.totals[1])), new String(String.valueOf(this.totals[2])), new String(String.valueOf(this.totals[3])), new String(String.valueOf(this.totals[4])), new String(String.valueOf(this.totals[5])), new String(String.valueOf(this.totals[6])), new String(StringUtilities.getHumanReadableSize(this.totals[7])), new String(StringUtilities.getHumanReadableSize(this.totals[8])), new String(String.valueOf(this.totals[11])), new String(String.valueOf(this.totals[12])), new String(String.valueOf(this.totals[13])), new String(String.valueOf(this.totals[14])), new String(StringUtilities.getHumanReadableSize(this.totals[15])), new String(StringUtilities.getHumanReadableSize(this.totals[16])), new String(StringUtilities.getHumanReadableSize(this.totals[9])), new String(StringUtilities.getHumanReadableSize(this.totals[10]))};
                    this.addRow(arrobject);
                } else {
                    Object[] arrobject = new String[]{new String("Total"), new String(""), new String(""), new String(""), new String(String.valueOf(this.totals[1])), new String(String.valueOf(this.totals[2])), new String(String.valueOf(this.totals[3])), new String(String.valueOf(this.totals[4])), new String(String.valueOf(this.totals[5])), new String(String.valueOf(this.totals[6])), new String(StringUtilities.getHumanReadableSize(this.totals[7])), new String(StringUtilities.getHumanReadableSize(this.totals[8])), new String(String.valueOf(this.totals[11])), new String(String.valueOf(this.totals[12])), new String(String.valueOf(this.totals[13])), new String(String.valueOf(this.totals[14])), new String(StringUtilities.getHumanReadableSize(this.totals[15])), new String(StringUtilities.getHumanReadableSize(this.totals[16])), new String(StringUtilities.getHumanReadableSize(this.totals[9])), new String(StringUtilities.getHumanReadableSize(this.totals[10]))};
                    this.addRow(arrobject);
                }
            }
            this.setupColumnPositions();
            return bl3;
        }
        return false;
    }

    public void setupColumnPositions() {
        if (this.m_colPositions == null) {
            return;
        }
        for (int i = 0; i < 21; ++i) {
            if (this.m_colPositions[i] == null) continue;
            int n = this.m_table.getColumnModel().getColumnIndex(this.m_colPositions[i]);
            this.m_table.moveColumn(n, i);
        }
    }

    public boolean findServers(DefaultMutableTreeNode defaultMutableTreeNode, boolean bl, boolean bl2) {
        boolean bl3 = false;
        Enumeration enumeration = defaultMutableTreeNode.children();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode defaultMutableTreeNode2 = (DefaultMutableTreeNode) enumeration.nextElement();
                if (defaultMutableTreeNode2 == null) {
                    return bl3;
                }
                if (defaultMutableTreeNode2 instanceof GemsConnectionNode) {
                    GemsConnectionNode gemsConnectionNode = (GemsConnectionNode) defaultMutableTreeNode2;
                    if (bl) {
                        if (!gemsConnectionNode.isConnected() && gemsConnectionNode.isAutoConnect()) {
                            gemsConnectionNode.connect();
                            bl3 = true;
                        } else if (gemsConnectionNode.getJmsServerInfo(true) == null) {
                            bl3 = true;
                        }
                    } else {
                        ServerInfo serverInfo = gemsConnectionNode.getJmsServerInfo(false);
                        this.populateServer((String) defaultMutableTreeNode2.getUserObject(), serverInfo, gemsConnectionNode, bl2);
                    }
                }
                if (!this.findServers(defaultMutableTreeNode2, bl, bl2)) continue;
                bl3 = true;
            }
        }
        return bl3;
    }

    public void populateServer(String string, ServerInfo serverInfo, GemsConnectionNode gemsConnectionNode, boolean bl) {
        if (serverInfo == null) {
            Object[] arrobject = bl ? new CellValue[]{new CellValue(string), new CellValue("Unknown", -1), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue("")} : new CellValue[]{new CellValue(string), new CellValue("Unknown", -1), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue(""), new CellValue("")};
            this.addRow(arrobject);
            return;
        }
        try {
            Object[] arrobject;
            boolean bl2 = gemsConnectionNode.isFtUrl();
            String string2 = "?";
            long l = 0;
            try {
                string2 = String.valueOf(serverInfo.getSessionCount());
                l = serverInfo.getSessionCount();
            } catch (Throwable var10_11) {
                // empty catch block
            }
            if (!bl) {
                Object[] arrobject2 = new CellValue[20];
                arrobject2[0] = new CellValue(string);
                arrobject2[1] = new CellValue(serverInfo.getState() == 4 ? "Active" : "Standby", -1);
                arrobject2[2] = new CellValue(serverInfo.getServerName());
                arrobject2[3] = new CellValue(bl2 && serverInfo.getState() == 4 && serverInfo.getBackupName() == null ? "Disconnected" : serverInfo.getBackupName(), -2);
                arrobject2[4] = new CellValue(String.valueOf(serverInfo.getConnectionCount()), serverInfo.getConnectionCount(), gemsConnectionNode.getWarnLimit("Connections"), gemsConnectionNode.getErrorLimit("Connections"));
                arrobject2[5] = new CellValue(string2, l, gemsConnectionNode.getWarnLimit("Sessions"), gemsConnectionNode.getErrorLimit("Sessions"));
                arrobject2[6] = new CellValue(String.valueOf(serverInfo.getQueueCount()), serverInfo.getQueueCount(), gemsConnectionNode.getWarnLimit("Queues"), gemsConnectionNode.getErrorLimit("Queues"));
                arrobject2[7] = new CellValue(String.valueOf(serverInfo.getTopicCount()), serverInfo.getTopicCount(), gemsConnectionNode.getWarnLimit("Topics"), gemsConnectionNode.getErrorLimit("Topics"));
                arrobject2[8] = new CellValue(String.valueOf(serverInfo.getDurableCount()), serverInfo.getDurableCount(), gemsConnectionNode.getWarnLimit("Durables"), gemsConnectionNode.getErrorLimit("Durables"));
                arrobject2[9] = new CellValue(String.valueOf(serverInfo.getPendingMessageCount()), serverInfo.getPendingMessageCount(), gemsConnectionNode.getWarnLimit("PendingMsgs"), gemsConnectionNode.getErrorLimit("PendingMsgs"));
                arrobject2[10] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getPendingMessageSize()), serverInfo.getPendingMessageSize(), gemsConnectionNode.getWarnLimit("PendingMsgSize"), gemsConnectionNode.getErrorLimit("PendingMsgSize"));
                arrobject2[11] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getMsgMem()), serverInfo.getMsgMem(), gemsConnectionNode.getWarnLimit("MsgMem"), gemsConnectionNode.getErrorLimit("MsgMem"));
                arrobject2[12] = new CellValue(String.valueOf(serverInfo.getInboundMessageCount()), serverInfo.getInboundMessageCount(), gemsConnectionNode.getWarnLimit("InMsgCount"), gemsConnectionNode.getErrorLimit("InMsgCount"));
                arrobject2[13] = new CellValue(String.valueOf(serverInfo.getInboundMessageRate()), serverInfo.getInboundMessageRate(), gemsConnectionNode.getWarnLimit("InMsgRate"), gemsConnectionNode.getErrorLimit("InMsgRate"));
                arrobject2[14] = new CellValue(String.valueOf(serverInfo.getOutboundMessageCount()), serverInfo.getOutboundMessageCount(), gemsConnectionNode.getWarnLimit("OutMsgCount"), gemsConnectionNode.getErrorLimit("OutMsgCount"));
                arrobject2[15] = new CellValue(String.valueOf(serverInfo.getOutboundMessageRate()), serverInfo.getOutboundMessageRate(), gemsConnectionNode.getWarnLimit("OutMsgRate"), gemsConnectionNode.getErrorLimit("OutMsgRate"));
                arrobject2[16] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getDiskReadRate()), serverInfo.getDiskReadRate(), gemsConnectionNode.getWarnLimit("DiskReadRate"), gemsConnectionNode.getErrorLimit("DiskReadRate"));
                arrobject2[17] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getDiskWriteRate()), serverInfo.getDiskWriteRate(), gemsConnectionNode.getWarnLimit("DiskWriteRate"), gemsConnectionNode.getErrorLimit("DiskWriteRate"));
                arrobject2[18] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getAsyncDBSize()), serverInfo.getAsyncDBSize(), gemsConnectionNode.getWarnLimit("AsyncDBSize"), gemsConnectionNode.getErrorLimit("AsyncDBSize"));
                arrobject2[19] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getSyncDBSize()), serverInfo.getSyncDBSize(), gemsConnectionNode.getWarnLimit("SyncDBSize"), gemsConnectionNode.getErrorLimit("SyncDBSize"));
                arrobject = arrobject2;
            } else {
                long[] arrl = this.totals;
                arrl[0] = arrl[0] + (long) gemsConnectionNode.getEventCount();
                Long l2 = gemsConnectionNode.getWarnLimit("Events");
                Long l3 = gemsConnectionNode.getErrorLimit("Events");
                CellValue[] arrcellValue = new CellValue[21];
                arrcellValue[0] = new CellValue(string);
                arrcellValue[1] = new CellValue(serverInfo.getState() == 4 ? "Active" : "Standby", -1);
                arrcellValue[2] = new CellValue(serverInfo.getServerName());
                arrcellValue[3] = new CellValue(bl2 && serverInfo.getState() == 4 && serverInfo.getBackupName() == null ? "Disconnected" : serverInfo.getBackupName(), -2);
                arrcellValue[4] = new CellValue(String.valueOf(gemsConnectionNode.getEventCount()), gemsConnectionNode.getEventCount(), l2 == null ? new Long(0) : l2, l3 == null ? new Long(0) : l3);
                arrcellValue[5] = new CellValue(String.valueOf(serverInfo.getConnectionCount()), serverInfo.getConnectionCount(), gemsConnectionNode.getWarnLimit("Connections"), gemsConnectionNode.getErrorLimit("Connections"));
                arrcellValue[6] = new CellValue(string2, l, gemsConnectionNode.getWarnLimit("Sessions"), gemsConnectionNode.getErrorLimit("Sessions"));
                arrcellValue[7] = new CellValue(String.valueOf(serverInfo.getQueueCount()), serverInfo.getQueueCount(), gemsConnectionNode.getWarnLimit("Queues"), gemsConnectionNode.getErrorLimit("Queues"));
                arrcellValue[8] = new CellValue(String.valueOf(serverInfo.getTopicCount()), serverInfo.getTopicCount(), gemsConnectionNode.getWarnLimit("Topics"), gemsConnectionNode.getErrorLimit("Topics"));
                arrcellValue[9] = new CellValue(String.valueOf(serverInfo.getDurableCount()), serverInfo.getDurableCount(), gemsConnectionNode.getWarnLimit("Durables"), gemsConnectionNode.getErrorLimit("Durables"));
                arrcellValue[10] = new CellValue(String.valueOf(serverInfo.getPendingMessageCount()), serverInfo.getPendingMessageCount(), gemsConnectionNode.getWarnLimit("PendingMsgs"), gemsConnectionNode.getErrorLimit("PendingMsgs"));
                arrcellValue[11] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getPendingMessageSize()), serverInfo.getPendingMessageSize(), gemsConnectionNode.getWarnLimit("PendingMsgSize"), gemsConnectionNode.getErrorLimit("PendingMsgSize"));
                arrcellValue[12] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getMsgMem()), serverInfo.getMsgMem(), gemsConnectionNode.getWarnLimit("MsgMem"), gemsConnectionNode.getErrorLimit("MsgMem"));
                arrcellValue[13] = new CellValue(String.valueOf(serverInfo.getInboundMessageCount()), serverInfo.getInboundMessageCount(), gemsConnectionNode.getWarnLimit("InMsgCount"), gemsConnectionNode.getErrorLimit("InMsgCount"));
                arrcellValue[14] = new CellValue(String.valueOf(serverInfo.getInboundMessageRate()), serverInfo.getInboundMessageRate(), gemsConnectionNode.getWarnLimit("InMsgRate"), gemsConnectionNode.getErrorLimit("InMsgRate"));
                arrcellValue[15] = new CellValue(String.valueOf(serverInfo.getOutboundMessageCount()), serverInfo.getOutboundMessageCount(), gemsConnectionNode.getWarnLimit("OutMsgCount"), gemsConnectionNode.getErrorLimit("OutMsgCount"));
                arrcellValue[16] = new CellValue(String.valueOf(serverInfo.getOutboundMessageRate()), serverInfo.getOutboundMessageRate(), gemsConnectionNode.getWarnLimit("OutMsgRate"), gemsConnectionNode.getErrorLimit("OutMsgRate"));
                arrcellValue[17] = new CellValue(String.valueOf(serverInfo.getDiskReadRate()), serverInfo.getDiskReadRate(), gemsConnectionNode.getWarnLimit("DiskReadRate"), gemsConnectionNode.getErrorLimit("DiskReadRate"));
                arrcellValue[18] = new CellValue(String.valueOf(serverInfo.getDiskWriteRate()), serverInfo.getDiskWriteRate(), gemsConnectionNode.getWarnLimit("DiskWriteRate"), gemsConnectionNode.getErrorLimit("DiskWriteRate"));
                arrcellValue[19] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getAsyncDBSize()), serverInfo.getAsyncDBSize(), gemsConnectionNode.getWarnLimit("AsyncDBSize"), gemsConnectionNode.getErrorLimit("AsyncDBSize"));
                arrcellValue[20] = new CellValue(StringUtilities.getHumanReadableSize(serverInfo.getSyncDBSize()), serverInfo.getSyncDBSize(), gemsConnectionNode.getWarnLimit("SyncDBSize"), gemsConnectionNode.getErrorLimit("SyncDBSize"));
                arrobject = arrcellValue;
            }
            if (Gems.getGems().getShowTotals()) {
                long[] arrl = this.totals;
                arrl[1] = arrl[1] + (long) serverInfo.getConnectionCount();
                long[] arrl2 = this.totals;
                arrl2[2] = arrl2[2] + l;
                long[] arrl3 = this.totals;
                arrl3[3] = arrl3[3] + (long) serverInfo.getQueueCount();
                long[] arrl4 = this.totals;
                arrl4[4] = arrl4[4] + (long) serverInfo.getTopicCount();
                long[] arrl5 = this.totals;
                arrl5[5] = arrl5[5] + (long) serverInfo.getDurableCount();
                long[] arrl6 = this.totals;
                arrl6[6] = arrl6[6] + serverInfo.getPendingMessageCount();
                long[] arrl7 = this.totals;
                arrl7[7] = arrl7[7] + serverInfo.getPendingMessageSize();
                long[] arrl8 = this.totals;
                arrl8[8] = arrl8[8] + serverInfo.getMsgMem();
                long[] arrl9 = this.totals;
                arrl9[9] = arrl9[9] + serverInfo.getAsyncDBSize();
                long[] arrl10 = this.totals;
                arrl10[10] = arrl10[10] + serverInfo.getSyncDBSize();
                long[] arrl11 = this.totals;
                arrl11[11] = arrl11[11] + serverInfo.getInboundMessageCount();
                long[] arrl12 = this.totals;
                arrl12[12] = arrl12[12] + serverInfo.getInboundMessageRate();
                long[] arrl13 = this.totals;
                arrl13[13] = arrl13[13] + serverInfo.getOutboundMessageCount();
                long[] arrl14 = this.totals;
                arrl14[14] = arrl14[14] + serverInfo.getOutboundMessageRate();
                long[] arrl15 = this.totals;
                arrl15[15] = arrl15[15] + serverInfo.getDiskReadRate();
                long[] arrl16 = this.totals;
                arrl16[16] = arrl16[16] + serverInfo.getDiskWriteRate();
            }
            this.addRow(arrobject);
        } catch (Exception var5_7) {
            System.err.println("JMSException: " + var5_7.getMessage());
            return;
        }
    }

    class MyRenderer
            extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
            Component component = super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
            component.setForeground(Color.black);
            if (object instanceof CellValue) {
                CellValue cellValue = (CellValue) object;
                this.setText(cellValue.toString());
                if (cellValue.m_value < -2) {
                    this.setHorizontalAlignment(2);
                    component.setBackground(Color.white);
                } else if (cellValue.m_value == -1) {
                    this.setHorizontalAlignment(2);
                    if (cellValue.m_cellValue.equals("Active")) {
                        component.setBackground(Color.green);
                    } else if (cellValue.m_cellValue.equals("Standby")) {
                        component.setBackground(Color.yellow);
                    } else if (cellValue.m_cellValue.equals("Unknown")) {
                        component.setBackground(Color.red);
                    } else {
                        component.setBackground(Color.white);
                    }
                } else if (cellValue.m_value == -2) {
                    this.setHorizontalAlignment(2);
                    component.setBackground(Color.white);
                    if (cellValue.m_cellValue != null && cellValue.m_cellValue.equals("Disconnected")) {
                        component.setBackground(Color.red);
                    }
                } else {
                    this.setHorizontalAlignment(4);
                    if (cellValue.m_errorLimit != null && cellValue.m_value > cellValue.m_errorLimit) {
                        component.setBackground(Color.red);
                    } else if (cellValue.m_warnLimit != null && cellValue.m_value > cellValue.m_warnLimit) {
                        component.setBackground(Color.orange);
                    } else {
                        component.setBackground(Color.white);
                    }
                }
            } else if (object instanceof String) {
                if (n2 > 3) {
                    this.setHorizontalAlignment(4);
                } else {
                    this.setHorizontalAlignment(2);
                }
                component.setBackground(Color.cyan);
            }
            return component;
        }
    }

}

