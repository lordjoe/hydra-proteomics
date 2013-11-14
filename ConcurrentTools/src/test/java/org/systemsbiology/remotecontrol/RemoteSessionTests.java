package org.systemsbiology.remotecontrol;

import org.junit.*;
import org.systemsbiology.common.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.hadoopgenerated.*;

/**
 * org.systemsbiology.remotecontrol.RemoteSessionTests
 * User: steven
 * Date: 5/26/11
 */
public class RemoteSessionTests {


    //    @Test
    @SuppressWarnings("UnusedDeclaration")
    public void testConnectionException() {
        RemoteSession rs = getRemoteSession();
        if (rs == null)   // not available
            return;
        rs.setConnected(false);
    }

    private RemoteSession getRemoteSession() {
        try {
            String user = RemoteUtilities.getUser(); // "training";  //
            String password = RemoteUtilities.getPassword(); // "training";  //
            String host = RemoteUtilities.getHost(); // "192.168.244.128"; // "hadoop1";
            RemoteSession rs = new RemoteSession(host, user, password);
            rs.setConnected(true);
            return rs;
        } catch (Throwable e) {
            while (e.getCause() != null)
                e = e.getCause();
            String localizedMessage = e.getLocalizedMessage();
            if (localizedMessage.contains("Connection timed out: connect"))
                return null;
            if (localizedMessage.contains("Permission denied"))
                return null;
            throw new RuntimeException(e);
        }
    }

    //    @Test
    @SuppressWarnings("UnusedDeclaration")
    public void testController() throws Exception {
        RemoteSession rs = getRemoteSession();
        if (rs == null)   // not available
            return;
        try {
            RemoteHadoopController controller = new RemoteHadoopController(rs);
            IFileSystem ftpAccessor = controller.getHDFSAccessor();
            // make sure there is no foobar
            if (ftpAccessor.exists("foobar")) {
                if (ftpAccessor.isDirectory("foobar"))
                    ftpAccessor.expunge("foobar");
                else
                    ftpAccessor.deleteFile("foobar");
            }
            Assert.assertFalse(ftpAccessor.exists("foobar"));
            // use exec to create it
            controller.executeCommand("mkdir foobar");
            Assert.assertTrue(ftpAccessor.exists("foobar"));
            Assert.assertTrue(ftpAccessor.isDirectory("foobar"));
            // use exec to delete it
            controller.executeCommand("rmdir foobar");
            Assert.assertFalse(ftpAccessor.exists("foobar"));
        } finally {
            rs.setConnected(false);
        }

    }

    @Test
    public void testNShotRunner() throws Exception {
        if (!RemoteTestConfiguration.isHDFSAccessible())
            return;
        try {
            String user = RemoteUtilities.getUser(); // "training";  //
            String password = RemoteUtilities.getPassword(); // "training";  //
            String host = RemoteUtilities.getHost(); // "192.168.244.128"; // "hadoop1";
            String defaultPath = RemoteUtilities.getDefaultPath();
            RemoteSession rs = new RemoteSession(host, user, password);
            rs.setConnected(true);

            final IHadoopController hc = new RemoteHadoopController(rs);

            IHadoopJob job = HadoopJob.buildJob3(
                    NShotTest.class,
                    null,
                    "/users/slewis/jobs",      // jar location
                    defaultPath + "/TestOutput",  // output location - will have outputN added
                    "res://NShot.jar"

            );
            //noinspection UnusedDeclaration
            boolean ret = hc.runJob(job);
        } catch (Exception e) {
            // throw new RuntimeException(e);  // todo run the test

        }
    }
}
