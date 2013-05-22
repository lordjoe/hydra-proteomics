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
    public static final String FILE_NAME = "little_lamb2.txt";
    public static final String FILE_NAME2 = "little_lamb_stays2.txt";

    @Test
    public void setPermissionTest()
    {
       // We can tell from the code - hard wired to use security over 0.2
       // HDFSAccessor.setHDFSHasSecurity(true);

         if(!HDFWithNameAccessor.isHDFSAccessible())  {
             System.out.println("Not running HDFS tests");
             return;
         }
         try {
             HDFWithNameAccessor access = (HDFWithNameAccessor)HDFSAccessor.getFileSystem(NAME_NODE,HDFS_PORT);
        //     access.setPermissions(new Path("/user/slewis"),IHDFSFileSystem.FULL_ACCESS);

      //       access.mkdir(BASE_DIRECTORY + "/ebi/");
       //      access.mkdir(BASE_DIRECTORY + "/ebi/Sample2/");
             FsPermission permissions ;


             Path src = new Path("/user/slewis/ebi");
             access.expunge("/user/slewis/ebi");

             access.mkdir("/user/slewis/ebi");
            access.setPermissions(src,IHDFSFileSystem.FULL_ACCESS);
             permissions = access.getPermissions(src);
             Assert.assertTrue(HDFWithNameAccessor.canAllRead(permissions));

             String filePath = RemoteUtilities.getDefaultPath() + "/ebi/Sample2/";
             access.mkdir(filePath);
             src = new Path(filePath);
             permissions = access.getPermissions(src);

             access.setPermissions(src,IHDFSFileSystem.FULL_ACCESS);
             permissions = access.getPermissions(src);


             access.writeToFileSystem(filePath, TEST_CONTENT);
             access.setPermissions(src,IHDFSFileSystem.FULL_ACCESS);
             access.setPermissions(new Path(filePath),IHDFSFileSystem.FULL_ACCESS);

              permissions = access.getPermissions("/user/slewis");
             Assert.assertTrue(HDFWithNameAccessor.canAllRead(permissions));

              permissions = access.getPermissions("/user/slewis");
             Assert.assertTrue(HDFWithNameAccessor.canAllRead(permissions));

              permissions = access.getPermissions(filePath);
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
                    "the lamb was sure to go." +
                    "He followed her to schoool one day";




    @Test
     public void versionTest()
     {
         HadoopMajorVersion mv = HadoopMajorVersion.CURRENT_VERSION;
         Assert.assertEquals(HadoopMajorVersion.Version1,mv);
       }

   @Test
    public void HDFSReadTest()
    {

       // We can tell from the code - hard wired to use security over 0.2
      //  HDFSAccessor.setHDFSHasSecurity(true);



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
            Assert.assertTrue(access.exists(filePath));


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
