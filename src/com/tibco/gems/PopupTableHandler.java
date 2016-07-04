/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class PopupTableHandler
        extends PopupHandler {
    static File m_currentdir = null;
    public JTable m_table;
    public int m_row = 0;
    public int m_col = 0;
    public Object m_value = null;
    public AbstractAction copy = null;
    public AbstractAction save2csv = null;

    public PopupTableHandler(JTable jTable) {
        this.m_table = jTable;
    }

    public JPopupMenu createPopup(Point point) {
        JPopupMenu jPopupMenu = super.createPopup(point);
        this.m_col = this.m_table.columnAtPoint(point);
        this.m_row = this.m_table.rowAtPoint(point);
        this.m_value = this.m_table.getModel().getValueAt(this.m_row, this.m_col);
        if (this.copy == null) {
            this.copy = new TableCopyAction("Copy Cell", null);
        }
        if (this.save2csv == null) {
            this.save2csv = new TableSave2CSVAction("Save Table To CSV File...", null);
        }
        jPopupMenu.add(this.copy);
        jPopupMenu.addSeparator();
        jPopupMenu.add(this.save2csv);
        return jPopupMenu;
    }

    public class TableSave2CSVAction
            extends AbstractAction {
        public TableSave2CSVAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                TableModel tableModel = PopupTableHandler.this.m_table.getModel();
                JFileChooser jFileChooser = new JFileChooser(PopupTableHandler.m_currentdir);
                jFileChooser.setApproveButtonText("Save");
                jFileChooser.setDialogTitle("Save Table To CSV File");
                int n = jFileChooser.showOpenDialog(PopupTableHandler.this.m_table);
                PopupTableHandler.m_currentdir = jFileChooser.getCurrentDirectory();
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
                        TableColumn tableColumn = PopupTableHandler.this.m_table.getColumnModel().getColumn(n2);
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

        public boolean isEnabled() {
            return PopupTableHandler.this.m_table != null;
        }
    }

    public class TableCopyAction
            extends AbstractAction {
        public TableCopyAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            StringSelection stringSelection = new StringSelection(PopupTableHandler.this.m_value.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
        }

        public boolean isEnabled() {
            return PopupTableHandler.this.m_table != null;
        }
    }

}

