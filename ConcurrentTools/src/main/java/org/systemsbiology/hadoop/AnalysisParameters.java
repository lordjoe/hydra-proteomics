package org.systemsbiology.hadoop;

import net.sf.samtools.*;
 import org.systemsbiology.data.*;

import java.io.*;

/**
 * org.systemsbiology.hadoop.AnalysisParameters
 * written by Steve Lewis
 * on Apr 8, 2010
 * This class is where we can inject critical parameters for analysis
 */
public class AnalysisParameters implements IAnalysisParameters
{
    public static final AnalysisParameters[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = AnalysisParameters.class;

    private static IAnalysisParameters gInstance;

    public synchronized static IAnalysisParameters getInstance() {
        if(gInstance == null) {
            gInstance = new AnalysisParameters();
        }
        return gInstance;
    }

    private boolean m_WriteFilteredAndJunk;
    private boolean m_OutputSam;
    private boolean m_DataSourcesTCGA;
    private boolean m_StrictParsing;
    private boolean m_Obfuscate;
    private String m_DefaultGenome;
    private String m_ChromosomeName;
    private IStreamFactory m_DefaultStreamFactory;
    private SAMFileHeader m_LastHeader;
    private String m_LastInputSource;
    private SAMFileWriterFactory m_WriterFactory = new SAMFileWriterFactory();

    private AnalysisParameters() {}

    public boolean isObfuscate() {
        return m_Obfuscate;
    }

    public void setObfuscate(final boolean pObfuscate) {
        m_Obfuscate = pObfuscate;
    }

       public boolean isDataSourcesTCGA() {
        return m_DataSourcesTCGA;
    }

    public void setDataSourcesTCGA(boolean pDataSourcesTCGA) {
        m_DataSourcesTCGA = pDataSourcesTCGA;
    }


    public String getLastInputSource() {
        return m_LastInputSource;
    }


    public boolean isWriteFilteredAndJunk() {
        return m_WriteFilteredAndJunk;
    }

    public void setWriteFilteredAndJunk(boolean pWriteFilteredAndJunk) {
        m_WriteFilteredAndJunk = pWriteFilteredAndJunk;
    }



    public void setLastInputSource(String pLastInputSource) {
        m_LastInputSource = pLastInputSource;
    }

    public SAMFileHeader getLastHeader() {
        return m_LastHeader;
    }

    public void setLastHeader(SAMFileHeader pLastHeader) {
        m_LastHeader = pLastHeader;
    }

    public String getDefaultGenome() {
        return m_DefaultGenome;
    }

    public void setDefaultGenome(String pDefaultGenome) {
        m_DefaultGenome = pDefaultGenome;
    }

    public boolean isOutputSam() {
        return m_OutputSam;
    }

    public void setOutputSam(boolean pOutputSam) {
        m_OutputSam = pOutputSam;
    }

    /**
     * true if we enforce parse constraints
     * @return  as above
     */
    public boolean isStrictParsing() {
        return m_StrictParsing;
    }



    /**
        * true if we enforce parse constraints
        * @param pStrictParsing  as above
        */
      public void setStrictParsing(boolean pStrictParsing) {
        m_StrictParsing = pStrictParsing;
    }

    /**
     * return a way to write data to files or other streams
     * @return
     */
    public IStreamFactory getDefaultStreamFactory() {
        return m_DefaultStreamFactory;
    }

    public void setDefaultStreamFactory(IStreamFactory pDefaultStreamFactory) {
        m_DefaultStreamFactory = pDefaultStreamFactory;
    }

    public String getJobName() {
        return m_ChromosomeName;
    }

    public void setJobName(String pChromosomeName) {
        if(pChromosomeName.endsWith(".bam") || pChromosomeName.endsWith(".sam"))
            pChromosomeName = pChromosomeName.substring(0,pChromosomeName.length() - 4);
        m_ChromosomeName = pChromosomeName;
    }
 }
