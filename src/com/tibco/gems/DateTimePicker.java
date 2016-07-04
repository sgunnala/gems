/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.*;
import javax.swing.border.Border;

public class DateTimePicker
        extends JDialog
        implements ItemListener,
        MouseListener,
        FocusListener,
        KeyListener,
        ActionListener {
    private static final String[] MONTHS = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] DAYS = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final Color WEEK_DAYS_FOREGROUND = Color.black;
    private static final Color DAYS_FOREGROUND = Color.blue;
    private static final Color SELECTED_DAY_FOREGROUND = Color.white;
    private static final Color SELECTED_DAY_BACKGROUND = Color.blue;
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    private static final Border FOCUSED_BORDER = BorderFactory.createLineBorder(Color.yellow, 1);
    private static final int FIRST_YEAR = 1900;
    private static final int LAST_YEAR = 2100;
    private GregorianCalendar calendar;
    private JLabel[][] days;
    private FocusablePanel daysGrid;
    private JComboBox month;
    private JComboBox year;
    private JComboBox hour;
    private JComboBox minute;
    private JComboBox second;
    private JButton previousMonth;
    private JButton nextMonth;
    private JButton ok;
    private JButton cancel;
    private int offset;
    private int lastDay;
    private JLabel day;
    private boolean okClicked;

    public DateTimePicker(Dialog dialog, String string) {
        super(dialog, string, true);
        this.construct();
    }

    private void construct() {
        int n;
        int n2;
        this.calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        this.month = new JComboBox<String>(MONTHS);
        this.month.addItemListener(this);
        this.year = new JComboBox();
        for (n2 = 1900; n2 <= 2100; ++n2) {
            this.year.addItem(Integer.toString(n2));
        }
        this.year.addItemListener(this);
        this.previousMonth = new JButton("<<");
        this.previousMonth.addActionListener(this);
        this.nextMonth = new JButton(">>");
        this.nextMonth.addActionListener(this);
        this.days = new JLabel[7][7];
        for (n2 = 0; n2 < 7; ++n2) {
            this.days[0][n2] = new JLabel(DAYS[n2], 4);
            this.days[0][n2].setForeground(WEEK_DAYS_FOREGROUND);
        }
        for (n2 = 1; n2 < 7; ++n2) {
            for (n = 0; n < 7; ++n) {
                this.days[n2][n] = new JLabel(" ", 4);
                this.days[n2][n].setForeground(DAYS_FOREGROUND);
                this.days[n2][n].setBackground(SELECTED_DAY_BACKGROUND);
                this.days[n2][n].setBorder(EMPTY_BORDER);
                this.days[n2][n].addMouseListener(this);
            }
        }
        String[] arrstring = new String[24];
        for (n = 0; n < 24; ++n) {
            arrstring[n] = n < 10 ? "0" + n : Integer.toString(n);
        }
        String[] arrstring2 = new String[60];
        for (int i = 0; i < 60; ++i) {
            arrstring2[i] = i < 10 ? "0" + i : Integer.toString(i);
        }
        this.hour = new JComboBox<String>(arrstring);
        this.hour.addItemListener(this);
        this.minute = new JComboBox<String>(arrstring2);
        this.minute.addItemListener(this);
        this.second = new JComboBox<String>(arrstring2);
        this.second.addItemListener(this);
        this.ok = new JButton("Ok");
        this.ok.addActionListener(this);
        this.cancel = new JButton("Cancel");
        this.cancel.addActionListener(this);
        JPanel jPanel = new JPanel();
        jPanel.add(this.previousMonth);
        jPanel.add(this.month);
        jPanel.add(this.year);
        jPanel.add(this.nextMonth);
        this.daysGrid = new FocusablePanel(new GridLayout(7, 7, 5, 0));
        this.daysGrid.addFocusListener(this);
        this.daysGrid.addKeyListener(this);
        for (int j = 0; j < 7; ++j) {
            for (int k = 0; k < 7; ++k) {
                this.daysGrid.add(this.days[j][k]);
            }
        }
        this.daysGrid.setBackground(Color.white);
        this.daysGrid.setBorder(BorderFactory.createLoweredBevelBorder());
        JPanel jPanel2 = new JPanel();
        jPanel2.add(this.daysGrid);
        GridLayout gridLayout = new GridLayout(2, 3);
        gridLayout.setHgap(10);
        JPanel jPanel3 = new JPanel(gridLayout);
        JLabel jLabel = new JLabel("Hour");
        jLabel.setHorizontalAlignment(0);
        JLabel jLabel2 = new JLabel("Minute");
        jLabel2.setHorizontalAlignment(0);
        JLabel jLabel3 = new JLabel("Second");
        jLabel3.setHorizontalAlignment(0);
        jPanel3.add(jLabel);
        jPanel3.add(jLabel2);
        jPanel3.add(jLabel3);
        jPanel3.add(this.hour);
        jPanel3.add(this.minute);
        jPanel3.add(this.second);
        JPanel jPanel4 = new JPanel();
        jPanel4.add(this.ok);
        jPanel4.add(this.cancel);
        JPanel jPanel5 = new JPanel(new BorderLayout());
        jPanel5.add((Component) jPanel3, "North");
        jPanel5.add((Component) jPanel4, "South");
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.add((Component) jPanel, "North");
        container.add((Component) jPanel2, "Center");
        container.add((Component) jPanel5, "South");
        this.pack();
        this.setResizable(false);
    }

    public DateTimePicker(Dialog dialog) {
        super(dialog, true);
        this.construct();
    }

    public DateTimePicker(Frame frame, String string) {
        super(frame, string, true);
        this.construct();
    }

    public DateTimePicker(Frame frame) {
        super(frame, true);
        this.construct();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.ok) {
            this.okClicked = true;
            this.setVisible(false);
        } else if (actionEvent.getSource() == this.previousMonth) {
            if (this.month.getSelectedIndex() == 0) {
                int n = 11;
                int n2 = this.year.getSelectedIndex() - 1;
                this.month.setSelectedIndex(n);
                this.year.setSelectedIndex(n2);
            } else {
                int n = this.month.getSelectedIndex() - 1;
                this.month.setSelectedIndex(n);
            }
            this.update();
        } else if (actionEvent.getSource() == this.nextMonth) {
            if (this.month.getSelectedIndex() == 11) {
                int n = 0;
                int n3 = this.year.getSelectedIndex() + 1;
                this.month.setSelectedIndex(n);
                this.year.setSelectedIndex(n3);
            } else {
                int n = this.month.getSelectedIndex() + 1;
                this.month.setSelectedIndex(n);
            }
            this.update();
        } else if (actionEvent.getSource() == this.cancel) {
            this.setVisible(false);
        }
    }

    private void update() {
        int n;
        int n2 = this.getSelectedDay();
        for (n = 0; n < 7; ++n) {
            this.days[1][n].setText(" ");
            this.days[5][n].setText(" ");
            this.days[6][n].setText(" ");
        }
        this.calendar.set(5, 1);
        this.calendar.set(2, this.month.getSelectedIndex() + 0);
        this.calendar.set(1, this.year.getSelectedIndex() + 1900);
        this.offset = this.calendar.get(7) - 1;
        this.lastDay = this.calendar.getActualMaximum(5);
        for (n = 0; n < this.lastDay; ++n) {
            this.days[(n + this.offset) / 7 + 1][(n + this.offset) % 7].setText(String.valueOf(n + 1));
        }
        if (n2 != -1) {
            if (n2 > this.lastDay) {
                n2 = this.lastDay;
            }
            this.setSelected(n2);
        }
    }

    private int getSelectedDay() {
        if (this.day == null) {
            return -1;
        }
        try {
            return Integer.parseInt(this.day.getText());
        } catch (NumberFormatException var1_1) {
            return -1;
        }
    }

    private void setSelected(int n) {
        this.setSelected(this.days[(n + this.offset - 1) / 7 + 1][(n + this.offset - 1) % 7]);
    }

    private void setSelected(JLabel jLabel) {
        if (this.day != null) {
            this.day.setForeground(DAYS_FOREGROUND);
            this.day.setOpaque(false);
            this.day.setBorder(EMPTY_BORDER);
        }
        this.day = jLabel;
        this.day.setForeground(SELECTED_DAY_FOREGROUND);
        this.day.setOpaque(true);
        if (this.daysGrid.hasFocus()) {
            this.day.setBorder(FOCUSED_BORDER);
        }
    }

    public void focusGained(FocusEvent focusEvent) {
        this.setSelected(this.day);
    }

    public void focusLost(FocusEvent focusEvent) {
        this.setSelected(this.day);
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        this.update();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        JLabel jLabel = (JLabel) mouseEvent.getSource();
        if (!jLabel.getText().equals(" ")) {
            this.setSelected(jLabel);
        }
        this.daysGrid.requestFocus();
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void keyPressed(KeyEvent keyEvent) {
        int n = this.getSelectedDay();
        switch (keyEvent.getKeyCode()) {
            case 37: {
                if (n <= 1) break;
                this.setSelected(n - 1);
                break;
            }
            case 39: {
                if (n >= this.lastDay) break;
                this.setSelected(n + 1);
                break;
            }
            case 38: {
                if (n <= 7) break;
                this.setSelected(n - 7);
                break;
            }
            case 40: {
                if (n > this.lastDay - 7) break;
                this.setSelected(n + 7);
            }
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
    }

    public Date select() {
        return this.select(new Date());
    }

    public Date select(Date date) {
        this.calendar.setTime(date);
        this.calendar.setTimeZone(TimeZone.getDefault());
        int n = this.calendar.get(5);
        int n2 = this.calendar.get(2);
        int n3 = this.calendar.get(1);
        int n4 = this.calendar.get(11);
        int n5 = this.calendar.get(12);
        int n6 = this.calendar.get(13);
        this.year.setSelectedIndex(n3 - 1900);
        this.month.setSelectedIndex(n2 - 0);
        this.hour.setSelectedIndex(n4);
        this.minute.setSelectedIndex(n5);
        this.second.setSelectedIndex(n6);
        this.setSelected(n);
        this.okClicked = false;
        this.setVisible(true);
        if (!this.okClicked) {
            return null;
        }
        this.calendar.set(5, this.getSelectedDay());
        this.calendar.set(2, this.month.getSelectedIndex() + 0);
        this.calendar.set(1, this.year.getSelectedIndex() + 1900);
        this.calendar.set(11, this.getSelectedHour());
        this.calendar.set(12, this.getSelectedMinute());
        this.calendar.set(13, this.getSelectedSecond());
        return this.calendar.getTime();
    }

    private int getSelectedHour() {
        if (this.hour == null) {
            return -1;
        }
        try {
            return Integer.parseInt(this.hour.getSelectedItem().toString());
        } catch (NumberFormatException var1_1) {
            return -1;
        }
    }

    private int getSelectedMinute() {
        if (this.minute == null) {
            return -1;
        }
        try {
            return Integer.parseInt(this.minute.getSelectedItem().toString());
        } catch (NumberFormatException var1_1) {
            return -1;
        }
    }

    private int getSelectedSecond() {
        if (this.second == null) {
            return -1;
        }
        try {
            return Integer.parseInt(this.second.getSelectedItem().toString());
        } catch (NumberFormatException var1_1) {
            return -1;
        }
    }

    private static class FocusablePanel
            extends JPanel {
        public FocusablePanel(LayoutManager layoutManager) {
            super(layoutManager);
        }

        public boolean isFocusTraversable() {
            return true;
        }
    }

}

