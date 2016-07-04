/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

public class MsgCellValue
        implements Comparable {
    public long m_cellValue;
    public long m_warnLimit = 1;
    public long m_errorLimit = 0;

    public MsgCellValue(long l, long l2, long l3) {
        this.m_cellValue = l;
        this.m_warnLimit = l2;
        this.m_errorLimit = l3;
    }

    public MsgCellValue(long l, long l2) {
        this.m_cellValue = l;
        this.m_errorLimit = l2;
    }

    public MsgCellValue(long l) {
        this.m_cellValue = l;
    }

    public String toString() {
        return String.valueOf(this.m_cellValue);
    }

    public int compareTo(Object object) throws ClassCastException {
        MsgCellValue msgCellValue = (MsgCellValue) object;
        if (this.m_cellValue > msgCellValue.m_cellValue) {
            return 1;
        }
        if (this.m_cellValue == msgCellValue.m_cellValue) {
            return 0;
        }
        return -1;
    }
}

