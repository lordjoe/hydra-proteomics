package org.systemsbiology.hadoop;

import org.junit.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.xtandem.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.HDFSTests
 * User: steven
 * Date: 3/9/11
 */
public class HDFSTests {
    public static final HDFSTests[] EMPTY_ARRAY = {};

    public static final String NAME_NODE = RemoteUtilities.getHost();
    public static final int HDFS_PORT = RemoteUtilities.getPort();
      public static final String BASE_DIRECTORY = RemoteUtilities.getDefaultPath() + "/test/";
    public static final String FILE_NAME = "little_lamb.txt";

    public static final String TEST_CONTENT =
            "Mary had a little lamb,\n" +
                    "little lamb, little lamb,\n" +
                    "Mary had a little lamb,\n" +
                    "whose fleece was white as snow.\n" +
                    "And everywhere that Mary went,\n" +
                    "Mary went, Mary went,\n" +
                    "and everywhere that Mary went,\n" +
                    "the lamb was sure to go.";


   @Test
    public void HDFSReadTest()
    {
        if(!JXTandemTestConfiguration.isHDFSAccessible())  {
            System.out.println("Not running HDFS tests");
            return;
        }
        try {
            IHDFSFileSystem access = HDFSAccessor.getFileSystem(NAME_NODE,HDFS_PORT);
            String filePath = BASE_DIRECTORY + FILE_NAME;
            access.guaranteeDirectory(BASE_DIRECTORY);
            access.writeToFileSystem(filePath,TEST_CONTENT);
            String result = access.readFromFileSystem(filePath );

            Assert.assertEquals(result, TEST_CONTENT);

            Assert.assertTrue(access.exists(filePath));

            access.deleteFile(filePath);

            Assert.assertFalse(access.exists(filePath));
        }
        catch (Exception e) {
            Throwable cause = XTandemUtilities.getUltimateCause(e);
            if (cause instanceof EOFException) {   // hdfs not available
                return;
            }
            throw new RuntimeException(e);

        }

    }

}
