package org.systemsbiology.aws;

import com.amazonaws.services.ec2.model.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.*;
import org.junit.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.hadoop.*;
import org.systemsbiology.xtandem.peptide.IProtein;
import org.systemsbiology.xtandem.taxonomy.*;
import org.systemsbiology.aws.*;
/**
 * org.systemsbiology.aws.S3RunTests
 * User: Steve
 * Date: 2/29/12
 */
public class S3RunTests {
    public static final S3RunTests[] EMPTY_ARRAY = {};

    @Test
    public void testLoad()
    {
        Configuration cfg = new Configuration();

        AWSUtilities.AWS_CONFIGURE_FILE_SYSTEM.configureFileSystem(cfg,"s3n://jxtandemtest/");
        cfg.set(XTandemHadoopUtilities.PATH_KEY, "s3n://jxtandemtest/Sample2/");
        cfg.set(XTandemHadoopUtilities.PARAMS_KEY, "s3n://jxtandemtest/Sample2/tandem.xml");
        XTandemMain.setRequiredPathPrefix("s3n://jxtandemtest/Sample2/");
          HadoopTandemMain main =  XTandemHadoopUtilities.loadFromConfiguration(null, cfg);
//        Assert.assertEquals( XTandemMain.getRequiredPathPrefix() + "tandem.xml",main.getTaskFile());
        Assert.assertEquals( XTandemMain.getRequiredPathPrefix() + "taxonomy.xml",main.getTaxonomyInfo());
           ITaxonomy taxonomy = main.getTaxonomy();
        IProtein[] proteins = taxonomy.getProteins();
        Assert.assertEquals(proteins.length,10);
         Assert.assertNotNull(main);

    }

    /**
     * todo pun your own numbers in
     */
    @Test
    public void readLogsDirectory()
    {
        String defaultDirectory = "jxtandemtest" ;
        String job = "j-1AAB6LPQXWWPJ" ;
         String uuid = "069d625a-b338-43c7-ade9-af311714e456";
        String answer = JobMonitor.findLogDirectory(job, 3, defaultDirectory, uuid);
        Assert.assertNotNull(answer);
        Assert.assertEquals("logs/j-1AAB6LPQXWWPJ/steps/60/",answer);
     }

}
