package org.systemsbiology.aws;

import java.util.*;

/**
 * org.systemsbiology.aws.InstancePricing
 * User: Steve
 * Date: 3/2/12
 */
public class FixedPricing implements IInstancePricing {


    private final AWSInstanceSize m_Size;
    private final int m_Price; // * 200 in cents


    public FixedPricing(AWSInstanceSize size, double price) {
        m_Size = size;
        m_Price = AWSPricingUtilities.priceToInt(price);
    }

    public AWSInstanceSize getSize() {
        return m_Size;
    }

    public double getPricingForProbability(double p) {
        return AWSPricingUtilities.intToPrice(m_Price);
    }
}
