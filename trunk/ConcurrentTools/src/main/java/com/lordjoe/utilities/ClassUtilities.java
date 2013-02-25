/**{ file
 @name ClassUtilities.java
 @function This class offers services related to Java Classes and class representations
 @author> Steven M. Lewis
 @copyright>
  ************************
  *  Copyright (c) 1996,97,98
  *  Steven M. Lewis
  *  www.LordJoe.com
 ************************
 @date> Mon Jun 22 21:48:24 PDT 1998
 @version> 1.0
 }*/
package com.lordjoe.utilities;


import java.io.*;
import java.util.*;
import java.lang.reflect.*;


/**
 * { class
 *
 * @name ClassUtilities
 * @function This class offers services related to Java Classes and class representations
 * }
 */
abstract public class ClassUtilities
{

    public static final Class THIS_CLASS = ClassUtilities.class;
    //- *******************
    //- Fields
    /**
     * { field
     *
     * @name gPrimativeTypes
     * @function names of java primative types
     * }
     */
    protected static final String gPrimativeTypes[] = {
            "int", "float", "boolean", "double", "byte", "char", "short", "long"};

// protected static final Class gPrimativeClasses[] = {
//     java.lang.int.class,java.lang.boolean.class,java.lang.double.class,
//     java.lang.byte.class,java.lang.char.class,java.lang.short.class,java.lang.long.class }
//  ;

    /**
     * { field
     *
     * @name gWrapperTypes
     * @function list of java wrapper classes corresponding to primative types
     * }
     */
    protected static final String gWrapperTypes[] = {
            "Integer", "Float", "Boolean", "Double", "Byte", "Character", "Integer", "Long"};

    /**
     * { field
     *
     * @name gWrapperClasses
     * @function list of java wrapper classes corresponding to primative types
     * }
     */
    protected static final Class gWrapperClasses[] = {
            Integer.class, Float.class, Boolean.class, Double.class,
            Byte.class, Character.class, Integer.class, Long.class};
    /**
     * { field
     *
     * @name gPrimitiveClasses
     * @function list of java wrapper classes corresponding to primative types
     * }
     */
    protected static final Class gPrimitiveClasses[] = {
            Integer.TYPE, Float.TYPE, Boolean.TYPE, Double.TYPE,
            Byte.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE};

    protected static Map gPrimitiveClassToWrapper;
    protected static Map gWrapperToPrimitiveClass;

    /**
     * { field
     *
     * @name gJavaPackageNames
     * @function list of java package names
     * }
     */
    protected static final String[] gJavaPackageNames = {
            "java.lang.", "java.awt.", "java.util.", "java.util.", "java.io.", "java.net.", "java.applet.", "java.awt.image.", "java.awt.event.", "java.awt.peer.", "java.awt.datatransfer.", "java.math.", "java.beans.", "java.security.", "java.text.", "java.rmi."};

    /**
     * { field
     *
     * @name gFullClassNames
     * @function list of full class names
     * @see Overlord.ClassUtilities#javaClassName
     *      }
     */
    protected static final  Hashtable gFullClassNames = new Hashtable();

    //- *******************
    //- Methods
// ************************************
// Public Functions
// ************************************

    /**
     * { method
     *
     * @param ClassName class to look for
     * @return the class - null if not found
     *         }
     * @name classForName
     * @function convert a class to a name without throwing an exception
     */
    public static Class classForName(String ClassName)
    {
        try {
            return (Class.forName(ClassName));
        }
        catch (ClassNotFoundException e) {
           //throw new RuntimeException(e);
        }
        return (null);
    }

    /**
     * { method
     *
     * @param ClassName class to look for
     * @return as above
     *         }
     * @name classForName
     * @function test if a class is loadable (also loads it)
     */
    public static boolean classExists(String ClassName)
    {
        return (classForName(ClassName) != null);
    }

    /**
     * { method
     *
     * @param ClassName class to look for
     * @return the class - null if not found
     *         }
     * @name classForType
     * @function convert a class to a name without throwing an exception -
     * can handle prinative types like int - returning java.lang.Integer
     */
    public static Class classForType(String ClassName)
    {
        return (classForName(objectClassName(ClassName)));
    }

    /**
     * { method
     *
     * @param ClassName class name of type name
     * @return the name int -> Ingeger other classes just return
     *         }
     * @name objectClassName
     * @function - given a primative or a class name return a class name
     */
    public static String objectClassName(String ClassName)
    {
        for (int i = 0; i < gPrimativeTypes.length; i++) {
            if (ClassName.equals(gPrimativeTypes[i])) {
                return ("java.lang." + gWrapperTypes[i]);
            }
        }
        return (ClassName);
    }

    /**
     * convert the input class to a wrapper class
     * returns in if the argument is not primitive
     *
     * @param in non-null class
     * @return non-null class guaranteed not to be primitive
     */
    public static String classNameToWrapperName(String ClassName)
    {
        for (int i = 0; i < gPrimativeTypes.length; i++) {
            if (ClassName.equals(gPrimativeTypes[i])) {
                return (gWrapperTypes[i]);
            }
        }
        return (ClassName);
    }

    /**
     * convert the input class to a wrapper class
     * returns in if the argument is not primitive
     *
     * @param in non-null class
     * @return non-null class guaranteed not to be primitive
     */
    public static Class primitiveToWrapper(Class in)
    {
        if (!in.isPrimitive())
            return (in);
        if (gPrimitiveClassToWrapper == null)
            buildWrapperMapping();
        return ((Class) gPrimitiveClassToWrapper.get(in));
    }

    /**
     * convert the input class from a wrapper class
     * to the corresponding primitive
     * returns in if the argument is a wrapper
     *
     * @param in non-null class
     * @return non-null class guaranteed not to be primitive
     */
    public static Class wrapperToPrimitive(Class in)
    {
        if (gPrimitiveClassToWrapper == null)
            buildWrapperMapping();
        Class ret = (Class) gWrapperToPrimitiveClass.get(in);
        if (ret != null)
            return (ret);
        return (in);
    }

    /**
     * build mappings primitive to Wrapper
     */
    protected static synchronized void buildWrapperMapping()
    {
        if (gPrimitiveClassToWrapper != null)
            return; // already done
        Map TheMap = new HashMap();
        Map TheMap2 = new HashMap();
        for (int i = 0; i < gPrimitiveClasses.length; i++) {
            TheMap.put(gPrimitiveClasses[i], gWrapperClasses[i]);
            TheMap2.put(gWrapperClasses[i], gPrimitiveClasses[i]);
        }
        gWrapperToPrimitiveClass = TheMap2;
        gPrimitiveClassToWrapper = TheMap;
    }

    /**
     * { method
     *
     * @param ShortName test name
     * @return full class name or null if not found
     *         }
     * @name javaClassName
     * @function given a java class name - i.e Rectangle search all java packages to
     * find the full name i.e. java.awt.Rectangle
     */
    public static String javaClassName(String ShortName)
    {
        Object answer = gFullClassNames.get(ShortName);
        if (answer != null) {
            return ((String) answer);
        }
        for (int k = 0; k < gJavaPackageNames.length; k++) {
            String ret = gJavaPackageNames[k] + ShortName;
            Class realClass = classForName(ret);
            if (realClass != null) {
                gFullClassNames.put(ShortName, ret);
                return (ret);
            }
        }
        return (ShortName);
        // give up
    }

    /**
     * method
     *
     * @param non-null o object to test
     * @return - non-null package name
     * @name packageName get name of the object's class' package
     */
    public static String packageName(Object o)
    {
        return (packageName(o.getClass()));
    }


    /**
     * method
     *
     * @param TestClass non-null o class to test
     * @return - non-null package name
     * @name packageName get name of the object's class' package
     */
    public static String packageName(Class TestClass)
    {
        Package ThePackage = TestClass.getPackage();
        if (ThePackage != null)
            return (ThePackage.getName());
        String FullName = TestClass.getName();
        return (packageName(FullName));
    }

    /**
     * get name of the  package from the full name
     *
     * @param FullName non-null full class name
     * @return - non-null package name
     */
    public static String packageName(String FullName)
    {
        int dotIndex = FullName.lastIndexOf(".");
        if (dotIndex != -1) {
            return (FullName.substring(0, dotIndex));
        }
        return (""); // no package
    }

    /**
     * get name of the  class from the full name
     *
     * @param FullName non-null full class name
     * @return - non-null class name
     */
    public static String shortClassName(String FullName)
    {
        int dotIndex = FullName.lastIndexOf(".");
        if (dotIndex == -1) {
            return (FullName); // no package
        }
        return (FullName.substring(dotIndex + 1));
    }

    /**
     * { method
     *
     * @param o object to test
     * @return class name
     *         }
     * @name shortClassName
     * @function get object s class then convert a full class name i.e. java.lang.Rectangle to the
     * short version i.e. Rectangle
     */
    public static String shortClassName(Object o)
    {
        return (shortClassName(o.getClass()));
    }

    /**
     * { method
     *
     * @param TestClass class
     * @return short name
     *         }
     * @name shortClassName
     * @function return the short name of the class - i.e
     * Rectangle for java.lang.Rectangle
     */
    public static String shortClassName(Class TestClass)
    {
        if (TestClass.isArray())
            return (shortClassName(TestClass.getComponentType()) + "[]");
        String FullName = TestClass.getName();
        int dotIndex = FullName.lastIndexOf(".");
        if (dotIndex != -1) {
            return (FullName.substring(dotIndex + 1));
        }
        return (FullName);
    }

    /**
     * { method
     *
     * @param ClassName name
     * @return true is primative
     *         }
     * @name isPrimitiveType
     * @function true if name is a primative type i.e. int, String
     */
    public static boolean isPrimitiveType(String ClassName)
    {
        for (int i = 0; i < gPrimativeTypes.length; i++) {
            if (ClassName.equals(gPrimativeTypes[i])) {
                return (true);
            }
        }
        return (false);
    }

    /**
     * { method
     *
     * @param TheType class name
     * @return the tag string
     *         }
     * @name typeToTag
     * @function convert a class name to tag for HTML documentation - primative
     * types return unaltered
     */
    public static String typeToTag(String TheType)
    {
        if (isPrimitiveType(TheType)) {
            return (TheType);
        }
        else {
            return ("<a href=\"" + TheType + ".html#_top_" + "\">" + TheType + "</a>");
        }
    }

    /**
     * { method
     *
     * @param test test object
     * @param c    test class
     * @return true if object is instance
     *         }
     * @name isInstance
     * @function test if an object is an instance of a class
     */
    public static boolean isInstance(Object test, Class c)
    {
        if (test != null) {
            return (c.isAssignableFrom(test.getClass()));
        }
        return (false);
    }

    /**
     * { method
     *
     * @param test Class to test
     * @param c    possible base class or interface
     * @return true if cast OK
     *         }
     * @name implementsClass
     * @function test is Class test can be cast as instance of class c
     */
    public static boolean implementsClass(Class test, Class c)
    {
        if (derivedClass(test, c)) {
            return (true);
        }
        return (implementsInterface(test, c));
    }

    /**
     * { method
     *
     * @param test             Class to test
     * @param possibleAncestor possible base
     * @return true if cast OK
     *         }
     * @name derivedClass
     * @function test is Class test can be cast as instance of class possibleAncestor
     */
    public static boolean derivedClass(Class test, Class possibleAncestor)
    {
        while (test != null) {
            if (test == possibleAncestor) {
                return (true);
            }
            test = test.getSuperclass();
        }
        return (false);
        // failure
    }

    /**
     * { method
     *
     * @param test              Class to test
     * @param possibleInterface possible interface
     * @return true if cast OK
     *         }
     * @name implementsInterface
     * @function test is Class test implements interface possibleInterface
     */
    public static boolean implementsInterface(Class test, Class possibleInterface)
    {
        Class interfaces[] = test.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            if (derivedClass(interfaces[i], possibleInterface)) {
                return (true);
            }
        }
        return (false);
        // failure
    }

    /**
     * { method
     *
     * @param test              Class to test
     * @param possibleInterface possible interface name
     * @return true if cast OK
     *         }
     * @name implementsInterface
     * @function test is Class test implements interface possibleInterface
     */
    public static boolean implementsInterface(Class test, String possibleInterface)
    {
        return (implementsInterface(test, classForName(possibleInterface)));
    }

    /**
     * { method
     *
     * @param items non-null array
     * @return - non-null common class
     *         }
     * @name lowestCommonClass
     * @function find lowes common class of an array
     */
    public static Class lowestCommonClass(Object[] items)
    {
        Class start = items.getClass().getComponentType();
        if (items.length == 0)
            return (start);
        // we are not smart about common interfaces
        if (start.isInterface())
            return (start);
        int i = 0;
        for (; i < items.length; i++) {
            if (items[i] != null) {
                start = items[i].getClass();
                i++;
                break;
            }
        }
        for (; i < items.length; i++) {
            if (items[i] != null)
                start = commonClass(start, items[i].getClass());
        }
        return (start);
    }

    /**
     * { method
     *
     * @param test1 first object - cannot be null
     * @param test2 second object object - cannot be null
     * @return - common class
     *         }
     * @name commonClass
     * @function find first common class of two objects
     */
    public static Class commonClass(Object test1, Object test2)
    {
        Class test1Class = test1.getClass();
        Class test2Class = test2.getClass();
        return (commonClass(test1Class, test2Class));
    }

    /**
     * { method
     *
     * @param test1 first Class - cannot be null
     * @param test2 second Class object - cannot be null
     * @return - common class
     *         }
     * @name commonClass
     * @function find first common class of two Classes
     */
    public static Class commonClass(Class test1, Class test2)
    {
        if (test1 == test2) {
            return (test1);
        }
        int level1 = derivedLevel(test1);
        int level2 = derivedLevel(test2);
        while (level1 > level2) {
            test1 = test1.getSuperclass();
            level1--;
        }
        while (level2 > level1) {
            test2 = test2.getSuperclass();
            level2--;
        }
        while (test1 != null) {
            if (test1 == test2) {
                return (test1);
            }
            test2 = test2.getSuperclass();
            test1 = test1.getSuperclass();
        }
        return (null);
        // no common ancestor - this should not fail
    }

    /**
     * { method
     *
     * @param test class to test - cannot be null
     * @return number of levels below Object
     *         }
     * @name derivedLevel
     * @function return number of levels below Object a class is derived
     */
    public static int derivedLevel(Class test)
    {
        int level = 0;
        while (test != null) {
            test = test.getSuperclass();
            level++;
        }
        return (level);
    }

    /**
     * { method
     *
     * @param m    <Add Comment Here>
     * @param type <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name isSetMethod
     * @function <Add Comment Here>
     */
    public static boolean isSetMethod(Method m, Class type)
    {
        if (!m.getName().startsWith("set")) {
            return (false);
        }
        if (m.getReturnType() != null) {
            return (false);
        }
        Class[] parameters = m.getParameterTypes();
        switch (parameters.length) {
            case 1:
                return (type == null || parameters[0] == type);
            case 2:
                if (type != null && parameters[0] != type) {
                    return (false);
                }
                // bad type
                return (parameters[1] == int.class);
            default:
                return (false);
        }
    }

    /**
     * { method
     *
     * @param m    <Add Comment Here>
     * @param type <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name isGetMethod
     * @function <Add Comment Here>
     */
    public static boolean isGetMethod(Method m, Class type)
    {
        if (!m.getName().startsWith("get") && !m.getName().startsWith("is")) {
            return (false);
        }
        if (m.getReturnType() == null) {
            return (false);
        }
        // no return
        if (type != null && m.getReturnType() != type) {
            return (false);
        }
        // wrong type
        Class[] parameters = m.getParameterTypes();
        switch (parameters.length) {
            case 0:
                return (true);
            case 1:
                return (parameters[0] == int.class);
            default:
                return (false);
        }
    }

    /**
     * { method
     *
     * @param c    <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findSetMethod
     * @function <Add Comment Here>
     */
    public static Method findSetMethod(Class c, String name)
    {
        // call spacific version with type null = dont care
        return (findSetMethod(c, name, null));
    }

    /**
     * { method
     *
     * @param c    <Add Comment Here>
     * @param type <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findSetMethod
     * @function <Add Comment Here>
     */
    public static Method findSetMethod(Class c, String name, Class type)
    {
        String MethodName = "set" + name;
        Method[] TheMethods;
        try {
            TheMethods = c.getMethods();
        }
        catch (SecurityException ex) {
            return (null);
            // fail
        }
        if (TheMethods == null) {
            return (null);
        }
        // fail
        // look for non-indexed get method
        for (int i = 0; i < TheMethods.length; i++) {
            if (TheMethods[i].getName().equals(MethodName)) {
                // look for 1 parameters - an int
                Class[] parameters = TheMethods[i].getParameterTypes();
                // look for 1 parameters
                if (parameters.length == 1 &&
                        (type == null || parameters[0] == type) &&
                        TheMethods[i].getReturnType() == Void.TYPE) {
                    return (TheMethods[i]);
                }
            }
        }
        // look for indexed get method
        for (int i = 0; i < TheMethods.length; i++) {
            if (TheMethods[i].getName().equals(MethodName)) {
                // look for 1 parameters - an int
                Class[] parameters = TheMethods[i].getParameterTypes();
                if (parameters.length == 2 && parameters[0] == int.class && (type == null || parameters[1] == type) && TheMethods[i].getReturnType() == null) {
                    return (TheMethods[i]);
                }
            }
        }
        return (null);
        // fail
    }

// Needed to fix a bug in the compiler when dealing with .class variables
// public static Class LScribbleObjectKuldge = Scribble.LScribbleObject.class;

    /**
     * { method
     *
     * @param c    <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findGetMethod
     * @function <Add Comment Here>
     */
    public static Method findGetMethod(Class c, String name)
    {
        // call spacific version with type null = dont care
        return (findGetMethod(c, name, null));
    }

    /**
     * { method
     *
     * @param c    <Add Comment Here>
     * @param type <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findGetMethod
     * @function <Add Comment Here>
     */
    public static Method findGetMethod(Class c, String name, Class type)
    {
        String MethodName = "get" + name;
        Method[] TheMethods;
        try {
            TheMethods = c.getMethods();
        }
        catch (SecurityException ex) {
            return (null);
            // fail
        }
        if (TheMethods == null) {
            return (null);
        }
        // fail
        // look for non-indexed get method
        for (int i = 0; i < TheMethods.length; i++) {
            if (TheMethods[i].getName().equals(MethodName)) {
                // look for 0 parameters
                if (TheMethods[i].getParameterTypes().length == 0 && TheMethods[i].getReturnType() != null && (type == null || TheMethods[i].getReturnType() == type)) {
                    return (TheMethods[i]);
                }
            }
        }
        // look for indexed get method
        for (int i = 0; i < TheMethods.length; i++) {
            if (TheMethods[i].getName().equals(MethodName)) {
                // look for 1 parameters - an int
                Class[] parameters = TheMethods[i].getParameterTypes();
                if (parameters.length == 1 && parameters[0] == int.class && TheMethods[i].getReturnType() != null && (type == null || TheMethods[i].getReturnType() == type)) {
                    return (TheMethods[i]);
                }
            }
        }
        // maybe there is an is method returning a boolean
        if (type == null) {
            return (findIsMethod(c, name));
        }
        return (null);
        // fail
    }

    /**
     * { method
     *
     * @param c    <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findIsMethod
     * @function <Add Comment Here>
     */
    public static Method findIsMethod(Class c, String name)
    {
        String MethodName = "is" + name;
        Method[] TheMethods;
        try {
            TheMethods = c.getMethods();
        }
        catch (SecurityException ex) {
            return (null);
            // fail
        }
        if (TheMethods == null) {
            return (null);
        }
        // fail
        // look for non-indexed get method
        for (int i = 0; i < TheMethods.length; i++) {
            if (TheMethods[i].getName().equals(MethodName)) {
                // look for 0 parameters
                if (TheMethods[i].getParameterTypes().length == 0 && TheMethods[i].getReturnType() == boolean.class) {
                    return (TheMethods[i]);
                }
            }
        }
        // look for indexed get method
        for (int i = 0; i < TheMethods.length; i++) {
            if (TheMethods[i].getName().equals(MethodName)) {
                // look for 1 parameters - an int
                Class[] parameters = TheMethods[i].getParameterTypes();
                if (parameters.length == 1 && parameters[0] == int.class && TheMethods[i].getReturnType() == boolean.class) {
                    return (TheMethods[i]);
                }
            }
        }
        return (null);
        // fail
    }

// Action methods take exactly one srting as the argument

    /**
     * { method
     *
     * @param o    <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findActionMethod
     * @function <Add Comment Here>
     */
    public static Method findActionMethod(Object o, String name)
    {
        return (findActionMethod(o.getClass(), name));
    }

    /**
     * { method
     *
     * @param c          <Add Comment Here>
     * @param MethodName <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findActionMethod
     * @function <Add Comment Here>
     */
    public static Method findActionMethod(Class c, String MethodName)
    {
        Method[] TheMethods;
        try {
            TheMethods = c.getMethods();
        }
        catch (SecurityException ex) {
            return (null);
            // fail
        }
        if (TheMethods == null) {
            return (null);
        }
        // fail
        // look for indexed get method
        for (int i = 0; i < TheMethods.length; i++) {
            if (TheMethods[i].getName().equals(MethodName)) {
                // look for 1 parameters - a String
                Class[] parameters = TheMethods[i].getParameterTypes();
                if (parameters.length == 1 && parameters[0] == java.lang.String.class) {
                    return (TheMethods[i]);
                }
            }
        }
        return (null);
        // fail
    }

// Action methods take exactly one srting as the argument

    /**
     * { method
     *
     * @param o    <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findUniqueMethod
     * @function <Add Comment Here>
     */
    public static Method findUniqueMethod(Object o, String name)
    {
        return (findUniqueMethod(o.getClass(), name));
    }

    /**
     * { method
     *
     * @param o    <Add Comment Here>
     * @param name <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findUniqueMethod
     * @function <Add Comment Here>
     */
    public static Method findSuitableMethod(Class c, String name, Object[] params)
    {
        Method ret = findUniqueMethod(c, name);
        if (ret != null)
            return (ret);
        Class[] callers = new Class[params.length];
        for (int i = 0; i < callers.length; i++) {
            if (params[i] != null)
                callers[i] = params[i].getClass();
            else
                callers[i] = Object.class;
        }
        try {
            ret = c.getMethod(name, callers);
            return (ret);
        }
        catch (NoSuchMethodException ex) {
            //LogUtilities.logError(THIS_CLASS, ex);
            // !!! more thing to do later
            return (null);
        }
    }

    /**
     * get a static field value
     *
     * @param c    non-null target class
     * @param name non-null name of an field
     * @return the value
     */
    public static Object getStaticField(Class c, String name)
    {
        try {
            Field f = c.getField(name);
            Object ret = f.get(null);
            return (ret);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Run a named static method on a class
     *
     * @param c    non-null target class
     * @param name non-null name of an existing uniqir method
     * @return
     */
    public static Object invokeUniqueMethod(Class c, String name)
    {
        return invokeUniqueMethod(c, name, Util.EMPTY_OBJECT_ARRAY);
    }

    /**
     * Run a named static method on a class
     *
     * @param c      non-null target class
     * @param name   non-null name of an existing uniqir method
     * @param params non-null array of parameters
     * @return
     */
    public static Object invokeUniqueMethod(Class c, String name, Object[] params)
    {
        Method met = findSuitableMethod(c, name, params);
        if (met == null)
            throw new IllegalArgumentException("Cannot find suitable method");
        try {
            Object ret = met.invoke(null, params);
            return (ret);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Run a named method on an object
     *
     * @param o      non-null target
     * @param name   non-null name of an existing uniqir method
     * @param params non-null array of parameters
     * @return
     */
    public static Object invokeUniqueMethod(Object o, String name, Object[] params)
    {
        Method met = findSuitableMethod(o.getClass(), name, params);
        if (met == null)
            throw new IllegalArgumentException("Cannot find suitable method");
        try {
            Object ret = met.invoke(o, params);
            return (ret);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isOverridden(String name, Class[] args, Class test1, Class test2)
    {
        try {
            Method used1 = null;
            Method used2 = null;
            try {
                used1 = test1.getMethod(name, args);
            }
            catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(
                        "Method '" + name + " not declared in class '" + test1.getName() + "'");
            }
            try {
                used2 = test2.getMethod(name, args);
            }
            catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(
                        "Method '" + name + " not declared in class '" + test2.getName() + "'");
            }
            return (!used1.equals(used2));
        }
        catch (SecurityException ex) {
            throw new IllegalStateException(
                    "The security manager does not allow access to non-public methods");
        }
    }

    public static boolean isOverridden(Method met, Class test)
    {
        try {
            Method used = test.getDeclaredMethod(met.getName(), met.getParameterTypes());
            return (!used.equals(met));
        }
        catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException(
                    "Method '" + met.getName() + " not declared in class '" + test.getName() + "'");
        }
        catch (SecurityException ex) {
            throw new IllegalStateException(
                    "The security manager does not allow access to non-public methods");
        }
    }

    /**
     * { method
     *
     * @param c          <Add Comment Here>
     * @param MethodName <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name findUniqueMethod
     * @function <Add Comment Here>
     */
    public static Method findUniqueMethod(Class c, String MethodName)
    {
        Method ret = null;
        Method[] TheMethods;
        try {
            TheMethods = c.getMethods();
        }
        catch (SecurityException ex) {
            return (ret);
            // fail
        }
        if (TheMethods == null) {
            return (ret);
        }
        // fail
        // look for named method
        for (int i = 0; i < TheMethods.length; i++) {
            String test = TheMethods[i].getName();
            if (test.equals(MethodName)) {
             //   Assertion.validate(ret == null);
                //test for uniqueness
                ret = TheMethods[i];
            }
        }
        return (ret);
        // fail
    }

//
// like newInstance but without having to worry about those
// nasty exceptions

    /**
     * { method
     *
     * @param TheClass <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name createInstance
     * @function <Add Comment Here>
     */
    public static Object createInstance(String TheClass)
    {
        Class RealClass = classForName(TheClass);
        //if(RealClass == null)
        //System.err.println("Failed to create class '" + TheClass + "'");
       // Assertion.validate(RealClass != null);
        return (createInstance(RealClass));
    }

//
// like newInstance but without having to worry about those
// nasty exceptions

    /**
     * { method
     *
     * @param TheClass <Add Comment Here>
     * @return <Add Comment Here>
     *         }
     * @name createInstance
     * @function <Add Comment Here>
     */
    public static Object createInstance(Class TheClass)
    {
        Object ret = null;
        try {
            // Try going through a NullConstructor if one exists
            Constructor NullConstructor = TheClass.getConstructor(Util.EMPTY_CLASS_ARRAY);
            if (NullConstructor != null)
                ret = NullConstructor.newInstance(Util.EMPTY_OBJECT_ARRAY);
            else
                ret = TheClass.newInstance();
        }
        catch (NoSuchMethodException ie) {
           throw new RuntimeException(ie);
            // catch this
        }
        catch (InvocationTargetException ie) {
            throw new RuntimeException(ie);
            // catch this
        }
        catch (InstantiationException ie) {
            throw new RuntimeException(ie);
            // catch this
        }
        catch (IllegalAccessException il) {
            // repeat in order to debug
            try {
                ret = TheClass.newInstance();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            // catch this
        }
        return (ret);
    }

    /**
     * { method
     *
     * @param v <Add Comment Here>
     * @param o <Add Comment Here>
     * @param s <Add Comment Here>
     *          }
     * @name accumulateClientMethod
     * @function <Add Comment Here>
     */
    public static void accumulateClientMethod(Vector v, String s, Object o)
    {
        Method m = ClassUtilities.findUniqueMethod(o, s);
       // Assertion.validate(m != null);
       // Assertion.validate(!v.contains(m));
        // not already found
        v.addElement(m);
    }

    /**
     * Fetch the value of a named field
     *
     * @param FieldName non-null name of a field which may or may not exist
     * @param source    non-null source object
     * @return possibly null field value NOTE null is returned if
     *         the field does not exist, cannot be accessed or is null
     */
    public static Object getFieldValue(String FieldName, Object source)
    {
        Class TheClass = source.getClass();
        Field Test = null;
        try {
            Test = TheClass.getField(FieldName);
        }
        catch (NoSuchFieldException ex) {
            return (null);
        }
        try {
            return (Test.get(source));
        }
        catch (IllegalAccessException ex) {
            return (null);
        }
        catch (IllegalArgumentException ex) {
            return (null);
        }
    }

    /**
     * Fetch the value of a named static field
     *
     * @param FieldName non-null name of a field which may or may not exist
     * @param source    non-null source class
     * @return possibly null field value NOTE null is returned if
     *         the field does not exist, cannot be accessed or is null
     */
    public static Object getFieldValue(String FieldName, Class TheClass)
    {
        Field Test = null;
        try {
            Test = TheClass.getField(FieldName);
        }
        catch (NoSuchFieldException ex) {
            return (null);
        }
        if ((Test.getModifiers() & Modifier.STATIC) == 0)
            return (null); // not static
        try {
            return (Test.get(null));
        }
        catch (IllegalAccessException ex) {
            return (null);
        }
        catch (IllegalArgumentException ex) {
            return (null);
        }
    }

    public static boolean isStatic(Member fld)
    {
        return Modifier.isStatic(fld.getModifiers());
    }

    public static boolean isPublic(Member fld)
    {
        return Modifier.isPublic(fld.getModifiers());
    }

    public static boolean isFinal(Member fld)
    {
        return Modifier.isFinal(fld.getModifiers());
    }


    public static void validateClassPath()
    {
        String sep = System.getProperty("path.separator");
        String[] classpath =  System.getProperty("java.class.path").split(sep);
        for (int i = 0; i < classpath.length; i++) {
            String s = classpath[i];
            validateClassPathElement(  s);
        }
    }

    public static void validateClassPathElement(String s)
    {
         File f = new File(s);
        System.out.println("Looking for " + f.getAbsolutePath() );
         if(!f.exists())  {
             System.out.println("==>ClassPath element " + f.getAbsolutePath() + " does not exist");
         }
    }



//- *******************
//- End Class ClassUtilities
}
