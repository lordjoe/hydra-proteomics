package org.systemsbiology.xtandem.fragmentation;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.UniprotFeatureType
 * User: steven
 * Date: 9/7/12
 */
public enum UniprotFeatureType {
    ACT_SITE,
    BINDING,
    CARBOHYD,
    CA_BIND,
    CHAIN,
    COILED,
    COMPBIAS,
    CONFLICT,
    CROSSLNK,
    DISULFID,
    DNA_BIND,
    DOMAIN,
    HELIX,
    INIT_MET,
    INTRAMEM,
    LIPID,
    METAL,
    MOD_RES,
    MOTIF,
    MUTAGEN,
    NON_CONS,
    NON_STD,
    NON_TER,
    NP_BIND,
    PEPTIDE,
    PROPEP,
    REGION,
    REPEAT,
    SIGNAL,
    SITE,
    STRAND,
    TOPO_DOM,
    TRANSIT,
    TRANSMEM,
    TURN,
    UNSURE,
    VARIANT,
    VAR_SEQ,
    ZN_FING,
    ;
    public static final UniprotFeatureType[] EMPTY_ARRAY = {};

    public static void showAllFeatures() {
         UniprotFeatureType[] values = UniprotFeatureType.values();
         String[] names = new String[values.length];
         for (int i = 0; i < values.length; i++) {
             names[i]  = values[i].toString();

         }
         Arrays.sort(names);
         for (int i = 0; i < names.length; i++) {
             String value = names[i];
             System.out.println(value  + ",");
         }
     }
}
