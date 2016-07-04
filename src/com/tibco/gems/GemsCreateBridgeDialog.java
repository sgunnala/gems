/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.DestinationBridgeInfo
 *  com.tibco.tibjms.admin.TibjmsAdmin
 *  com.tibco.tibjms.admin.TibjmsAdminException
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.tibco.tibjms.admin.DestinationBridgeInfo;
import com.tibco.tibjms.admin.TibjmsAdminException;

public class GemsCreateBridgeDialog
        extends JPanel {
    JFrame m_frame;
    GemsConnectionNode m_cn;
    boolean m_isQueue;
    String m_dest;
    JTextField m_sel;
    JTextField m_tar;
    JComboBox m_tartype;

    public GemsCreateBridgeDialog(JFrame jFrame, GemsConnectionNode gemsConnectionNode, boolean bl, String string) {
        super(new SpringLayout());
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_isQueue = bl;
        this.m_dest = string;
        this.init();
    }

    public void init() {
        this.add(new JLabel("Source " + (this.m_isQueue ? "Queue:" : "Topic")));
        JTextField jTextField = new JTextField(25);
        jTextField.setText(this.m_dest);
        jTextField.setEditable(false);
        this.add(jTextField);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BoxLayout(jPanel, 0));
        this.add(new JLabel("Target Type:"));
        this.m_tartype = new JComboBox();
        this.m_tartype.addItem("Queue");
        this.m_tartype.addItem("Topic");
        this.add(this.m_tartype);
        this.add(new JLabel("Target Destination:"));
        this.m_tar = new JTextField(25);
        jPanel.add(this.m_tar);
        JButton jButton = new JButton("...");
        jButton.setPreferredSize(new Dimension(18, 16));
        jButton.addActionListener(new DestinationWizardAction());
        jPanel.add(jButton);
        this.add(jPanel);
        this.add(new JLabel("Selector:"));
        this.m_sel = new JTextField(25);
        this.add(this.m_sel);
        SpringUtilities.makeCompactGrid(this, 4, 2, 5, 5, 5, 5);
    }

    public boolean createBridge() {
        int n = JOptionPane.showConfirmDialog(this.m_frame, this, "Create Bridge", 2);
        if (n == 0 && this.m_cn != null && this.m_cn.m_adminConn != null) {
            try {
                DestinationBridgeInfo destinationBridgeInfo = new DestinationBridgeInfo(this.m_isQueue ? 1 : 2, this.m_dest, this.m_tartype.getSelectedItem().equals("Queue") ? 1 : 2, this.m_tar.getText(), this.m_sel.getText().length() > 0 ? this.m_sel.getText() : null);
                this.m_cn.m_adminConn.createDestinationBridge(destinationBridgeInfo);
            } catch (TibjmsAdminException var2_3) {
                JOptionPane.showMessageDialog(this.m_frame, var2_3.getMessage(), "Error", 1);
                return false;
            }
            return true;
        }
        return false;
    }

    class DestinationWizardAction
            implements ActionListener {
        DestinationWizardAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsCreateBridgeDialog.this.m_frame, GemsCreateBridgeDialog.this.m_cn, GemsCreateBridgeDialog.this.m_tartype.getSelectedItem().equals("Queue") ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsCreateBridgeDialog.this.m_tar.setText(gemsDestinationPicker.m_retDest.m_destName);
            }
        }
    }

}

