/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class PopupMsgPropTableHandler
        extends PopupTableHandler {
    public AbstractAction openMsg = null;
    public AbstractAction remProp = null;
    public AbstractAction compProp = null;
    public AbstractAction presvProp = null;
    public AbstractAction viewBytes = null;
    public AbstractAction viewBytesText = null;
    public AbstractAction replytoProp = null;
    public AbstractAction addint = null;
    public AbstractAction addbool = null;
    public AbstractAction addstring = null;
    public AbstractAction addbyte = null;
    public AbstractAction addshort = null;
    public AbstractAction addlong = null;
    public AbstractAction adddouble = null;
    public AbstractAction addfloat = null;
    public JMenu addprop = null;
    public JMenu addfield = null;
    public GemsMsgPropTableModel m_model;

    public PopupMsgPropTableHandler(JTable jTable) {
        super(jTable);
        this.m_model = (GemsMsgPropTableModel) jTable.getModel();
    }

    public JPopupMenu createPopup(Point point) {
        JPopupMenu jPopupMenu = super.createPopup(point);
        if (this.addint == null) {
            this.addint = new AddIntPropertyAction("Integer", null);
        }
        if (this.addlong == null) {
            this.addlong = new AddLongPropertyAction("Long", null);
        }
        if (this.addshort == null) {
            this.addshort = new AddShortPropertyAction("Short", null);
        }
        if (this.addbyte == null) {
            this.addbyte = new AddBytePropertyAction("Byte", null);
        }
        if (this.addfloat == null) {
            this.addfloat = new AddFloatPropertyAction("Float", null);
        }
        if (this.adddouble == null) {
            this.adddouble = new AddDoublePropertyAction("Double", null);
        }
        if (this.addbool == null) {
            this.addbool = new AddBooleanPropertyAction("Boolean", null);
        }
        if (this.addstring == null) {
            this.addstring = new AddStringPropertyAction("String", null);
        }
        if (this.m_model.isMapMsg()) {
            if (this.openMsg == null) {
                this.openMsg = new OpenMsgAction("Open Sub Message...", null);
            }
            if (this.viewBytes == null) {
                this.viewBytes = new ViewBytesAction("View Bytes Field", null);
            }
            if (this.viewBytesText == null) {
                this.viewBytesText = new ViewBytesTextAction("View Bytes Field As Text", null);
            }
            this.addfield = new JMenu("Add Field");
            if (this.remProp == null) {
                this.remProp = new RemovePropertyAction("Remove Field", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.addfield);
            this.addfield.add(this.addstring);
            this.addfield.add(this.addbool);
            this.addfield.add(this.addint);
            this.addfield.add(this.addbyte);
            this.addfield.add(this.addshort);
            this.addfield.add(this.addlong);
            this.addfield.add(this.addfloat);
            this.addfield.add(this.adddouble);
            jPopupMenu.add(this.remProp);
            jPopupMenu.addSeparator();
            jPopupMenu.add(this.openMsg);
            jPopupMenu.add(this.viewBytes);
            jPopupMenu.add(this.viewBytesText);
        } else {
            this.addprop = new JMenu("Add Custom Property");
            JMenu jMenu = new JMenu("Add Tibco Property");
            JMenu jMenu2 = new JMenu("Add JMS Property");
            if (this.remProp == null) {
                this.remProp = new RemovePropertyAction("Remove Property", null);
            }
            if (this.compProp == null) {
                this.compProp = new AddCompressPropertyAction("JMS_TIBCO_COMPRESS", null);
            }
            if (this.presvProp == null) {
                this.presvProp = new AddPreservePropertyAction("JMS_TIBCO_PRESERVE_UNDELIVERED", null);
            }
            if (this.replytoProp == null) {
                this.replytoProp = new AddJMSReplyToPropertyAction("JMSReplyTo...", null);
            }
            jPopupMenu.addSeparator();
            jPopupMenu.add(jMenu2);
            jMenu2.add(this.replytoProp);
            jPopupMenu.add(jMenu);
            jMenu.add(this.compProp);
            jMenu.add(this.presvProp);
            jPopupMenu.add(this.addprop);
            this.addprop.add(this.addstring);
            this.addprop.add(this.addbool);
            this.addprop.add(this.addint);
            this.addprop.add(this.addbyte);
            this.addprop.add(this.addshort);
            this.addprop.add(this.addlong);
            this.addprop.add(this.addfloat);
            this.addprop.add(this.adddouble);
            jPopupMenu.add(this.remProp);
        }
        return jPopupMenu;
    }

    public class RemovePropertyAction
            extends AbstractAction {
        public RemovePropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.removeRow(PopupMsgPropTableHandler.this.m_row);
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isCellEditable(PopupMsgPropTableHandler.this.m_row, 0);
        }
    }

    public class AddJMSReplyToPropertyAction
            extends AbstractAction {
        public AddJMSReplyToPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            for (Container container = PopupMsgPropTableHandler.this.m_model.m_table.getParent(); container != null; container = container.getParent()) {
                if (!(container instanceof GemsMessageFrame)) continue;
                GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker((JFrame) container, ((GemsMessageFrame) container).m_cn);
                if (gemsDestinationPicker.m_retDest != null) {
                    PopupMsgPropTableHandler.this.m_model.addJMSReplyToProperty(gemsDestinationPicker.m_retDest);
                }
                return;
            }
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddPreservePropertyAction
            extends AbstractAction {
        public AddPreservePropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addPreserveProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddCompressPropertyAction
            extends AbstractAction {
        public AddCompressPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addCompressProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddPropertyAction
            extends AbstractAction {
        public AddPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addIntegerProperty("intf", 123);
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddStringPropertyAction
            extends AbstractAction {
        public AddStringPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addStringProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddBooleanPropertyAction
            extends AbstractAction {
        public AddBooleanPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addBooleanProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddBytePropertyAction
            extends AbstractAction {
        public AddBytePropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addByteProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddDoublePropertyAction
            extends AbstractAction {
        public AddDoublePropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addDoubleProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddFloatPropertyAction
            extends AbstractAction {
        public AddFloatPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addFloatProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddLongPropertyAction
            extends AbstractAction {
        public AddLongPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addLongProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddShortPropertyAction
            extends AbstractAction {
        public AddShortPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addShortProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class AddIntPropertyAction
            extends AbstractAction {
        public AddIntPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            PopupMsgPropTableHandler.this.m_model.addIntProperty();
        }

        public boolean isEnabled() {
            return PopupMsgPropTableHandler.this.m_model.isEditable();
        }
    }

    public class OpenMsgAction
            extends AbstractAction {
        public OpenMsgAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame gemsMessageFrame = (GemsMessageFrame) SwingUtilities.getWindowAncestor(PopupMsgPropTableHandler.this.m_table);
            gemsMessageFrame.openSubMapMsg((String) PopupMsgPropTableHandler.this.m_model.getValueAt(PopupMsgPropTableHandler.this.m_row, 0));
        }

        public boolean isEnabled() {
            String string = PopupMsgPropTableHandler.this.m_model.getValueAt(PopupMsgPropTableHandler.this.m_row, 1).toString();
            String string2 = (String) PopupMsgPropTableHandler.this.m_model.getValueAt(PopupMsgPropTableHandler.this.m_row, 0);
            return string.startsWith("MapMsg:{") || string2.equals("message_bytes");
        }
    }

    public class ViewBytesAction
            extends AbstractAction {
        public ViewBytesAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame gemsMessageFrame = (GemsMessageFrame) SwingUtilities.getWindowAncestor(PopupMsgPropTableHandler.this.m_table);
            gemsMessageFrame.createBytesFieldUI((String) PopupMsgPropTableHandler.this.m_model.getValueAt(PopupMsgPropTableHandler.this.m_row, 0), false);
        }

        public boolean isEnabled() {
            String string = PopupMsgPropTableHandler.this.m_model.getValueAt(PopupMsgPropTableHandler.this.m_row, 1).toString();
            return string.startsWith("byte[]");
        }
    }

    public class ViewBytesTextAction
            extends AbstractAction {
        public ViewBytesTextAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsMessageFrame gemsMessageFrame = (GemsMessageFrame) SwingUtilities.getWindowAncestor(PopupMsgPropTableHandler.this.m_table);
            gemsMessageFrame.createBytesFieldUI((String) PopupMsgPropTableHandler.this.m_model.getValueAt(PopupMsgPropTableHandler.this.m_row, 0), true);
        }

        public boolean isEnabled() {
            String string = PopupMsgPropTableHandler.this.m_model.getValueAt(PopupMsgPropTableHandler.this.m_row, 1).toString();
            return string.startsWith("byte[]");
        }
    }

}

