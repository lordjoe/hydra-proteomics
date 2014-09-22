package org.systemsbiology.aws;

import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.filecache.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;
import org.systemsbiology.hadoop.*;

import java.io.*;
import java.net.*;

/**
 * org.systemsbiology.aws.CapabilitiesTester
 * User: steven
 * Date: Aug 16, 2010
 */
public class CapabilitiesTester {
    public static final CapabilitiesTester[] EMPTY_ARRAY = {};

    private static class CapabilitiesTestReducer extends OneShotReducer<Text, Text, Text, Text> {
        @Override
        protected void processOnce(final Context context) {
            String value = listDistributedFiles(context);
            safeWrite(new Text("LocalCacheFiles"), new Text(value), context);

            value = listCacheFiles(context);
            safeWrite(new Text("CacheFiles"), new Text(value), context);

            Configuration configuration = context.getConfiguration();

            try {
                String s3file = "s3n://lordjoe/FeeFie.txt";
                Path path = new Path(s3file);
                FileSystem s3fs = path.getFileSystem(configuration);
                InputStream is = s3fs.open(path);
                String fee_fie = FileUtilities.readInFile(is);
                safeWrite(new Text("FEE_FIE_FILE"), new Text(fee_fie), context);

            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }


        }
    }


    protected static String listDistributedFiles(final Reducer.Context context) {
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
            }
            else {
                sb.append("No Local Files");
            }
            return sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


    protected static String listCacheFiles(final Reducer.Context context) {
        try {
            StringBuilder sb = new StringBuilder();
            Configuration configuration = context.getConfiguration();

            LocalFileSystem system = FileSystem.getLocal(configuration);
            URI[] files = DistributedCache.getCacheFiles(configuration);
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    URI file = files[i];

                    sb.append("URI is " + file.toString());
                }
            }
            else {
                sb.append("No CacheFiles");
            }
            return sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }


    public static void main(String[] args) throws Exception {
        Job job = OneKeyValueInputFormat.buildOneShotJob("CapabilitiesTest",CapabilitiesTestReducer.class);

         Path outputDir = new Path("TestJob");
        FileOutputFormat.setOutputPath(job, outputDir);

        String s = "s3n://lordjoe/Hello";
        Configuration conf  = job.getConfiguration();
        DistributedCache.addCacheFile(new URI(s + "#Hello.x"), conf);
        DistributedCache.addCacheFile(new URI("s3n://lordjoe/FeeFie.txt#FeeFie.txt"), conf);



        boolean ans = job.waitForCompletion(true);
        int ret = ans ? 0 : 1;
        System.exit(0);

    }


}
