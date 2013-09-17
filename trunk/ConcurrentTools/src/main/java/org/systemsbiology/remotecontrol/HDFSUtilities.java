package org.systemsbiology.remotecontrol;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.log4j.lf5.viewer.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.HDFSUtilities
 * User: steven
 * Date: Jun 9, 2010
 */
public class HDFSUtilities {
    public static final HDFSUtilities[] EMPTY_ARRAY = {};

    public static final String FILE_NAME = "little_lamb.txt";
    public static final String BASE_DIRECTORY = "/tmp/sanity_check/";

    public static final String TEST_CONTENT =
            "Mary had a little lamb,\n" +
                    "little lamb, little lamb,\n" +
                    "Mary had a little lamb,\n" +
                    "whose fleece was white as snow.\n" +
                    "And everywhere that Mary went,\n" +
                    "Mary went, Mary went,\n" +
                    "and everywhere that Mary went,\n" +
                    "the lamb was sure to go.";

    /**
     * make sure you cam access an hdfs on
     * @param nameNode !null name node
     * @param port   posr for hdfs
     * @throws  IllegalStateException or error
     */
    public static  void hDFSSanityTest(String nameNode, int port) {
        IHDFSFileSystem access = HDFSAccessor.getFileSystem(nameNode, port);
        String filePath = BASE_DIRECTORY + FILE_NAME;
        access.guaranteeDirectory(BASE_DIRECTORY);
        access.writeToFileSystem(filePath, TEST_CONTENT);
        String result = access.readFromFileSystem(filePath);

        if (!TEST_CONTENT.equals(result)) {
            throw new IllegalStateException("The combination " + nameNode + ":" + port +
              " is not an accessible hdfs system" );
        }

        access.deleteFile(filePath);


    }


    private static boolean gOutputDirectoriesPrecleared;

    public static boolean isOutputDirectoriesPrecleared() {
        return gOutputDirectoriesPrecleared;
    }

    public static void setOutputDirectoriesPrecleared(final boolean pOutputDirectoriesPrecleared) {
        gOutputDirectoriesPrecleared = pOutputDirectoriesPrecleared;
    }

    /*
       Name	Type	Description
       mapred.job.id	String	The job id
       mapred.jar	String	job.jar location in job directory
       job.local.dir	 String	 The job specific shared scratch space
       mapred.tip.id	 String	 The task id
       mapred.task.id	 String	 The task attempt id
       mapred.task.is.map	 boolean	Is this a map task
       mapred.task.partition	 int	The id of the task within the job
       map.input.file	 String	 The filename that the map is reading from
       map.input.start	 long	 The offset of the start of the map input split
       map.input.length	long	The number of bytes in the map input split
       mapred.work.output.dir	 String	The task's temporary output directory
    */

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * read a properties file from the data
     *
     * @param ctx      !null context
     * @param fullPath path to file
     * @return !null properties
     */
    public static Properties getHDFSProperties(TaskInputOutputContext ctx, String fullPath) {
        ByteArrayInputStream inp = new ByteArrayInputStream(getHDFSBytes(ctx, fullPath));
        Properties ret = new Properties();
        try {
            ret.load(inp);
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        return ret;
    }

    /**
     * NOTE DO NOT USE IF YOU CANNOT HOLD THE ENTIRE FILE IN MEMORY!!!
     * read ab HDFS file and turn it to an input Stream
     *
     * @param ctx      !null context
     * @param fullPath path to file
     * @return !null InputStream
     */
    public static InputStream getHDFSInputStream(TaskInputOutputContext ctx, String fullPath) {
        return new ByteArrayInputStream(getHDFSBytes(ctx, fullPath));
    }


    /**
     * read text from hdfs assuming it is a text file  NOTE DO NOT USE IF YOU CANNOT HOLD THE ENTIRE FILE IN MEMORY!!!
     *
     * @param ctx      !null context
     * @param fullPath path to file
     * @return !null String
     */
    public static String getHDFSText(TaskInputOutputContext ctx, String fullPath) {
        return new String(getHDFSBytes(ctx, fullPath));
    }

    /**
     * read data from hdfs NOTE DO NOT USE IF YOU CANNOT HOLD THE ENTIRE FILE IN MEMORY!!!
     *
     * @param ctx      !null context
     * @param fullPath path to file
     * @return !null array of bytes
     */
    public static byte[] getHDFSBytes(TaskInputOutputContext ctx, String fullPath) {
        byte[] data = new byte[DEFAULT_BUFFER_SIZE];
        ByteArrayOutputStream inp = new ByteArrayOutputStream();
        FileSystem dfs = getFileSystem(ctx);
        StringBuilder sb = new StringBuilder();
        try {
            Path src = new Path(fullPath);
            FSDataInputStream fs = dfs.open(src);
            int read = fs.read(data);
            while (read >= 0) {
                //sendMessage(ctx," read  " + read + " length " + data.length);
                if (read > 0)
                    inp.write(data, 0, read);
                read = fs.read(data);
            }
            fs.close();
            return inp.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * NOTE ONLY GOOD FOR A TEXT,INT context
     *
     * @param ctx
     * @param txt
     */
    public static void sendMessage(TaskInputOutputContext ctx, String txt) {
        IntWritable result = new IntWritable(1);
        Text key = new Text(txt);
        try {
            ctx.write(key, result);
        }
        catch (IOException e1) {
            throw new RuntimeException(e1);

        }
        catch (InterruptedException e1) {
            throw new RuntimeException(e1);

        }

    }


    /**
     * read lines from hdfs NOTE DO NOT USE IF YOU CANNOT HOLD THE ENTIRE FILE IN MEMORY!!!
     *
     * @param ctx      !null context
     * @param fullPath path to file
     * @return !null array of Strings
     */
    public static String[] getHDFSLines(TaskInputOutputContext ctx, String fullPath) {
        return getHDFSText(ctx, fullPath).split("\n");
    }

    public static FileSystem getFileSystem(final TaskInputOutputContext ctx) {
        try {
            Configuration cnfg = ctx.getConfiguration();
            FileSystem dfs = FileSystem.get(cnfg);
            return dfs;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    /**
//     * retuen an empty directory on the hadoop fs -
//     * this is needed by jobs
//     *
//     * @param outputDirectory
//     * @param ech
//     * @return
//     */
//    public static String getEmptyOutputDirectory(String outputDirectory, ExecChannel ech) {
//        if (isOutputDirectoriesPrecleared()) {
//            return outputDirectory;
//        }
//        String base = outputDirectory + "/output";
//        String command = RemoteHadoopController.HADOOP_COMMAND + "fs -ls " + outputDirectory;
//        String currentFiles = ech.execCommand(command, IOutputListener.DEFAULT_LISTENERS);
//        int index = 1;
//        while (true) {
//            String test = base + index++;
//            if (!currentFiles.contains(test)) {
//                return test;
//            }
//        }
//    }
    /**
     * retuen an empty directory on the hadoop fs -
     * this is needed by jobs
     *
     * @param outputDirectory
     * @param ech
     * @return
     */
    public static String getEmptyOutputDirectory(String outputDirectory, IFileSystem ech) {
        if (isOutputDirectoriesPrecleared()) {
            return outputDirectory;
        }
        String base = outputDirectory + "/output";

        int index = 1;
        while (true) {
            String test = base + index++;
            if (!ech.exists(test)) {
                return test;
            }
        }
    }


    /**
     * return the size of a file or the total size of a directory
     * @param file  !null file might not exist
     * @param fs !null file system
     * @return  size of the file - 0 if it does not exist or total size of files in directory
     */
    public static long size(String file, IFileSystem fs) {
        if(!fs.exists(file))
             return 0;
        if(!fs.isDirectory(file))
             return fs.fileLength(file);
        final String[] files   = fs.ls(file);
        long ret = 0;
        for (int i = 0; i < files.length; i++) {
            String s = file + "/" + files[i];
            ret += size(s,fs);

        }
        return ret;
    }


    public static String parentDirectory(String file) {
        int index = file.lastIndexOf("/");
        if (index == -1)
            return null;
        return file.substring(0, index);
    }


    public static String baseFile(final String pFileName) {
        int index = pFileName.lastIndexOf("/");
        if (index == -1) {
            return pFileName;
        }
        return pFileName.substring(index + 1);
    }


    public static void main(String[] args) {
        hDFSSanityTest("glados",9000);
    }


}
