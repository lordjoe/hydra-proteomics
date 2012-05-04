package org.systemsbiology.awscluster;

import com.amazonaws.*;
import com.amazonaws.auth.*;
import com.amazonaws.services.elasticmapreduce.*;
import com.amazonaws.services.elasticmapreduce.model.*;
import org.apache.commons.httpclient.*;
import org.systemsbiology.aws.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.awscluster.ClusterServiceHandler
 *
 * @author Steve Lewis
 * @date Oct 4, 2010
 */
public class ClusterServiceHandler extends HttpServlet
{
    public static ClusterServiceHandler[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = ClusterServiceHandler.class;

    public static final String ACTION_TAG = "action";
    public static final String PING_ACTION = "ping";
    public static final String REGISTER_ACTION = "register";
    public static final String SOFT_CLUSTER_ACTION = "soft_get_cluster";
    public static final String HARD_CLUSTER_ACTION = "hard_get_cluster";

    private final Map<DualStringValue, AmazonElasticMapReduce> m_UserPWTOCredeitials =
            new HashMap<DualStringValue, AmazonElasticMapReduce>();


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/plain");
   //     Request req = request instanceof Request ? (Request) request : HttpConnection.getCurrentConnection().getRequest();
        // baseRequest.getAttribute()
        String type = (String) request.getParameter(ACTION_TAG);

        Map<String, String> params = getParams(request);
        String resp = buildResponse(type, params);

        final ServletOutputStream outputStream = response.getOutputStream();
        PrintWriter out = new PrintWriter(outputStream);
        out.print(resp);
        out.flush();
        out.close();

       // req.setHandled(true);
    }


    protected Map<String, String> getParams(HttpServletRequest reg)
    {
        Map<String, String> params = new HashMap<String, String>();
        Enumeration<String> e = reg.getParameterNames();
        while (e.hasMoreElements()) {
            String s = e.nextElement();
            if (ACTION_TAG.equals(s)) {
                continue;
            }
            params.put(s, (String) reg.getParameter(s));
        }
        return params;
    }


    protected String buildResponse(String type, Map<String, String> params)
    {
        if (type.equals(PING_ACTION))
            return "ping";      // I am alive

        if (type.equals(REGISTER_ACTION))
            return buildRegisterResponse(params);

        if (type.equals(SOFT_CLUSTER_ACTION))
            return buildClusterResponse(params, false);

        if (type.equals(HARD_CLUSTER_ACTION))
            return buildClusterResponse(params, true);

        return buildErrorResponse(type);
    }


    protected String buildErrorResponse(String type)
    {
        return "Error - type " + type + " is not recognized ";
    }

    protected DualStringValue extractUser(Map<String, String> params)
    {

        final String user = params.get("user");
        final String password = params.get("password");
        DualStringValue ds1 = new DualStringValue(user, password);
        return ds1;
    }


    protected AmazonElasticMapReduce extractService(Map<String, String> params)
    {

        return m_UserPWTOCredeitials.get(extractUser(params));
    }

    protected String buildRegisterResponse(Map<String, String> params)
    {
        DualStringValue ds1 = extractUser(params);

        final String AWSSecretKey = params.get("AWSSecretKey");
        final String AWSAccessKeyId = params.get("AWSAccessKeyId");
        AWSCredentials ds2 = new BasicAWSCredentials(AWSAccessKeyId, AWSSecretKey);
        AmazonElasticMapReduce service = new AmazonElasticMapReduceClient(ds2);
        m_UserPWTOCredeitials.put(ds1, service);

        return "OK";
    }

    protected String buildClusterResponse(Map<String, String> params, boolean exactMatch)
    {
        AmazonElasticMapReduce service = extractService(params);

        int maxLife = 1000 * 60 * 60 * 2; // 2 hours
        final String ml = params.get("MaxLife");
          if (ml != null) {
              maxLife =Integer.parseInt(ml);
         }

         JobFlowInstancesDetail jd = AWSClusterUtilities.requestFromParams(params);

        throw new UnsupportedOperationException("Fix This"); // ToDo
  //      final JobFlowDetail jobFlowDetail = AWSClusterUtilities.guaranteeJob(service, jd,maxLife);
   //     return jobFlowDetail.getJobFlowId();
    }

}
