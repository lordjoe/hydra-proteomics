package org.systemsbiology.aws;

import org.junit.*;
import org.systemsbiology.common.*;

/**
 * org.systemsbiology.aws.S3AccessorTests
 * User: steven
 * Date: 12/7/11
 */
public class S3AccessorTests {
    public static final S3AccessorTests[] EMPTY_ARRAY = {};

    public static final String BASE_DIRECTORY = "test/";
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
    public void S3ReadTest() {
          IFileSystem access = new S3Accessor(AWSUtilities.getDefaultBucketName());
        String filePath = BASE_DIRECTORY + FILE_NAME;
        access.guaranteeDirectory(BASE_DIRECTORY);

        boolean didExist = access.exists(BASE_DIRECTORY);
        access.writeToFileSystem(filePath, TEST_CONTENT);
        String result = access.readFromFileSystem(filePath);

        Assert.assertEquals(result, TEST_CONTENT);

        Assert.assertTrue(access.exists(filePath));
        Assert.assertTrue(access.isFile(filePath));
        Assert.assertTrue(access.exists(BASE_DIRECTORY));
        Assert.assertTrue(access.isDirectory(BASE_DIRECTORY));

        access.deleteFile(filePath);

        Assert.assertFalse(access.exists(filePath));

        // if empty directories wiped out this may be cone
        if (access.isEmptyDirectoryAllowed()) {
            Assert.assertEquals(didExist, access.exists(BASE_DIRECTORY));
        }
        else {
            Assert.assertFalse(access.isDirectory(BASE_DIRECTORY));
            Assert.assertFalse(access.exists(BASE_DIRECTORY));
        }

    }

}
