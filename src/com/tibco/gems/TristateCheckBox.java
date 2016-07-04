/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

public class TristateCheckBox
        extends JCheckBox {
    public static final State NOT_SELECTED = new State();
    public static final State SELECTED = new State();
    public static final State DONT_CARE = new State();
    private final TristateDecorator model;

    public TristateCheckBox() {
        this(null);
    }

    public TristateCheckBox(String string) {
        this(string, DONT_CARE);
    }

    public TristateCheckBox(String string, State state) {
        this(string, null, state);
    }

    public TristateCheckBox(String string, Icon icon, State state) {
        super(string, icon);
        super.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent mouseEvent) {
                TristateCheckBox.this.grabFocus();
                TristateCheckBox.this.model.nextState();
            }
        });
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        actionMapUIResource.put("pressed", new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                TristateCheckBox.this.grabFocus();
                TristateCheckBox.this.model.nextState();
            }
        });
        actionMapUIResource.put("released", null);
        SwingUtilities.replaceUIActionMap(this, actionMapUIResource);
        this.model = new TristateDecorator(this.getModel());
        this.setModel(this.model);
        this.setState(state);
    }

    public void addMouseListener(MouseListener mouseListener) {
    }

    public State getState() {
        return this.model.getState();
    }

    public void setState(State state) {
        this.model.setState(state);
    }

    public void setSelected(boolean bl) {
        if (bl) {
            this.setState(SELECTED);
        } else {
            this.setState(NOT_SELECTED);
        }
    }

    public static class State {
        private State() {
        }
    }

    private class TristateDecorator
            implements ButtonModel {
        private final ButtonModel other;

        private TristateDecorator(ButtonModel buttonModel) {
            this.other = buttonModel;
        }

        public Object[] getSelectedObjects() {
            return this.other.getSelectedObjects();
        }

        private void nextState() {
            State state = this.getState();
            if (state == TristateCheckBox.NOT_SELECTED) {
                this.setState(TristateCheckBox.SELECTED);
            } else if (state == TristateCheckBox.SELECTED) {
                this.setState(TristateCheckBox.DONT_CARE);
            } else if (state == TristateCheckBox.DONT_CARE) {
                this.setState(TristateCheckBox.NOT_SELECTED);
            }
        }

        private State getState() {
            if (this.isSelected() && !this.isArmed()) {
                return TristateCheckBox.SELECTED;
            }
            if (this.isSelected() && this.isArmed()) {
                return TristateCheckBox.DONT_CARE;
            }
            return TristateCheckBox.NOT_SELECTED;
        }

        private void setState(State state) {
            if (state == TristateCheckBox.NOT_SELECTED) {
                this.other.setArmed(false);
                this.setPressed(false);
                this.setSelected(false);
            } else if (state == TristateCheckBox.SELECTED) {
                this.other.setArmed(false);
                this.setPressed(false);
                this.setSelected(true);
            } else {
                this.other.setArmed(true);
                this.setPressed(true);
                this.setSelected(true);
            }
        }

        public boolean isArmed() {
            return this.other.isArmed();
        }

        public void setArmed(boolean bl) {
        }

        public boolean isSelected() {
            return this.other.isSelected();
        }

        public boolean isEnabled() {
            return this.other.isEnabled();
        }

        public void setEnabled(boolean bl) {
            TristateCheckBox.this.setFocusable(bl);
            this.other.setEnabled(bl);
        }

        public boolean isPressed() {
            return this.other.isPressed();
        }

        public boolean isRollover() {
            return this.other.isRollover();
        }

        public void setRollover(boolean bl) {
            this.other.setRollover(bl);
        }

        public void setPressed(boolean bl) {
            this.other.setPressed(bl);
        }

        public void setSelected(boolean bl) {
            this.other.setSelected(bl);
        }        public void setMnemonic(int n) {
            this.other.setMnemonic(n);
        }

        public int getMnemonic() {
            return this.other.getMnemonic();
        }

        public void setActionCommand(String string) {
            this.other.setActionCommand(string);
        }

        public String getActionCommand() {
            return this.other.getActionCommand();
        }

        public void setGroup(ButtonGroup buttonGroup) {
            this.other.setGroup(buttonGroup);
        }

        public void addActionListener(ActionListener actionListener) {
            this.other.addActionListener(actionListener);
        }

        public void removeActionListener(ActionListener actionListener) {
            this.other.removeActionListener(actionListener);
        }

        public void addItemListener(ItemListener itemListener) {
            this.other.addItemListener(itemListener);
        }

        public void removeItemListener(ItemListener itemListener) {
            this.other.removeItemListener(itemListener);
        }

        public void addChangeListener(ChangeListener changeListener) {
            this.other.addChangeListener(changeListener);
        }

        public void removeChangeListener(ChangeListener changeListener) {
            this.other.removeChangeListener(changeListener);
        }


    }

}

