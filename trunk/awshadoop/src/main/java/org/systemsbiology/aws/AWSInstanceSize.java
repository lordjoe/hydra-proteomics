package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.AWSInstanceSize
 * User: steven
 * Date: May 27, 2010
 * Enumeration for AWS sizes
 */
public enum AWSInstanceSize
{
    Micro,Small, Large, XLarge, M2Large, M4Large, M8Large, C1Medium, C1Large,CCLarge,CC2XLarge;

    @Override
    public String toString()
    {
        switch (this) {
            case Micro:
                 return "t1.micro";
            case Small:
                 return "m1.small";
             case Large:
                return "m1.large";
            case XLarge:
                return "m1.xlarge";
            case M2Large:
                return "m2.2xlarge";
            case M4Large:
                return "m2.2xlarge";
            case M8Large:
                return "m2.4xlarge";
            case C1Medium:
                return "c1.medium";
            case C1Large:
                return "c1.xlarge";
            case CCLarge:
                  return "cc1.4xlarge";
            case CC2XLarge:
                  return "cc2.8xlarge";
           }
        return super.toString();
    }

    public static boolean iscompatibleWith(AWSInstanceSize want, AWSInstanceSize have)
    {
        if(want == have)
            return true;
        switch(want) {
            case Micro:
                return true;
            case Small:
                return have != Micro;
             case Large:
                return have ==  Large || have == XLarge  || have == M2Large || have == M8Large ||  have == C1Large;
            case XLarge:
                return have == XLarge || have == M2Large || have == M8Large ||  have == C1Large;
            default:
                return false;
        }
    }

     public static AWSInstanceSize getInstanceSize(String s)
    {
        if (s.equalsIgnoreCase("Small")) return Small;
        if (s.equalsIgnoreCase("Large")) return Large;
        if (s.equalsIgnoreCase("XLarge")) return XLarge;
        if (s.equalsIgnoreCase("M2Large")) return M2Large;
        if (s.equalsIgnoreCase("M4Large")) return M4Large;
        if (s.equalsIgnoreCase("M8Large")) return M8Large;
        if (s.equalsIgnoreCase("C1Large")) return C1Large;
        if (s.equalsIgnoreCase("C1Medium")) return C1Medium;
        if (s.equalsIgnoreCase("Medium")) return C1Medium;
        if (s.equalsIgnoreCase("Micro")) return Micro;

        if (s.equalsIgnoreCase("t1.micro")) return Micro;
        if (s.equalsIgnoreCase("m1.small")) return Small;
        if (s.equalsIgnoreCase("m1.large")) return Large;
        if (s.equalsIgnoreCase("m1.xlarge")) return XLarge;
        if (s.equalsIgnoreCase("m2.2xlarge")) return M2Large;
        if (s.equalsIgnoreCase("m2.4xlarge")) return M4Large;
        if (s.equalsIgnoreCase("c1.xlarge")) return M8Large;
        if (s.equalsIgnoreCase("C1Large")) return C1Large;
        if (s.equalsIgnoreCase("c1.medium")) return C1Medium;
        if(s.equalsIgnoreCase("CCLarge"))
                     return CCLarge;
         if(s.equalsIgnoreCase("Cluster Compute Quadruple Extra Large Instance"))
                     return CCLarge;
        


        return AWSInstanceSize.valueOf(s); // probably throw an exception
    }

    public static AWSInstanceSize parse(String s)
    {
         return AWSInstanceSize.getInstanceSize(s); // probably throw an exception
    }
}
