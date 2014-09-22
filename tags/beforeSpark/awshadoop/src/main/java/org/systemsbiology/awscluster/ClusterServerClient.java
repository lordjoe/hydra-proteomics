package org.systemsbiology.awscluster;

import com.amazonaws.auth.*;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.systemsbiology.aws.*;

import java.net.*;
//
///**
// * org.systemsbiology.awscluster.ClusterServerClient
// *
// * @author Steve Lewis
// * @date Oct 6, 2010
// */
//public class ClusterServerClient
//{
//    public static ClusterServerClient[] EMPTY_ARRAY = {};
//    public static Class THIS_CLASS = ClusterServerClient.class;
//
//    public static final String LOCAL_USEER = "local";
//    public static final String LOCAL_PASSWORD = "me";
//
//
//    private static URL gServerUrl = null;
//
//    public static URL getServerUrl()
//    {
//        return gServerUrl;
//    }
//
//    public static void setServerUrl(URL p_ServerUrl)
//    {
//        gServerUrl = p_ServerUrl;
//    }
//
//    private static ClusterServerClient gInstance;
//
//
//    public static synchronized ClusterServerClient getInstance()
//    {
//        if (gInstance == null)
//            gInstance = new ClusterServerClient();
//        return gInstance;
//    }
//
//    public static final int HTTP_PORT = 8081;
//    private AWSCredentials m_Credentials;
//    private boolean m_Registered;
//    private String m_BaseUrl = "http://localhost" + ":" +  HTTP_PORT + "/cluster/fun";
//    private DualStringValue m_UserPassword = new DualStringValue(LOCAL_USEER, LOCAL_PASSWORD);
//
//    private ClusterServerClient()
//    {
//    }
//
//    public boolean isRegistered()
//    {
//        return m_Registered;
//    }
//
//    public void setRegistered(boolean pRegistered)
//    {
//        m_Registered = pRegistered;
//    }
//
//    public AWSCredentials getCredentials()
//    {
//        return m_Credentials;
//    }
//
//    public void setCredentials(AWSCredentials pCredentials)
//    {
//        m_Credentials = pCredentials;
//    }
//
//    public void guaranteeReqistration()
//    {
//        if (isRegistered())
//            return;
//
//        register(getCredentials());
//        m_Registered = true;
//
//    }
//
//    public void register(AWSCredentials detail)
//    {
//        String query = buildRegistryQuery(getCredentials());
//
//        URL u = getServerUrl();
//        if (query.length() > 0) {
//            try {
//                final String s = u.toString();
//                final String spec = s + "?" + query;
//                u = new URL(spec);
//            }
//            catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        String response = ServerUtilities.readURL(u);
//
//
//    }
//
//
//    /**
//     * make sure the server is running and available - if using a local server than launch it
//     */
//    protected void guaranteeServer()
//    {
//        String query = "action=ping";
//
//        URL u = getServerUrl();
//        final String s = u.toString();
//        try {
//            final String spec = s + "?" + query;
//            u = new URL(spec);
//        }
//        catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//
//        String response = ServerUtilities.readURL(u);
//        if ("ping".equals(response))
//            return; // server is running
//        if(s.startsWith("http://localhost"))
//              launchServer();
//
//    }
//
//
//    protected void launchServer()
//    {
//        final String cp = ClassPathUtilities.getClasspath();
//        final String cls = ClusterServer.class.getName();
//        Process p = ClassPathUtilities.launchProcess(cp, cls);
//
//
//    }
//
//    public String getClusterId(JobFlowInstancesDetail detail)
//    {
//
//        guaranteeServer();
//        guaranteeReqistration();
//        String query = buildClusterQuery(detail);
//        query = "action=" + ClusterServiceHandler.HARD_CLUSTER_ACTION + "&" + buildUserPasswordQuery() + query;
//        URL u = getServerUrl();
//        if (query.length() > 0) {
//            try {
//                u = new URL(u.toString() + "?" + query);
//            }
//            catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return ServerUtilities.readURL(u);
//
//    }
//
//    protected String buildUserPasswordQuery()
//    {
//        return "user=" + LOCAL_USEER + "&password=" + LOCAL_PASSWORD + "&";
//    }
//
//    protected String buildRegistryQuery(AWSCredentials creds)
//    {
//        return
//                "action=" + ClusterServiceHandler.REGISTER_ACTION + "&" + buildUserPasswordQuery() +
//                        "AWSAccessKeyId" + "=" + creds.getAWSAccessKeyId() + "&" + "AWSSecretKey" + "=" + creds.getAWSSecretKey();
//    }
//
//
//    protected String buildClusterQuery(JobFlowInstancesDetail request)
//    {
//        StringBuilder query = new StringBuilder();
//
//
//        Object test = null;
//        test = request.getInstanceCount();
//        if (test != null) {
//            final String value = test.toString();
//            query.append(ServerUtilities.buildPropertyString("InstanceCount", value));
//        }
//
//        test = request.getEc2KeyName();
//        if (test != null) {
//            final String value = test.toString();
//            query.append("&" + ServerUtilities.buildPropertyString("Ec2KeyName", value));
//        }
//
//        test = request.getHadoopVersion();
//        if (test != null) {
//            final String value = test.toString();
//            query.append("&" + ServerUtilities.buildPropertyString("HadoopVersion", value));
//        }
//
//        test = request.getMasterInstanceType();
//        if (test != null) {
//            final String value = test.toString();
//            query.append("&" +
//                    ServerUtilities.buildPropertyString("MasterInstanceType", value));
//        }
//
//        test = request.getSlaveInstanceType();
//        if (test != null) {
//            final String value = test.toString();
//            query.append("&" + ServerUtilities.buildPropertyString("SlaveInstanceType", value));
//        }
//
//        return query.toString();
//
////        try {
////            return URLEncoder.encode(query.toString(), "UTF-8");
////        }
////        catch (UnsupportedEncodingException e) {
////            throw new RuntimeException(e);
////        }
//    }
//
//
//    public String getSoftClusterId(JobFlowInstancesDetail detail)
//    {
//        guaranteeReqistration();
//        String query = buildClusterQuery(detail);
//        query = "action=" + ClusterServiceHandler.SOFT_CLUSTER_ACTION + "&" + buildUserPasswordQuery() + query;
//
//        URL u = getServerUrl();
//        if (query.length() > 0) {
//            try {
//                final String spec = u.toString() + "?" + query;
//                u = new URL(spec);
//            }
//            catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return ServerUtilities.readURL(u);
//    }
//
//    public static String getMinimalCluster()
//    {
//        return getCluster(AWSClusterUtilities.getMinimalClusterRequest());
//    }
//
//    public static String getCluster(final JobFlowInstancesDetail jd)
//    {
//        ClusterServerClient client = null;
//         try {
//            client = new ClusterServerClient();
//            client.setCredentials(AWSUtilities.getCredentials());
//            ClusterServerClient.setServerUrl(new URL(client.m_BaseUrl));
//              String id = client.getClusterId(jd);
//            return id;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public static void main(String[] args)
//    {
//        getMinimalCluster();
//
//    }
//
//
//
//}
