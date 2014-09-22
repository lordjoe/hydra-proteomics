package org.systemsbiology.remotecontrol;


import org.junit.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;

import java.util.*;

/**
 * org.systemsbiology.remotecontrol.FTPWrapperTest
 * User: steven
 * Date: 5/12/11
 */
public class FTPWrapperTest {
    public static final FTPWrapperTest[] EMPTY_ARRAY = {};

    public static final String TEST_CONTENT =
            "Mary had a little lamb,\n" +
                    "little lamb, little lamb,\n" +
                    "Mary had a little lamb,\n" +
                    "whose fleece was white as snow.\n" +
                    "And everywhere that Mary went,\n" +
                    "Mary went, Mary went,\n" +
                    "and everywhere that Mary went,\n" +
                    "the lamb was sure to go.";


    //   @Test
    public void testLS() {
        if (!RemoteTestConfiguration.isHDFSAccessible())
            return;
        IFileSystem wrapper = buildFTPWrapper();
        String[] items = wrapper.ls("/");
        Assert.assertTrue(items.length > 30);
        Set<String> fileSet = new HashSet<String>(Arrays.asList(items));
        Assert.assertTrue(fileSet.contains("usr"));

        ((FTPWrapper) wrapper).exit();
    }

    //   @Test
    public void testGuaranteeDirectory() {
        if (!RemoteTestConfiguration.isHDFSAccessible())
            return;
        IFileSystem fs = buildFTPWrapper();
        String myHome = fs.pwd();
        String newDir = myHome + "/foo";
        if (!fs.exists(newDir))
            fs.guaranteeDirectory(newDir);
        Assert.assertTrue(fs.exists(newDir));
        Assert.assertTrue(fs.isDirectory(newDir));

        fs.expunge(newDir);
        Assert.assertFalse(fs.exists(newDir));

        fs.guaranteeDirectory(newDir);
        fs.writeToFileSystem(newDir + "/mary.txt", TEST_CONTENT);

        String[] files = fs.ls(newDir);
        Assert.assertEquals(1, files.length);
        Assert.assertEquals(files[0], "mary.txt");
        fs.expunge(newDir);
        Assert.assertFalse(fs.exists(newDir));


        ((FTPWrapper) fs).exit();
    }

    //  @Test
    public void testFileReadWrite() {
        if (!RemoteTestConfiguration.isHDFSAccessible())
            return;
        IFileSystem fs = buildFTPWrapper();
        String myHome = fs.pwd();
        String newDir = myHome + "/mary.txt";
        if (fs.exists(newDir))
            fs.deleteFile(newDir);
        Assert.assertFalse(fs.exists(newDir));
        fs.writeToFileSystem(newDir, TEST_CONTENT);
        Assert.assertTrue(fs.exists(newDir));
        String copyContent = fs.readFromFileSystem(newDir);
        Assert.assertEquals(TEST_CONTENT, copyContent);


        fs.deleteFile(newDir);
        Assert.assertFalse(fs.exists(newDir));
        Assert.assertFalse(fs.exists(newDir));

        ((FTPWrapper) fs).exit();
    }

    //       @Test
    public void testHDFSLS() {
        if (!RemoteTestConfiguration.isHDFSAccessible())
            return;
        IFileSystem fs = HDFSAccessor.getFileSystem();
        String[] items = fs.ls("/");
        Assert.assertTrue(items.length > 3);
        Set<String> fileSet = new HashSet<String>(Arrays.asList(items));
        Assert.assertTrue(fileSet.contains("user"));

    }

    //   @Test
    public void testHDFSGuaranteeDirectory() {
        if (!RemoteTestConfiguration.isHDFSAccessible())
            return;
        IFileSystem fs = HDFSAccessor.getFileSystem();
        String myHome = fs.pwd();
        String newDir = myHome + "/foo";
        if (!fs.exists(newDir))
            fs.guaranteeDirectory(newDir);
        Assert.assertTrue(fs.exists(newDir));
        Assert.assertTrue(fs.isDirectory(newDir));

        fs.expunge(newDir);
        Assert.assertFalse(fs.exists(newDir));

        fs.guaranteeDirectory(newDir);
        fs.writeToFileSystem(newDir + "/mary.txt", TEST_CONTENT);

        String[] files = fs.ls(newDir);
        Assert.assertEquals(1, files.length);
        Assert.assertEquals(files[0], "mary.txt");
        fs.expunge(newDir);
        Assert.assertFalse(fs.exists(newDir));


    }

    //    @Test
    public void testHDFSFileReadWrite() {
        if (!RemoteTestConfiguration.isHDFSAccessible())
            return;
        IFileSystem fs = HDFSAccessor.getFileSystem();
        String myHome = fs.pwd();
        String newDir = myHome + "/mary.txt";
        if (fs.exists(newDir))
            fs.deleteFile(newDir);
        Assert.assertFalse(fs.exists(newDir));
        fs.writeToFileSystem(newDir, TEST_CONTENT);
        Assert.assertTrue(fs.exists(newDir));
        String copyContent = fs.readFromFileSystem(newDir);
        Assert.assertEquals(TEST_CONTENT, copyContent);


        fs.deleteFile(newDir);
        Assert.assertFalse(fs.exists(newDir));
        Assert.assertFalse(fs.exists(newDir));

    }

    private FTPWrapper buildFTPWrapper() {
        // todo handle as preferences
        final String cook = "cook";  // nazme of the remote system
        return new FTPWrapper(RemoteUtilities.getUser(), RemoteUtilities.getPassword(), cook);
    }
}
