package org.systemsbiology.awscluster;

import java.io.*;

/**
 * org.systemsbiology.awscluster.ClassPathUtilities
 *      These are a collection of static methods designed to
 *   allow a new java process to be launched - assuming that all libraries are in the classpath
 * @author Steve Lewis
 * @date Oct 7, 2010
 */
public class ClassPathUtilities
{
    public static ClassPathUtilities[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ClassPathUtilities.class;

    private static Character gClassPathSeparator = null;

    public static Character getClassPathSeparator()
    {
        // figure it out - yes we might just look at the os type
        if(gClassPathSeparator == null)  {
             String s = getClasspath();
            if(s.contains(";"))
                 gClassPathSeparator = ';';
            else
                gClassPathSeparator = ':';

        }
        return gClassPathSeparator;
    }

    public static void setClassPathSeparator(Character pClassPathSeparator)
    {
        gClassPathSeparator = pClassPathSeparator;
    }

    public static Process launchProcess(String[] librarySpec,String fullClassName)
     {
         String cp = classPathExistingLibraries(librarySpec);

         return launchProcess(fullClassName, cp);
       }

    public static Process launchProcess( String pCp,String fullClassName)
    {
        StringBuilder sb = new StringBuilder();
        final String javaHome = System.getProperty("java.home");
        sb.append(javaHome + "/bin/javaw");

        if(pCp.contains(" ") && !pCp.contains("\""))
            pCp = "\""  + pCp + "\"";


        sb.append(" -cp " + pCp);

        sb.append("  " + fullClassName);
        String command = sb.toString();

        try {
            final Process process = Runtime.getRuntime().exec(command);
            return process;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static final String CLASS_PATH_PROPERTY = "java.class.path";

    public static String getClasspath()
    {
        return System.getProperty(CLASS_PATH_PROPERTY);
    }

    public static String[] getClasspathElements()
    {


        
        return getClasspath().split(getClassPathSeparator().toString());
    }

    public static String classPathExistingLibraries(String[] libraryNames)      {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < libraryNames.length; i++) {
            String libraryName = libraryNames[i];
            if(sb.length() > 0)
                sb.append(getClassPathSeparator());
            sb.append(classPathExistingLibrary(libraryName));
            
        }

        return sb.toString();
    }

    public static String classPathExistingLibrary(String  libraryName )      {
         String[] elts = getClasspathElements();
        for (int i = 0; i < elts.length; i++) {
            String elt = elts[i];
            if(elt.contains(libraryName ))
                return elt;
        }
        throw new IllegalStateException("cannot find library " + libraryName + " in classpath " + getClasspath());
    }


    public static final String[] SERVER_SPEC = { "jetty-6","servlet-api", "jetty-util"};
//    public static void main(String[] args)
//    {
//        final String cp = getClasspath();
//        final String cls = ClusterServer.class.getName();
//        Process p = launchProcess(cp, cls);
//    }
}
