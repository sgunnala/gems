/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import com.tibco.gems.Gems;

public class GemsSubscriptionData
        extends AbstractTableModel {
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm:ss");
    private GemsChartFrame m_chart;
    private long _timeLimit = 300000;
    private Vector _chartingColumns = null;
    private Vector _subscriptionDataHeaders = null;
    private Vector[] _subscriptionDataVector = null;

    public GemsSubscriptionData(Vector vector) {
        this.setColumnIdentifiers(vector);
        this._chartingColumns = new Vector();
        timeFormat.setTimeZone(TimeZone.getDefault());
    }

    public void setColumnIdentifiers(Vector vector) {
        int n;
        this._subscriptionDataVector = new Vector[vector.size() + 1];
        this._subscriptionDataHeaders = new Vector(vector.size() + 1);
        this._subscriptionDataHeaders.insertElementAt("     ", 0);
        for (n = 0; n < vector.size(); ++n) {
            this._subscriptionDataHeaders.addElement((String) vector.elementAt(n));
        }
        for (n = 0; n < this._subscriptionDataHeaders.size(); ++n) {
            this._subscriptionDataVector[n] = new Vector(50);
        }
    }

    public long getTimeLimit() {
        return this._timeLimit;
    }

    public void setTimeLimit(long l) {
        this._timeLimit = l;
        this.purgeOldData(0);
        this.setXAxisMinMax();
    }

    public synchronized void purgeOldData(long l) {
        Long l2;
        long l3 = l;
        Vector vector = this._subscriptionDataVector[0];
        int n = vector.size();
        if (n <= 1) {
            return;
        }
        if (l3 == 0) {
            Long l4 = (Long) vector.elementAt(0);
            l3 = l4 - this._timeLimit;
        }
        for (int i = n - 1; i >= 0 && l3 > (l2 = (Long) vector.elementAt(0)) + this._timeLimit; --i) {
            for (int j = 0; j < this._subscriptionDataHeaders.size(); ++j) {
                this._subscriptionDataVector[j].removeElementAt(0);
            }
        }
    }

    public void setXAxisMinMax() {
    }

    public synchronized String[] getPointLabels() {
        Vector vector = this._subscriptionDataVector[0];
        if (vector == null) {
            return null;
        }
        int n = vector.size();
        String[] arrstring = new String[n];
        for (int i = 0; i < n; ++i) {
            long l = (Long) vector.elementAt(i);
            Date date = new Date(l);
            arrstring[i] = new String(timeFormat.format(date));
        }
        return arrstring;
    }

    public String getSeriesLabel(int n) {
        return null;
    }

    public String getSeriesName(int n) {
        return null;
    }

    public String getName() {
        return "Real-time Data";
    }

    public void setChart(GemsChartFrame gemsChartFrame) {
        this.m_chart = gemsChartFrame;
    }

    public synchronized Object getDataItem(int n, int n2) {
        Vector vector = this._subscriptionDataVector[0];
        Vector vector2 = this._subscriptionDataVector[1];
        switch (n) {
            case 0: {
                return vector.elementAt(n2);
            }
            case 1: {
                return vector2.elementAt(n2);
            }
        }
        return null;
    }

    public synchronized int getDataInterpretation() {
        return 0;
    }    public Object getValueAt(int n, int n2) {
        int n3;
        int n4 = this.getRowCount();
        int n5 = n3 = n4 != 0 ? n4 - 1 : n4;
        if (n2 == 0) {
            long l = (Long) this._subscriptionDataVector[0].elementAt(n3 - n);
            Date date = new Date(l);
            return date.toString();
        }
        return this._subscriptionDataVector[n2].elementAt(n3 - n);
    }

    public String getColumnName(int n) {
        return (String) this._subscriptionDataHeaders.elementAt(n);
    }    public int getColumnCount() {
        return this._subscriptionDataHeaders != null ? this._subscriptionDataHeaders.size() : 0;
    }

    public boolean isCellEditable(int n, int n2) {
        return false;
    }

    public Vector getColumnHeaders() {
        return this._subscriptionDataHeaders;
    }    public int getRowCount() {
        if (this._subscriptionDataVector == null || this._subscriptionDataVector[0] == null) {
            return 0;
        }
        return this._subscriptionDataVector[0].size();
    }

    public Vector getTimeSlices() {
        return this._subscriptionDataVector[0];
    }

    public synchronized Vector getDataPoints(String string) {
        for (int i = 0; i < this._subscriptionDataHeaders.size(); ++i) {
            if (!this._subscriptionDataHeaders.elementAt(i).equals(string)) continue;
            return this._subscriptionDataVector[i];
        }
        return new Vector();
    }

    public int getChartableFieldIndex(String string) {
        for (int i = 0; i < this._chartingColumns.size(); ++i) {
            int n = ((Long) this._chartingColumns.elementAt(i)).intValue();
            if (!this._subscriptionDataHeaders.elementAt(n).equals(string)) continue;
            return i;
        }
        return -1;
    }

    public void showChartableField(int n) {
        try {
            this.m_chart.showChartableField(n);
        } catch (Exception var2_2) {
            Gems.getGems();
            Gems.debug(var2_2.toString());
        }
    }

    public void removeChartableField(String string, int n) {
        this.m_chart.removeChartableField(n);
    }

    public synchronized void addRow(Date date, Hashtable hashtable) {
        this.purgeOldData(date.getTime());
        this._subscriptionDataVector[0].addElement(new Long(date.getTime()));
        for (int i = 1; i < this._subscriptionDataHeaders.size(); ++i) {
            String string = (String) this._subscriptionDataHeaders.elementAt(i);
            this._subscriptionDataVector[i].addElement(hashtable.get(string));
        }
        this.fireTableChanged(new TableModelEvent(this));
    }






}

