package com.lordjoe.lib.xml;

import com.lordjoe.utilities.*;
import org.systemsbiology.common.*;

import java.lang.reflect.*;
import java.text.*;
import java.util.*;


/**
 * Help class with static methods to set and get properties by name and
 * access any exposed collections in an object
 *
 * @author Steve Lewis
 * @since july 99
 */

public abstract class ClassAnalyzer
{
    private static ILogger gLogger;

    public static ILogger getLogger() {
        return gLogger;
    }

    public static void setLogger(ILogger pLogger) {
        gLogger = pLogger;
    }

    protected static final Map<Class, IObjectConverter> gConverters =
            new HashMap<Class, IObjectConverter>();

    public static void registerConverter(Class cls, IObjectConverter conv)
     {
         gConverters.put(cls,conv);
     }

    public static IObjectConverter getRegisterConverter(Class cls)
     {
         IObjectConverter objectConverter = gConverters.get(cls);
         if(cls.getName().contains("OraclePropertyType"))
            CommonUtilities.breakHere();
         return objectConverter;
     }

    protected static IObjectConverter gConverter;

    public static void setObjectConverter(IObjectConverter aConverter)
    {
        gConverter = aConverter;
    }

    private static final Map<String, IAttributeBuilder> gAttributeBuilders =
            new HashMap<String, IAttributeBuilder>();

    public static void registerBuilder(String s, IAttributeBuilder b)
    {
        gAttributeBuilders.put(s, b);
    }

    private static String gMemberVariablePrefix = "";
    private static String gStaticMemberVariablePrefix = "";

    public static String getMemberVariablePrefix()
    {
        return gMemberVariablePrefix;
    }

    public static void setMemberVariablePrefix(String pMemberVariablePrefix)
    {
        gMemberVariablePrefix = pMemberVariablePrefix;
    }

    public static String getStaticMemberVariablePrefix()
    {
        return gStaticMemberVariablePrefix;
    }

    public static void setStaticMemberVariablePrefix(String pStaticMemberVariablePrefix)
    {
        gStaticMemberVariablePrefix = pStaticMemberVariablePrefix;
    }

    /**
     * table of all declared non-static fields for a class or ita base classes
     */
    public static final Map<Class, Field[]> m_DeclaredClassFields =
            new HashMap<Class, Field[]>();
    public static final Field[] EMPTY_FIELD_ARRAY = {};

    public static Field[] getClassFields(Class cls)
    {
        synchronized (m_DeclaredClassFields) {
            Field[] ret = m_DeclaredClassFields.get(cls);
            if (ret == null) {
                ret = buildClassFields(cls);
                m_DeclaredClassFields.put(cls, ret);
            }
            return ret;

        }
    }

    protected static Field[] buildClassFields(Class cls)
    {
        if (cls.isInterface() || cls.isArray())
            return EMPTY_FIELD_ARRAY;
        synchronized (m_DeclaredClassFields) {
            String className = cls.getName();
            if (className.equals("org.wcs.gain.ui.IndividualSampleFacade"))
                 CommonUtilities.breakHere();
            if (className.equals("org.wcs.gain.SamplingUnit"))
                 CommonUtilities.breakHere();
            if (className.equals("org.wcs.gain.AbstractWSCObject"))
                 CommonUtilities.breakHere();
            Field[] current = EMPTY_FIELD_ARRAY;
            if (Object.class != cls) {
                current = getClassFields(cls.getSuperclass());
            }
            Map<String, Field> holder = new HashMap<String, Field>();
            for (int i = 0; i < current.length; i++) {
                Field field = current[i];
                holder.put(field.getName(), field);
            }
            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (ClassUtilities.isStatic(field))
                    continue;
                holder.put(field.getName(), field);
            }
            Field[] ret = new Field[holder.size()];
            holder.values().toArray(ret);
            return ret;
        }
    }

    /**
     * Table mapping Class to a Map holding
     * a map from method name to method
     * NOTE - This assumes that there is one and
     * only one public method of a given name
     */
    protected static Map<Class, ClassAnalysis> gClassToAnalysisTable;

    /**
     * used to invoke getters which have no arguments
     */
    public static final Object[] EMPTY_CALL = new Object[0];


    public static Object getStaticField(String name, Class cls)
    {
        try {
            Field fld = cls.getField(name);
            return fld.get(null);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * all properties supported by the class
     *
     * @param TheClass non-null target class
     * @return - non-null properties collection
     */
    public static ClassProperty[] getProperties(Class TheClass)
    {
        ClassAnalysis anal = getAnalysis(TheClass);
        if (anal.getPropertiesList() == null)
            anal.buildPropertiesList();
        return (anal.getPropertiesList());
    }

    /**
     * look uo one property by name
     *
     * @param TheClass non-null target class
     * @param PropName non-null property name
     * @return - non-null properties collection
     */
    public static boolean isPropertyReadable(Class TheClass, String PropName)
    {
        ClassProperty prop = getClassProperty(TheClass, PropName);
        if (prop == null)
            return (false);
        if (prop.isStatic())
            return (false);
        if (prop.getGetMethod() == null)
            return (false);
        return (true);
    }

    /**
     * look uo one property by name
     *
     * @param theClass     non-null target class
     * @param propertyName non-null property name
     * @return - possibly null property
     */
    public static ClassProperty findClassProperty(Class theClass, String propertyName)
    {
        ClassProperty[] TheProperties = getProperties(theClass);
        for (int i = 0; i < TheProperties.length; i++) {
            String s = TheProperties[i].getName();
            if (s.equalsIgnoreCase(propertyName)) {
                return (TheProperties[i]);
            }
        }
        return (null);
    }


    /**
     * look uo one property by name
     *
     * @param theClass     non-null target class
     * @param propertyName non-null property name
     * @return - possibly null property
     */
    public static ClassProperty getClassProperty(Class theClass, String propertyName)
    {
        ClassProperty ret = findClassProperty(theClass, propertyName);
        if (ret == null)
            getLogger().debug("Can't find property '" + propertyName + "' in " + theClass);
        return (ret);
    }

    /**
     * all properties supported by the class
     *
     * @param TheClass non-null target class
     * @return - non-null properties collection
     */
    public static String[] getPropertyNames(Class TheClass)
    {
        ClassProperty[] props = getProperties(TheClass);
        String[] ret = new String[props.length];
        for (int i = 0; i < ret.length; i++)
            ret[i] = props[i].getName();
        return (ret);
    }

    /**
     * return all collections supported by the class
     *
     * @param TheClass non-null target class
     * @return - non-null properties collection
     */
    public static ClassCollection[] getCollections(Class TheClass)
    {
        ClassAnalysis anal = getAnalysis(TheClass);
        if (anal.getCollectionsList() == null)
            anal.buildCollectionsList();
        return (anal.getCollectionsList());
    }


    /**
     * return a collection descriptor
     *
     * @param TheClass non-null target class
     * @param name     non-null propertyName - must hand methods get<name>
     * @return - possibly null ClassCollection
     */
    public static ClassCollection getCollection(Class TheClass, String name)
    {
        ClassAnalysis anal = getAnalysis(TheClass);
        Map<String, ClassCollection> tb = anal.getCollections();
        ClassCollection ret = tb.get(name);
        if (ret != null)
            return (ret);
        // try caseless get
        ret =   anal.getCaselessCollections().get(name.toUpperCase());
        return (ret);
    }

    /**
     * return a collection descriptor
     *
     * @param TheObject non-null target object
     * @param name      non-null propertyName - must hand methods get<name>
     * @return - possibly null ClassCollection
     */
    public static ClassCollection getCollection(Object TheObject, String name)
    {
        return (getCollection(TheObject.getClass(), name));
    }

    /**
     * add an item to the collection given by name
     *
     * @param Target non-null target object
     * @param name   non-null collection name
     * @param added  non-null item to add - must be acceptable type
     */
    public static void addCollectionItem(Object TheTarget, String name, Object added)
    {
        ClassCollection col = getCollection(TheTarget, name);
        if (col == null)
            throw new IllegalArgumentException("Collection for " + name + " not found");
        Method TheMethod = col.getAddMethod();
        try {
            Object[] call = {added};
            TheMethod.invoke(TheTarget, call);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(
                    "IllegalAccessException getter for " + name + " not found");
        }
        catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(
                    "InvokationTargetException getter for " + name + " not found");
        }

    }

    /**
     * return a value for the property of a given property name
     *
     * @param Target non-null target object
     * @param name   non-null collection name
     * @param added  non-null item to add - must be acceptable type
     */
    public static Enumeration getCollectionItems(Object TheTarget, String name)
    {
        ClassCollection col = getCollection(TheTarget, name);
        if (col == null)
            throw new IllegalArgumentException("Collection for " + name + " not found");
        Method TheMethod = col.getEnumerateMethod();
        try {
            Object ret = TheMethod.invoke(TheTarget, EMPTY_CALL);
            if (ret instanceof Iterator) {
                return (new IteratorToEnumeration((Iterator) ret));
            }
            return ((Enumeration) ret);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(
                    "IllegalAccessException getter for " + name + " not found");
        }
        catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(
                    "InvokationTargetException getter for " + name + " not found");
        }

    }

    /**
     * return a value for the property of a given property name this
     * call can accept properties of the form customer.address.line1 which
     * will find a property called customer - if non-null will find a property in
     * customer called field which will find a property called line1. If any item
     * in the chain is null then null is returned
     *
     * @param Target non-null target object
     * @param name   non-null propertyName - must hand methods set<name>
     * @return - possibly null propetyValue
     */
    public static Object getProperty(Object TheTarget, String name)
    {
        if (name.indexOf('.') == -1)
            return (getSingleProperty(TheTarget, TheTarget.getClass(), name));
        String[] items = name.split("\\.");
        Object current = TheTarget;
        for (int i = 0; i < items.length; i++) {
            current = getSingleProperty(current, current.getClass(), items[i]);
            if (current == null)
                return (null);
        }
        return (current);
    }

    /**
     * return a value for the property of a given property name this
     * call can accept properties of the form customer.address.line1 which
     * will find a property called customer - if non-null will find a property in
     * customer called field which will find a property called line1. If any item
     * in the chain is null then null is returned
     *
     * @param Target non-null target object
     * @param name   non-null propertyName - must hand methods set<name>
     * @return - possibly null propetyValue
     */
    public static Object getProperty(Object TheTarget, Class targetClass, String name)
    {
        if (name.indexOf('.') == -1)
            return (getSingleProperty(TheTarget, targetClass, name));
        String[] items = name.split("\\.");
        Object current = TheTarget;
        for (int i = 0; i < items.length; i++) {
            current = getSingleProperty(current, current.getClass(), items[i]);
            if (current == null)
                return (null);
        }
        return (current);
    }

    /**
     * set a value for the property of a given property name this
     * call can accept properties of the form customer.address.line1 which
     * will find a property called customer - if non-null will find a property in
     * customer called field which will find a property called line1. If any item
     * in the chain is null IllegalArgumentException is thrown
     *
     * @param Target non-null target object
     * @param name   non-null propertyName - must hand methods set<name>
     * @return - possibly null propetyValue
     */
    protected static Object getSingleProperty(Object TheTarget, Class TheClass, String name)
    {
        Method TheMethod = findGetMethod(TheClass, name);
        if (TheMethod == null) {
            String[] AllProps = Util.removeDuplicateEntries(getAllProperties(TheClass));
            String Message = "Property getter for " + name + " in class " +
                    TheClass.getName() + " not found\nValid Properties are:\n" +
                    Util.buildListString(AllProps, 8);
            getLogger().error(Message);
            throw new IllegalArgumentException(Message);
        }
        Object ret = null;
        try {
            ret = TheMethod.invoke(TheTarget, EMPTY_CALL);
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "IllegalArgumentException getter for " + name + " with message " +
                            ex.getMessage());
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(
                    "IllegalAccessException getter for " + name + " not found");
        }
        catch (InvocationTargetException ex1) {
            Throwable RealException = ex1.getTargetException();
            throw new RuntimeException("Error getting property " + name, RealException);
        }
        return (ret);
    }

    /**
     * set the value for the property of a given property name
     *
     * @param Target non-null target object
     * @param name   non-null propertyName - must hand methods set<name>
     * @param value  possibly null new propetyValue - note if null and there are several set methods
     *               any one might be called
     * @throws IllegalArgumentException if elements of the chain are null
     */
    public static void setProperty(Object TheTarget, String name, Object value)
    {
        if (name.indexOf('.') == -1) {
            setSingleProperty(TheTarget, name, value);
            return;
        }

        String[] items = name.split("\\.");
        Object current = TheTarget;
        for (int i = 0; i < items.length - 1; i++) {
            current = getSingleProperty(current, current.getClass(), items[i]);
            if (current == null)
                throw new IllegalArgumentException(
                        "Property " + name + " cannot be set from Object " +
                                TheTarget);
        }
        setSingleProperty(current, name, value);
    }

    /**
     * set the value for the property of a given property name
     *
     * @param Target non-null target object
     * @param name   non-null propertyName - must hand methods set<name>
     * @param value  possibly null new propetyValue - note if null and there are several set methods
     *               any one might be called
     * @throws IllegalArgumentException if elements of the chain are null
     */
    public static void setProperties(Object TheTarget, Map Values)
    {
        String PropName = null;
        Object PropValue = null;
        Set set = Values.keySet();
        String[] items = (String[]) set.toArray(new String[set.size()]);
        try {
            for (int i = 0; i < items.length; i++) {
                PropName = items[i];
                PropValue = Values.get(PropName);
                setSingleProperty(TheTarget, PropName, PropValue);
            }
        }
        catch (Exception ex) {
            throw new PropertySetFailException(PropName, PropValue, TheTarget, ex);
        }
    }

    /**
     * set the value for the property of a given property name
     *
     * @param Target non-null target object
     * @param name   non-null propertyName - must hand methods set<name>
     * @param value  possibly null new propetyValue - note if null and there are several set methods
     *               any one might be called
     * @throws IllegalArgumentException if elements of the chain are null
     */
    public static void setProperties(Object TheTarget, Map Values, ILogger log)
    {
        Collection keys = Values.keySet();
        String[] items = new String[keys.size()];
        keys.toArray(items);
        for (int i = 0; i < items.length; i++) {
            Object value = Values.get(items[i]);
            try {
                setSingleProperty(TheTarget, items[i], value);
            }
            catch (Exception ex) {
                log.error("Error setting '" + items[i] + "' to '" + value, ex);
            }
        }
    }

    /**
     * using refleaction call a non-oberloaded method
     * This call is faster and cleaner that doing inling because the
     * lookup is cached
     *
     * @param TheTarget  non-null target obejct
     * @param methodName non-null name of a method in the target
     * @param args       argumewnts
     * @return whatever is returned
     */
    public static Object invokeNonOverloadedMethod(Object TheTarget, String methodName,
                                                   Object... args)
    {
        if ("setToneDefinition".equals(methodName))
             CommonUtilities.breakHere();
        Class theClass = TheTarget.getClass();
        Method TheMethod = findNonOverloadedMethod(theClass, methodName);
        if (TheMethod == null) {
            TheMethod = findOverloadedMethodWithArgs(theClass, methodName, args);
            if (TheMethod == null) {

                // repeat to debug
                throw new IllegalArgumentException(
                        "No non-overloaded method " + methodName + " not found in class '" +
                                TheTarget.getClass().getName());
            }

        }
        Object ret = null;

        try {
            ret = TheMethod.invoke(TheTarget, args);
            return ret;
        }
        catch (IllegalArgumentException ex) {
            throw ex;
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("IllegalAccessException for " + methodName);
        }
        catch (InvocationTargetException ex) {
            //throw new RuntimeException(ex.getTargetException());
            Throwable innerEx = ex.getTargetException();
            StringBuilder sb = new StringBuilder(64);
            sb.append("ClassAnalyzer encountered an ");
            sb.append(innerEx.getClass().getName());
            sb.append(" attempting to invoke the '");
            sb.append(TheMethod.getName()).append("' method of ");
            sb.append(TheTarget.toString()).append(" with args values of: ");
            for (int i = 0; i < args.length; i++) {
                if (i > 0)
                    sb.append(", ");
                if (args[i] == null)
                    sb.append("null");
                else
                    sb.append(args[i].toString());
            }
            throw new RuntimeException(
                    "Calling " + methodName + " failed - cause:" + sb.toString(),
                    innerEx);
        }
        catch (RuntimeException ex2) {
            String myValue = "null";
            String Message = "Setting " + methodName + " to Value " + myValue + " failed";
          //  getLogger().logError(ClassAnalyzer.class, Message, ex2);
            throw new RuntimeException(Message, ex2);
        }

    }

    /**
     * set the value for the property of a given property name
     *
     * @param Target non-null target object
     * @param name   non-null propertyName - must hand methods set<name>
     * @param value  possibly null new propetyValue - note if null and there are several set methods
     *               any one might be called
     */
    public static void setSingleProperty(Object TheTarget, String name, Object value)
    {
        //noinspection UnusedDeclaration
        Class TestArgs = null;
        if (value != null)
            TestArgs = value.getClass();
        Method TheMethod = findSetMethod(TheTarget.getClass(), name, false);
        if (TheMethod == null) {
            // repeat to debug
            StringBuilder error = new StringBuilder(256);
            String sep = System.getProperty("line.separator");
            error.append("Property setter for ").append(name).append(" in class ");
            error.append(TheTarget.getClass().getName()).append(" not found");
            String[] Properties = Util.removeDuplicateEntries(
                    getAllProperties(TheTarget.getClass()));
            for (int i = 0; i < Properties.length; i++)
                error.append(sep).append("  ").append(Properties[i]);
           // Assertion.logError(error.toString());

            //noinspection UnusedAssignment
            TheMethod = findSetMethod(TheTarget.getClass(), name, false);
            throw new IllegalArgumentException(
                    "Property setter for " + name + " not found in class '" +
                            TheTarget.getClass().getName() + "\n" +
                            "Available properties are: \n" +
                            Util.buildListString(Properties, 1, 8)
            );
        }
        Object ret = null;
        try {
            value = convertValueForMethod(TheMethod, value);
        }
        catch (RuntimeException ex3) {
            value = convertValueForMethod(TheMethod, value);
            throw new RuntimeException(
                    "Setting " + name + " to Value " + value.toString() + " failed", ex3);
        }
        Object[] args = {value};

        try {
            ret = TheMethod.invoke(TheTarget, args);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("IllegalAccessException for " + name);
        }
        catch (InvocationTargetException ex) {
            //throw new RuntimeException(ex.getTargetException());
            Throwable innerEx = ex.getTargetException();
            StringBuffer sb = new StringBuffer(64);
            sb.append("ClassAnalyzer encountered an ");
            sb.append(innerEx.getClass().getName());
            sb.append(" attempting to invoke the '");
            sb.append(TheMethod.getName()).append("' method of ");
            sb.append(TheTarget.toString()).append(" with args values of: ");
            for (int i = 0; i < args.length; i++) {
                if (i > 0)
                    sb.append(", ");
                if (args[i] == null)
                    sb.append("null");
                else
                    sb.append(args[i].toString());
            }
            String valueString = "null";
            if (value != null)
                valueString = value.toString();
            throw new RuntimeException(
                    "Setting " + name + " to Value " + valueString + " failed - cause:" + sb.toString(),
                    innerEx);
        }
        catch (RuntimeException ex2) {
            String myValue = "null";
            if (value != null)
                myValue = value.toString();
            String Message = "Setting " + name + " to Value " + myValue + " failed";
          //  LogUtilities.logError(ClassAnalyzer.class, Message, ex2);
            throw new RuntimeException(Message, ex2);
        }
    }

    /**
     * find a method which matches the name name in class TheClass where data is the input
     *
     * @param TheClass non-null Class to search for the method
     * @return if true return null of the method cannot be found
     */
    public static String[] getAllProperties(Class TheClass)
    {
        ClassAnalysis anal = getAnalysis(TheClass);
        Map<String, ClassProperty> tb = anal.getProperties();
        TreeSet holder = new TreeSet();
        Iterator keys = tb.keySet().iterator();
        while (keys.hasNext())
            holder.add(keys.next());

        String[] ret = (String[])holder.toArray(new String[holder.size()]);
        return (ret);
    }


    /**
     * find a method which matches the name name in class TheClass where data is the input
     *
     * @param TheClass non-null Class to search for the method
     * @param name     non-nullmethod name search caseful and then caseless
     * @param data     class of data to set
     * @param if       true return null of the method cannot be found
     */
    public static Method findNonOverloadedMethod(Class TheClass, String name)
    {
        ClassAnalysis anal = getAnalysis(TheClass);
        return anal.getNonOverloadedMethods().get(name);
    }

    /**
     * fina a method natching name and arg count
     *
     * @param TheClass non-null Class to search for the method
     * @param name     non-nullmethod name search caseful and then caseless
     * @param argCount number or arguments
     * @return
     */
    public static Method findOverloadedMethodWithArgs(Class TheClass, String name, int argCount)
    {
        Method[] methods = TheClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            if (name.equals(methodName)) {
                Class<?>[] types = method.getParameterTypes();
                if (argCount == types.length)   {
                     return method;

                }
            }
        }
        return null;
    }
    /**
     * fina a method natching name and arg count
     *
     * @param TheClass non-null Class to search for the method
     * @param name     non-nullmethod name search caseful and then caseless
     * @param argCount number or arguments
     * @return
     */
    public static Method findOverloadedMethodWithArgs(Class TheClass, String name, Object[] args)
    {
        Class[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if(arg != null)
                types[i] = arg.getClass();
            else
                types[i] = Object.class;
         }
        try {
            return TheClass.getMethod(name,types);
        }
        catch(NoSuchMethodException ex) {
            Method ret = findOverloadedMethodWithArgs2(TheClass, name, args);
            if(ret == null)
                ret = findOverloadedMethodWithArgs(TheClass, name, args.length);
            return ret;
        }
    }

    /**
     * fina a method natching name and arg count
     *
     * @param TheClass non-null Class to search for the method
     * @param name     non-nullmethod name search caseful and then caseless
     * @param argCount number or arguments
     * @return
     */
    public static Method findOverloadedMethodWithArgs2(Class TheClass, String name, Object[] args)
    {
        Method[] methods = TheClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            String methodName = method.getName();
            if (name.equals(methodName)) {
                boolean ok = true;
                Class<?>[] types = method.getParameterTypes();
                if (args.length == types.length)   {
                    for (int j = 0; j < types.length; j++) {
                        Class<?> type = types[j];
                        if(!type.isInstance(args[j])) {
                            ok = false;
                            break;
                        }
                    }
                    if(ok)
                        return method;

                }
            }
        }
        return null;
     }


    /**
     * find a method which matches the name name in class TheClass where data is the input
     *
     * @param TheClass non-null Class to search for the method
     * @param name     non-nullmethod name search caseful and then caseless
     * @param data     class of data to set
     * @param if       true return null of the method cannot be found
     */
    public static Method findSetMethod(Class TheClass, String name, boolean nullOK)
    {
        ClassAnalysis anal = getAnalysis(TheClass);
        Map<String, ClassProperty> tb = anal.getProperties();
        ClassProperty prop = tb.get(name);
        if (prop == null) { // try caseless search
            prop = anal.getCaselessProperties().get(name.toUpperCase());
        }
        if (prop != null)
            return (prop.getSetMethod());
        if (nullOK)
            return (null);
        StringBuffer error = new StringBuffer(256);
        String sep = System.getProperty("line.separator");
        error.append("Cannot find method to set '").append(name).append("' property in ").append(
                TheClass.getName());
        error.append(sep).append("Methods are: ");
        Iterator e = tb.keySet().iterator();
        while (e.hasNext()) {
            error.append(sep).append("   ");
            error.append(e.next());
        }
        throw new IllegalArgumentException(
                "Cannot find method to set '" + name + "' property in " + TheClass.getName());
    }

    /**
     * return a get method for a given property name
     *
     * @param TheClass non-null target class
     * @param name     non-null propertyName - must hand methods get<name>
     * @return - possibly null Method
     */
    public static Method findGetMethod(Class TheClass, String name)
    {
        ClassAnalysis anal = getAnalysis(TheClass);
        Map<String, ClassProperty> tb = anal.getProperties();
        ClassProperty prop = (ClassProperty) tb.get(name);
        if (prop == null) { // try caseless search
            prop = (ClassProperty) anal.getCaselessProperties().get(name.toUpperCase());
        }
        if (prop != null)
            return (prop.getGetMethod());
        return (null);
    }

    /* =========================================================
   protected code
   ========================================================= */

    protected static ClassAnalysis getAnalysis(Class in)
    {
        // make sure basic tables built
        if (gClassToAnalysisTable == null) {
            buildSourceTable();
        }
        ClassAnalysis ret = (ClassAnalysis) gClassToAnalysisTable.get(in);
        if (ret != null)
            return (ret);
        return (analyzeClass(in));
    }

    protected static synchronized void buildSourceTable()
    {
        if (gClassToAnalysisTable == null) {
            gClassToAnalysisTable = new HashMap<Class, ClassAnalysis>();
        }
    }

    protected static synchronized ClassAnalysis analyzeClass(Class in)
    {
        ClassAnalysis ret = gClassToAnalysisTable.get(in);
        if (ret != null)
            return (ret);
        ret = new ClassAnalysis(in);

        Method[] methods = in.getMethods();
        accumulateProperties(in, ret.getProperties(), ret.getCaselessProperties(), methods);
        accumulateMethods(in, ret.getNonOverloadedMethods(), methods);
        accumulateCollections(in, ret.getCollections(), ret.getCaselessCollections(), methods);

        gClassToAnalysisTable.put(in, ret);
        return (ret);
    }

    /**
     * create a map of methods that can be looked up uniquely by name
     * this simplified reflection
     *
     * @param TheClass
     * @param uniqueMethods
     * @param methods
     */
    protected static synchronized void accumulateMethods(Class TheClass,
                                                         Map<String, Method> uniqueMethods,
                                                         Method[] methods)
    {
        Set<String> nonUnique = new HashSet();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            String s = m.getName();
            if (uniqueMethods.get(s) != null)
                nonUnique.add(s);
            else
                uniqueMethods.put(s, m);
        }
        // drop non-unique methods
        for (Iterator<String> stringIterator = nonUnique.iterator(); stringIterator.hasNext();) {
            String s = stringIterator.next();
            uniqueMethods.remove(s);
        }
    }


    protected static synchronized void accumulateProperties(Class TheClass,
                                                            Map<String, ClassProperty> items,
                                                            Map<String, ClassProperty> caseLessItems,
                                                            Method[] methods)
    {
        for (int i = 0; i < methods.length; i++) {
            // HACK - ignore methods which are not get and set
            // possibilities i.e. CDGJColllection.add(Object,String) S. Lewis
            if (methods[i].getParameterTypes().length <= 1) {
                String testName = methods[i].getName();
                testName = null;
                if (ClassUtilities.isGetMethod(methods[i], null)) {
                    handlePossibleProperty(TheClass, methods[i], methods, items, caseLessItems);
                }
            }
        }
    }


    protected static synchronized void accumulateCollections(Class TheClass,
                                                             Map<String, ClassCollection> items,
                                                             Map<String, ClassCollection> uppercaseItems,
                                                             Method[] methods)
    {
        Vector possibleCollections = new Vector();

        for (int i = 0; i < methods.length; i++) {
            // HACK - ignore methods which are not get and set
            // possibilities i.e. CDGJColllection.add(Object,String) S. Lewis
            if (methods[i].getParameterTypes().length == 1) {
                String testName = methods[i].getName();
                if (testName.startsWith("add") && !testName.endsWith("Listener"))
                    possibleCollections.addElement(testName.substring(3));
            }
        }
        for (int i = 0; i < possibleCollections.size(); i++) {
            String ColectionName = (String) possibleCollections.elementAt(i);
              ClassCollection TheCollection = buildCollection(TheClass, ColectionName, methods);
            if (TheCollection != null && validateClassCollection(TheCollection)) {
                items.put(ColectionName, TheCollection);
                uppercaseItems.put(ColectionName.toUpperCase(), TheCollection);
            }
        }

    }

    protected static ClassCollection buildCollection(Class TheClass, String Name, Method[] methods)
    {
        String AddName = "add" + Name;
        String CountName = "get" + Name + "Count";
        String RemoveName = "remove" + Name;
        String EnumerateName = "enumerate" + Name;
        String IterateName = "iterate" + Name;

        ClassCollection ret = new ClassCollection(TheClass);
        ret.setName(Name);

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getParameterTypes().length == 1) {
                String testName = methods[i].getName();
                if (testName.equals(AddName)) {
                    ret.setAddMethod(methods[i]);
                }
                if (testName.equals(RemoveName)) {
                    ret.setRemoveMethod(methods[i]);
                }
            }
        }

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getParameterTypes().length == 0) {
                String testName = methods[i].getName();
                Class<?> returnType = methods[i].getReturnType();
                if (testName.equals(CountName)) {
                    if (returnType == Integer.TYPE)
                        ret.setCountMethod(methods[i]);
                }
                if (testName.equals(EnumerateName)) {
                    if (returnType == Enumeration.class)
                        ret.setEnumerateMethod(methods[i]);
                }
                if (testName.equals(IterateName)) {
                    if (returnType == Iterator.class)
                        ret.setEnumerateMethod(methods[i]);
                }
                if (returnType.isArray()) {
                    if (returnType.getComponentType() == ret.getType()) {
                        if (testName.equals("get" + Name + "Items") || testName.startsWith(
                                "getAll" + Name))
                            ret.setItemsMethod(methods[i]);
                    }
                }
            }
        }
        return (ret);

    }


    protected static void handlePossibleProperty(Class TheClass, Method test, Method[] methods,
                                                 Map<String, ClassProperty> tb,
                                                 Map<String, ClassProperty> caselessTb)
    {
        Method SetMethod = null;
        String testName = test.getName();
        String propName;
        if (TheClass.getName().startsWith("com.wcs.gain.SamplingUnitLaboratoryFinding")) {
             CommonUtilities.breakHere();
        }
        if (testName.equals("getClass"))
            return;
        String TestSet;
        if (testName.startsWith("get")) {
            propName = testName.substring(3);
        }
        else {
            propName = testName.substring(2);
        }
        TestSet = "set" + propName;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(TestSet)) {
                SetMethod = methods[i];
                break;
            }
        }
        //   if(SetMethod == null)
        //        return;
        ClassProperty prop = new ClassProperty();
        prop.setName(propName);
        prop.setOwningClass(TheClass);
        prop.setGetMethod(test);
        prop.setSetMethod(SetMethod);

        String lsPropName = propName.toLowerCase();
        if (lsPropName.equals("description"))
             CommonUtilities.breakHere();
        Field[] fields = getClassFields(TheClass);
        String backingFieldName = null;
         for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldFullName = field.getName();
            if (backingFieldName != null) {
                if (backingFieldName.equals(fieldFullName)) {
                    prop.setBackingField(field);
                    break;
                }

            }
            else {
                String fieldName = fieldFullName.toLowerCase();
                if (fieldName.contains(lsPropName)) {
                    String testFullName = ClassAnalyzer.getMemberVariablePrefix() + lsPropName;
                    if (fieldName.equals(testFullName)) {
                        prop.setBackingField(field);
                        break;
                    }
                }
            }

        }

        tb.put(propName, prop);
        caselessTb.put(propName.toUpperCase(), prop);

    }

    public static Object convertValueForMethod(Method TheMethod, Object value)
    {
        Class RequiredClass = TheMethod.getParameterTypes()[0];
        return (convertValueToClass(RequiredClass, value));
    }


    protected static Object convertStringToDesiredType(Class dataType, String sValue)
    {
        Object value = null;
        // try a registered solution
        IObjectConverter conv = getRegisterConverter(dataType);
        if(conv != null)
            return conv.convertToDesiredClass(sValue,dataType);
        dataType = ClassUtilities.primitiveToWrapper(dataType);
        if (dataType.isEnum()) {
            return Enum.valueOf(dataType, sValue);
        }
        if (dataType == String.class) {
            value = sValue;
        }
        else if (dataType == Integer.class) {
            value = new Integer(sValue);
        }
        else if (dataType == Boolean.class) {
            // Accept "t" as true
            if (sValue.equals("t"))
                value =  Boolean.TRUE;
            else
                value =  Boolean.valueOf(sValue);
        }
        else if (dataType == Short.class) {
            value = new Short(sValue);
        }
        else if (dataType == Character.class) {
            if (sValue.length() > 0)
                value = new Character(sValue.charAt(0));
            else
                value = new Character(' ');
        }
        else if (dataType == Byte.class) {
            value = new Byte(sValue);
        }
        else if (dataType == Double.class) {
            value = new Double(sValue);
        }
        else if (dataType == Float.class) {
            value = new Float(sValue);
        }
        else if (dataType == Long.class) {
                value = new Long(sValue);
        }
        else if (dataType == Class.class) {
            try {
                value = Class.forName(sValue);
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Bad class name " + sValue);
            }

        }
        else if (dataType == com.lordjoe.utilities.PseudoEnum.class) {
            value = PseudoEnum.valueOf(dataType, sValue);
        }
        else if (dataType == java.sql.Timestamp.class) {
            //fixed format expected, per java.util.Date case below
            java.util.Date tempValue = Util.parseDate(sValue);
            value = new java.sql.Timestamp(tempValue.getTime());
        }
        else if (dataType == java.sql.Date.class) {
            //fixed format expected, per java.util.Date case below
            java.util.Date tempValue = Util.parseDate(sValue);
            value = new java.sql.Date(tempValue.getTime());
        }
        else if (dataType == java.util.Date.class) {
            /*
                * stub in the only date handler we'll need,
                * rather than extending the date conversion library
                * (for now...)
                * TODO generalize string to date conversion in local conversion
                * utilities?
                */
            value = Util.parseDate(sValue);
        }
        else if (dataType == java.lang.Object.class) {
            value = sValue;
        }
        else {
            // find a constructor taking a string and call it
            Class[] args = {String.class};
            try {
                Constructor Const = dataType.getConstructor(args);
                Object[] constArgs = {sValue};
                value = Const.newInstance(constArgs);
            }


            catch (Exception ex) {
                throw new IllegalArgumentException("Data cannot be constructed from String: " +
                        dataType.getName() + " String = " + sValue);
            }
        }
        return (value);
    }

    public static final String DATE_FORMAT_1 = "dd/MM/yyyy";
    public static final String DATE_FORMAT_1X = "dd-MM-yyyy";
    public static final String DATE_FORMAT_2 = "dd/MM/yy";
    public static final String DATE_FORMAT_2X = "dd-MM-yy";
    public static final String DATE_FORMAT_3 = "dd/MM/yyyy hh:mm";
    public static final String DATE_FORMAT_3X = "dd-MM-yyyy hh:mm";
    public static final String DATE_FORMAT_4 = "dd/MM/yyyy hh:mm";
    public static final String DATE_FORMAT_4X = "dd-MM-yyyy hh:mm";
    public static final String DATE_FORMAT_5 = "dd MMM yyyy";
    public static final String DATE_FORMAT_5X = "dd-MMM-yyyy";

    private static Date stringToDate(String sValue)
    {
        Date value;

        if (sValue.startsWith("<?xml"))
            return XMLUtil.decodeXMLDate(sValue);
        try {
            int len = sValue.length();
            if (len == DATE_FORMAT_1.length() || len == DATE_FORMAT_1.length() - 1) {
                if (sValue.contains("/"))
                    return new SimpleDateFormat(DATE_FORMAT_1).parse(sValue);
                if (sValue.contains("-"))
                    return new SimpleDateFormat(DATE_FORMAT_1X).parse(sValue);
            }
            if (len == DATE_FORMAT_2.length() || len == DATE_FORMAT_2.length() - 1) {
                if (sValue.contains("/"))
                    return new SimpleDateFormat(DATE_FORMAT_2).parse(sValue);
                if (sValue.contains("-"))
                    return new SimpleDateFormat(DATE_FORMAT_2X).parse(sValue);
                throw new UnsupportedOperationException("Fix This"); // ToDo
            }
            if (len == DATE_FORMAT_3.length()) {
                if (sValue.contains("/"))
                    return new SimpleDateFormat(DATE_FORMAT_3).parse(sValue);
                if (sValue.contains("-"))
                    return new SimpleDateFormat(DATE_FORMAT_3X).parse(sValue);
                throw new UnsupportedOperationException("Fix This"); // ToDo
            }
            if (len == DATE_FORMAT_4.length()) {
                if (sValue.contains("/"))
                    return new SimpleDateFormat(DATE_FORMAT_4).parse(sValue);
                if (sValue.contains("-"))
                    return new SimpleDateFormat(DATE_FORMAT_4X).parse(sValue);
                throw new UnsupportedOperationException("Fix This"); // ToDo
            }
            if (len == DATE_FORMAT_5.length() || len == DATE_FORMAT_5.length() - 1) {
                if (sValue.contains(" "))
                    return new SimpleDateFormat(DATE_FORMAT_5).parse(sValue);
                if (sValue.contains("-"))
                    return new SimpleDateFormat(DATE_FORMAT_5X).parse(sValue);
                throw new UnsupportedOperationException("Fix This"); // ToDo
            }
            else
                value = new Date(Long.parseLong(sValue));
            return value;
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static Object convertValueToClass(Class RequiredClass, Object value)
    {
        if (value == null)
            return (convertNullValueToClass(RequiredClass)); // no conversion needed;
        if ("null".equals(value))
            return (convertNullValueToClass(RequiredClass)); // no conversion needed;
        if (RequiredClass.isInstance(value))
            return (value); // no conversion needed;
        Class ValueClass = value.getClass();
        ValueClass = null;
        if (PseudoEnum.class.isAssignableFrom(RequiredClass)) {
            return (PseudoEnum.valueOf(RequiredClass, value.toString()));
        }
        if (value instanceof String) {
            return (convertStringToDesiredType(RequiredClass, (String) value));
        }
        if (RequiredClass.isPrimitive()) {
            return (convertPrimitive(RequiredClass, value));
        }
        if (Number.class.isAssignableFrom(RequiredClass)) {
            return (convertNumber(RequiredClass, value));
        }
        if (RequiredClass == String.class)
            return (value.toString());

        return (handleBadConversion(RequiredClass, value)); // cannot handle this case
    }

    protected static Object convertNullValueToClass(Class RequiredClass)
    {
        if (!RequiredClass.isPrimitive())
            return (null);
        if (RequiredClass == Boolean.TYPE)
            return (Boolean.FALSE);
        if (RequiredClass == Integer.TYPE)
            return (new Integer(0));
        if (RequiredClass == Double.TYPE)
            return (new Double(0));
        if (RequiredClass == Character.TYPE)
            return (new Character((char) 0));
        if (RequiredClass == Byte.TYPE)
            return (new Byte((byte) 0));
        if (RequiredClass == Float.TYPE)
            return ((float) 0);
        if (RequiredClass == Short.TYPE)
            return ((short) 0);
        return (null);
    }

    protected static Object handleBadConversion(Class RequiredClass, Object value)
    {
        if (gConverter != null) {
            return (gConverter.convertToDesiredClass(value, RequiredClass));
        }

        throw new IllegalArgumentException("Cannot convert " + value +
                " of class " + value.getClass().getName() +
                " to " + RequiredClass.getName());
    }

    protected static Object convertNumber(Class RequiredClass, Object avalue)
    {
        if (avalue instanceof Number) {
            Number RealValue = (Number) avalue;
            if (RequiredClass == Integer.class) {
                return (new Integer(RealValue.intValue()));
            }
            if (RequiredClass == Double.class) {
                return (RealValue.doubleValue());
            }
            if (RequiredClass == Short.class) {
                return (new Short((short) RealValue.intValue()));
            }
            if (RequiredClass == Long.class) {
                return (RealValue.longValue());
            }
        }
        return (handleBadConversion(RequiredClass, avalue)); // cannot handle this case
    }

    protected static Object convertPrimitive(Class RequiredClass, Object value)
    {
        Class ValueClass = value.getClass();

        // Check Obviove conversion
        if (RequiredClass == Boolean.TYPE) {
            if (ValueClass == Boolean.class) return (value);
            return (handleBadConversion(RequiredClass, value)); // cannot handle this case
        }
        if (RequiredClass == Integer.TYPE) {
            if (ValueClass == Integer.class) return (value);
            if (ValueClass == Double.class)
                return (new Integer((int) ((Double) value).intValue()));
            return (handleBadConversion(RequiredClass, value)); // cannot handle this case
        }
        if (RequiredClass == Double.TYPE) {
            if (ValueClass == Double.class) return (value);
            if (ValueClass == Integer.class)
                return ((double) ((Integer) value).intValue());
        }
        if (RequiredClass == Short.TYPE) {
            if (ValueClass == Short.class) return (value);
            return (handleBadConversion(RequiredClass, value)); // cannot handle this case
        }
        if (RequiredClass == Byte.TYPE) {
            if (ValueClass == Byte.class) return (value);
            return (handleBadConversion(RequiredClass, value)); // cannot handle this case
        }
        if (RequiredClass == Character.TYPE) {
            if (ValueClass == Character.class) return (value);
            return (handleBadConversion(RequiredClass, value)); // cannot handle this case
        }
        if (RequiredClass == Long.TYPE) {
            if (ValueClass == Long.class) return (value);
            return (handleBadConversion(RequiredClass, value)); // cannot handle this case
        }
        if (RequiredClass == Float.TYPE) {
            if (ValueClass == Float.class) return (value);
            return (handleBadConversion(RequiredClass, value)); // cannot handle this case
        }

        return (handleBadConversion(RequiredClass, value)); // cannot handle this case
    }


    protected static Object buildAttribute(String name, ClassAnalysis anal)
    {
        IAttributeBuilder builder = gAttributeBuilders.get(name);
        if (builder == null)
            return null;
        Object ret = builder.buildAttribute(name, anal);
        if (ret != null)
            anal.putAttribute(name, ret);
        return ret;
    }

    public static void setAttributes(Object item, NameValue[] attributes)
    {
        for (int i = 0; i < attributes.length; i++) {
            String TestName = attributes[i].m_Name;
            // class is a special case - needed by xml
            if (TestName.equals("class")) {
                continue;
            }
            if (TestName.startsWith("xmlns")) {
                continue;
            }
            if (TestName.startsWith("xsi:")) {
                continue;
            }
            if (TestName.equals(XMLUtil.TAG_HANDLED)) {
                continue;
            }
            //noinspection ConstantConditions
            if (TestName != null && TestName.length() > 0)
                setProperty(item, attributes[i].m_Name, attributes[i].m_Value);
        }
    }


    protected static class IteratorToEnumeration implements Enumeration
    {
        private Iterator m_It;

        public boolean hasMoreElements()
        {
            return (m_It.hasNext());
        }

        public Object nextElement()
        {
            return (m_It.next());
        }

        protected IteratorToEnumeration(Iterator it)
        {
            m_It = it;
        }
    }

    public static boolean validateClassCollection(ClassCollection c)
    {
        if (c.getAddMethod() == null) {
         //   Assertion.logError("No add method for " + c.getName() +
         //           " collection in class " + c.getOwnerClass().getName());
            return (false);
        }
//        if (c.gtRemoveMethod(null)) {
//            //    Assertion.logError("No remove method for " + c.m_Name +
//            //      " collection, in class " + c.m_Class.getUrl());
//            return (false);
//        }
        /*   if(c.m_EnumerateMethod == null) {
            Assertion.logError("No enumerate method for " + c.m_Name +
               " collection in, class " + c.m_Class.getUrl());
               return(false);
        }
        */
//        if (c.getCountMethod() == null) {
//            Assertion.logError("No count method for " + c.getName() +
//                    " collection, in class " + c.getOwnerClass().getName());
//            return (false);
//        }
        if (c.getItemsMethod() == null) {
            //        Assertion.logError("No items method for " + c.getName() +
            //                " collection, in class " + c.getOwnerClass().getName());
            return (false);
        }
        return (true);

    }

}
