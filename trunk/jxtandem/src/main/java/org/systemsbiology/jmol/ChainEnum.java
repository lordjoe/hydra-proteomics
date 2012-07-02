package org.systemsbiology.jmol;

/**
 * org.systemsbiology.jmol.ChainEnum
 * User: steven
 * Date: 7/2/12
 */
public enum ChainEnum {
    A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z, Chn1,Chn2, Chn3,Chn4,Chn5,Chn6,Chn7,Chn8,Chn9;
    public static final ChainEnum[] EMPTY_ARRAY = {};

    public static ChainEnum fromString(String s)
    {
        if(Character.isDigit(s.charAt(0)))
            s = "Chn"  + s;
        return valueOf(s);
    }

}
