package org.systemsbiology.gatk;


import java.lang.reflect.*;

/**
 * org.systemsbiology.gatk.MainRunner
 * User: Steve
 * Date: 6/1/12
 */
public class MainRunner {
    public static final MainRunner[] EMPTY_ARRAY = {};

    public static final Class[] MAIN_ARGUMENTS = {String[].class};

    /**
     * needed to make a Class from a string without throwing a checked exception
     *
     * @param mainClassName
     * @return
     */
    private static Class getClassFromName(final String mainClassName) {
        try {
            return Class.forName(mainClassName);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    private final Class m_MainClass;
    private final Method m_MainMethod;
    private final boolean m_AsInstance;

    public MainRunner(final String mainClassName) {
        this(getClassFromName(mainClassName));
    }


    public MainRunner(final Class mainClass) {
        m_MainClass = mainClass;
        try {
            // Samtools like system exis in the main - instanceMain kills that
            Class test = Class.forName("net.sf.picard.cmdline.CommandLineProgram");
            if (test.isAssignableFrom(mainClass)) {
                m_MainMethod = mainClass.getMethod("instanceMain", MAIN_ARGUMENTS);
                m_AsInstance = true;
            }
            else {
                m_MainMethod = mainClass.getMethod("main", MAIN_ARGUMENTS);
                m_AsInstance = false;
              }
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("bad libarary");

        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("class " + mainClass + " has no main method");

        }
    }

    public boolean isAsInstance() {
        return m_AsInstance;
    }

    public Class getMainClass() {
        return m_MainClass;
    }

    public void runMain(String[] args) {

        Object[] realArgs = {args};
        try {
            Object o = null;
            if(isAsInstance())   {
                o = getMainClass().newInstance();
            }
            m_MainMethod.invoke(o, realArgs);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);

        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);

        }
    }
}
