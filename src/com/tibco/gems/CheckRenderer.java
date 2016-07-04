/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

public class CheckRenderer
        extends JPanel
        implements TreeCellRenderer {
    protected TristateCheckBox check;
    protected TreeLabel label;

    public CheckRenderer() {
        this.setLayout(null);
        this.check = new TristateCheckBox();
        this.add(this.check);
        this.label = new TreeLabel();
        this.add(this.label);
        this.check.setBackground(UIManager.getColor("Tree.textBackground"));
        this.label.setForeground(UIManager.getColor("Tree.textForeground"));
    }

    public Component getTreeCellRendererComponent(JTree jTree, Object object, boolean bl, boolean bl2, boolean bl3, int n, boolean bl4) {
        String string = jTree.convertValueToText(object, bl, bl2, bl3, n, bl4);
        this.setEnabled(jTree.isEnabled());
        this.check.setSelected(((CheckNode) object).isSelected());
        CheckNode checkNode = (CheckNode) object;
        if (checkNode.getSelectionMode() == 4 && checkNode.getChildCount() > 0) {
            int n2 = checkNode.getLeafCount();
            int n3 = checkNode.getSelectedLeafCount(0);
            if (n3 > 0 && n3 < n2) {
                this.check.setState(TristateCheckBox.DONT_CARE);
            }
        }
        this.label.setFont(jTree.getFont());
        this.label.setText(string);
        this.label.setSelected(bl);
        if (bl) {
            this.label.setForeground(UIManager.getColor("Tree.selectionForeground"));
        } else {
            this.label.setForeground(UIManager.getColor("Tree.textForeground"));
        }
        this.label.setFocus(bl4);
        return this;
    }

    public void doLayout() {
        Dimension dimension = this.check.getPreferredSize();
        Dimension dimension2 = this.label.getPreferredSize();
        int n = 0;
        int n2 = 0;
        if (dimension.height < dimension2.height) {
            n = (dimension2.height - dimension.height) / 2;
        } else {
            n2 = (dimension.height - dimension2.height) / 2;
        }
        this.check.setBounds(0, n, dimension.width, dimension.height);
        this.label.setBounds(dimension.width, n2, dimension2.width, dimension2.height);
    }    public Dimension getPreferredSize() {
        Dimension dimension = this.check.getPreferredSize();
        Dimension dimension2 = this.label.getPreferredSize();
        return new Dimension(dimension.width + dimension2.width, dimension.height < dimension2.height ? dimension2.height : dimension.height);
    }

    public class TreeLabel
            extends JLabel {
        boolean isSelected;
        boolean hasFocus;

        public void paint(Graphics graphics) {
            String string = this.getText();
            if (string != null && 0 < string.length()) {
                if (this.isSelected) {
                    graphics.setColor(UIManager.getColor("Tree.selectionBackground"));
                } else {
                    graphics.setColor(UIManager.getColor("Tree.textBackground"));
                }
                Dimension dimension = this.getPreferredSize();
                int n = 0;
                Icon icon = this.getIcon();
                if (icon != null) {
                    n = icon.getIconWidth() + Math.max(0, this.getIconTextGap() - 1);
                }
                graphics.fillRect(n, 0, dimension.width - 1 - n, dimension.height);
                if (this.hasFocus) {
                    graphics.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                    graphics.drawRect(n, 0, dimension.width - 1 - n, dimension.height - 1);
                }
            }
            super.paint(graphics);
        }        public void setBackground(Color color) {
            if (color instanceof ColorUIResource) {
                color = null;
            }
            super.setBackground(color);
        }

        public void setSelected(boolean bl) {
            this.isSelected = bl;
        }

        public void setFocus(boolean bl) {
            this.hasFocus = bl;
        }        public Dimension getPreferredSize() {
            Dimension dimension = super.getPreferredSize();
            if (dimension != null) {
                dimension = new Dimension(dimension.width + 3, dimension.height);
            }
            return dimension;
        }




    }

    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) {
            color = null;
        }
        super.setBackground(color);
    }



}

