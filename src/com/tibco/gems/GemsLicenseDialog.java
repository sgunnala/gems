/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GemsLicenseDialog
        extends JDialog {
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel lblWarning;
    private JScrollPane scrollPane1;
    private JTextPane txtLicense;
    private JPanel buttonBar;
    private JButton okButton;

    public GemsLicenseDialog(Frame frame) {
        super(frame);
        this.initComponents();
    }

    private void initComponents() {
        this.dialogPane = new JPanel();
        this.contentPanel = new JPanel();
        this.lblWarning = new JLabel();
        this.scrollPane1 = new JScrollPane();
        this.txtLicense = new JTextPane();
        this.buttonBar = new JPanel();
        this.okButton = new JButton();
        this.setTitle("Gems License");
        this.setModal(true);
        this.setDefaultCloseOperation(2);
        this.setResizable(false);
        this.dialogPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.dialogPane.setLayout(new BorderLayout(20, 20));
        this.contentPanel.setLayout(new BorderLayout(10, 10));
        this.lblWarning.setText("By using this software you agree to the following license agreement:");
        this.lblWarning.setFont(new Font("Tahoma", 1, 14));
        this.contentPanel.add((Component) this.lblWarning, "First");
        this.scrollPane1.setHorizontalScrollBarPolicy(31);
        this.scrollPane1.setVerticalScrollBarPolicy(22);
        this.scrollPane1.setPreferredSize(new Dimension(550, 475));
        this.txtLicense.setText("text");
        this.txtLicense.setEditable(false);
        this.txtLicense.setFont(new Font("Courier New", 0, 11));
        this.scrollPane1.setViewportView(this.txtLicense);
        this.contentPanel.add((Component) this.scrollPane1, "Center");
        this.dialogPane.add((Component) this.contentPanel, "Center");
        this.okButton.setText("OK");
        this.okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsLicenseDialog.this.okButtonActionPerformed();
            }
        });
        this.buttonBar.add(this.okButton);
        this.dialogPane.add((Component) this.buttonBar, "Last");
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
        this.txtLicense.setText(License.getLicenseAgreement());
        this.txtLicense.setCaretPosition(0);
        this.setContentPane(this.dialogPane);
        this.pack();
    }

    private void okButtonActionPerformed() {
        this.dispose();
    }

    public GemsLicenseDialog(Dialog dialog) {
        super(dialog);
        this.initComponents();
    }

}

