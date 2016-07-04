/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.text.MessageFormat;
import java.text.NumberFormat;

public abstract class StringUtilities {
    public static String getHumanReadableSize(long l) {
        long l2 = (long) Math.pow(2.0, 20.0);
        long l3 = (long) Math.pow(2.0, 10.0);
        long l4 = (long) Math.pow(2.0, 30.0);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(1);
        double d = 0.0;
        long l5 = Math.abs(l);
        String string = "";
        if (l5 / l4 >= 1) {
            d = (double) l5 / (double) l4;
            string = "GB";
        } else if (l5 / l2 >= 1) {
            d = (double) l5 / (double) l2;
            string = "MB";
        } else if (l5 / l3 >= 1) {
            d = (double) l5 / (double) l3;
            string = "KB";
        } else {
            d = l5;
            string = "b";
        }
        return numberFormat.format((double) (l < 0 ? -1 : 1) * d) + string;
    }

    public static String dumpBytes(byte[] arrby) {
        StringBuffer stringBuffer = new StringBuffer(arrby.length);
        for (int i = 0; i < arrby.length; ++i) {
            String string = Integer.toHexString(256 + (arrby[i] & 255)).substring(1);
            stringBuffer.append((string.length() < 2 ? "0" : "") + string);
        }
        return stringBuffer.toString();
    }

    public static String arrayToString(Object[] arrobject) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; arrobject != null && i < arrobject.length; ++i) {
            stringBuffer.append(arrobject[i].toString());
            if (!(arrobject[i] instanceof String) || i + 1 >= arrobject.length) continue;
            stringBuffer.append(", ");
        }
        return stringBuffer.toString();
    }

    public static String getFullHumanReadableTime(long l) {
        long l2 = 1000;
        long l3 = 60 * l2;
        long l4 = 60 * l3;
        long l5 = 24 * l4;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(0);
        StringBuffer stringBuffer = new StringBuffer("");
        double d = 0.0;
        boolean bl = true;
        boolean bl2 = false;
        long l6 = Math.abs(l);
        String string = null;
        while (l6 >= 1) {
            stringBuffer.append(" ");
            if (l6 / l5 >= 1) {
                bl2 = false;
                d = (double) l6 / (double) l5;
                l6 -= l5 * (long) d;
                bl = false;
                string = "Days";
            } else if (l6 / l4 >= 1) {
                bl2 = false;
                d = (double) l6 / (double) l4;
                string = "Hours";
                l6 -= l4 * (long) d;
            } else if (l6 / l3 >= 1) {
                bl2 = false;
                d = (double) l6 / (double) l3;
                string = "Minutes";
                l6 -= l3 * (long) d;
            } else if (l6 / l2 >= 1) {
                bl2 = false;
                d = (double) l6 / (double) l2;
                string = "Seconds";
                l6 -= l2 * (long) d;
                if (!bl) {
                    continue;
                }
            } else {
                if (bl2) {
                    d = l6;
                    string = "Millisconds";
                }
                l6 -= l6;
                if (!bl2) continue;
            }
            Object[] arrobject = new Object[]{numberFormat.format(Math.floor(d)), string};
            stringBuffer.append(MessageFormat.format("{0} {1}", arrobject));
        }
        return stringBuffer.toString().trim();
    }

    public static String stripSpaces(String string) {
        return string.replaceAll("\\s+", "");
    }
}

