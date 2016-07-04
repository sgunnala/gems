/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.lang.reflect.Method;

public class GemsProperty {
    protected String name;
    protected Class type;
    protected String help;

    public GemsProperty(String string, Class class_) {
        this.name = string;
        this.type = class_;
        this.help = "<html> <br> <br> </html>";
    }

    public GemsProperty(String string, Class class_, String string2) {
        this.name = string;
        this.type = class_;
        this.help = string2;
    }

    public void setValue(Object object, String string) {
        try {
            Class class_ = Class.forName(object.getClass().getName());
            Class[] arrclass = new Class[]{this.type};
            Method method = class_.getMethod(this.getSetMethod(), arrclass);
            Object[] arrobject = new Object[1];
            if (this.type.isAssignableFrom(Long.TYPE)) {
                arrobject[0] = new Long(string);
            } else if (this.type.isAssignableFrom(Boolean.TYPE)) {
                arrobject[0] = new Boolean(string);
            } else if (this.type.isAssignableFrom(Class.forName("java.lang.String"))) {
                arrobject[0] = string;
            } else if (this.type.isAssignableFrom(Integer.TYPE)) {
                arrobject[0] = new Integer(string);
            } else if (this.type.isAssignableFrom(Byte.TYPE)) {
                arrobject[0] = new Byte(string);
            } else {
                System.err.println("GemsProperty type not supported: " + this.type.getName());
            }
            method.invoke(object, arrobject);
        } catch (Throwable var3_4) {
            System.err.println(var3_4);
        }
    }

    public String getSetMethod() {
        return "set" + this.name;
    }

    public String getValue(Object object) {
        try {
            Class class_ = Class.forName(object.getClass().getName());
            Method method = class_.getMethod(this.getGetMethod(), null);
            Object object2 = method.invoke(object, null);
            return object2 == null ? "" : object2.toString();
        } catch (Throwable var2_3) {
            System.err.println(var2_3);
            return new String();
        }
    }

    public String getGetMethod() {
        if (this.type.isAssignableFrom(Boolean.TYPE)) {
            return "is" + this.name;
        }
        return "get" + this.name;
    }

    public void setBoolean(Object object, boolean bl) {
        try {
            Class class_ = Class.forName(object.getClass().getName());
            Class[] arrclass = new Class[]{this.type};
            Method method = class_.getMethod(this.getSetMethod(), arrclass);
            Object[] arrobject = new Object[]{new Boolean(bl)};
            method.invoke(object, arrobject);
        } catch (Throwable var3_4) {
            System.err.println(var3_4);
        }
    }

    public void setLong(Object object, long l) {
        try {
            Class class_ = Class.forName(object.getClass().getName());
            Class[] arrclass = new Class[]{this.type};
            Method method = class_.getMethod(this.getSetMethod(), arrclass);
            Object[] arrobject = new Object[]{new Long(l)};
            method.invoke(object, arrobject);
        } catch (Throwable var4_4) {
            System.err.println(var4_4);
        }
    }

    public void setString(Object object, String string) {
        try {
            Class class_ = Class.forName(object.getClass().getName());
            Class[] arrclass = new Class[]{this.type};
            Method method = class_.getMethod(this.getSetMethod(), arrclass);
            Object[] arrobject = new Object[]{new String(string)};
            method.invoke(object, arrobject);
        } catch (Throwable var3_4) {
            System.err.println(var3_4);
        }
    }

    public String getHelpText() {
        return this.help;
    }
}

