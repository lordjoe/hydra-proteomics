package org.systemsbiology.hadoop.consolidator;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.s3.*;
import org.apache.hadoop.mapreduce.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.consolidator.ConsolidatorUtilities
 *
 * @author Steve Lewis
 * @date Oct 23, 2010
 */
public class ConsolidatorUtilities {
    public static ConsolidatorUtilities[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ConsolidatorUtilities.class;

    public static FileSystem buildFileSystem(String name, TaskInputOutputContext context) {
        try {
            if ("s3".equals(name)) {
                final FileSystem fileSystem = new S3FileSystem(null);
                return fileSystem;
            }
            if ("local".equals(name)) {
                final FileSystem fileSystem = new LocalFileSystem();
                return fileSystem;
            }

            return FileSystem.get(context.getConfiguration());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
