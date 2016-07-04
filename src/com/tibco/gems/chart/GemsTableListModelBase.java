/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems.chart;

import java.util.Vector;

public interface GemsTableListModelBase {
    public int getColumnCount();

    public int getRowCount();

    public Vector getDataVector();

    public Object getValueAt(int var1, int var2);

    public Class getColumnClass(int var1);

    public int[] getColumnSizes();

    public String[] getColumnHeadings();
}

