package org.systemsbiology.hadoop;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.junit.*;

import java.net.*;

/**
 * org.systemsbiology.hadoop.ResourceFileSystemTests
 * tests org.systemsbiology.hadoop.ResourceFileSystem
 * User: steven
 * Date: 2/1/12
 */
public class ResourceFileSystemTests {
    public static final ResourceFileSystemTests[] EMPTY_ARRAY = {};

    public static final int GETTYSBURG_LINES = 15; // lines in gettysburg address needed to compensate for pc lines
    public static final String GETTYSBURG =
            "Four score and seven years ago our fathers brought forth on this continent, a new nation, conceived in Liberty, and\n" +
                    "dedicated to the proposition that all men are created equal.\n" +
                    "\n" +
                    "Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated,\n" +
                    "can long endure. We are met on a great battle-field of that war. We have come to dedicate a portion of that field,\n" +
                    "as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting\n" +
                    "and proper that we should do this.\n" +
                    "\n" +
                    "But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave\n" +
                    "men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world\n" +
                    "will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the\n" +
                    "living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced.\n" +
                    "It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take\n" +
                    "increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve\n" +
                    "that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and\n" +
                    "that government of the people, by the people, for the people, shall not perish from the earth.";

    private Configuration m_Conf;

    public Configuration getConf() {
        return m_Conf;
    }


    @Before
    public void setUp() throws Exception {
        m_Conf = new Configuration();
        String clasName = ResourceFileSystem.class.getName();
        m_Conf.set(ResourceFileSystem.BASE_CLASS_PROPERTY_NAME, clasName);
        m_Conf.set(ResourceFileSystem.SCHEME_PROPERTY_NAME, clasName);
    }

    @Test
    public void testResourceFileRead() throws Exception {
        URI test = new URI("res:///");
        FileSystem fileSystem = FileSystem.get(test, getConf());
        Assert.assertEquals(fileSystem.getClass(), ResourceFileSystem.class);

        ResourceFileSystem realFileSystem = (ResourceFileSystem) fileSystem;
        Assert.assertEquals(realFileSystem.getBaseClass(), ResourceFileSystem.class);


        HDFSAccessor accessor = new HDFSAccessor(fileSystem);

        Assert.assertTrue(accessor.exists("res://Gettysburg.txt"));
        long resLength = accessor.fileLength("res://Gettysburg.txt");
        int expectedLength = GETTYSBURG.length() + GETTYSBURG_LINES;
        Assert.assertEquals(expectedLength, resLength);

        String read = accessor.readFromFileSystem("res://Gettysburg.txt");
        read = read.replace("\r", "");
        Assert.assertEquals(GETTYSBURG, read);


        boolean exists = accessor.exists("res://NotThere.txt");
        Assert.assertFalse(exists);
        Assert.assertEquals(0, accessor.fileLength("res://NotThere.txt"));

    }
}
