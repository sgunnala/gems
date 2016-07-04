/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.DestinationInfo
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;

import com.tibco.tibjms.admin.DestinationInfo;

public abstract class GemsPurgeBase
        extends JDialog {
    protected String m_type;
    protected JTextField m_conn;
    protected JTextField m_pattern;
    protected JButton m_startButton;
    protected JButton m_stopButton;
    protected JButton m_lookup;
    protected GemsDestTableModel m_tableModel;
    JFrame m_frame;
    JPanel m_panel;
    GemsConnectionNode m_cn;
    JTable m_table;
    TableSorter m_sorter;

    public GemsPurgeBase(JFrame jFrame, GemsConnectionNode gemsConnectionNode, String string, String string2) {
        super(jFrame, Gems.getGems().getTitlePrefix() + "Purge " + string + "s", true);
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
        JLabel jLabel2 = new JLabel(string + " Pattern:", 11);
        this.m_pattern = new JTextField(string2, 32);
        jLabel2.setLabelFor(this.m_pattern);
        jPanel2.add(jLabel2);
        jPanel3.add(this.m_pattern);
        this.m_lookup = new JButton("LookUp");
        this.m_lookup.addActionListener(new LookupAction());
        jPanel3.add(this.m_lookup);
        jPanel2.add(jPanel3);
        this.m_tableModel = new GemsDestTableModel(false, true);
        this.m_sorter = new TableSorter(this.m_tableModel);
        this.m_table = new JTable(this.m_sorter);
        this.m_table.getTableHeader().setReorderingAllowed(false);
        this.m_sorter.setTableHeader(this.m_table.getTableHeader());
        this.m_table.setRowSelectionAllowed(false);
        this.m_tableModel.m_table = this.m_table;
        this.addMouseListenerToTable(this.m_table);
        JScrollPane jScrollPane = new JScrollPane(this.m_table);
        jScrollPane.setPreferredSize(new Dimension(635, 300));
        jPanel.add((Component) jScrollPane, "Center");
        JPanel jPanel4 = new JPanel(true);
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        Component component = Box.createRigidArea(new Dimension(225, 10));
        jPanel4.add(component);
        this.m_startButton = new JButton("Purge");
        this.m_startButton.addActionListener(new StartPressed());
        this.m_stopButton = new JButton("Cancel");
        this.m_stopButton.addActionListener(new StopPressed());
        jPanel4.add(this.m_startButton);
        component = Box.createRigidArea(new Dimension(20, 10));
        jPanel4.add(component);
        jPanel4.add(this.m_stopButton);
        jPanel.add((Component) jPanel4, "South");
        SpringUtilities.makeCompactGrid(jPanel2, 2, 2, 5, 5, 5, 5);
        this.m_frame.setIconImage(Gems.getGems().m_icon.getImage());
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
                GemsPurgeBase.this.dispose();
            }
        });
        jMenu = new JMenu("Edit");
        jMenu.setMnemonic(69);
        jMenuBar.add(jMenu);
        jMenuItem = new JMenuItem("Select All");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 2));
        jMenuItem.addActionListener(new SelectAllAction());
        jMenu.add(jMenuItem);
        return jMenuBar;
    }

    public void addMouseListenerToTable(JTable jTable) {
        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && GemsPurgeBase.this.m_table.getSelectedColumn() > 0) {
                    GemsPurgeBase.this.m_tableModel.toggleSelectedRow();
                }
            }
        };
        jTable.addMouseListener(mouseAdapter);
    }

    public void dispose() {
        super.dispose();
    }

    public void start() {
        this.purgeDestinations(this.m_tableModel.getSelectedDestinations());
        this.m_tableModel.populateDestinationInfo(this.getDestinationInfo());
    }

    public abstract void purgeDestinations(Vector var1);

    public abstract DestinationInfo[] getDestinationInfo();

    public void stop() {
        this.dispose();
    }

    class SelectAllAction
            implements ActionListener {
        SelectAllAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPurgeBase.this.m_tableModel.selectAllRows();
        }
    }

    class LookupAction
            implements ActionListener {
        LookupAction() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPurgeBase.this.m_tableModel.buildColumnHeaders();
            GemsPurgeBase.this.m_sorter.setSortingStatus(3, -1);
            GemsPurgeBase.this.m_tableModel.populateDestinationInfo(GemsPurgeBase.this.getDestinationInfo());
        }
    }

    class StopPressed
            implements ActionListener {
        StopPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPurgeBase.this.stop();
        }
    }

    class StartPressed
            implements ActionListener {
        StartPressed() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            GemsPurgeBase.this.start();
        }
    }

}

