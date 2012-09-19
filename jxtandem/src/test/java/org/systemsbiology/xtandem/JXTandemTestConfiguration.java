package org.systemsbiology.xtandem;

import org.systemsbiology.hadoop.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.xtandem.taxonomy.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.JXTandemTestConfiguration
 * manages whether tests can run on the basis of access to resources such as
 * the cluster, hdfs, databases ...
 * User: Steve
 * Date: 8/25/11
 */
public class JXTandemTestConfiguration {
    public static final JXTandemTestConfiguration[] EMPTY_ARRAY = {};

    private static Map<String, Boolean> gConditionToAvailability = new HashMap<String, Boolean>();

    public static boolean isHDFSAccessible() {

        HDFSAccessor access = null;
        String host = RemoteUtilities.getHost();
        int port = RemoteUtilities.getPort();
        String user = RemoteUtilities.getUser();
        RemoteUtilities.getPassword()
        String connStr = host + ":" + port + ":" + user + ":" + RemoteUtilities.getPassword();
        Boolean ret = gConditionToAvailability.get(connStr);
        if (ret == null) {
            try {
                access = new HDFSAccessor(host, port);
                ret = Boolean.TRUE;
            }
            catch (Exception e) {
                ret = Boolean.FALSE;
            }

//             try {
//                 new FTPWrapper(RemoteUtilities.getUser(), RemoteUtilities.getPassword(), RemoteUtilities.getHost());
//                 ret = Boolean.TRUE;
//             }
//             catch (Exception e) {
//                 ret = Boolean.FALSE;
//
//             }
            gConditionToAvailability.put(connStr, ret);
        }
        return ret;
    }


//    public static boolean isDatabaseAccessible(IParameterHolder data) {
//        String connStr = SpringJDBCUtilities.buildConnectionString(data);
//        Boolean ret = gConditionToAvailability.get(connStr);
//        if (ret == null) {
//            try {
//                SpringJDBCUtilities.buildDataSource(data);
//                ret = Boolean.TRUE;
//            }
//            catch (Exception e) {
//                ret = Boolean.FALSE;
//
//            }
//            gConditionToAvailability.put(connStr, ret);
//        }
//        return ret;
//    }

}
