package org.systemsbiology.aws;

import com.amazonaws.services.elasticmapreduce.model.*;

import java.util.*;

/**
 * org.systemsbiology.aws.AWSPricingUtilities
 * User: Steve
 * Date: 3/2/12
 */
public class AWSPricingUtilities {
    public static final AWSPricingUtilities[] EMPTY_ARRAY = {};

    private static final double PRICE_FACTOR = 1000;
    private static final Map<AWSInstanceSize, IInstancePricing> gBasePricing = new HashMap<AWSInstanceSize, IInstancePricing>();

    //
    public static final double[] BASE_PRICING =
            {
                0.02,   // Micro,
                0.08,   // Small,
                0.32,   // Large,
                0.64,   // XLarge,
                0.50,   // M2Large,
                1.00,   // M4Large,
                2.00,   // M8Large,
                0.17,   // C1Medium,
                0.68,   // C1Large,
                1.30,   // CCLarge,
                2.40,   // CC2XLarge;

            };
    static {
        AWSInstanceSize[] values = AWSInstanceSize.values();
        for (int i = 0; i < values.length; i++) {
            AWSInstanceSize value = values[i];
            gBasePricing.put(value,new FixedPricing(value,BASE_PRICING[i]));
        }
    }


    public static int priceToInt(double p)
    {
        if(p < 0)
            return -1;
        return  (int) (PRICE_FACTOR * p);
    }

    public static double intToPrice(int p)
    {
        if(p < 0)
            return -1;
        return   p / PRICE_FACTOR;
    }

    public static int getBasePriceInt(final AWSInstanceSize pSize) {
        double basePrice = getBasePrice(pSize);
        return priceToInt(basePrice);
    }


    public static double getBasePrice(final AWSInstanceSize pSize) {
        IInstancePricing iInstancePricing = gBasePricing.get(pSize);
        return iInstancePricing.getPricingForProbability(0.5);
    }

}
