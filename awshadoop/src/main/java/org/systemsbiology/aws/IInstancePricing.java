package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.IInstancePricing
 * User: Steve
 * Date: 3/2/12
 */
public interface IInstancePricing {
    public static final IInstancePricing[] EMPTY_ARRAY = {};



    public AWSInstanceSize getSize();

     public double getPricingForProbability(double p);

}
