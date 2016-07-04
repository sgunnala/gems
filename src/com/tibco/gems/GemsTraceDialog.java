/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.TraceInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import com.tibco.tibjms.admin.TraceInfo;

public class GemsTraceDialog
        extends JDialog {
    protected JTextField m_traceText;
    protected JButton m_setButton;
    protected JButton m_addButton;
    protected JButton m_removeButton;
    protected JButton m_clearButton;
    protected JButton m_applyButton;
    protected JButton m_closeButton;
    protected JRadioButton m_consoleButton;
    protected JRadioButton m_logfileButton;
    JFrame m_frame;
    JPanel m_panel;
    GemsConnectionNode m_cn;
    TraceInfo m_ti;
    boolean m_isLogTrace = true;

    public GemsTraceDialog(JFrame jFrame, GemsConnectionNode gemsConnectionNode) {
        super(jFrame, "Configure Server Trace: (" + gemsConnectionNode.getName() + ")", true);
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.init();
        this.reset();
        this.pack();
        this.show();
    }

    public void init() {
        this.setLocationRelativeTo(this.m_frame);
        this.setDefaultCloseOperation(2);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel2.setBorder(new EtchedBorder());
        jPanel.add((Component) jPanel2, "North");
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        jPanel2.add(jPanel3);
        this.m_consoleButton = new JRadioButton("Console Trace");
        this.m_consoleButton.addActionListener(new consolePressed());
        this.m_logfileButton = new JRadioButton("Log Trace");
        this.m_logfileButton.setSelected(true);
        this.m_logfileButton.addActionListener(new logPressed());
        Component component = Box.createRigidArea(new Dimension(100, 10));
        jPanel3.add(component);
        jPanel3.add(this.m_consoleButton);
        component = Box.createRigidArea(new Dimension(70, 10));
        jPanel3.add(component);
        jPanel3.add(this.m_logfileButton);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.m_consoleButton);
        buttonGroup.add(this.m_logfileButton);
        JLabel jLabel = new JLabel("Current Trace Setting:");
        jPanel2.add(jLabel);
        this.m_traceText = new JTextField("", 30);
        this.m_traceText.setEditable(false);
        jPanel2.add(this.m_traceText);
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        jPanel2.add(jPanel4);
        component = Box.createRigidArea(new Dimension(70, 10));
        jPanel4.add(component);
        this.m_setButton = new JButton("Set...");
        this.m_setButton.addActionListener(new setPressed());
        jPanel4.add(this.m_setButton);
        component = Box.createRigidArea(new Dimension(30, 10));
        jPanel4.add(component);
        this.m_addButton = new JButton("Add...");
        this.m_addButton.addActionListener(new addPressed());
        jPanel4.add(this.m_addButton);
        component = Box.createRigidArea(new Dimension(30, 10));
        jPanel4.add(component);
        this.m_removeButton = new JButton("Remove...");
        this.m_removeButton.addActionListener(new removePressed());
        jPanel4.add(this.m_removeButton);
        component = Box.createRigidArea(new Dimension(30, 10));
        jPanel4.add(component);
        this.m_clearButton = new JButton("Clear");
        this.m_clearButton.addActionListener(new clearPressed());
        jPanel4.add(this.m_clearButton);
        component = Box.createRigidArea(new Dimension(30, 10));
        jPanel4.add(component);
        Font font = new Font("Serif", 0, 11);
        JLabel jLabel2 = new JLabel("DEFAULT includes ACL, ADMIN, CONFIG, CONNECT_ERROR, INFO, LIMITS, MSG, ROUTE, RVADV, WARNING");
        jLabel2.setFont(font);
        jPanel2.add(jLabel2);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel2.add(component);
        JPanel jPanel5 = new JPanel(true);
        jPanel5.setLayout(new BoxLayout(jPanel5, 0));
        component = Box.createRigidArea(new Dimension(190, 10));
        jPanel5.add(component);
        this.m_applyButton = new JButton("Apply");
        this.m_applyButton.addActionListener(new applyPressed());
        this.m_closeButton = new JButton("Close");
        this.m_closeButton.addActionListener(new closePressed());
        jPanel5.add(this.m_applyButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel5.add(component);
        jPanel5.add(this.m_closeButton);
        jPanel.add((Component) jPanel5, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 6, 1, 10, 10, 10, 10);
    }

    public void reset() {
        this.m_ti = this.m_cn.getServerTrace(this.m_isLogTrace);
        this.update();
    }

    public void update() {
        this.m_traceText.setText(GemsTraceDialog.TraceInfoToString(this.m_ti));
    }

    public static String TraceInfoToString(TraceInfo traceInfo) {
        String string = "set=";
        long l = traceInfo.getTraceSetItems();
        string = string + GemsTraceDialog.TraceItemsToString(l, "");
        string = string + ";add=";
        l = traceInfo.getTraceAddItems();
        string = string + GemsTraceDialog.TraceItemsToString(l, "+");
        string = string + ";remove=";
        l = traceInfo.getTraceRemoveItems();
        string = string + GemsTraceDialog.TraceItemsToString(l, "-");
        return string;
    }

    public static String TraceItemsToString(long l, String string) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean bl = false;
        if ((l & Trace.DEFAULT.id) == Trace.DEFAULT.id) {
            bl = true;
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.DEFAULT);
            stringBuffer.append(',');
        }
        if ((l & Trace.MEMORY.id) == Trace.MEMORY.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.MEMORY);
            stringBuffer.append(',');
        }
        if ((l & Trace.MEMORY_DEBUG.id) == Trace.MEMORY_DEBUG.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.MEMORY_DEBUG);
            stringBuffer.append(',');
        }
        if ((l & Trace.JAAS.id) == Trace.JAAS.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.JAAS);
            stringBuffer.append(',');
        }
        if ((l & Trace.JVM.id) == Trace.JVM.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.JVM);
            stringBuffer.append(',');
        }
        if ((l & Trace.JVMERR.id) == Trace.JVMERR.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.JVMERR);
            stringBuffer.append(',');
        }
        if ((l & Trace.DBSTORE.id) == Trace.DBSTORE.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.DBSTORE);
            stringBuffer.append(',');
        }
        if ((l & Trace.LOAD.id) == Trace.LOAD.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.LOAD);
            stringBuffer.append(',');
        }
        if ((l & Trace.MULTICAST.id) == Trace.MULTICAST.id) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.MULTICAST);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.ACL.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.ACL);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.ADMIN.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.ADMIN);
            stringBuffer.append(',');
        }
        if ((l & Trace.AUTH.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.AUTH);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.CONFIG.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.CONFIG);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.CONFIG_DETAIL.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.CONFIG_DETAIL);
            stringBuffer.append(',');
        }
        if ((l & Trace.CONNECT.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.CONNECT);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.CONNECT_ERROR.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.CONNECT_ERROR);
            stringBuffer.append(',');
        }
        if ((l & Trace.DEST.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.DEST);
            stringBuffer.append(',');
        }
        if ((l & Trace.FLOW.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.FLOW);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.INFO.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.INFO);
            stringBuffer.append(',');
        }
        if ((l & Trace.LDAP_DEBUG.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.LDAP_DEBUG);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.LIMITS.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.LIMITS);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.MSG.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.MSG);
            stringBuffer.append(',');
        }
        if ((l & Trace.PRODCONS.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.PRODCONS);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.ROUTE.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.ROUTE);
            stringBuffer.append(',');
        }
        if ((l & Trace.ROUTE_DEBUG.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.ROUTE_DEBUG);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.RVADV.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.RVADV);
            stringBuffer.append(',');
        }
        if ((l & Trace.SS.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.SS);
            stringBuffer.append(',');
        }
        if ((l & Trace.SSL.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.SSL);
            stringBuffer.append(',');
        }
        if ((l & Trace.SSL_DEBUG.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.SSL_DEBUG);
            stringBuffer.append(',');
        }
        if ((l & Trace.TX.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.TX);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.WARNING.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.WARNING);
            stringBuffer.append(',');
        }
        if (!bl && (l & Trace.MSTORE.id) != 0) {
            stringBuffer.append(string);
            stringBuffer.append((Object) Trace.MSTORE);
            stringBuffer.append(',');
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.setLength(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public void cancel() {
        this.dispose();
    }

    public void dispose() {
        super.dispose();
    }

    public void doSetTrace() {
        GemsTracePopup gemsTracePopup = new GemsTracePopup(this, this.m_setButton);
        Vector vector = gemsTracePopup.getSelectedTraceItems();
        if (vector != null) {
            long l = 0;
            for (int i = 0; i < vector.size(); ++i) {
                l |= ((Trace) vector.get((int) i)).id;
            }
            this.m_ti.setTraceItems(l);
            this.update();
        }
    }

    public void doAddTrace() {
        GemsTracePopup gemsTracePopup = new GemsTracePopup(this, this.m_addButton);
        Vector vector = gemsTracePopup.getSelectedTraceItems();
        if (vector != null) {
            long l = 0;
            for (int i = 0; i < vector.size(); ++i) {
                l |= ((Trace) vector.get((int) i)).id;
            }
            this.m_ti.clearAddTraceItems();
            this.m_ti.addTraceItems(l);
            this.update();
        }
    }

    public void doRemoveTrace() {
        GemsTracePopup gemsTracePopup = new GemsTracePopup(this, this.m_removeButton);
        Vector vector = gemsTracePopup.getSelectedTraceItems();
        if (vector != null) {
            long l = 0;
            for (int i = 0; i < vector.size(); ++i) {
                l |= ((Trace) vector.get((int) i)).id;
            }
            this.m_ti.clearRemoveTraceItems();
            this.m_ti.removeTraceItems(l);
            this.update();
        }
    }

    void debugDump() {
        System.out.println("TRACE_MEMORY 2097152");
        System.out.println("TRACE_MEMORY_DEBUG 4194304");
        System.out.println("TRACE_JAAS 536870912");
        System.out.println("TRACE_JVM 268435456");
        System.out.println("TRACE_JVMERR 4294967296");
        System.out.println("TRACE_DBSTORE 2147483648");
        System.out.println("TRACE_MULTICAST 1073741824");
        System.out.println("TRACE_SS 1048576");
        System.out.println("TRACE_MSTORE 8589934592");
        System.out.println("TRACE_LOADER 17179869184");
        System.out.println("TRACE_CONFIG_DETAIL 34359738368");
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Trace {
        DEFAULT(67204336),
        ACL(64),
        ADMIN(4096),
        AUTH(0x2000000),
        CONFIG(8192),
        CONFIG_DETAIL(0x800000000L),
        CONNECT(32768),
        CONNECT_ERROR(65536),
        DBSTORE(0x80000000L),
        DEST(262144),
        FLOW(0x8000000),
        INFO(16),
        JAAS(0x20000000),
        JVM(0x10000000),
        JVMERR(0x100000000L),
        LDAP_DEBUG(0x1000000),
        LIMITS(128),
        LOAD(0x400000000L),
        MEMORY(0x200000),
        MEMORY_DEBUG(0x400000),
        MSG(0x4000000),
        MSTORE(0x200000000L),
        MULTICAST(0x40000000),
        PRODCONS(131072),
        ROUTE(1024),
        ROUTE_DEBUG(2048),
        RVADV(16384),
        SS(0x100000),
        SSL(256),
        SSL_DEBUG(512),
        TX(524288),
        WARNING(32);

        public final long id;

        private Trace(long l) {
            this.id = l;
        }
    }

    class logPressed
            implements ActionListener {
        logPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.m_isLogTrace = true;
            GemsTraceDialog.this.reset();
        }
    }

    class consolePressed
            implements ActionListener {
        consolePressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.m_isLogTrace = false;
            GemsTraceDialog.this.reset();
        }
    }

    class clearPressed
            implements ActionListener {
        clearPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.m_ti.clearAllTraceItems();
            GemsTraceDialog.this.update();
        }
    }

    class removePressed
            implements ActionListener {
        removePressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.doRemoveTrace();
        }
    }

    class addPressed
            implements ActionListener {
        addPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.doAddTrace();
        }
    }

    class setPressed
            implements ActionListener {
        setPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.doSetTrace();
        }
    }

    class closePressed
            implements ActionListener {
        closePressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.cancel();
        }
    }

    class applyPressed
            implements ActionListener {
        applyPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsTraceDialog.this.m_cn.setServerTrace(GemsTraceDialog.this.m_ti, GemsTraceDialog.this.m_isLogTrace);
            GemsTraceDialog.this.reset();
        }
    }

}

