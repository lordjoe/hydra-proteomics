package org.systemsbiology.gene;

import org.systemsbiology.chromosome.*;
import org.systemsbiology.location.*;

/**
 * org.systemsbiology.gene.IGeneListing
 * written by Steve Lewis
 * on Apr 14, 2010
 */
public interface IGeneListing
{
    public static final IGeneListing[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IGeneListing.class;


    public IChromosomeInterval getLocation();

    public IGeneLocation getStartLocation();

    public IGeneLocation getEndLocation();

    public String getName();

    public String getRefSequence();

    public IChromosome getChromosome();

    public GenePolarity getPolarity();

    public int getStartPosition();

    public int getEndPosition();
}