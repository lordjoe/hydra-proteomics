package org.systemsbiology.awscluster;


//import static javax.servlet.http.HttpServletResponse.SC_OK;
//
//import java.io.*;
//
//import javax.servlet.*;
//import javax.servlet.http.*;
//
//import org.mortbay.jetty.*;
//import org.mortbay.jetty.handler.*;
//import org.mortbay.jetty.nio.*;
//import org.mortbay.jetty.servlet.*;
//
//
///**
// * org.systemsbiology.awscluster.ClusterServer
// *
// * @author Steve Lewis
// * @date Oct 4, 2010
// */
//public class ClusterServer
//{
//    public static ClusterServer[] EMPTY_ARRAY = {};
//    public static Class THIS_CLASS = ClusterServer.class;
//
//
//    public static final int HTTP_PORT = 8081;
//
//    private Server m_Server;
//
//    private ClusterServer()
//    {
//    }
//
//
//    public void start() throws Exception
//    {
//        configureServer();
//        startServer();
//    }
//
//    protected void startServer() throws Exception
//    {
//        m_Server.start();
//    }
//
//    protected void configureServer()
//    {
//        m_Server = new Server(HTTP_PORT);
//         ContextHandlerCollection contexts = new ContextHandlerCollection();
//        m_Server.setHandler(contexts);
//
//
//        Context context = new Context(contexts, "/cluster");
//        context.addServlet(ClusterServiceHandler.class, "/fun");
//
//
//
//    }
//
//
//
//
//    public void stop() throws Exception
//    {
//        m_Server.stop();
//    }
//
//
//    protected Server getServer()
//    {
//        return m_Server;
//    }
//
//    public static void main(String[] args) throws Exception
//    {
//        ClusterServer cs = new ClusterServer();
//        cs.start();
//    }
//}
