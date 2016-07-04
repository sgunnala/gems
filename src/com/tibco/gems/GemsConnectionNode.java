/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.ConnectionInfo
 *  com.tibco.tibjms.admin.GroupInfo
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.ServerInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 *  com.tibco.tibjms.admin.TopicInfo
 *  com.tibco.tibjms.admin.TraceInfo
 *  com.tibco.tibjms.admin.UserInfo
 *  javax.jms.Message
 */
package com.tibco.gems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Vector;

import javax.jms.Message;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tibco.gems.chart.GemsChartFrame;
import com.tibco.gems.chart.GemsSubscriber;
import com.tibco.tibjms.admin.ConnectionInfo;
import com.tibco.tibjms.admin.GroupInfo;
import com.tibco.tibjms.admin.QueueInfo;
import com.tibco.tibjms.admin.ServerInfo;
import com.tibco.tibjms.admin.TibjmsAdmin;
import com.tibco.tibjms.admin.TibjmsAdminException;
import com.tibco.tibjms.admin.TopicInfo;
import com.tibco.tibjms.admin.TraceInfo;
import com.tibco.tibjms.admin.UserInfo;

public class GemsConnectionNode
        extends IconNode
        implements Runnable {
    static Hashtable props = null;
    static Hashtable connections = new Hashtable();
    protected GemsSubscriber m_sub = null;
    protected GemsChartFrame m_chart = null;
    protected Timer m_chartUpdateTimer = null;
    String m_url;
    String m_user;
    String m_password;
    String m_logDir = "./log";
    LOG_TYPE m_logServerInfo = LOG_TYPE.Never;
    Vector m_logColumnNames = null;
    File m_logFile = null;
    FileOutputStream m_fileOut = null;
    PrintStream m_filePrint = null;
    SimpleDateFormat m_fileDateFormat = new SimpleDateFormat("ddMMMyyyy");
    SimpleDateFormat dateFormatMillis = null;
    TibjmsAdmin m_adminConn = null;
    ServerInfo m_serverInfo = null;
    DefaultMutableTreeNode m_topicsNode = null;
    DefaultMutableTreeNode m_queuesNode = null;
    DefaultMutableTreeNode m_usersNode = null;
    DefaultMutableTreeNode m_consumersNode = null;
    DefaultMutableTreeNode m_channelsNode = null;
    DefaultMutableTreeNode m_producersNode = null;
    DefaultMutableTreeNode m_connectionsNode = null;
    DefaultMutableTreeNode m_sysConnectionsNode = null;
    DefaultMutableTreeNode m_clientsNode = null;
    DefaultMutableTreeNode m_routesNode = null;
    DefaultMutableTreeNode m_bridgesNode = null;
    DefaultMutableTreeNode m_transactionsNode = null;
    DefaultMutableTreeNode m_transportsNode = null;
    DefaultMutableTreeNode m_durablesNode = null;
    DefaultMutableTreeNode m_groupsNode = null;
    DefaultMutableTreeNode m_aclsNode = null;
    DefaultMutableTreeNode m_adminAclsNode = null;
    DefaultMutableTreeNode m_factoriesNode = null;
    DefaultMutableTreeNode m_storesFileNode = null;
    DefaultMutableTreeNode m_storesDbNode = null;
    DefaultMutableTreeNode m_mstoresNode = null;
    DefaultMutableTreeNode m_serviceNode = null;
    Vector m_ssNodes = null;
    DefaultMutableTreeNode m_ssTriggerNode = null;
    DefaultMutableTreeNode m_ssRecipeNode = null;
    DefaultMutableTreeNode m_ssInterfaceNode = null;
    Hashtable m_warnLimits = null;
    Hashtable m_errorLimits = null;
    Hashtable m_clientIDs = null;
    Properties m_SSSystems = new Properties();
    long m_delay = 0;
    Thread m_thread = null;
    boolean m_isAutoConnect = false;
    boolean m_isInError = false;
    boolean m_isInWarning = false;
    Map m_sslParams = new Hashtable();
    GemsServiceTable m_services = null;
    GemsEventMonitor m_eventMonitor = null;

    public GemsConnectionNode(String string) {
        super(string, true);
        this.m_url = "tcp://localhost:7222";
        this.m_user = "admin";
        this.setIconName("computer");
        this.init();
    }

    public synchronized void init() {
        if (props == null) {
            try {
                props = new Hashtable();
                props.put("isAuthorizationEnabled", new GemsProperty("AuthorizationEnabled", Boolean.TYPE, "<html>Enable or disable Authorization mode<br> for subsequent requests</html>"));
                props.put("DetailedStatistics", new GemsProperty("DetailedStatistics", Integer.TYPE, "<html>0 = None, or a combination of<br>2 = Producers, 4 = Consumers, 8 = Routes</html>"));
                props.put("MaxMsgMemory", new GemsProperty("MaxMsgMemory", Long.TYPE, "<html>Maximum number of bytes the<br>server can use for messages</html>"));
                props.put("MaxStatisticsMemory", new GemsProperty("MaxStatisticsMemory", Long.TYPE, "<html>Maximum amount of memory (bytes)<br>to use for detailed statistic gathering</html>"));
                props.put("isMessageSwappingEnabled", new GemsProperty("MessageSwappingEnabled", Boolean.TYPE, "<html>Enables or disables the ability to swap<br>messages to disk</html>"));
                props.put("RateInterval", new GemsProperty("RateInterval", Long.TYPE, "<html>The statistics interval(millisecs) for routes<br>destinations, producers, and consumers</html>"));
                props.put("ServerRateInterval", new GemsProperty("ServerRateInterval", Long.TYPE, "<html>The interval(millisecs) over which server<br>statistics are averaged</html>"));
                props.put("StatisticsCleanupInterval", new GemsProperty("StatisticsCleanupInterval", Long.TYPE, "<html>How long (in millisecs) the server keeps detailed<br>statistics if the destination has no activity</html>"));
                props.put("isStatisticsEnabled", new GemsProperty("StatisticsEnabled", Boolean.TYPE, "<html>Enables or disables statistic gathering for<br>producers, consumers, destinations, and routes</html>"));
                props.put("isTrackCorrelationIds", new GemsProperty("TrackCorrelationIds", Boolean.TYPE, "<html>Enables or disables tracking messages<br>by CorrelationID</html>"));
                props.put("isTrackMsgIds", new GemsProperty("TrackMsgIds", Boolean.TYPE, "<html>Enables or disables tracking<br>messages by MessageID</html>"));
                props.put("MulticastStatisticsInterval", new GemsProperty("MulticastStatisticsInterval", Long.TYPE, "<html>Multicast statistics interval in milliseconds<br>0 disables multicast statistics collection</html>"));
            } catch (Throwable var1_1) {
                System.err.println(var1_1);
            }
        }
        try {
            this.dateFormatMillis = new SimpleDateFormat(Gems.getGems().getLogDateTimeFormat());
        } catch (Throwable var1_2) {
            System.err.println("Bad LogDateTimeFormat in gems.props file: " + var1_2);
            this.dateFormatMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss SSS zzz yyyy");
        }
    }

    public GemsConnectionNode(String string, String url, String user, String password, long delay, boolean bl, LOG_TYPE lOG_TYPE, String logDir) {
        super(string, true);
        this.m_url = url;
        this.m_user = user;
        this.m_password = password;
        this.m_delay = delay * 1000;
        this.m_logDir = logDir;
        this.m_logServerInfo = lOG_TYPE;
        this.setIconName("computer");
        this.init();
        this.m_isAutoConnect = bl;
    }

    static Hashtable getConnections() {
        return connections;
    }

    public boolean showError() {
        if (this.m_adminConn == null) {
            return true;
        }
        if (this.m_eventMonitor != null && this.m_eventMonitor.m_messages.size() > 0) {
            return true;
        }
        return this.m_isInError;
    }

    public boolean showWarning() {
        return this.m_isInWarning;
    }

    public boolean showEvents() {
        return this.m_eventMonitor != null;
    }

    public boolean isAutoConnect() {
        return this.m_isAutoConnect;
    }

    public void addEventMonitor(long l, boolean bl) {
        this.m_eventMonitor = new GemsEventMonitor(this, l, bl);
    }

    public void addEventSubscription(String string, String string2) {
        this.m_eventMonitor.addSubscription(string, string2);
    }

    public void clearEventMessages() {
        if (this.m_eventMonitor != null) {
            this.m_eventMonitor.reset();
        }
    }

    public Vector getEventMessages() {
        if (this.m_eventMonitor != null) {
            return this.m_eventMonitor.getMessages();
        }
        return null;
    }

    public int getEventCount() {
        if (this.m_eventMonitor != null) {
            return this.m_eventMonitor.getMessageCount();
        }
        return 0;
    }

    public void addServiceTable(long period, boolean useQTemps, boolean useTTemps, boolean enabled) {
        this.m_services = new GemsServiceTable(this, period, useQTemps, useTTemps, enabled);
    }

    public void addService(String name, String reqDest, boolean reqIsQueue, String respDest, boolean respIsQueue, long respLimit) {
        this.m_services.addService(name, reqDest, reqIsQueue, respDest, respIsQueue, respLimit);
    }

    public TraceInfo getServerTrace(boolean bl) {
        if (this.m_adminConn == null) {
            return null;
        }
        try {
            ServerInfo serverInfo = this.m_adminConn.getInfo();
            if (serverInfo != null) {
                if (bl) {
                    return serverInfo.getLogTraceInfo();
                }
                return serverInfo.getConsoleTraceInfo();
            }
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
        }
        return null;
    }

    public void setServerTrace(TraceInfo traceInfo, boolean bl) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            ServerInfo serverInfo = this.m_adminConn.getInfo();
            if (serverInfo != null) {
                if (bl) {
                    serverInfo.setLogTraceInfo(traceInfo);
                } else {
                    throw new RuntimeException("serverInfo.setTraceBufferTraceInfo(traceInfo);");
                }
                this.m_adminConn.updateServer(serverInfo);
                Gems.getGems().scheduleRepaint();
            }
        } catch (TibjmsAdminException var3_4) {
            System.err.println("JMSException: " + var3_4.getMessage());
            return;
        }
    }

    public void setServerProperty(String string) {
        if (this.m_adminConn == null || string == null) {
            return;
        }
        try {
            String string2;
            GemsSetPropDialog gemsSetPropDialog;
            GemsProperty gemsProperty = (GemsProperty) props.get(string);
            String string3 = null;
            ServerInfo serverInfo = this.m_adminConn.getInfo();
            if (serverInfo == null) {
                return;
            }
            if (string != null && (gemsProperty = (GemsProperty) props.get(string)) != null) {
                string3 = gemsProperty.getValue((Object) serverInfo);
            }
            if ((string2 = (gemsSetPropDialog = new GemsSetPropDialog(Gems.getGems().m_frame, "Set server Property for " + (String) this.getUserObject())).getValue(props, string, string3)) != null) {
                gemsProperty = (GemsProperty) props.get(gemsSetPropDialog.getSelectedProp());
                gemsProperty.setValue((Object) serverInfo, string2);
                this.m_adminConn.updateServer(serverInfo);
                Gems.getGems().scheduleRepaint();
            }
        } catch (TibjmsAdminException var2_3) {
            JOptionPane.showMessageDialog(Gems.getGems().m_frame, var2_3.getMessage(), "Set Server Property", 0);
            return;
        }
    }

    public void connect(String string, String string2, String string3, String string4, Map map) {
        this.setUserObject(string);
        this.m_url = string2;
        this.m_user = string3;
        this.m_password = string4;
        this.m_sslParams = map;
        this.connect();
    }

    public synchronized void connect() {
        block44:
        {
            String string;
            Properties properties;
            Object object;
            this.removeAllChildren();
            try {
                System.err.println("Connecting to: " + this.m_url);
                this.m_adminConn = this.isSSL() && this.m_sslParams != null ? new TibjmsAdmin(this.m_url, this.m_user, this.m_password, this.m_sslParams) : new TibjmsAdmin(this.m_url, this.m_user, this.m_password);
                this.m_adminConn.setAutoSave(true);
                this.m_adminConn.setCommandTimeout(Gems.getGems().getAdminTimeout());
                string = (String) this.getUserObject();
                string = string + "(" + this.m_url + ")";
                connections.put(string, this);
                Gems.debug("Got connection");
                this.m_serverInfo = this.m_adminConn.getInfo();
                Gems.debug("Got serverInfo");
                this.updateErrorWarningFlags();
                this.m_logColumnNames = ReflectionUtils.getGetterMethodNames(this.m_serverInfo.getClass());
            } catch (TibjmsAdminException var1_2) {
                System.err.println(var1_2.toString());
                return;
            }
            string = Gems.getGems().getHideViews();
            if (string.indexOf("ACLs") < 0) {
                this.m_aclsNode = new DefaultMutableTreeNode("ACLs", true);
                this.add(this.m_aclsNode);
                this.m_adminAclsNode = new DefaultMutableTreeNode("AdminACLs", true);
                this.add(this.m_adminAclsNode);
            }
            if (string.indexOf("Bridges") < 0) {
                this.m_bridgesNode = new DefaultMutableTreeNode("Bridges", true);
                this.add(this.m_bridgesNode);
            }
            if (string.indexOf("Channels") < 0) {
                this.m_channelsNode = new DefaultMutableTreeNode("Channels", true);
                this.add(this.m_channelsNode);
            }
            if (string.indexOf("Clients") < 0) {
                this.m_clientsNode = new DefaultMutableTreeNode("Clients", true);
                this.add(this.m_clientsNode);
            }
            if (string.indexOf("Connections") < 0) {
                this.m_connectionsNode = new DefaultMutableTreeNode("Connections(Client)", true);
                this.add(this.m_connectionsNode);
                this.m_sysConnectionsNode = new DefaultMutableTreeNode("Connections(System)", true);
                this.add(this.m_sysConnectionsNode);
            }
            if (string.indexOf("Consumers") < 0) {
                this.m_consumersNode = new DefaultMutableTreeNode("Consumers", true);
                this.add(this.m_consumersNode);
            }
            if (string.indexOf("Durables") < 0) {
                this.m_durablesNode = new DefaultMutableTreeNode("Durables", true);
                this.add(this.m_durablesNode);
            }
            if (string.indexOf("Factories") < 0) {
                this.m_factoriesNode = new DefaultMutableTreeNode("Factories", true);
                this.add(this.m_factoriesNode);
            }
            if (string.indexOf("Groups") < 0) {
                this.m_groupsNode = new DefaultMutableTreeNode("Groups", true);
                this.add(this.m_groupsNode);
            }
            if (string.indexOf("Producers") < 0) {
                this.m_producersNode = new DefaultMutableTreeNode("Producers", true);
                this.add(this.m_producersNode);
            }
            if (string.indexOf("Queues") < 0) {
                object = Gems.getGems().getQueueNamePattern();
                this.m_queuesNode = object.equals(">") ? new DefaultMutableTreeNode("Queues", true) : new DefaultMutableTreeNode("Queues(" + (String) object + ")", true);
                this.add(this.m_queuesNode);
            }
            if (string.indexOf("Routes") < 0) {
                this.m_routesNode = new DefaultMutableTreeNode("Routes", true);
                this.add(this.m_routesNode);
            }
            if (this.m_services != null) {
                this.m_serviceNode = new DefaultMutableTreeNode("Services", true);
                this.add(this.m_serviceNode);
            }
            if (string.indexOf("Stores") < 0) {
                this.m_storesFileNode = new DefaultMutableTreeNode("Stores(File)", true);
                this.add(this.m_storesFileNode);
                this.m_storesDbNode = new DefaultMutableTreeNode("Stores(DB)", true);
                this.add(this.m_storesDbNode);
                try {
                    if (Class.forName("com.tibco.tibjms.admin.MStoreInfo") != null) {
                        this.m_mstoresNode = new DefaultMutableTreeNode("Stores(MStore)", true);
                        this.add(this.m_mstoresNode);
                    }
                } catch (Exception var2_4) {
                    // empty catch block
                }
            }
            if (string.indexOf("Topics") < 0) {
                object = Gems.getGems().getTopicNamePattern();
                this.m_topicsNode = object.equals(">") ? new DefaultMutableTreeNode("Topics", true) : new DefaultMutableTreeNode("Topics(" + (String) object + ")", true);
                this.add(this.m_topicsNode);
            }
            if (string.indexOf("Transactions") < 0) {
                this.m_transactionsNode = new DefaultMutableTreeNode("Transactions", true);
                this.add(this.m_transactionsNode);
            }
            if (string.indexOf("Transports") < 0) {
                this.m_transportsNode = new DefaultMutableTreeNode("Transports", true);
                this.add(this.m_transportsNode);
            }
            if (string.indexOf("Users") < 0) {
                this.m_usersNode = new DefaultMutableTreeNode("Users", true);
                this.add(this.m_usersNode);
            }
            Enumeration enumeration2 = this.m_SSSystems.propertyNames();
            while (enumeration2.hasMoreElements()) {
                String string2 = (String) enumeration2.nextElement();
                if (this.m_ssNodes == null) {
                    this.m_ssNodes = new Vector();
                }
                properties = (Properties) this.m_SSSystems.get(string2);
                GemsSSNode gemsSSNode = new GemsSSNode(string2, properties.getProperty("QUEUE"), properties.getProperty("VERSION"), this);
                this.add(gemsSSNode);
                this.m_ssTriggerNode = new DefaultMutableTreeNode("Counters", true);
                gemsSSNode.add(this.m_ssTriggerNode);
                this.m_ssTriggerNode = new DefaultMutableTreeNode("Interfaces", true);
                gemsSSNode.add(this.m_ssTriggerNode);
                this.m_ssTriggerNode = new DefaultMutableTreeNode("Transports", true);
                gemsSSNode.add(this.m_ssTriggerNode);
                Enumeration enumeration = properties.propertyNames();
                while (enumeration.hasMoreElements()) {
                    String string3 = (String) enumeration.nextElement();
                    if (Objects.equals(string3, "QUEUE") || Objects.equals(string3, "VERSION")) continue;
                    if (Objects.equals(properties.getProperty(string3), "INT")) {
                        this.m_ssInterfaceNode = new DefaultMutableTreeNode("Int: " + string3, true);
                        gemsSSNode.add(this.m_ssInterfaceNode);
                        this.m_ssRecipeNode = new DefaultMutableTreeNode("Active Recipes", true);
                        this.m_ssInterfaceNode.add(this.m_ssRecipeNode);
                        this.m_ssRecipeNode = new DefaultMutableTreeNode("Active Triggers", true);
                        this.m_ssInterfaceNode.add(this.m_ssRecipeNode);
                        this.m_ssTriggerNode = new DefaultMutableTreeNode("Disabled", true);
                        this.m_ssInterfaceNode.add(this.m_ssTriggerNode);
                        this.m_ssTriggerNode = new DefaultMutableTreeNode("Listeners", true);
                        this.m_ssInterfaceNode.add(this.m_ssTriggerNode);
                    }
                    if (properties.getProperty(string3) != "IMS") continue;
                    this.m_ssInterfaceNode = new DefaultMutableTreeNode("IMS", true);
                    gemsSSNode.add(this.m_ssInterfaceNode);
                    this.m_ssRecipeNode = new DefaultMutableTreeNode("IMS Statistics", true);
                    this.m_ssInterfaceNode.add(this.m_ssRecipeNode);
                    this.m_ssRecipeNode = new DefaultMutableTreeNode("IMS Buffers", true);
                    this.m_ssInterfaceNode.add(this.m_ssRecipeNode);
                }
                this.m_ssNodes.add(gemsSSNode);
            }
            try {
                if (this.m_topicsNode != null) {
                    if (this.m_serverInfo.getTopicCount() > Gems.getGems().getMaxTopics() && Gems.getGems().getTopicNamePattern().length() <= 1) {
                        System.err.println("Warning: Over " + Gems.getGems().getMaxTopics() + " topics, main topics display disabled. Increase MaxTopics or configure a TopicNamePattern in the gems.props property file");
                    } else {
                        TopicInfo[] topics;

                        try {
                            topics = this.m_adminConn.getTopics(Gems.getGems().getTopicNamePattern(), Gems.getGems().getPermType());
                        } catch (Throwable var3_7) {
                            topics = this.m_adminConn.getTopics(Gems.getGems().getTopicNamePattern());
                        }
                        for (int i = 0; i < topics.length; ++i) {
                            if (topics[i].getName().startsWith("$sys")) continue;
                            this.m_topicsNode.add(new GemsTopicNode(topics[i].getName()));
                        }
                    }
                }
                if (this.m_queuesNode == null) break block44;
                if (this.m_serverInfo.getQueueCount() > Gems.getGems().getMaxQueues() && Gems.getGems().getQueueNamePattern().length() <= 1) {
                    System.err.println("Warning: Over " + Gems.getGems().getMaxQueues() + " queues, main queues display disabled. Increase MaxQueues or configure a QueueNamePattern in the gems.props property file");
                    break block44;
                }

                QueueInfo[] queues;

                try {
                    queues = this.m_adminConn.getQueues(Gems.getGems().getQueueNamePattern(), Gems.getGems().getPermType());
                } catch (Throwable var3_9) {
                    queues = this.m_adminConn.getQueues(Gems.getGems().getQueueNamePattern());
                }
                for (int i = 0; i < queues.length; ++i) {
                    try {
                        this.m_queuesNode.add(new GemsQueueNode(queues[i].getName(), queues[i].isRouted()));
                    } catch (Throwable var4_12) {
                        this.m_queuesNode.add(new GemsQueueNode(queues[i].getName()));
                    }
                }
            } catch (TibjmsAdminException var2_5) {
                System.err.println("JMSException: " + var2_5.getMessage());
                return;
            }
        }
        if (this.m_serverInfo != null && this.m_serverInfo.getState() == 4) {
            if (this.m_eventMonitor != null) {
                this.m_eventMonitor.systemStart();
            }
            if (this.m_services != null) {
                this.m_services.systemStart();
            }
        }
    }

    public boolean isSSL() {
        return this.m_url.startsWith("ssl");
    }

    public void updateErrorWarningFlags() {
        this.m_isInError = false;
        this.m_isInWarning = false;
        if (this.isFtUrl() && this.m_serverInfo.getState() == 4 && this.m_serverInfo.getBackupName() == null) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        Long l = this.getErrorLimit("Connections");
        if (l != null && (long) this.m_serverInfo.getConnectionCount() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("Sessions");
        try {
            if (l != null && (long) this.m_serverInfo.getSessionCount() > l) {
                this.m_isInError = true;
                Gems.getGems().treeRepaint();
                return;
            }
        } catch (Throwable var2_2) {
            // empty catch block
        }
        if ((l = this.getErrorLimit("Queues")) != null && (long) this.m_serverInfo.getQueueCount() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("Topics");
        if (l != null && (long) this.m_serverInfo.getTopicCount() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("Durables");
        if (l != null && (long) this.m_serverInfo.getDurableCount() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("PendingMsgs");
        if (l != null && this.m_serverInfo.getPendingMessageCount() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("PendingMsgsSize");
        if (l != null && this.m_serverInfo.getPendingMessageSize() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("MsgMem");
        if (l != null && this.m_serverInfo.getMsgMem() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("InMsgCount");
        if (l != null && this.m_serverInfo.getInboundMessageCount() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("InMsgRate");
        if (l != null && this.m_serverInfo.getInboundMessageRate() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("OutMsgCount");
        if (l != null && this.m_serverInfo.getOutboundMessageCount() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getErrorLimit("OutMsgRate");
        if (l != null && this.m_serverInfo.getOutboundMessageRate() > l) {
            this.m_isInError = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("Connections");
        if (l != null && (long) this.m_serverInfo.getConnectionCount() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("Sessions");
        try {
            if (l != null && (long) this.m_serverInfo.getSessionCount() > l) {
                this.m_isInWarning = true;
                Gems.getGems().treeRepaint();
                return;
            }
        } catch (Throwable var2_3) {
            // empty catch block
        }
        if ((l = this.getWarnLimit("Queues")) != null && (long) this.m_serverInfo.getQueueCount() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("Topics");
        if (l != null && (long) this.m_serverInfo.getTopicCount() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("Durables");
        if (l != null && (long) this.m_serverInfo.getDurableCount() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("PendingMsgs");
        if (l != null && this.m_serverInfo.getPendingMessageCount() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("PendingMsgsSize");
        if (l != null && this.m_serverInfo.getPendingMessageSize() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("MsgMem");
        if (l != null && this.m_serverInfo.getMsgMem() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("InMsgCount");
        if (l != null && this.m_serverInfo.getInboundMessageCount() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("InMsgRate");
        if (l != null && this.m_serverInfo.getInboundMessageRate() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("OutMsgCount");
        if (l != null && this.m_serverInfo.getOutboundMessageCount() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        l = this.getWarnLimit("OutMsgRate");
        if (l != null && this.m_serverInfo.getOutboundMessageRate() > l) {
            this.m_isInWarning = true;
            Gems.getGems().treeRepaint();
            return;
        }
        Gems.getGems().treeRepaint();
    }

    public boolean isFtUrl() {
        if (this.m_url != null && this.m_url.indexOf(44) > 0) {
            return true;
        }
        return false;
    }

    public Long getErrorLimit(String string) {
        if (this.m_errorLimits != null) {
            return (Long) this.m_errorLimits.get(string);
        }
        return null;
    }

    public Long getWarnLimit(String string) {
        if (this.m_warnLimits != null) {
            return (Long) this.m_warnLimits.get(string);
        }
        return null;
    }

    public void connect(String string, String string2, String string3, String string4) {
        this.setUserObject(string);
        this.m_url = string2;
        this.m_user = string3;
        this.m_password = string4;
        this.connect();
    }

    public void setSSLParams(Map map) {
        this.m_sslParams = map;
    }

    public synchronized boolean isStandbyMode() {
        if (this.m_serverInfo != null && this.m_serverInfo.getState() == 3) {
            return true;
        }
        return false;
    }

    public synchronized ServerInfo getJmsServerInfo(boolean bl) {
        if (!bl) {
            return this.m_serverInfo;
        }
        if (this.m_adminConn != null) {
            try {
                this.m_serverInfo = this.m_adminConn.getInfo();
                if (this.m_chartUpdateTimer != null) {
                    this.m_chartUpdateTimer.start();
                }
                this.updateErrorWarningFlags();
                this.doLogging();
            } catch (TibjmsAdminException var2_2) {
                System.err.println("JMSException: " + var2_2.toString());
                System.err.println("Disconnecting from " + this.m_url);
                this.disconnect();
            } catch (Exception var2_3) {
                System.err.println("Exception: " + var2_3.toString());
                System.err.println("Disconnecting from " + this.m_url);
                this.disconnect();
            }
        }
        return this.m_serverInfo;
    }

    public synchronized ServerInfo getJmsServerInfoUserReq(boolean bl) {
        if (!bl) {
            return this.m_serverInfo;
        }
        if (this.m_adminConn != null) {
            try {
                this.m_serverInfo = this.m_adminConn.getInfo();
                this.updateErrorWarningFlags();
            } catch (TibjmsAdminException var2_2) {
                System.err.println("JMSException: " + var2_2.toString());
                System.err.println("Disconnecting from " + this.m_url);
                this.disconnect();
            }
        }
        return this.m_serverInfo;
    }

    public void disconnect() {
        if (this.m_ssNodes != null) {
            for (int i = 0; i < this.m_ssNodes.size(); ++i) {
                GemsSSNode gemsSSNode = (GemsSSNode) this.m_ssNodes.get(i);
                gemsSSNode.destroy();
            }
            this.m_ssNodes.removeAllElements();
        }
        if (this.m_eventMonitor != null) {
            this.m_eventMonitor.systemStop();
        }
        if (this.m_services != null) {
            this.m_services.systemStop();
        }
        this.removeAllChildren();
        String string = (String) this.getUserObject();
        string = string + "(" + this.m_url + ")";
        connections.remove(string);
        if (this.m_services != null) {
            this.m_services.stopServices();
        }
        try {
            if (this.m_adminConn != null) {
                this.m_adminConn.close();
            }
            this.m_adminConn = null;
            this.m_serverInfo = null;
        } catch (TibjmsAdminException var2_4) {
            System.err.println("JMSException: " + var2_4.getMessage());
            return;
        }
    }

    public TibjmsAdmin getJmsAdmin() {
        return this.m_adminConn;
    }

    public boolean isConnected() {
        return this.m_adminConn != null;
    }

    public DefaultMutableTreeNode createQueue(String string, boolean bl) {
        if (this.m_adminConn == null) {
            return null;
        }
        try {
            Object object;
            if (!bl) {
                object = new QueueInfo(string);
                this.m_adminConn.createQueue((QueueInfo) object);
            }

            DefaultMutableTreeNode result = null;
            int n = string.indexOf(64);
            if (n > 0) {
                string = string.substring(0, n);
            }

            if (this.m_queuesNode == null) {
                return null;
            }

            if (this.m_serverInfo.getQueueCount() <= Gems.getGems().getMaxQueues() || Gems.getGems().getQueueNamePattern().length() > 1) {
                QueueInfo[] arrqueueInfo;
                this.m_queuesNode.removeAllChildren();
                try {
                    arrqueueInfo = this.m_adminConn.getQueues(Gems.getGems().getQueueNamePattern(), Gems.getGems().getPermType());
                } catch (Throwable var7_9) {
                    arrqueueInfo = this.m_adminConn.getQueues(Gems.getGems().getQueueNamePattern());
                }

                for (QueueInfo queueInfo : arrqueueInfo) {
                    GemsQueueNode queueNode;

                    try {
                        queueNode = new GemsQueueNode(queueInfo.getName(), queueInfo.isRouted());
                    } catch (Throwable var8_12) {
                        queueNode = new GemsQueueNode(queueInfo.getName());
                    }

                    if (string.equals(queueInfo.getName())) {
                        result = queueNode;
                    }

                    this.m_queuesNode.add(queueNode);
                }
            } else {
                boolean bl3 = string.indexOf(64) > 0;
                result = new GemsQueueNode(string, bl3);
                this.m_queuesNode.add(result);
            }

            return result;
        } catch (TibjmsAdminException var3_4) {
            System.err.println("JMSException: " + var3_4.getMessage());
            return null;
        }
    }

    public DefaultMutableTreeNode removeQueue(String string) {
        if (this.m_adminConn == null) {
            return null;
        }
        try {
            if (this.m_queuesNode == null) {
                return null;
            }
            this.m_adminConn.destroyQueue(string);
            Enumeration enumeration = this.m_queuesNode.children();
            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) enumeration.nextElement();
                if (defaultMutableTreeNode == null || !string.equals((String) defaultMutableTreeNode.getUserObject())) continue;
                defaultMutableTreeNode.removeFromParent();
                return this.m_queuesNode;
            }
            return this.m_queuesNode;
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return null;
        }
    }

    public void purgeQueue(String string) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            this.m_adminConn.purgeQueue(string);
        } catch (TibjmsAdminException var2_2) {
            System.err.println("JMSException: " + var2_2.getMessage());
            return;
        }
    }

    public void purgeTopic(String string) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            this.m_adminConn.purgeTopic(string);
        } catch (TibjmsAdminException var2_2) {
            System.err.println("JMSException: " + var2_2.getMessage());
            return;
        }
    }

    public void createUser(String string, String string2, String string3) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            UserInfo userInfo = new UserInfo(string, string3);
            userInfo.setPassword(string2);
            this.m_adminConn.createUser(userInfo);
        } catch (TibjmsAdminException var4_5) {
            System.err.println("JMSException: " + var4_5.getMessage());
            return;
        }
    }

    public void updateUser(String string, String string2, String string3) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            UserInfo userInfo = new UserInfo(string, string3);
            userInfo.setPassword(string2);
            this.m_adminConn.updateUser(userInfo);
        } catch (TibjmsAdminException var4_5) {
            System.err.println("JMSException: " + var4_5.getMessage());
            return;
        }
    }

    public void destroyUser(String string) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            this.m_adminConn.destroyUser(string);
        } catch (TibjmsAdminException var2_2) {
            System.err.println("JMSException: " + var2_2.getMessage());
            return;
        }
    }

    public void createGroup(String string, String string2) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            GroupInfo groupInfo = new GroupInfo(string, string2);
            this.m_adminConn.createGroup(groupInfo);
        } catch (TibjmsAdminException var3_4) {
            System.err.println("JMSException: " + var3_4.getMessage());
            return;
        }
    }

    public void addUserToGroup(String string, String string2) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            this.m_adminConn.addUserToGroup(string2, string);
        } catch (TibjmsAdminException var3_3) {
            System.err.println("JMSException: " + var3_3.getMessage());
            return;
        }
    }

    public void removeUserFromGroup(String string, String string2) {
        if (this.m_adminConn == null) {
            return;
        }
        try {
            this.m_adminConn.removeUserFromGroup(string2, string);
        } catch (TibjmsAdminException var3_3) {
            System.err.println("JMSException: " + var3_3.getMessage());
            return;
        }
    }

    public void createDurable(String string, String string2, String string3, String string4) {
        if (this.m_adminConn == null) {
            return;
        }
        if (string3 != null && string3.length() == 0) {
            string3 = null;
        }
        try {
            this.m_adminConn.createDurable(string2, string, string3, string4, false, false);
        } catch (TibjmsAdminException var5_5) {
            System.err.println("JMSException: " + var5_5.getMessage());
            return;
        }
    }

    public void destroyDurable(String string, String string2) {
        if (this.m_adminConn == null) {
            return;
        }
        if (string2 != null && string2.length() == 0) {
            string2 = null;
        }
        try {
            this.m_adminConn.destroyDurable(string, string2);
        } catch (TibjmsAdminException var3_3) {
            System.err.println("JMSException: " + var3_3.getMessage());
            return;
        }
    }

    public void purgeDurable(String string, String string2) {
        if (this.m_adminConn == null) {
            return;
        }
        if (string2 != null && string2.length() == 0) {
            string2 = null;
        }
        try {
            this.m_adminConn.purgeDurable(string, string2);
        } catch (TibjmsAdminException var3_3) {
            System.err.println("JMSException: " + var3_3.getMessage());
            return;
        }
    }

    public void destroyConnection(String string) {
        long l;
        if (this.m_adminConn == null) {
            return;
        }
        try {
            l = Long.parseLong(string);
        } catch (Exception var4_3) {
            System.err.println("Exception: " + var4_3.getMessage());
            return;
        }
        try {
            this.m_adminConn.destroyConnection(l);
        } catch (TibjmsAdminException var4_4) {
            System.err.println("JMSException: " + var4_4.getMessage());
            return;
        }
    }

    public DefaultMutableTreeNode createTopic(String string, boolean bl) {
        if (this.m_adminConn == null) {
            return null;
        }

        try {
            Object object;
            if (!bl) {
                object = new TopicInfo(string);
                this.m_adminConn.createTopic((TopicInfo) object);
            }

            if (this.m_topicsNode == null) {
                return null;
            }

            DefaultMutableTreeNode result = null;
            if (this.m_serverInfo.getTopicCount() <= Gems.getGems().getMaxTopics() || Gems.getGems().getTopicNamePattern().length() > 1) {
                TopicInfo[] arrtopicInfo;
                this.m_topicsNode.removeAllChildren();
                try {
                    arrtopicInfo = this.m_adminConn.getTopics(Gems.getGems().getTopicNamePattern(), Gems.getGems().getPermType());
                } catch (Throwable var6_7) {
                    arrtopicInfo = this.m_adminConn.getTopics(Gems.getGems().getTopicNamePattern());
                }

                for (TopicInfo topicInfo : arrtopicInfo) {
                    if (topicInfo.getName().startsWith("$sys")) continue;

                    DefaultMutableTreeNode temp = new GemsTopicNode(topicInfo.getName());
                    if (string.equals(topicInfo.getName())) {
                        result = temp;
                    }

                    this.m_topicsNode.add(temp);
                }
            } else {
                result = new GemsTopicNode(string);
                this.m_topicsNode.add(result);
            }

            return result;
        } catch (TibjmsAdminException var3_4) {
            System.err.println("JMSException: " + var3_4.getMessage());
            return null;
        }
    }

    public DefaultMutableTreeNode removeTopic(String string) {
        if (this.m_adminConn == null) {
            return null;
        }
        try {
            if (this.m_topicsNode == null) {
                return null;
            }
            this.m_adminConn.destroyTopic(string);
            Enumeration enumeration = this.m_topicsNode.children();
            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) enumeration.nextElement();
                if (defaultMutableTreeNode == null || !string.equals((String) defaultMutableTreeNode.getUserObject())) continue;
                defaultMutableTreeNode.removeFromParent();
                return this.m_topicsNode;
            }
            return this.m_topicsNode;
        } catch (TibjmsAdminException var2_3) {
            System.err.println("JMSException: " + var2_3.getMessage());
            return null;
        }
    }

    public void removeMessage(Message message) throws Exception {
        if (this.m_adminConn == null) {
            return;
        }
        this.m_adminConn.destroyMessage(message.getJMSMessageID());
    }

    public void addWarnLimits(Node node) {
        this.m_warnLimits = new Hashtable();
        NamedNodeMap namedNodeMap = node.getAttributes();
        for (int i = 0; i < namedNodeMap.getLength(); ++i) {
            Node node2 = namedNodeMap.item(i);
            this.m_warnLimits.put(node2.getNodeName(), new Long(node2.getNodeValue()));
        }
    }

    public void addErrorLimits(Node node) {
        this.m_errorLimits = new Hashtable();
        NamedNodeMap namedNodeMap = node.getAttributes();
        for (int i = 0; i < namedNodeMap.getLength(); ++i) {
            Node node2 = namedNodeMap.item(i);
            this.m_errorLimits.put(node2.getNodeName(), new Long(node2.getNodeValue()));
        }
    }

    public String getClientID(long l) {
        String string;
        if (l == 0) {
            return "";
        }
        if (this.m_adminConn == null) {
            return "";
        }
        if (this.m_clientIDs == null) {
            this.m_clientIDs = new Hashtable();
        }
        if ((string = (String) this.m_clientIDs.get(String.valueOf(l))) == null) {
            this.m_clientIDs.clear();
            try {
                ConnectionInfo[] arrconnectionInfo = this.m_adminConn.getConnections();
                for (int i = 0; arrconnectionInfo != null && i < arrconnectionInfo.length; ++i) {
                    if (arrconnectionInfo[i].getClientID() == null) continue;
                    this.m_clientIDs.put(String.valueOf(arrconnectionInfo[i].getID()), arrconnectionInfo[i].getClientID());
                }
            } catch (TibjmsAdminException var4_4) {
                System.err.println("JMSException: " + var4_4.getMessage());
                return "";
            }
            string = (String) this.m_clientIDs.get(String.valueOf(l));
        }
        if (string != null) {
            return string;
        }
        return "";
    }

    public void run() {
        while (this.m_delay > 0) {
            try {
                Thread.sleep(this.m_delay);
            } catch (Exception var1_1) {
                // empty catch block
            }
            if (!this.isConnected()) {
                this.connect();
                if (!this.isConnected()) continue;
                Gems.getGems().scheduleRepaint();
                continue;
            }
            this.getJmsServerInfo(true);
            if (this.m_sub != null) {
                this.m_sub.onData((Object) this.m_serverInfo);
            }
            if (this.isConnected()) continue;
            Gems.getGems().scheduleRepaint();
        }
    }

    public void startCharting() {
        if (this.m_sub == null) {
            Vector vector = ReflectionUtils.getGetterMethodNames(this.m_serverInfo.getClass());
            this.m_sub = new GemsSubscriber(vector, "hello");
            this.m_chart = new GemsChartFrame(Gems.getGems().getTitlePrefix() + "Real Time Chart for " + this.getName() + " (" + this.m_url + ")", vector, this.m_sub, this);
            this.m_sub.addChart(this.m_chart);
            this.m_chartUpdateTimer = new Timer(0, new ChartRefreshTimerAction());
            this.m_chartUpdateTimer.setRepeats(false);
        } else {
            this.m_chart.bringToFront();
        }
    }

    public String getName() {
        return (String) this.getUserObject();
    }

    public void stopCharting() {
        this.m_sub = null;
        this.m_chart = null;
    }

    public void addSSLParam(Node node) {
        Node node2;
        Node node3;
        NamedNodeMap namedNodeMap = node.getAttributes();
        Node node4 = namedNodeMap.getNamedItem("type");
        if (node4 == null) {
            node4 = namedNodeMap.getNamedItem("Type");
        }
        if ((node3 = namedNodeMap.getNamedItem("name")) == null) {
            node3 = namedNodeMap.getNamedItem("Name");
        }
        if ((node2 = namedNodeMap.getNamedItem("value")) == null) {
            node2 = namedNodeMap.getNamedItem("Value");
        }
        if (node4 == null || node3 == null || node2 == null) {
            return;
        }
        Gems.debug("SSLParam: " + node3.getNodeValue() + "=" + node2.getNodeValue());
        if (node4.getNodeValue().equalsIgnoreCase("string")) {
            this.m_sslParams.put(node3.getNodeValue(), node2.getNodeValue());
        } else if (node4.getNodeValue().equalsIgnoreCase("boolean")) {
            this.m_sslParams.put(node3.getNodeValue(), new Boolean(node2.getNodeValue()));
        } else if (node4.getNodeValue().equalsIgnoreCase("long")) {
            this.m_sslParams.put(node3.getNodeValue(), new Long(node2.getNodeValue()));
        } else if (node4.getNodeValue().equalsIgnoreCase("integer") || node4.getNodeValue().equalsIgnoreCase("int")) {
            this.m_sslParams.put(node3.getNodeValue(), new Integer(node2.getNodeValue()));
        }
    }

    public void addSubStation(Node node) {
        Node node2;
        NamedNodeMap namedNodeMap = node.getAttributes();
        Node node3 = namedNodeMap.getNamedItem("alias");
        if (node3 == null) {
            node3 = namedNodeMap.getNamedItem("Alias");
        }
        if ((node2 = namedNodeMap.getNamedItem("adminqueue")) == null) {
            node2 = namedNodeMap.getNamedItem("Queue");
        }
        if (node3 == null || node2 == null) {
            return;
        }
        Properties properties = new Properties();
        properties.put("QUEUE", node2.getNodeValue());
        Node node4 = namedNodeMap.getNamedItem("version");
        if (node4 == null) {
            node4 = namedNodeMap.getNamedItem("Version");
        }
        if (node4 == null) {
            properties.put("VERSION", "2.4");
        } else {
            properties.put("VERSION", node4.getNodeValue().substring(0, 3));
        }
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; nodeList != null && i < nodeList.getLength(); ++i) {
            Node node5;
            Node node6 = nodeList.item(i);
            if (!node6.getNodeName().equals("Interface")) continue;
            NamedNodeMap namedNodeMap2 = node6.getAttributes();
            Node node7 = namedNodeMap2.getNamedItem("name");
            if (node7 == null) {
                node7 = namedNodeMap2.getNamedItem("Name");
            }
            if ((node5 = namedNodeMap2.getNamedItem("type")) == null) {
                node5 = namedNodeMap2.getNamedItem("Type");
            }
            if (node5 == null) {
                properties.put(node7.getNodeValue(), "INT");
                continue;
            }
            if (node5.getNodeValue().equals("IMS")) {
                properties.put(node7.getNodeValue(), "IMS");
                continue;
            }
            properties.put(node7.getNodeValue(), "INT");
        }
        this.m_SSSystems.put(node3.getNodeValue(), properties);
    }

    public void doLogging() {
        if (this.m_adminConn != null && this.m_logServerInfo != LOG_TYPE.Never && (this.m_logServerInfo == LOG_TYPE.Always || this.m_isInWarning && this.m_logServerInfo == LOG_TYPE.WarnLimits || this.m_isInError && (this.m_logServerInfo == LOG_TYPE.ErrorLimits || this.m_logServerInfo == LOG_TYPE.WarnLimits))) {
            int n;
            Object object;
            Date date = new Date();
            try {
                object = this.m_fileDateFormat.format(date).toString() + ".csv";
                if (this.m_logFile == null || !this.m_logFile.getName().endsWith((String) object)) {
                    if (this.m_filePrint != null) {
                        this.m_filePrint.close();
                    }
                    if (this.m_fileOut != null) {
                        this.m_fileOut.close();
                    }
                    File file = new File(this.m_logDir);
                    file.mkdirs();
                    this.m_logFile = new File(file, StringUtilities.stripSpaces(this.getName()) + "-" + (String) object);
                    this.m_fileOut = new FileOutputStream(this.m_logFile, true);
                    this.m_filePrint = new PrintStream(this.m_fileOut);
                    if (this.m_logFile.exists()) {
                        StringBuffer stringBuffer = new StringBuffer("timestamp");
                        stringBuffer.append(",");
                        for (int i = 0; i < this.m_logColumnNames.size(); ++i) {
                            stringBuffer.append(this.m_logColumnNames.get(i));
                            if (i + 1 >= this.m_logColumnNames.size()) continue;
                            stringBuffer.append(",");
                        }
                        this.m_filePrint.println(stringBuffer.toString());
                    }
                }
            } catch (IOException var2_3) {
                System.err.println("Error: Failed to create ServerInfo logfile: JavaIOException: " + var2_3.getMessage());
                this.m_logServerInfo = LOG_TYPE.Never;
                this.m_logFile = null;
                this.m_fileOut = null;
                this.m_filePrint = null;
                return;
            }
            if (this.m_filePrint != null) {
                Hashtable hashtable = ReflectionUtils.getGetterMethodValues(this.m_serverInfo);
                StringBuffer stringBuffer = new StringBuffer(this.dateFormatMillis.format(date).toString());
                stringBuffer.append(",");
                for (n = 0; n < this.m_logColumnNames.size(); ++n) {
                    stringBuffer.append(hashtable.get(this.m_logColumnNames.get(n)));
                    if (n + 1 >= this.m_logColumnNames.size()) continue;
                    stringBuffer.append(",");
                }
                this.m_filePrint.println(stringBuffer.toString());
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum LOG_TYPE {
        Never,
        WarnLimits,
        ErrorLimits,
        Always;


        private LOG_TYPE() {
        }
    }

    class ChartRefreshTimerAction
            implements ActionListener {
        ChartRefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsConnectionNode.this.m_sub != null) {
                GemsConnectionNode.this.m_sub.onData((Object) GemsConnectionNode.this.m_serverInfo);
            }
        }
    }

}

