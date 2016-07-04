/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.TibjmsAdmin
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class GemsDestPropEditor
        extends JDialog {
    protected String m_type;
    protected JTextField m_conn;
    protected JTextField m_dest;
    protected JButton m_closeButton;
    protected JButton m_refreshButton;
    protected JButton m_lookup;
    protected GemsDetailsTableModel m_tableModel;
    JFrame m_frame;
    JPanel m_panel;
    GemsConnectionNode m_cn;
    JTable m_table;
    TableSorter m_sorter;

    public GemsDestPropEditor(JFrame jFrame, GemsConnectionNode gemsConnectionNode, String string, String string2) {
        super(jFrame, Gems.getGems().getTitlePrefix() + string + " Property Editor", true);
        this.setLocationRelativeTo(jFrame);
        this.setDefaultCloseOperation(2);
        this.m_frame = jFrame;
        this.m_cn = gemsConnectionNode;
        this.m_type = string;
        JMenuBar jMenuBar = this.constructMenuBar();
        this.setJMenuBar(jMenuBar);
        JPanel jPanel = new JPanel(true);
        jPanel.setLayout(new BorderLayout());
        this.getContentPane().add("Center", jPanel);
        JPanel jPanel2 = new JPanel(new SpringLayout(), true);
        jPanel.add((Component) jPanel2, "North");
        JLabel jLabel = new JLabel("Server:", 11);
        this.m_conn = new JTextField(gemsConnectionNode.getName(), 20);
        this.m_conn.setEditable(false);
        this.m_conn.setMaximumSize(new Dimension(0, 24));
        jLabel.setLabelFor(this.m_conn);
        jPanel2.add(jLabel);
        jPanel2.add(this.m_conn);
        JPanel jPanel3 = new JPanel(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        jPanel3.setMinimumSize(new Dimension(300, 24));
        JLabel jLabel2 = new JLabel(string + ":", 11);
        this.m_dest = new JTextField(string2, 32);
        this.m_dest.addKeyListener(new SubmitListener());
        jLabel2.setLabelFor(this.m_dest);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_dest);
        this.m_lookup = new JButton("...");
        this.m_lookup.addActionListener(new LookupAction());
        jPanel3.add(this.m_lookup);
        jPanel2.add(jPanel3);
        this.m_tableModel = new GemsDetailsTableModel();
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_tableModel.setPopupHandler(new PopupDestPropTableHandler(this.m_table, this.m_tableModel, this));
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_tableModel.setTable(this.m_table);
        this.m_table.setSelectionMode(0);
        if (!Gems.getGems().getViewOnlyMode()) {
            this.addMouseListenerToTable(this.m_table);
        }
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(635, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        Component component = Box.createRigidArea(new Dimension(225, 10));
        jPanel4.add(component);
        this.m_refreshButton = new JButton("Refresh");
        this.m_refreshButton.addActionListener(new RefreshPressed());
        jPanel4.add(this.m_refreshButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel4.add(component);
        this.m_closeButton = new JButton("Close");
        this.m_closeButton.addActionListener(new ClosePressed());
        jPanel4.add(this.m_closeButton);
        jPanel.add((Component) jPanel4, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 2, 2, 5, 5, 5, 5);
        this.m_frame.setIconImage(Gems.getGems().m_icon.getImage());
        this.getProperties();
        this.pack();
        this.show();
    }

    private JMenuBar constructMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        jMenu.setMnemonic(70);
        jMenuBar.add(jMenu);
        JMenuItem jMenuItem = jMenu.add(new JMenuItem("Exit"));
        jMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                GemsDestPropEditor.this.dispose();
            }
        });
        if (!Gems.getGems().getViewOnlyMode()) {
            jMenu = new JMenu("Edit");
            jMenu.setMnemonic(69);
            jMenuBar.add(jMenu);
            jMenuItem = new JMenuItem("Edit Property...");
            jMenuItem.addActionListener(new EditPropAction());
            jMenu.add(jMenuItem);
        }
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    GemsDestPropEditor.this.editSelectedProperty();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void getProperties() {
        if (this.m_dest.getText().length() == 0) {
            return;
        }
        if (this.m_type.equals("Queue")) {
            this.m_tableModel.populateQueueInfo(this.m_cn.m_adminConn, this.m_dest.getText());
        } else {
            this.m_tableModel.populateTopicInfo(this.m_cn.m_adminConn, this.m_dest.getText());
        }
    }

    public void dispose() {
        super.dispose();
    }

    public void editSelectedProperty() {
        this.editProperty(this.m_tableModel.getSelectedCol1());
    }

    public void editProperty(String string) {
        if (this.m_type.equals("Queue")) {
            GemsQueueNode.setProperty(this.m_frame, this.m_cn, string, this.m_dest.getText());
        } else {
            GemsTopicNode.setProperty(this.m_frame, this.m_cn, string, this.m_dest.getText());
        }
        this.getProperties();
    }

    public void editRowProperty(int n) {
        String string = (String) this.m_table.getValueAt(n, 0);
        this.editProperty(string);
    }

    class EditPropAction
            implements ActionListener {
        EditPropAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestPropEditor.this.editSelectedProperty();
        }
    }

    class LookupAction
            implements ActionListener {
        LookupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestinationPicker gemsDestinationPicker = new GemsDestinationPicker(GemsDestPropEditor.this.m_frame, GemsDestPropEditor.this.m_cn, GemsDestPropEditor.this.m_type.equals("Queue") ? GemsDestination.DEST_TYPE.Queue : GemsDestination.DEST_TYPE.Topic);
            if (gemsDestinationPicker.m_retDest != null) {
                GemsDestPropEditor.this.m_dest.setText(gemsDestinationPicker.m_retDest.m_destName);
                GemsDestPropEditor.this.getProperties();
            }
        }
    }

    public class SubmitListener
            implements KeyListener {
        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 10) {
                GemsDestPropEditor.this.getProperties();
            }
        }

        public void keyReleased(KeyEvent keyEvent) {
        }
    }

    class RefreshPressed
            implements ActionListener {
        RefreshPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestPropEditor.this.getProperties();
        }
    }

    class ClosePressed
            implements ActionListener {
        ClosePressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsDestPropEditor.this.dispose();
        }
    }

}

