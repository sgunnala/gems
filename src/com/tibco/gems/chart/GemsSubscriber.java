/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.tibco.gems.ReflectionUtils;

public class GemsSubscriber {
    private Vector _chartTableViews;
    private Vector _rowData;
    private Vector _chartableFields;
    private Vector _columnHeaders;
    private String _title;

    public GemsSubscriber(Vector vector, String string) {
        this._columnHeaders = vector;
        this._chartableFields = vector;
        this._rowData = new Vector();
        this._chartTableViews = new Vector(10);
        this._title = string;
    }

    public void constructColumnHeaders() {
    }

    public boolean anyChartableField() {
        return this._chartableFields.size() > 0;
    }

    public Vector getChartableFields() {
        return this._chartableFields;
    }

    public String getTitle() {
        return this._title;
    }

    public void addChart(GemsChartFrame gemsChartFrame) {
        this._chartTableViews.addElement(gemsChartFrame);
    }

    public void doneSelectedInTabbedPane(GemsChartFrame gemsChartFrame) {
        this._chartTableViews.removeElement(gemsChartFrame);
    }

    public void doneSelectedInTableView() {
        this._rowData.removeAllElements();
        this._rowData = null;
    }

    public void onData(Object object) {
        Hashtable hashtable = ReflectionUtils.getGetterMethodValues(object);
        Date date = new Date();
        for (int i = 0; i < this._chartTableViews.size(); ++i) {
            GemsChartFrame gemsChartFrame = (GemsChartFrame) this._chartTableViews.elementAt(i);
            gemsChartFrame.newDataAvailable(date, hashtable);
        }
    }
}

