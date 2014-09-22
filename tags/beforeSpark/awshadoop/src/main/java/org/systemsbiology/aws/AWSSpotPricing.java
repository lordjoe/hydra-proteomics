package org.systemsbiology.aws;

import com.amazonaws.services.ec2.*;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.elasticmapreduce.model.InstanceRoleType;

import java.util.*;

/**
 * org.systemsbiology.aws.AWSSpotPricing
 * User: Steve
 * Date: 3/2/12
 */
public class AWSSpotPricing {
    public static final AWSSpotPricing[] EMPTY_ARRAY = {};
    public static int gDefaultPriceDurationMinutes = 7 * 24 * 60;


    public static class ASWSpotPrice implements Comparable<ASWSpotPrice> {
        private final AWSInstanceSize m_Size;
        private final int m_Price; // * 200 in cents
        private final Date m_When;

        public ASWSpotPrice(final int pPrice, final Date pWhen, final AWSInstanceSize pSize) {
            m_Price = pPrice;
            m_When = pWhen;
            m_Size = pSize;
        }

        public ASWSpotPrice(SpotPrice prc) {
            double priceStr = Double.parseDouble(prc.getSpotPrice());
            m_Price = AWSPricingUtilities.priceToInt(priceStr);
            m_When = prc.getTimestamp();
            String name = prc.getInstanceType().toString();
            m_Size = AWSInstanceSize.getInstanceSize(name);
        }

        public AWSInstanceSize getSize() {
            return m_Size;
        }

        public int getPrice() {
            return m_Price;
        }

        public Date getWhen() {
            return m_When;
        }


        @Override
        public int compareTo(final ASWSpotPrice o) {
            if (this == o) return 0;
            if (getSize() != o.getSize())
                return getSize().compareTo(o.getSize());
            int p1 = getPrice();
            int p2 = o.getPrice();
            if (p1 != p2)
                return p1 < p2 ? -1 : 1;
            return getWhen().compareTo(o.getWhen());

        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final ASWSpotPrice that = (ASWSpotPrice) o;

            if (m_Price != that.m_Price) return false;
            if (m_Size != that.m_Size) return false;
            if (m_When != null ? !m_When.equals(that.m_When) : that.m_When != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = m_Size != null ? m_Size.hashCode() : 0;
            result = 31 * result + m_Price;
            result = 31 * result + (m_When != null ? m_When.hashCode() : 0);
            return result;
        }
    }

    public static int getDefaultPriceDurationMinutes() {
        return gDefaultPriceDurationMinutes;
    }

    public static void setDefaultPriceDurationMinutes(final int pDefaultPriceDurationMinutes) {
        gDefaultPriceDurationMinutes = pDefaultPriceDurationMinutes;
    }


    public static Map<AWSInstanceSize, InstancePricing> getSpotPrice(AWSInstanceSize... size) {
        return getSpotPrice(AWSUtilities.getDefaultLocation(), getDefaultPriceDurationMinutes(), size);
    }

    public static Map<AWSInstanceSize, InstancePricing> getSpotPrice(AWSLocation region, int sinceMinutes, AWSInstanceSize... size) {
        DescribeSpotPriceHistoryRequest req = new DescribeSpotPriceHistoryRequest();

        long statrMillisec = System.currentTimeMillis() - sinceMinutes * 60L * 1000L;
        Date d = new Date(statrMillisec);
        req.setStartTime(d);

        if (size != null && size.length > 0) {
            List<String> holder = new ArrayList<String>();
            for (int i = 0; i < size.length; i++) {
                holder.add(size[i].toString());
            }
            req.setInstanceTypes(holder);
        }

        if (region != null)
            req.setAvailabilityZone(region.toString());

        AmazonEC2 s3 = AWSUtilities.getAmazonEC2();

        DescribeSpotPriceHistoryResult res = s3.describeSpotPriceHistory(req);

        Map<AWSInstanceSize, InstancePricing> ret = new HashMap<AWSInstanceSize, InstancePricing>();
        List<SpotPrice> spotPriceHistory = res.getSpotPriceHistory();

        for (int i = 0; i < size.length; i++) {
            AWSInstanceSize sz = size[i];
            ret.put(sz, new InstancePricing(sz, spotPriceHistory));
        }
        return ret;
    }


    public static double findAveragePrice(AWSInstanceSize sz, List<SpotPrice> spotPriceHistory) {
        String type = sz.toString();
        int number = 0;
        double sum = 0;
        for (SpotPrice sp : spotPriceHistory) {
            if (type.equals(sp.getInstanceType())) {
                String spotPrice = sp.getSpotPrice();
                try {
                    double price = Double.parseDouble(spotPrice);
                    number++;
                    sum += price;
                }
                catch (NumberFormatException e) {
                    throw new RuntimeException(e);

                }

            }
        }
        if (number < 1)
            return -1;
        return sum / number;

    }

    public static final double DEFAULT_PROPABILITY = 0.9;

    public static double getRecommendedBidPrice(AWSInstanceSize size) {
        return getRecommendedBidPrice(size, DEFAULT_PROPABILITY);
    }

    public static double getRecommendedBidPrice(AWSInstanceSize size, double propability) {
        double basePrice = AWSPricingUtilities.getBasePrice(size);
        AWSInstanceSize[] sizes = {size};
        Map<AWSInstanceSize, InstancePricing> prices = getSpotPrice(sizes);
        InstancePricing price = prices.get(size);
        double pricingForProbability = price.getPricingForProbability(propability);
        return pricingForProbability;
    }

    public static InstanceGroupConfig makeSpotBid(final AWSInstanceSize size, int count) {
          return makeSpotBid(  size, count,InstanceRoleType.MASTER,MarketType.SPOT);
    }
    public static InstanceGroupConfig makeSpotBid(final AWSInstanceSize size, int count,InstanceRoleType role,MarketType market) {
   //     priceStr = "0.080";
        InstanceGroupConfig ret = new InstanceGroupConfig();
        ret.setInstanceRole(role.toString());
        // mastewr is on demand
        if(role == InstanceRoleType.MASTER)  {
            ret.setMarket(market.toString());
        }
        else {
            ret.setMarket(market.toString());

        }
        if(MarketType.SPOT == market)   {
            double price = getRecommendedBidPrice(size);
            String priceStr =  String.format("%8.3f",price).trim();
            ret.setBidPrice(priceStr);

        }
        ret.setInstanceType(size.toString());
        ret.setInstanceCount(count);
         return ret;
    }


    public static void main(String[] args) {
        AWSInstanceSize[] sizes = {AWSInstanceSize.Micro, AWSInstanceSize.Large, AWSInstanceSize.Small, AWSInstanceSize.XLarge};
        Map<AWSInstanceSize, InstancePricing> prices = getSpotPrice(sizes);
        for (AWSInstanceSize size : prices.keySet()) {
            InstancePricing price = prices.get(size);
            double pricingForProbability = price.getPricingForProbability(.9);
            System.out.println(size + " " + pricingForProbability);
        }

    }

}
