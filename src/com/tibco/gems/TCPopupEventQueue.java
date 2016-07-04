/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

public class TCPopupEventQueue
        extends EventQueue {
    static File m_currentdir = null;
    public JPopupMenu popup;
    public BasicAction cut;
    public BasicAction copy;
    public BasicAction paste;
    public BasicAction selectAll;
    public BasicAction hexbytes;
    public BasicAction asciibytes;
    public BasicAction textAct;
    public BasicAction xmlAct;
    public BasicAction save2csv;
    public BasicAction servprop;
    public BasicAction clear;
    public BasicAction showtotal;
    public BasicAction servtrace;
    public BasicAction purge;

    public void createEventTablePopupMenu(JTable jTable, Object object) {
        this.copy = new TableCopyAction("Copy Cell", null);
        this.clear = new TableClearEventsAction("Clear Events", null);
        this.copy.setTableComponent(jTable, object);
        this.clear.setTableComponent(jTable, object);
        this.popup = new JPopupMenu();
        this.popup.add(this.copy);
        this.popup.addSeparator();
        this.popup.add(this.clear);
    }

    public class TableClearEventsAction
            extends BasicAction {
        int row;

        public TableClearEventsAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            TableSorter tableSorter = (TableSorter) this.tabcomp.getModel();
            Gems.getGems().clearCurrentEventsDisplay();
        }
    }

    public class TablePurgeQueueAction
            extends BasicAction {
        int row;

        public TablePurgeQueueAction(String string, Icon icon, int n) {
            super(string, icon);
            this.row = n;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            TableSorter tableSorter = (TableSorter) this.tabcomp.getModel();
            System.err.println(tableSorter.getTableModel().getValueAt(this.row, 0));
        }
    }

    public class TableSelectAllAction
            extends BasicAction {
        public TableSelectAllAction(String string, Icon icon) {
            super(string, icon);
            this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("ctrl A"));
        }

        public void actionPerformed(ActionEvent actionEvent) {
            TableSorter tableSorter = (TableSorter) this.tabcomp.getModel();
            if (tableSorter.getTableModel() instanceof GemsDestTableModel) {
                GemsDestTableModel gemsDestTableModel = (GemsDestTableModel) tableSorter.getTableModel();
                gemsDestTableModel.selectAllRows();
            } else if (tableSorter.getTableModel() instanceof GemsMessageTableModel) {
                GemsMessageTableModel gemsMessageTableModel = (GemsMessageTableModel) tableSorter.getTableModel();
                gemsMessageTableModel.selectAllRows();
            }
        }
    }

    public class TableSave2CSVAction
            extends BasicAction {
        public TableSave2CSVAction(String string, Icon icon) {
            super(string, icon);
        }

        public boolean isEnabled() {
            return this.tabcomp != null;
        }        public void actionPerformed(ActionEvent actionEvent) {
            try {
                TableModel tableModel = this.tabcomp.getModel();
                JFileChooser jFileChooser = new JFileChooser(TCPopupEventQueue.m_currentdir);
                jFileChooser.setApproveButtonText("Save");
                jFileChooser.setDialogTitle("Save Table To CSV File");
                int n = jFileChooser.showOpenDialog(this.tabcomp);
                TCPopupEventQueue.m_currentdir = jFileChooser.getCurrentDirectory();
                if (n == 0) {
                    int n2;
                    File file = jFileChooser.getSelectedFile();
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    PrintWriter printWriter = new PrintWriter(fileOutputStream);
                    int n3 = tableModel.getColumnCount();
                    int n4 = tableModel.getRowCount();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (n2 = 0; n2 < n3; ++n2) {
                        TableColumn tableColumn = this.tabcomp.getColumnModel().getColumn(n2);
                        stringBuffer.append(tableColumn.getHeaderValue());
                        if (n2 + 1 >= n3) continue;
                        stringBuffer.append(Gems.getGems().getCSVFileDelimiter());
                    }
                    if (stringBuffer.charAt(0) == 'I' && stringBuffer.charAt(1) == 'D') {
                        printWriter.print('\'');
                    }
                    printWriter.println(stringBuffer.toString());
                    for (n2 = 0; n2 < n4; ++n2) {
                        StringBuffer stringBuffer2 = new StringBuffer();
                        for (int i = 0; i < n3; ++i) {
                            stringBuffer2.append(tableModel.getValueAt(n2, i));
                            if (i + 1 >= n3) continue;
                            stringBuffer2.append(Gems.getGems().getCSVFileDelimiter());
                        }
                        printWriter.println(stringBuffer2.toString());
                    }
                    printWriter.close();
                }
            } catch (IOException var2_3) {
                System.err.println("JavaIOException: " + var2_3.getMessage());
                return;
            }
        }


    }

    public class TableCopyAction
            extends BasicAction {
        public TableCopyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            StringSelection stringSelection = new StringSelection(this.v.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
        }

        public boolean isEnabled() {
            return this.tabcomp != null;
        }
    }

    public class ShowTotalAction
            extends BasicAction {
        public ShowTotalAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Gems.getGems().setShowTotals(!Gems.getGems().getShowTotals());
        }

        public boolean isEnabled() {
            return this.tabcomp != null;
        }
    }

    public class TableServerTraceAction
            extends BasicAction {
        public TableServerTraceAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Gems.getGems().getTreeModel().setServerTrace();
        }

        public boolean isEnabled() {
            return true;
        }
    }

    public class TableServerPropertyAction
            extends BasicAction {
        public TableServerPropertyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Gems.getGems().getTreeModel().serverPanelDoubleClick((String) this.v);
        }

        public boolean isEnabled() {
            return this.v != null;
        }
    }

    public class SelectAllAction
            extends BasicAction {
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
            extends BasicAction {
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
            extends BasicAction {
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
            extends BasicAction {
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

    public abstract class BasicAction
            extends AbstractAction {
        JTextComponent txtcomp;
        JTable tabcomp;
        Object v;

        public BasicAction(String string, Icon icon) {
            super(string, icon);
            this.putValue("ShortDescription", string);
        }

        public void setTextComponent(JTextComponent jTextComponent) {
            this.txtcomp = jTextComponent;
        }

        public void setTableComponent(JTable jTable, Object object) {
            this.tabcomp = jTable;
            this.v = object;
        }

        public abstract void actionPerformed(ActionEvent var1);
    }

    public class AsciiBytesAction
            extends BasicAction {
        public AsciiBytesAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (this.txtcomp != null) {
                ((GemsMessageText) this.txtcomp).setMode(2);
            }
        }

        public boolean isEnabled() {
            return this.txtcomp != null;
        }
    }

    public class HexBytesAction
            extends BasicAction {
        public HexBytesAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            ((GemsMessageText) this.txtcomp).setMode(1);
        }

        public boolean isEnabled() {
            return this.txtcomp != null;
        }
    }

    public class XmlAction
            extends BasicAction {
        public XmlAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            ((GemsMessageText) this.txtcomp).setMode(4);
        }

        public boolean isEnabled() {
            return this.txtcomp != null;
        }
    }

    public class TextAction
            extends BasicAction {
        public TextAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            ((GemsMessageText) this.txtcomp).setMode(3);
        }

        public boolean isEnabled() {
            return this.txtcomp != null;
        }
    }

    protected void dispatchEvent(AWTEvent aWTEvent) {
        super.dispatchEvent(aWTEvent);
        if (!(aWTEvent instanceof MouseEvent)) {
            return;
        }
        MouseEvent mouseEvent = (MouseEvent) aWTEvent;
        if (!mouseEvent.isPopupTrigger()) {
            return;
        }
        Component component = SwingUtilities.getDeepestComponentAt((Component) mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY());
        if (component == null) {
            return;
        }
        Point point = mouseEvent.getPoint();
        Point point2 = mouseEvent.getComponent().getLocation();
        Point point3 = component.getLocationOnScreen();
        point3.translate((int) (-point2.getX()), (int) (-point2.getY()));
        point.translate((int) (-point3.getX()), (int) (-point3.getY()));
        boolean bl = false;
        Object object = null;
        int n = 0;
        int n2 = 0;
        if (component instanceof JTable) {
            bl = true;
            n2 = ((JTable) component).columnAtPoint(point);
            n = ((JTable) component).rowAtPoint(point);
            object = ((JTable) component).getModel().getValueAt(n, n2);
            if (object == null) {
                return;
            }
        } else if (!(component instanceof JTextComponent)) {
            return;
        }
        if (MenuSelectionManager.defaultManager().getSelectedPath().length > 0) {
            return;
        }
        if (bl) {
            TableSorter tableSorter = null;
            if (((JTable) component).getModel() instanceof TableSorter) {
                tableSorter = (TableSorter) ((JTable) component).getModel();
            }
            if (tableSorter != null && tableSorter.getTableModel() instanceof GemsDestTableModel) {
                this.createCheckTablePopupMenu((JTable) component, object);
            } else if (((JTable) component).getModel() instanceof GetPopupHandler) {
                PopupHandler popupHandler = ((GetPopupHandler) ((Object) ((JTable) component).getModel())).getPopupHandler();
                this.popup = popupHandler.createPopup(point);
            } else if (tableSorter != null && tableSorter.getTableModel() instanceof GetPopupHandler) {
                PopupHandler popupHandler = ((GetPopupHandler) ((Object) tableSorter.getTableModel())).getPopupHandler();
                this.popup = popupHandler.createPopup(point);
            } else {
                this.createTablePopupMenu((JTable) component, object, n, n2);
            }
        } else {
            ((JTextComponent) component).grabFocus();
            this.createTextPopupMenu((JTextComponent) component);
        }
        this.showPopup((Component) mouseEvent.getSource(), mouseEvent);
    }

    public void createCheckTablePopupMenu(JTable jTable, Object object) {
        this.copy = new TableCopyAction("Copy Cell", null);
        this.selectAll = new TableSelectAllAction("Select All", null);
        this.copy.setTableComponent(jTable, object);
        this.selectAll.setTableComponent(jTable, object);
        this.popup = new JPopupMenu();
        this.popup.add(this.copy);
        this.popup.addSeparator();
        this.popup.add(this.selectAll);
    }

    public void createTablePopupMenu(JTable jTable, Object object, int n, int n2) {
        this.copy = new TableCopyAction("Copy Cell", null);
        this.servprop = new TableServerPropertyAction("Set Server Property...", null);
        this.servtrace = new TableServerTraceAction("Set Server Trace...", null);
        this.save2csv = new TableSave2CSVAction("Save Table To CSV File...", null);
        this.purge = null;
        this.copy.setTableComponent(jTable, object);
        this.save2csv.setTableComponent(jTable, object);
        this.servprop.setTableComponent(jTable, object);
        this.popup = new JPopupMenu();
        this.popup.add(this.copy);
        this.popup.addSeparator();
        this.popup.add(this.save2csv);
        if (this.purge != null) {
            this.popup.addSeparator();
            this.popup.add(this.purge);
        }
        TableSorter tableSorter = null;
        if (jTable.getModel() instanceof TableSorter) {
            tableSorter = (TableSorter) jTable.getModel();
        }
        if (tableSorter != null && !Gems.getGems().getViewOnlyMode() && tableSorter.getTableModel() instanceof GemsServerInfoTableModel) {
            this.popup.addSeparator();
            this.popup.add(this.servprop);
            this.popup.add(this.servtrace);
        } else if (jTable.getModel() instanceof GemsServerMonitorTableModel) {
            this.showtotal = new ShowTotalAction("Show/Hide Totals", null);
            this.showtotal.setTableComponent(jTable, object);
            this.popup.addSeparator();
            this.popup.add(this.showtotal);
        }
    }

    public void createTextPopupMenu(JTextComponent jTextComponent) {
        this.cut = new CutAction("Cut", null);
        this.copy = new CopyAction("Copy", null);
        this.paste = new PasteAction("Paste", null);
        this.selectAll = new SelectAllAction("Select All", null);
        this.cut.setTextComponent(jTextComponent);
        this.copy.setTextComponent(jTextComponent);
        this.paste.setTextComponent(jTextComponent);
        this.selectAll.setTextComponent(jTextComponent);
        this.popup = new JPopupMenu();
        this.popup.add(this.cut);
        this.popup.add(this.copy);
        this.popup.add(this.paste);
        this.popup.addSeparator();
        this.popup.add(this.selectAll);
        if (jTextComponent instanceof GemsMessageText) {
            if (((GemsMessageText) jTextComponent).getMode() == 1) {
                this.popup.addSeparator();
                this.asciibytes = new AsciiBytesAction("View as Text", null);
                this.asciibytes.setTextComponent(jTextComponent);
                this.popup.add(this.asciibytes);
            } else if (((GemsMessageText) jTextComponent).getMode() == 2) {
                this.popup.addSeparator();
                this.hexbytes = new HexBytesAction("View as Hex", null);
                this.hexbytes.setTextComponent(jTextComponent);
                this.popup.add(this.hexbytes);
            } else if (((GemsMessageText) jTextComponent).getMode() == 4) {
                this.popup.addSeparator();
                this.textAct = new TextAction("View as Text", null);
                this.textAct.setTextComponent(jTextComponent);
                this.popup.add(this.textAct);
            } else if (((GemsMessageText) jTextComponent).getMode() == 3) {
                this.popup.addSeparator();
                this.xmlAct = new XmlAction("View as XML", null);
                this.xmlAct.setTextComponent(jTextComponent);
                this.popup.add(this.xmlAct);
            }
        }
    }

    public void showPopup(Component component, MouseEvent mouseEvent) {
        this.popup.validate();
        this.popup.show(component, mouseEvent.getX(), mouseEvent.getY());
    }

}

