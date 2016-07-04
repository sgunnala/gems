/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GemsConnectionPanel
        extends JPanel {
    protected GemsTreeModel m_treeModel;
    protected JTextField m_alias;
    protected JTextField m_url;
    protected JTextField m_user;
    protected JTextField m_password;
    protected JButton m_connButton;
    protected JButton m_disconnButton;

    public GemsConnectionPanel(GemsTreeModel gemsTreeModel) {
        super(true);
        this.setLayout(new BoxLayout(this, 3));
        this.m_treeModel = gemsTreeModel;
        JPanel jPanel = new JPanel(new SpringLayout(), true);
        this.add(jPanel);
        JLabel jLabel = new JLabel("Connection Alias:", 11);
        this.m_alias = new JTextField("EMS Connection", 20);
        this.m_alias.setMaximumSize(new Dimension(1000, 24));
        jLabel.setLabelFor(this.m_alias);
        jPanel.add(jLabel);
        jPanel.add(this.m_alias);
        JLabel jLabel2 = new JLabel("URL:", 11);
        this.m_url = new JTextField("tcp://localhost:7222", 20);
        this.m_url.setMaximumSize(new Dimension(0, 24));
        jLabel2.setLabelFor(this.m_url);
        jPanel.add(jLabel2);
        jPanel.add(this.m_url);
        JLabel jLabel3 = new JLabel("Admin Username:", 11);
        this.m_user = new JTextField("admin", 20);
        this.m_user.setMaximumSize(new Dimension(0, 24));
        jLabel3.setLabelFor(this.m_user);
        jPanel.add(jLabel3);
        jPanel.add(this.m_user);
        JLabel jLabel4 = new JLabel("Password:", 11);
        this.m_password = new JPasswordField(20);
        this.m_password.setMaximumSize(new Dimension(0, 24));
        jLabel4.setLabelFor(this.m_password);
        jPanel.add(jLabel4);
        jPanel.add(this.m_password);
        JPanel jPanel2 = new JPanel(true);
        jPanel2.setLayout(new BoxLayout(jPanel2, 2));
        this.m_connButton = new JButton("Connect");
        this.m_connButton.addActionListener(new ConnectPressed());
        this.m_disconnButton = new JButton("Disconnect");
        this.m_disconnButton.addActionListener(new DisconnectPressed());
        this.m_disconnButton.setEnabled(false);
        jPanel2.add(this.m_connButton);
        jPanel2.add(this.m_disconnButton);
        this.add(jPanel2);
        SpringUtilities.makeCompactGrid(jPanel, 4, 2, 5, 5, 5, 5);
    }

    public void setDetails(String string, String string2, String string3, boolean bl) {
        this.m_alias.setText(string);
        this.m_url.setText(string2);
        this.m_user.setText(string3);
        if (bl) {
            this.m_connButton.setEnabled(false);
            this.m_disconnButton.setEnabled(true);
            this.m_alias.setEnabled(false);
            this.m_url.setEnabled(false);
            this.m_user.setEnabled(false);
            this.m_password.setEnabled(false);
        } else {
            this.m_connButton.setEnabled(true);
            this.m_disconnButton.setEnabled(false);
            this.m_alias.setEnabled(true);
            this.m_url.setEnabled(true);
            this.m_user.setEnabled(true);
            this.m_password.setEnabled(true);
        }
    }

    class DisconnectPressed
            implements ActionListener {
        DisconnectPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsConnectionPanel.this.m_treeModel.disconnectCurrentNode();
            GemsConnectionPanel.this.m_connButton.setEnabled(true);
            GemsConnectionPanel.this.m_disconnButton.setEnabled(false);
        }
    }

    class ConnectPressed
            implements ActionListener {
        ConnectPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsConnectionPanel.this.m_treeModel.connectCurrentNode(GemsConnectionPanel.this.m_alias.getText(), GemsConnectionPanel.this.m_url.getText(), GemsConnectionPanel.this.m_user.getText(), GemsConnectionPanel.this.m_password.getText())) {
                GemsConnectionPanel.this.m_connButton.setEnabled(false);
                GemsConnectionPanel.this.m_disconnButton.setEnabled(true);
            } else {
                GemsConnectionPanel.this.m_connButton.setEnabled(true);
                GemsConnectionPanel.this.m_disconnButton.setEnabled(false);
            }
        }
    }

}

