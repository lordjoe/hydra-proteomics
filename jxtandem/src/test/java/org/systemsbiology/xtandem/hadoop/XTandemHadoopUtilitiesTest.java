package org.systemsbiology.xtandem.hadoop;

import org.junit.*;

/**
 * Created with IntelliJ IDEA.
 * User: attilacsordas
 * Date: 16/05/2013
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class XTandemHadoopUtilitiesTest {

    public static XTandemHadoopUtilitiesTest[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = XTandemHadoopUtilitiesTest.class;

    @Test
    public void testSubstringForAsDecoy() {
        String prefix = "DECOY_";
        String label = "DECOY_P31946";

        String processed_label = label.substring(prefix.length(), label.length());

        Assert.assertEquals("P31946", processed_label);

        String prefix2 = "###REV###";

        String label2 = "###REV###P31946";

        String processed_label2 = label2.substring(prefix2.length(), label2.length());

        Assert.assertEquals("P31946", processed_label2);

    }


    @Test
    public void testAsDecoy() {

        String post_label = "DECOY_P31946";

        String label1 = "P31946";

        String label2 = "###REV###P31946";

        String label3 = "###RND###P31946";

        //String label4 = "RND_";

        //String label5 = "REV_";

        //String label6 = "REV1_";

        String label7 = "RND_P31946";

        String label8 = "REV_P31946";

        String label9 = "REV1_P31946";

        // 2 ways of doing the same thing, handle empty string
        Assert.assertEquals(null, XTandemHadoopUtilities.asDecoy(label1));
        Assert.assertNull(XTandemHadoopUtilities.asDecoy(label1));

        Assert.assertEquals(post_label, XTandemHadoopUtilities.asDecoy(label2));
        Assert.assertEquals(post_label, XTandemHadoopUtilities.asDecoy(label3));
        //Assert.assertEquals(post_label,XTandemHadoopUtilities.asDecoy(label4));
        //Assert.assertEquals(post_label,XTandemHadoopUtilities.asDecoy(label5));
        //Assert.assertEquals(post_label,XTandemHadoopUtilities.asDecoy(label6));
        Assert.assertEquals(post_label, XTandemHadoopUtilities.asDecoy(label7));
        Assert.assertEquals(post_label, XTandemHadoopUtilities.asDecoy(label8));
        Assert.assertEquals(post_label, XTandemHadoopUtilities.asDecoy(label9));


    }


}
