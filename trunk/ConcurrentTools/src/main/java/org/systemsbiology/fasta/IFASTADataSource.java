package org.systemsbiology.fasta;

import org.systemsbiology.chromosome.*;

import java.io.*;

/**
 * org.systemsbiology.fasta.IFASTADataSource
 * User: steven
 * Date: May 24, 2010
 */
public interface IFASTADataSource {
    public static final IFASTADataSource[] EMPTY_ARRAY = {};


    public String getFASTAResource(IChromosome chr);

}
