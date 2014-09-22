package org.systemsbiology.hadoop;

import org.systemsbiology.data.*;

/**
 * org.systemsbiology.hadoop.IAnalysisParameters
 * written by Steve Lewis
 * on Apr 8, 2010
 */
public interface IAnalysisParameters
{
    public static final IAnalysisParameters[] EMPTY_ARRAY = {};
    public static final Class THIS_CLASS = IAnalysisParameters.class;



    public boolean isObfuscate();

    public void setObfuscate(final boolean pObfuscate);

    /**
     * if true use special directory conventions
     * @return
     */
    public boolean isDataSourcesTCGA();

    public void setDataSourcesTCGA(boolean pDataSourcesTCGA);
    /**
     * return a way to write data to files or other streams
     *
     * @return
     */
    public IStreamFactory getDefaultStreamFactory();

    /**
     * true if we enforce parse constraints
     *
     * @return as above
     */
    public boolean isStrictParsing();

    /**
     * true if we enforce parse constraints
     *
     * @param pStrictParsing as above
     */
    public void setStrictParsing(boolean pStrictParsing);

    /**
     * currently options are human and yeast - may go to hg18, hg19 ...
     * @param s
     */
    public void setDefaultGenome(String s);

    public String getDefaultGenome();

    /**
     * if true write sam not bam files
     * @return
     */
    public boolean isOutputSam();

    public void setOutputSam(boolean isso);

    /**
     * this is really the file name
     * @return
     */
    public String getJobName();

    public void setJobName(String pChromosomeName);

    /**
     * return chromosome set - human or yeast
     * @return
     */

    public void setDefaultStreamFactory(IStreamFactory pDefaultStreamFactory);


    public String getLastInputSource();

    public void setLastInputSource(String pLastInputSource);


    /**
     * on hte first pass generate bam files with junk and clean data
     * after that all data should be clean
     * @return
     */
    public boolean isWriteFilteredAndJunk();

    public void setWriteFilteredAndJunk(boolean pWriteFilteredAndJunk);




}