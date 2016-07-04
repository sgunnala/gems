/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class IconNode
        extends DefaultMutableTreeNode {
    protected Icon icon = null;
    protected String iconName;

    public IconNode() {
        this(null);
    }

    public IconNode(Object object) {
        this(object, true, null);
    }

    public IconNode(Object object, boolean bl, Icon icon) {
        super(object, bl);
        this.icon = icon;
    }

    public IconNode(Object object, boolean bl) {
        super(object, bl);
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getIconName() {
        if (this.iconName != null) {
            return this.iconName;
        }
        String string = this.userObject.toString();
        int n = string.lastIndexOf(".");
        if (n != -1) {
            return string.substring(++n);
        }
        return null;
    }

    public void setIconName(String string) {
        this.iconName = string;
    }
}

