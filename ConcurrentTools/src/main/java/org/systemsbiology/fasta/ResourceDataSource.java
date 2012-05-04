package org.systemsbiology.fasta;

import org.systemsbiology.chromosome.*;

import java.io.*;

/**
 * org.systemsbiology.fasta.ResourceDataSource
 * User: steven
 * Date: May 24, 2010
 */
public class ResourceDataSource implements IFASTADataSource
{
    public static final ResourceDataSource[] EMPTY_ARRAY = {};

    private final String m_Prefix;
    private final String m_Postfix = ".fa";

    public ResourceDataSource(final String pPrefix) {
        m_Prefix = pPrefix;
    }

    public String getPrefix() {
        return m_Prefix;
    }

    public String getPostfix() {
        return m_Postfix;
    }

    @Override
    public String getFASTAResource(final IChromosome chr) {
        String chrStr = chr.toString();
       // chrStr = mapChromosomes(chrStr);
        String ResName = getPrefix() + chrStr + getPostfix();
        return ResName;
    }

    protected static String mapChromosomes(String s)
    {
        if("chrI".equalsIgnoreCase(s))  return "chr01";
        if("chrII".equalsIgnoreCase(s))  return "chr02";
        if("chrIII".equalsIgnoreCase(s))  return "chr03";
        if("chrIV".equalsIgnoreCase(s))  return "chr04";
        if("chrV".equalsIgnoreCase(s))  return "chr05";
        if("chrVI".equalsIgnoreCase(s))  return "chr06";
        if("chrVII".equalsIgnoreCase(s))  return "chr07";
        if("chrVIII".equalsIgnoreCase(s))  return "chr08";
        if("chrIX".equalsIgnoreCase(s))  return "chr09";
        if("chrX".equalsIgnoreCase(s))  return "chr10";
        if("chrXI".equalsIgnoreCase(s))  return "chr11";
        if("chrXII".equalsIgnoreCase(s))  return "chr12";
         if("chrXIII".equalsIgnoreCase(s))  return "chr13";
        if("chrXIV".equalsIgnoreCase(s))  return "chr14";
        if("chrXV".equalsIgnoreCase(s))  return "chr15";
        if("chrXVI".equalsIgnoreCase(s))  return "chr16";
         return s ;
                      }
}
