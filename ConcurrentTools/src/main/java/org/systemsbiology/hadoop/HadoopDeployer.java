package org.systemsbiology.hadoop;


import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * org.systemsbiology.hadoop.HadoopDeployer
 * build a hadoop jar file
 *
 * @author Steve Lewis
 * @date Apr 3, 2007
 */
public class HadoopDeployer {
    public static HadoopDeployer[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = HadoopDeployer.class;


    public static final String[] EEXCLUDED_JARS_LIST = {
            "idea_rt.jar",
            "javaws.jar",
            "jce.jar",
            "hadoop-0.20.1+152-mrunit",
            "management-agent.jar",
            "alt-rt.jar",
            "charsets.jar",
            "classes.jar",
            "jconsole.jar",
            "slf4j-api-1.4.3.jar",
            "slf4j-log4j12-1.4.3.jar",
            "jsse.jar",
            "laf.jar",
            "ui.jar",
            "testng-5.5-jdk15.jar",
            "junit-dep-4.8.1.jar",
        //    "hadoop-0.20.2-core.jar",
            "hadoop-0.20.2-tools.jar",
       //     "hadoop-core-0.20.1.jar",
        //    "commons-logging-1.1.1.jar",
       //     "commons-cli-1.2.jar",
            //     "commons-logging-1.1.1.jar",
            "slf4j-log4j12-1.4.3.jar",
            "log4j-1.2.15.jar",
            //  "xmlenc-0.52.jar",           //
            //    "commons-cli-1.2.jar",          //
            //    "commons-logging-api-1.0.4.jar",
            //    "commons-httpclient-3.0.1.jar",    //
            //    "commons-net-1.4.1.jar",           //
            //    "slf4j-api-1.4.3.jar",
            "karmasphere-client.jar",
            "servlet-api-2.5-6.1.14.jar",
            //    "commons-codec-1.3.jar",     //
      //      "commons-el-1.0.jar",
            // //   "commons-io-1.4.jar",
            //    "aws-java-sdk-1.0.005.jar",    // leave off
            "mysql-connector-java-5.0.4.jar",
            "junit-4.4.jar",
            "aws-java-sdk-1.0.12.jar",
       //     "commons-httpclient-3.1.jar",
        //    "commons-codec-1.3.jar",
            "jackson-core-asl-1.6.1.jar",
            "stax-api-1.0.1.jar",
            "jetty-6.1.25.jar",
            "jetty-util-6.1.25.jar",
         //   "servlet-api-2.5-20081211.jar",
            "servlet-api-2.5.jar",
            "log4j-1.2.9.jar",
            "junit-4.8.1.jar",
            "openjpa-persistence-2.0.0.jar",
            "openjpa-kernel-2.0.0.jar",
            "openjpa-lib-2.0.0.jar",
      //      "commons-lang-2.1.jar",
      //      "commons-collections-3.2.1.jar",
            "serp-1.13.1.jar",
            "geronimo-jms_1.1_spec-1.1.1.jar",
            "geronimo-jta_1.1_spec-1.1.1.jar",
            "commons-pool-1.5.3.jar",
            "geronimo-jpa_2.0_spec-1.0.jar",
            "jackson-core-asl-1.7.4.jar",
         //   "xml-apis-1.0.b2.jar",
        //    "xml-apis-ext-1.3.04.jar",
            "gragent.jar"
    };

    public static final Set<String> EXCLUDED_JARS = new HashSet(Arrays.asList(EEXCLUDED_JARS_LIST));

    public static void addExcludedLibrary(String lib) {
        EXCLUDED_JARS.add(lib);
    }

    public static final String HADOOP_HOME = "E:\\ThirdParty\\hadoop-0.21.0";

    private int gJarNumber = 0;
    private static boolean isQuiet;

    private static Properties gExcludedProperty;

    public static File[] filterClassPath(String[] pathItems, String javaHome) {
        readExcludedProperties();

        List holder = new ArrayList();
        for (int i = 0; i < pathItems.length; i++) {
            String item = pathItems[i];
            String jarName = new File(item).getName();
            if (".".equals(item))
                continue;
            if (EXCLUDED_JARS.contains(item))
                continue;
            if (EXCLUDED_JARS.contains(jarName))
                continue;
            if (item.indexOf(javaHome) > -1)
                continue;
            File itemFile = new File(item);
            if (!itemFile.exists())
                continue;
            if (itemFile.isFile()) {
                continue;
            }

        }

        for (int i = 0; i < pathItems.length; i++) {
            String item = pathItems[i];
            File itemFile = new File(item);
            String jarName = itemFile.getName();
            if (".".equals(item))
                continue;
            if (inExcludedJars(item))
                continue;
            if (item.indexOf(javaHome) > -1)
                continue;
            if(jarName.startsWith("hadoop"))
                 jarName = itemFile.getName();

            if (EXCLUDED_JARS.contains(item))
                continue;
            if (EXCLUDED_JARS.contains(jarName))
                continue;
            if (HADOOP_HOME.indexOf(javaHome) > -1)
                continue;
            if (!itemFile.exists())
                continue;
            if (itemFile.isFile()) {
                holder.add(itemFile);
                continue;
            }
            if (itemFile.isDirectory()) {
                continue;
            }

        }
        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    private static void readExcludedProperties() {
        try {
            gExcludedProperty = new Properties();
            gExcludedProperty.load(HadoopDeployer.class.getResourceAsStream("ExcludedLibraries.properties"));
            String prop = gExcludedProperty.getProperty("ExcludedLibraries");
            String[] props = prop.split(",");
            for (int i = 0; i < props.length; i++) {
                String s = props[i];
                EXCLUDED_JARS.add(s);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String GLOBAL_DIR = "E:\\ThirdParty";

    public static File[] filterClassPathDirectories(String[] pathItems, String javaHome) {
        List holder = new ArrayList();
        for (int i = 0; i < pathItems.length; i++) {
            String item = pathItems[i];
            if (".".equals(item))
                continue;
            if (GLOBAL_DIR.equals(item))
                continue;
            if (EXCLUDED_JARS.contains(item))
                continue;
            if (item.indexOf(javaHome) > -1)
                continue;
            File itemFile = new File(item);
            if (!itemFile.exists())
                continue;
            if (itemFile.isFile()) {
                continue;
            }
            if (itemFile.isDirectory())
                holder.add(itemFile);
        }


        File[] ret = new File[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    protected static boolean inExcludedJars(String s) {
        for (int i = 0; i < EEXCLUDED_JARS_LIST.length; i++) {
            String test = EEXCLUDED_JARS_LIST[i];
            if (s.endsWith(test))
                return true;
        }
        return false;
    }

    public static String pathToJarName(File itemFile) {
        String test = itemFile.getName();
        if ("classes".equalsIgnoreCase(test)) {
            test = itemFile.getParentFile().getName();
        }
        return test + ".jar";
    }

//    public static File makeJar(File libDir, File itemFile)
//    {
//        String jarName = pathToJarName(itemFile);
//        File jarFile = new File(libDir, jarName);
//        String cmd = "jar -cvf " + jarFile.getAbsolutePath() + " -C " + itemFile.getAbsolutePath() + " .";
//        System.out.println(cmd);
//        try {
//            Runtime.getRuntime().exec(cmd);
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return jarFile;
//    }

    public static void copyLibraries(ZipOutputStream out, File[] libs) throws IOException {
        for (int i = 0; i < libs.length; i++) {
            File lib = libs[i];
            final String name = "lib/" + lib.getName();
            System.out.println(name);
            ZipEntry ze = new ZipEntry(name);
            out.putNextEntry(ze);
            copyFile(lib, out);
            out.closeEntry();
        }
    }

    /**
     * { method
     *
     * @param dst destination file name
     * @param src source file name
     * @return true for success
     *         }
     * @name copyFile
     * @function copy file named src into new file named dst
     */
    public static boolean copyFile(File src, ZipOutputStream dst) {
        int bufsize = 1024;
        try {
            RandomAccessFile srcFile = new RandomAccessFile(src, "r");
            long len = srcFile.length();
            if (len > 0x7fffffff) {
                return (false);
            }
            // too large
            int l = (int) len;
            if (l == 0) {
                return (false);
            }
            // failure - no data

            int bytesRead = 0;
            byte[] buffer = new byte[bufsize];
            while ((bytesRead = srcFile.read(buffer, 0, bufsize)) != -1) {
                dst.write(buffer, 0, bytesRead);
            }
            srcFile.close();
            return true;
        }
        catch (IOException ex) {
            return (false);
        }
    }


    /**
     * { method
     *
     * @param TheFile name of file to create
     * @param data    date to write
     * @return true = success
     *         }
     * @name writeFile
     * @function write the string data to the file Filename
     */
    public static boolean writeFile(File TheFile, String data) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(TheFile));
            if (out != null) {
                out.print(data);
                out.close();
                return (true);
            }
            return (false);
            // failure
        }
        catch (IOException ex) {
            return (false); // browser disallows
        }
        catch (SecurityException ex) {
            return (false); // browser disallows
        }
    }


    public static void deployLibrariesToJar(File deployDir) {
        try {
            File parentFile = deployDir.getParentFile();
            if(parentFile != null)
                 parentFile.mkdirs();

            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(deployDir));

            String javaHome = System.getProperty("java.home");
            String classpath = System.getProperty("java.class.path");
            String[] pathItems = null;
            if (classpath.contains(";")) {
                pathItems = classpath.split(";");
            }
            else {
                if (classpath.contains(":")) {
                    pathItems = classpath.split(":");   // Linux stlye
                }
                else {
                    String[] items = {classpath};
                    pathItems = items; // only 1 I guess
                }
            }
            File[] pathLibs = filterClassPath(pathItems, javaHome);
            copyLibraries(out, pathLibs);
            File[] pathDirectories = filterClassPathDirectories(pathItems, javaHome);
            for (int i = 0; i < pathDirectories.length; i++) {
                File pathDirectory = pathDirectories[i];
                copyLibraryDirectory("", pathDirectory, out);
            }
            out.flush();
            out.close();

        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static String nextPath(String s, String name) {
        if (s == null || s.length() == 0)
            return name;
        return s + "/" + name;
    }

    private static void copyLibraryDirectory(final String s, final File dir, final ZipOutputStream pOut) throws IOException {
        File[] list = dir.listFiles();
        if (list == null) return;
        for (int i = 0; i < list.length; i++) {
            File file = list[i];
            if (file.isDirectory()) {
                final String np = nextPath(s, file.getName());
                copyLibraryDirectory(np, file, pOut);
            }
            else {
                final String np = nextPath(s, file.getName());
                ZipEntry ze = new ZipEntry(np);
                pOut.putNextEntry(ze);
                copyFile(file, pOut);
                pOut.closeEntry();
            }
        }
    }


    public static void makeHadoopJar(final String pJarName) {
        File deployDir = new File(pJarName);
        deployLibrariesToJar(deployDir);
    }

    public static void main(String[] args) {
        String jarName = "FooBar.jar";
        if (args.length > 0)
            jarName = args[0];
        makeHadoopJar(jarName);

    }
}