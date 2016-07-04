/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.SSLParams
 *  com.tibco.tibjms.admin.ServerInfo
 *  com.tibco.tibjms.admin.TraceInfo
 *  com.tibco.tibjms.admin.VersionInfo
 */
package com.tibco.gems;

import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.tibco.tibjms.admin.ServerInfo;

public class GemsServerInfoTableModel
        extends DefaultTableModel {
    JTable m_table;
    Object m_obj = new Object();
    MyRenderer m_renderer;

    public GemsServerInfoTableModel() {
        this.m_renderer = new MyRenderer();
        this.addColumn("Property");
        this.addColumn("Value");
    }

    public void setTable(JTable jTable) {
        this.m_table = jTable;
        if (!Gems.getGems().getViewOnlyMode()) {
            this.m_table.setDefaultRenderer(this.m_obj.getClass(), this.m_renderer);
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
        if (object instanceof Long) {
            return String.valueOf((Long) object);
        }
        return (String) object;
    }

    public void populateTable(ServerInfo serverInfo) {
        Date date = new Date();
        this.setRowCount(0);
        if (serverInfo == null) {
            return;
        }
        Object[] arrobject = new String[]{"ACLsFile", serverInfo.getACLsFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"AsyncDBSize", StringUtilities.getHumanReadableSize(serverInfo.getAsyncDBSize())};
        this.addRow(arrobject);
        arrobject = new String[]{"BackupName", serverInfo.getBackupName()};
        this.addRow(arrobject);
        arrobject = new String[]{"BridgesFile", serverInfo.getBridgesFile()};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"ChannelsFile", serverInfo.getChannelsFile()};
            this.addRow(arrobject);
            arrobject = new String[]{"ClientHeartbeatServerInterval", String.valueOf(serverInfo.getClientHeartbeatServerInterval())};
            this.addRow(arrobject);
            arrobject = new String[]{"ClientTimeoutServerConnection", String.valueOf(serverInfo.getClientTimeoutServerConnection())};
            this.addRow(arrobject);
        } catch (Throwable var4_4) {
            // empty catch block
        }
        arrobject = new String[]{"ClientTraceFilterType", String.valueOf(serverInfo.getClientTraceFilterType())};
        this.addRow(arrobject);
        arrobject = new String[]{"ClientTraceTarget", String.valueOf(serverInfo.getClientTraceTarget())};
        this.addRow(arrobject);
        arrobject = new String[]{"ConfigFile", serverInfo.getConfigFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"ConnectionCount", String.valueOf(serverInfo.getConnectionCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"ConsoleTraceInfo", GemsTraceDialog.TraceInfoToString(serverInfo.getConsoleTraceInfo())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"ConsumerCount", String.valueOf(serverInfo.getConsumerCount())};
            this.addRow(arrobject);
        } catch (Throwable var4_5) {
            // empty catch block
        }

        System.out.println("serverInfo.getDestinationBacklogSwapout()");
//        try {
//            arrobject = new String[]{"DestinationBacklogSwapout", String.valueOf(serverInfo.getDestinationBacklogSwapout())};
//            this.addRow(arrobject);
//        }
//        catch (Throwable var4_7) {
        // empty catch block
//        }


        arrobject = new String[]{"DetailedStatistics", String.valueOf(serverInfo.getDetailedStatistics())};
        this.addRow(arrobject);
        arrobject = new String[]{"DiskReadRate", StringUtilities.getHumanReadableSize(serverInfo.getDiskReadRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"DiskWriteRate", StringUtilities.getHumanReadableSize(serverInfo.getDiskWriteRate())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"DiskReadOperationsRate", String.valueOf(serverInfo.getDiskReadOperationsRate())};
            this.addRow(arrobject);
            arrobject = new String[]{"DiskWriteOperationsRate", String.valueOf(serverInfo.getDiskWriteOperationsRate())};
            this.addRow(arrobject);
        } catch (Throwable var4_8) {
            // empty catch block
        }
        arrobject = new String[]{"DurableCount", String.valueOf(serverInfo.getDurableCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"DurablesFile", serverInfo.getDurablesFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"FactoriesFile", serverInfo.getFactoriesFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"FaultTolerantActivation", String.valueOf(serverInfo.getFaultTolerantActivation())};
        this.addRow(arrobject);
        arrobject = new String[]{"FaultTolerantFailoverReread", String.valueOf(serverInfo.getFaultTolerantFailoverReread())};
        this.addRow(arrobject);
        arrobject = new String[]{"FaultTolerantHeartbeat", String.valueOf(serverInfo.getFaultTolerantHeartbeat())};
        this.addRow(arrobject);
        arrobject = new String[]{"FaultTolerantReconnectTimeout", String.valueOf(serverInfo.getFaultTolerantReconnectTimeout())};
        this.addRow(arrobject);
        arrobject = new String[]{"FaultTolerantURL", serverInfo.getFaultTolerantURL()};
        this.addRow(arrobject);
        arrobject = new String[]{"GroupsFile", serverInfo.getGroupsFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundBytesRate", String.valueOf(serverInfo.getInboundBytesRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundMessageCount", String.valueOf(serverInfo.getInboundMessageCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"InboundMessageRate", String.valueOf(serverInfo.getInboundMessageRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"ListenPorts", StringUtilities.arrayToString(serverInfo.getListenPorts())};
        this.addRow(arrobject);
        arrobject = new String[]{"LogFileMaxSize", StringUtilities.getHumanReadableSize(serverInfo.getLogFileMaxSize())};
        this.addRow(arrobject);
        arrobject = new String[]{"LogFileName", serverInfo.getLogFileName()};
        this.addRow(arrobject);
        arrobject = new String[]{"LogTraceInfo", GemsTraceDialog.TraceInfoToString(serverInfo.getLogTraceInfo())};
        this.addRow(arrobject);

        System.out.println("serverInfo.getMaxClientMsgSize()");
//        try {
//            arrobject = new String[]{"MaxClientMsgSize", StringUtilities.getHumanReadableSize(serverInfo.getMaxClientMsgSize())};
//            this.addRow(arrobject);
//        }
//        catch (Throwable var4_9) {
        // empty catch block
//        }

        arrobject = new String[]{"MaxConnections", String.valueOf(serverInfo.getMaxConnections())};
        this.addRow(arrobject);
        arrobject = new String[]{"MaxMsgMemory", StringUtilities.getHumanReadableSize(serverInfo.getMaxMsgMemory())};
        this.addRow(arrobject);
        arrobject = new String[]{"MaxStatisticsMemory", StringUtilities.getHumanReadableSize(serverInfo.getMaxStatisticsMemory())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"MessagePoolBlockSize", String.valueOf(serverInfo.getMessagePoolBlockSize())};
            this.addRow(arrobject);
            arrobject = new String[]{"MessagePoolSize", String.valueOf(serverInfo.getMessagePoolSize())};
            this.addRow(arrobject);
        } catch (Throwable var4_10) {
            // empty catch block
        }
        arrobject = new String[]{"MsgMem", StringUtilities.getHumanReadableSize(serverInfo.getMsgMem())};
        this.addRow(arrobject);
        arrobject = new String[]{"MsgMemPooled", StringUtilities.getHumanReadableSize(serverInfo.getMsgMemPooled())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"MulticastStatisticsInterval", String.valueOf(serverInfo.getMulticastStatisticsInterval())};
            this.addRow(arrobject);
            arrobject = new String[]{"NPSendCheckMode", String.valueOf(serverInfo.getNPSendCheckMode())};
            this.addRow(arrobject);
        } catch (Throwable var4_11) {
            // empty catch block
        }
        arrobject = new String[]{"OutboundMessageCount", String.valueOf(serverInfo.getOutboundMessageCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"OutboundMessageRate", String.valueOf(serverInfo.getOutboundMessageRate())};
        this.addRow(arrobject);
        arrobject = new String[]{"PendingMessageCount", String.valueOf(serverInfo.getPendingMessageCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"PendingMessageSize", StringUtilities.getHumanReadableSize(serverInfo.getPendingMessageSize())};
        this.addRow(arrobject);
        arrobject = new String[]{"ProcessId", String.valueOf(serverInfo.getProcessId())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"ProducerCount", String.valueOf(serverInfo.getProducerCount())};
            this.addRow(arrobject);
        } catch (Throwable var4_12) {
            // empty catch block
        }
        arrobject = new String[]{"QueueCount", String.valueOf(serverInfo.getQueueCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"QueuesFile", serverInfo.getQueuesFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"RateInterval", String.valueOf(serverInfo.getRateInterval())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"ReserveMemory", StringUtilities.getHumanReadableSize(serverInfo.getReserveMemory())};
            this.addRow(arrobject);
            arrobject = new String[]{"RouteRecoverInterval", String.valueOf(serverInfo.getRouteRecoverInterval())};
            this.addRow(arrobject);
            arrobject = new String[]{"RouteRecoverCount", String.valueOf(serverInfo.getRouteRecoverCount())};
            this.addRow(arrobject);
        } catch (Throwable var4_13) {
            // empty catch block
        }
        arrobject = new String[]{"RoutesFile", serverInfo.getRoutesFile()};
        this.addRow(arrobject);

        try {
            String[] arrstring = new String[2];
            arrstring[0] = "ServerConfigurationMode";
            System.out.println("serverInfo.getServerConfigurationMode()");
//            arrstring[1] = serverInfo.getServerConfigurationMode() == 0 ? "Conf" : "XML";
            arrobject = arrstring;
            this.addRow(arrobject);
        } catch (Throwable var4_14) {
            // empty catch block
        }

        try {
            arrobject = new String[]{"ServerHeartbeatClientInterval", String.valueOf(serverInfo.getServerHeartbeatClientInterval())};
            this.addRow(arrobject);
            arrobject = new String[]{"ServerHeartbeatServerInterval", String.valueOf(serverInfo.getServerHeartbeatServerInterval())};
            this.addRow(arrobject);
        } catch (Throwable var4_15) {
            // empty catch block
        }
        arrobject = new String[]{"ServerName", serverInfo.getServerName()};
        this.addRow(arrobject);
        arrobject = new String[]{"ServerRateInterval", String.valueOf(serverInfo.getServerRateInterval())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"ServerTimeoutClientConnection", String.valueOf(serverInfo.getServerTimeoutClientConnection())};
            this.addRow(arrobject);
            arrobject = new String[]{"ServerTimeoutServerConnection", String.valueOf(serverInfo.getServerTimeoutServerConnection())};
            this.addRow(arrobject);
            arrobject = new String[]{"SessionCount", String.valueOf(serverInfo.getSessionCount())};
            this.addRow(arrobject);
        } catch (Throwable var4_16) {
            // empty catch block
        }
        arrobject = new String[]{"SSLCertUserSpecname", String.valueOf(serverInfo.getSSLCertUserSpecname())};
        this.addRow(arrobject);
        arrobject = new String[]{"SSLDHSize", String.valueOf(serverInfo.getSSLDHSize())};
        this.addRow(arrobject);
        String[] arrstring = new String[2];
        arrstring[0] = "SSLParams";
        arrstring[1] = serverInfo.getSSLParams() == null ? "" : serverInfo.getSSLParams().toString();
        arrobject = arrstring;
        this.addRow(arrobject);
        date.setTime(serverInfo.getStartTime());
        arrobject = new String[]{"StartTime", date.toString()};
        this.addRow(arrobject);
        String[] arrstring2 = new String[2];
        arrstring2[0] = "State";
        arrstring2[1] = serverInfo.getState() == 4 ? "active" : "standby";
        arrobject = arrstring2;
        this.addRow(arrobject);
        arrobject = new String[]{"StatisticsCleanupInterval", String.valueOf(serverInfo.getStatisticsCleanupInterval())};
        this.addRow(arrobject);
        arrobject = new String[]{"StoreAsyncMinimum", StringUtilities.getHumanReadableSize(serverInfo.getStoreAsyncMinimum())};
        this.addRow(arrobject);
        arrobject = new String[]{"StoreDirectory", serverInfo.getStoreDirectory()};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"StoresFile", serverInfo.getStoresFile()};
            this.addRow(arrobject);
        } catch (Throwable var4_17) {
            // empty catch block
        }
        arrobject = new String[]{"StoreMinimum", StringUtilities.getHumanReadableSize(serverInfo.getStoreMinimum())};
        this.addRow(arrobject);
        arrobject = new String[]{"StoreSyncMinimum", StringUtilities.getHumanReadableSize(serverInfo.getStoreSyncMinimum())};
        this.addRow(arrobject);
        arrobject = new String[]{"SyncDBSize", StringUtilities.getHumanReadableSize(serverInfo.getSyncDBSize())};
        this.addRow(arrobject);
        arrobject = new String[]{"TibrvcmFile", serverInfo.getTibrvcmFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"TopicCount", String.valueOf(serverInfo.getTopicCount())};
        this.addRow(arrobject);
        arrobject = new String[]{"TopicsFile", serverInfo.getTopicsFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"TransportsFile", serverInfo.getTransportsFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"UpTime", StringUtilities.getFullHumanReadableTime(serverInfo.getUpTime())};
        this.addRow(arrobject);
        arrobject = new String[]{"URL", String.valueOf(serverInfo.getURL())};
        this.addRow(arrobject);
        try {
            int[] arrn = serverInfo.getUserAuthLocations();
            String string = "";
            for (int i = 0; arrn != null && i < arrn.length; ++i) {
                string = string + (arrn[i] == 3 ? "Local" : (arrn[i] == 1 ? "LDAP" : "System"));
                if (i >= arrn.length - 1) continue;
                string = string + ",";
            }
            arrobject = new String[]{"UserAuthLocations", string};
            this.addRow(arrobject);
        } catch (Throwable var4_18) {
            // empty catch block
        }
        arrobject = new String[]{"UsersFile", serverInfo.getUsersFile()};
        this.addRow(arrobject);
        arrobject = new String[]{"VersionInfo", serverInfo.getVersionInfo().toString()};
        this.addRow(arrobject);
        arrobject = new String[]{"isAuthorizationEnabled", String.valueOf(serverInfo.isAuthorizationEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isClientTraceEnabled", String.valueOf(serverInfo.isClientTraceEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isEvaluation", String.valueOf(serverInfo.isEvaluation())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFlowControlEnabled", String.valueOf(serverInfo.isFlowControlEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isFSyncEnabled", String.valueOf(serverInfo.isFSyncEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isMessageSwappingEnabled", String.valueOf(serverInfo.isMessageSwappingEnabled())};
        this.addRow(arrobject);
        try {
            arrobject = new String[]{"isMulticastEnabled", String.valueOf(serverInfo.isMulticastEnabled())};
            this.addRow(arrobject);
        } catch (Throwable var4_19) {
            // empty catch block
        }
        arrobject = new String[]{"isRoutingEnabled", String.valueOf(serverInfo.isRoutingEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSSLRequireClientCert", String.valueOf(serverInfo.isSSLRequireClientCert())};
        this.addRow(arrobject);
        arrobject = new String[]{"isSSLUserCertUsername", String.valueOf(serverInfo.isSSLUserCertUsername())};
        this.addRow(arrobject);
        arrobject = new String[]{"isStatisticsEnabled", String.valueOf(serverInfo.isStatisticsEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isStoreCRCEnabled", String.valueOf(serverInfo.isStoreCRCEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isStoreTruncateEnabled", String.valueOf(serverInfo.isStoreTruncateEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isTibrvTransportsEnabled", String.valueOf(serverInfo.isTibrvTransportsEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isTibssTransportsEnabled", String.valueOf(serverInfo.isTibssTransportsEnabled())};
        this.addRow(arrobject);
        arrobject = new String[]{"isTrackCorrelationIds", String.valueOf(serverInfo.isTrackCorrelationIds())};
        this.addRow(arrobject);
        arrobject = new String[]{"isTrackMsgIds", String.valueOf(serverInfo.isTrackMsgIds())};
        this.addRow(arrobject);
    }

    class MyRenderer
            extends DefaultTableCellRenderer {
        public MyRenderer() {
            this.setToolTipText("Double-click to set property");
        }
    }

}

