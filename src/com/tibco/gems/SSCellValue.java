/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.awt.*;

public class SSCellValue
        implements Comparable {
    public String m_cellValue;
    public long m_cellValueLong;
    public String m_cellType;
    public String m_errorValue;
    public long m_warnLimit = 10;
    public long m_errorLimit = 100;
    String m_datatype = "S";

    public SSCellValue(long l, String string, long l2, long l3) {
        this.m_cellValueLong = l;
        this.m_datatype = "L";
        this.m_cellValue = String.valueOf(this.m_cellValueLong);
        this.m_cellType = string;
        this.m_warnLimit = l2;
        this.m_errorLimit = l3;
    }

    public SSCellValue(long l, String string, long l2) {
        this.m_cellValueLong = l;
        this.m_datatype = "L";
        this.m_cellValue = String.valueOf(this.m_cellValueLong);
        this.m_cellType = string;
        this.m_errorLimit = l2;
        this.m_warnLimit = l2;
    }

    public SSCellValue(String string, String string2, String string3) {
        this.m_cellValue = string;
        this.m_datatype = "S";
        this.m_cellType = string2;
        this.m_errorValue = string3;
    }

    public SSCellValue(String string, String string2) {
        this.m_cellValue = string;
        this.m_cellType = string2;
    }

    public SSCellValue(String string) {
        this.m_cellValue = string;
        this.m_cellType = "";
    }

    public String toString() {
        return String.valueOf(this.m_cellValue);
    }

    public int compareTo(Object object) throws ClassCastException {
        SSCellValue sSCellValue = (SSCellValue) object;
        if (!this.m_cellValue.equals(sSCellValue.m_cellValue)) {
            return 1;
        }
        if (this.m_cellValue == sSCellValue.m_cellValue) {
            return 0;
        }
        return -1;
    }

    public Color CellColour() {
        if (this.m_cellType.equals("Head")) {
            return Color.lightGray;
        }
        if (this.m_cellType.equals("Error") && this.m_datatype.equals("L")) {
            if (this.m_cellValueLong >= this.m_errorLimit) {
                return Color.red;
            }
            if (this.m_cellValueLong >= this.m_warnLimit) {
                return Color.yellow;
            }
            return Color.green;
        }
        if (this.m_cellType.equals("Large") && this.m_datatype.equals("L")) {
            if (this.m_cellValueLong >= this.m_errorLimit) {
                return Color.yellow;
            }
            if (this.m_cellValueLong >= this.m_warnLimit) {
                return Color.cyan;
            }
        }
        if (this.m_cellType.equals("Status") && this.m_datatype.equals("S")) {
            if (this.m_cellValue.equals(this.m_errorValue)) {
                return Color.red;
            }
            return Color.green;
        }
        return Color.white;
    }
}

