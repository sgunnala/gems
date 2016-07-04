/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.*;

public class GemsSelectorWizardDialog
        extends JDialog {
    public String m_selector = new String();
    protected JComboBox m_ba;
    protected Date m_start = null;
    protected Frame m_frame = null;
    protected boolean m_cancelled = false;
    protected JTextField m_st;

    public GemsSelectorWizardDialog(Frame frame, String string, String string2) {
        super(frame, string, true);
        this.m_frame = frame;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add(jPanel2);
        JLabel jLabel = new JLabel(string2 + " messages with timestamps ", 11);
        jLabel.setMaximumSize(new Dimension(0, 24));
        this.m_ba = new JComboBox();
        this.m_ba.addItem(" After ");
        this.m_ba.addItem(" Before ");
        jLabel.setLabelFor(this.m_ba);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_ba);
        this.m_start = new Date(System.currentTimeMillis() - 600000);
        this.m_st = new JTextField(this.m_start.toString(), 20);
        this.m_st.setEditable(false);
        JButton jButton = new JButton("Select...");
        jButton.addActionListener(new DatePickerAction());
        jPanel2.add(this.m_st);
        jPanel2.add(jButton);
        SpringUtilities.makeCompactGrid(jPanel2, 2, 2, 5, 5, 8, 8);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout());
        JButton jButton2 = new JButton("OK ");
        JButton jButton3 = new JButton("Cancel ");
        jPanel3.add(jButton2);
        jPanel3.add(jButton3);
        jButton2.addActionListener(new OkPressed());
        jButton3.addActionListener(new CancelPressed());
        jPanel.add(jPanel3);
        this.setContentPane(jPanel);
        this.setLocationRelativeTo(frame);
        this.pack();
        this.show();
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsSelectorWizardDialog.this.m_cancelled = true;
            GemsSelectorWizardDialog.this.dispose();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsSelectorWizardDialog.this.m_selector = "JMSTimestamp ";
            String string = (String) GemsSelectorWizardDialog.this.m_ba.getSelectedItem();
            GemsSelectorWizardDialog.this.m_selector = string.equals(" Before ") ? GemsSelectorWizardDialog.this.m_selector + "< " : GemsSelectorWizardDialog.this.m_selector + "> ";
            GemsSelectorWizardDialog.this.m_selector = GemsSelectorWizardDialog.this.m_selector + GemsSelectorWizardDialog.this.m_start.getTime();
            GemsSelectorWizardDialog.this.dispose();
        }
    }

    class DatePickerAction
            implements ActionListener {
        DatePickerAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            DateTimePicker dateTimePicker = new DateTimePicker(GemsSelectorWizardDialog.this.m_frame, "Select Date and Time:");
            dateTimePicker.setLocationRelativeTo(GemsSelectorWizardDialog.this.m_frame);
            Date date = dateTimePicker.select(GemsSelectorWizardDialog.this.m_start);
            if (date != null) {
                GemsSelectorWizardDialog.this.m_start = date;
                GemsSelectorWizardDialog.this.m_st.setText(GemsSelectorWizardDialog.this.m_start.toString());
            }
            dateTimePicker.dispose();
        }
    }

}

