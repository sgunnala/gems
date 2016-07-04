/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.*;

public class GemsDynamicPropertyDialog
        extends JDialog
        implements ActionListener {
    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");
    private boolean isOK = false;
    private Map propertyControls;
    private String m_className;
    private JPanel m_panel;

    public GemsDynamicPropertyDialog(JFrame jFrame, String string, String string2, String string3) {
        super(jFrame, string, true);
        this.m_className = string2;
        this.okButton.addActionListener(this);
        this.cancelButton.addActionListener(this);
        this.m_panel = this.getPanel(string2);
        this.getContentPane().setLayout(new BorderLayout());
        if (string3 != null) {
            this.getContentPane().add((Component) new JLabel(string3), "North");
        }
        this.getContentPane().add((Component) this.m_panel, "Center");
        JPanel jPanel = new JPanel(new GridLayout(1, 2, 4, 0));
        jPanel.add(this.okButton);
        jPanel.add(this.cancelButton);
        JPanel jPanel2 = new JPanel(new FlowLayout(1));
        jPanel2.add(jPanel);
        this.getContentPane().add((Component) jPanel2, "South");
    }

    protected JPanel getPanel(String string) {
        try {
            Class class_ = Class.forName(string);
            return this.getPanel(class_);
        } catch (ClassNotFoundException var2_3) {
            var2_3.printStackTrace();
            return null;
        }
    }

    protected JPanel getPanel(Class class_) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.ipady = 1;
        int n = 0;
        JPanel jPanel = new JPanel(gridBagLayout);
        Map<String, String> map = ReflectionUtils.getWriteableProperties(class_);
        this.propertyControls = new TreeMap();
        for (String string : map.keySet()) {
            String string2 = (String) map.get(string);
            if (ReflectionUtils.isPrimitiveType(string2)) {
                Object object;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(Character.toUpperCase(string.charAt(0)));
                boolean bl = false;
                boolean bl2 = false;
                for (int i = 1; i < string.length(); ++i) {
                    object = string.charAt(i);
                    if (Character.isUpperCase((char) object)) {
                        if (!bl && i > 1) {
                            stringBuffer.append(" ");
                        }
                        bl = true;
                    } else {
                        bl = false;
                    }
                    stringBuffer.append((char) object);
                }
                stringBuffer.append(":");
                JLabel jLabel = new JLabel(stringBuffer.toString());
                gridBagConstraints.gridx = 0;
                gridBagConstraints.anchor = 12;
                gridBagLayout.setConstraints(jLabel, gridBagConstraints);
                jPanel.add(jLabel);
                gridBagConstraints.gridx = 1;
                gridBagConstraints.anchor = 18;
                if (string2.equals("boolean") || string2.equals("java.lang.Boolean")) {
                    object = new JCheckBox();
                    this.propertyControls.put(string, object);
                    gridBagLayout.setConstraints((Component) object, gridBagConstraints);
                    jPanel.add((Component) object);
                } else {
                    object = new JTextField(30);
                    this.propertyControls.put(string, object);
                    gridBagLayout.setConstraints((Component) object, gridBagConstraints);
                    jPanel.add((Component) object);
                }
                n += 30;
            }
            ++gridBagConstraints.gridy;
        }
        jPanel.setSize(425, n);
        return jPanel;
    }

    public Object getObject() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dimension2 = this.m_panel.getSize();
        int n = dimension2.width;
        int n2 = dimension2.height + 30;
        this.setSize(n, n2);
        this.setLocation(dimension.width / 2 - n / 2, dimension.height / 2 - n2 / 2);
        this.pack();
        this.setVisible(true);
        if (!this.isOK) {
            return null;
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for (Object string : this.propertyControls.keySet()) {
            String string2 = null;
            JComponent jComponent = (JComponent) this.propertyControls.get(string);
            if (jComponent instanceof JTextField) {
                string2 = ((JTextField) jComponent).getText();
            } else if (jComponent instanceof JCheckBox) {
                string2 = Boolean.toString(((JCheckBox) jComponent).isSelected());
            }
            treeMap.put((String) string, string2);
        }
        return ReflectionUtils.buildObject(this.m_className, treeMap);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        this.isOK = actionEvent.getSource() == this.okButton;
        this.setVisible(false);
    }
}

