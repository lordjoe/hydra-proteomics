package org.systemsbiology.gene;

import org.systemsbiology.chromosome.*;
import org.systemsbiology.location.*;

/**
 * org.systemsbiology.gene.GeneListing
 * written by Steve Lewis
 * on Apr 12, 2010
 */
public class GeneListing implements IGeneListing
{
    public static final GeneListing[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = GeneListing.class;

    public static final int NAME_POSITION = 0;
    public static final int REFSEQUENCE_POSITION = 1;
    public static final int CHROMOSOME_POSITION = 2;
    public static final int POLARITY_POSITION = 3;
    public static final int START_POSITION = 4;
    public static final int END_POSITION = 5;

    private final String m_Name;
    private final String m_RefSequence;
    private final IChromosome m_Chromosome;
    private final GenePolarity m_Polarity;
    private final int m_StartPosition;
    private final int m_EndPosition;
    private IChromosomeInterval m_Location;
    private IGeneLocation m_StartLocation;
    private IGeneLocation m_EndLocation;


    public GeneListing(String pName, String pRefSequence, IChromosome pChromosome, GenePolarity pPolarity, int pStartPosition, int pEndPosition) {
        m_Name = pName;
        m_RefSequence = pRefSequence;
        m_Chromosome = pChromosome;
        m_Polarity = pPolarity;
        m_StartPosition = pStartPosition;
        m_EndPosition = pEndPosition;
       }

    public GeneListing(String s)  {
        String[] items = s.split("\t");
        String pName = items[NAME_POSITION];
        String pRefSequence = items[REFSEQUENCE_POSITION];
        IChromosome pChromosome = null;
        String s1 = items[CHROMOSOME_POSITION];
        try {
            pChromosome = DefaultChromosome.parseChromosome(s1);
        }
        catch (IllegalArgumentException e) {
            pChromosome = null;
        }
        GenePolarity pPolarity = items[POLARITY_POSITION].equals("+") ? GenePolarity.Positive : GenePolarity.Negative;
        int pStartPosition = Integer.parseInt(items[START_POSITION]);
        int pEndPosition = Integer.parseInt(items[END_POSITION]);
        m_Name = pName;
         m_RefSequence = pRefSequence;
         m_Chromosome = pChromosome;
         m_Polarity = pPolarity;
         m_StartPosition = pStartPosition;
         m_EndPosition = pEndPosition;
         

    }

    public IChromosomeInterval getLocation() {
        if(m_Location == null)
            m_Location = new GenomeInterval(getChromosome(),getStartPosition(),getEndPosition());
        return m_Location;
    }

    public IGeneLocation getStartLocation() {
        if(m_StartLocation == null)
             m_StartLocation = new GenomeLocation(getChromosome(),getStartPosition());
         return m_StartLocation;
    }

    public IGeneLocation getEndLocation() {
        if(m_EndLocation == null)
             m_EndLocation = new GenomeLocation(getChromosome(),getEndPosition());
         return m_EndLocation;
    }

    public String getName() {
        return m_Name;
    }

    public String getRefSequence() {
        return m_RefSequence;
    }

    public IChromosome getChromosome() {
        return m_Chromosome;
    }

    public GenePolarity getPolarity() {
        return m_Polarity;
    }

    public int getStartPosition() {
        return m_StartPosition;
    }

    public int getEndPosition() {
        return m_EndPosition;
    }
}
