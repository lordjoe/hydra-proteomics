package org.systemsbiology.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.filecache.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;

import java.io.*;
import java.util.*;


/**
 * org.systemsbiology.hadoop.HelloWorldExecutableReducer
 * User: steven
 * Date: Jul 30, 2010
 */
public class HelloWorldExecutableReducer extends AbstractExecutableReducer<Text, Text, Text, Text> {
    public static final HelloWorldExecutableReducer[] EMPTY_ARRAY = {};

    public static final String HELLO_WORLD_PROGRAM =
            "#include <iostream>\n" +
                    "\n" +
                    "int main ()\n" +
                    "{\n" +
                    "  std::cout << \"Hello World!\\n\";\n" +
                    "  return 0;\n" +
                    "}\n";

    public static final String TARGET_SOURCE_FILE = "HelloWorld.cc";

    public static final String PROGRAM_RESOURCE = "MotifLocator_linux.x";
    public static final String PROGRAM_FILE = "MotifLocator_linux";

    public static final String HELLO_WORLD_PERL =
            "#!/usr/bin/perl\n" +
                    "\n" +
                    "print \"Hello world from Perl!\";";

    public static final String HELLO_WORLD_PERL_FILE = "HelloWorld.pl";

    private String m_ExecName;

    public HelloWorldExecutableReducer() {
    }

    public String getExecName() {
        return m_ExecName;
    }

    public void setExecName(final String pExecName) {
        m_ExecName = pExecName;
    }

    /**
     * make sure that the executable exists
     */
    @Override
    protected void guaranteeExecutable(final Context context) {
        Configuration configuration = context.getConfiguration();
        if (configuration == null)
            System.err.println("Configuration is null");
        String athname = System.getProperty("user.dir");
        System.err.println("user.dir " + athname);

        File directory = new File(athname);
        try {
            LocalFileSystem localFs = FileSystem.getLocal(configuration);
            if (HadoopUtilities.isWindows()) {
                String s = "HelloCPP.exe";
                setExecName(s);
                HadoopUtilities.writeResourceAsFile(getClass(), "HelloCPP.exe", localFs, s);
            } else {
                if (HadoopUtilities.isLinux()) {
                    HadoopUtilities.writeStringAsFile(localFs, TARGET_SOURCE_FILE, HELLO_WORLD_PROGRAM);
                    HadoopUtilities.writeStringAsFile(localFs, HELLO_WORLD_PERL_FILE, HELLO_WORLD_PERL);

                    String s = PROGRAM_FILE;
                    String prop = directory.getAbsolutePath();
                    System.err.println("Directory path " + prop);
                    HadoopUtilities.writeResourceAsFile(getClass(), PROGRAM_RESOURCE, localFs, s);
                    setExecName(s);
                    File file = localFs.pathToFile(new Path(s));
                    String path = file.getAbsolutePath();
                    System.err.println("Exe path " + prop);
//                    int ret = FileUtil.chmod(path, "a+x");
                    //Runtime.getRuntime().exec("chmod a+x Hello");
                }
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException(e);

        }
    }

    /**
     * This method is called once for each key. Most applications will define
     * their reduce class by overriding this method. The default implementation
     * is an identity function.
     */
    @Override
    protected void reduce(final Text key, final Iterable<Text> values, final Context context) throws IOException, InterruptedException {

        super.reduce(key, values, context);    //To change body of overridden methods use File | Settings | File Templates.
    }


    protected String listDistributedFiles(final Context context) {
        try {
            StringBuilder sb = new StringBuilder();
            Configuration configuration = context.getConfiguration();

            LocalFileSystem system = FileSystem.getLocal(configuration);
            Path[] files = DistributedCache.getLocalCacheFiles(configuration);
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    Path file = files[i];
                    File file1 = system.pathToFile(file);
                    sb.append("Path is " + file1.getAbsolutePath());
                }
            } else {
                sb.append("No Local Files");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * invoke the exe in some manner
     *
     * @param key
     * @param context
     * @param otherArgs
     * @return
     */
    @Override
    protected String callExecutable(final Text key, final Text value, final Context context, final Object... otherArgs) {
        try {
            Configuration configuration = context.getConfiguration();
            LocalFileSystem system = FileSystem.getLocal(configuration);

            StringBuilder sb = new StringBuilder();
            String files = listDistributedFiles(context);
            sb.append(files);

//            Properties properties = System.getProperties();
//            for(String ky : properties.stringPropertyNames())   {
//                String s = properties.getProperty(ky);
//                sb.append(ky + "=" + s + "\n");
//            }

            String e = getExecName();
            System.err.println("Calling exec" + e);
            File f = system.pathToFile(new Path(e));


            String apath = f.getAbsolutePath();
            runProcess(sb, f, "g++", "-v");
            runProcess(sb, f, "g++", TARGET_SOURCE_FILE, "-o", "HelloWorld");

            runProcess(sb, f, "./HelloWorld");
            //     runProcess(sb, f, "ls","-l");
            //       runProcess(sb, f, "chmod","a+x","Hello");
            runProcess(sb, f, "ls", "-l");

            runProcess(sb, f, "perl", HELLO_WORLD_PERL_FILE);


//            sb.append("\n");
//            sb.append(apath);
//            sb.append("\nCalling " + apath + (f.exists() ? " exists" : " not there"));
//
            runProcess(sb, f, "./" + PROGRAM_FILE);

            runProcess(sb, f, "ps", "-a");
            sb.append("\n");

            Text t = new Text();
            String val = sb.toString();
            t.set(val);
            context.write(key, t);
            return val;

        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException(e);

        }
    }

    private void runProcess(final StringBuilder pSb, final File pF, String... pCommand) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(pCommand);
        pSb.append("\n");
        for (int i = 0; i < pCommand.length; i++) {
            String s = pCommand[i];
            pSb.append(s);
            pSb.append(" ");
        }
        pSb.append("\n");

        Map<String, String> environ = builder.environment();

        //       File directory = pF.getParentFile();
        //       builder.directory(directory);

        final Process process = builder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        LineNumberReader br = new LineNumberReader(isr);
        String line;

        int i1 = process.waitFor();
        int i = process.exitValue();
        pSb.append("\nwait for " + i1 + " exit " + 1);
        while ((line = br.readLine()) != null) {
            if (pSb.length() > 0) pSb.append("\n");
            pSb.append(line);
        }

        pSb.append("\n");
        pSb.append("errors\n");

        InputStream iserr = process.getErrorStream();
        InputStreamReader isrer = new InputStreamReader(iserr);
        LineNumberReader brerr = new LineNumberReader(isrer);
        while ((line = brerr.readLine()) != null) {
            if (pSb.length() > 0) pSb.append("\n");
            pSb.append(line);
        }

        pSb.append("\n");

    }
//
//    private void runProcess2(final StringBuilder pSb, final File pF, final List<String> pCommand) throws IOException, InterruptedException {
//        ProcessBuilder builder = new ProcessBuilder(pCommand);
//        pSb.append("\n");
//        pSb.append(pCommand);
//
//        Map<String, String> environ = builder.environment();
//
//        File directory = pF.getParentFile();
//        builder.directory(directory);
//
//        final Process process = builder.start();
//        InputStream is = process.getInputStream();
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//        String line;
//
//        int i1 = process.waitFor();
//        int i = process.exitValue();
//        pSb.append("\nwait for " + i1 + " exit " + 1);
//        while ((line = br.readLine()) != null) {
//            if (pSb.length() > 0) pSb.append("\n");
//            pSb.append(line);
//        }
//    }
}
