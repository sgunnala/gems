/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.*;

public class GemsBrowserFilterDialog
        extends JDialog {
    protected JTextField m_filter;
    protected boolean m_cancelled = false;
    protected Frame m_frame;
    protected Pattern m_pattern = null;

    public GemsBrowserFilterDialog(Frame frame, String string) {
        super(frame, string, true);
        this.m_frame = frame;
    }

    public Pattern getFilter(Pattern pattern, String string) {
        Font font = new Font("Serif", 0, 14);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel(new BorderLayout(10, 10));
        jPanel.add(jPanel2);
        jPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel jLabel = new JLabel("Only show " + string + " that match this regex:");
        jLabel.setFont(font);
        this.m_filter = new JTextField(pattern != null ? pattern.pattern() : "");
        this.m_filter.setFont(font);
        jPanel2.add((Component) jLabel, "North");
        jPanel2.add((Component) this.m_filter, "Center");
        JLabel jLabel2 = new JLabel("<html>Example: &nbsp;&nbsp;.*[Ee]rror.* &nbsp;&nbsp;matches " + string + " containing Error or error</html>");
        jLabel2.setFont(font);
        jLabel2.setEnabled(false);
        jPanel2.add((Component) jLabel2, "South");
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
        this.setLocationRelativeTo(this.m_frame);
        this.pack();
        this.show();
        if (!this.m_cancelled) {
            return this.m_pattern;
        }
        return null;
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsBrowserFilterDialog.this.m_cancelled = true;
            GemsBrowserFilterDialog.this.dispose();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                GemsBrowserFilterDialog.this.m_pattern = Pattern.compile(GemsBrowserFilterDialog.this.m_filter.getText());
            } catch (Exception var2_2) {
                JOptionPane.showMessageDialog(GemsBrowserFilterDialog.this.m_frame, var2_2.getMessage(), "Error", 1);
                System.err.println("Exception: " + var2_2.getMessage());
                return;
            }
            GemsBrowserFilterDialog.this.dispose();
        }
    }

}

