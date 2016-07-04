/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  javax.jms.Destination
 *  javax.jms.JMSException
 *  javax.jms.MapMessage
 *  javax.jms.Message
 *  javax.jms.StreamMessage
 */
package com.tibco.gems;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventObject;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.StreamMessage;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class GemsMsgPropTableModel
        extends DefaultTableModel
        implements GetPopupHandler {
    JTable m_table;
    boolean m_isEditable;
    boolean m_isMapMsg = false;
    boolean m_hasSubMapMsg = false;
    int s_headerRows = 6;
    PopupHandler m_popup = null;
    SimpleDateFormat dateFormatMillis = new SimpleDateFormat("EEE MMM dd HH:mm:ss SSS zzz yyyy");

    public GemsMsgPropTableModel(boolean bl) {
        this.m_isEditable = bl;
        if (!Gems.getGems().getShowExtendedProperties()) {
            this.s_headerRows -= 3;
        }
    }

    public GemsMsgPropTableModel(boolean bl, boolean bl2) {
        this.m_isEditable = bl;
        this.s_headerRows = 0;
        this.m_isMapMsg = bl2;
    }

    static int getPropType(Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof String) {
            return 9;
        }
        if (object instanceof Integer) {
            return 5;
        }
        if (object instanceof Long) {
            return 6;
        }
        if (object instanceof Boolean) {
            return 1;
        }
        if (object instanceof Short) {
            return 4;
        }
        if (object instanceof Byte) {
            return 2;
        }
        if (object instanceof Double) {
            return 8;
        }
        return !(object instanceof Float) ? 255 : 7;
    }

    public PopupHandler getPopupHandler() {
        if (this.m_popup == null) {
            this.m_popup = new PopupMsgPropTableHandler(this.m_table);
        }
        return this.m_popup;
    }

    public int getHeaderRows() {
        return this.s_headerRows;
    }

    public boolean isCellEditable(int n, int n2) {
        if (this.m_isEditable) {
            if (n < this.s_headerRows && n2 == 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean hasSubMapMsg() {
        return this.m_hasSubMapMsg;
    }

    public boolean isMapMsg() {
        return this.m_isMapMsg;
    }

    public boolean isEditable() {
        return this.m_isEditable;
    }

    public void populateMapMsg(MapMessage mapMessage) {
        this.m_isMapMsg = true;
        this.setRowCount(0);
        this.setColumnCount(0);
        if (mapMessage == null) {
            return;
        }
        this.addColumn("Field");
        this.addColumn("Value");
        try {
            Enumeration enumeration = mapMessage.getMapNames();
            while (enumeration.hasMoreElements()) {
                String string = (String) enumeration.nextElement();
                Object[] arrobject = new String[]{string, GemsMsgPropTableModel.toTypedString(this, mapMessage.getObject(string))};
                this.addRow(arrobject);
            }
        } catch (JMSException var2_4) {
            System.err.println("JMSException: " + var2_4.getMessage());
            return;
        }
    }

    static String toTypedString(GemsMsgPropTableModel gemsMsgPropTableModel, Object object) {
        if (object == null) {
            return null;
        }
        String string = "";
        String string2 = "";
        int n = GemsMsgPropTableModel.getFieldType(object);
        if (n != 255) {
            string2 = GemsMsgPropTableModel.getTypeName(object);
        } else {
            n = GemsMsgPropTableModel.getExtendedType(object);
        }
        switch (n) {
            case 10: {
                byte[] arrby = (byte[]) object;
                String string3 = String.valueOf(arrby.length) + " bytes";
                return string2 + ":" + string3;
            }
            case 11: {
                if (gemsMsgPropTableModel != null) {
                    gemsMsgPropTableModel.m_hasSubMapMsg = true;
                }
                return "MapMsg:{" + ((MapMessage) object).toString() + " }";
            }
            case 18: {
                return "StreamMsg:{" + ((StreamMessage) object).toString() + " }";
            }
            case 12: {
                return "short-array:[" + ((short[]) object).length + " elements]";
            }
            case 13: {
                return "int-array:[" + ((int[]) object).length + " elements]";
            }
            case 14: {
                return "long-array:[" + ((long[]) object).length + " elements]";
            }
            case 15: {
                return "float-array:[" + ((float[]) object).length + " elements]";
            }
            case 16: {
                return "double-array:[" + ((double[]) object).length + " elements]";
            }
            case 255: {
                return "<unknown type>";
            }
        }
        return string2 + ":" + object.toString();
    }

    static int getFieldType(Object object) {
        if (object == null) {
            return 0;
        }
        if (object instanceof String) {
            return 9;
        }
        if (object instanceof Integer) {
            return 5;
        }
        if (object instanceof Long) {
            return 6;
        }
        if (object instanceof Boolean) {
            return 1;
        }
        if (object instanceof Short) {
            return 4;
        }
        if (object instanceof Double) {
            return 8;
        }
        if (object instanceof byte[]) {
            return 10;
        }
        if (object instanceof Byte) {
            return 2;
        }
        if (object instanceof Float) {
            return 7;
        }
        return !(object instanceof Character) ? 255 : 3;
    }

    static String getTypeName(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof byte[]) {
            return "byte[]";
        }
        String string = object.getClass().getName();
        if (string.length() < 1) {
            return string;
        }
        int n = string.lastIndexOf(46, string.length() - 1);
        if (n >= 0 && n < string.length() - 1) {
            string = string.substring(n + 1, string.length());
        }
        return string;
    }

    static int getExtendedType(Object object) {
        if (object == null) {
            return 255;
        }
        if (object instanceof MapMessage) {
            return 11;
        }
        if (object instanceof StreamMessage) {
            return 18;
        }
        if (object instanceof short[]) {
            return 12;
        }
        if (object instanceof int[]) {
            return 13;
        }
        if (object instanceof long[]) {
            return 14;
        }
        if (object instanceof float[]) {
            return 15;
        }
        return !(object instanceof double[]) ? 255 : 16;
    }

    public void populatePropertyInfo(Message message) {
        this.setRowCount(0);
        this.setColumnCount(0);
        if (message == null) {
            return;
        }
        this.addColumn("Property");
        this.addColumn("Value");
        try {
            String string;
            String string2;
            Object[] arrobject;
            Date date = new Date();
            String string3 = message.getJMSMessageID();
            if (string3 != null && string3.length() > 0) {
                arrobject = new String[]{"JMSMessageID", string3};
                this.addRow(arrobject);
            }
            date.setTime(message.getJMSTimestamp());
            Gems.debug("JMSTimestamp: " + message.getJMSTimestamp());
            arrobject = new String[]{"JMSTimestamp", this.dateFormatMillis.format(date).toString()};
            this.addRow(arrobject);
            String string4 = message.getJMSDeliveryMode() == 2 ? "PERSISTENT" : (message.getJMSDeliveryMode() == 1 ? "NON_PERSISTENT" : "RELIABLE");
            arrobject = new String[]{"JMSDeliveryMode", string4};
            this.addRow(arrobject);
            Destination destination = message.getJMSDestination();
            if (destination != null) {
                arrobject = new String[]{"JMSDestination", destination.toString()};
                this.addRow(arrobject);
            }
            if ((string2 = message.getJMSCorrelationID()) != null && string2.length() > 0) {
                arrobject = new String[]{"JMSCorrelationID", string2};
                this.addRow(arrobject);
            }
            if ((string = message.getJMSType()) != null && string.length() > 0) {
                arrobject = new String[]{"JMSType", string};
                this.addRow(arrobject);
            }
            if (Gems.getGems().getShowExtendedProperties()) {
                if (message.getJMSExpiration() != 0) {
                    date.setTime(message.getJMSExpiration());
                    arrobject = new String[]{"JMSExpiration", this.dateFormatMillis.format(date).toString()};
                    this.addRow(arrobject);
                }
                arrobject = new String[]{"JMSPriority", String.valueOf(message.getJMSPriority())};
                this.addRow(arrobject);
                if (message.getJMSReplyTo() != null) {
                    arrobject = new String[]{"JMSReplyTo", message.getJMSReplyTo().toString()};
                    this.addRow(arrobject);
                }
            }
            Enumeration enumeration = message.getPropertyNames();
            while (enumeration.hasMoreElements()) {
                String string5 = (String) enumeration.nextElement();
                if (string5.equals("msg_timestamp")) {
                    date.setTime(message.getLongProperty("msg_timestamp"));
                    arrobject = new String[]{"msg_timestamp", this.dateFormatMillis.format(date).toString()};
                    Gems.debug("msg_timestamp: " + message.getLongProperty("msg_timestamp"));
                } else {
                    arrobject = new Object[]{string5, message.getObjectProperty(string5)};
                }
                this.addRow(arrobject);
            }
        } catch (JMSException var2_5) {
            System.err.println("JMSException: " + var2_5.getMessage());
            return;
        }
    }

    public void populateEmptyPropertyInfo(int n) {
        this.setRowCount(0);
        this.setColumnCount(0);
        if (this.m_isMapMsg) {
            this.addColumn("Field");
        } else {
            this.addColumn("Property");
        }
        this.addColumn("Value");
        TableColumn tableColumn = this.m_table.getColumn("Value");
        tableColumn.setCellEditor(new MultiEditor());
        tableColumn.setCellRenderer(new MultiRenderer());
        for (int i = 0; i < n; ++i) {
            Object[] arrobject = new String[]{new String(), new String()};
            this.addRow(arrobject);
        }
    }

    public void emptyEditorPropertyInfo(String string) {
        this.setRowCount(0);
        this.setColumnCount(0);
        this.addColumn("Property");
        this.addColumn("Value");
        TableColumn tableColumn = this.m_table.getColumn("Value");
        tableColumn.setCellEditor(new MultiEditor());
        tableColumn.setCellRenderer(new MultiRenderer());
        Object[] arrobject = new Object[]{"JMSDeliveryMode", DELMODE.NON_PERSISTENT};
        this.addRow(arrobject);
        arrobject = new String[]{"JMSType", ""};
        this.addRow(arrobject);
        if (Gems.getGems().getShowExtendedProperties()) {
            arrobject = new String[]{"JMSExpiration", "0"};
            this.addRow(arrobject);
            arrobject = new String[]{"JMSPriority", "4"};
            this.addRow(arrobject);
        }
    }

    public void emptyPropertyInfo(String string) {
        this.setRowCount(0);
        this.setColumnCount(0);
        this.addColumn("Property");
        this.addColumn("Value");
        TableColumn tableColumn = this.m_table.getColumn("Value");
        tableColumn.setCellEditor(new MultiEditor());
        tableColumn.setCellRenderer(new MultiRenderer());
        Object[] arrobject = new Object[]{"JMSDeliveryMode", DELMODE.NON_PERSISTENT};
        this.addRow(arrobject);
        arrobject = new String[]{"JMSDestination", string};
        this.addRow(arrobject);
        arrobject = new String[]{"JMSCorrelationID", ""};
        this.addRow(arrobject);
        arrobject = new String[]{"JMSType", ""};
        this.addRow(arrobject);
        if (Gems.getGems().getShowExtendedProperties()) {
            arrobject = new String[]{"JMSExpiration", "0"};
            this.addRow(arrobject);
            arrobject = new String[]{"JMSPriority", "4"};
            this.addRow(arrobject);
        }
    }

    public void addPropertyInfo(String string) {
        int n = string.indexOf(61);
        if (n > 0) {
            String string2 = string.substring(n + 1);
            int n2 = string2.indexOf(58);
            try {
                if (n2 > 0) {
                    switch (GemsMsgPropTableModel.getTypeFromString(string2.substring(0, n2))) {
                        case 9: {
                            Object[] arrobject = new Object[]{string.substring(0, n), string2.substring(n2 + 1)};
                            this.addRow(arrobject);
                            return;
                        }
                        case 5: {
                            Object[] arrobject = new Object[]{string.substring(0, n), new Integer(string2.substring(n2 + 1))};
                            this.addRow(arrobject);
                            return;
                        }
                        case 6: {
                            Object[] arrobject = new Object[]{string.substring(0, n), new Long(string2.substring(n2 + 1))};
                            this.addRow(arrobject);
                            return;
                        }
                        case 1: {
                            Object[] arrobject = new Object[]{string.substring(0, n), new Boolean(string2.substring(n2 + 1))};
                            this.addRow(arrobject);
                            return;
                        }
                        case 4: {
                            Object[] arrobject = new Object[]{string.substring(0, n), new Short(string2.substring(n2 + 1))};
                            this.addRow(arrobject);
                            return;
                        }
                        case 2: {
                            Object[] arrobject = new Object[]{string.substring(0, n), new Byte(string2.substring(n2 + 1))};
                            this.addRow(arrobject);
                            return;
                        }
                        case 8: {
                            Object[] arrobject = new Object[]{string.substring(0, n), new Double(string2.substring(n2 + 1))};
                            this.addRow(arrobject);
                            return;
                        }
                        case 7: {
                            Object[] arrobject = new Object[]{string.substring(0, n), new Float(string2.substring(n2 + 1))};
                            this.addRow(arrobject);
                            return;
                        }
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 18:
                        case 255: {
                            Object[] arrobject = new Object[]{string.substring(0, n), string2};
                            this.addRow(arrobject);
                            return;
                        }
                    }
                } else {
                    Object[] arrobject = new String[]{string.substring(0, n), string2};
                    this.addRow(arrobject);
                }
            } catch (Exception var6_15) {
                System.err.println("Exception: " + var6_15.getMessage());
            }
        }
    }

    static int getTypeFromString(String string) {
        if (string.equals("String")) {
            return 9;
        }
        if (string.equals("Integer")) {
            return 5;
        }
        if (string.equals("Long")) {
            return 6;
        }
        if (string.equals("Boolean")) {
            return 1;
        }
        if (string.equals("Short")) {
            return 4;
        }
        if (string.equals("Byte")) {
            return 2;
        }
        if (string.equals("Double")) {
            return 8;
        }
        if (string.equals("Float")) {
            return 7;
        }
        if (string.equals("byte[]")) {
            return 10;
        }
        if (string.equals("MapMag")) {
            return 11;
        }
        if (string.equals("StreamMsg")) {
            return 18;
        }
        if (string.equals("short-array")) {
            return 12;
        }
        if (string.equals("int-array")) {
            return 13;
        }
        if (string.equals("long-array")) {
            return 14;
        }
        if (string.equals("float-array")) {
            return 15;
        }
        if (string.equals("double-array")) {
            return 16;
        }
        return 255;
    }

    public void addProperty() {
        Object[] arrobject = new String[]{new String(), new String()};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
        this.m_table.editCellAt(this.m_table.getRowCount() - 1, 0);
    }

    public void addJMSReplyToProperty(GemsDestination gemsDestination) {
        int n = 0;
        for (n = 0; n < this.m_table.getRowCount(); ++n) {
            String string = (String) this.getValueAt(n, 0);
            if (string.equals("JMSReplyTo")) {
                return;
            }
            if (!string.startsWith("JMS") || string.startsWith("JMS_")) break;
        }
        Object[] arrobject = new Object[]{"JMSReplyTo", gemsDestination};
        this.insertRow(n, arrobject);
    }

    public void addCompressProperty() {
        int n = 0;
        for (n = 0; n < this.m_table.getRowCount(); ++n) {
            String string = (String) this.getValueAt(n, 0);
            if (string.equals("JMS_TIBCO_COMPRESS")) {
                return;
            }
            if (!string.startsWith("JMS")) break;
        }
        Object[] arrobject = new Object[]{"JMS_TIBCO_COMPRESS", new Boolean("true")};
        this.insertRow(n, arrobject);
    }

    public void addPreserveProperty() {
        int n = 0;
        for (n = 0; n < this.m_table.getRowCount(); ++n) {
            String string = (String) this.getValueAt(n, 0);
            if (string.equals("JMS_TIBCO_PRESERVE_UNDELIVERED")) {
                return;
            }
            if (!string.startsWith("JMS")) break;
        }
        Object[] arrobject = new Object[]{"JMS_TIBCO_PRESERVE_UNDELIVERED", new Boolean("true")};
        this.insertRow(n, arrobject);
    }

    public void addEmptyPropertyInfo(int n) {
        for (int i = 0; i < n; ++i) {
            Object[] arrobject = new String[]{new String(), new String()};
            this.addRow(arrobject);
        }
    }

    public void addBooleanProperty() {
        Object[] arrobject = new Object[]{"boolean" + this.m_table.getColumnName(0), new Boolean("true")};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addByteProperty() {
        Object[] arrobject = new Object[]{"byte" + this.m_table.getColumnName(0), 0};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addFloatProperty() {
        Object[] arrobject = new Object[]{"float" + this.m_table.getColumnName(0), new Float(0.0)};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addDoubleProperty() {
        Object[] arrobject = new Object[]{"double" + this.m_table.getColumnName(0), new Double(0.0)};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addStringProperty() {
        Object[] arrobject = new Object[]{"string" + this.m_table.getColumnName(0), new String()};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addShortProperty() {
        Object[] arrobject = new Object[]{"short" + this.m_table.getColumnName(0), (short) 0};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addLongProperty() {
        Object[] arrobject = new Object[]{"long" + this.m_table.getColumnName(0), new Long(0)};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addIntProperty() {
        Object[] arrobject = new Object[]{"int" + this.m_table.getColumnName(0), new Integer(0)};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    public void addIntegerProperty(String string, Integer n) {
        Object[] arrobject = new Object[]{string, n};
        this.addRow(arrobject);
        this.m_table.changeSelection(this.m_table.getRowCount() - 1, 0, false, false);
    }

    String getJMSCorrelationID() {
        for (int i = 0; i < this.getRowCount(); ++i) {
            if (!((String) this.getValueAt(i, 0)).equals("JMSCorrelationID")) continue;
            return (String) this.getValueAt(i, 1);
        }
        return "";
    }

    int getJMSDeliveryMode() {
        for (int i = 0; i < this.getRowCount(); ++i) {
            Object object;
            if (!((String) this.getValueAt(i, 0)).equals("JMSDeliveryMode") || !((object = this.getValueAt(i, 1)) instanceof DELMODE))
                continue;
            if ((DELMODE) ((Object) object) == DELMODE.PERSISTENT) {
                return 2;
            }
            if ((DELMODE) ((Object) object) != DELMODE.RELIABLE) continue;
            return 22;
        }
        return 1;
    }

    String getJMSDestination() {
        for (int i = 0; i < this.getRowCount(); ++i) {
            if (!((String) this.getValueAt(i, 0)).equals("JMSDestination")) continue;
            return (String) this.getValueAt(i, 1);
        }
        return "";
    }

    int getJMSExpiration() {
        for (int i = 0; i < this.getRowCount(); ++i) {
            if (!((String) this.getValueAt(i, 0)).equals("JMSExpiration")) continue;
            return Integer.parseInt((String) this.getValueAt(i, 1));
        }
        return 0;
    }

    int getJMSPriority() {
        for (int i = 0; i < this.getRowCount(); ++i) {
            if (!((String) this.getValueAt(i, 0)).equals("JMSPriority")) continue;
            return Integer.parseInt((String) this.getValueAt(i, 1));
        }
        return 4;
    }

    GemsDestination getJMSReplyTo() {
        int n = 0;
        for (n = 0; n < this.m_table.getRowCount(); ++n) {
            String string = (String) this.getValueAt(n, 0);
            if (string.equals("JMSReplyTo")) {
                return (GemsDestination) this.getValueAt(n, 1);
            }
            if (!string.startsWith("JMS")) break;
        }
        return null;
    }

    String getJMSType() {
        if (Gems.getGems().getShowExtendedProperties()) {
            return (String) this.getValueAt(3, 1);
        }
        return "";
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static enum DELMODE {
        NON_PERSISTENT,
        PERSISTENT,
        RELIABLE;


        private DELMODE() {
        }
    }

    private class MultiEditor
            implements TableCellEditor {
        private static final int DEFAULT = 0;
        private static final int COMBO_BOOL = 1;
        private static final int COMBO_DELMODE = 2;
        private static final int NOEDIT_DEST = 3;
        private DefaultCellEditor[] cellEditors;
        private JComboBox delmodeComboBox;
        private JComboBox boolComboBox;
        private JTextField noEditTextField;
        private int cellEditorID;
        private Object currObj;

        public MultiEditor() {
            this.noEditTextField = new JTextField();
            this.currObj = null;
            this.cellEditors = new DefaultCellEditor[4];
            this.delmodeComboBox = new JComboBox();
            for (DELMODE dELMODE : DELMODE.values()) {
                this.delmodeComboBox.addItem(dELMODE);
            }
            this.cellEditors[2] = new DefaultCellEditor(this.delmodeComboBox);
            this.boolComboBox = new JComboBox();
            this.boolComboBox.addItem("true");
            this.boolComboBox.addItem("false");
            this.cellEditors[1] = new DefaultCellEditor(this.boolComboBox);
            this.cellEditors[0] = new DefaultCellEditor(new JTextField());
            this.noEditTextField.setEditable(false);
            this.cellEditors[3] = new DefaultCellEditor(this.noEditTextField);
            this.cellEditorID = 0;
        }

        public Component getTableCellEditorComponent(JTable jTable, Object object, boolean bl, int n, int n2) {
            this.currObj = object;
            if (object instanceof DELMODE) {
                this.cellEditorID = 2;
            } else if (object instanceof Boolean) {
                this.cellEditorID = 1;
                this.boolComboBox.setSelectedItem(object.toString());
            } else if (object instanceof GemsDestination) {
                for (Container container = GemsMsgPropTableModel.this.m_table.getParent(); container != null; container = container.getParent()) {
                    if (!(container instanceof GemsMessageFrame)) continue;
                    GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker((JFrame) container, ((GemsMessageFrame) container).m_cn);
                    if (gemsDestinationPicker.m_retDest == null) break;
                    object = this.currObj = gemsDestinationPicker.m_retDest;
                    break;
                }
                this.cellEditorID = 3;
            } else {
                this.cellEditorID = 0;
            }
            return this.cellEditors[this.cellEditorID].getTableCellEditorComponent(jTable, object, bl, n, n2);
        }

        public Object getCellEditorValue() {
            switch (this.cellEditorID) {
                case 2: {
                    return this.delmodeComboBox.getSelectedItem();
                }
                case 1: {
                    return new Boolean((String) this.boolComboBox.getSelectedItem());
                }
                case 3: {
                    return this.currObj;
                }
            }
            try {
                Object object = this.cellEditors[this.cellEditorID].getCellEditorValue();
                if (this.currObj instanceof String) {
                    return object;
                }
                if (this.currObj instanceof Integer) {
                    return new Integer((String) object);
                }
                if (this.currObj instanceof Long) {
                    return new Long((String) object);
                }
                if (this.currObj instanceof Short) {
                    return new Short((String) object);
                }
                if (this.currObj instanceof Byte) {
                    return new Byte((String) object);
                }
                if (this.currObj instanceof Float) {
                    return new Float((String) object);
                }
                if (this.currObj instanceof Double) {
                    return new Double((String) object);
                }
                return object;
            } catch (Exception var1_2) {
                JOptionPane.showMessageDialog(null, var1_2.getMessage(), "Error", 1);
                System.err.println("Exception: " + var1_2.getMessage());
                return this.currObj;
            }
        }

        public boolean isCellEditable(EventObject eventObject) {
            return this.cellEditors[this.cellEditorID].isCellEditable(eventObject);
        }

        public boolean shouldSelectCell(EventObject eventObject) {
            return this.cellEditors[this.cellEditorID].shouldSelectCell(eventObject);
        }

        public boolean stopCellEditing() {
            return this.cellEditors[this.cellEditorID].stopCellEditing();
        }

        public void cancelCellEditing() {
            this.cellEditors[this.cellEditorID].cancelCellEditing();
        }

        public void addCellEditorListener(CellEditorListener cellEditorListener) {
            this.cellEditors[this.cellEditorID].addCellEditorListener(cellEditorListener);
        }

        public void removeCellEditorListener(CellEditorListener cellEditorListener) {
            this.cellEditors[this.cellEditorID].removeCellEditorListener(cellEditorListener);
        }

        public Component getComponent() {
            return this.cellEditors[this.cellEditorID].getComponent();
        }

        public int getClickCountToStart() {
            return this.cellEditors[this.cellEditorID].getClickCountToStart();
        }

        public void setClickCountToStart(int n) {
            this.cellEditors[this.cellEditorID].setClickCountToStart(n);
        }
    }

    class MultiRenderer
            extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
            Component component = super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
            return super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
        }
    }

}

