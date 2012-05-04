package org.systemsbiology.xtandem;

import org.junit.*;

/**
 * org.systemsbiology.xtandem.Base64Tests
 * User: Steve
 * Date: 9/4/11
 */
public class Base64Tests {
    public static final Base64Tests[] EMPTY_ARRAY = {};

    public static final float[] FLOAT_DATA = {
            1.34f, (float)Math.PI,   (float)Math.E,  2.0f,
            2.1f, 1.3f, 67.345f,
     };

    public static final double[] DOUBLE_DATA = {
            1.34,  Math.PI,   (float)Math.E,  2.0,
            2.1, 1.3, 67.345,
     };

    @Test
    public void testFloat()
    {
        String s = Base64.encodeFloatsAsString(FLOAT_DATA);
        float[] fx = Base64.decodeFloats(s);
        for (int i = 0; i < fx.length; i++) {
            Assert.assertEquals(fx[i],FLOAT_DATA[i],0.0001);
           }
    }

    @Test
    public void testDouble()
    {
        String s = Base64.encodeDoublesAsString(DOUBLE_DATA);
        double[] fx = Base64.decodeDoubles(s);
        for (int i = 0; i < fx.length; i++) {
            Assert.assertEquals(fx[i],DOUBLE_DATA[i],0.0001);
           }
    }

}
