/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.ACLEntry
 *  com.tibco.tibjms.admin.AdminACLEntry
 *  com.tibco.tibjms.admin.AdminPermissions
 *  com.tibco.tibjms.admin.BridgeInfo
 *  com.tibco.tibjms.admin.BridgeTarget
 *  com.tibco.tibjms.admin.ChannelInfo
 *  com.tibco.tibjms.admin.ConnectionFactoryInfo
 *  com.tibco.tibjms.admin.ConnectionInfo
 *  com.tibco.tibjms.admin.ConsumerInfo
 *  com.tibco.tibjms.admin.DbStoreInfo
 *  com.tibco.tibjms.admin.DestinationBridgeInfo
 *  com.tibco.tibjms.admin.DestinationInfo
 *  com.tibco.tibjms.admin.DurableInfo
 *  com.tibco.tibjms.admin.FileStoreInfo
 *  com.tibco.tibjms.admin.GroupInfo
 *  com.tibco.tibjms.admin.MStoreInfo
 *  com.tibco.tibjms.admin.Permissions
 *  com.tibco.tibjms.admin.PrincipalInfo
 *  com.tibco.tibjms.admin.ProducerInfo
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.RVCMTransportInfo
 *  com.tibco.tibjms.admin.RVQueuePolicy
 *  com.tibco.tibjms.admin.RVTransportInfo
 *  com.tibco.tibjms.admin.RouteInfo
 *  com.tibco.tibjms.admin.RouteSelector
 *  com.tibco.tibjms.admin.SSTransportInfo
 *  com.tibco.tibjms.admin.StatData
 *  com.tibco.tibjms.admin.StoreInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 *  com.tibco.tibjms.admin.TopicInfo
 *  com.tibco.tibjms.admin.TransactionInfo
 *  com.tibco.tibjms.admin.TransportInfo
 *  com.tibco.tibjms.admin.UserInfo
 *  com.tibco.tibjms.admin.VersionInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.tibco.tibjms.admin.*;

//import com.tibco.tibjms.admin.MStoreInfo;

public class GemsDetailsTableModel
        extends DefaultTableModel
        implements GetPopupHandler {
    static String[] s_error_cols = new String[]{"Error"};
    static String[] s_service_cols = new String[]{"ServiceName", "StartPeriod", "RequestCount", "ResponseCount", "AvgRequests/Min", "AvgRespTime(ms)", "MaxRespTime(ms)", "RespTimeLimit(ms)", "%WithinLimit", "OverLimitCount", "LastRequestAt"};
    static String[] s_queues_cols = new String[]{"QueueName", "PendingMsgCount", "PendingMsgSize", "ReceiverCount", "InTotalMsgs", "OutTotalMsgs", "InMsgRate", "OutMsgRate", "Static", "Routed", "RouteConnected", "RouteName"};
    static String[] s_topics_cols = new String[]{"TopicName", "PendingMsgCount", "PendingMsgSize", "SubscriberCount", "InTotalMsgs", "OutTotalMsgs", "InMsgRate", "OutMsgRate", "DurableCount", "Static", "Global"};
    static String[] s_conn_cols = new String[]{"ID", "Type", "Host", "Address", "ClientID", "ConsumerCount", "ProducerCount", "SessionCount", "StartTime", "UpTime", "URL", "Username", "ClientVersion", "ClientType", "isFT", "isXA", "isAdmin", "UncommittedCount", "UncommittedSize"};
    static String[] s_sys_conn_cols = new String[]{"ID", "Type", "Host", "Address", "ConsumerCount", "ProducerCount", "SessionCount", "StartTime", "UpTime", "URL", "Username", "ClientVersion", "isFT", "UncommittedCount", "UncommittedSize"};
    static String[] s_bridge_cols = new String[]{"SourceName", "SourceType", "Targets"};
    static String[] s_destbridge_cols = new String[]{"Source", "SourceType", "Target", "TargetType", "Selector"};
    static String[] s_storesFile_cols = new String[]{"StoreName", "FileName", "FileSize", "FreeSpace", "UsedSpace", "Fragmentation", "MsgSize", "MsgCount", "SyncWrites", "SwappedSize", "SwappedCount"};
    static String[] s_storesDb_cols = new String[]{"StoreName", "URL", "UserName", "MsgCount", "SwappedCount", "DriverName", "DriverDialect"};
    static String[] s_storesMStore_cols = new String[]{"StoreName", "FileName", "FileSize", "FreeSpace", "UsedSpace", "MsgCount", "SwappedCount", "DiscardScanInterval", "DiscardScanBytes", "FirstScanFinished"};
    static String[] s_group_cols = new String[]{"GroupName", "Description", "isExternal", "Users"};
    static String[] s_chan_cols = new String[]{"Name", "Address", "Interface", "Active", "ByteRate", "MsgRate", "TotalBytes", "TotalMsgs", "BacklogCount", "BacklogSize", "BufferedBytes", "MaxRate", "MaxTime", "TransmittedBytes", "RetransmittedBytes", "TTL", "Priority"};
    static String[] s_fact_cols = new String[]{"Aliases", "URL", "Type", "ClientID", "ConnectCount", "ConnectDelay", "ReconnectCount", "ReconnectDelay", "XA", "LB Metric"};
    static String[] s_cons_cols = new String[]{"ID", "CreateTime", "DestinationName", "DestinationType", "DurableName", "PendingMsgCount", "PendingMsgSize", "Selector", "ConnectionID", "SessionID", "Username", "ByteRate", "MsgRate", "TotalBytes", "TotalMsgs", "Multicast"};
    static String[] s_route_cols = new String[]{"Name", "URL", "Connected", "ConnectionID", "Stalled", "ZoneName", "ZoneType", "InTotalMsgs", "OutTotalMsgs", "InMsgRate", "OutMsgRate", "inSelectors", "outSelectors", "Configured", "BacklogCount", "BacklogSize"};
    static String[] s_dur_cols = new String[]{"DurableName", "TopicName", "Active", "PendingMsgCount", "PendingMsgSize", "ClientID", "ConsumerID", "Username", "Selector", "NoLocal", "DeliveredMsgCount", "Static"};
    static String[] s_qprop_cols = new String[]{"QueueProperty", "Value"};
    static String[] s_tprop_cols = new String[]{"TopicProperty", "Value"};
    static String[] s_prod_cols = new String[]{"ID", "CreateTime", "DestinationName", "DestinationType", "ConnectionID", "SessionID", "Username", "ByteRate", "MsgRate", "TotalBytes", "TotalMsgs"};
    static String[] s_acl_cols = new String[]{"DestinationName", "DestinationType", "PrincipalName", "PrincipalType", "isExternal", "Permissions"};
    static String[] s_adminacl_cols = new String[]{"PrincipalName", "PrincipalType", "isExternal", "AdministrativePermissions"};
    static String[] s_prop_cols = new String[]{"Property", "Value"};
    static String[] s_ssactive_cols = new String[]{"Identifier", "Date", "Time"};
    static String[] s_ssactiveL_cols = new String[]{"Destination"};
    static String[] s_ssinactive_cols = new String[]{"Identifier", "SSType", "Reason", "Disable Date", "Disable Time"};
    static String[] s_ssinterface_cols = new String[]{"Interface ID", "Type", "Date", "Time", "Busy", "Applid", "Status"};
    static String[] s_ssimsstats_cols = new String[]{"IMS Buffer", "Value"};
    static String[] s_ssimsbuffs_cols = new String[]{"IMS Property", "Value"};
    static String[] s_ssimsgen_cols = new String[]{"IMS Connection Property", "Con Value"};
    static String[] s_user_cols = new String[]{"UserName", "Desciption", "isExternal"};
    static String[] s_transact_cols = new String[]{"State", "GlobalTransactionId", "FormatId", "BranchQualifier"};
    static String[] s_transprt_cols = new String[]{"TransportName", "Type", "QueueImportDelMode", "TopicImportDelMode", "ExportHeaders", "ExportProperties", "FurtherInfo"};
    static String[] s_client_cols = new String[]{"ClientID", "ConnectionID", "ConsumerCount", "ProducerCount", "SessionCount", "InTotalMsgs", "OutTotalMsgs", "InMsgRate", "OutMsgRate"};
    JTable m_table;
    MyRenderer m_renderer;
    Hashtable m_colWidths;
    boolean flag;
    PopupHandler m_popup;
    SimpleDateFormat dateFormatMillis;

    public GemsDetailsTableModel() {
        this.m_renderer = new MyRenderer();
        this.m_colWidths = null;
        this.flag = true;
        this.m_popup = null;
        this.dateFormatMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss SSS zzz yyyy");
        this.initColumnWidths();
    }

    public void initColumnWidths() {
        this.m_colWidths = new Hashtable();
        this.m_colWidths.put(new String("ClientID"), new Integer(150));
        this.m_colWidths.put(new String("ConnectionID"), new Integer(80));
        this.m_colWidths.put(new String("ConnectionCount"), new Integer(100));
        this.m_colWidths.put(new String("ConsumerCount"), new Integer(100));
        this.m_colWidths.put(new String("ProducerCount"), new Integer(100));
        this.m_colWidths.put(new String("SessionCount"), new Integer(100));
        this.m_colWidths.put(new String("InTotalMsgs"), new Integer(80));
        this.m_colWidths.put(new String("InMsgRate"), new Integer(80));
        this.m_colWidths.put(new String("OutTotalMsgs"), new Integer(80));
        this.m_colWidths.put(new String("OutMsgRate"), new Integer(80));
        this.m_colWidths.put(new String("PrincipalName"), new Integer(100));
        this.m_colWidths.put(new String("PrincipalType"), new Integer(100));
        this.m_colWidths.put(new String("AdministrativePermissions"), new Integer(600));
        this.m_colWidths.put(new String("DestinationName"), new Integer(200));
        this.m_colWidths.put(new String("DestinationType"), new Integer(100));
        this.m_colWidths.put(new String("PrincipalName"), new Integer(100));
        this.m_colWidths.put(new String("Permissions"), new Integer(400));
        this.m_colWidths.put(new String("QueueName"), new Integer(275));
        this.m_colWidths.put(new String("PendingMsgCount"), new Integer(120));
        this.m_colWidths.put(new String("PendingMsgSize"), new Integer(110));
        this.m_colWidths.put(new String("ReceiverCount"), new Integer(90));
        this.m_colWidths.put(new String("RouteConnected"), new Integer(100));
        this.m_colWidths.put(new String("RouteName"), new Integer(100));
        this.m_colWidths.put(new String("TopicName"), new Integer(275));
        this.m_colWidths.put(new String("SubscriberCount"), new Integer(100));
        this.m_colWidths.put(new String("DurableCount"), new Integer(90));
        this.m_colWidths.put(new String("ID"), new Integer(50));
        this.m_colWidths.put(new String("Type"), new Integer(100));
        this.m_colWidths.put(new String("Address"), new Integer(100));
        this.m_colWidths.put(new String("ClientID"), new Integer(100));
        this.m_colWidths.put(new String("Host"), new Integer(110));
        this.m_colWidths.put(new String("ClientVersion"), new Integer(100));
        this.m_colWidths.put(new String("StartTime"), new Integer(200));
        this.m_colWidths.put(new String("SourceName"), new Integer(200));
        this.m_colWidths.put(new String("Source"), new Integer(200));
        this.m_colWidths.put(new String("SourceType"), new Integer(100));
        this.m_colWidths.put(new String("Targets"), new Integer(500));
        this.m_colWidths.put(new String("Target"), new Integer(200));
        this.m_colWidths.put(new String("TargetType"), new Integer(100));
        this.m_colWidths.put(new String("FileName"), new Integer(200));
        this.m_colWidths.put(new String("SwappedSize"), new Integer(100));
        this.m_colWidths.put(new String("SwappedCount"), new Integer(100));
        this.m_colWidths.put(new String("GroupName"), new Integer(100));
        this.m_colWidths.put(new String("Description"), new Integer(150));
        this.m_colWidths.put(new String("Users"), new Integer(500));
        this.m_colWidths.put(new String("Aliases"), new Integer(200));
        this.m_colWidths.put(new String("URL"), new Integer(200));
        this.m_colWidths.put(new String("ConnectDelay"), new Integer(100));
        this.m_colWidths.put(new String("ConnectCount"), new Integer(100));
        this.m_colWidths.put(new String("ReconnectCount"), new Integer(110));
        this.m_colWidths.put(new String("ReconnectDelay"), new Integer(110));
        this.m_colWidths.put(new String("XA"), new Integer(50));
        this.m_colWidths.put(new String("LB Metric"), new Integer(80));
        this.m_colWidths.put(new String("CreateTime"), new Integer(200));
        this.m_colWidths.put(new String("DurableName"), new Integer(100));
        this.m_colWidths.put(new String("Selector"), new Integer(180));
        this.m_colWidths.put(new String("Name"), new Integer(100));
        this.m_colWidths.put(new String("inSelectors"), new Integer(120));
        this.m_colWidths.put(new String("outSelectors"), new Integer(120));
        this.m_colWidths.put(new String("Configured"), new Integer(70));
        this.m_colWidths.put(new String("Stalled"), new Integer(60));
        this.m_colWidths.put(new String("ZoneName"), new Integer(120));
        this.m_colWidths.put(new String("ZoneType"), new Integer(120));
        this.m_colWidths.put(new String("Active"), new Integer(40));
        this.m_colWidths.put(new String("Static"), new Integer(50));
        this.m_colWidths.put(new String("Global"), new Integer(50));
        this.m_colWidths.put(new String("DeliveredMsgCount"), new Integer(110));
        this.m_colWidths.put(new String("ServiceName"), new Integer(150));
        this.m_colWidths.put(new String("StartPeriod"), new Integer(200));
        this.m_colWidths.put(new String("RequestCount"), new Integer(100));
        this.m_colWidths.put(new String("ResponseCount"), new Integer(100));
        this.m_colWidths.put(new String("AvgRequests/Min"), new Integer(110));
        this.m_colWidths.put(new String("AvgRespTime(ms)"), new Integer(110));
        this.m_colWidths.put(new String("MaxRespTime(ms)"), new Integer(110));
        this.m_colWidths.put(new String("RespTimeLimit(ms)"), new Integer(120));
        this.m_colWidths.put(new String("%WithinLimit"), new Integer(90));
        this.m_colWidths.put(new String("OverLimitCount"), new Integer(100));
        this.m_colWidths.put(new String("LastRequestAt"), new Integer(200));
        this.m_colWidths.put(new String("StoreName"), new Integer(100));
        this.m_colWidths.put(new String("BacklogCount"), new Integer(100));
        this.m_colWidths.put(new String("BacklogSize"), new Integer(100));
        this.m_colWidths.put(new String("BufferedBytes"), new Integer(100));
        this.m_colWidths.put(new String("TransmittedBytes"), new Integer(120));
        this.m_colWidths.put(new String("RetransmittedBytes"), new Integer(130));
        this.m_colWidths.put(new String("Interface"), new Integer(100));
        this.m_colWidths.put(new String("DriverName"), new Integer(150));
        this.m_colWidths.put(new String("DriverDialect"), new Integer(150));
        this.m_colWidths.put(new String("UncommittedSize"), new Integer(110));
        this.m_colWidths.put(new String("UncommittedCount"), new Integer(110));
        this.m_colWidths.put(new String("SyncWrites"), new Integer(80));
        this.m_colWidths.put(new String("Identifier"), new Integer(280));
        this.m_colWidths.put(new String("Date"), new Integer(100));
        this.m_colWidths.put(new String("Time"), new Integer(110));
        this.m_colWidths.put(new String("Reason"), new Integer(110));
        this.m_colWidths.put(new String("SSType"), new Integer(80));
        this.m_colWidths.put(new String("Disable Date"), new Integer(100));
        this.m_colWidths.put(new String("Disable Time"), new Integer(100));
        this.m_colWidths.put(new String("Interface ID"), new Integer(80));
        this.m_colWidths.put(new String("Type"), new Integer(80));
        this.m_colWidths.put(new String("Busy"), new Integer(80));
        this.m_colWidths.put(new String("Applid"), new Integer(100));
        this.m_colWidths.put(new String("Status"), new Integer(200));
        this.m_colWidths.put(new String("Destination"), new Integer(400));
        this.m_colWidths.put(new String("IMS Property"), new Integer(200));
        this.m_colWidths.put(new String("IMS Buffer"), new Integer(200));
        this.m_colWidths.put(new String("IMS Connection Property"), new Integer(300));
        this.m_colWidths.put(new String("Con Value"), new Integer(300));
        this.m_colWidths.put(new String("Fragmentation"), new Integer(100));
        this.m_colWidths.put(new String("DiscardScanInterval"), new Integer(120));
        this.m_colWidths.put(new String("DiscardScanBytes"), new Integer(115));
        this.m_colWidths.put(new String("FirstScanFinished"), new Integer(100));
        this.m_colWidths.put(new String("State"), new Integer(150));
        this.m_colWidths.put(new String("GlobalTransactionId"), new Integer(300));
        this.m_colWidths.put(new String("FormatId"), new Integer(200));
        this.m_colWidths.put(new String("BranchQualifier"), new Integer(300));
        this.m_colWidths.put(new String("TransportName"), new Integer(100));
        this.m_colWidths.put(new String("QueueImportDelMode"), new Integer(135));
        this.m_colWidths.put(new String("TopicImportDelMode"), new Integer(130));
        this.m_colWidths.put(new String("ExportHeaders"), new Integer(95));
        this.m_colWidths.put(new String("ExportProperties"), new Integer(105));
        this.m_colWidths.put(new String("FurtherInfo"), new Integer(700));
        String string = Gems.getGems().getDetailPaneColWidths();
        String[] arrstring = string.split(",");
        for (int i = 0; i < arrstring.length; ++i) {
            try {
                int n = arrstring[i].indexOf(58);
                if (n <= 0) continue;
                String string2 = arrstring[i].substring(0, n);
                String string3 = arrstring[i].substring(n + 1);
                this.m_colWidths.put(string2, Integer.valueOf(string3));
                continue;
            } catch (NumberFormatException var4_5) {
                System.err.println("NumberFormatException: " + var4_5.getMessage());
                return;
            }
        }
    }

    public PopupHandler getPopupHandler() {
        if (this.m_popup == null) {
            this.m_popup = new PopupDetailsTableHandler(this.m_table, this);
        }
        return this.m_popup;
    }

    public void setPopupHandler(PopupHandler popupHandler) {
        this.m_popup = popupHandler;
    }

    public void setTable(JTable jTable) {
        this.m_table = jTable;
        if (Gems.getGems().getColourPendingMsgs()) {
            this.m_table.setDefaultRenderer(new Long(1).getClass(), this.m_renderer);
            this.m_table.setDefaultRenderer(new Object().getClass(), this.m_renderer);
            this.m_table.setDefaultRenderer(new MsgCellValue(0).getClass(), this.m_renderer);
            this.m_table.setDefaultRenderer(new CellValue("").getClass(), this.m_renderer);
            this.m_table.setDefaultRenderer(new SSCellValue("", "Head").getClass(), this.m_renderer);
        }
    }

    public Class getColumnClass(int n) {
        Object object = this.getValueAt(0, n);
        if (object != null) {
            if (object instanceof MsgCellValue) {
                return new Long(0).getClass();
            }
            if (object instanceof SSCellValue) {
                return new String().getClass();
            }
            if (object instanceof CellValue) {
                return new String().getClass();
            }
            return object.getClass();
        }
        return new String().getClass();
    }

    public boolean isCellEditable(int n, int n2) {
        return false;
    }

    public String getSelectedCol1() {
        if (this.m_table.getSelectedRow() < 0) {
            return null;
        }
        if (this.getColumnCount() == 1 && this.getColumnName(0).equals("Error")) {
            return null;
        }
        Object object = this.m_table.getValueAt(this.m_table.getSelectedRow(), 0);
        if (object instanceof Long) {
            return String.valueOf((Long) object);
        }
        return (String) object;
    }

    public String getSelectedCol(int n) {
        if (this.m_table.getSelectedRow() < 0) {
            return null;
        }
        Object object = this.m_table.getValueAt(this.m_table.getSelectedRow(), n - 1);
        if (object instanceof Long) {
            return String.valueOf((Long) object);
        }
        return (String) object;
    }

    public int getSelectedRow() {
        return this.m_table.getSelectedRow();
    }

    public void maintainSelection(int n, String string) {
        if (n < 0 || n >= this.m_table.getRowCount()) {
            return;
        }
        Object object = this.m_table.getValueAt(n, 0);
        String string2 = object instanceof Long ? String.valueOf((Long) object) : (String) object;
        if (string2.equals(string)) {
            this.m_table.addRowSelectionInterval(n, n);
        }
    }

    public void populateErrorInfo(String string) {
        this.setRowCount(0);
        this.setColumnCount(0);
        this.m_table.setAutoResizeMode(4);
        this.setColumnIdentifiers(s_error_cols);
        this.addRow(new Object[]{string});
    }

    public void populateServiceInfo(GemsServiceTable gemsServiceTable) {
        if (gemsServiceTable == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 11 || !this.getColumnName(0).equals("ServiceName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_service_cols);
            this.setupColumnWidths();
        }
        try {
            Enumeration enumeration = gemsServiceTable.m_services.keys();
            while (enumeration.hasMoreElements()) {
                GemsService gemsService = (GemsService) gemsServiceTable.m_services.get(enumeration.nextElement());
                Date date = new Date();
                Date date2 = new Date();
                date.setTime(gemsService.m_started);
                date2.setTime(gemsService.m_lastRequest);
                long l = System.currentTimeMillis();
                double d = 0.0;
                double d2 = (double) (l - gemsService.m_started) / 60000.0;
                if (d2 >= 1.0) {
                    d = (double) gemsService.m_hits / d2;
                }
                double d3 = 0.0;
                if (gemsService.m_responses > 0) {
                    d3 = (double) gemsService.m_totalLatency / (double) gemsService.m_responses;
                }
                double d4 = 100.0;
                if (gemsService.m_responses > 0) {
                    d4 -= (double) gemsService.m_overLimitCount / (double) gemsService.m_responses * 100.0;
                }
                Object[] arrobject = new Object[11];
                arrobject[0] = gemsService.m_name;
                arrobject[1] = gemsServiceTable.m_running ? date.toString() : "Disabled";
                arrobject[2] = new Long(gemsService.m_hits);
                arrobject[3] = new Long(gemsService.m_responses);
                arrobject[4] = new Double(d);
                arrobject[5] = new Double(d3);
                arrobject[6] = new Long(gemsService.m_maxLatency);
                arrobject[7] = new Long(gemsService.m_respLimit);
                arrobject[8] = new Double(d4);
                arrobject[9] = new Long(gemsService.m_overLimitCount);
                arrobject[10] = gemsService.m_lastRequest > 0 ? this.dateFormatMillis.format(date2).toString() : "";
                Object[] arrobject2 = arrobject;
                this.addRow(arrobject2);
            }
        } catch (Exception var2_4) {
            System.err.println("JMSException: " + var2_4.getMessage());
            return;
        }
    }

    public void setupColumnWidths() {
        for (int i = 0; i < this.getColumnCount(); ++i) {
            TableColumn tableColumn = this.m_table.getColumnModel().getColumn(i);
            tableColumn.addPropertyChangeListener(new ColumnListener());
            Integer n = (Integer) this.m_colWidths.get(tableColumn.getHeaderValue());
            if (n == null) continue;
            tableColumn.setPreferredWidth(n);
        }
    }

    public void populateQueuesInfo(QueueInfo[] arrqueueInfo) {
        if (arrqueueInfo == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 12 || !this.getColumnName(0).equals("QueueName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_queues_cols);
            this.setupColumnWidths();
        }
        try {
            for (int i = 0; i < arrqueueInfo.length; ++i) {
                String string = "?";
                String string2 = "";
                try {
                    string = String.valueOf(arrqueueInfo[i].isRouted());
                    string2 = arrqueueInfo[i].getRouteName();
                } catch (Throwable var6_8) {
                    // empty catch block
                }
                long l = 0;
                try {
                    l = arrqueueInfo[i].getMaxMsgs();
                } catch (Throwable var8_9) {
                    // empty catch block
                }
                Object[] arrobject = new Object[]{arrqueueInfo[i].getName(), new MsgCellValue(arrqueueInfo[i].getPendingMessageCount(), l), new CellValue(StringUtilities.getHumanReadableSize(arrqueueInfo[i].getPendingMessageSize()), arrqueueInfo[i].getPendingMessageSize(), 1L, (Long) arrqueueInfo[i].getMaxBytes()), new Long(arrqueueInfo[i].getReceiverCount()), new Long(arrqueueInfo[i].getInboundStatistics().getTotalMessages()), new Long(arrqueueInfo[i].getOutboundStatistics().getTotalMessages()), new Long(arrqueueInfo[i].getInboundStatistics().getMessageRate()), new Long(arrqueueInfo[i].getOutboundStatistics().getMessageRate()), String.valueOf(arrqueueInfo[i].isStatic()), string, String.valueOf(arrqueueInfo[i].isRouteConnected()), string2};
                this.addRow(arrobject);
            }
        } catch (Exception var2_4) {
            System.err.println("JMSException: " + var2_4.getMessage());
            return;
        }
    }

    public void populateTopicsInfo(TopicInfo[] arrtopicInfo) {
        if (arrtopicInfo == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 11 || !this.getColumnName(0).equals("TopicName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_topics_cols);
            this.setupColumnWidths();
        }
        try {
            for (int i = 0; i < arrtopicInfo.length; ++i) {
                long l = 0;
                try {
                    l = arrtopicInfo[i].getMaxMsgs();
                } catch (Throwable var6_6) {
                    // empty catch block
                }
                Object[] arrobject = new Object[]{arrtopicInfo[i].getName(), new MsgCellValue(arrtopicInfo[i].getPendingMessageCount(), l), new CellValue(StringUtilities.getHumanReadableSize(arrtopicInfo[i].getPendingMessageSize()), arrtopicInfo[i].getPendingMessageSize(), 1L, (Long) arrtopicInfo[i].getMaxBytes()), new Long(arrtopicInfo[i].getSubscriberCount()), new Long(arrtopicInfo[i].getInboundStatistics().getTotalMessages()), new Long(arrtopicInfo[i].getOutboundStatistics().getTotalMessages()), new Long(arrtopicInfo[i].getInboundStatistics().getMessageRate()), new Long(arrtopicInfo[i].getOutboundStatistics().getMessageRate()), new Long(arrtopicInfo[i].getDurableCount()), String.valueOf(arrtopicInfo[i].isStatic()), String.valueOf(arrtopicInfo[i].isGlobal())};
                this.addRow(arrobject);
            }
        } catch (Exception var2_4) {
            System.err.println("JMSException: " + var2_4.getMessage());
            return;
        }
    }

    public void populateConnectionInfo(TibjmsAdmin tibjmsAdmin) {
        if (tibjmsAdmin == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 19 || !this.getColumnName(2).equals("Host")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_conn_cols);
            this.setupColumnWidths();
        }
        try {
            ConnectionInfo[] arrconnectionInfo = tibjmsAdmin.getConnections();
            Date date = new Date();
            for (int i = 0; arrconnectionInfo != null && i < arrconnectionInfo.length; ++i) {
                Long l = new Long(-1);
                String string = "?";
                try {
                    l = new Long(arrconnectionInfo[i].getUncommittedCount());
                    string = StringUtilities.getHumanReadableSize(new Long(arrconnectionInfo[i].getUncommittedSize()));
                } catch (Throwable var8_9) {
                    // empty catch block
                }
                date.setTime(arrconnectionInfo[i].getStartTime());
                Object[] arrobject = new Object[19];
                arrobject[0] = new Long(arrconnectionInfo[i].getID());
                arrobject[1] = arrconnectionInfo[i].getType() != null ? arrconnectionInfo[i].getType() : "<unknown>";
                arrobject[2] = arrconnectionInfo[i].getHost() != null ? arrconnectionInfo[i].getHost() : "<unknown>";
                arrobject[3] = arrconnectionInfo[i].getAddress();
                arrobject[4] = arrconnectionInfo[i].getClientID() != null ? arrconnectionInfo[i].getClientID() : new String();
                arrobject[5] = new Long(arrconnectionInfo[i].getConsumerCount());
                arrobject[6] = new Long(arrconnectionInfo[i].getProducerCount());
                arrobject[7] = new Long(arrconnectionInfo[i].getSessionCount());
                arrobject[8] = date.toString();
                arrobject[9] = new Long(arrconnectionInfo[i].getUpTime());
                arrobject[10] = arrconnectionInfo[i].getURL();
                arrobject[11] = arrconnectionInfo[i].getUserName();
                arrobject[12] = arrconnectionInfo[i].getVersionInfo().toString();
                arrobject[13] = arrconnectionInfo[i].getClientType();
                arrobject[14] = String.valueOf(arrconnectionInfo[i].isFT());
                arrobject[15] = String.valueOf(arrconnectionInfo[i].isXA());
                arrobject[16] = String.valueOf(arrconnectionInfo[i].isAdmin());
                arrobject[17] = l;
                arrobject[18] = string;
                Object[] arrobject2 = arrobject;
                this.addRow(arrobject2);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateSystemConnectionInfo(TibjmsAdmin tibjmsAdmin) {
        if (tibjmsAdmin == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 15 || !this.getColumnName(2).equals("Host")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_sys_conn_cols);
            this.setupColumnWidths();
        }
        try {
            ConnectionInfo[] arrconnectionInfo = tibjmsAdmin.getSystemConnections();
            Date date = new Date();
            for (int i = 0; arrconnectionInfo != null && i < arrconnectionInfo.length; ++i) {
                Long l = new Long(-1);
                String string = "?";
                try {
                    l = new Long(arrconnectionInfo[i].getUncommittedCount());
                    string = StringUtilities.getHumanReadableSize(new Long(arrconnectionInfo[i].getUncommittedSize()));
                } catch (Throwable var8_10) {
                    // empty catch block
                }
                date.setTime(arrconnectionInfo[i].getStartTime());
                Object[] arrobject = new Object[15];
                arrobject[0] = new Long(arrconnectionInfo[i].getID());
                arrobject[1] = arrconnectionInfo[i].getType() != null ? arrconnectionInfo[i].getType() : "<unknown>";
                arrobject[2] = arrconnectionInfo[i].getHost() != null ? arrconnectionInfo[i].getHost() : "<unknown>";
                arrobject[3] = arrconnectionInfo[i].getAddress();
                arrobject[4] = new Long(arrconnectionInfo[i].getConsumerCount());
                arrobject[5] = new Long(arrconnectionInfo[i].getProducerCount());
                arrobject[6] = new Long(arrconnectionInfo[i].getSessionCount());
                arrobject[7] = date.toString();
                arrobject[8] = new Long(arrconnectionInfo[i].getUpTime());
                arrobject[9] = arrconnectionInfo[i].getURL();
                arrobject[10] = arrconnectionInfo[i].getUserName();
                arrobject[11] = arrconnectionInfo[i].getVersionInfo().toString();
                arrobject[12] = String.valueOf(arrconnectionInfo[i].isFT());
                arrobject[13] = l;
                arrobject[14] = string;
                Object[] arrobject2 = arrobject;
                this.addRow(arrobject2);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        } catch (Exception var2_4) {
            return;
        }
    }

    public void populateBridgeInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        try {
            DestinationBridgeInfo[] arrdestinationBridgeInfo = gemsConnectionNode.m_adminConn.getDestinationBridges(0, ">");
            this.populatetDesBridgeInfo(arrdestinationBridgeInfo);
            return;
        } catch (Throwable var2_3) {
            this.setRowCount(0);
            if (this.getColumnCount() != 3 || !this.getColumnName(0).equals("SourceName")) {
                this.setColumnCount(0);
                this.m_table.setAutoResizeMode(0);
                this.setColumnIdentifiers(s_bridge_cols);
                this.setupColumnWidths();
            }
            try {
                BridgeInfo[] arrbridgeInfo = gemsConnectionNode.m_adminConn.getBridges();
                for (int i = 0; arrbridgeInfo != null && i < arrbridgeInfo.length; ++i) {
                    Object[] arrobject = new Object[3];
                    arrobject[0] = arrbridgeInfo[i].getName();
                    arrobject[1] = arrbridgeInfo[i].getType() == 1 ? new String("Queue") : new String("Topic");
                    arrobject[2] = StringUtilities.arrayToString((Object[]) arrbridgeInfo[i].getTargets());
                    Object[] arrobject2 = arrobject;
                    this.addRow(arrobject2);
                }
            } catch (TibjmsAdminException var3_5) {
                System.err.println("JMSException: " + var3_5.getMessage());
                return;
            }
            return;
        }
    }

    public void populatetDesBridgeInfo(DestinationBridgeInfo[] arrdestinationBridgeInfo) {
        if (this.getColumnCount() != 5 || !this.getColumnName(0).equals("Source")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_destbridge_cols);
            this.setupColumnWidths();
        }
        for (int i = 0; arrdestinationBridgeInfo != null && i < arrdestinationBridgeInfo.length; ++i) {
            Object[] arrobject = new Object[5];
            arrobject[0] = arrdestinationBridgeInfo[i].getSourceName();
            arrobject[1] = arrdestinationBridgeInfo[i].getSourceType() == 1 ? new String("Queue") : new String("Topic");
            arrobject[2] = arrdestinationBridgeInfo[i].getTargetName();
            arrobject[3] = arrdestinationBridgeInfo[i].getTargetType() == 1 ? new String("Queue") : new String("Topic");
            arrobject[4] = arrdestinationBridgeInfo[i].getSelector();
            Object[] arrobject2 = arrobject;
            this.addRow(arrobject2);
        }
    }

    public void populateStoresFileInfo(GemsConnectionNode gemsConnectionNode) {
        String[] arrstring;
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 11 || !this.getColumnName(1).equals("FileName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_storesFile_cols);
            this.setupColumnWidths();
        }
        try {
            arrstring = gemsConnectionNode.m_adminConn.getStores();
        } catch (Throwable var3_3) {
            try {
                StoreInfo storeInfo = gemsConnectionNode.m_adminConn.getStoreInfo(2);
                Object[] arrobject = new Object[]{"$sys.nonfailsafe", "async-msgs.db", StringUtilities.getHumanReadableSize(storeInfo.getFileSize()), StringUtilities.getHumanReadableSize(storeInfo.getFreeSpace()), StringUtilities.getHumanReadableSize(storeInfo.getUsedSpace()), "N/A", StringUtilities.getHumanReadableSize(storeInfo.getMsgBytes()), String.valueOf(storeInfo.getMsgCount()), "false", StringUtilities.getHumanReadableSize(storeInfo.getSwappedBytes()), String.valueOf(storeInfo.getSwappedCount())};
                this.addRow(arrobject);
                storeInfo = gemsConnectionNode.m_adminConn.getStoreInfo(1);
                arrobject = new Object[]{"$sys.failsafe", "sync-msgs.db", StringUtilities.getHumanReadableSize(storeInfo.getFileSize()), StringUtilities.getHumanReadableSize(storeInfo.getFreeSpace()), StringUtilities.getHumanReadableSize(storeInfo.getUsedSpace()), "N/A", StringUtilities.getHumanReadableSize(storeInfo.getMsgBytes()), String.valueOf(storeInfo.getMsgCount()), "true", StringUtilities.getHumanReadableSize(storeInfo.getSwappedBytes()), String.valueOf(storeInfo.getSwappedCount())};
                this.addRow(arrobject);
            } catch (TibjmsAdminException var4_7) {
                System.err.println("JMSException: " + var4_7.getMessage());
                return;
            }
            return;
        }
        try {
            for (int i = 0; i < arrstring.length; ++i) {
                String string = arrstring[i];
                StoreInfo storeInfo = gemsConnectionNode.m_adminConn.getStoreInfo(string);
                FileStoreInfo fileStoreInfo = null;
                if (!(storeInfo instanceof FileStoreInfo)) continue;
                fileStoreInfo = (FileStoreInfo) storeInfo;
                String string2 = "N/A";
//                try {
//                    string2 = String.valueOf(fileStoreInfo.getFragmentation()) + "%";
//                }
//                catch (Throwable var9_14) {
//                     empty catch block
//                }
                System.out.println("fileStoreInfo.getFragmentation()");

                Object[] arrobject = new Object[]{string, fileStoreInfo.getFileName(), StringUtilities.getHumanReadableSize(fileStoreInfo.getSize()), StringUtilities.getHumanReadableSize(fileStoreInfo.getNotInUseSpace()), StringUtilities.getHumanReadableSize(fileStoreInfo.getInUseSpace()), string2, StringUtilities.getHumanReadableSize(storeInfo.getMsgBytes()), String.valueOf(storeInfo.getMsgCount()), String.valueOf(fileStoreInfo.isSynchronousWriteEnabled()), StringUtilities.getHumanReadableSize(storeInfo.getSwappedBytes()), String.valueOf(storeInfo.getSwappedCount())};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var3_5) {
            System.err.println("JMSException: " + var3_5.getMessage());
            return;
        }
    }

    public void populateStoresDbInfo(GemsConnectionNode gemsConnectionNode) {
        String[] arrstring;
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 7 || !this.getColumnName(5).equals("DriverName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_storesDb_cols);
            this.setupColumnWidths();
        }
        try {
            arrstring = gemsConnectionNode.m_adminConn.getStores();
        } catch (Throwable var3_3) {
            return;
        }
        try {
            for (int i = 0; i < arrstring.length; ++i) {
                String string = arrstring[i];
                StoreInfo storeInfo = gemsConnectionNode.m_adminConn.getStoreInfo(string);
                DbStoreInfo dbStoreInfo = null;
                if (!(storeInfo instanceof DbStoreInfo)) continue;
                dbStoreInfo = (DbStoreInfo) storeInfo;
                Object[] arrobject = new Object[]{string, dbStoreInfo.getURL(), dbStoreInfo.getUserName(), String.valueOf(storeInfo.getMsgCount()), String.valueOf(storeInfo.getSwappedCount()), dbStoreInfo.getDriverName(), dbStoreInfo.getDriverDialect()};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var3_5) {
            System.err.println("JMSException: " + var3_5.getMessage());
            return;
        }
    }

    public void populateStoresMStoreInfo(GemsConnectionNode gemsConnectionNode) {
        String[] arrstring;
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 10 || !this.getColumnName(7).equals("DiscardScanInterval")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_storesMStore_cols);
            this.setupColumnWidths();
        }
        try {
            arrstring = gemsConnectionNode.m_adminConn.getStores();
        } catch (Throwable var3_3) {
            return;
        }

        throw new RuntimeException("MStoreInfo");

//        try {
//            for (int i = 0; i < arrstring.length; ++i) {
//                String string = arrstring[i];
//                StoreInfo storeInfo = gemsConnectionNode.m_adminConn.getStoreInfo(string);
//                MStoreInfo mStoreInfo = null;
//                if (!(storeInfo instanceof MStoreInfo)) continue;
//                mStoreInfo = (MStoreInfo)storeInfo;
//                Object[] arrobject = new Object[]{string, mStoreInfo.getFileName(), StringUtilities.getHumanReadableSize(mStoreInfo.getSize()), StringUtilities.getHumanReadableSize(mStoreInfo.getNotInUseSpace()), StringUtilities.getHumanReadableSize(mStoreInfo.getInUseSpace()), String.valueOf(storeInfo.getMsgCount()), String.valueOf(storeInfo.getSwappedCount()), String.valueOf(mStoreInfo.getDiscardScanInterval()), StringUtilities.getHumanReadableSize(mStoreInfo.getDiscardScanBytes()), String.valueOf(mStoreInfo.isFirstScanFinished())};
//                this.addRow(arrobject);
//            }
//        }
//        catch (TibjmsAdminException var3_5) {
//            System.err.println("JMSException: " + var3_5.getMessage());
//            return;
//        }
    }

    public void populateGroupInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 4 || !this.getColumnName(0).equals("GroupName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_group_cols);
            this.setupColumnWidths();
        }
        try {
            GroupInfo[] arrgroupInfo = gemsConnectionNode.m_adminConn.getGroups();
            for (int i = 0; arrgroupInfo != null && i < arrgroupInfo.length; ++i) {
                StringBuffer stringBuffer = new StringBuffer();
                UserInfo[] arruserInfo = arrgroupInfo[i].getUsers();
                for (int j = 0; j < arruserInfo.length; ++j) {
                    if (j > 0) {
                        stringBuffer.append(", ");
                    }
                    stringBuffer.append(arruserInfo[j].getName());
                }
                Object[] arrobject = new Object[]{arrgroupInfo[i].getName(), arrgroupInfo[i].getDescription(), String.valueOf(arrgroupInfo[i].isExternal()), new String(stringBuffer)};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateChannelsInfo(GemsConnectionNode gemsConnectionNode) {
        ChannelInfo[] arrchannelInfo;
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 17 || !this.getColumnName(1).equals("Address")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_chan_cols);
            this.setupColumnWidths();
        }
        try {
            arrchannelInfo = gemsConnectionNode.m_adminConn.getChannels();
        } catch (Throwable var3_3) {
            return;
        }
        try {
            for (int i = 0; arrchannelInfo != null && i < arrchannelInfo.length; ++i) {
                Object[] arrobject = new Object[]{arrchannelInfo[i].getName(), arrchannelInfo[i].getAddress(), arrchannelInfo[i].getInterface(), String.valueOf(arrchannelInfo[i].isActive()), new Long(arrchannelInfo[i].getStatistics().getByteRate()), new Long(arrchannelInfo[i].getStatistics().getMessageRate()), new Long(arrchannelInfo[i].getStatistics().getTotalBytes()), new Long(arrchannelInfo[i].getStatistics().getTotalMessages()), new Long(arrchannelInfo[i].getBacklogCount()), StringUtilities.getHumanReadableSize(new Long(arrchannelInfo[i].getBacklogSize())), StringUtilities.getHumanReadableSize(new Long(arrchannelInfo[i].getBufferedBytes())), StringUtilities.getHumanReadableSize(new Long(arrchannelInfo[i].getMaxRate())), new Long(arrchannelInfo[i].getMaxTime()), StringUtilities.getHumanReadableSize(new Long(arrchannelInfo[i].getTransmittedBytes())), StringUtilities.getHumanReadableSize(new Long(arrchannelInfo[i].getRetransmittedBytes())), new Long(arrchannelInfo[i].getTtl()), new Long(arrchannelInfo[i].getPriority())};
                this.addRow(arrobject);
            }
        } catch (Exception var3_5) {
            System.err.println("Exception: " + var3_5.getMessage());
            return;
        }
    }

    public void populateFactoryInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 10 || !this.getColumnName(0).equals("Aliases")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_fact_cols);
            this.setupColumnWidths();
        }
        try {
            ConnectionFactoryInfo[] arrconnectionFactoryInfo = gemsConnectionNode.m_adminConn.getConnectionFactories();
            for (int i = 0; arrconnectionFactoryInfo != null && i < arrconnectionFactoryInfo.length; ++i) {
                String string = "Generic";
                if (arrconnectionFactoryInfo[i].getDestinationType() == 1) {
                    string = "Queue";
                } else if (arrconnectionFactoryInfo[i].getDestinationType() == 2) {
                    string = "Topic";
                }
                String string2 = "None";
                if (arrconnectionFactoryInfo[i].getMetric() == 1) {
                    string2 = "Connections";
                } else if (arrconnectionFactoryInfo[i].getMetric() == 2) {
                    string2 = "ByteRate";
                }
                Object[] arrobject = new Object[]{StringUtilities.arrayToString(arrconnectionFactoryInfo[i].getJNDINames()), arrconnectionFactoryInfo[i].getURL(), string, arrconnectionFactoryInfo[i].getClientID(), new Long(arrconnectionFactoryInfo[i].getConnectAttemptCount()), new Long(arrconnectionFactoryInfo[i].getConnectAttemptDelay()), new Long(arrconnectionFactoryInfo[i].getReconnectAttemptCount()), new Long(arrconnectionFactoryInfo[i].getReconnectAttemptDelay()), String.valueOf(arrconnectionFactoryInfo[i].getXAType()), string2};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateConsumerInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 16 || !this.getColumnName(4).equals("DurableName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_cons_cols);
            this.setupColumnWidths();
        }
        try {
            try {
                ConsumerInfo[] arrconsumerInfo = gemsConnectionNode.m_adminConn.getConsumers(null, null, null, false, 5);
                this.addConsumerInfo(arrconsumerInfo);
            } catch (Throwable var3_5) {
                this.setRowCount(0);
                ConsumerInfo[] arrconsumerInfo = gemsConnectionNode.m_adminConn.getConsumersStatistics();
                this.addConsumerInfo(arrconsumerInfo);
            }
        } catch (TibjmsAdminException var2_4) {
            System.err.println("JMSException: " + var2_4.getMessage());
            return;
        }
    }

    public void addConsumerInfo(ConsumerInfo[] arrconsumerInfo) {
        try {
            Date date = new Date();
            for (int i = 0; arrconsumerInfo != null && i < arrconsumerInfo.length; ++i) {
                String string = "?";
                Long l = new Long(-1);
                Long l2 = new Long(-1);
                try {
                    string = arrconsumerInfo[i].getSelector();
                    l = new Long(arrconsumerInfo[i].getPendingMessageCount());
                    l2 = new Long(arrconsumerInfo[i].getPendingMessageSize());
                } catch (Throwable var8_10) {
                    // empty catch block
                }
                date.setTime(arrconsumerInfo[i].getCreateTime());
                boolean bl = false;
                try {
                    bl = arrconsumerInfo[i].isMulticast();
                } catch (Throwable var9_12) {
                    // empty catch block
                }
                String string2 = "Generic";
                if (arrconsumerInfo[i].getDestinationType() == 1) {
                    string2 = "Queue";
                } else if (arrconsumerInfo[i].getDestinationType() == 2) {
                    string2 = "Topic";
                }
                Object[] arrobject = new Object[]{new Long(arrconsumerInfo[i].getID()), date.toString(), arrconsumerInfo[i].getDestinationName(), string2, arrconsumerInfo[i].getDurableName(), l, l2, string, new Long(arrconsumerInfo[i].getConnectionID()), new Long(arrconsumerInfo[i].getSessionID()), arrconsumerInfo[i].getUsername(), new Long(arrconsumerInfo[i].getStatistics().getByteRate()), new Long(arrconsumerInfo[i].getStatistics().getMessageRate()), new Long(arrconsumerInfo[i].getStatistics().getTotalBytes()), new Long(arrconsumerInfo[i].getStatistics().getTotalMessages()), String.valueOf(bl)};
                this.addRow(arrobject);
            }
        } catch (Exception var2_5) {
            System.err.println("JMSException: " + var2_5.getMessage());
            return;
        }
    }

    public void populateRoutesInfo(TibjmsAdmin tibjmsAdmin) {
        if (tibjmsAdmin == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 16 || !this.getColumnName(1).equals("URL")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_route_cols);
            this.setupColumnWidths();
        }
        try {
            RouteInfo[] arrrouteInfo = tibjmsAdmin.getRoutes();
            for (int i = 0; arrrouteInfo != null && i < arrrouteInfo.length; ++i) {
                Long l = new Long(-1);
                String string = "?";
                try {
                    l = new Long(arrrouteInfo[i].getBacklogCount());
                    string = StringUtilities.getHumanReadableSize(new Long(arrrouteInfo[i].getBacklogSize()));
                } catch (Throwable var7_9) {
                    // empty catch block
                }
                short s = arrrouteInfo[i].getZoneType();
                Object[] arrobject = new Object[16];
                arrobject[0] = arrrouteInfo[i].getName();
                arrobject[1] = arrrouteInfo[i].getURL();
                arrobject[2] = String.valueOf(arrrouteInfo[i].isConnected());
                arrobject[3] = new Long(arrrouteInfo[i].getConnectionID());
                arrobject[4] = String.valueOf(arrrouteInfo[i].isStalled());
                arrobject[5] = arrrouteInfo[i].getZoneName();
                arrobject[6] = s == 0 ? new String("MultiHop") : (s == 1 ? new String("OneHop") : new String("Unknown"));
                arrobject[7] = new Long(arrrouteInfo[i].getInboundStatistics().getTotalMessages());
                arrobject[8] = new Long(arrrouteInfo[i].getOutboundStatistics().getTotalMessages());
                arrobject[9] = new Long(arrrouteInfo[i].getInboundStatistics().getMessageRate());
                arrobject[10] = new Long(arrrouteInfo[i].getOutboundStatistics().getMessageRate());
                arrobject[11] = StringUtilities.arrayToString((Object[]) arrrouteInfo[i].getIncomingSelectors());
                arrobject[12] = StringUtilities.arrayToString((Object[]) arrrouteInfo[i].getOutgoingSelectors());
                arrobject[13] = String.valueOf(arrrouteInfo[i].isConfigured());
                arrobject[14] = l;
                arrobject[15] = string;
                Object[] arrobject2 = arrobject;
                this.addRow(arrobject2);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateDurablesInfo(TibjmsAdmin tibjmsAdmin) {
        if (tibjmsAdmin == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 12 || !this.getColumnName(0).equals("DurableName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_dur_cols);
            this.setupColumnWidths();
        }
        try {
            DurableInfo[] arrdurableInfo = tibjmsAdmin.getDurables();
            for (int i = 0; arrdurableInfo != null && i < arrdurableInfo.length; ++i) {
                String string = new String();
                Long l = new Long(-1);
                try {
                    string = String.valueOf(arrdurableInfo[i].isStatic());
                    l = new Long(arrdurableInfo[i].getDeliveredMessageCount());
                } catch (Throwable var7_9) {
                    // empty catch block
                }
                boolean bl = false;
                try {
                    bl = arrdurableInfo[i].isConnected();
                } catch (Throwable var8_10) {
                    bl = arrdurableInfo[i].isActive();
                }
                Object[] arrobject = new Object[12];
                arrobject[0] = arrdurableInfo[i].getDurableName();
                arrobject[1] = arrdurableInfo[i].getTopicName();
                arrobject[2] = String.valueOf(bl);
                arrobject[3] = new Long(arrdurableInfo[i].getPendingMessageCount());
                arrobject[4] = new Long(arrdurableInfo[i].getPendingMessageSize());
                arrobject[5] = arrdurableInfo[i].getClientID() != null ? arrdurableInfo[i].getClientID() : new String();
                arrobject[6] = new Long(arrdurableInfo[i].getConsumerID());
                arrobject[7] = arrdurableInfo[i].getUserName();
                arrobject[8] = arrdurableInfo[i].getSelector();
                arrobject[9] = String.valueOf(arrdurableInfo[i].isNoLocalEnabled());
                arrobject[10] = l;
                arrobject[11] = string;
                Object[] arrobject2 = arrobject;
                this.addRow(arrobject2);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateQueueInfo(TibjmsAdmin tibjmsAdmin, String string) {
        QueueInfo queueInfo;
        this.setRowCount(0);
        if (this.getColumnCount() != 2 || !this.getColumnName(0).equals("QueueProperty")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(2);
            this.setColumnIdentifiers(s_qprop_cols);
        }
        if (tibjmsAdmin == null) {
            return;
        }
        try {
            queueInfo = tibjmsAdmin.getQueue(string);
        } catch (TibjmsAdminException var4_4) {
            System.err.println("JMSException: " + var4_4.getMessage());
            return;
        }
        if (queueInfo == null) {
            return;
        }
        Object[] arrobject = new String[]{"Name", string};
        this.addRow(arrobject);
        arrobject = new String[]{"PendingMessageCount", String.valueOf(queueInfo.getPendingMessageCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"PendingMessageSize", StringUtilities.getHumanReadableSize(queueInfo.getPendingMessageSize())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundByteRate", String.valueOf(queueInfo.getInboundStatistics().getByteRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundMessageRate", String.valueOf(queueInfo.getInboundStatistics().getMessageRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundTotalBytes", String.valueOf(queueInfo.getInboundStatistics().getTotalBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundTotalMessages", String.valueOf(queueInfo.getInboundStatistics().getTotalMessages())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundByteRate", String.valueOf(queueInfo.getOutboundStatistics().getByteRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundMessageRate", String.valueOf(queueInfo.getOutboundStatistics().getMessageRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundTotalBytes", String.valueOf(queueInfo.getOutboundStatistics().getTotalBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundTotalMessages", String.valueOf(queueInfo.getOutboundStatistics().getTotalMessages())};
        this.addRow(arrobject);
        arrobject = new String[]{"ConsumerCount", String.valueOf(queueInfo.getConsumerCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"DeliveredMessageCount", String.valueOf(queueInfo.getDeliveredMessageCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"InTransitMessageCount", String.valueOf(queueInfo.getInTransitMessageCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"MaxRedelivery", String.valueOf(queueInfo.getMaxRedelivery())};
        this.addRow(arrobject);
        arrobject = new String[]{"isMaxRedeliveryInherited", String.valueOf(queueInfo.isMaxRedeliveryInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"Prefetch", String.valueOf(queueInfo.getPrefetch())};
        this.addRow(arrobject);
        arrobject = new String[]{"isPrefetchInherited", String.valueOf(queueInfo.isPrefetchInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"ReceiverCount", String.valueOf(queueInfo.getReceiverCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"isExclusive", String.valueOf(queueInfo.isExclusive())};
        this.addRow(arrobject);
        arrobject = new String[]{"isExclusiveInherited", String.valueOf(queueInfo.isExclusiveInherited())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"isRouted", String.valueOf(queueInfo.isRouted())};
            this.addRow(arrobject);
            arrobject = new String[]{"getRouteName", queueInfo.getRouteName()};
            this.addRow(arrobject);
        } catch (Throwable var5_6) {
            // empty catch block
        }
        arrobject = new String[]{"isRouteConnected", String.valueOf(queueInfo.isRouteConnected())};
        this.addRow(arrobject);
        arrobject = new String[]{"FlowControl", String.valueOf(queueInfo.getFlowControlMaxBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFlowControlInherited", String.valueOf(queueInfo.isFlowControlMaxBytesInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"MaxBytes", String.valueOf(queueInfo.getMaxBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"isMaxBytesInherited", String.valueOf(queueInfo.isMaxBytesInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"MsgTrace", String.valueOf(queueInfo.getMsgTrace())};
        this.addRow(arrobject);
        arrobject = new String[]{"isMsgTraceInherited", String.valueOf(queueInfo.isMsgTraceInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFailsafe", String.valueOf(queueInfo.isFailsafe())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFailsafeInherited", String.valueOf(queueInfo.isFailsafeInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isGlobal", String.valueOf(queueInfo.isGlobal())};
        this.addRow(arrobject);
        arrobject = new String[]{"isGlobalInherited", String.valueOf(queueInfo.isGlobalInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isStatic", String.valueOf(queueInfo.isStatic())};
        this.addRow(arrobject);
        arrobject = new String[]{"isTemporary", String.valueOf(queueInfo.isTemporary())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSecure", String.valueOf(queueInfo.isSecure())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSecureInherited", String.valueOf(queueInfo.isSecureInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderName", String.valueOf(queueInfo.isSenderName())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderNameInherited", String.valueOf(queueInfo.isSenderNameInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderNameEnforced", String.valueOf(queueInfo.isSenderNameEnforced())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderNameEnforcedInherited", String.valueOf(queueInfo.isSenderNameEnforcedInherited())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"ExpiryOverride", String.valueOf(queueInfo.getExpiryOverride())};
            this.addRow(arrobject);
            arrobject = new String[]{"isExpiryOverrideInherited", String.valueOf(queueInfo.isExpiryOverrideInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_7) {
            // empty catch block
        }
        try {
            arrobject = new String[]{"OverflowPolicy", String.valueOf(queueInfo.getOverflowPolicy())};
            this.addRow(arrobject);
            arrobject = new String[]{"isOverflowPolicyInherited", String.valueOf(queueInfo.isOverflowPolicyInherited())};
            this.addRow(arrobject);
            arrobject = new String[]{"MaxMsgs", String.valueOf(queueInfo.getMaxMsgs())};
            this.addRow(arrobject);
            arrobject = new String[]{"isMaxMsgsInherited", String.valueOf(queueInfo.isMaxMsgsInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_8) {
            // empty catch block
        }
        arrobject = new String[]{"JNDINames", StringUtilities.arrayToString(queueInfo.getJNDINames())};
        this.addRow(arrobject);
        arrobject = new String[]{"ImportTransports", StringUtilities.arrayToString(queueInfo.getImportTransports())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"isImportTransportsInherited", String.valueOf(queueInfo.isImportTransportsInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_9) {
            // empty catch block
        }
        arrobject = new String[]{"BridgeTargets", StringUtilities.arrayToString((Object[]) queueInfo.getBridgeTargets())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"isBridgeTargetsInherited", String.valueOf(queueInfo.isBridgeTargetsInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_10) {
            // empty catch block
        }

        try {
            arrobject = new String[]{"Store", queueInfo.getStore()};
            this.addRow(arrobject);
            arrobject = new String[]{"isStoreInherited", String.valueOf(queueInfo.isStoreInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_11) {
            // empty catch block
        }

        System.out.println("getRedeliveryDelay / isRedeliveryDelayInherited");

//        try {
//            arrobject = new String[]{"RedeliveryDelay", String.valueOf(queueInfo.getRedeliveryDelay())};
//            this.addRow(arrobject);
//            arrobject = new String[]{"isRedeliveryDelayInherited", String.valueOf(queueInfo.isRedeliveryDelayInherited())};
//            this.addRow(arrobject);
//        }
//        catch (Throwable var5_12) {
        // empty catch block
//        }
    }

    public void populateTopicInfo(TibjmsAdmin tibjmsAdmin, String string) {
        TopicInfo topicInfo;
        this.setRowCount(0);
        if (this.getColumnCount() != 2 || !this.getColumnName(0).equals("TopicProperty")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(2);
            this.setColumnIdentifiers(s_tprop_cols);
        }
        if (tibjmsAdmin == null) {
            return;
        }
        try {
            topicInfo = tibjmsAdmin.getTopic(string);
        } catch (TibjmsAdminException var4_4) {
            System.err.println("JMSException: " + var4_4.getMessage());
            return;
        }
        if (topicInfo == null) {
            return;
        }
        Object[] arrobject = new String[]{"Name", string};
        this.addRow(arrobject);
        arrobject = new String[]{"PendingMessageCount", String.valueOf(topicInfo.getPendingMessageCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"PendingMessageSize", StringUtilities.getHumanReadableSize(topicInfo.getPendingMessageSize())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundByteRate", String.valueOf(topicInfo.getInboundStatistics().getByteRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundMessageRate", String.valueOf(topicInfo.getInboundStatistics().getMessageRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundTotalBytes", String.valueOf(topicInfo.getInboundStatistics().getTotalBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundTotalMessages", String.valueOf(topicInfo.getInboundStatistics().getTotalMessages())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundByteRate", String.valueOf(topicInfo.getOutboundStatistics().getByteRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundMessageRate", String.valueOf(topicInfo.getOutboundStatistics().getMessageRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundTotalBytes", String.valueOf(topicInfo.getOutboundStatistics().getTotalBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundTotalMessages", String.valueOf(topicInfo.getOutboundStatistics().getTotalMessages())};
        this.addRow(arrobject);
        arrobject = new String[]{"ActiveDurableCount", String.valueOf(topicInfo.getActiveDurableCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"DurableCount", String.valueOf(topicInfo.getDurableCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"ConsumerCount", String.valueOf(topicInfo.getConsumerCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"SubscriberCount", String.valueOf(topicInfo.getSubscriberCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"FlowControl", String.valueOf(topicInfo.getFlowControlMaxBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFlowControlInherited", String.valueOf(topicInfo.isFlowControlMaxBytesInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"MaxBytes", String.valueOf(topicInfo.getMaxBytes())};
        this.addRow(arrobject);
        arrobject = new String[]{"isMaxBytesInherited", String.valueOf(topicInfo.isMaxBytesInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"MsgTrace", String.valueOf(topicInfo.getMsgTrace())};
        this.addRow(arrobject);
        arrobject = new String[]{"isMsgTraceInherited", String.valueOf(topicInfo.isMsgTraceInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFailsafe", String.valueOf(topicInfo.isFailsafe())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFailsafeInherited", String.valueOf(topicInfo.isFailsafeInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isGlobal", String.valueOf(topicInfo.isGlobal())};
        this.addRow(arrobject);
        arrobject = new String[]{"isGlobalInherited", String.valueOf(topicInfo.isGlobalInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isStatic", String.valueOf(topicInfo.isStatic())};
        this.addRow(arrobject);
        arrobject = new String[]{"isTemporary", String.valueOf(topicInfo.isTemporary())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSecure", String.valueOf(topicInfo.isSecure())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSecureInherited", String.valueOf(topicInfo.isSecureInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderName", String.valueOf(topicInfo.isSenderName())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderNameInherited", String.valueOf(topicInfo.isSenderNameInherited())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderNameEnforced", String.valueOf(topicInfo.isSenderNameEnforced())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSenderNameEnforcedInherited", String.valueOf(topicInfo.isSenderNameEnforcedInherited())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"ExpiryOverride", String.valueOf(topicInfo.getExpiryOverride())};
            this.addRow(arrobject);
            arrobject = new String[]{"isExpiryOverrideInherited", String.valueOf(topicInfo.isExpiryOverrideInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_6) {
            // empty catch block
        }
        try {
            arrobject = new String[]{"OverflowPolicy", String.valueOf(topicInfo.getOverflowPolicy())};
            this.addRow(arrobject);
            arrobject = new String[]{"isOverflowPolicyInherited", String.valueOf(topicInfo.isOverflowPolicyInherited())};
            this.addRow(arrobject);
            arrobject = new String[]{"MaxMsgs", String.valueOf(topicInfo.getMaxMsgs())};
            this.addRow(arrobject);
            arrobject = new String[]{"isMaxMsgsInherited", String.valueOf(topicInfo.isMaxMsgsInherited())};
            this.addRow(arrobject);
            arrobject = new String[]{"Prefetch", String.valueOf(topicInfo.getPrefetch())};
            this.addRow(arrobject);
            arrobject = new String[]{"isPrefetchInherited", String.valueOf(topicInfo.isPrefetchInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_7) {
            // empty catch block
        }
        arrobject = new String[]{"JNDINames", StringUtilities.arrayToString(topicInfo.getJNDINames())};
        this.addRow(arrobject);
        arrobject = new String[]{"ImportTransports", StringUtilities.arrayToString(topicInfo.getImportTransports())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"isImportTransportsInherited", String.valueOf(topicInfo.isImportTransportsInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_8) {
            // empty catch block
        }
        arrobject = new String[]{"ExportTransports", StringUtilities.arrayToString(topicInfo.getExportTransports())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"isExportTransportsInherited", String.valueOf(topicInfo.isExportTransportsInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_9) {
            // empty catch block
        }
        arrobject = new String[]{"BridgeTargets", StringUtilities.arrayToString((Object[]) topicInfo.getBridgeTargets())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"isBridgeTargetsInherited", String.valueOf(topicInfo.isBridgeTargetsInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_10) {
            // empty catch block
        }
        try {
            arrobject = new String[]{"Channel", topicInfo.getChannel()};
            this.addRow(arrobject);
            arrobject = new String[]{"isChannelInherited", String.valueOf(topicInfo.isChannelInherited())};
            this.addRow(arrobject);
            arrobject = new String[]{"isMulticastEnabled", String.valueOf(topicInfo.isMulticastEnabled())};
            this.addRow(arrobject);
            arrobject = new String[]{"Store", topicInfo.getStore()};
            this.addRow(arrobject);
            arrobject = new String[]{"isStoreInherited", String.valueOf(topicInfo.isStoreInherited())};
            this.addRow(arrobject);
        } catch (Throwable var5_11) {
            // empty catch block
        }
    }

    public void populateProducerInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 11 || !this.getColumnName(5).equals("SessionID")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_prod_cols);
            this.setupColumnWidths();
        }
        try {
            ProducerInfo[] arrproducerInfo;
            try {
                arrproducerInfo = gemsConnectionNode.m_adminConn.getProducersStatistics();
            } catch (Throwable var3_4) {
                arrproducerInfo = gemsConnectionNode.m_adminConn.getProducersStatistics();
            }
            Date date = new Date();
            for (int i = 0; arrproducerInfo != null && i < arrproducerInfo.length; ++i) {
                String string = null;
                if (Gems.getGems().getShowClientId()) {
                    string = gemsConnectionNode.getClientID(arrproducerInfo[i].getConnectionID());
                }
                if (string == null) {
                    string = new String();
                }
                date.setTime(arrproducerInfo[i].getCreateTime());
                String string2 = "Generic";
                if (arrproducerInfo[i].getDestinationType() == 1) {
                    string2 = "Queue";
                } else if (arrproducerInfo[i].getDestinationType() == 2) {
                    string2 = "Topic";
                }
                Object[] arrobject = new Object[]{new Long(arrproducerInfo[i].getID()), date.toString(), arrproducerInfo[i].getDestinationName(), string2, new Long(arrproducerInfo[i].getConnectionID()), new Long(arrproducerInfo[i].getSessionID()), arrproducerInfo[i].getUsername(), new Long(arrproducerInfo[i].getStatistics().getByteRate()), new Long(arrproducerInfo[i].getStatistics().getMessageRate()), new Long(arrproducerInfo[i].getStatistics().getTotalBytes()), new Long(arrproducerInfo[i].getStatistics().getTotalMessages())};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateACLInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 6 || !this.getColumnName(2).equals("PrincipalName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_acl_cols);
            this.setupColumnWidths();
        }
        try {
            ACLEntry[] arraCLEntry = gemsConnectionNode.m_adminConn.getACLEntries();
            Date date = new Date();
            for (int i = 0; arraCLEntry != null && i < arraCLEntry.length; ++i) {
                Object[] arrobject = new Object[6];
                arrobject[0] = arraCLEntry[i].getDestination().getName();
                arrobject[1] = arraCLEntry[i].getDestination() instanceof QueueInfo ? new String("Queue") : new String("Topic");
                arrobject[2] = arraCLEntry[i].getPrincipal().getName();
                arrobject[3] = arraCLEntry[i].getPrincipal() instanceof UserInfo ? new String("User") : new String("Group");
                arrobject[4] = String.valueOf(arraCLEntry[i].getPrincipal().isExternal());
                arrobject[5] = arraCLEntry[i].getPermissions().toString();
                Object[] arrobject2 = arrobject;
                this.addRow(arrobject2);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateAdminACLInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 4 || !this.getColumnName(0).equals("PrincipalName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_adminacl_cols);
            this.setupColumnWidths();
        }
        try {
            Object[] arrobject = gemsConnectionNode.m_adminConn.getPermissions();
            Date date = new Date();
            for (int i = 0; arrobject != null && i < arrobject.length; ++i) {
                if (!(arrobject[i] instanceof AdminACLEntry)) continue;
                Object[] arrobject2 = new Object[4];
                arrobject2[0] = ((AdminACLEntry) arrobject[i]).getPrincipal().getName();
                arrobject2[1] = ((AdminACLEntry) arrobject[i]).getPrincipal() instanceof UserInfo ? new String("User") : new String("Group");
                arrobject2[2] = String.valueOf(((AdminACLEntry) arrobject[i]).getPrincipal().isExternal());
                arrobject2[3] = ((AdminACLEntry) arrobject[i]).getPermissions().toString();
                Object[] arrobject3 = arrobject2;
                this.addRow(arrobject3);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateSSInfo(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode) {
        if (gemsConnectionNode == null || gemsSSNode == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 2 || !this.getColumnName(0).equals("Property")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(2);
            this.setColumnIdentifiers(s_prop_cols);
        }
        MessageFormat messageFormat = new MessageFormat("SXS0800I -  {0} - {1}");
        MessageFormat messageFormat2 = new MessageFormat("SXS0800I -  UoW - {0} - {1}");
        MessageFormat messageFormat3 = new MessageFormat("SXS0850I -  IId:{0} UoW Que statistics request");
        MessageFormat messageFormat4 = new MessageFormat("SXS0851I -  {0} - {1}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,ALL").split("\n");
        for (int i = 0; i < arrstring.length; ++i) {
            Object[] arrobject;
            int n;
            if (arrstring[i].startsWith("SXS0800I") && (n = arrstring[i].indexOf("Statistics request")) == -1) {
                try {
                    Object[] arrobject2 = messageFormat.parse(arrstring[i]);
                    if (arrobject2[0].toString().startsWith("UoW Stress Status")) {
                        arrobject = new Object[]{arrobject2[0].toString(), new SSCellValue(Long.valueOf(arrobject2[1].toString()), "Error", 1, 100)};
                    } else if (arrobject2[0].toString().compareTo("UoW") == 0) {
                        arrobject2 = messageFormat2.parse(arrstring[i]);
                        arrobject = new String[]{"UoW " + arrobject2[0].toString(), arrobject2[1].toString()};
                    } else {
                        arrobject = new String[]{arrobject2[0].toString(), arrobject2[1].toString()};
                    }
                    this.addRow(arrobject);
                } catch (ParseException var11_16) {
                    System.err.println("Message Format Error(): " + var11_16.getMessage() + " " + arrstring[i]);
                }
            }
            if (arrstring[i].startsWith("SXS0850I")) {
                try {
                    Object[] arrobject3 = messageFormat3.parse(arrstring[i]);
                    arrobject = new Object[]{new SSCellValue("Interface", "Head"), arrobject3[0].toString().trim()};
                    this.addRow(arrobject);
                } catch (ParseException var10_12) {
                    System.err.println("Message Format Error(): " + var10_12.getMessage() + " " + arrstring[i]);
                }
            }
            if (!arrstring[i].startsWith("SXS0851I")) continue;
            try {
                Object[] arrobject4 = messageFormat4.parse(arrstring[i]);
                arrobject = new String[]{arrobject4[0].toString(), arrobject4[1].toString()};
                this.addRow(arrobject);
                continue;
            } catch (ParseException var10_14) {
                System.err.println("Message Format Error(): " + var10_14.getMessage() + " " + arrstring[i]);
            }
        }
    }

    public void populateSSActive(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode, String string, String string2) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 3 || !this.getColumnName(0).equals("Identifier")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_ssactive_cols);
            this.setupColumnWidths();
        }
        MessageFormat messageFormat = new MessageFormat("SXT5587I Since Date:{1} Time:{2} RID:{3}");
        MessageFormat messageFormat2 = new MessageFormat("SXT5587I IId:{0} Since Date:{1} Time:{2} RID:{3}");
        MessageFormat messageFormat3 = new MessageFormat("SXT5589I Since Date:{1} Time:{2} TID:{3}");
        MessageFormat messageFormat4 = new MessageFormat("SXT5589I IId:{0} Since Date:{1} Time:{2} TID:{3}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,ACT," + string + ",INTF=" + string2).split("\n");
        for (int i = 1; i < arrstring.length; ++i) {
            try {
                Object[] arrobject;
                Object[] arrobject2;
                if (arrstring[i].startsWith("SXT5587I Since")) {
                    arrobject = messageFormat.parse(arrstring[i]);
                    arrobject2 = new String[]{arrobject[3].toString(), arrobject[1].toString(), arrobject[2].toString().substring(0, 8)};
                    this.addRow(arrobject2);
                }
                if (arrstring[i].startsWith("SXT5587I IId")) {
                    arrobject = messageFormat2.parse(arrstring[i]);
                    arrobject2 = new String[]{arrobject[3].toString(), arrobject[1].toString(), arrobject[2].toString().substring(0, 8)};
                    this.addRow(arrobject2);
                }
                if (arrstring[i].startsWith("SXT5589I Since")) {
                    arrobject = messageFormat3.parse(arrstring[i]);
                    arrobject2 = new String[]{arrobject[3].toString(), arrobject[1].toString(), arrobject[2].toString().substring(0, 8)};
                    this.addRow(arrobject2);
                }
                if (!arrstring[i].startsWith("SXT5589I IId")) continue;
                arrobject = messageFormat4.parse(arrstring[i]);
                arrobject2 = new String[]{arrobject[3].toString(), arrobject[1].toString(), arrobject[2].toString().substring(0, 8)};
                this.addRow(arrobject2);
                continue;
            } catch (ParseException var12_13) {
                System.err.println("Message Format Error(): " + var12_13.getMessage() + " " + arrstring[i]);
            }
        }
    }

    public void populateSSListeners(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode, String string) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 1 || !this.getColumnName(0).equals("Destination")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_ssactiveL_cols);
            this.setupColumnWidths();
        }
        MessageFormat messageFormat = new MessageFormat("SXT5585I Dest:{0}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,ACT,LISTENERS,INTF=" + string).split("\n");
        for (int i = 1; i < arrstring.length; ++i) {
            if (!arrstring[i].startsWith("SXT5585I")) continue;
            try {
                Object[] arrobject = messageFormat.parse(arrstring[i]);
                Object[] arrobject2 = new String[]{arrobject[0].toString()};
                this.addRow(arrobject2);
                continue;
            } catch (ParseException var8_9) {
                System.err.println("Message Format Error(): " + var8_9.getMessage() + " " + arrstring[i]);
            }
        }
    }

    public void populateSSInActive(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode, String string) {
        this.setRowCount(0);
        if (this.getColumnCount() != 5 || !this.getColumnName(0).equals("Identifier")) {
            if (gemsConnectionNode == null || gemsSSNode == null) {
                return;
            }
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_ssinactive_cols);
            this.setupColumnWidths();
        }
        MessageFormat messageFormat = new MessageFormat("SXT5594I Disabled Date:{1} Time:{2} {3}:{4}, Rsn: {5}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,DIS,INTF=" + string).split("\n");
        String string2 = "";
        String string3 = "";
        String string4 = "";
        for (int i = 1; i < arrstring.length; ++i) {
            if (!arrstring[i].startsWith("SXT5594I")) continue;
            try {
                Object[] arrobject = messageFormat.parse(arrstring[i]);
                string2 = arrobject[3].toString().startsWith("RID") ? "Recipe" : "Trigger";
                string3 = arrobject[1].toString().startsWith("<None>") ? "" : arrobject[1].toString();
                string4 = arrobject[2].toString().equals("") ? "" : arrobject[2].toString().substring(0, 8);
                Object[] arrobject2 = new String[]{arrobject[4].toString(), string2, arrobject[5].toString(), string3, string4};
                this.addRow(arrobject2);
                continue;
            } catch (ParseException var11_12) {
                System.err.println("Error(): " + var11_12.getMessage() + " " + arrstring[i]);
            }
        }
    }

    public void populateSSCounters(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode) {
        if (gemsConnectionNode == null || gemsSSNode == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 2 || !this.getColumnName(0).equals("Property")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(2);
            this.setColumnIdentifiers(s_prop_cols);
        }
        MessageFormat messageFormat = new MessageFormat("SXS0960I {3} IId:{0} Type:{1} Vers:{2}");
        MessageFormat messageFormat2 = new MessageFormat("SXS0965I - {3}:{0} - Used:{1} Errors:{2}");
        MessageFormat messageFormat3 = null;
        MessageFormat messageFormat4 = new MessageFormat("SXT5840I {3}:{0} - Used:{1} Errors:{2}");
        messageFormat3 = gemsSSNode.m_ver != "2.4" ? new MessageFormat("SXS0961I {0}:{1}") : new MessageFormat("SXS0961I - {0}:{1}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,COUNTERS").split("\n");
        for (int i = 0; i < arrstring.length; ++i) {
            Object[] arrobject;
            Object[] arrobject2;
            if (arrstring[i].startsWith("SXS0960I")) {
                try {
                    arrobject2 = messageFormat.parse(arrstring[i]);
                    arrobject = new Object[]{new SSCellValue("Interface Name", "Head"), arrobject2[0].toString().trim()};
                    this.addRow(arrobject);
                    arrobject = new String[]{"Interface Type", arrobject2[1].toString()};
                    this.addRow(arrobject);
                    arrobject = new String[]{"Interface Version", arrobject2[2].toString()};
                    this.addRow(arrobject);
                } catch (ParseException var10_11) {
                    System.err.println("Message Format Error(): " + var10_11.getMessage() + " " + arrstring[i]);
                }
            }
            if (arrstring[i].startsWith("SXS0965I")) {
                try {
                    arrobject2 = messageFormat2.parse(arrstring[i]);
                    arrobject = arrobject2[3].toString().equals("RID") ? new Object[]{new SSCellValue("Recipe Name", "Head"), arrobject2[0].toString()} : new Object[]{new SSCellValue("Trigger Name", "Head"), arrobject2[0].toString()};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"Used", new SSCellValue(Long.valueOf(arrobject2[1].toString()), "Large", 1000, 10000)};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"Errors", new SSCellValue(Long.valueOf(arrobject2[2].toString()), "Error", 1, 100)};
                    this.addRow(arrobject);
                } catch (ParseException var10_12) {
                    System.err.println("Message Format Error(): " + var10_12.getMessage() + " " + arrstring[i]);
                }
            }
            if (arrstring[i].startsWith("SXT5840I")) {
                try {
                    arrobject2 = messageFormat4.parse(arrstring[i]);
                    arrobject = arrobject2[3].toString().equals("RID") ? new Object[]{new SSCellValue("Recipe Name", "Head"), arrobject2[0].toString()} : new Object[]{new SSCellValue("Trigger Name", "Head"), arrobject2[0].toString()};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"Used", new SSCellValue(Long.valueOf(arrobject2[1].toString()), "Large", 1000, 10000)};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"Errors", new SSCellValue(Long.valueOf(arrobject2[2].toString()), "Error", 1, 100)};
                    this.addRow(arrobject);
                } catch (ParseException var10_13) {
                    System.err.println("Message Format Error(): " + var10_13.getMessage() + " " + arrstring[i]);
                }
            }
            if (!arrstring[i].startsWith("SXS0961I")) continue;
            try {
                arrobject2 = messageFormat3.parse(arrstring[i]);
                arrobject = new String[]{arrobject2[0].toString(), arrobject2[1].toString()};
                this.addRow(arrobject);
                continue;
            } catch (ParseException var10_14) {
                System.err.println("Error: " + var10_14.getMessage());
            }
        }
    }

    public void populateSSInterface(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode) {
        String string;
        int n;
        if (gemsConnectionNode == null || gemsSSNode == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 6 || !this.getColumnName(0).equals("Interface ID")) {
            if (gemsConnectionNode == null || gemsSSNode == null) {
                return;
            }
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_ssinterface_cols);
            this.setupColumnWidths();
        }
        MessageFormat messageFormat = null;
        messageFormat = gemsSSNode.m_ver.equals("2.4") || gemsSSNode.m_ver.equals("2.3") || gemsSSNode.m_ver.equals("2.2") || gemsSSNode.m_ver.equals("2.1") || gemsSSNode.m_ver.equals("2.0") ? new MessageFormat("SXS0951I - IId:{0} Type:{1} TCB:{2} Vers:{3} Start Date:{4}  Time:{5} All Workers Busy:{6} Times") : new MessageFormat("SXS0951I - IId:{0} Type:{1} TCB:{2} Vers:{3} All Workers Busy:{6} Times");
        MessageFormat messageFormat2 = new MessageFormat("SXS0990I - IId:{0} Type:{1} Applid:{2} Connection:{3}");
        MessageFormat messageFormat3 = new MessageFormat("SXS0951I - IId:{0} Type:{1} TCB:{2} Vers:{3}   Start Date:{4}  Time:{5}    All Workers Busy:{6} Times");
        MessageFormat messageFormat4 = new MessageFormat("SXS0951I - IId:{0} Type:{1} TCB:{2} Vers:{3} All Workers Busy:{6} Times    Connection:{7} (UP/DOWN)");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,INTF").split("\n");
        Vector<String> vector = new Vector<String>();
        int n2 = -1;
        for (n = 1; n < arrstring.length; ++n) {
            if (arrstring[n].startsWith("SXA1008I") || arrstring[n].startsWith("SXS0950I")) continue;
            if (arrstring[n].startsWith("SXS0951I")) {
                ++n2;
                vector.add(arrstring[n]);
                continue;
            }
            if (arrstring[n].startsWith("SXS0990I")) {
                string = vector.get(n2).toString() + "\n" + arrstring[n];
                vector.setElementAt(string, n2);
                continue;
            }
            string = vector.get(n2).toString() + arrstring[n];
            vector.setElementAt(string, n2);
        }
        for (n = 0; n < vector.size(); ++n) {
            string = vector.get(n).toString();
            if (!string.startsWith("SXS0951I")) continue;
            String[] arrstring2 = string.split("\n");
            try {
                Object[] arrobject;
                Object[] arrobject2;
                if (arrstring2.length > 1) {
                    arrobject2 = messageFormat.parse(arrstring2[0]);
                    Object[] arrobject3 = messageFormat2.parse(arrstring2[1]);
                    arrobject = new Object[]{arrobject2[0].toString(), arrobject2[1].toString(), arrobject2[4].toString(), arrobject2[5].toString(), new SSCellValue(Long.valueOf(arrobject2[6].toString()), "Error", 10, 100), arrobject3[2].toString(), new SSCellValue(arrobject3[3].toString(), "Status", "DOWN")};
                } else {
                    arrobject2 = null;
                    if (string.contains("Type:Master")) {
                        arrobject2 = messageFormat3.parse(arrstring2[0]);
                        arrobject = new Object[]{arrobject2[0].toString(), arrobject2[1].toString(), arrobject2[4].toString(), arrobject2[5].toString(), new SSCellValue(Long.valueOf(arrobject2[6].toString()), "Error", 10, 100)};
                    } else if (string.contains("Connection:")) {
                        arrobject2 = messageFormat4.parse(arrstring2[0]);
                        arrobject = new Object[]{arrobject2[0].toString(), arrobject2[1].toString(), "", "", new SSCellValue(Long.valueOf(arrobject2[6].toString()), "Error", 10, 100), "", new SSCellValue(arrobject2[7].toString(), "Status", "DOWN")};
                    } else {
                        arrobject2 = messageFormat.parse(arrstring2[0]);
                        arrobject = gemsSSNode.m_ver.equals("2.4") ? new Object[]{arrobject2[0].toString(), arrobject2[1].toString(), arrobject2[4].toString(), arrobject2[5].toString(), new SSCellValue(Long.valueOf(arrobject2[6].toString()), "Error", 10, 100)} : new Object[]{arrobject2[0].toString(), arrobject2[1].toString(), "", "", new SSCellValue(Long.valueOf(arrobject2[6].toString()), "Error", 10, 100)};
                    }
                }
                this.addRow(arrobject);
                continue;
            } catch (ParseException var14_15) {
                System.err.println("Message Format Error(): " + var14_15.getMessage() + " " + string);
            }
        }
    }

    public void populateSSTransport(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode) {
        if (gemsConnectionNode == null || gemsSSNode == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 2 || !this.getColumnName(0).equals("Property")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(2);
            this.setColumnIdentifiers(s_prop_cols);
        }
        MessageFormat messageFormat = new MessageFormat("SXT5670I Transports active {1}, in-error {2}, unused {3}, disabled {4}");
        MessageFormat messageFormat2 = new MessageFormat("SXT5670I IId:{5} Transports active {1}, in-error {2}, unused {3}, disabled {4}");
        MessageFormat messageFormat3 = new MessageFormat("SXT5669I Transport {1} used {2}, state={3}, process-err={4}");
        MessageFormat messageFormat4 = new MessageFormat("SXT5669I IId:{5} Transport {1} used {2}, state={3}, process-err={4}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,TPORT").split("\n");
        for (int i = 0; i < arrstring.length; ++i) {
            Object[] arrobject;
            Object[] arrobject2;
            if (arrstring[i].startsWith("SXT5670I IId:")) {
                try {
                    arrobject2 = messageFormat2.parse(arrstring[i]);
                    arrobject = new Object[]{new SSCellValue("Summary", "Head"), "Interface: " + arrobject2[5].toString()};
                    this.addRow(arrobject);
                    arrobject = new String[]{"Active", arrobject2[1].toString()};
                    this.addRow(arrobject);
                    arrobject = new String[]{"Unused", arrobject2[3].toString()};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"Disabled", new SSCellValue(Long.valueOf(arrobject2[4].toString()), "Error", 1, 2)};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"In Error", new SSCellValue(Long.valueOf(arrobject2[2].toString()), "Error", 1, 1)};
                    this.addRow(arrobject);
                } catch (ParseException var10_11) {
                    System.err.println("Message Format Error(): " + var10_11.getMessage() + " " + arrstring[i]);
                }
            }
            if (arrstring[i].startsWith("SXT5670I Trans")) {
                try {
                    arrobject2 = messageFormat.parse(arrstring[i]);
                    arrobject = new Object[]{new SSCellValue("Summary", "Head"), ""};
                    this.addRow(arrobject);
                    arrobject = new String[]{"Active", arrobject2[1].toString()};
                    this.addRow(arrobject);
                    arrobject = new String[]{"Unused", arrobject2[3].toString()};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"Disabled", new SSCellValue(Long.valueOf(arrobject2[4].toString()), "Error", 1, 2)};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"In Error", new SSCellValue(Long.valueOf(arrobject2[2].toString()), "Error", 1, 1)};
                    this.addRow(arrobject);
                } catch (ParseException var10_12) {
                    System.err.println("Message Format Error(): " + var10_12.getMessage() + " " + arrstring[i]);
                }
            }
            if (arrstring[i].startsWith("SXT5669I IId:")) {
                try {
                    arrobject2 = messageFormat4.parse(arrstring[i]);
                    arrobject = new Object[]{new SSCellValue("Transport", "Head"), arrobject2[1].toString().trim()};
                    this.addRow(arrobject);
                    arrobject = new String[]{"Used", arrobject2[2].toString()};
                    this.addRow(arrobject);
                    arrobject = new String[]{"State", arrobject2[3].toString()};
                    this.addRow(arrobject);
                    arrobject = new Object[]{"Error", arrobject2[4].toString()};
                    this.addRow(arrobject);
                } catch (ParseException var10_13) {
                    System.err.println("Message Format Error(): " + var10_13.getMessage() + " " + arrstring[i]);
                }
            }
            if (!arrstring[i].startsWith("SXT5669I Tran")) continue;
            try {
                arrobject2 = messageFormat3.parse(arrstring[i]);
                arrobject = new Object[]{new SSCellValue("Transport", "Head"), arrobject2[1].toString().trim()};
                this.addRow(arrobject);
                arrobject = new String[]{"Used", arrobject2[2].toString()};
                this.addRow(arrobject);
                arrobject = new String[]{"State", arrobject2[3].toString()};
                this.addRow(arrobject);
                arrobject = new Object[]{"Error", arrobject2[4].toString()};
                this.addRow(arrobject);
                continue;
            } catch (ParseException var10_14) {
                System.err.println("Message Format Error(): " + var10_14.getMessage() + " " + arrstring[i]);
            }
        }
    }

    public void populateSSIMSStats(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode) {
        if (gemsConnectionNode == null || gemsSSNode == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 6 || !this.getColumnName(0).equals("Interface ID")) {
            if (gemsConnectionNode == null || gemsSSNode == null) {
                return;
            }
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_ssimsstats_cols);
            this.setupColumnWidths();
        }
        MessageFormat messageFormat = new MessageFormat("SXI4967I Count {0} - {1}");
        MessageFormat messageFormat2 = new MessageFormat("SXG2077I Operational Request Feedback for IId:{0}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,IMS,STATS").split("\n");
        for (int i = 0; i < arrstring.length; ++i) {
            Object[] arrobject;
            Object[] arrobject2;
            if (arrstring[i].startsWith("SXG2077I")) {
                try {
                    arrobject = messageFormat2.parse(arrstring[i]);
                    arrobject2 = new Object[]{new SSCellValue("Interface Name", "Head"), arrobject[0].toString().trim()};
                    this.addRow(arrobject2);
                } catch (ParseException var8_9) {
                    System.err.println("Message Format Error(): " + var8_9.getMessage() + " " + arrstring[i]);
                }
            }
            if (!arrstring[i].startsWith("SXI4967I")) continue;
            try {
                arrobject = messageFormat.parse(arrstring[i]);
                arrobject2 = new String[]{arrobject[1].toString(), arrobject[0].toString().trim()};
                this.addRow(arrobject2);
                continue;
            } catch (ParseException var8_10) {
                System.err.println("Message Format Error(): " + var8_10.getMessage() + " " + arrstring[i]);
            }
        }
    }

    public void populateSSIMSBuffers(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode) {
        if (gemsConnectionNode == null || gemsSSNode == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 6 || !this.getColumnName(0).equals("Interface ID")) {
            if (gemsConnectionNode == null || gemsSSNode == null) {
                return;
            }
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_ssimsbuffs_cols);
            this.setupColumnWidths();
        }
        MessageFormat messageFormat = new MessageFormat("SXI4969I Have {0} active buffers - {1} SRB, {2} GRP {3} TRN");
        MessageFormat messageFormat2 = new MessageFormat("SXG2077I Operational Request Feedback for IId:{0}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,IMS,BUFFERS").split("\n");
        for (int i = 0; i < arrstring.length; ++i) {
            Object[] arrobject;
            Object[] arrobject2;
            if (arrstring[i].startsWith("SXG2077I")) {
                try {
                    arrobject = messageFormat2.parse(arrstring[i]);
                    arrobject2 = new Object[]{new SSCellValue("Interface Name", "Head"), arrobject[0].toString().trim()};
                    this.addRow(arrobject2);
                } catch (ParseException var8_9) {
                    System.err.println("Message Format Error(): " + var8_9.getMessage() + " " + arrstring[i]);
                }
            }
            if (!arrstring[i].startsWith("SXI4969I")) continue;
            try {
                arrobject = messageFormat.parse(arrstring[i]);
                arrobject2 = new String[]{"Total Active", arrobject[0].toString().trim()};
                this.addRow(arrobject2);
                arrobject2 = new String[]{"SRB", arrobject[1].toString().trim()};
                this.addRow(arrobject2);
                arrobject2 = new String[]{"GRP", arrobject[2].toString().trim()};
                this.addRow(arrobject2);
                arrobject2 = new String[]{"TRN", arrobject[3].toString().trim()};
                this.addRow(arrobject2);
                continue;
            } catch (ParseException var8_10) {
                System.err.println("Message Format Error(): " + var8_10.getMessage() + " " + arrstring[i]);
            }
        }
    }

    public void populateSSIMSGen(GemsConnectionNode gemsConnectionNode, GemsSSNode gemsSSNode) {
        Object[] arrobject;
        Object[] arrobject2;
        int n;
        if (gemsConnectionNode == null || gemsSSNode == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 6 || !this.getColumnName(0).equals("Interface ID")) {
            if (gemsConnectionNode == null || gemsSSNode == null) {
                return;
            }
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_ssimsgen_cols);
            this.setupColumnWidths();
        }
        MessageFormat messageFormat = new MessageFormat("SXG2077I Operational Request Feedback for IId:{0}");
        MessageFormat messageFormat2 = new MessageFormat("SXI4965I Group:{0}, SS:{1}  , IMS:{2}");
        MessageFormat messageFormat3 = new MessageFormat("SXI4964I Connecton status: {0}");
        String[] arrstring = gemsSSNode.RunCommand("SHOW,IMS,CONNAME").split("\n");
        String[] arrstring2 = gemsSSNode.RunCommand("SHOW,IMS,CONNECT").split("\n");
        for (n = 0; n < arrstring.length; ++n) {
            if (arrstring[n].startsWith("SXG2077I")) {
                try {
                    arrobject2 = messageFormat.parse(arrstring[n]);
                    arrobject = new Object[]{new SSCellValue("Interface Name", "Head"), arrobject2[0].toString().trim()};
                    this.addRow(arrobject);
                } catch (ParseException var10_11) {
                    System.err.println("Message Format Error(): " + var10_11.getMessage() + " " + arrstring[n]);
                }
            }
            if (!arrstring[n].startsWith("SXI4965I")) continue;
            try {
                arrobject2 = messageFormat2.parse(arrstring[n]);
                arrobject = new String[]{"Group Name", arrobject2[0].toString().trim()};
                this.addRow(arrobject);
                arrobject = new String[]{"Member Name", arrobject2[1].toString().trim()};
                this.addRow(arrobject);
                arrobject = new String[]{"IMS Name", arrobject2[2].toString().trim()};
                this.addRow(arrobject);
                continue;
            } catch (ParseException var10_12) {
                System.err.println("Message Format Error(): " + var10_12.getMessage() + " " + arrstring[n]);
            }
        }
        for (n = 0; n < arrstring2.length; ++n) {
            if (arrstring[n].startsWith("SXG2077I")) {
                try {
                    arrobject2 = messageFormat.parse(arrstring2[n]);
                    arrobject = new Object[]{new SSCellValue("Interface Name", "Head"), arrobject2[0].toString().trim()};
                    this.addRow(arrobject);
                } catch (ParseException var10_13) {
                    System.err.println("Message Format Error(): " + var10_13.getMessage() + " " + arrstring[n]);
                }
            }
            if (!arrstring2[n].startsWith("SXI4964I")) continue;
            try {
                arrobject2 = messageFormat3.parse(arrstring2[n]);
                arrobject = new String[]{"Connection Status", arrobject2[0].toString().trim()};
                this.addRow(arrobject);
                continue;
            } catch (ParseException var10_14) {
                System.err.println("Message Format Error(): " + var10_14.getMessage() + " " + arrstring[n]);
            }
        }
    }

    public void populateUserInfo(TibjmsAdmin tibjmsAdmin) {
        if (tibjmsAdmin == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 3 || !this.getColumnName(0).equals("UserName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(2);
            this.setColumnIdentifiers(s_user_cols);
        }
        try {
            UserInfo[] arruserInfo = tibjmsAdmin.getUsers();
            for (int i = 0; arruserInfo != null && i < arruserInfo.length; ++i) {
                Object[] arrobject = new Object[]{String.valueOf(arruserInfo[i].getName()), arruserInfo[i].getDescription(), String.valueOf(arruserInfo[i].isExternal())};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateTransactionInfo(TibjmsAdmin tibjmsAdmin) {
        if (tibjmsAdmin == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 4 || !this.getColumnName(1).equals("GlobalTransactionId")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_transact_cols);
            this.setupColumnWidths();
        }
        try {
            TransactionInfo[] arrtransactionInfo = tibjmsAdmin.getTransactions();
            for (int i = 0; arrtransactionInfo != null && i < arrtransactionInfo.length; ++i) {
                StringBuffer stringBuffer = new StringBuffer();
                StringBuffer stringBuffer2 = new StringBuffer();
                byte[] arrby = arrtransactionInfo[i].getGlobalTransactionId();
                for (int j = 0; j < arrby.length; ++j) {
                    if (arrby[j] >= 32 && arrby[j] <= 126 && arrby[j] != 37) {
                        stringBuffer.append(arrby[j]);
                        continue;
                    }
                    stringBuffer.append("%" + Byte.toString(arrby[j]));
                }
                byte[] arrby2 = arrtransactionInfo[i].getBranchQualifier();
                for (int k = 0; k < arrby2.length; ++k) {
                    if (arrby2[k] >= 32 && arrby2[k] <= 126 && arrby2[k] != 37) {
                        stringBuffer2.append(arrby2[k]);
                        continue;
                    }
                    stringBuffer2.append("%" + Byte.toString(arrby2[k]));
                }
                Object[] arrobject = new Object[]{this.TxStateToString(arrtransactionInfo[i].getState()), stringBuffer.toString(), String.valueOf(arrtransactionInfo[i].getFormatId()), stringBuffer2.toString()};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public String TxStateToString(char c) {
        if (c == 'A') {
            return new String("Active");
        }
        if (c == 'E') {
            return new String("EndSuccess");
        }
        if (c == 'P') {
            return new String("Prepared");
        }
        if (c == 'R') {
            return new String("RollbackOnly");
        }
        if (c == 'S') {
            return new String("Suspended");
        }
        return new String("Unknown");
    }

    public void populateTransportInfo(TibjmsAdmin tibjmsAdmin) {
        if (tibjmsAdmin == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 7 || !this.getColumnName(0).equals("TransportName")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_transprt_cols);
            this.setupColumnWidths();
        }
        try {
            TransportInfo[] arrtransportInfo = tibjmsAdmin.getTransports();
            for (int i = 0; arrtransportInfo != null && i < arrtransportInfo.length; ++i) {
                int n = arrtransportInfo[i].getType();
                int n2 = arrtransportInfo[i].getTopicImportDeliveryMode();
                int n3 = arrtransportInfo[i].getQueueImportDeliveryMode();
                String string = new String();
                if (arrtransportInfo[i] instanceof RVTransportInfo) {
                    RVTransportInfo rVTransportInfo = (RVTransportInfo) arrtransportInfo[i];
                    string = string + "Service='" + rVTransportInfo.getService() + "', ";
                    string = string + "Network='" + rVTransportInfo.getNetwork() + "', ";
                    string = string + "Daemon='" + rVTransportInfo.getDaemon() + "', ";
                    try {
                        string = string + rVTransportInfo.getPolicy().toString();
                    } catch (Throwable var10_12) {
                    }
                } else if (arrtransportInfo[i] instanceof RVCMTransportInfo) {
                    RVCMTransportInfo rVTransportInfo = (RVCMTransportInfo) arrtransportInfo[i];
                    string = string + "RV Tport='" + rVTransportInfo.getRVTransportName() + "', ";
                    string = string + "CM Name='" + rVTransportInfo.getCMName() + "', ";
                    string = string + "CM Ledger='" + rVTransportInfo.getLedger() + "', ";
                    string = string + "Sync Ledger=" + rVTransportInfo.getSyncLedger() + ", ";
                    string = string + "Request Old=" + rVTransportInfo.getRequestOld() + ", ";
                    string = string + "Default Time Limit=" + rVTransportInfo.getDefaultTimeLimit() + ", ";
                    try {
                        string = string + "Explicit Config Only=" + rVTransportInfo.getExlicitConfigOnly() + ", ";
                    } catch (Throwable var10_13) {
                        // empty catch block
                    }
                    try {
                        string = string + rVTransportInfo.getPolicy().toString();
                    } catch (Throwable var10_14) {
                    }
                } else if (arrtransportInfo[i] instanceof SSTransportInfo) {
                    SSTransportInfo rVTransportInfo = (SSTransportInfo) arrtransportInfo[i];
                    string = string + "ServerNames='" + rVTransportInfo.getServerNames() + "', ";
                    string = string + "Username='" + rVTransportInfo.getUsername() + "', ";
                    string = string + "Project='" + rVTransportInfo.getProject() + "', ";
                    string = string + "DeliveryMode=" + GemsDetailsTableModel.deliveryModeAsString(rVTransportInfo.getDeliveryMode()) + ", ";
                    string = string + "LoadBalanceMode=" + GemsDetailsTableModel.loadBalanceModeAsString(rVTransportInfo.getLoadBalanceMode()) + ", ";
                    string = string + "OverrideLoadBalanceMode=" + (rVTransportInfo.getOverrideLoadBalanceMode() ? "enabled" : "disabled") + ", ";
                    string = string + "GMDFileDelete=" + (rVTransportInfo.getGmdFileDelete() ? "enabled" : "disabled") + ", ";
                    string = string + "SubscribeMode=" + GemsDetailsTableModel.subscribeModeAsString(rVTransportInfo.getSubscribeMode()) + ", ";
                    try {
                        string = string + "ImportSSHeaders=" + GemsDetailsTableModel.importSSHeadersAsString(rVTransportInfo.getImportSSHeaders()) + ", ";
                        string = string + "PreserveGMD=" + GemsDetailsTableModel.preserveGMDAsString(rVTransportInfo.getPreserveGMD()) + "";
                    } catch (Throwable var10_15) {
                        // empty catch block
                    }
                }
                String[] arrobject = new String[7];
                arrobject[0] = arrtransportInfo[i].getName();
                String string2 = n != 0 ? (n != 1 ? (n != 2 ? (n != 3 ? "UNKNOWN" : "SS") : "RVCM") : "RV") : ((arrobject)[1] = "ALL");
                String string3 = n3 != 1 ? (n3 != 2 ? (n3 != 22 ? "UNKNOWN" : "RELIABLE") : "PERSISTENT") : ((arrobject)[2] = "NON_PERSISTENT");
                arrobject[3] = n2 != 1 ? (n2 != 2 ? (n2 != 22 ? "UNKNOWN" : "RELIABLE") : "PERSISTENT") : "NON_PERSISTENT";
                arrobject[4] = String.valueOf(arrtransportInfo[i].getExportHeaders());
                arrobject[5] = String.valueOf(arrtransportInfo[i].getExportProperties());
                arrobject[6] = string;
                Object[] arrobject2 = arrobject;
                this.addRow(arrobject2);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    private static String deliveryModeAsString(int n) {
        switch (n) {
            case 0: {
                return "best_effort";
            }
            case 1: {
                return "some";
            }
            case 2: {
                return "all";
            }
            case 3: {
                return "ordered";
            }
            case 4: {
                return "persistent";
            }
        }
        return "unknown";
    }

    private static String loadBalanceModeAsString(int n) {
        switch (n) {
            case 0: {
                return "none";
            }
            case 1: {
                return "round_robin";
            }
            case 2: {
                return "weighted";
            }
            case 3: {
                return "sorted";
            }
        }
        return "unknown";
    }

    private static String subscribeModeAsString(int n) {
        switch (n) {
            case 0: {
                return "exact";
            }
            case 1: {
                return "all";
            }
        }
        return "unknown";
    }

    private static String importSSHeadersAsString(int n) {
        switch (n) {
            case 0: {
                return "none";
            }
            case 1: {
                return "type_num";
            }
            case 2: {
                return "all";
            }
        }
        return "unknown";
    }

    private static String preserveGMDAsString(int n) {
        switch (n) {
            case 0: {
                return "never";
            }
            case 1: {
                return "always";
            }
            case 2: {
                return "receivers";
            }
        }
        return "unknown";
    }

    public void populateStringInfo(Object[] arrobject) {
        this.setRowCount(0);
        this.setColumnCount(0);
        if (arrobject == null) {
            return;
        }
        this.m_table.setAutoResizeMode(2);
        this.addColumn("Info");
        for (int i = 0; i < arrobject.length; ++i) {
            Object[] arrobject2 = new String[]{arrobject[i].toString()};
            this.addRow(arrobject2);
        }
    }

    public void populateClientsInfoOld(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 9 || !this.getColumnName(0).equals("ClientID")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            String[] arrobject = new String[]{"ClientID", "ConnectionCount", "ConsumerCount", "ProducerCount", "SessionCount", "InTotalMsgs", "OutTotalMsgs", "InMsgRate", "OutMsgRate"};
            this.setColumnIdentifiers(arrobject);
            this.setupColumnWidths();
        }
        try {
            Object object;
            Client client;
            Hashtable arrobject = new Hashtable();
            ConnectionInfo[] arrconnectionInfo = gemsConnectionNode.m_adminConn.getConnections();
            for (int i = 0; arrconnectionInfo != null && i < arrconnectionInfo.length; ++i) {
                String clientID = arrconnectionInfo[i].getClientID();
                if (clientID == null || clientID.length() == 0) continue;
                client = (Client) arrobject.get(clientID);
                if (client != null) {
                    ++client.m_connCount;
                    client.m_consCount += (long) arrconnectionInfo[i].getConsumerCount();
                    client.m_prodCount += (long) arrconnectionInfo[i].getProducerCount();
                    client.m_sessCount += (long) arrconnectionInfo[i].getSessionCount();
                } else {
                    client = new Client(clientID, arrconnectionInfo[i].getConsumerCount(), arrconnectionInfo[i].getProducerCount(), arrconnectionInfo[i].getSessionCount());
                    arrobject.put(clientID, client);
                }
                ProducerInfo[] arrproducerInfo = gemsConnectionNode.m_adminConn.getProducersStatistics(new Long(arrconnectionInfo[i].getID()), null, null);
                for (int j = 0; arrproducerInfo != null && j < arrproducerInfo.length; ++j) {
                    if (client == null) continue;
                    client.m_outMsgs += arrproducerInfo[j].getStatistics().getTotalMessages();
                    client.m_outMsgRate += arrproducerInfo[j].getStatistics().getMessageRate();
                }
                ConsumerInfo[] arrconsumerInfo = gemsConnectionNode.m_adminConn.getConsumersStatistics(new Long(arrconnectionInfo[i].getID()), null, null);
                for (int k = 0; arrconsumerInfo != null && k < arrconsumerInfo.length; ++k) {
                    if (client == null) continue;
                    client.m_inMsgs += arrconsumerInfo[k].getStatistics().getTotalMessages();
                    client.m_inMsgRate += arrconsumerInfo[k].getStatistics().getMessageRate();
                }
            }
            Enumeration enumeration = arrobject.elements();
            while (enumeration.hasMoreElements()) {
                client = (Client) enumeration.nextElement();
                Object[] arrobject2 = new Object[]{client.m_id, new Long(client.m_connCount), new Long(client.m_consCount), new Long(client.m_prodCount), new Long(client.m_sessCount), new Long(client.m_inMsgs), new Long(client.m_outMsgs), new Long(client.m_inMsgRate), new Long(client.m_outMsgRate)};
                this.addRow(arrobject2);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    public void populateClientsInfo(GemsConnectionNode gemsConnectionNode) {
        if (gemsConnectionNode == null || gemsConnectionNode.m_adminConn == null) {
            return;
        }
        this.setRowCount(0);
        if (this.getColumnCount() != 9 || !this.getColumnName(0).equals("ClientID")) {
            this.setColumnCount(0);
            this.m_table.setAutoResizeMode(0);
            this.setColumnIdentifiers(s_client_cols);
            this.setupColumnWidths();
        }
        try {
            ConnectionInfo[] arrconnectionInfo = gemsConnectionNode.m_adminConn.getConnections();
            for (int i = 0; arrconnectionInfo != null && i < arrconnectionInfo.length; ++i) {
                String string = arrconnectionInfo[i].getClientID();
                if (string == null || string.length() == 0) continue;
                long l = 0;
                long l2 = 0;
                long l3 = 0;
                long l4 = 0;
                ProducerInfo[] arrproducerInfo = gemsConnectionNode.m_adminConn.getProducersStatistics(new Long(arrconnectionInfo[i].getID()), null, null);
                for (int j = 0; arrproducerInfo != null && j < arrproducerInfo.length; ++j) {
                    l2 += arrproducerInfo[j].getStatistics().getTotalMessages();
                    l4 += arrproducerInfo[j].getStatistics().getMessageRate();
                }
                ConsumerInfo[] arrconsumerInfo = gemsConnectionNode.m_adminConn.getConsumersStatistics(new Long(arrconnectionInfo[i].getID()), null, null);
                for (int k = 0; arrconsumerInfo != null && k < arrconsumerInfo.length; ++k) {
                    l += arrconsumerInfo[k].getStatistics().getTotalMessages();
                    l3 += arrconsumerInfo[k].getStatistics().getMessageRate();
                }
                Object[] arrobject = new Object[]{string, new Long(arrconnectionInfo[i].getID()), new Long(arrconnectionInfo[i].getConsumerCount()), new Long(arrconnectionInfo[i].getProducerCount()), new Long(arrconnectionInfo[i].getSessionCount()), new Long(l), new Long(l2), new Long(l3), new Long(l4)};
                this.addRow(arrobject);
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return;
        }
    }

    class ColumnListener
            implements PropertyChangeListener {
        ColumnListener() {
        }

        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (!GemsDetailsTableModel.this.flag) {
                return;
            }
            if (propertyChangeEvent.getPropertyName().equals("preferredWidth")) {
                TableColumn tableColumn = (TableColumn) propertyChangeEvent.getSource();
                GemsDetailsTableModel.this.m_colWidths.put(tableColumn.getHeaderValue(), propertyChangeEvent.getNewValue());
            }
        }
    }

    class MyRenderer
            extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
            if (jTable.getColumnModel().getColumn(n2).getPreferredWidth() <= 15) {
                object = null;
            }
            Component component = super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
            if (object instanceof MsgCellValue) {
                MsgCellValue msgCellValue = (MsgCellValue) object;
                this.setHorizontalAlignment(4);
                if (msgCellValue.m_errorLimit > 0 && msgCellValue.m_cellValue >= msgCellValue.m_errorLimit * 8 / 10) {
                    component.setBackground(Color.red);
                    component.setForeground(jTable.getForeground());
                } else if (msgCellValue.m_warnLimit > 0 && msgCellValue.m_cellValue >= msgCellValue.m_warnLimit) {
                    component.setBackground(Color.orange);
                    component.setForeground(jTable.getForeground());
                } else if (bl) {
                    component.setBackground(jTable.getSelectionBackground());
                } else {
                    component.setBackground(Color.white);
                }
            } else if (object instanceof CellValue) {
                CellValue cellValue = (CellValue) object;
                this.setHorizontalAlignment(4);
                if (cellValue.m_errorLimit > 0 && cellValue.m_value >= cellValue.m_errorLimit * 8 / 10) {
                    component.setBackground(Color.red);
                    component.setForeground(jTable.getForeground());
                } else if (cellValue.m_warnLimit > 0 && cellValue.m_value >= cellValue.m_warnLimit) {
                    component.setBackground(Color.orange);
                    component.setForeground(jTable.getForeground());
                } else if (bl) {
                    component.setBackground(jTable.getSelectionBackground());
                } else {
                    component.setBackground(Color.white);
                }
            } else if (object instanceof SSCellValue) {
                SSCellValue sSCellValue = (SSCellValue) object;
                component.setBackground(sSCellValue.CellColour());
            } else if (object instanceof Long) {
                this.setHorizontalAlignment(4);
                if ((Long) object > 0 && jTable.getColumnName(n2).startsWith("Pending")) {
                    component.setBackground(Color.orange);
                    component.setForeground(jTable.getForeground());
                } else if ((Long) object == 0 && jTable.getColumnName(n2).equals("ConnectionID")) {
                    component.setBackground(Color.orange);
                    component.setForeground(jTable.getForeground());
                } else if (bl) {
                    component.setBackground(jTable.getSelectionBackground());
                } else {
                    component.setBackground(Color.white);
                }
            } else if (object instanceof String) {
                this.setHorizontalAlignment(2);
                String string = (String) object;
                if (jTable.getColumnName(n2).equals("Connected") && string.equals("false")) {
                    component.setBackground(Color.red);
                    component.setForeground(jTable.getForeground());
                } else if (jTable.getColumnName(n2).equals("Stalled") && string.equals("true")) {
                    component.setBackground(Color.orange);
                    component.setForeground(jTable.getForeground());
                } else if (jTable.getColumnName(n2).equals("RouteConnected") && string.equals("false") && n2 > 1) {
                    Object object2;
                    if (jTable.getColumnName(n2 - 1).equals("Routed") && (object2 = jTable.getValueAt(n, n2 - 1)) instanceof String && ((String) object2).equals("true")) {
                        component.setBackground(Color.red);
                        component.setForeground(jTable.getForeground());
                    }
                } else if (jTable.getColumnName(n2).equals("Routed") && string.equals("true") && jTable.getColumnCount() > n2) {
                    Object object3;
                    if (jTable.getColumnName(n2 + 1).equals("RouteConnected") && (object3 = jTable.getValueAt(n, n2 + 1)) instanceof String && ((String) object3).equals("false")) {
                        component.setBackground(Color.red);
                        component.setForeground(jTable.getForeground());
                    }
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

    class Client {
        String m_id;
        long m_connCount;
        long m_consCount;
        long m_prodCount;
        long m_sessCount;
        long m_inMsgs;
        long m_outMsgs;
        long m_inMsgRate;
        long m_outMsgRate;

        Client(String string, long l, long l2, long l3) {
            this.m_id = string;
            this.m_connCount = 1;
            this.m_consCount = l;
            this.m_prodCount = l2;
            this.m_sessCount = l3;
            this.m_inMsgs = 0;
            this.m_outMsgs = 0;
            this.m_inMsgRate = 0;
            this.m_outMsgRate = 0;
        }
    }

}

