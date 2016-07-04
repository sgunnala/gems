/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.*;

public class GemsConnectionPicker
        extends JDialog {
    protected JComboBox m_conn;
    protected JButton m_okButton;
    protected JButton m_cancelButton;
    protected Hashtable m_connTable;
    Frame m_frame;
    JPanel m_panel;
    GemsConnectionNode m_cn;
    GemsConnectionNode m_result = null;

    public GemsConnectionPicker(Frame frame, GemsConnectionNode gemsConnectionNode, String string) {
        super(frame, "Select the EMS server to " + string, true);
        this.setLocationRelativeTo(frame);
        this.setDefaultCloseOperation(2);
        this.m_frame = frame;
        this.m_cn = gemsConnectionNode;
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add((Component) jPanel2, "North");
        this.m_connTable = GemsConnectionNode.getConnections();
        JLabel jLabel = new JLabel("EMS Server:", 11);
        this.m_conn = new JComboBox();
        Enumeration enumeration = this.m_connTable.keys();
        if (this.m_connTable.size() == 0) {
            return;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            arrayList.add((String) name);
            Collections.sort(arrayList);
            this.m_conn.insertItemAt(name, arrayList.indexOf(name));
            if (gemsConnectionNode == null || !name.startsWith(gemsConnectionNode.getName())) continue;
            this.m_conn.setSelectedItem(name);
        }
        if (this.m_conn.getSelectedItem() == null) {
            this.m_conn.setSelectedIndex(0);
        }
        this.m_conn.setMaximumSize(new Dimension(0, 24));
        jLabel.setLabelFor(this.m_conn);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_conn);
        JPanel objectPanel = new JPanel(true);
        objectPanel.setLayout(new BoxLayout((Container) objectPanel, 0));
        Component component = Box.createRigidArea(new Dimension(110, 10));
        objectPanel.add(component);
        this.m_okButton = new JButton("Ok");
        this.m_okButton.addActionListener(new OkPressed());
        this.m_cancelButton = new JButton("Cancel");
        this.m_cancelButton.addActionListener(new CancelPressed());
        objectPanel.add(this.m_okButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        objectPanel.add(component);
        objectPanel.add(this.m_cancelButton);
        jPanel.add((Component) objectPanel, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 1, 2, 5, 5, 15, 15);
    }

    public GemsConnectionNode getConnection() {
        if (this.m_connTable.size() == 0) {
            return null;
        }
        if (this.m_connTable.size() == 1) {
            return (GemsConnectionNode) this.m_connTable.get(this.m_conn.getSelectedItem());
        }
        this.pack();
        this.show();
        return this.m_result;
    }

    public void ok() {
        this.m_result = (GemsConnectionNode) this.m_connTable.get(this.m_conn.getSelectedItem());
        this.dispose();
    }

    public void dispose() {
        super.dispose();
    }

    public void cancel() {
        this.m_result = null;
        this.dispose();
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionPicker.this.cancel();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionPicker.this.ok();
        }
    }

}

