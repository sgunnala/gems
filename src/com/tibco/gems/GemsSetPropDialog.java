/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.*;

public class GemsSetPropDialog
        extends JDialog {
    protected JComboBox m_props;
    protected JTextField m_type;
    protected JTextField m_value;
    protected JLabel m_help;
    protected boolean m_cancelled = true;
    protected String m_selProp;
    protected Hashtable m_propList;

    public GemsSetPropDialog(Frame frame, String string) {
        super(frame, string, true);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add(jPanel2);
        JLabel jLabel = new JLabel("Property:", 11);
        this.m_props = new JComboBox();
        this.m_props.addActionListener(new PropSelected());
        jLabel.setLabelFor(this.m_props);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_props);
        JLabel jLabel2 = new JLabel("Type:", 11);
        this.m_type = new JTextField("", 28);
        this.m_type.setEditable(false);
        jLabel2.setLabelFor(this.m_type);
        jPanel2.add(jLabel2);
        jPanel2.add(this.m_type);
        JLabel jLabel3 = new JLabel("Value:", 11);
        this.m_value = new JTextField("", 28);
        jLabel3.setLabelFor(this.m_value);
        jPanel2.add(jLabel3);
        jPanel2.add(this.m_value);
        JLabel jLabel4 = new JLabel("<html><br><br><br></html>");
        jPanel2.add(jLabel4);
        this.m_help = new JLabel("");
        jLabel4.setLabelFor(this.m_help);
        jPanel2.add(this.m_help);
        SpringUtilities.makeCompactGrid(jPanel2, 4, 2, 5, 5, 5, 5);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout());
        JButton jButton = new JButton("OK ");
        JButton jButton2 = new JButton("Cancel ");
        jPanel3.add(jButton);
        jPanel3.add(jButton2);
        jButton.addActionListener(new OkPressed());
        jButton2.addActionListener(new CancelPressed());
        jPanel.add(jPanel3);
        this.setContentPane(jPanel);
        this.setLocationRelativeTo(frame);
    }

    public String getValue(Hashtable hashtable, String string, String string2) {
        this.m_propList = hashtable;
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            this.m_props.addItem((String) enumeration.nextElement());
        }
        if (string != null) {
            this.m_props.setSelectedItem(string);
            GemsProperty gemsProperty = (GemsProperty) hashtable.get(string);
            if (gemsProperty != null) {
                this.m_type.setText(gemsProperty.type.getName());
                this.m_help.setText(gemsProperty.help);
            }
        }
        this.m_selProp = (String) this.m_props.getSelectedItem();
        if (string2 != null) {
            this.m_value.setText(string2);
        }
        this.pack();
        this.show();
        if (this.m_cancelled) {
            return null;
        }
        return this.m_value.getText();
    }

    public String getSelectedProp() {
        return this.m_selProp;
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsSetPropDialog.this.m_cancelled = true;
            GemsSetPropDialog.this.dispose();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsSetPropDialog.this.m_cancelled = false;
            GemsSetPropDialog.this.dispose();
        }
    }

    class PropSelected
            implements ActionListener {
        PropSelected() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsSetPropDialog.this.m_selProp = (String) GemsSetPropDialog.this.m_props.getSelectedItem();
            GemsProperty gemsProperty = (GemsProperty) GemsSetPropDialog.this.m_propList.get(GemsSetPropDialog.this.m_selProp);
            GemsSetPropDialog.this.m_value.setText("");
            if (gemsProperty != null) {
                GemsSetPropDialog.this.m_type.setText(gemsProperty.type.getName());
                GemsSetPropDialog.this.m_help.setText(gemsProperty.help);
            }
            GemsSetPropDialog.this.invalidate();
        }
    }

}

