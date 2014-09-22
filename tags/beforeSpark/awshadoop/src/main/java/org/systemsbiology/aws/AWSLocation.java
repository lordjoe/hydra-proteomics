package org.systemsbiology.aws;

/**
 * org.systemsbiology.aws.AWSLocation
 * User: steven
 * Date: May 27, 2010
 * Enumeration for AWS locations
 */
public enum AWSLocation {
    East,West,West2;

    @Override
    public String toString() {
        switch(this)  {
            case East: return "us-east-1a";
            case West: return "us-west-1a";
            case West2: return "us-west-2";
              }
        return super.toString();
    }
}