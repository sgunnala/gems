/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

public class IconNodeRenderer
        extends DefaultTreeCellRenderer {
    Font font = null;
    Font bfont;
    Color col = UIManager.getColor("Tree.textForeground");
    Color scol = UIManager.getColor("Tree.selectionForeground");

    public Component getTreeCellRendererComponent(JTree jTree, Object object, boolean bl, boolean bl2, boolean bl3, int n, boolean bl4) {
        if (this.font == null) {
            this.font = this.getFont();
            if (this.font != null) {
                this.bfont = new Font(this.font.getName(), 1, this.font.getSize());
            }
        }
        if (object != null && object instanceof GemsConnectionNode) {
            if (((GemsConnectionNode) object).isStandbyMode()) {
                this.setTextNonSelectionColor(this.col);
                this.setTextSelectionColor(this.scol);
                this.setFont(this.font);
            } else if (((GemsConnectionNode) object).showError()) {
                this.setTextNonSelectionColor(Color.red);
                this.setTextSelectionColor(Color.red);
                this.setFont(this.bfont);
            } else if (((GemsConnectionNode) object).showWarning()) {
                this.setTextNonSelectionColor(Color.orange);
                this.setTextSelectionColor(Color.orange);
                this.setFont(this.bfont);
            } else {
                this.setTextNonSelectionColor(this.col);
                this.setTextSelectionColor(this.scol);
                this.setFont(this.font);
            }
        } else {
            this.setTextNonSelectionColor(this.col);
            this.setTextSelectionColor(this.scol);
            this.setFont(this.font);
        }
        super.getTreeCellRendererComponent(jTree, object, bl, bl2, bl3, n, bl4);
        if (object != null && object instanceof IconNode) {
            Icon icon = ((IconNode) object).getIcon();
            if (icon == null) {
                Hashtable hashtable = (Hashtable) jTree.getClientProperty("JTree.icons");
                String string = ((IconNode) object).getIconName();
                if (hashtable != null && string != null && (icon = (Icon) hashtable.get(string)) != null) {
                    this.setIcon(icon);
                }
            } else {
                this.setIcon(icon);
            }
        } else {
            this.setIcon(null);
        }
        return this;
    }
}

