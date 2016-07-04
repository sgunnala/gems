/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  com.tibco.tibjms.admin.ConnectionFactoryInfo
 */
package com.tibco.gems;

import com.tibco.tibjms.admin.ConnectionFactoryInfo;

public class GemsDummyConnectionFactoryInfo
        extends ConnectionFactoryInfo {
    String m_factoryName = "";

    public GemsDummyConnectionFactoryInfo() {
        super("", null, 0, null);
    }

    public String getJndiName() {
        return this.m_factoryName;
    }

    public void setJndiName(String string) {
        this.m_factoryName = string;
    }
}

