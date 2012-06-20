package org.systemsbiology.gatk;


import java.lang.reflect.*;

/**
 * org.systemsbiology.gatk.MainRunner
 * User: Steve
 * Date: 6/1/12
 */
public class GATKRunner {
    public static final GATKRunner[] EMPTY_ARRAY = {};


    public static final String DEFAULT_MIMIMUM_SCORE = "100";
    public static final int DEFAULT_NUMBER_THREADS = 4;
    public static final int DEFAULT_MINIMUM_BASE_QUALITY_SCORE = 5;

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
    private final Object m_Instance;
    private final Method m_MainMethod;

    public GATKRunner() {
        this(getClassFromName("org.broadinstitute.sting.gatk.CommandLineGATK"));
    }


    public GATKRunner(final Class mainClass) {
        m_MainClass = mainClass;
        try {
            Class cmdLine = getClassFromName("org.broadinstitute.sting.commandline.CommandLineProgram");
            final Class[] MAIN_ARGUMENTS = {cmdLine, String[].class};
            // Samtools like system exis in the main - instanceMain kills that
            m_MainMethod = mainClass.getMethod("start", MAIN_ARGUMENTS);
            m_Instance = m_MainClass.newInstance();
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);

        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);

        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
    }

    public Class getMainClass() {
        return m_MainClass;
    }

    public void runMain(String[] args) {
          Object[] realArgs = {m_Instance,args};
        try {
              m_MainMethod.invoke(null,   realArgs);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);

        }
      }
}
