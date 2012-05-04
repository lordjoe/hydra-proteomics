package org.systemsbiology.remotecontrol;

import com.lordjoe.utilities.*;
import org.systemsbiology.chromosome.*;

import java.io.*;
import java.util.*;
import java.util.prefs.*;

/**
 * org.systemsbiology.remotecontrol.RemoteUtilities
 * User: steven
 * Date: Jun 4, 2010
 */
public class RemoteUtilities {
    public static final RemoteUtilities[] EMPTY_ARRAY = {};

    public static final String EDIT_REMOTE_PRPOERTIES =
            "You will need to configure asccess properties java  org.systemsbiology.remotecontrol.RemoteUtilities user=<user_name> password=<password> path=<hdfs_path> \n" +
                    " - user should be the name of the user on the hadoop main system \n" +
                    " - password should be the password of the user on the hadoop main system \n" +
                    " - host should be the name of the  hadoop main system  " +
                    " - path should be the name of the a path on hdfs  " +
                    " - port should be the port of  hdfs - 9000 is the default " +
                    " - hadoop_home should be the value of what you get when you say echo $HADOOP_HOME  " +
                           "";


    public static final String USER_STRING = "user";
    public static final String PASSWORD_STRING = "password";
    public static final String HOST_STRING = "host";
    public static final String JOB_TRACKER_STRING = "jobtracker";
    public static final String PORT_STRING = "port";
    public static final String HADOOP_HOME = "hadoop_home";
    public static final String DEFAULT_PATH_STRING = "default_path";

 //   private static Properties gAccessProperties;


    private static void validateProperties(final Properties pP) {
        if ( getProperty(USER_STRING) == null)
            throw new IllegalStateException(EDIT_REMOTE_PRPOERTIES);
        if ( getProperty(PASSWORD_STRING) == null)
            throw new IllegalStateException(EDIT_REMOTE_PRPOERTIES);
        if ( getProperty(HOST_STRING) == null)
            throw new IllegalStateException(EDIT_REMOTE_PRPOERTIES);
        if ( getProperty(DEFAULT_PATH_STRING) == null)
            throw new IllegalStateException(EDIT_REMOTE_PRPOERTIES);
        if ( getProperty(PORT_STRING) == null)
            throw new IllegalStateException(EDIT_REMOTE_PRPOERTIES);
    }




    public static void setProperty(String name,String value)
    {
        Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
        if(PASSWORD_STRING.equals(name))
            value = Encrypt.encryptString(value);
          prefs.put(name, value);

    }

    public static String getProperty(String name)
    {
        Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
        String BadValue = "Set " + name;
        String s = prefs.get(name, BadValue);
        if(BadValue.equals(s))
            return null;
        return s;
    }


    public static String getUser() {
        return  getProperty(USER_STRING);
     }

    public static String getPassword() {
        String encrypted = getProperty(PASSWORD_STRING);
        if(encrypted == null)
            return null;
        return Encrypt.decryptString(encrypted);
     }

    public static String getHost() {
         return  getProperty(HOST_STRING);
       }
    public static String getJobTracker() {
         return  getProperty(JOB_TRACKER_STRING);
       }

    public static int getPort() {
        return  Integer.parseInt(getProperty(PORT_STRING));
    }

    public static String getDefaultPath() {
        return  getProperty(DEFAULT_PATH_STRING);

    }




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
            "hadoop-0.20.2-core.jar",
            "hadoop-0.20.1+152-mrunit.jar",
            "hadoop-0.20.2-tools.jar",
            "hadoop-core-0.20.1.jar",
            "commons-logging-1.1.1.jar",
            "commons-cli-1.2.jar",
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
            "commons-el-1.0.jar",
            // //   "commons-io-1.4.jar",
            //    "aws-java-sdk-1.0.005.jar",    // leave off
            "junit-4.4.jar",
            "junit-4.8.1.jar"
    };

    public static final Set<String> EXCLUDED_JARS = new HashSet(Arrays.asList(EEXCLUDED_JARS_LIST));

    private int gJarNumber = 0;
    private static boolean isQuiet;

    public static File[] filterClassPath(String[] pathItems, String javaHome) {
        List holder = new ArrayList();
        for (int i = 0; i < pathItems.length; i++) {
            String item = pathItems[i];
            if (".".equals(item))
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

        }

        for (int i = 0; i < pathItems.length; i++) {
            String item = pathItems[i];
            if (".".equals(item))
                continue;
            if (inExcludedJars(item))
                continue;
            if (item.indexOf(javaHome) > -1)
                continue;
            File itemFile = new File(item);
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


    protected static boolean inExcludedJars(String s) {
        for (int i = 0; i < EEXCLUDED_JARS_LIST.length; i++) {
            String test = EEXCLUDED_JARS_LIST[i];
            if (s.endsWith(test))
                return true;
        }
        return false;
    }

    public static void handlePreference(final Preferences prefs, String key, String value) {
        if ("path".equals(key)) {
            key = DEFAULT_PATH_STRING;
        }
        if (PASSWORD_STRING.equals(key)) {
            value = Encrypt.encryptString(value);
            System.out.println("Encrypted Password:" + value);
        }
        if (key.endsWith("_root_password")) {
            value = Encrypt.encryptString(value);
        }
        prefs.put(key, value);
    }

    private static void buildPreferences(Preferences prefs, final String[] args) {
        if (args.length == 0)
            throw new IllegalStateException("usage user=<user_name> password=<password> host=<hadoop_launcher>");
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            String[] items = arg.split("=");
            if (items.length != 2)
                throw new IllegalStateException("bad property spec " + arg);
            handlePreference(prefs, items[0], items[1]);
        }
    }

    private static boolean testPreferences(Preferences prefs) {
        String user = getProperty(USER_STRING);
        if (user == null || user.length() == 0  )
            return false;
        String password = getProperty(PASSWORD_STRING);
        if (password == null || password.length() == 0  )
            return false;
        String host = getProperty(HOST_STRING);
        if (host == null || host.length() == 0  )
            return false;
        String path = getProperty(DEFAULT_PATH_STRING);
        if (path == null || path.length() == 0  )
            return false;
        String port = getProperty(PORT_STRING);
        if (port == null || port.length() == 0  )
            return false;
        return true;
    }

    protected static void usage() {
        System.out.println("Usage:");
        System.out.println("java ... org.systemsbiology.remotecontrol.RemoteUtilities host=glados jobtracker=glados:9001 port=9000 user=slewis password=secret path=/users/hdfs");
        System.out.println("or any subset of the above");
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            usage();
            return;
        }

        Preferences prefs = Preferences.userNodeForPackage(RemoteUtilities.class);
  //      if (!testPreferences(prefs))
         buildPreferences(prefs, args);
        String home = getProperty("hadoop_home");
        String host = getHost();
        String password = getPassword();
        String user = getUser();
        String path = getDefaultPath();
        String jobtracker = getJobTracker();
          int port = getPort();
        }


}
