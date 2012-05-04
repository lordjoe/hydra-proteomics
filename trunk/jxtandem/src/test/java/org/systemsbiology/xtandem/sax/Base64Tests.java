package org.systemsbiology.xtandem.sax;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.systemsbiology.xtandem.*;

/**
 * org.systemsbiology.xtandem.sax.Base64Tests
 * User: Steve
 * Date: 9/1/11
 */
public class Base64Tests {
    public static final Base64Tests[] EMPTY_ARRAY = {};

    public static final int NPEAKS = 57;

    public static final String BASE64_PEAKS =
            "eJxzyFT+9KSvq8ghkgEMHAqZ9nvUn54E5xdvWqllEGoE55cbXy+W+BMA51c/9udt8WWE8+tdWj/kZ7rA+Q1tCaXf/irB+Y1VwX2ffbMQ/EttvTt2HYfzm3adRzGvZdL9G2tXz4fz25yrvZQtD8L5HbPNxSMeScL5XdHnrM+aroDzuzcf1llaVQHn975REz+4dymc37dHX2r+vnI4f0JTpFi7NhOcP/FcvYgQ6x4YHwBGGVAN";

    public ISpectrumPeak[] buildPeaks()
    {
        ISpectrumPeak[] ret = new  ISpectrumPeak[NPEAKS];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new SpectrumPeak(5 * i,0.33f * i);

        }
        return ret;
    }

    /**
     * make sure Base64 encode decode works
     */
    @Test
    public void testEncodeDecode()
    {
        ISpectrumPeak[] original = buildPeaks();
        String s1 = XTandemUtilities.encodePeaks(original,MassResolution.Bits32);
        ISpectrumPeak[] read =  XTandemUtilities.decodePeaks(s1,MassResolution.Bits32);
        for (int i = 0; i < read.length; i++) {
            ISpectrumPeak oPeak = read[i];
            ISpectrumPeak rPeak = original[i];
            Assert.assertTrue(oPeak.equivalent(rPeak));
        }
        String s2 = XTandemUtilities.encodePeaks(read,MassResolution.Bits32);
        Assert.assertEquals(s1,s2);
    }

    public static final double[] PEAK_MZ =
            {
                    201.1234,
                    272.1717,
                    315.1663,
                    371.2401,
                    446.2068,
                    500.2827,
                    528.7971,
                    559.2908,
                    570.3157,
                    599.3511,
                    658.3593,
                    712.4352,
                    787.4019,
                    843.4757,
                    886.4703,
                    957.5186,
                    983.523,
                    1056.587,
                    1139.6241,

            };

    /**
     * make sure Base64 encode decode works
     */
    @Test
    public void testBase64Decode()
    {
         ISpectrumPeak[] read =  XTandemUtilities.decodeCompressedPeaks(BASE64_PEAKS, MassResolution.Bits64);

        for (int i = 0; i < read.length; i++) {
            ISpectrumPeak oPeak = read[i];
            Assert.assertEquals(PEAK_MZ[i],oPeak.getMassChargeRatio(),0.001);
            Assert.assertEquals(100,oPeak.getPeak(),0.001);
            System.out.println("" + oPeak.getMassChargeRatio() + "," ); // +  oPeak.getPeak() + ",");
          //  ISpectrumPeak rPeak = read[i];
          //  Assert.assertTrue(oPeak.equivalent(rPeak));
        }
        Assert.assertEquals(19,read.length);
     }

}
