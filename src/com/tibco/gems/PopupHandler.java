/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class PopupHandler {
    public JPopupMenu createPopup(Point point) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        return jPopupMenu;
    }

    public class SelectAllAction
            extends TextAction {
        public SelectAllAction(String string, Icon icon) {
            super(string, icon);
            this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("ctrl A"));
        }

        public void actionPerformed(ActionEvent actionEvent) {
            this.txtcomp.selectAll();
        }

        public boolean isEnabled() {
            return this.txtcomp != null && this.txtcomp.isEnabled() && this.txtcomp.getText().length() > 0 && (this.txtcomp.getSelectedText() == null || this.txtcomp.getSelectedText().length() < this.txtcomp.getText().length());
        }
    }

    public class PasteAction
            extends TextAction {
        public PasteAction(String string, Icon icon) {
            super(string, icon);
            this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("ctrl V"));
        }

        public void actionPerformed(ActionEvent actionEvent) {
            this.txtcomp.paste();
        }

        public boolean isEnabled() {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            return this.txtcomp != null && this.txtcomp.isEnabled() && this.txtcomp.isEditable() && transferable.isDataFlavorSupported(DataFlavor.stringFlavor);
        }
    }

    public class CopyAction
            extends TextAction {
        public CopyAction(String string, Icon icon) {
            super(string, icon);
            this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("ctrl C"));
        }

        public void actionPerformed(ActionEvent actionEvent) {
            this.txtcomp.copy();
        }

        public boolean isEnabled() {
            return this.txtcomp != null && this.txtcomp.getSelectedText() != null;
        }
    }

    public class CutAction
            extends TextAction {
        public CutAction(String string, Icon icon) {
            super(string, icon);
            this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("ctrl X"));
        }

        public void actionPerformed(ActionEvent actionEvent) {
            this.txtcomp.cut();
        }

        public boolean isEnabled() {
            return this.txtcomp != null && this.txtcomp.isEditable() && this.txtcomp.getSelectedText() != null;
        }
    }

    public abstract class TextAction
            extends AbstractAction {
        JTextComponent txtcomp;

        public TextAction(String string, Icon icon) {
            super(string, icon);
            this.putValue("ShortDescription", string);
        }

        public void setTextComponent(JTextComponent jTextComponent) {
            this.txtcomp = jTextComponent;
        }

        public abstract void actionPerformed(ActionEvent var1);
    }

}

