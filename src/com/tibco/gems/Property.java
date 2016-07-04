/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.lang.reflect.Method;

public class Property {
    private String name;
    private Method setter;
    private Method getter;
    private boolean readable;
    private boolean writeable;
    private String classType;

    public Property() {
    }

    public Property(String name, String classType, Method setter, Method getter) {
        this.name = name;
        this.classType = classType;
        this.setter = setter;
        this.getter = getter;
        this.writeable = setter != null;
        this.readable = getter != null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public Method getSetter() {
        return this.setter;
    }

    public void setSetter(Method method) {
        this.setter = method;
        if (method != null) {
            this.writeable = true;
        }
    }

    public Method getGetter() {
        return this.getter;
    }

    public void setGetter(Method method) {
        this.getter = method;
        if (method != null) {
            this.readable = true;
        }
    }

    public boolean isReadable() {
        return this.readable;
    }

    public void setReadable(boolean bl) {
        this.readable = bl;
    }

    public boolean isWriteable() {
        return this.writeable;
    }

    public void setWriteable(boolean bl) {
        this.writeable = bl;
    }

    public String getClassType() {
        return this.classType;
    }

    public void setClassType(String string) {
        this.classType = string;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Property(").append(this.name).append(": ");
        stringBuffer.append(this.classType).append(") -> ");
        if (this.readable) {
            stringBuffer.append("getter: ").append(this.getter.getName());
        }
        if (this.writeable) {
            stringBuffer.append(", setter: ").append(this.setter.getName());
        }
        return stringBuffer.toString();
    }
}

