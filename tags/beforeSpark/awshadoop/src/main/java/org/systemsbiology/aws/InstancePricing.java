package org.systemsbiology.aws;

import com.amazonaws.services.ec2.model.*;

import java.util.*;

/**
* org.systemsbiology.aws.InstancePricing
* User: Steve
* Date: 3/2/12
*/
public class InstancePricing implements IInstancePricing {
    private final AWSInstanceSize m_Size;
    private final Integer[] m_Price; // * 200 in cents
    private final int m_BasePrice; // * 200 in cents


    public InstancePricing(AWSInstanceSize size, List<SpotPrice> pPrice) {
        m_Size = size;
        m_BasePrice = AWSPricingUtilities.getBasePriceInt(size);
        List<Integer> holder = new ArrayList<Integer>();
        for ( SpotPrice spp : pPrice ) {
                AWSSpotPricing.ASWSpotPrice pp = new AWSSpotPricing.ASWSpotPrice(spp);
               if (pp.getSize() != size)
                continue;
            holder.add(pp.getPrice());
        }
        m_Price = new Integer[holder.size()];
        holder.toArray(m_Price);
        Arrays.sort(m_Price);
    }

    public AWSInstanceSize getSize() {
        return m_Size;
    }

    public Integer getBasePrice() {
        return m_BasePrice;
    }

    public double getPricingForProbability(double p) {
        int pricing = getInternalPricingForProbability(  p);
        Integer basePrice = getBasePrice();
        if(pricing == -1)
            return AWSPricingUtilities.intToPrice(basePrice);
         if(pricing >= basePrice)
             return AWSPricingUtilities.intToPrice(basePrice);

        return AWSPricingUtilities.intToPrice(pricing);
    }

    protected int getInternalPricingForProbability(double p) {
        int n = (int) (0.5 + (p * m_Price.length));
        if (n > m_Price.length - 1)
            return m_Price[m_Price.length - 1];
        if (n < 0)
            return m_Price[0];
        return m_Price[n];
    }
}
