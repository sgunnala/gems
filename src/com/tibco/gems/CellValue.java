/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

public class CellValue
        implements Comparable {
    public String m_cellValue;
    public long m_value = -99;
    public Long m_warnLimit = null;
    public Long m_errorLimit = null;

    public CellValue(String string, long l, Long l2, Long l3) {
        this.m_cellValue = string;
        this.m_value = l;
        this.m_warnLimit = l2;
        this.m_errorLimit = l3;
    }

    public CellValue(String string, int n, Long l, Long l2) {
        this.m_cellValue = string;
        this.m_value = n;
        this.m_warnLimit = l;
        this.m_errorLimit = l2;
    }

    public CellValue(char[] arrc) {
        this.m_cellValue = new String(arrc);
    }

    public CellValue(String string) {
        this.m_cellValue = string;
    }

    public CellValue(String string, int n) {
        this.m_cellValue = string;
        this.m_value = n;
    }

    public CellValue(String string, long l) {
        this.m_cellValue = string;
        this.m_value = l;
    }

    public String toString() {
        return this.m_cellValue;
    }

    public int compareTo(Object object) throws ClassCastException {
        CellValue cellValue = (CellValue) object;
        if (this.m_value > cellValue.m_value) {
            return 1;
        }
        if (this.m_value == cellValue.m_value) {
            return 0;
        }
        return -1;
    }
}

