package org.systemsbiology.xtandem.sax;

import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.sax.ProtXMLDecection
 * User: Steve
 * Date: 3/15/13
 */
public class ProtXMLDetection {
    public static final ProtXMLDetection[] EMPTY_ARRAY = {};

    private String m_Name;
    private Set<String> m_AlternateNames = new HashSet<String>();
     private String m_Annotation;
     private double m_Probability;
     private double m_FractionCoverage;
     private IPolypeptide[] m_DetectedPeptides = IPolypeptide.EMPTY_ARRAY;

     private int m_ProteinLength;

    public ProtXMLDetection() {
    }

    public String getName() {
        return m_Name;
    }

    public void setName(final String pName) {
        m_Name = pName;
        addAlternateName(pName);
    }


    public void addAlternateName(final String pName) {
         m_AlternateNames.add(pName);
    }

    public static class StringCompatatorLengthThenAlpha implements Comparator<String>   {
        @Override
        public int compare(String o1, String o2) {
            if(o1 == o2)
                return 0;
           if(o1.length() == o2.length())
               return o1.compareTo(o2) ;
            return o1.length() < o2.length() ? -1 : 1;
        }
    }

    public static final Comparator<String> COMPARE_LENGTH_THEN_ALPHA = new StringCompatatorLengthThenAlpha();

    /**
     * usually the shortest of alternate proteins looks like a uniprot not something moew complex
     * @return
     */
    public String[] getAlternateNames()
    {
        String[] ret = new String[m_AlternateNames.size()];
        m_AlternateNames.toArray(ret);
        Arrays.sort(ret,COMPARE_LENGTH_THEN_ALPHA);
        return ret;
    }


    public String getAnnotation() {
        return m_Annotation;
    }

    public void setAnnotation(final String pAnnotation) {
        m_Annotation = pAnnotation;
    }

    public double getProbability() {
        return m_Probability;
    }

    public void setProbability(final double pProbability) {
        if(pProbability == 0)
            return;
        m_Probability = pProbability;
    }

    public double getFractionCoverage() {
        return m_FractionCoverage;
    }

    public void setFractionCoverage(final double pFractionCoverage) {
        if(pFractionCoverage == 0)
            return;
        m_FractionCoverage = pFractionCoverage;
    }

    public IPolypeptide[] getDetectedPeptides() {
        return m_DetectedPeptides;
    }

    public void setDetectedPeptides(final IPolypeptide[] pDetectedPeptides) {
        m_DetectedPeptides = pDetectedPeptides;
    }

    public int getProteinLength() {
        return m_ProteinLength;
    }

    public void setProteinLength(final int pProteinLength) {
        m_ProteinLength = pProteinLength;
    }

    @Override
    public String toString() {
        return getName() + ":" + getAnnotation();
    }
}
