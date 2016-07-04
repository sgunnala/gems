/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.Tibjms
 *  com.tibco.tibjms.admin.ConnectionFactoryInfo
 *  com.tibco.tibjms.admin.QueueInfo
 *  com.tibco.tibjms.admin.RouteInfo
 *  com.tibco.tibjms.admin.ServerInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 *  com.tibco.tibjms.admin.TopicInfo
 *  com.tibco.tibjms.version
 *  javax.jms.Message
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.Message;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tibco.tibjms.Tibjms;
import com.tibco.tibjms.admin.ConnectionFactoryInfo;
import com.tibco.tibjms.admin.RouteInfo;
import com.tibco.tibjms.admin.ServerInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;
import com.tibco.tibjms.version;

import static javax.swing.JOptionPane.YES_OPTION;

public class Gems {
    static Gems m_gems;
    final String m_version = "Gems v3.4";
    final String p_maxMessageView = "MaxMessageView";
    final String p_viewOldMessagesFirst = "ViewOldMessagesFirst";
    final String p_queueNamePattern = "QueueNamePattern";
    final String p_topicNamePattern = "TopicNamePattern";
    final String p_showExtendedProperties = "ShowExtendedProperties";
    final String p_displayRefresh = "DisplayRefresh";
    final String p_showMonitorTopics = "ShowMonitorTopics";
    final String p_showRootNode = "ShowRootNode";
    final String p_screenWidth = "DisplayWidth";
    final String p_screenHeight = "DisplayHeight";
    final String p_msgReadDelay = "MsgReadDelay";
    final String p_requestReplyTimeout = "RequestReplyTimeout";
    final String p_colourPendingMsgs = "ColourPendingMsgs";
    final String p_serverConfigFile = "ServerConfigFile";
    final String p_viewOnlyMode = "ViewOnlyMode";
    final String p_lookAndFeel = "LookAndFeel";
    final String p_permType = "PermType";
    final String p_debug = "Debug";
    final String p_showTotals = "ShowTotals";
    final String p_maxDisplayBytes = "MaxDisplayBytes";
    final String p_showClientId = "ShowClientId";
    final String p_hideViews = "HideViews";
    final String p_connectTimeout = "ConnectTimeout";
    final String p_SSTimeout = "SSTimeout";
    final String p_allowStandbyOperations = "AllowStandbyOperations";
    final String p_adminTimeout = "AdminTimeout";
    final String p_useServerTimestamps = "UseServerTimestamps";
    final String p_showPathInTitleBar = "ShowPathInTitleBar";
    final String p_showRootInTitleBar = "ShowRootInTitleBar";
    final String p_detailPaneColWidths = "DetailPanelColWidths";
    final String p_logDateTimeFormat = "LogDateTimeFormat";
    final String p_CSVFileDelimiter = "CSVFileDelimiter";
    final String p_maxQueues = "MaxQueues";
    final String p_maxTopics = "MaxTopics";
    final String p_serverInfoColPositions = "ServerInfoColPositions";
    public ImageIcon m_icon = null;
    public boolean m_useMetalIcons = true;
    public int m_displayRefresh = 0;
    protected JTree m_tree;
    protected JTabbedPane m_tabPane;
    protected GemsTreeModel m_treeModel;
    protected GemsServerMonitorPanel m_servMonitorPanel;
    protected GemsServerInfoPanel m_servPanel;
    protected GemsConnectionPanel m_connPanel;
    protected GemsDetailsPanel m_detailsPanel;
    protected GemsMessagePanel m_messagePanel;
    protected int m_lastSelectedTab = 0;
    protected Properties m_props;
    protected boolean m_lock = false;
    protected boolean m_serviceMenu = false;
    protected boolean m_substationMenu = false;
    protected boolean m_showEvents = false;
    protected JCheckBoxMenuItem m_autoRefresh = null;
    protected JCheckBoxMenuItem m_showTotals = null;
    protected Timer m_timer = null;
    protected Mutex m_xlock = new Mutex();
    String m_jreVersion = null;
    JFrame m_frame;
    JMenuBar m_menuBar;

    public Gems(String[] arrstring) {
        if (arrstring.length > 0) {
            this.initProps(arrstring[0]);
        } else {
            this.initProps(null);
        }
    }

    public void initProps(String string) {
        this.m_props = new Properties();
        this.m_props.setProperty("MaxMessageView", "100");
        this.m_props.setProperty("ViewOldMessagesFirst", "false");
        this.m_props.setProperty("QueueNamePattern", ">");
        this.m_props.setProperty("TopicNamePattern", ">");
        this.m_props.setProperty("ShowExtendedProperties", "false");
        this.m_props.setProperty("DisplayRefresh", "20");
        this.m_props.setProperty("ShowMonitorTopics", "false");
        this.m_props.setProperty("DisplayWidth", "1200");
        this.m_props.setProperty("DisplayHeight", "600");
        this.m_props.setProperty("ShowRootNode", "true");
        this.m_props.setProperty("ColourPendingMsgs", "true");
        this.m_props.setProperty("MsgReadDelay", "250");
        this.m_props.setProperty("RequestReplyTimeout", "10");
        this.m_props.setProperty("ServerConfigFile", "servers.xml");
        this.m_props.setProperty("ViewOnlyMode", "true");
        this.m_props.setProperty("Debug", "false");
        this.m_props.setProperty("LookAndFeel", "");
        this.m_props.setProperty("PermType", "3");
        this.m_props.setProperty("MaxDisplayBytes", "1024");
        this.m_props.setProperty("ShowClientId", "false");
        this.m_props.setProperty("HideViews", "Clients");
        this.m_props.setProperty("ShowTotals", "false");
        this.m_props.setProperty("ConnectTimeout", "500");
        this.m_props.setProperty("SSTimeout", "5000");
        this.m_props.setProperty("AdminTimeout", "5000");
        this.m_props.setProperty("AllowStandbyOperations", "false");
        this.m_props.setProperty("UseServerTimestamps", "false");
        this.m_props.setProperty("ShowRootInTitleBar", "false");
        this.m_props.setProperty("ShowPathInTitleBar", "true");
        this.m_props.setProperty("DetailPanelColWidths", "");
        this.m_props.setProperty("LogDateTimeFormat", "EEE MMM dd HH:mm:ss SSS zzz yyyy");
        this.m_props.setProperty("CSVFileDelimiter", ",");
        this.m_props.setProperty("MaxQueues", "1000");
        this.m_props.setProperty("MaxTopics", "1000");
        this.m_props.setProperty("ServerInfoColPositions", "");
        if (string != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(string);
                this.m_props.load(fileInputStream);
            } catch (Exception var2_3) {
                System.err.println("Exception: " + var2_3.getMessage());
                return;
            }
        }
        this.m_displayRefresh = this.getDisplayRefresh();
    }

    public int getDisplayRefresh() {
        try {
            return Integer.parseInt(this.m_props.getProperty("DisplayRefresh"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 20;
        }
    }

    public static void main(String[] arrstring) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Unable to set Nimbus L&F");
        }

        m_gems = new Gems(arrstring);
        m_gems.start();
    }

    private static Box hBox(Object... components) {
        Box hBox = Box.createHorizontalBox();

        fillBox(hBox, components, true);

        return hBox;
    }

    private static void fillBox(Box hBox, Object[] components, boolean horizontal) {
        for (Object component : components) {
            if (component instanceof Number) {
                int size = ((Number) component).intValue();
                hBox.add(horizontal ? Box.createHorizontalStrut(size) : Box.createVerticalStrut(size));
            } else if (component instanceof String) {
                hBox.add(new JLabel(component.toString()));
            } else if (component instanceof Component) {
                hBox.add((Component) component);
            }
        }
    }

    private static Box vBox(Object... components) {
        Box vBox = Box.createVerticalBox();

        fillBox(vBox, components, false);

        return vBox;
    }

    public void start() {
        this.m_jreVersion = System.getProperty("java.version");
        Gems.debug("JRE Version = " + this.m_jreVersion);
        System.err.print("TIBCO Gems v3.4");
        version.main((String[]) null);
        Gems.debug("Default socketConnectionTimeout: " + String.valueOf(Tibjms.getSocketConnectTimeout()));
        Tibjms.setSocketConnectTimeout((long) this.getConnectTimeout());
        Gems.debug("Setting socketConnectionTimeout: " + String.valueOf(this.getConnectTimeout()));
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new TCPopupEventQueue());
        this.buildFrame();
        this.treeSelectionChange(this.getSelectedNode(), false);

        if (this.m_displayRefresh > 0) {
            this.m_timer = new Timer(this.m_displayRefresh * 1000 + 1000, new RefreshTimerAction());
            this.m_timer.start();
            this.m_timer.setInitialDelay(200);
        }

        if (this.m_displayRefresh / 5 < 1) {
        }

        ConnectThread connectThread = new ConnectThread(this.m_displayRefresh);
        connectThread.start();
    }

    public void buildFrame() {
        JSplitPane splitPane;
        Object object2;

        JTabbedPane hashtableTabbedPane;
        JComponent object3Component;
        JPanel object4Panel;

        object3Component = this.buildTreeViewFromFile(this.getServerConfigFile());
        if (object3Component == null) {
            object3Component = this.buildTreeView();
        }
        hashtableTabbedPane = this.buildTabbedView();
        this.m_menuBar = this.constructMenuBar();
        object4Panel = new JPanel(true);
        object2 = this.getTitlePrefix() + "Gems v3.4";
        if (this.getViewOnlyMode()) {
            object2 = (String) object2 + " (view only)";
        }
        this.m_frame = new JFrame((String) object2);
        this.m_frame.getContentPane().add("Center", object4Panel);
        this.m_frame.setJMenuBar(this.m_menuBar);
        this.m_frame.setBackground(Color.lightGray);
        splitPane = new JSplitPane(1, object3Component, hashtableTabbedPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        object3Component.setMinimumSize(new Dimension(100, 50));
        hashtableTabbedPane.setMinimumSize(new Dimension(100, 50));
        object4Panel.setLayout(new BorderLayout());
        object4Panel.add("Center", splitPane);
        this.m_frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        this.m_tree.addTreeSelectionListener(this.m_treeModel);
        this.m_frame.setSize(new Dimension(this.getScreenWidth(), this.getScreenHeight()));
        String string = "/images/gem.jpg";
        URL uRL = this.getClass().getResource(string);
        this.m_icon = uRL != null ? new ImageIcon(uRL) : new ImageIcon("gem.jpg");
        if (this.m_icon != null) {
            this.m_frame.setIconImage(this.m_icon.getImage());
        }
        this.m_frame.setVisible(true);
    }

    public JComponent buildTreeView() {
        IconNode iconNode = new IconNode("EMS-Servers");
        String string = "/images/gem16.gif";
        URL uRL = this.getClass().getResource(string);
        if (uRL != null) {
            ImageIcon imageIcon = new ImageIcon(uRL);
            if (imageIcon.getImageLoadStatus() == 8) {
                iconNode.setIcon(imageIcon);
            }
        } else {
            ImageIcon imageIcon = new ImageIcon("gem16.jpg");
            if (imageIcon.getImageLoadStatus() == 8) {
                iconNode.setIcon(imageIcon);
            }
        }
        this.m_treeModel = new GemsTreeModel(iconNode);
        this.m_treeModel.m_tree = this.m_tree = new JTree(this.m_treeModel);
        this.m_treeModel.m_gems = this;
        this.m_tree.setCellRenderer(new IconNodeRenderer());
        this.m_tree.putClientProperty("JTree.icons", this.makeIcons());
        this.m_tree.setRowHeight(-1);
        this.m_tree.getSelectionModel().setSelectionMode(1);
        this.m_tree.expandRow(0);
        this.m_treeModel.newJMSConnection("EMS Connection");
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(this.m_tree);
        return jScrollPane;
    }

    public JComponent buildTreeViewFromFile(String string) {
        DocumentBuilder documentBuilder;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException var4_4) {
            System.err.println("Exception: " + var4_4.getMessage());
            return null;
        }
        try {
            Object object;
            Object object2;
            Document document = documentBuilder.parse(new File(string));
            Element element = document.getDocumentElement();
            IconNode iconNode = new IconNode(element.getNodeName());
            String string2 = element.getAttribute("icon");
            if (string2 != null && string2.length() > 0) {
                ImageIcon imageIcon = new ImageIcon(string2);
                if (imageIcon.getImageLoadStatus() == 8) {
                    iconNode.setIcon(imageIcon);
                }
            } else {
                ImageIcon imageIcon = null;
                URL url = this.getClass().getResource("/images/gem16.jpg");
                if (url != null) {
                    imageIcon = new ImageIcon(url);
                    if (imageIcon.getImageLoadStatus() == 8) {
                        iconNode.setIcon(imageIcon);
                    }
                } else {
                    imageIcon = new ImageIcon("gem16.jpg");
                    if (imageIcon.getImageLoadStatus() == 8) {
                        iconNode.setIcon(imageIcon);
                    }
                }
            }
            this.m_treeModel = new GemsTreeModel(iconNode);
            this.m_treeModel.m_tree = this.m_tree = new JTree(this.m_treeModel);
            this.m_treeModel.m_gems = this;
            this.m_tree.setCellRenderer(new IconNodeRenderer());
            this.m_tree.putClientProperty("JTree.icons", this.makeIcons());
            this.m_tree.setRowHeight(-1);
            this.m_tree.getSelectionModel().setSelectionMode(1);
            this.m_tree.setRootVisible(this.getShowRootNode());
            object2 = this.buildNodes(element, iconNode);
            this.m_tree.expandPath(new TreePath(iconNode.getPath()));
            Enumeration enumeration = iconNode.children();
            DefaultMutableTreeNode serializable2Node;
            if (enumeration.hasMoreElements() && (serializable2Node = (DefaultMutableTreeNode) enumeration.nextElement()) != null) {
                this.m_tree.expandPath(new TreePath(serializable2Node.getPath()));
            }
            this.m_tree.setSelectionPath(new TreePath(iconNode.getPath()));
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.getViewport().add(this.m_tree);
            return scrollPane;
        } catch (Exception var4_6) {
            System.err.println("Exception: " + var4_6.getMessage());
            return null;
        }
    }

    public DefaultMutableTreeNode buildNodes(Node node, DefaultMutableTreeNode defaultMutableTreeNode) {
        DefaultMutableTreeNode root;
        block42:
        {
            root = null;
            try {
                NodeList nodeList = node.getChildNodes();
                if (nodeList == null) break block42;
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Node node2 = nodeList.item(i);
                    if (node2.getNodeType() != 1) continue;
                    if (node2.getNodeName().equals("ConnectionNode")) {
                        String connectionName = ((Element) node2).getAttribute("alias");
                        if (connectionName == null || connectionName.length() == 0) {
                            connectionName = ((Element) node2).getAttribute("name");
                        }

                        String urlAttribute = ((Element) node2).getAttribute("url");
                        String userAttribute = ((Element) node2).getAttribute("user");
                        String passwordAttribute = ((Element) node2).getAttribute("password");
                        String autoConnectAttribute = ((Element) node2).getAttribute("autoConnect");
                        String logDirAttribute = ((Element) node2).getAttribute("logDir");
                        String logServerInfoAttribute = ((Element) node2).getAttribute("logServerInfo");

                        GemsConnectionNode.LOG_TYPE logType;
                        if (logServerInfoAttribute != null && logServerInfoAttribute.length() > 0) {
                            if (logServerInfoAttribute.equals(GemsConnectionNode.LOG_TYPE.Never.toString())) {
                                logType = GemsConnectionNode.LOG_TYPE.Never;
                            } else if (logServerInfoAttribute.equals(GemsConnectionNode.LOG_TYPE.WarnLimits.toString())) {
                                logType = GemsConnectionNode.LOG_TYPE.WarnLimits;
                            } else if (logServerInfoAttribute.equals(GemsConnectionNode.LOG_TYPE.ErrorLimits.toString())) {
                                logType = GemsConnectionNode.LOG_TYPE.ErrorLimits;
                            } else if (logServerInfoAttribute.equals(GemsConnectionNode.LOG_TYPE.Always.toString())) {
                                logType = GemsConnectionNode.LOG_TYPE.Always;
                            } else {
                                System.err.println("Error: Illegal logServerInfo attribute: " + logServerInfoAttribute + " for " + connectionName + " must be one of: Never, WarnLimits, ErrorLimits, Always. Using WarnLimits");
                                logType = GemsConnectionNode.LOG_TYPE.WarnLimits;
                            }
                        } else {
                            System.err.println("Warning: No logServerInfo attribute specified for " + connectionName + " logging of ServerInfo will be disabled (see servers.xml for details)");
                            logType = GemsConnectionNode.LOG_TYPE.Never;
                        }

                        boolean autoConnect = true;
                        if (autoConnectAttribute != null && autoConnectAttribute.length() > 0) {
                            autoConnect = Boolean.valueOf(autoConnectAttribute);
                        }

                        if (connectionName == null) {
                            connectionName = "EMS Connection";
                        }

                        if (urlAttribute == null) {
                            urlAttribute = "tcp://localhost:7222";
                        }

                        if (userAttribute == null) {
                            connectionName = "admin";
                        }

                        if (logDirAttribute == null) {
                            logDirAttribute = "./log";
                        }

                        ImageIcon imageIcon;
                        GemsConnectionNode gemsConnectionNode = new GemsConnectionNode(connectionName, urlAttribute, userAttribute,
                                passwordAttribute, 15, autoConnect, logType, logDirAttribute);
                        String iconAttribute = ((Element) node2).getAttribute("icon");
                        if (iconAttribute != null && (imageIcon = new ImageIcon(iconAttribute)).getImageLoadStatus() == 8) {
                            gemsConnectionNode.setIcon(imageIcon);
                        }

                        defaultMutableTreeNode.add(gemsConnectionNode);
                        NodeList object4Nodes = node2.getChildNodes();
                        for (int j = 0; object4Nodes != null && j < object4Nodes.getLength(); ++j) {
                            Node node3 = object4Nodes.item(j);
                            if (node3.getNodeName().equals("WarnLimits")) {
                                gemsConnectionNode.addWarnLimits(node3);
                                continue;
                            }

                            if (node3.getNodeName().equals("ErrorLimits")) {
                                gemsConnectionNode.addErrorLimits(node3);
                                continue;
                            }

                            if (node3.getNodeName().equals("SSLParam")) {
                                gemsConnectionNode.addSSLParam(node3);
                                continue;
                            }

                            if (node3.getNodeName().equals("EventMonitor")) {
                                String maxDisplayedEventsAttribute = ((Element) node3).getAttribute("maxDisplayedEvents");
                                long period = 100;
                                try {
                                    period = Long.parseLong(maxDisplayedEventsAttribute);
                                } catch (Exception var24_25) {
                                    System.err.println("Exception: " + var24_25.getMessage());
                                }

                                boolean enabled = false;
                                String enabledAttribute = ((Element) node3).getAttribute("enabled");
                                if (enabledAttribute != null && enabledAttribute.length() > 0) {
                                    enabled = Boolean.valueOf(enabledAttribute);
                                }

                                gemsConnectionNode.addEventMonitor(period, enabled);
                                this.m_showEvents = true;
                                NodeList nodeList2 = node3.getChildNodes();
                                for (int n = 0; nodeList2 != null && n < nodeList2.getLength(); n += 1) {
                                    Node node1 = nodeList2.item(n);
                                    if (!node1.getNodeName().equals("EventSubscription")) continue;
                                    String monitorTopicAttribute = ((Element) node1).getAttribute("monitorTopic");
                                    String selectorAttribute = ((Element) node1).getAttribute("selector");
                                    if (monitorTopicAttribute == null) continue;
                                    gemsConnectionNode.addEventSubscription(monitorTopicAttribute, selectorAttribute);
                                }
                                continue;
                            }

                            if (node3.getNodeName().equals("ServiceMonitor")) {
                                String periodAttribute = ((Element) node3).getAttribute("period");
                                long period = 60;
                                try {
                                    period = Long.parseLong(periodAttribute);
                                } catch (Exception ex) {
                                    System.err.println("Exception: " + ex.getMessage());
                                }

                                boolean tmpQueueReplies = false;
                                String tmpQueueRepliesAttribute = ((Element) node3).getAttribute("tmpQueueReplies");
                                if (tmpQueueRepliesAttribute != null && tmpQueueRepliesAttribute.length() > 0) {
                                    tmpQueueReplies = Boolean.valueOf(tmpQueueRepliesAttribute);
                                }

                                boolean tmpTopicReplies = false;
                                String tmpTopicRepliesAttribute = ((Element) node3).getAttribute("tmpTopicRepliesAttribute");
                                if (tmpTopicRepliesAttribute != null && tmpTopicRepliesAttribute.length() > 0) {
                                    tmpTopicReplies = Boolean.valueOf(tmpTopicRepliesAttribute);
                                }

                                boolean enabled = false;
                                String enabledAttribute = ((Element) node3).getAttribute("enabledAttribute");
                                if (enabledAttribute != null && enabledAttribute.length() > 0) {
                                    enabled = Boolean.valueOf(enabledAttribute);
                                }

                                gemsConnectionNode.addServiceTable(period, tmpQueueReplies, tmpTopicReplies, enabled);
                                this.m_serviceMenu = true;
                                NodeList object5List = node3.getChildNodes();
                                for (int k = 0; object5List != null && k < object5List.getLength(); ++k) {
                                    Node node4 = object5List.item(k);
                                    if (!node4.getNodeName().equals("Service")) continue;
                                    String nameAttribute = ((Element) node4).getAttribute("name");
                                    String requestQueueAttribute = ((Element) node4).getAttribute("requestQueue");
                                    String requestTopicAttribute = ((Element) node4).getAttribute("requestTopic");
                                    String replyQueueAttribute = ((Element) node4).getAttribute("replyQueue");
                                    String replyTopicAttribute = ((Element) node4).getAttribute("replyTopic");

                                    long respLimit = 1000;
                                    String responseTimeLimitAttribute = ((Element) node4).getAttribute("responseTimeLimit");
                                    if (responseTimeLimitAttribute != null && responseTimeLimitAttribute.length() > 0) {
                                        try {
                                            respLimit = Long.parseLong(responseTimeLimitAttribute);
                                        } catch (Exception ex) {
                                            System.err.println("Exception: " + ex.getMessage());
                                        }
                                    }

                                    if (nameAttribute == null || requestQueueAttribute == null && requestTopicAttribute == null) {
                                        continue;
                                    }

                                    gemsConnectionNode.addService(
                                            nameAttribute,
                                            requestQueueAttribute != null ? requestQueueAttribute : requestTopicAttribute,
                                            requestQueueAttribute != null,
                                            replyQueueAttribute != null ? replyQueueAttribute : replyTopicAttribute,
                                            replyQueueAttribute != null, respLimit);
                                }

                                continue;
                            }

                            if (!node3.getNodeName().equals("SS-Node")) continue;
                            gemsConnectionNode.addSubStation(node3);
                            this.m_substationMenu = true;
                        }

                        if (root == null) {
                            root = gemsConnectionNode;
                        }

                        if (!autoConnect) continue;
                        gemsConnectionNode.connect();
                        continue;
                    }
                    IconNode object3Node = new IconNode(node2.getNodeName());
                    String iconUrlAttribute = ((Element) node2).getAttribute("icon");

                    ImageIcon object2Icon = null;
                    if (iconUrlAttribute != null && (object2Icon = new ImageIcon(iconUrlAttribute)).getImageLoadStatus() == 8) {
                        object3Node.setIcon(object2Icon);
                    }
                    defaultMutableTreeNode.add(object3Node);
                    DefaultMutableTreeNode rootTemp = this.buildNodes(node2, object3Node);
                    if (root != null) continue;
                    root = rootTemp;
                }
            } catch (Exception var4_5) {
                System.err.println("Exception: " + var4_5.getMessage());
                var4_5.printStackTrace();
                return null;
            }
        }
        return root;
    }

    public GemsTreeModel getTreeModel() {
        return this.m_treeModel;
    }

    public JTabbedPane buildTabbedView() {
        this.m_tabPane = new JTabbedPane();
        this.m_connPanel = new GemsConnectionPanel(this.m_treeModel);
        this.m_servPanel = new GemsServerInfoPanel();
        this.m_servMonitorPanel = new GemsServerMonitorPanel();
        this.m_detailsPanel = new GemsDetailsPanel();
        this.m_messagePanel = new GemsMessagePanel();
        this.m_tabPane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane jTabbedPane = (JTabbedPane) changeEvent.getSource();
                int n = jTabbedPane.getSelectedIndex();
                if (n > 0) {
                    Gems.this.m_lastSelectedTab = n;
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        });
        return this.m_tabPane;
    }

    public String getProp(String string) {
        return this.m_props.getProperty(string);
    }

    public String getServerConfigFile() {
        return this.m_props.getProperty("ServerConfigFile");
    }

    public String getLookAndFeel() {
        return this.m_props.getProperty("LookAndFeel");
    }

    public String getHideViews() {
        return this.m_props.getProperty("HideViews");
    }

    public String getDetailPaneColWidths() {
        return this.m_props.getProperty("DetailPanelColWidths");
    }

    public String getLogDateTimeFormat() {
        return this.m_props.getProperty("LogDateTimeFormat");
    }

    public int getMaxDisplayBytes() {
        try {
            return Integer.parseInt(this.m_props.getProperty("MaxDisplayBytes"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 1024;
        }
    }

    public int getScreenWidth() {
        try {
            return Integer.parseInt(this.m_props.getProperty("DisplayWidth"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 1200;
        }
    }

    public int getScreenHeight() {
        try {
            return Integer.parseInt(this.m_props.getProperty("DisplayHeight"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 600;
        }
    }

    public int getMaxMessageView() {
        try {
            return Integer.parseInt(this.m_props.getProperty("MaxMessageView"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 100;
        }
    }

    public long getConnectTimeout() {
        try {
            return Long.parseLong(this.m_props.getProperty("ConnectTimeout"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 500;
        }
    }

    public long getSSTimeout() {
        try {
            return Long.parseLong(this.m_props.getProperty("SSTimeout"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 5000;
        }
    }

    public long getAdminTimeout() {
        try {
            return Long.parseLong(this.m_props.getProperty("AdminTimeout"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 5000;
        }
    }

    public boolean getViewOldMessagesFirst() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ViewOldMessagesFirst"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public void setViewOldMessagesFirst(boolean bl) {
        if (bl) {
            this.m_props.setProperty("ViewOldMessagesFirst", "true");
        } else {
            this.m_props.setProperty("ViewOldMessagesFirst", "false");
        }
    }

    public boolean getUseServerTimestamps() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("UseServerTimestamps"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public void setUseServerTimestamps(boolean bl) {
        if (bl) {
            this.m_props.setProperty("UseServerTimestamps", "true");
        } else {
            this.m_props.setProperty("UseServerTimestamps", "false");
        }
    }

    public boolean getShowTotals() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ShowTotals"));
        } catch (Exception var1_1) {
            return false;
        }
    }

    public void setShowTotals(boolean bl) {
        if (bl) {
            this.m_props.setProperty("ShowTotals", "true");
        } else {
            this.m_props.setProperty("ShowTotals", "false");
        }
        this.m_showTotals.setState(bl);
        this.treeSelectionChange(this.getSelectedNode(), false);
    }

    public void treeSelectionChange(DefaultMutableTreeNode defaultMutableTreeNode, boolean bl) {
        if (defaultMutableTreeNode != null && defaultMutableTreeNode.isRoot()) {
            this.m_autoRefresh.setState(true);
        }
        this.treeSelectionChange1(defaultMutableTreeNode, bl);
    }

    protected DefaultMutableTreeNode getSelectedNode() {
        TreePath treePath = this.m_tree.getSelectionPath();
        if (treePath != null) {
            return (DefaultMutableTreeNode) treePath.getLastPathComponent();
        }
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public synchronized void treeSelectionChange1(DefaultMutableTreeNode selectedNode, boolean var2_2) {
        try {
            if (this.m_lock) {
                return;
            }

            this.m_lock = true;
            this.m_xlock.acquire();
            this.setTitle();
            int selectedIndex = this.m_tabPane.getSelectedIndex();
            int level = 1;

            GemsConnectionNode connectionNode = null;
            for (DefaultMutableTreeNode node = selectedNode; node != null; ++level, node = (DefaultMutableTreeNode) node.getParent()) {
                if (!(node instanceof GemsConnectionNode)) continue;
                connectionNode = (GemsConnectionNode) node;
                break;
            }

            if (connectionNode == null) {
                while (this.m_tabPane.getTabCount() > 1) {
                    this.m_tabPane.removeTabAt(this.m_tabPane.getTabCount() - 1);
                }

                if (this.m_tabPane.getTabCount() == 1) {
                    if (!this.m_tabPane.getTitleAt(0).equals("Server Monitor")) {
                        this.m_tabPane.removeTabAt(0);
                        this.m_tabPane.addTab("Server Monitor", this.m_servMonitorPanel);
                    }
                } else {
                    this.m_tabPane.addTab("Server Monitor", this.m_servMonitorPanel);
                }

                if (selectedNode != null && selectedNode.getParent() == null && selectedNode.getChildCount() == 0) {
                    this.m_treeModel.reload();
                    this.m_lock = false;
                    this.m_xlock.release();
                    return;
                }

                if (selectedNode == null) {
                    selectedNode = (DefaultMutableTreeNode) this.m_treeModel.getRoot();
                }

                if (this.m_servMonitorPanel.getModel().populateTable(selectedNode, var2_2, this.m_showEvents)) {
                    this.m_treeModel.reload();
                    if (this.m_tabPane.getTabCount() == 1) {
                        this.m_tabPane.removeTabAt(0);
                    }
                }

                this.m_lock = false;
                this.m_xlock.release();
                return;
            }

            if (this.m_tabPane.getTabCount() == 0) {
                this.m_tabPane.addTab("Connection", this.m_connPanel);
                selectedIndex = 0;
            } else if (!this.m_tabPane.getTitleAt(0).equals("Connection")) {
                this.m_tabPane.removeTabAt(0);
                this.m_tabPane.insertTab("Connection", null, this.m_connPanel, null, 0);
                selectedIndex = 0;
            }

            if (!var2_2) {
                this.m_connPanel.setDetails((String) connectionNode.getUserObject(), connectionNode.m_url, connectionNode.m_user, connectionNode.isConnected());
            }

            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == 1) {
                this.m_tabPane.addTab("Server Info", this.m_servPanel);
                selectedIndex = 1;
                if (this.m_showEvents) {
                    this.m_tabPane.addTab("Event Monitor", this.m_messagePanel);
                    if (connectionNode.showEvents()) {
                        selectedIndex = 2;
                    }
                }
                this.m_tabPane.setSelectedIndex(selectedIndex);
            }

            if (!connectionNode.isConnected()) {
                while (this.m_tabPane.getTabCount() > 1) {
                    this.m_tabPane.removeTabAt(this.m_tabPane.getTabCount() - 1);
                }
            }

            if (selectedIndex >= 1 && this.m_tabPane.getTabCount() > 1) {
                this.m_servPanel.getModel().populateTable(connectionNode.getJmsServerInfoUserReq(!var2_2));
            }

            if (selectedIndex >= 2 && this.m_tabPane.getTabCount() > 2 && this.m_showEvents) {
                this.m_messagePanel.getModel().populateEventMessageInfo(connectionNode.getEventMessages());
            }

            if (selectedNode == null) {
                this.m_lock = false;
                this.m_xlock.release();
                return;
            }

            String selectedUserObject = (String) selectedNode.getUserObject();

            int tabIndex = 2;
            if (this.m_showEvents) {
                tabIndex = 3;
            }

            if (level == 1) {
                while (this.m_tabPane.getTabCount() > tabIndex) {
                    this.m_tabPane.removeTabAt(this.m_tabPane.getTabCount() - 1);
                }
            } else {
                GemsDetailsTableModel detailsTableModel = this.m_detailsPanel.getModel();

                if (level == 2) {
                    while (this.m_tabPane.getTabCount() > tabIndex + 1) {
                        this.m_tabPane.removeTabAt(this.m_tabPane.getTabCount() - 1);
                    }
                    this.m_frame.setCursor(Cursor.getPredefinedCursor(3));

                    if (selectedUserObject.equals("Consumers")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("Consumer Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }

                        this.m_tabPane.setTitleAt(tabIndex, "Consumer Info");

                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateConsumerInfo(connectionNode);
                        }
                    } else if (selectedUserObject.equals("Producers")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("Producer Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }
                        this.m_tabPane.setTitleAt(tabIndex, "Producer Info");
                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateProducerInfo(connectionNode);
                        }
                    } else if (selectedUserObject.equals("Users")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("User Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }
                        this.m_tabPane.setTitleAt(tabIndex, "User Info");
                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateUserInfo(connectionNode.getJmsAdmin());
                        }
                    } else if (selectedUserObject.equals("Clients")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("Client Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }
                        this.m_tabPane.setTitleAt(tabIndex, "Client Info");
                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateClientsInfo(connectionNode);
                        }
                    } else if (selectedUserObject.equals("Connections(Client)")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("Client Connection Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }
                        this.m_tabPane.setTitleAt(tabIndex, "Client Connection Info");
                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateConnectionInfo(connectionNode.getJmsAdmin());
                        }
                    } else if (selectedUserObject.equals("Connections(System)")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("System Connection Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }
                        this.m_tabPane.setTitleAt(tabIndex, "System Connection Info");
                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateSystemConnectionInfo(connectionNode.getJmsAdmin());
                        }
                    } else {
                        ServerInfo serverInfo = connectionNode.getJmsServerInfo(false);

                        if (selectedUserObject.startsWith("Topics")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Topics Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            try {
                                this.m_tabPane.setTitleAt(tabIndex, "Topics Info");
                                if (selectedIndex < tabIndex) System.out.println("**GOTO lbl447");

                                if (serverInfo == null) {
                                    detailsTableModel.populateErrorInfo("Not connected to EMS server");
                                } else {
                                    if (serverInfo.getTopicCount() > Gems.getGems().getMaxTopics() && Gems.getGems().getTopicNamePattern().length() <= 1) {
                                        detailsTableModel.populateErrorInfo("Too many topics. Increase MaxTopics or set a TopicNamePattern filter in the gems.props property file");
                                    }
                                }

                                try {
                                    detailsTableModel.populateTopicsInfo(connectionNode.getJmsAdmin().getTopics(this.getTopicNamePattern(), this.getPermType()));
                                } catch (Throwable var10_16) {
                                    detailsTableModel.populateTopicsInfo(connectionNode.getJmsAdmin().getTopics(this.getTopicNamePattern()));
                                }
                            } catch (TibjmsAdminException e) {
                                System.err.println("JMSException: " + e.getMessage());
                                this.m_lock = false;
                                this.m_frame.setCursor(Cursor.getDefaultCursor());
                                this.m_xlock.release();
                                return;
                            }
                        } else if (selectedUserObject.startsWith("Queues")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Queues Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }

                            try {
                                this.m_tabPane.setTitleAt(tabIndex, "Queues Info");
                                if (selectedIndex < tabIndex) System.out.println("**GOTO lbl447");

                                if (serverInfo == null) {
                                    detailsTableModel.populateErrorInfo("Not connected to EMS server");
                                } else {
                                    if (serverInfo.getQueueCount() > Gems.getGems().getMaxQueues() && Gems.getGems().getQueueNamePattern().length() <= 1) {
                                        detailsTableModel.populateErrorInfo("Too many queues. Increase MaxQueues or set a QueueNamePattern filter in the gems.props property file");
                                    }
                                }

                                try {
                                    detailsTableModel.populateQueuesInfo(connectionNode.getJmsAdmin().getQueues(this.getQueueNamePattern(), this.getPermType()));
                                } catch (Throwable var10_17) {
                                    detailsTableModel.populateQueuesInfo(connectionNode.getJmsAdmin().getQueues(this.getQueueNamePattern()));
                                }
                            } catch (TibjmsAdminException ex) {
                                System.err.println("JMSException: " + ex.getMessage());
                                this.m_lock = false;
                                this.m_frame.setCursor(Cursor.getDefaultCursor());
                                this.m_xlock.release();
                                return;
                            }
                        } else if (selectedUserObject.equals("Bridges")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Bridge Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Bridge Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateBridgeInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("Factories")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Factories Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Factories Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateFactoryInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("Channels")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Channels Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Channels Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateChannelsInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("Stores(File)")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Stores(File) Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Stores(File) Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateStoresFileInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("Stores(DB)")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Stores(DB) Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Stores(DB) Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateStoresDbInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("Stores(MStore)")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Stores(MStore) Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Stores(MStore) Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateStoresMStoreInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("Services")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Service Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Service Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateServiceInfo(connectionNode.m_services);
                            }
                        } else if (selectedUserObject.equals("Routes")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Route Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Route Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateRoutesInfo(connectionNode.getJmsAdmin());
                            }
                        } else if (selectedUserObject.equals("Transactions")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Transaction Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Transaction Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateTransactionInfo(connectionNode.getJmsAdmin());
                            }
                        } else if (selectedUserObject.equals("Transports")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Transport Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Transport Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateTransportInfo(connectionNode.getJmsAdmin());
                            }
                        } else if (selectedUserObject.equals("Durables")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Durable Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Durable Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateDurablesInfo(connectionNode.getJmsAdmin());
                            }
                        } else if (selectedUserObject.equals("Groups")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("Group Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "Group Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateGroupInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("ACLs")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("ACL Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "ACL Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateACLInfo(connectionNode);
                            }
                        } else if (selectedUserObject.equals("AdminACLs")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("AdminACL Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "AdminACL Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateAdminACLInfo(connectionNode);
                            }
                        } else {
                            if (!selectedNode.getClass().getSimpleName().equals("GemsSSNode")) {
                                this.m_lock = false;
                                this.m_frame.setCursor(Cursor.getDefaultCursor());
                                this.m_xlock.release();
                                return;
                            }
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSInfo(connectionNode, (GemsSSNode) selectedNode);
                            }
                        }
                    }
                } else if (level == 3) {
                    this.m_frame.setCursor(Cursor.getPredefinedCursor(3));
                    String userObject = (String) ((DefaultMutableTreeNode) selectedNode.getParent()).getUserObject();
                    if (userObject.startsWith("Topics")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("Topic Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }
                        this.m_tabPane.setTitleAt(tabIndex, "Topic Info");
                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateTopicInfo(connectionNode.getJmsAdmin(), selectedUserObject);
                        }
                    } else if (userObject.startsWith("Queues")) {
                        if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                            this.m_tabPane.addTab("Queue Info", this.m_detailsPanel);
                            selectedIndex = tabIndex;
                            this.m_tabPane.setSelectedIndex(selectedIndex);
                        }
                        this.m_tabPane.setTitleAt(tabIndex, "Queue Info");
                        if (selectedIndex >= tabIndex) {
                            detailsTableModel.populateQueueInfo(connectionNode.getJmsAdmin(), selectedUserObject);
                        }
                    } else if (selectedNode.getParent().getClass().getSimpleName().equals("GemsSSNode")) {
                        if (selectedUserObject.equals("Counters")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Counters Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSCounters(connectionNode, (GemsSSNode) selectedNode.getParent());
                            }
                        } else if (selectedUserObject.equals("Interfaces")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Interfaces Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSInterface(connectionNode, (GemsSSNode) selectedNode.getParent());
                            }
                        } else if (selectedUserObject.equals("Transports")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Transports Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSTransport(connectionNode, (GemsSSNode) selectedNode.getParent());
                            }
                        } else if (selectedUserObject.equals("IMS")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation IMS General");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSIMSGen(connectionNode, (GemsSSNode) selectedNode.getParent());
                            }
                        }
                    }
                } else if (level == 4) {
                    this.m_frame.setCursor(Cursor.getPredefinedCursor(3));
                    String var9_15 = (String) ((DefaultMutableTreeNode) selectedNode.getParent()).getUserObject();
                    String var10_18 = "";
                    if (!var9_15.equals("IMS")) {
                        var10_18 = var9_15.substring(5);
                    }
                    if (selectedNode.getParent().getParent().getClass().getSimpleName().equals("GemsSSNode")) {
                        if (selectedUserObject.equals("Listeners")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Listeners Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSListeners(connectionNode, (GemsSSNode) selectedNode.getParent().getParent(), var10_18);
                            }
                        } else if (selectedUserObject.equals("Active Recipes")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Active Recipes Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSActive(connectionNode, (GemsSSNode) selectedNode.getParent().getParent(), "RID", var10_18);
                            }
                        } else if (selectedUserObject.equals("Active Triggers")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Active Triggers Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSActive(connectionNode, (GemsSSNode) selectedNode.getParent().getParent(), "TID", var10_18);
                            }
                        } else if (selectedUserObject.equals("Disabled")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation Disabled Info");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSInActive(connectionNode, (GemsSSNode) selectedNode.getParent().getParent(), var10_18);
                            }
                        } else if (selectedUserObject.equals("IMS Statistics")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation IMS Statistics");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSIMSStats(connectionNode, (GemsSSNode) selectedNode.getParent().getParent());
                            }
                        } else if (selectedUserObject.equals("IMS Buffers")) {
                            if (connectionNode.isConnected() && this.m_tabPane.getTabCount() == tabIndex) {
                                this.m_tabPane.addTab("SubStation Info", this.m_detailsPanel);
                                selectedIndex = tabIndex;
                                this.m_tabPane.setSelectedIndex(selectedIndex);
                            }
                            this.m_tabPane.setTitleAt(tabIndex, "SubStation IMS Buffers");
                            if (selectedIndex >= tabIndex) {
                                detailsTableModel.populateSSIMSBuffers(connectionNode, (GemsSSNode) selectedNode.getParent().getParent());
                            }
                        }
                    }
                }
            }
            lbl447:
            // 52 sources:
            this.m_frame.setCursor(Cursor.getDefaultCursor());
            this.m_lock = false;
            this.m_xlock.release();
            return;
        } catch (Exception var3_4) {
            Gems.debug(var3_4.toString());
            this.m_frame.setCursor(Cursor.getDefaultCursor());
            this.m_lock = false;
            this.m_xlock.release();
            return;
        }
    }

    public void setTitle() {
        this.m_frame.setTitle(this.getTitlePrefix() + "Gems v3.4 Patched" + (getViewOnlyMode() ? " (view only)" : ""));
    }

    public String getTopicNamePattern() {
        return this.m_props.getProperty("TopicNamePattern");
    }

    public static Gems getGems() {
        return m_gems;
    }

    public int getMaxTopics() {
        try {
            return Integer.parseInt(this.m_props.getProperty("MaxTopics"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 1000;
        }
    }

    public int getPermType() {
        try {
            return Integer.parseInt(this.m_props.getProperty("PermType"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 3;
        }
    }

    public String getQueueNamePattern() {
        return this.m_props.getProperty("QueueNamePattern");
    }

    public int getMaxQueues() {
        try {
            return Integer.parseInt(this.m_props.getProperty("MaxQueues"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return 1000;
        }
    }

    public static void debug(String string) {
        if (m_gems.getDebug()) {
            System.err.println("Debug: " + string);
        }
    }

    public String getTitlePrefix() {
        if (!this.getShowPathInTitleBar()) {
            return "";
        }
        TreePath treePath = this.m_tree.getSelectionPath();
        Object[] arrobject = null;
        int n = 0;
        if (!this.getShowRootInTitleBar()) {
            n = 1;
        }
        if (treePath != null) {
            DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (!defaultMutableTreeNode.isRoot()) {
                while (defaultMutableTreeNode.getLevel() > 0) {
                    if (defaultMutableTreeNode instanceof GemsConnectionNode) {
                        arrobject = defaultMutableTreeNode.getUserObjectPath();
                        String string = "";
                        for (int i = n; i < arrobject.length; ++i) {
                            string = string + arrobject[i].toString();
                            if (i >= arrobject.length - 1) continue;
                            string = string + ".";
                        }
                        if (string.length() > 0) {
                            string = string + ": ";
                        }
                        return string;
                    }
                    defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
                }
            }
            if (arrobject == null) {
                arrobject = treePath.getPath();
            }
            String string = "";
            for (int i = n; i < arrobject.length; ++i) {
                string = string + ((DefaultMutableTreeNode) arrobject[i]).getUserObject().toString();
                if (i >= arrobject.length - 1) continue;
                string = string + ".";
            }
            if (string.length() > 0) {
                string = string + ": ";
            }
            return string;
        }
        return ((DefaultMutableTreeNode) this.m_treeModel.getRoot()).getUserObject().toString() + ": ";
    }

    public boolean getViewOnlyMode() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ViewOnlyMode"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public boolean getDebug() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("Debug"));
        } catch (Exception var1_1) {
            return false;
        }
    }

    public boolean getShowPathInTitleBar() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ShowPathInTitleBar"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return true;
        }
    }

    public boolean getShowRootInTitleBar() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ShowRootInTitleBar"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public boolean getShowRootNode() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ShowRootNode"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public boolean getShowMonitorTopics() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ShowMonitorTopics"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public boolean getColourPendingMsgs() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ColourPendingMsgs"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public boolean getShowExtendedProperties() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ShowExtendedProperties"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public boolean getShowClientId() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("ShowClientId"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public int getMsgReadDelay() {
        try {
            int n = Integer.parseInt(this.m_props.getProperty("MsgReadDelay"));
            if (n < 10) {
                n = 10;
            }
            return n;
        } catch (Exception var1_2) {
            this.m_props.setProperty("MsgReadDelay", "250");
            return 250;
        }
    }

    public void setMsgReadDelay(String string) {
        this.m_props.setProperty("MsgReadDelay", string);
    }

    public int getRequestReplyTimeout() {
        try {
            return Integer.parseInt(this.m_props.getProperty("RequestReplyTimeout"));
        } catch (Exception var1_1) {
            this.m_props.setProperty("RequestReplyTimeout", "10");
            return 10;
        }
    }

    public void setRequestReplyTimeout(String string) {
        this.m_props.setProperty("RequestReplyTimeout", string);
    }

    public String getRequestReplyTimeoutStr() {
        return this.m_props.getProperty("RequestReplyTimeout");
    }

    public String getMsgReadDelayStr() {
        return this.m_props.getProperty("MsgReadDelay");
    }

    public String getCSVFileDelimiter() {
        return this.m_props.getProperty("CSVFileDelimiter");
    }

    public String getServerInfoColPositions() {
        return this.m_props.getProperty("ServerInfoColPositions");
    }

    public boolean isStandbyOpsAllowed(GemsConnectionNode gemsConnectionNode) {
        if (!this.getAllowStandbyOperations() && gemsConnectionNode.isStandbyMode()) {
            JOptionPane.showMessageDialog(this.m_frame, "Admin operations on standby servers are disabled (see gems.props)", "Error", 1);
            return false;
        }
        return true;
    }

    public boolean getAllowStandbyOperations() {
        try {
            return Boolean.valueOf(this.m_props.getProperty("AllowStandbyOperations"));
        } catch (Exception var1_1) {
            System.err.println("Exception: " + var1_1.getMessage());
            return false;
        }
    }

    public void treeRepaint() {
        this.m_tree.repaint();
    }

    public GemsDetailsPanel getDetailsPanel() {
        return this.m_detailsPanel;
    }

    public void clearCurrentEventsDisplay() {
        GemsConnectionNode gemsConnectionNode = this.getConnectionNode();
        if (gemsConnectionNode != null) {
            gemsConnectionNode.clearEventMessages();
            this.scheduleRepaint();
        }
    }

    public void scheduleRepaint() {
        if (this.m_timer != null) {
            this.m_timer.restart();
        }
    }

    private Hashtable makeIcons() {
        Hashtable<String, Icon> hashtable = new Hashtable<String, Icon>();
        hashtable.put("floppyDrive", MetalIconFactory.getTreeFloppyDriveIcon());
        hashtable.put("hardDrive", MetalIconFactory.getTreeHardDriveIcon());
        hashtable.put("computer", MetalIconFactory.getTreeComputerIcon());
        hashtable.put("queue", TextIcons.getIcon("queue"));
        hashtable.put("routedqueue", TextIcons.getIcon("routedqueue"));
        hashtable.put("topic", TextIcons.getIcon("topic"));
        return hashtable;
    }

    private JMenuBar constructMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        String string = Gems.getGems().getHideViews();
        JMenu jMenu = new JMenu("File");
        jMenu.setMnemonic(70);
        jMenuBar.add(jMenu);
        JMenuItem jMenuItem = jMenu.add(new JMenuItem("Exit"));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        jMenu = new JMenu("Edit");
        jMenu.setMnemonic(69);
        jMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        jMenuItem.setText("Cut");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 2));
        jMenu.add(jMenuItem);
        jMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        jMenuItem.setText("Copy");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 2));
        jMenu.add(jMenuItem);
        jMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        jMenuItem.setText("Paste");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 2));
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        jMenu = new JMenu("View");
        jMenu.setMnemonic(86);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("Refresh"));
        jMenuItem.addActionListener(new RefreshAction());
        this.m_autoRefresh = new JCheckBoxMenuItem("Auto Refresh");
        if (this.m_displayRefresh > 0) {
            this.m_autoRefresh.setState(true);
            jMenuItem = jMenu.add(this.m_autoRefresh);
        }
        this.m_showTotals = new JCheckBoxMenuItem("Show Server Totals");
        this.m_showTotals.setState(this.getShowTotals());
        this.m_showTotals.addActionListener(new ShowTotalsAction());
        jMenuItem = jMenu.add(this.m_showTotals);
        if (this.m_displayRefresh > 0) {
            jMenu = new JMenu("Chart");
            jMenu.setMnemonic(67);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Chart Server..."));
            jMenuItem.addActionListener(new ChartAction());
        }
        jMenu = new JMenu("Server");
        jMenu.setMnemonic(83);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("New EMS Server Connection"));
        jMenuItem.addActionListener(new AddConnectionAction());
        jMenuItem = jMenu.add(new JMenuItem("Monitor Connections..."));
        jMenuItem.addActionListener(new MonitorConnectionAction());
        if (!this.getViewOnlyMode()) {
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Set Server Property..."));
            jMenuItem.addActionListener(new SetServerPropertyAction());
            jMenuItem = jMenu.add(new JMenuItem("Set Server Trace..."));
            jMenuItem.addActionListener(new SetServerTraceAction());
        }
        if (!this.getViewOnlyMode() && string.indexOf("Connections") < 0) {
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Destroy Client Connection"));
            jMenuItem.addActionListener(new DisconnectClientAction());
        }
        if (!this.getViewOnlyMode() && string.indexOf("Bridges") < 0) {
            jMenu = new JMenu("Bridges");
            jMenu.setMnemonic(66);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Create Bridge"));
            jMenuItem.addActionListener(new CreateBridgeAction());
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Destroy Bridge"));
            jMenuItem.addActionListener(new DestroyBridgeAction());
            jMenu.addSeparator();
        }
        jMenuItem = jMenu.add(new JMenuItem("Manage Bridges..."));
        jMenuItem.addActionListener(new FindBridgeTargetsAction());
        jMenu = new JMenu("Queues");
        jMenu.setMnemonic(81);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("Show Queues..."));
        jMenuItem.addActionListener(new ShowQueuesAction());
        jMenuItem = jMenu.add(new JMenuItem("Queue Browser..."));
        jMenuItem.addActionListener(new BrowseQueueAction());
        jMenuItem = jMenu.add(new JMenuItem("Queue Properties..."));
        jMenuItem.addActionListener(new SetQueuePropertyAction());
        jMenuItem = jMenu.add(new JMenuItem("Monitor Request/Reply Queue..."));
        jMenuItem.addActionListener(new MonitorReqReplyQueueAction());
        jMenuItem = jMenu.add(new JMenuItem("Monitor Queue..."));
        jMenuItem.addActionListener(new MonitorQueueAction());
        if (!this.getViewOnlyMode()) {
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Send TextMessage..."));
            jMenuItem.addActionListener(new SendMessageAction(false));
            jMenuItem = jMenu.add(new JMenuItem("Send MapMessage..."));
            jMenuItem.addActionListener(new SendMessageAction(true));
            jMenuItem = jMenu.add(new JMenuItem("Request/Reply Tester..."));
            jMenuItem.addActionListener(new RequestReplyTesterAction(true));
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Create Queue"));
            jMenuItem.addActionListener(new CreateQueueAction());
            jMenuItem = jMenu.add(new JMenuItem("Set Queue Permissions..."));
            jMenuItem.addActionListener(new SetQueuePermissionsAction());
            jMenu.addSeparator();
            if (string.indexOf("Queues") < 0) {
                jMenuItem = jMenu.add(new JMenuItem("Purge Queue"));
                jMenuItem.addActionListener(new PurgeQueueAction());
            }
            jMenuItem = jMenu.add(new JMenuItem("Purge Multiple Queues..."));
            jMenuItem.addActionListener(new PurgeQueuesAction());
            jMenuItem = jMenu.add(new JMenuItem("Purge $TMP Queues..."));
            jMenuItem.addActionListener(new PurgeTMPQueuesAction());
            if (string.indexOf("Queues") < 0) {
                jMenuItem = jMenu.add(new JMenuItem("Destroy Queue"));
                jMenuItem.addActionListener(new DeleteQueueAction());
            }
        }
        jMenu = new JMenu("Topics");
        jMenu.setMnemonic(84);
        jMenuBar.add(jMenu);
        jMenuItem = jMenu.add(new JMenuItem("Show Topics..."));
        jMenuItem.addActionListener(new ShowTopicsAction());
        jMenuItem = jMenu.add(new JMenuItem("Topic Subscriber..."));
        jMenuItem.addActionListener(new SubscribeTopicAction());
        jMenuItem = jMenu.add(new JMenuItem("Topic Properties..."));
        jMenuItem.addActionListener(new SetTopicPropertyAction());
        jMenuItem = jMenu.add(new JMenuItem("Monitor Request/Reply Topic..."));
        jMenuItem.addActionListener(new MonitorReqReplyTopicAction());
        jMenuItem = jMenu.add(new JMenuItem("Monitor Topic..."));
        jMenuItem.addActionListener(new MonitorTopicAction());
        if (!this.getViewOnlyMode()) {
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Publish TextMessage..."));
            jMenuItem.addActionListener(new PublishMessageAction(false));
            jMenuItem = jMenu.add(new JMenuItem("Publish MapMessage..."));
            jMenuItem.addActionListener(new PublishMessageAction(true));
            jMenuItem = jMenu.add(new JMenuItem("Request/Reply Tester..."));
            jMenuItem.addActionListener(new RequestReplyTesterAction(false));
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Create Topic"));
            jMenuItem.addActionListener(new CreateTopicAction());
            jMenuItem = jMenu.add(new JMenuItem("Set Topic Permissions..."));
            jMenuItem.addActionListener(new SetTopicPermissionsAction());
            jMenu.addSeparator();
            if (string.indexOf("Topics") < 0) {
                jMenuItem = jMenu.add(new JMenuItem("Purge Topic"));
                jMenuItem.addActionListener(new PurgeTopicAction());
            }
            jMenuItem = jMenu.add(new JMenuItem("Purge Multiple Topics..."));
            jMenuItem.addActionListener(new PurgeTopicsAction());
            jMenuItem = jMenu.add(new JMenuItem("Purge $TMP Topics..."));
            jMenuItem.addActionListener(new PurgeTMPTopicsAction());
            if (string.indexOf("Topics") < 0) {
                jMenuItem = jMenu.add(new JMenuItem("Destroy Topic"));
                jMenuItem.addActionListener(new DeleteTopicAction());
            }
        }
        if (string.indexOf("Durables") < 0) {
            jMenu = new JMenu("Durables");
            jMenu.setMnemonic(68);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Browse Durable..."));
            jMenuItem.addActionListener(new BrowseDurableAction());
            if (!this.getViewOnlyMode()) {
                jMenuItem = jMenu.add(new JMenuItem("Create Durable"));
                jMenuItem.addActionListener(new CreateDurableAction());
                jMenu.addSeparator();
                jMenuItem = jMenu.add(new JMenuItem("Destroy Durable"));
                jMenuItem.addActionListener(new DestroyDurableAction());
                jMenuItem = jMenu.add(new JMenuItem("Purge Durable"));
                jMenuItem.addActionListener(new PurgeDurableAction());
            }
        }
        if (!this.getViewOnlyMode() && string.indexOf("Users") < 0) {
            jMenu = new JMenu("Users");
            jMenu.setMnemonic(85);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Create User"));
            jMenuItem.addActionListener(new CreateUserAction());
            jMenuItem = jMenu.add(new JMenuItem("Update User"));
            jMenuItem.addActionListener(new UpdateUserAction());
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Create Group"));
            jMenuItem.addActionListener(new CreateGroupAction());
            jMenuItem = jMenu.add(new JMenuItem("Add User To Group"));
            jMenuItem.addActionListener(new AddUserToGroupAction());
            jMenuItem = jMenu.add(new JMenuItem("Remove User From Group"));
            jMenuItem.addActionListener(new RemoveUserFromGroupAction());
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Set Admin Permissions..."));
            jMenuItem.addActionListener(new SetAdminPermissionsAction());
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Destroy User"));
            jMenuItem.addActionListener(new DestroyUserAction());
            jMenuItem = jMenu.add(new JMenuItem("Destroy Group"));
            jMenuItem.addActionListener(new DestroyGroupAction());
        }
        if (!this.getViewOnlyMode() && string.indexOf("Routes") < 0) {
            jMenu = new JMenu("Routes");
            jMenu.setMnemonic(82);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Create Route"));
            jMenuItem.addActionListener(new CreateRouteAction());
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Destroy Route"));
            jMenuItem.addActionListener(new DestroyRouteAction());
            jMenu.addSeparator();
            jMenuItem = jMenu.add(new JMenuItem("Monitor Routes..."));
            jMenuItem.addActionListener(new MonitorRouteAction());
        }
        if (this.m_serviceMenu) {
            jMenu = new JMenu("Services");
            jMenu.setMnemonic(83);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Enable Service Monitoring"));
            jMenuItem.addActionListener(new StartServiceMonitoringAction());
            jMenuItem = jMenu.add(new JMenuItem("Disable Service Monitoring"));
            jMenuItem.addActionListener(new StopServiceMonitoringAction());
        }
        if (!this.getViewOnlyMode() && string.indexOf("Factories") < 0) {
            jMenu = new JMenu("Factories");
            jMenu.setMnemonic(65);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Create Connection Factory..."));
            jMenuItem.addActionListener(new CreateFactoryAction());
            jMenuItem = jMenu.add(new JMenuItem("Destroy Connection Factory"));
            jMenuItem.addActionListener(new DestroyFactoryAction());
        }
        if (!this.getViewOnlyMode() && this.m_substationMenu) {
            jMenu = new JMenu("SubStation");
            jMenu.setMnemonic(78);
            jMenuBar.add(jMenu);
            jMenuItem = jMenu.add(new JMenuItem("Refresh"));
            jMenuItem.addActionListener(new RefreshSSAction());
            jMenuItem = jMenu.add(new JMenuItem("Enable"));
            jMenuItem.addActionListener(new EnableSSAction());
            jMenuItem = jMenu.add(new JMenuItem("Disable"));
            jMenuItem.addActionListener(new DisableSSAction());
            jMenuItem = jMenu.add(new JMenuItem("Unload"));
            jMenuItem.addActionListener(new UnloadSSAction());
            jMenuItem = jMenu.add(new JMenuItem("Enable New Object"));
            jMenuItem.addActionListener(new NewSSRecipeAction());
            jMenuItem = jMenu.add(new JMenuItem("Reset Counters"));
            jMenuItem.addActionListener(new ResetSSAction());
        }
        jMenu = new JMenu("Help");
        jMenu.setMnemonic(72);
        jMenuBar.add(jMenu);
        if (this.m_jreVersion != null && !this.m_jreVersion.startsWith("1.5")) {
            jMenuItem = jMenu.add(new JMenuItem("Help..."));
            jMenuItem.addActionListener(new ShowHelp());
        }
        jMenuItem = jMenu.add(new JMenuItem("About Gems..."));
        jMenuItem.addActionListener(new AboutAction());
        return jMenuBar;
    }

    class ConnectThread
            extends Thread {
        long m_delay;

        ConnectThread(long l) {
            this.m_delay = l * 1000;
        }

        public void run() {
            while (this.m_delay > 0) {
                try {
                    ConnectThread.sleep(this.m_delay);
                    DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
                    defaultMutableTreeNode = (DefaultMutableTreeNode) Gems.this.m_treeModel.getRoot();
                    if (Gems.this.m_servMonitorPanel.getModel().findServers(defaultMutableTreeNode, true, Gems.this.m_showEvents)) {
                        Gems.this.m_tree.repaint();
                        if (Gems.this.m_timer == null) continue;
                        Gems.this.m_timer.restart();
                        continue;
                    }
                    if (Gems.this.m_timer == null) continue;
                    Gems.this.m_timer.restart();
                } catch (Exception var1_2) {
                    System.err.println("Exception: " + var1_2.getMessage());
                }
            }
        }
    }

    class RefreshTimerAction
            implements ActionListener {
        RefreshTimerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            if (!Gems.this.m_autoRefresh.getState()) {
                return;
            }
            String string = null;
            int n = 0;
            if (Gems.this.m_detailsPanel.isShowing()) {
                n = Gems.this.m_detailsPanel.getModel().getSelectedRow();
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            }
            Gems.getGems().treeSelectionChange(defaultMutableTreeNode, true);
            if (Gems.this.m_detailsPanel.isShowing() && string != null) {
                Gems.this.m_detailsPanel.getModel().maintainSelection(n, string);
            }
        }
    }

    class RefreshThread
            extends Thread {
        long m_delay;

        RefreshThread(long l) {
            this.m_delay = l * 1000;
        }

        public void run() {
            while (this.m_delay > 0) {
                try {
                    RefreshThread.sleep(this.m_delay);
                    Gems.getGems().treeSelectionChange(Gems.this.getSelectedNode(), true);
                } catch (Exception var1_1) {
                    System.err.println("Exception: " + var1_1.getMessage());
                }
            }
        }
    }

    class OptionsDialog
            implements ActionListener {
        OptionsDialog() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JPanel jPanel = new JPanel(new SpringLayout());
            jPanel.add(new JLabel("Only show Queues with name pattern:"));
            JTextField jTextField = new JTextField(25);
            jTextField.setText(Gems.this.getQueueNamePattern());
            jPanel.add(jTextField);
            jPanel.add(new JLabel("Only show Topics with name pattern:"));
            JTextField jTextField2 = new JTextField(25);
            jTextField2.setText(Gems.this.getTopicNamePattern());
            jPanel.add(jTextField2);
            jPanel.add(new JLabel("Target Type:"));
            JComboBox<String> jComboBox = new JComboBox<String>();
            jComboBox.addItem("Queue");
            jComboBox.addItem("Topic");
            jPanel.add(jComboBox);
            jPanel.add(new JLabel("Selector:"));
            JTextField jTextField3 = new JTextField(25);
            jPanel.add(jTextField3);
            SpringUtilities.makeCompactGrid(jPanel, 4, 2, 5, 5, 5, 5);
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, jPanel, "Edit Options", 2);
            if (n == 0) {
                Gems.this.m_props.setProperty("QueueNamePattern", jTextField.getText());
                Gems.this.m_props.setProperty("TopicNamePattern", jTextField2.getText());
                Gems.this.m_treeModel.reloadTree();
            }
        }
    }

    class ChartAction
            implements ActionListener {
        ChartAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionPicker gemsConnectionPicker = new GemsConnectionPicker(Gems.this.m_frame, Gems.this.getConnectionNode(), "Chart");
            GemsConnectionNode gemsConnectionNode = gemsConnectionPicker.getConnection();
            if (gemsConnectionNode != null) {
                Gems.this.m_frame.setCursor(Cursor.getPredefinedCursor(3));
                gemsConnectionNode.startCharting();
                Gems.this.m_frame.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    class BrowseDurableAction
            implements ActionListener {
        BrowseDurableAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("durable")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Durables")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Durable to browse!", "Browse Durable", 1);
                return;
            }
            String string3 = Gems.this.m_detailsPanel.getModel().getSelectedCol(6);
            String string4 = Gems.this.m_detailsPanel.getModel().getSelectedCol(2);
            String string5 = Gems.this.m_detailsPanel.getModel().getSelectedCol(9);
            Boolean bl = new Boolean(Gems.this.m_detailsPanel.getModel().getSelectedCol(10));
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsDurableBrowser(gemsConnectionNode, string2, string3, string4, string5, bl);
            }
        }
    }

    class StopServiceMonitoringAction
            implements ActionListener {
        StopServiceMonitoringAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                if (gemsConnectionNode.m_services == null) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, "There are no services configured for " + gemsConnectionNode.getName(), "Disable Service Monitoring", 1);
                    return;
                }
                if (gemsConnectionNode.m_services.m_running) {
                    int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Disable service monitoring for " + gemsConnectionNode.getName() + "?", "Disable Service Monitoring", 0);
                    if (n == 0) {
                        gemsConnectionNode.m_services.userStop();
                    }
                } else {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, "Service monitoring is currently disabled for " + gemsConnectionNode.getName(), "Disable Service Monitoring", 1);
                }
            }
        }
    }

    class DestroyFactoryAction
            implements ActionListener {
        DestroyFactoryAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Factories")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Factory to destroy!", "Destroy Factory", 1);
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Connection Factory: " + string2, "Destroy Factory", 0);
            if (n == 0) {
                try {
                    if (gemsConnectionNode != null && gemsConnectionNode.m_adminConn != null) {
                        gemsConnectionNode.m_adminConn.destroyConnectionFactory(string2);
                    }
                    Gems.this.scheduleRepaint();
                } catch (TibjmsAdminException var7_7) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, var7_7.getMessage(), "Error", 1);
                    return;
                }
            }
        }
    }

    class CreateFactoryAction
            implements ActionListener {
        CreateFactoryAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            if (gemsConnectionNode != null && gemsConnectionNode.m_adminConn != null) {
                String string = "<html>Jndi Name is mandatory, other fields can be left blank for defaults.<br>Set Destination Type to 1 for Queue, 2 for Topic, or leave blank for generic. <br>For load balancing, set Metric to 1 for connections, 2 for byte rate, or leave blank for none.</html>";
                GemsDynamicPropertyDialog gemsDynamicPropertyDialog = new GemsDynamicPropertyDialog(Gems.this.m_frame, "Create Connection Factory", "com.tibco.gems.GemsDummyConnectionFactoryInfo", string);
                GemsDummyConnectionFactoryInfo gemsDummyConnectionFactoryInfo = null;
                try {
                    gemsDummyConnectionFactoryInfo = (GemsDummyConnectionFactoryInfo) ((Object) gemsDynamicPropertyDialog.getObject());
                } catch (Exception var6_6) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, var6_6.getMessage(), "Error", 1);
                    return;
                }
                if (gemsDummyConnectionFactoryInfo == null) {
                    return;
                }
                try {
                    gemsConnectionNode.m_adminConn.createConnectionFactory(gemsDummyConnectionFactoryInfo.getJndiName(), (ConnectionFactoryInfo) gemsDummyConnectionFactoryInfo);
                    Gems.this.scheduleRepaint();
                } catch (TibjmsAdminException var6_7) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, var6_7.getMessage(), "Error", 1);
                    return;
                }
            }
        }
    }

    class StartServiceMonitoringAction
            implements ActionListener {
        StartServiceMonitoringAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                if (gemsConnectionNode.m_services == null) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, "There are no services configured for " + gemsConnectionNode.getName(), "Enable Service Monitoring", 1);
                    return;
                }
                if (gemsConnectionNode.m_services.m_running) {
                    int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Service monitoring for " + gemsConnectionNode.getName() + " is already enabled, do you want to reset stats?", "Enable Service Monitoring", 0);
                    if (n == 0) {
                        gemsConnectionNode.m_services.reset();
                    }
                } else {
                    int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Enable service monitoring for " + gemsConnectionNode.getName() + "?", "Enable Service Monitoring", 0);
                    if (n == 0) {
                        gemsConnectionNode.m_services.userStart();
                    }
                }
            }
        }
    }

    class PurgeDurableAction
            implements ActionListener {
        PurgeDurableAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String string;
            if (!Gems.this.checkConnected("durable")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string2 = (String) defaultMutableTreeNode.getUserObject();
            String string3 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string3 == null || !string2.equals("Durables")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Durable to purge!", "Purge Durable", 1);
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Purge Durable: " + string3 + " clientID " + ((string = Gems.this.m_detailsPanel.getModel().getSelectedCol(6)) == null ? "" : string), "Purge Durable", 0);
            if (n == 0) {
                if (gemsConnectionNode != null) {
                    gemsConnectionNode.purgeDurable(string3, string);
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class DestroyDurableAction
            implements ActionListener {
        DestroyDurableAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String string;
            if (!Gems.this.checkConnected("durable")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string2 = (String) defaultMutableTreeNode.getUserObject();
            String string3 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string3 == null || !string2.equals("Durables")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Durable to destroy!", "Destroy Durable", 1);
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Durable: " + string3 + " clientID " + ((string = Gems.this.m_detailsPanel.getModel().getSelectedCol(6)) == null ? "" : string), "Destroy Durable", 0);
            if (n == 0) {
                if (gemsConnectionNode != null) {
                    gemsConnectionNode.destroyDurable(string3, string);
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class DisconnectClientAction
            implements ActionListener {
        DisconnectClientAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Object object;
            if (!Gems.this.checkConnected("client connection")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string2 == null || !string.startsWith("Connections") && !string.equals("Clients")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a client connection on the Connection Info display to destroy", "Destroy Connection", 1);
                return;
            }
            int n = 1;
            if (string.equals("Clients")) {
                string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol(2);
                object = Gems.this.m_detailsPanel.getModel().getSelectedCol(1);
                n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Connection: ID " + string2 + " for " + (String) object, "Destroy Client Connection", 0);
            } else {
                object = Gems.this.m_detailsPanel.getModel().getSelectedCol(3);
                n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Connection: ID " + string2 + " on " + (String) object, "Destroy Connection", 0);
            }
            if (n == 0) {
                GemsConnectionNode connectionNode = Gems.this.getConnectionNode();
                if (connectionNode != null) {
                    connectionNode.destroyConnection(string2);
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class CreateDurableAction
            implements ActionListener {
        CreateDurableAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            JTextField jTextField = new JTextField(20);
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, 1));
            jPanel.add(new JLabel("Durable Name:"));
            JTextField jTextField2 = new JTextField(20);
            jPanel.add(jTextField2);
            jPanel.add(new JLabel("Topic Name:"));
            jPanel.add(jTextField);
            jPanel.add(new JLabel("Client Id:"));
            JTextField jTextField3 = new JTextField(25);
            jPanel.add(jTextField3);
            jPanel.add(new JLabel("Selector:"));
            JTextField jTextField4 = new JTextField(25);
            jPanel.add(jTextField4);
            int n = JOptionPane.showConfirmDialog(null, jPanel, "Create Durable", 2);
            if (n == 0) {
                gemsConnectionNode.createDurable(jTextField2.getText(), jTextField.getText(), jTextField3.getText(), jTextField4.getText());
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class RemoveUserFromGroupAction
            implements ActionListener {
        RemoveUserFromGroupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("user")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Users") && !string.equals("Groups")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a User to remove or a Group to remove from!", "Remove User From Group", 1);
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            if (string.equals("Users")) {
                String string3 = JOptionPane.showInputDialog(Gems.this.m_frame, (Object) ("Remove User  " + string2 + "  From Group:"));
                if (string3 != null) {
                    gemsConnectionNode.removeUserFromGroup(string2, string3);
                }
            } else {
                String string4 = JOptionPane.showInputDialog(Gems.this.m_frame, (Object) ("Remove From Group  " + string2 + "  User:"));
                if (string4 != null) {
                    gemsConnectionNode.removeUserFromGroup(string4, string2);
                }
            }
            Gems.this.scheduleRepaint();
        }
    }

    class AddUserToGroupAction
            implements ActionListener {
        AddUserToGroupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("user")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Users") && !string.equals("Groups")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a User to add or a Group to add to!", "Add User To Group", 1);
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            if (string.equals("Users")) {
                String string3 = JOptionPane.showInputDialog(Gems.this.m_frame, (Object) ("Add User  " + string2 + "  To Group:"));
                if (string3 != null) {
                    gemsConnectionNode.addUserToGroup(string2, string3);
                }
            } else {
                String string4 = JOptionPane.showInputDialog(Gems.this.m_frame, (Object) ("Add To Group  " + string2 + "  User:"));
                if (string4 != null) {
                    gemsConnectionNode.addUserToGroup(string4, string2);
                }
            }
            Gems.this.scheduleRepaint();
        }
    }

    class CreateGroupAction
            implements ActionListener {
        CreateGroupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, 1));
            jPanel.add(new JLabel("Groupname:"));
            JTextField jTextField = new JTextField(20);
            jPanel.add(jTextField);
            jPanel.add(new JLabel("Description:"));
            JTextField jTextField2 = new JTextField(25);
            jPanel.add(jTextField2);
            int n = JOptionPane.showConfirmDialog(null, jPanel, "Create Group", 2);
            if (n == 0) {
                gemsConnectionNode.createGroup(jTextField.getText(), jTextField2.getText());
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class DestroyGroupAction
            implements ActionListener {
        DestroyGroupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("group")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Groups")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Group to destroy!", "Destroy Group", 1);
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Group: " + string2, "Destroy Group", 0);
            if (n == 0) {
                if (gemsConnectionNode != null && gemsConnectionNode.m_adminConn != null) {
                    try {
                        gemsConnectionNode.m_adminConn.destroyGroup(string2);
                    } catch (TibjmsAdminException var7_7) {
                        System.err.println("JMSException: " + var7_7.getMessage());
                        return;
                    }
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class DestroyUserAction
            implements ActionListener {
        DestroyUserAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("user")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Users")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a User to destroy!", "Destroy User", 1);
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy User: " + string2, "Destroy User", 0);
            if (n == 0) {
                if (gemsConnectionNode != null) {
                    gemsConnectionNode.destroyUser(string2);
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class UpdateUserAction
            implements ActionListener {
        UpdateUserAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode;
            if (!Gems.this.checkConnected("user")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = new String();
            if (defaultMutableTreeNode != null && string2 != null && string.equals("Users")) {
                string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            }
            if ((gemsConnectionNode = Gems.this.getConnectionNode()) == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            JPasswordField jPasswordField = new JPasswordField(20);
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, 1));
            jPanel.add(new JLabel("Username:"));
            JTextField jTextField = new JTextField(20);
            jTextField.setText(string2);
            jPanel.add(jTextField);
            jPanel.add(new JLabel("Password:"));
            jPanel.add(jPasswordField);
            jPanel.add(new JLabel("Description:"));
            JTextField jTextField2 = new JTextField(25);
            jPanel.add(jTextField2);
            int n = JOptionPane.showConfirmDialog(null, jPanel, "Update User", 2);
            if (n == 0) {
                gemsConnectionNode.updateUser(jTextField.getText(), new String(jPasswordField.getPassword()), jTextField2.getText());
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class CreateUserAction
            implements ActionListener {
        CreateUserAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("user")) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                return;
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            JPasswordField jPasswordField = new JPasswordField(20);
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, 1));
            jPanel.add(new JLabel("Username:"));
            JTextField jTextField = new JTextField(20);
            jPanel.add(jTextField);
            jPanel.add(new JLabel("Password:"));
            jPanel.add(jPasswordField);
            jPanel.add(new JLabel("Description:"));
            JTextField jTextField2 = new JTextField(25);
            jPanel.add(jTextField2);
            int n = JOptionPane.showConfirmDialog(null, jPanel, "Create User", 2);
            if (n == 0) {
                gemsConnectionNode.createUser(jTextField.getText(), new String(jPasswordField.getPassword()), jTextField2.getText());
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class DestroyRouteAction
            implements ActionListener {
        DestroyRouteAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("route")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol(1);
            String string3 = Gems.this.m_detailsPanel.getModel().getSelectedCol(2);
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Routes")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Route to destroy!", "Destroy Route", 1);
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Route to Server: " + string2 + " at " + string3, "Destroy Route", 0);
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            if (n == 0 && gemsConnectionNode != null && gemsConnectionNode.m_adminConn != null) {
                try {
                    gemsConnectionNode.m_adminConn.destroyRoute(string2);
                } catch (TibjmsAdminException var8_8) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, var8_8.getMessage(), "Error", 1);
                    return;
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class CreateRouteAction
            implements ActionListener {
        CreateRouteAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            Object var3_3 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            JPanel jPanel = new JPanel(new SpringLayout());
            jPanel.add(new JLabel("Server Name"));
            JTextField jTextField = new JTextField(25);
            jPanel.add(jTextField);
            jPanel.add(new JLabel("Server URL:"));
            JTextField jTextField2 = new JTextField(25);
            jPanel.add(jTextField2);
            jTextField2.setText("tcp://localhost:7222");
            jPanel.add(new JLabel("Zone Name:"));
            JTextField jTextField3 = new JTextField(25);
            jTextField3.setText("default_mhop_zone");
            jPanel.add(jTextField3);
            jPanel.add(new JLabel("Zone Type:"));
            JComboBox<String> jComboBox = new JComboBox<String>();
            jComboBox.addItem("MultiHop");
            jComboBox.addItem("OneHop");
            jPanel.add(jComboBox);
            SpringUtilities.makeCompactGrid(jPanel, 4, 2, 5, 5, 5, 5);
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, jPanel, "Create Route to Server", 2);
            if (n == 0 && gemsConnectionNode != null && gemsConnectionNode.m_adminConn != null) {
                try {
                    RouteInfo routeInfo = new RouteInfo(jTextField.getText(), jTextField2.getText(), null, jTextField3.getText(), (short) (jComboBox.getSelectedItem().equals("MultiHop") ? 0 : 1));
                    gemsConnectionNode.m_adminConn.createRoute(routeInfo);
                } catch (TibjmsAdminException var11_12) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, var11_12.getMessage(), "Error", 1);
                    return;
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class FindBridgeTargetsAction
            implements ActionListener {
        FindBridgeTargetsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            String string = new String();
            String string2 = "Topic";
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                string2 = "Topic";
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                string = (String) defaultMutableTreeNode.getUserObject();
                string2 = "Topic";
            } else if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Queues")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                string2 = "Queue";
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Queues")) {
                string = (String) defaultMutableTreeNode.getUserObject();
                string2 = "Queue";
            }
            if (string == null || string.length() == 0) {
                string = "";
            }
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            GemsManageBridgesDialog gemsManageBridgesDialog = new GemsManageBridgesDialog(Gems.this.m_frame, gemsConnectionNode, string2, string, "Target");
            Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
        }
    }

    class DestroyBridgeAction
            implements ActionListener {
        DestroyBridgeAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("bridge")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            String string = (String) defaultMutableTreeNode.getUserObject();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol(1);
            if (defaultMutableTreeNode == null || string2 == null || !string.equals("Bridges")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Bridge to destroy!", "Destroy Bridge", 1);
                return;
            }
            String string3 = Gems.this.m_detailsPanel.getModel().getSelectedCol(2);
            String string4 = Gems.this.m_detailsPanel.getModel().getSelectedCol(3);
            String string5 = Gems.this.m_detailsPanel.getModel().getSelectedCol(4);
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "<html>Destroy Bridge:<p>Source " + string3 + ":" + string2 + "<p>Target " + string5 + ": " + string4 + "</html>", "Destroy Bridge", 0);
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }
            if (n == 0 && gemsConnectionNode != null && gemsConnectionNode.m_adminConn != null) {
                try {
                    gemsConnectionNode.m_adminConn.destroyDestinationBridge(string3.equals("Queue") ? 1 : 2, string2, string5.equals("Queue") ? 1 : 2, string4);
                } catch (TibjmsAdminException var10_10) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, var10_10.getMessage(), "Error", 1);
                    return;
                }
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    private class CreateBridgeAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("destination")) {
                return;
            }

            DefaultMutableTreeNode selectedNode = Gems.this.getSelectedNode();
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (selectedNode == null || gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a source destination to create a bridge from!", "Create Bridge", 1);
                return;
            }

            boolean bl = true;
            String string = new String();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
            if (((String) selectedNode.getUserObject()).startsWith("Topics")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                bl = false;
            } else if (parentNode != null && ((String) parentNode.getUserObject()).startsWith("Topics")) {
                string = (String) selectedNode.getUserObject();
                bl = false;
            } else if (((String) selectedNode.getUserObject()).startsWith("Queues")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (parentNode != null && ((String) parentNode.getUserObject()).startsWith("Queues")) {
                string = (String) selectedNode.getUserObject();
            }

            if (string == null || string.length() == 0) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a destination to create a bridge from!", "Create Bridge", 1);
                return;
            }

            if (!Gems.getGems().isStandbyOpsAllowed(gemsConnectionNode)) {
                return;
            }

            GemsCreateBridgeDialog gemsCreateBridgeDialog = new GemsCreateBridgeDialog(Gems.this.m_frame, gemsConnectionNode, bl, string);
            if (gemsCreateBridgeDialog.createBridge()) {
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class DestroyMessageAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("xxx")) {
                return;
            }

            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }

            Message message = Gems.this.m_messagePanel.getModel().getSelectedMessage();
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null || !((String) defaultMutableTreeNode2.getUserObject()).startsWith("Queues") || Gems.this.m_tabPane.getSelectedIndex() != 3 || message == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Message to destroy!", "Destroy Message", 1);
                return;
            }

            try {
                String string = (String) defaultMutableTreeNode.getUserObject();
                int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Message: " + message.getJMSMessageID(), "Destroy Message", 0);
                if (n == 0) {
                    Gems.this.getConnectionNode().removeMessage(message);
                }
            } catch (Exception var5_6) {
                System.err.println("JMSException: " + var5_6.getMessage());
                return;
            }
        }
    }

    private class MonitorConnectionAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }

            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsTopicSubscriber(gemsConnectionNode, "$sys.monitor.connection.*", "Connection Monitor");
            }
        }
    }

    private class MonitorRouteAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsTopicSubscriber(gemsConnectionNode, "$sys.monitor.route.*", "Route Monitor");
            }
        }
    }

    private class ShowTopicsAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsDestDisplay(gemsConnectionNode, "Topic");
            }
        }
    }

    private class ShowQueuesAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsDestDisplay(gemsConnectionNode, "Queue");
            }
        }
    }

    private class MonitorQueueAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("queue")) {
                return;
            }

            DefaultMutableTreeNode selectedNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode parentNode = null;
            if (selectedNode != null) {
                parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
            }

            if (selectedNode == null || parentNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Queue to monitor!", "Monitor Queue", 1);
                return;
            }

            String string = "";
            if (((String) selectedNode.getUserObject()).startsWith("Queues")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (((String) parentNode.getUserObject()).startsWith("Queues")) {
                string = (String) selectedNode.getUserObject();
            }

            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsTopicSubscriber(gemsConnectionNode, "$sys.monitor.Q.*." + string, "Queue Monitor");
            }
        }
    }

    private class MonitorReqReplyQueueAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("queue")) {
                return;
            }
            DefaultMutableTreeNode selectedNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode parentNode = null;
            if (selectedNode != null) {
                parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
            }

            if (selectedNode == null || parentNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Request Queue to monitor!", "Monitor Request/Reply Queue", 1);
                return;
            }

            String string = "";
            if (((String) selectedNode.getUserObject()).startsWith("Queues")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (((String) parentNode.getUserObject()).startsWith("Queues")) {
                string = (String) selectedNode.getUserObject();
            }

            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsReqReplyMonitor(gemsConnectionNode, string, true, "Queue Request/Reply Monitor");
            }
        }
    }

    class ResetSSAction implements ActionListener {
        ResetSSAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("SubStation")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "A SubStation Node or an item below the node must be selected", "Enable New SS Object", 1);
                return;
            }
            GemsSSNode gemsSSNode = null;
            String string = "";
            if (defaultMutableTreeNode2.getClass().getSimpleName().equals("GemsSSNode")) {
                gemsSSNode = (GemsSSNode) defaultMutableTreeNode2;
            } else if (defaultMutableTreeNode.getClass().getSimpleName().equals("GemsSSNode")) {
                gemsSSNode = (GemsSSNode) defaultMutableTreeNode;
            } else {
                return;
            }
            String string2 = "";
            string2 = gemsSSNode.RunCommand("REFRESH,COUNTERS");
            JOptionPane.showMessageDialog(Gems.this.m_frame, string2, "Refresh SS Counters", 1);
        }
    }

    class UnloadSSAction
            implements ActionListener {
        UnloadSSAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("SubStation")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent().getParent();
            }
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null || !((String) defaultMutableTreeNode.getUserObject()).startsWith("Active") && !((String) defaultMutableTreeNode.getUserObject()).startsWith("Disabled")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Recipe or Trigger to Unload!", "Unload SS Object", 1);
                return;
            }
            String string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol(2);
            GemsSSNode gemsSSNode = null;
            String string3 = "";
            if (!defaultMutableTreeNode2.getClass().getSimpleName().equals("GemsSSNode")) {
                return;
            }
            gemsSSNode = (GemsSSNode) defaultMutableTreeNode2;
            String string4 = ((String) ((DefaultMutableTreeNode) defaultMutableTreeNode.getParent()).getUserObject()).substring(5);
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active R")) {
                string3 = gemsSSNode.RunCommand("UNLOAD,RID=" + string + ",INTF=" + string4);
            }
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active T")) {
                string3 = gemsSSNode.RunCommand("UNLOAD,TID=" + string + ",INTF=" + string4);
            }
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Disabled")) {
                string3 = string2.equals("Recipe") ? gemsSSNode.RunCommand("UNLOAD,RID=" + string + ",INTF=" + string4) : gemsSSNode.RunCommand("UNLOAD,TID=" + string + ",INTF=" + string4);
            }
            JOptionPane.showMessageDialog(Gems.this.m_frame, string3, "Refresh SS Object", 1);
        }
    }

    class NewSSRecipeAction
            implements ActionListener {
        NewSSRecipeAction() {
        }

        private Object makeObj(final String string) {
            return new Object() {

                public String toString() {
                    return string;
                }
            };
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Object object;
            int n;
            if (!Gems.this.checkConnected("SubStation")) {
                return;
            }
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, 1));
            JLabel jLabel = new JLabel("Name:");
            jPanel.add(jLabel);
            JTextField jTextField = new JTextField(10);
            jLabel.setLabelFor(jTextField);
            jPanel.add(jTextField);
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            JComboBox<Object> jComboBox = new JComboBox<Object>();
            if (defaultMutableTreeNode.getParent().getParent().getClass().getSimpleName().equals("GemsSSNode")) {
                GemsSSNode gemsSSNode = (GemsSSNode) defaultMutableTreeNode.getParent().getParent();
                n = gemsSSNode.getChildCount();
                for (int i = 0; i < n; ++i) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) gemsSSNode.getChildAt(i);
                    String userObject = (String) node.getUserObject();
                    if (!userObject.startsWith("Int:")) continue;
                    jComboBox.addItem(this.makeObj(userObject.substring(5)));
                }
            } else if (defaultMutableTreeNode.getParent().getClass().getSimpleName().equals("GemsSSNode")) {
                GemsSSNode gemsSSNode2 = (GemsSSNode) defaultMutableTreeNode.getParent();
                n = gemsSSNode2.getChildCount();
                for (int defaultMutableTreeNode2 = 0; defaultMutableTreeNode2 < n; ++defaultMutableTreeNode2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) gemsSSNode2.getChildAt(defaultMutableTreeNode2);
                    String userObject = (String) node.getUserObject();
                    if (!userObject.startsWith("Int:")) continue;
                    jComboBox.addItem(this.makeObj(userObject.substring(5)));
                }
            } else if (defaultMutableTreeNode.getClass().getSimpleName().equals("GemsSSNode")) {
                int n2 = defaultMutableTreeNode.getChildCount();
                for (n = 0; n < n2; ++n) {
                    DefaultMutableTreeNode defaultMutableTreeNode3 = (DefaultMutableTreeNode) defaultMutableTreeNode.getChildAt(n);
                    String userObject = (String) defaultMutableTreeNode3.getUserObject();
                    if (!userObject.startsWith("Int:")) continue;
                    jComboBox.addItem(this.makeObj(userObject.substring(5)));
                }
            } else {
                return;
            }
            jPanel.add(jComboBox);
            JComboBox<String> jComboBox2 = new JComboBox<String>(new String[]{"Recipe", "Trigger"});
            jPanel.add(jComboBox2);
            n = JOptionPane.showConfirmDialog(null, jPanel, "Enable New SubStation Object", 2);
            if (n == 0) {
                DefaultMutableTreeNode defaultMutableTreeNode2 = Gems.this.getSelectedNode();
                DefaultMutableTreeNode objectNode = null;
                if (defaultMutableTreeNode2 != null && ((String) (objectNode = (DefaultMutableTreeNode) defaultMutableTreeNode2.getParent()).getUserObject()).startsWith("Int")) {
                    object = (DefaultMutableTreeNode) objectNode.getParent();
                }
                if (defaultMutableTreeNode2 == null || objectNode == null) {
                    JOptionPane.showMessageDialog(Gems.this.m_frame, "A SubStation Node or an item below the node must be selected", "Enable New SS Object", 1);
                    return;
                }
                GemsSSNode object2 = null;
                String string = "";
                if (objectNode.getClass().getSimpleName().equals("GemsSSNode")) {
                    object2 = (GemsSSNode) objectNode;
                } else if (defaultMutableTreeNode2.getClass().getSimpleName().equals("GemsSSNode")) {
                    object2 = (GemsSSNode) defaultMutableTreeNode2;
                } else {
                    return;
                }
                String string2 = jTextField.getText();
                String string3 = jComboBox2.getSelectedItem().toString();
                String string4 = jComboBox.getSelectedItem().toString();
                string = string3.equals("Recipe") ? object2.RunCommand("REFRESH,RID=" + string2 + ",INTF=" + string4) : object2.RunCommand("REFRESH,TID=" + string2 + ",INTF=" + string4);
                JOptionPane.showMessageDialog(Gems.this.m_frame, string, "Enable New SS Object", 1);
            }
        }

    }

    class DisableSSAction
            implements ActionListener {
        DisableSSAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("SubStation")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent().getParent();
            }
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null || !((String) defaultMutableTreeNode.getUserObject()).startsWith("Active")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Recipe or Trigger to Disable!", "Enable SS Object", 1);
                return;
            }
            String string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            GemsSSNode gemsSSNode = null;
            String string2 = "";
            if (!defaultMutableTreeNode2.getClass().getSimpleName().equals("GemsSSNode")) {
                return;
            }
            gemsSSNode = (GemsSSNode) defaultMutableTreeNode2;
            String string3 = ((String) ((DefaultMutableTreeNode) defaultMutableTreeNode.getParent()).getUserObject()).substring(5);
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active R")) {
                string2 = gemsSSNode.RunCommand("DISABLE,RID=" + string + ",INTF=" + string3);
            }
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active T")) {
                string2 = gemsSSNode.RunCommand("DISABLE,TID=" + string + ",INTF=" + string3);
            }
            JOptionPane.showMessageDialog(Gems.this.m_frame, string2, "Disable SS Object", 1);
        }
    }

    class EnableSSAction
            implements ActionListener {
        EnableSSAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("SubStation")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent().getParent();
            }
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null || !((String) defaultMutableTreeNode.getUserObject()).startsWith("Disabled")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Recipe or Trigger to Enable!", "Enable SS Object", 1);
                return;
            }
            String string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol(2);
            GemsSSNode gemsSSNode = null;
            String string3 = "";
            if (!defaultMutableTreeNode2.getClass().getSimpleName().equals("GemsSSNode")) {
                return;
            }
            gemsSSNode = (GemsSSNode) defaultMutableTreeNode2;
            String string4 = ((String) ((DefaultMutableTreeNode) defaultMutableTreeNode.getParent()).getUserObject()).substring(5);
            string3 = string2.equals("Recipe") ? gemsSSNode.RunCommand("ENABLE,RID=" + string + ",INTF=" + string4) : gemsSSNode.RunCommand("ENABLE,TID=" + string + ",INTF=" + string4);
            JOptionPane.showMessageDialog(Gems.this.m_frame, string3, "Enable SS Object", 1);
        }
    }

    class RefreshSSAction
            implements ActionListener {
        RefreshSSAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("SubStation")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent().getParent();
            }
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null || !((String) defaultMutableTreeNode.getUserObject()).startsWith("Active") && !((String) defaultMutableTreeNode.getUserObject()).startsWith("Disabled")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Recipe or Trigger to refresh!", "Refresh SS Object", 1);
                return;
            }
            String string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            String string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol(2);
            GemsSSNode gemsSSNode = null;
            String string3 = "";
            if (!defaultMutableTreeNode2.getClass().getSimpleName().equals("GemsSSNode")) {
                return;
            }
            gemsSSNode = (GemsSSNode) defaultMutableTreeNode2;
            String string4 = ((String) ((DefaultMutableTreeNode) defaultMutableTreeNode.getParent()).getUserObject()).substring(5);
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active R")) {
                string3 = gemsSSNode.RunCommand("REFRESH,RID=" + string + ",INTF=" + string4);
            }
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Active T")) {
                string3 = gemsSSNode.RunCommand("REFRESH,TID=" + string + ",INTF=" + string4);
            }
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Disabled")) {
                string3 = string2.equals("Recipe") ? gemsSSNode.RunCommand("REFRESH,RID=" + string + ",INTF=" + string4) : gemsSSNode.RunCommand("REFRESH,TID=" + string + ",INTF=" + string4);
            }
            JOptionPane.showMessageDialog(Gems.this.m_frame, string3, "Refresh SS Object", 1);
        }
    }

    class BrowseQueueAction
            implements ActionListener {
        BrowseQueueAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("queue")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Queue to browse!", "Browse Queue", 1);
                return;
            }
            String string = new String();
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Queues")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Queues")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                new GemsQueueBrowser(gemsConnectionNode, string);
            }
        }
    }

    class DeleteTopicAction
            implements ActionListener {
        DeleteTopicAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            String string = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
                String string2 = (String) defaultMutableTreeNode.getUserObject();
                if (string2.startsWith("Topics")) {
                    string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                    string = string2;
                }
            }
            if (string == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Topic to destroy!", "Destroy Topic", 1);
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Topic: " + string, "Destroy Topic", 0);
            if (n == 0) {
                Gems.this.m_treeModel.removeTopic(string);
            }
        }
    }

    private class CreateTopicAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }

            String topicName = JOptionPane.showInputDialog(Gems.this.m_frame, "Create Topic:");
            if (topicName != null) {
                Gems.this.m_treeModel.createTopic(topicName.trim(), false);
            }
        }
    }

    private class PurgeTopicAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode selectedNode = Gems.this.getSelectedNode();

            String topicName = null;
            if (selectedNode != null) {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();

                String userObject = (String) selectedNode.getUserObject();
                if (userObject.startsWith("Topics")) {
                    topicName = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                } else if ((parentNode != null) && ((String) parentNode.getUserObject()).startsWith("Topics")) {
                    topicName = userObject;
                }
            }

            if (topicName == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Topic to purge!", "Purge Topic", 1);
                return;
            }

            int result = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Purge Topic: " + topicName, "Purge Topic", 0);
            if (result == YES_OPTION) {
                Gems.this.m_treeModel.purgeTopic(topicName);
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    private class SetQueuePermissionsAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }

            DefaultMutableTreeNode selectedNode = Gems.this.getSelectedNode();
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (selectedNode == null || gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Queue to set permissions!", "Set Permissions On Queue", 1);
                return;
            }

            String string = "";
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
            if (((String) selectedNode.getUserObject()).startsWith("Queues")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (parentNode != null && ((String) parentNode.getUserObject()).startsWith("Queues")) {
                string = (String) selectedNode.getUserObject();
            }

            new GemsPermissionDialog(Gems.this.m_frame, "Set Queue Permissions", true, gemsConnectionNode, string);
        }
    }

    class SetTopicPermissionsAction
            implements ActionListener {
        SetTopicPermissionsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (defaultMutableTreeNode == null || gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Topic to set permissions!", "Set Permissions On Topic", 1);
                return;
            }
            defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            String string = new String();
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }
            GemsPermissionDialog gemsPermissionDialog = new GemsPermissionDialog(Gems.this.m_frame, "Set Topic Permissions", false, gemsConnectionNode, string);
        }
    }

    class SetAdminPermissionsAction
            implements ActionListener {
        SetAdminPermissionsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("user or group")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (defaultMutableTreeNode == null || gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a user or goup to set permissions!", "Set Admin Permissions", 1);
                return;
            }
            defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            String string = null;
            if (((String) defaultMutableTreeNode.getUserObject()).equals("Users")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).equals("Users")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }
            if (string != null) {
                GemsAdminPermissionDialog gemsAdminPermissionDialog = new GemsAdminPermissionDialog(Gems.this.m_frame, gemsConnectionNode, string, "User", Gems.this.m_detailsPanel.getModel().getSelectedCol(3));
            } else {
                String string2 = null;
                if (((String) defaultMutableTreeNode.getUserObject()).equals("Groups")) {
                    string2 = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).equals("Groups")) {
                    string2 = (String) defaultMutableTreeNode.getUserObject();
                }
                if (string2 != null) {
                    GemsAdminPermissionDialog gemsAdminPermissionDialog = new GemsAdminPermissionDialog(Gems.this.m_frame, gemsConnectionNode, string2, "Group", Gems.this.m_detailsPanel.getModel().getSelectedCol(3));
                } else {
                    GemsAdminPermissionDialog gemsAdminPermissionDialog = new GemsAdminPermissionDialog(Gems.this.m_frame, gemsConnectionNode);
                }
            }
        }
    }

    class MonitorTopicAction
            implements ActionListener {
        MonitorTopicAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (defaultMutableTreeNode == null || gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Topic to monitor!", "Monitor Topic", 1);
                return;
            }
            defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            String string = new String();
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }
            new GemsTopicSubscriber(gemsConnectionNode, "$sys.monitor.T.*." + string, "Topic Monitor");
        }
    }

    class RequestReplyTesterAction
            implements ActionListener {
        boolean m_isQueue;

        public RequestReplyTesterAction(boolean bl) {
            this.m_isQueue = true;
            this.m_isQueue = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select an EMS server in the tree view!", "Request/Reply Tester", 1);
                return;
            }
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }
            if (this.m_isQueue) {
                String string = new String();
                if (defaultMutableTreeNode != null) {
                    if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Queues")) {
                        string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                    } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Queues")) {
                        string = (String) defaultMutableTreeNode.getUserObject();
                    }
                }
                new GemsReqReplyTester(gemsConnectionNode, string, true, "Queue Request/Reply Tester");
            } else {
                String string = new String();
                if (defaultMutableTreeNode != null) {
                    if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                        string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                    } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                        string = (String) defaultMutableTreeNode.getUserObject();
                    }
                }
                new GemsReqReplyTester(gemsConnectionNode, string, false, "Topic Request/Reply Tester");
            }
        }
    }

    class MonitorReqReplyTopicAction
            implements ActionListener {
        MonitorReqReplyTopicAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (defaultMutableTreeNode == null || gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Request Topic to monitor!", "Monitor Request/Reply Topic", 1);
                return;
            }
            defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            String string = new String();
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }
            new GemsReqReplyMonitor(gemsConnectionNode, string, false, "Topic Request/Reply Monitor");
        }
    }

    class SubscribeTopicAction
            implements ActionListener {
        SubscribeTopicAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (defaultMutableTreeNode == null || gemsConnectionNode == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Topic to subscribe to!", "Subscribe To Topic", 1);
                return;
            }
            defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            String string = new String();
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }
            new GemsTopicSubscriber(gemsConnectionNode, string, "Topic Subscriber");
        }
    }

    class PurgeTMPTopicsAction
            implements ActionListener {
        PurgeTMPTopicsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            Object var3_3 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            GemsPurgeTopics gemsPurgeTopics = new GemsPurgeTopics(Gems.this.m_frame, gemsConnectionNode, "$TMP$.>");
        }
    }

    class PurgeTopicsAction
            implements ActionListener {
        PurgeTopicsAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            Object var3_3 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            GemsPurgeTopics gemsPurgeTopics = new GemsPurgeTopics(Gems.this.m_frame, gemsConnectionNode);
        }
    }

    class PurgeTMPQueuesAction
            implements ActionListener {
        PurgeTMPQueuesAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            Object var3_3 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            GemsPurgeQueues gemsPurgeQueues = new GemsPurgeQueues(Gems.this.m_frame, gemsConnectionNode, "$TMP$.>");
        }
    }

    class PurgeQueuesAction
            implements ActionListener {
        PurgeQueuesAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            Object var3_3 = null;
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            GemsPurgeQueues gemsPurgeQueues = new GemsPurgeQueues(Gems.this.m_frame, gemsConnectionNode);
        }
    }

    class SetServerPropertyAction
            implements ActionListener {
        SetServerPropertyAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }
            GemsConnectionNode gemsConnectionNode = Gems.this.getConnectionNode();
            if (gemsConnectionNode != null) {
                if (!Gems.this.isStandbyOpsAllowed(gemsConnectionNode)) {
                    return;
                }
                gemsConnectionNode.setServerProperty("");
            }
        }
    }

    class PurgeQueueAction
            implements ActionListener {
        PurgeQueueAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("queue")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            String string = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
                String string2 = (String) defaultMutableTreeNode.getUserObject();
                if (string2.startsWith("Queues")) {
                    string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Queues")) {
                    string = string2;
                }
            }
            if (string == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Queue to purge!", "Purge Queue", 1);
                return;
            }
            int n = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Purge Queue: " + string, "Purge Queue", 0);
            if (n == 0) {
                Gems.this.m_treeModel.purgeQueue(string);
                Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
            }
        }
    }

    class SetTopicPropertyAction
            implements ActionListener {
        SetTopicPropertyAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            String string;
            Object object;
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            string = null;
            GemsTopicNode gemsTopicNode = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
                String userObject = (String) defaultMutableTreeNode.getUserObject();
                if (userObject.startsWith("Topics")) {
                    string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                    if (string != null) {
                        Enumeration enumeration = defaultMutableTreeNode.children();
                        while (enumeration.hasMoreElements()) {
                            DefaultMutableTreeNode defaultMutableTreeNode3 = (DefaultMutableTreeNode) enumeration.nextElement();
                            if (defaultMutableTreeNode3 == null || !string.equals((String) defaultMutableTreeNode3.getUserObject()))
                                continue;
                            gemsTopicNode = (GemsTopicNode) defaultMutableTreeNode3;
                            break;
                        }
                    }
                } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                    gemsTopicNode = (GemsTopicNode) defaultMutableTreeNode;
                    string = (String) gemsTopicNode.getUserObject();
                }
            }
            object = new GemsDestPropEditor(Gems.this.m_frame, Gems.this.getConnectionNode(), "Topic", string);
        }
    }

    private class SetQueuePropertyAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            String string;
            if (!Gems.this.checkConnected("queue")) {
                return;
            }

            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            string = null;
            GemsQueueNode gemsQueueNode = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
                String userObject = (String) defaultMutableTreeNode.getUserObject();
                if (userObject.startsWith("Queues")) {
                    string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                    if (string != null) {
                        Enumeration enumeration = defaultMutableTreeNode.children();
                        while (enumeration.hasMoreElements()) {
                            DefaultMutableTreeNode defaultMutableTreeNode3 = (DefaultMutableTreeNode) enumeration.nextElement();
                            if (defaultMutableTreeNode3 == null || !string.equals((String) defaultMutableTreeNode3.getUserObject()))
                                continue;
                            gemsQueueNode = (GemsQueueNode) defaultMutableTreeNode3;
                            break;
                        }
                    }
                } else if (defaultMutableTreeNode2 != null && ((String) defaultMutableTreeNode2.getUserObject()).startsWith("Queues")) {
                    gemsQueueNode = (GemsQueueNode) defaultMutableTreeNode;
                    string = (String) gemsQueueNode.getUserObject();
                }
            }
            new GemsDestPropEditor(Gems.this.m_frame, Gems.this.getConnectionNode(), "Queue", string);
        }
    }

    private class DeleteQueueAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("queue")) {
                return;
            }

            String name = null;
            DefaultMutableTreeNode selectedNode = Gems.this.getSelectedNode();
            if (selectedNode != null) {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                String selectedUserObject = (String) selectedNode.getUserObject();

                if (selectedUserObject.startsWith("Queues")) {
                    name = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
                } else if (parentNode != null && ((String) parentNode.getUserObject()).startsWith("Queues")) {
                    name = selectedUserObject;
                }
            }

            if (name == null || name.startsWith("$")) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Queue to destroy!", "Destroy Queue", 1);
                return;
            }

            int result = JOptionPane.showConfirmDialog(Gems.this.m_frame, "Destroy Queue: " + name, "Destroy Queue", 0);
            if (result == YES_OPTION) {
                Gems.this.m_treeModel.removeQueue(name);
            }
        }
    }

    private class CreateQueueAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(hBox("Queue Name:", 5, "For routed queue use queueName@serverName"), BorderLayout.NORTH);

            JTextArea queuesTextArea = new JTextArea(10, 30);
            mainPanel.add(vBox(5, new JScrollPane(queuesTextArea), 5), BorderLayout.CENTER);

            JTextField separatorTextField = new JTextField();
            mainPanel.add(hBox("Separator: ", separatorTextField), BorderLayout.SOUTH);

            int result = JOptionPane.showConfirmDialog(null, mainPanel, "Create Queue", 2);
            String queuesText = queuesTextArea.getText().trim();
            if ((result == YES_OPTION) && queuesText.length() > 0) {
                String separator = separatorTextField.getText().trim();
                if (separator.isEmpty()) {
                    separator = " ";
                }

                String[] queues = queuesText.split(separator);
                for (String queue : queues) {
                    String queueName = queue.trim();
                    System.out.println("Creating " + queueName);
                    Gems.this.m_treeModel.createQueue(queueName, false);
                }
            }
        }
    }

    private class SetServerTraceAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected(null)) {
                return;
            }

            Gems.this.m_treeModel.setServerTrace();
        }
    }

    private class AboutAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            JOptionPane.showMessageDialog(Gems.this.m_frame, "<html><font size=\"5\">Gems v3.4</font><p><font size =\"4\">Graphical EMS Administration and Monitoring Tool</font><p><p>This software comes with no warranties of any kind. <p>See Terms of Use at: <a href=\"http://www.tibcommunity.com\">http://www.tibcommunity.com</a></html>", "About Gems", 1, Gems.this.m_icon);
        }
    }

    class ShowLicense implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            GemsLicenseDialog gemsLicenseDialog = new GemsLicenseDialog(Gems.this.m_frame);
            gemsLicenseDialog.show();
        }
    }

    private class ShowHelp implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            GemsBrowser gemsBrowser = new GemsBrowser("file:doc/index.htm");
        }
    }

    private class PublishMessageAction implements ActionListener {
        boolean m_isMap;

        PublishMessageAction(boolean bl) {
            this.m_isMap = false;
            this.m_isMap = bl;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("topic")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;
            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }
            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Topic to publish to!", "Publish Message", 1);
                return;
            }

            String string = "";
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Topics")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (((String) defaultMutableTreeNode2.getUserObject()).startsWith("Topics")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }

            GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(Gems.this.getConnectionNode(), true, string, false, Gems.this.m_frame, false, this.m_isMap);
        }
    }

    private class SendMessageAction implements ActionListener {
        private boolean m_isMap;

        SendMessageAction(boolean isMap) {
            this.m_isMap = isMap;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (!Gems.this.checkConnected("queue")) {
                return;
            }
            DefaultMutableTreeNode defaultMutableTreeNode = Gems.this.getSelectedNode();
            DefaultMutableTreeNode defaultMutableTreeNode2 = null;

            if (defaultMutableTreeNode != null) {
                defaultMutableTreeNode2 = (DefaultMutableTreeNode) defaultMutableTreeNode.getParent();
            }

            if (defaultMutableTreeNode == null || defaultMutableTreeNode2 == null) {
                JOptionPane.showMessageDialog(Gems.this.m_frame, "Select a Queue to send to!", "Send Message", 1);
                return;
            }

            String string = "";
            if (((String) defaultMutableTreeNode.getUserObject()).startsWith("Queues")) {
                string = Gems.this.m_detailsPanel.getModel().getSelectedCol1();
            } else if (((String) defaultMutableTreeNode2.getUserObject()).startsWith("Queues")) {
                string = (String) defaultMutableTreeNode.getUserObject();
            }

            GemsMessageFrame gemsMessageFrame = new GemsMessageFrame(Gems.this.getConnectionNode(), true, string, true, Gems.this.m_frame, false, this.m_isMap);
        }
    }

    class RefreshTreeViewAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Gems.this.m_treeModel.reloadTree();
        }
    }

    private class RefreshAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Gems.this.treeSelectionChange(Gems.this.getSelectedNode(), false);
        }
    }

    private class AddConnectionAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionNode gemsConnectionNode = Gems.this.m_treeModel.newJMSConnection("EMS Connection");
        }
    }

    private class ShowTotalsAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Gems.this.setShowTotals(!Gems.this.getShowTotals());
        }
    }

    GemsSSNode getSSConnectionNode() {
        TreePath treePath = this.m_tree.getSelectionPath();

        if (treePath != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (selectedNode.isRoot()) {
                return null;
            }

            GemsSSNode gemsSSNode = null;
            while (selectedNode.getLevel() > 0) {
                if (selectedNode instanceof GemsSSNode) {
                    gemsSSNode = (GemsSSNode) selectedNode;
                    break;
                }

                selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
            }

            return gemsSSNode;
        }

        return null;
    }

    private boolean checkConnected(String string) {
        GemsConnectionNode gemsConnectionNode = this.getConnectionNode();

        if (gemsConnectionNode == null || !gemsConnectionNode.isConnected()) {
            if (string != null) {
                JOptionPane.showMessageDialog(this.m_frame, "Select a " + string + " for an EMS server connection!", "Error", 1);
            } else {
                JOptionPane.showMessageDialog(this.m_frame, "Select an EMS server connection in the tree view", "Error", 1);
            }

            return false;
        }

        return true;
    }

    GemsConnectionNode getConnectionNode() {
        TreePath treePath = this.m_tree.getSelectionPath();

        if (treePath != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (selectedNode.isRoot()) {
                return null;
            }

            GemsConnectionNode gemsConnectionNode = null;
            while (selectedNode.getLevel() > 0) {
                if (selectedNode instanceof GemsConnectionNode) {
                    gemsConnectionNode = (GemsConnectionNode) selectedNode;
                    break;
                }

                selectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
            }

            return gemsConnectionNode;
        }

        return null;
    }

    protected TreePath[] getSelectedPaths() {
        return this.m_tree.getSelectionPaths();
    }
}

