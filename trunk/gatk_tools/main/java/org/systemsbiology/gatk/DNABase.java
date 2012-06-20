package org.systemsbiology.gatk;

import java.util.*;

/**
 * org.systemsbiology.gatk.DNABase
 * User: steven
 * Date: 6/12/12
 */
public enum DNABase {
    A,C,G,T;
    public static final DNABase[] EMPTY_ARRAY = {};

    public static DNABase[] fromString(String s) {
        if(s.contains(","))
            s = s.substring(0,s.indexOf(","));
        List<DNABase> holder = new ArrayList<DNABase>();
         for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i,i + 1);
            holder.add(DNABase.valueOf(c));
        }

        DNABase[] ret = new DNABase[holder.size()];
        holder.toArray(ret);
        return ret;
    }

}
