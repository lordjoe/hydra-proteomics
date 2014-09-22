package org.systemsbiology.hadoop;

/**
 * org.systemsbiology.hadoop.IJobBuilderFactory
 *
 * @author Steve Lewis
 * @date 01/11/13
 */
public interface IJobBuilderFactory {

    /**
     * build a factory given a launcher
     * @param launcher
     * @return
     */
    public IJobBuilder getJobBuilder(IStreamOpener launcher,Object... otherData);
}
