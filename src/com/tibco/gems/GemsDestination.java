/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

public class GemsDestination {
    public String m_destName;
    public DEST_TYPE m_destType;

    public GemsDestination(String string, DEST_TYPE dEST_TYPE) {
        this.m_destName = string;
        this.m_destType = dEST_TYPE;
    }

    public String toString() {
        return "[" + (Object) ((Object) this.m_destType) + "]" + this.m_destName;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum DEST_TYPE {
        Queue,
        Topic;


        private DEST_TYPE() {
        }
    }

}

