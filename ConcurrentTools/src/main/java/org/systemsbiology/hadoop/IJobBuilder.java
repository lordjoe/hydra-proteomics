package org.systemsbiology.hadoop;

import java.util.*;

/**
 * org.systemsbiology.hadoop.IJobBuilder
 * used by a number of Hadoop launch programs - this builds a
 * list of jobs to run
 *
 * @author Steve Lewis
 * @date 29/10/13
 */
public interface IJobBuilder {

    /**
     * if we start at the first job - how many jobs are there in total
     * @return
     */
    public int getNumberJobs();

    /**
     * build the list of jobs to run
     *
     * @return
     */
    @SuppressWarnings("UnusedDeclaration")
    public List<IHadoopJob> buildJobs();

    /**
      * build the list of jobs to run
      * NOTE look at ClusterLauncherJobBuilder.buildJobs for a good
      * sample - there are a few important steps there like setting the pass number which
      * sets the output directory name - default in Output + passnumber
     * passnumber is jobnumber + 1
      * @param  startjob job to start at (default = 0)
      * @return  !null
    */
    public List<IHadoopJob> buildJobs(int startjob);
}
