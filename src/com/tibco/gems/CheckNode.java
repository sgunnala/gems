/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckNode
        extends DefaultMutableTreeNode {
    public static final int SINGLE_SELECTION = 0;
    public static final int DIG_IN_SELECTION = 4;
    protected int selectionMode;
    protected boolean isSelected;

    public CheckNode() {
        this(null);
    }

    public CheckNode(Object object) {
        this(object, true, false);
    }

    public CheckNode(Object object, boolean bl, boolean bl2) {
        super(object, bl);
        this.isSelected = bl2;
        this.setSelectionMode(4);
    }

    public int getSelectionMode() {
        return this.selectionMode;
    }

    public void setSelectionMode(int n) {
        this.selectionMode = n;
    }

    public void updateSelection() {
        if (this.selectionMode == 4) {
            Gems.debug("UpSel " + this.toString());
            if (this.children != null) {
                Enumeration enumeration = this.children.elements();
                int n = 0;
                while (enumeration.hasMoreElements()) {
                    CheckNode checkNode = (CheckNode) enumeration.nextElement();
                    if (!checkNode.isSelected()) continue;
                    ++n;
                }
                this.isSelected = n > 0;
            }

            CheckNode object;
            if ((object = (CheckNode) this.getParent()) != null) {
                object.updateSelection();
            }
        }
    }

    public int getSelectedLeafCount(int n) {
        if (this.children() != null && this.getChildCount() > 0) {
            Enumeration enumeration = this.children();
            while (enumeration.hasMoreElements()) {
                CheckNode checkNode = (CheckNode) enumeration.nextElement();
                n = checkNode.getSelectedLeafCount(n);
            }
            return n;
        }
        if (this.isSelected()) {
            ++n;
        }
        return n;
    }

    public boolean areAllChildrenSelected() {
        if (this.children != null) {
            Enumeration enumeration = this.children.elements();
            int n = 0;
            int n2 = 0;
            while (enumeration.hasMoreElements()) {
                ++n2;
                CheckNode checkNode = (CheckNode) enumeration.nextElement();
                if (!checkNode.isSelected()) continue;
                ++n;
            }
            if (n2 > 0 && n == n2) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean bl) {
        this.isSelected = bl;
        if (this.selectionMode == 4) {
            if (this.children != null) {
                Enumeration enumeration = this.children.elements();
                while (enumeration.hasMoreElements()) {
                    CheckNode checkNode = (CheckNode) enumeration.nextElement();
                    checkNode.setSelected(bl);
                }
            }
            CheckNode object;
            if ((object = (CheckNode) this.getParent()) != null) {
                object.updateSelection();
            }
        }
    }
}

