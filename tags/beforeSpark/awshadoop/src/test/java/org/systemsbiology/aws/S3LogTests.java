package org.systemsbiology.aws;

import org.junit.*;
import org.systemsbiology.aws.*;

/**
 * org.systemsbiology.aws.S3LogTests
 * User: steven
 * Date: 3/28/12
 */
public class S3LogTests {
    public static final S3LogTests[] EMPTY_ARRAY = {};

    /**
     * not real it depends on aspecific case but code to test finding log directories
     * ToDo handle the unhappy case when the directory does nto exist
     */
    @Test
    public void testGetLogs() {
        String jobName = "j-1AAB6LPQXWWPJ";
        String defaultDirectory = "logs";
        String uuid = "0e33ba24-3703-4b54-b526-bf6fc6e5bb7b";
        String dir = JobMonitor.findLogDirectory(jobName,1000, defaultDirectory, uuid);
        Assert.assertEquals("logs/j-1AAB6LPQXWWPJ/steps/12/", dir);

        uuid = "4524fc02-a159-4641-bfb3-df55625188b6";
        dir = JobMonitor.findLogDirectory(jobName,1000, defaultDirectory, uuid);
        Assert.assertEquals("logs/j-1AAB6LPQXWWPJ/steps/14/", dir);

        uuid = "6c629137-9565-4c06-8cb2-b90047627dc0";
        dir = JobMonitor.findLogDirectory(jobName,1000, defaultDirectory, uuid);
        Assert.assertEquals("logs/j-1AAB6LPQXWWPJ/steps/15/", dir);
    }

}
