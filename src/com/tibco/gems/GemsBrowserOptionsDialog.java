/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GemsBrowserOptionsDialog
        extends JDialog {
    protected JTextField m_delay = null;
    protected JTextField m_timeout = null;
    protected JCheckBox m_oldFirst;
    protected JCheckBox m_useServerTimestamps = null;
    protected boolean m_cancelled = false;

    public GemsBrowserOptionsDialog(Frame frame, String string, boolean bl, boolean bl2, boolean bl3) {
        super(frame, string, true);
        this.init(frame, string, bl, bl2, bl3);
    }

    public void init(Frame frame, String string, boolean bl, boolean bl2, boolean bl3) {
        JLabel jLabel;
        Container container;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add(jPanel2);
        int n = 0;
        if (bl3) {
            jLabel = new JLabel("Message read delay (millisecs):", 11);
            this.m_delay = new JTextField(Gems.getGems().getMsgReadDelayStr(), 20);
            this.m_delay.setMaximumSize(new Dimension(50, 0));
            jLabel.setLabelFor(this.m_delay);
            jPanel2.add(jLabel);
            jPanel2.add(this.m_delay);
            ++n;
        }
        jLabel = new JLabel("View oldest messages first:", 11);
        this.m_oldFirst = new JCheckBox();
        this.m_oldFirst.setSelected(Gems.getGems().getViewOldMessagesFirst());
        jLabel.setLabelFor(this.m_oldFirst);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_oldFirst);
        ++n;
        if (bl) {
            JLabel jLabel1 = new JLabel("Use Server based timestamps:", 11);
            this.m_useServerTimestamps = new JCheckBox();
            this.m_useServerTimestamps.setSelected(Gems.getGems().getUseServerTimestamps());
            jLabel1.setLabelFor(this.m_useServerTimestamps);
            jPanel2.add(jLabel1);
            jPanel2.add(this.m_useServerTimestamps);
            ++n;
        }
        if (bl2) {
            JLabel jLabel1 = new JLabel("Request/reply timeout (secs):", 11);
            this.m_timeout = new JTextField(Gems.getGems().getRequestReplyTimeoutStr(), 20);
            this.m_timeout.setMaximumSize(new Dimension(50, 0));
            jLabel1.setLabelFor(this.m_timeout);
            jPanel2.add(jLabel1);
            jPanel2.add(this.m_timeout);
            ++n;
        }
        SpringUtilities.makeCompactGrid(jPanel2, n, 2, 5, 5, 8, 8);
        container = new JPanel();
        container.setLayout(new FlowLayout());
        JButton jButton = new JButton("OK ");
        JButton jButton2 = new JButton("Cancel ");
        container.add(jButton);
        container.add(jButton2);
        jButton.addActionListener(new OkPressed());
        jButton2.addActionListener(new CancelPressed());
        jPanel.add(container);
        this.setContentPane(jPanel);
        this.setLocationRelativeTo(frame);
        this.pack();
        this.show();
    }

    public GemsBrowserOptionsDialog(Frame frame, String string, boolean bl) {
        super(frame, string, true);
        this.init(frame, string, bl, true, true);
    }

    public GemsBrowserOptionsDialog(Frame frame, String string) {
        super(frame, string, true);
        this.init(frame, string, false, false, true);
    }

    class CancelPressed
            implements ActionListener {
        CancelPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsBrowserOptionsDialog.this.m_cancelled = true;
            GemsBrowserOptionsDialog.this.dispose();
        }
    }

    class OkPressed
            implements ActionListener {
        OkPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (GemsBrowserOptionsDialog.this.m_delay != null) {
                Gems.getGems().setMsgReadDelay(GemsBrowserOptionsDialog.this.m_delay.getText());
            }
            Gems.getGems().setViewOldMessagesFirst(GemsBrowserOptionsDialog.this.m_oldFirst.isSelected());
            if (GemsBrowserOptionsDialog.this.m_useServerTimestamps != null) {
                Gems.getGems().setUseServerTimestamps(GemsBrowserOptionsDialog.this.m_useServerTimestamps.isSelected());
            }
            if (GemsBrowserOptionsDialog.this.m_timeout != null && GemsBrowserOptionsDialog.this.m_timeout.getText() != null) {
                Gems.getGems().setRequestReplyTimeout(GemsBrowserOptionsDialog.this.m_timeout.getText());
            }
            GemsBrowserOptionsDialog.this.dispose();
        }
    }

}

