/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

public class GemsBrowser extends JFrame implements HyperlinkListener, ActionListener {
    private JTextField urlField;
    private JEditorPane htmlPane;
    private String initialURL;

    public GemsBrowser(String string) {
        super("Gems Help");
        this.initialURL = string;
        this.setDefaultCloseOperation(2);
        this.setIconImage(Gems.getGems().m_icon.getImage());
        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel("URL:");
        this.urlField = new JTextField(60);
        this.urlField.setText(string);
        this.urlField.addActionListener(this);
        jPanel.add(jLabel);
        jPanel.add(this.urlField);
        this.getContentPane().add((Component) jPanel, "North");
        try {
            this.htmlPane = new JEditorPane(string);
            this.htmlPane.setEditable(false);
            this.htmlPane.addHyperlinkListener(this);
            JScrollPane dimension = new JScrollPane(this.htmlPane);
            this.getContentPane().add((Component) dimension, "Center");
        } catch (IOException var4_5) {
            System.err.println("JMSException: " + var4_5.getMessage());
        }
        Dimension dimension = this.getToolkit().getScreenSize();
        int n = dimension.width * 7 / 10;
        int n2 = dimension.height * 8 / 10 + 20;
        this.setBounds(n / 7, n2 / 7, n, n2);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String string = "";
        string = this.urlField.getText();
        try {
            this.htmlPane.setPage(new URL(string));
        } catch (IOException var3_3) {
            System.err.println("JMSException: " + var3_3.getMessage());
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        if (hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                if (hyperlinkEvent instanceof HTMLFrameHyperlinkEvent) {
                    HTMLFrameHyperlinkEvent hTMLFrameHyperlinkEvent = (HTMLFrameHyperlinkEvent) hyperlinkEvent;
                    HTMLDocument hTMLDocument = (HTMLDocument) this.htmlPane.getDocument();
                    hTMLDocument.processHTMLFrameHyperlinkEvent(hTMLFrameHyperlinkEvent);
                } else {
                    this.htmlPane.setPage(hyperlinkEvent.getURL());
                    this.urlField.setText(hyperlinkEvent.getURL().toExternalForm());
                }
            } catch (IOException var2_3) {
                System.err.println("JMSException: " + var2_3.getMessage());
            }
        }
    }
}

