/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class ReflectionUtils {
    private static Set primitiveTypes = new HashSet();
    private static Set excludedProperties;
    private static Map classProperties;

    public static boolean isPrimitiveType(String string) {
        return primitiveTypes.contains(string);
    }

    public static boolean isCollection(Class class_) throws RXMLException {
        return ReflectionUtils.containsInterface(class_, "java.util.Collection") || ReflectionUtils.isSet(class_) || ReflectionUtils.isList(class_);
    }

    public static boolean containsInterface(Class class_, String string) throws RXMLException {
        try {
            if (class_.getName().equals(string)) {
                return true;
            }
            Class<?>[] arrclass = class_.getInterfaces();
            for (int i = 0; i < arrclass.length; ++i) {
                String string2 = arrclass[i].getName();
                if (!string2.equals(string)) continue;
                return true;
            }
            return false;
        } catch (Exception var2_3) {
            System.err.println(var2_3.getClass() + ": " + var2_3.getMessage());
            throw new RXMLException(var2_3);
        }
    }

    public static boolean isSet(Class class_) throws RXMLException {
        return ReflectionUtils.containsInterface(class_, "java.util.Set");
    }

    public static boolean isList(Class class_) throws RXMLException {
        return ReflectionUtils.containsInterface(class_, "java.util.List");
    }

    public static boolean isMap(Class class_) throws RXMLException {
        return ReflectionUtils.containsInterface(class_, "java.util.Map");
    }

    public static Map getClassProperties(String string) {
        try {
            Class class_ = Class.forName(string);
            return ReflectionUtils.getClassProperties(class_);
        } catch (ClassNotFoundException var1_2) {
            System.err.println(var1_2.getClass() + ": " + var1_2.getMessage());
            return null;
        }
    }

    public static Map<Object, Property> getClassProperties(Class class_) {
        String string = class_.getName();
        if (classProperties.containsKey(string)) {
            return (Map) classProperties.get(string);
        }
        TreeMap<Object, Property> treeMap = new TreeMap<>();
        Method[] arrmethod = class_.getMethods();
        for (int i = 0; i < arrmethod.length; ++i) {
            Property property;
            Object object;

            String string2 = arrmethod[i].getName();
            if (string2.startsWith("get") || string2.startsWith("is")) {
                Class<?>[] arrclass2 = arrmethod[i].getParameterTypes();
                if (arrclass2.length > 0 || Modifier.isTransient(arrmethod[i].getModifiers())) continue;
                String name = string2.startsWith("get") ? "" + Character.toLowerCase(string2.charAt(3)) + string2.substring(4) : "" + Character.toLowerCase(string2.charAt(2)) + string2.substring(3);
                if (treeMap.containsKey(name)) {
                    Property property1 = treeMap.get(name);
                    property1.setGetter(arrmethod[i]);
                    continue;
                }

                object = arrmethod[i].getReturnType().getName();
                if (excludedProperties.contains(name)) continue;
                property = new Property(name, (String) object, null, arrmethod[i]);
                treeMap.put(name, property);
                continue;
            }

            if (!string2.startsWith("set")) continue;
            String name = "" + Character.toLowerCase(string2.charAt(3)) + string2.substring(4);
            String string3 = arrmethod[i].getReturnType().getName();
            Class<?>[] arrclass;
            if (!string3.equals("void") || (arrclass = arrmethod[i].getParameterTypes()).length != 1) continue;
            if (treeMap.containsKey(name)) {
                Property property1 = treeMap.get(name);
                property1.setSetter(arrmethod[i]);
                continue;
            }
            object = arrclass[0].getName();
            property = new Property(name, (String) object, arrmethod[i], null);
            treeMap.put(name, property);
        }

        classProperties.put(string, treeMap);
        return treeMap;
    }

    public static Object getPropertyValue(Object object, String string) {
        Property property = ReflectionUtils.getProperty(object.getClass(), string);
        if (property.isReadable()) {
            try {
                return property.getGetter().invoke(object, new Object[0]);
            } catch (IllegalAccessException var3_3) {
                System.err.println(var3_3.getClass() + ": " + var3_3.getMessage());
            } catch (InvocationTargetException var3_4) {
                System.err.println(var3_4.getClass() + ": " + var3_4.getMessage());
            }
        }
        return null;
    }

    public static Property getProperty(Class class_, String string) {
        Map map = ReflectionUtils.getClassProperties(class_);
        if (!map.containsKey(string)) {
            return null;
        }
        return (Property) map.get(string);
    }

    public static Method getSetterMethod(String string, String string2) {
        Class class_ = ReflectionUtils.getClassFromName(string);
        if (class_ == null) {
            return null;
        }
        return ReflectionUtils.getSetterMethod(class_, string2);
    }

    public static Class getClassFromName(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException var1_1) {
            System.err.println(var1_1.getClass() + ": " + var1_1.getMessage());
            return null;
        }
    }

    public static Method getSetterMethod(Class class_, String string) {
        Property property = ReflectionUtils.getProperty(class_, string);
        if (property == null) {
            return null;
        }
        return property.getSetter();
    }

    public static Method getGetterMethod(String string, String string2) {
        Class class_ = ReflectionUtils.getClassFromName(string);
        if (class_ == null) {
            return null;
        }
        return ReflectionUtils.getGetterMethod(class_, string2);
    }

    public static Method getGetterMethod(Class class_, String string) {
        Property property = ReflectionUtils.getProperty(class_, string);
        if (property == null) {
            return null;
        }
        return property.getGetter();
    }

    public static Annotation[] getSetterAnnotations(String string, String string2) {
        Class class_ = ReflectionUtils.getClassFromName(string);
        if (class_ == null) {
            return null;
        }
        return ReflectionUtils.getSetterAnnotations(class_, string2);
    }

    public static Annotation[] getSetterAnnotations(Class class_, String string) {
        Method method = ReflectionUtils.getSetterMethod(class_, string);
        if (method == null) {
            return null;
        }
        return method.getAnnotations();
    }

    public static Object getSetterAnnotationPropertyValue(String string, String string2, String string3, String string4) {
        try {
            Class class_ = Class.forName(string);
            return ReflectionUtils.getSetterAnnotationPropertyValue(class_, string2, string3, string4);
        } catch (Exception var4_5) {
            System.err.println(var4_5.getClass() + ": " + var4_5.getMessage());
            return null;
        }
    }

    public static Object getSetterAnnotationPropertyValue(Class class_, String string, String string2, String string3) {
        Annotation[] arrannotation = ReflectionUtils.getSetterAnnotations(class_, string);
        for (int i = 0; i < arrannotation.length; ++i) {
            if (!arrannotation[i].annotationType().getName().equals(string2)) continue;
            return ReflectionUtils.getAnnotationPropertyValue(arrannotation[i], string3);
        }
        return null;
    }

    public static Object getAnnotationPropertyValue(Annotation annotation, String string) {
        try {
            Method[] arrmethod = annotation.annotationType().getMethods();
            for (int i = 0; i < arrmethod.length; ++i) {
                if (!arrmethod[i].getName().equals(string)) continue;
                return arrmethod[i].invoke(annotation, new Object[0]);
            }
        } catch (Exception var2_3) {
            System.err.println(var2_3.getClass() + ": " + var2_3.getMessage());
        }
        return null;
    }

    public static Annotation[] getPropertyAnnotations(String string, String string2) {
        return ReflectionUtils.getGetterAnnotations(string, string2);
    }

    public static Annotation[] getGetterAnnotations(String string, String string2) {
        Class class_ = ReflectionUtils.getClassFromName(string);
        if (class_ == null) {
            return null;
        }
        return ReflectionUtils.getGetterAnnotations(class_, string2);
    }

    public static Annotation[] getGetterAnnotations(Class class_, String string) {
        Method method = ReflectionUtils.getGetterMethod(class_, string);
        if (method == null) {
            return null;
        }
        return method.getDeclaredAnnotations();
    }

    public static Annotation[] getPropertyAnnotations(Class class_, String string) {
        return ReflectionUtils.getGetterAnnotations(class_, string);
    }

    public static Map getReadableProperties(String string) {
        try {
            Class class_ = Class.forName(string);
            return ReflectionUtils.getReadableProperties(class_);
        } catch (ClassNotFoundException var1_2) {
            System.err.println(var1_2.getClass() + ": " + var1_2.getMessage());
            return null;
        }
    }

    public static Map<String, String> getReadableProperties(Class class_) {
        Map<Object, Property> map = ReflectionUtils.getClassProperties(class_);
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for (Object string : map.keySet()) {
            Property property = map.get(string);
            if (!property.isReadable()) continue;
            treeMap.put((String) string, property.getClassType());
        }
        return treeMap;
    }

    public static Vector getGetterMethodNames(Class class_) {
        Vector<String> vector = new Vector<String>();
        Method[] arrmethod = class_.getMethods();
        for (int i = 0; i < arrmethod.length; ++i) {
            Class<?>[] arrclass;
            int n;
            String string = arrmethod[i].getName();
            if (!string.startsWith("get") || (arrclass = arrmethod[i].getParameterTypes()).length > 0 || Modifier.isTransient(n = arrmethod[i].getModifiers()) || !arrmethod[i].getReturnType().getName().equals("long") && !arrmethod[i].getReturnType().getName().equals("int") || !string.contains("MsgMem") && !string.contains("Size") && !string.contains("Count") && !string.contains("Rate"))
                continue;
            String string2 = Character.isLowerCase(string.charAt(4)) ? "" + Character.toLowerCase(string.charAt(3)) + string.substring(4) : string.substring(3);
            vector.add(string2);
        }
        return vector;
    }

    public static Hashtable getGetterMethodValues(Object object) {
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        Class class_ = object.getClass();
        Method[] arrmethod = class_.getMethods();
        for (int i = 0; i < arrmethod.length; ++i) {
            int n;
            Class<?>[] arrclass;
            String string = arrmethod[i].getName();
            if (!string.startsWith("get") || (arrclass = arrmethod[i].getParameterTypes()).length > 0 || Modifier.isTransient(n = arrmethod[i].getModifiers()))
                continue;
            try {
                if (!arrmethod[i].getReturnType().getName().equals("long") && !arrmethod[i].getReturnType().getName().equals("int") || !string.contains("MsgMem") && !string.contains("Size") && !string.contains("Count") && !string.contains("Rate"))
                    continue;
                String string2 = Character.isLowerCase(string.charAt(4)) ? "" + Character.toLowerCase(string.charAt(3)) + string.substring(4) : string.substring(3);
                Object object2 = arrmethod[i].invoke(object, null);
                hashtable.put(string2, object2);
                continue;
            } catch (Throwable var8_9) {
                System.err.println(var8_9.getClass() + ": " + var8_9.getMessage());
            }
        }
        return hashtable;
    }

    public static Map getWriteableProperties(String string) {
        try {
            Class class_ = Class.forName(string);
            return ReflectionUtils.getWriteableProperties(class_);
        } catch (ClassNotFoundException var1_2) {
            System.err.println(var1_2.getClass() + ": " + var1_2.getMessage());
            return null;
        }
    }

    public static Map<String, String> getWriteableProperties(Class class_) {
        Map map = ReflectionUtils.getClassProperties(class_);
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for (Object string : map.keySet()) {
            Property property = (Property) map.get(string);
            if (!property.isWriteable()) continue;
            treeMap.put((String) string, property.getClassType());
        }
        return treeMap;
    }

    public static Object buildObject(String string, Map map) {
        try {
            Class class_ = Class.forName(string);
            Object obj = class_.newInstance();
            Method[] arrmethod = class_.getMethods();
            for (int i = 0; i < arrmethod.length; ++i) {
                Class<?>[] arrclass;
                String string2;
                String string3;
                String string4 = arrmethod[i].getName();
                if (!string4.startsWith("set")) continue;
                String string5 = "" + Character.toLowerCase(string4.charAt(3)) + string4.substring(4);
                String string6 = arrmethod[i].getReturnType().getName();
                if (!string6.equals("void") || (arrclass = arrmethod[i].getParameterTypes()).length != 1 || !map.containsKey(string5) || !primitiveTypes.contains(string2 = arrclass[0].getName()) || (string3 = (String) map.get(string5)) == null || string3.length() <= 0)
                    continue;
                Object[] arrobject = new Object[]{ReflectionUtils.convertStringToObjectWrapper(string2, string3)};
                arrmethod[i].invoke(obj, arrobject);
            }
            return obj;
        } catch (Exception var2_3) {
            System.err.println(var2_3.getClass() + ": " + var2_3.getMessage());
            return null;
        }
    }

    private static Object convertStringToObjectWrapper(String string, String string2) {
        Object object = null;
        if (string.equals("java.lang.Integer") || string.equals("int")) {
            object = new Integer(string2);
        } else if (string.equals("java.lang.Long") || string.equals("long")) {
            object = new Long(string2);
        } else if (string.equals("java.lang.Short") || string.equals("short")) {
            object = new Short(string2);
        } else if (string.equals("java.lang.Byte") || string.equals("byte")) {
            object = new Byte(string2);
        } else if (string.equals("java.lang.Float") || string.equals("float")) {
            object = new Float(string2);
        } else if (string.equals("java.lang.Double") || string.equals("double")) {
            object = new Double(string2);
        } else if (string.equals("java.lang.String")) {
            object = string2;
        } else if (string.equals("java.lang.Boolean") || string.equals("boolean")) {
            object = new Boolean(string2);
        }
        return object;
    }

    static {
        primitiveTypes.add("byte");
        primitiveTypes.add("short");
        primitiveTypes.add("int");
        primitiveTypes.add("long");
        primitiveTypes.add("float");
        primitiveTypes.add("double");
        primitiveTypes.add("boolean");
        primitiveTypes.add("java.lang.String");
        primitiveTypes.add("java.lang.Byte");
        primitiveTypes.add("java.lang.Short");
        primitiveTypes.add("java.lang.Integer");
        primitiveTypes.add("java.lang.Long");
        primitiveTypes.add("java.lang.Float");
        primitiveTypes.add("java.lang.Double");
        primitiveTypes.add("java.lang.Boolean");
        excludedProperties = new HashSet();
        excludedProperties.add("class");
        classProperties = new TreeMap();
    }
}

