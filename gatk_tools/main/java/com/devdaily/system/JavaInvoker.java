package com.devdaily.system;

import java.io.*;
import java.util.*;

/**
 * com.devdaily.system.JavaInvoker
 * User: steven
 * Date: 7/25/12
 */
public class JavaInvoker {
    public static final JavaInvoker[] EMPTY_ARRAY = {};
    public static final long ONE_MEG = 1024 * 1024;
    public static final long ONE_GIG  = ONE_MEG * 1024;
     public static final long MINIMUM_MEMORY = 250 * ONE_MEG;

    /**
       * invoke a main with the current classpath in a new process - may be needed when some programmer is so
       * sloppy as to call System.exit() in his main this version asks for 250 Mb (MINIMUM_MEMORY)
       * @param mainClassName  !null full name of a class wiht a main
        * @param args  arguments
       * @return  0 for success 1 for failure (exception)
       */
    public static int invokeInNewProcess(String mainClassName,   String... args) {
         return invokeInNewProcess( mainClassName,MINIMUM_MEMORY,  args);
    }

    /**
     * invoke a main with the current classpath in a new process - may be needed when some programmer is so
     * sloppy as to call System.exit() in his main
     * @param mainClassName  !null full name of a class wiht a main
     * @param memory memory in bytes
     * @param args  arguments
     * @return  0 for success 1 for failure (exception)
     */
    public static int invokeInNewProcess(String mainClassName, long memory, String... args) {
        try {
// build the system command we want to run
            memory = Math.max(memory, MINIMUM_MEMORY);
            String memoryStr = "-Xmx" + memory / ONE_MEG + "m";
            List<String> commands = new ArrayList<String>();
            commands.add("java");
            commands.add(memoryStr);
            commands.add("-cp");
            String classPath = System.getProperty("java.class.path");
            commands.add(classPath);
            commands.add(mainClassName);
            commands.addAll(Arrays.asList(args));

            // execute the command
            SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
            int result = commandExecutor.executeCommand();

            // get the stdout and stderr from the command that was run
            StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
            StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

            // print the stdout and stderr
            if (stdout.length() > 0)
                System.out.println(stdout);
            if (stderr.length() > 0)
                System.err.println(stderr);
            return result;
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * test code to show use
     * @param args
     */
    public static void main(String[] args) {
         int result = invokeInNewProcess(TestMain.class.getName(), 2 *JavaInvoker.ONE_GIG, "Fee","Fie","Foe","Fum" )  ;
        System.out.println("Result = " + result);
        // should return result 1 when an exception is thrown
        result = invokeInNewProcess(TestMainWithException.class.getName(), 2 *JavaInvoker.ONE_GIG, "Fee","Fie","Foe","Fum" )  ;
        System.out.println("Result = " + result);
    }

}
