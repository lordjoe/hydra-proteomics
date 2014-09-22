package org.systemsbiology.xtandem.fragmentation;

import org.systemsbiology.xtandem.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.FragmentationUtilities
 * UserAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation(" steven
 * DateAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation(" 7/16/12
 */
public class FragmentationUtilities {
  public static final FragmentationUtilities[] EMPTY_ARRAY = {};

  /*# Amino acid hydrophobicity scale from
  # "Experimentally determined hydrophobicity scale for proteins at
  # membrane interfaces," Wimley and White,Nat Struct Biol 3AAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("842 (1996).
  #
  # More positive means more hydrophobic.
  # Values for the ionized forms of asp,glu,his are used here,and the
  # met value is also assigned to selenomethionine.
  #
  # Use this file to assign the attribute in Chimera with the
  # Define Attribute tool or the command defattr.
  # see httpgHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("//www.cgl.ucsf.edu/chimera/docs/ContributedSoftware/defineattrib/wwHydrophobicity.txt
  */
  // 
  private static final Map<FastaAminoAcid,Double> gAAHydrophobicity =
      new HashMap<FastaAminoAcid,Double>();
  /*  # Amino acid hydrophobicity scale from
# the supplementary information for "Recognition of transmembrane 
# helices by the endoplasmic reticulum translocon," Hessa et al.,
# Nature 433gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("377 (2005).
#
# More negative means more hydrophobic. 
# The met value is also assigned to selenomethionine.
#
# Use this file to assign the attribute in Chimera with the 
# Define Attribute tool or the command defattr.
#
*/
  private static final Map<FastaAminoAcid,Double> gHAAHydrophobicity =
      new HashMap<FastaAminoAcid,Double>();

  static {
//wwHydrophobicity
//residues
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("asp"),-1.23);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("glu"),-2.02);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("asn"),-0.42);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("gln"),-0.58);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("lys"),-0.99);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("arg"),-0.81);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("his"),-0.96);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("gly"),-0.01);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("pro"),-0.45);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("ser"),-0.13);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("thr"),-0.14);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("cys"),0.24);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("met"),0.23);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("mse"),0.23);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("ala"),-0.17);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("val"),-0.07);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("ile"),0.31);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("leu"),0.56);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("phe"),1.13);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("trp"),1.85);
    gAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("tyr"),0.94);
//attributegHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation(" hhHydrophobicity
//recipientgHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation(" residues
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("asp"),3.49);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("glu"),2.68);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("asn"),2.05);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("gln"),2.36);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("lys"),2.71);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("arg"),2.58);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("his"),2.06);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("gly"),0.74);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("pro"),2.23);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("ser"),0.84);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("thr"),0.52);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("cys"),-0.13);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("met"),-0.10);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("mse"),-0.10);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("ala"),0.11);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("val"),-0.31);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("ile"),-0.60);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("leu"),-0.55);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("phe"),-0.32);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("trp"),0.30);
	gHAAHydrophobicity.put(FastaAminoAcid.fromAbbreviation("tyr"),0.68);
  }

  public static double getHydrophobicity(FastaAminoAcid aa) {
    Double ret = gAAHydrophobicity.get(aa);
    if(ret == null)
      return 0;
    return ret;
  }

}
