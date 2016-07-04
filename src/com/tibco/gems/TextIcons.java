/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;

class TextIcons
        extends MetalIconFactory.TreeLeafIcon {
    private static Hashtable labels;
    protected String label;
    private Color foreground = null;

    protected TextIcons() {
    }

    public static Icon getIcon(String string) {
        if (labels == null) {
            labels = new Hashtable();
            TextIcons.setDefaultSet();
        }
        TextIcons textIcons = new TextIcons();
        textIcons.label = (String) labels.get(string);
        return textIcons;
    }

    private static void setDefaultSet() {
        labels.put("c", "C");
        labels.put("java", "J");
        labels.put("html", "H");
        labels.put("htm", "H");
        labels.put("queue", "Q");
        labels.put("topic", "T");
        labels.put("routedqueue", "q");
    }

    public static void setLabelSet(String string, String string2) {
        if (labels == null) {
            labels = new Hashtable();
            TextIcons.setDefaultSet();
        }
        labels.put(string, string2);
    }

    public void paintIcon(Component component, Graphics graphics, int n, int n2) {
        super.paintIcon(component, graphics, n, n2);
        if (this.label != null) {
            graphics.setFont(component.getFont());
            if (this.foreground == null) {
                this.foreground = component.getForeground();
            }
            graphics.setColor(this.foreground);
            FontMetrics fontMetrics = graphics.getFontMetrics();
            int n3 = (this.getIconWidth() - fontMetrics.stringWidth(this.label)) / 2;
            int n4 = (this.getIconHeight() - fontMetrics.getHeight()) / 2 - 2;
            graphics.drawString(this.label, n + n3, n2 + n4 + fontMetrics.getHeight());
        }
    }
}

