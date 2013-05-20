package org.systemsbiology.hadoop;

import org.apache.hadoop.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.*;
import org.junit.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.xtandem.*;

import java.io.*;
import java.lang.annotation.*;

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
    public static final String FILE_NAME2 = "little_lamb_stays.txt";

   // @Test
    public void setPermissionTest()
    {
        HDFSAccessor.setHDFSHasSecurity(true);

         if(!HDFWithNameAccessor.isHDFSAccessible())  {
             System.out.println("Not running HDFS tests");
             return;
         }
         try {
             HDFWithNameAccessor access = (HDFWithNameAccessor)HDFSAccessor.getFileSystem(NAME_NODE,HDFS_PORT);
             access.setPermissions(new Path("/user/slewis"),IHDFSFileSystem.FULL_ACCESS);

      //       access.mkdir(BASE_DIRECTORY + "/ebi/");
       //      access.mkdir(BASE_DIRECTORY + "/ebi/Sample2/");

             String filePath = BASE_DIRECTORY + "/ebi/Sample2/";
             access.mkdir(BASE_DIRECTORY + "/ebi/Sample2/");
             access.writeToFileSystem(filePath, TEST_CONTENT);

             FsPermission permissions = access.getPermissions("/user/slewis");
             Assert.assertTrue(HDFWithNameAccessor.canAllRead(permissions));
         }
        catch (Exception e) {
            Throwable cause = XTandemUtilities.getUltimateCause(e);
            if (cause instanceof EOFException) {   // hdfs not available
                return;
            }
            throw new RuntimeException(e);

        }
    }

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
     public void versionTest()
     {
         HadoopMajorVersion mv = HadoopUtilities.HADOOP_MAJOR_VERSION;
         Assert.assertEquals(HadoopMajorVersion.Version1,mv);
       }

   @Test
    public void HDFSReadTest()
    {


        HDFSAccessor.setHDFSHasSecurity(true);



        if(!JXTandemTestConfiguration.isHDFSAccessible())  {
            System.out.println("Not running HDFS tests");
            return;
        }
        try {
            IHDFSFileSystem access = HDFSAccessor.getFileSystem(NAME_NODE,HDFS_PORT);
            access.guaranteeDirectory(BASE_DIRECTORY);

            String filePath = BASE_DIRECTORY + FILE_NAME;
             access.writeToFileSystem(filePath,TEST_CONTENT);
            String result = access.readFromFileSystem(filePath );

            Assert.assertEquals(result, TEST_CONTENT);

            Assert.assertTrue(access.exists(filePath));

            access.deleteFile(filePath);
            Assert.assertFalse(access.exists(filePath));

              filePath = BASE_DIRECTORY + FILE_NAME2;
            access.writeToFileSystem(filePath,TEST_CONTENT);


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
