package com.lordjoe.server;

import org.mortbay.jetty.*;
import org.mortbay.jetty.servlet.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * com.lordjoe.server.RestianServer
 * code illustrating the use of a sample Jetty stand alone server
 *      call  http://localhost:8080/rest?i1=3&i2=5
 *      returns "8"
 *
 * @author Steve Lewis
 * @date Nov 28, 2007
 */
public class RestianServer extends HttpServlet {
    public static RestianServer[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = RestianServer.class;

    protected void doGet(HttpServletRequest pHttpServletRequest, HttpServletResponse rsp)
            throws ServletException, IOException {
        doPost(pHttpServletRequest, rsp);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
            throws ServletException, IOException {
        String ret = null;
        String s1 = req.getParameter("i1");
        String s2 = req.getParameter("i2");
        Integer sum = new Integer(s1) + new Integer(s2);

        respond(rsp, sum.toString());

    }


    private void respond(HttpServletResponse rsp, String pRet) throws IOException {
        PrintWriter out = new PrintWriter(rsp.getOutputStream());
        out.println(pRet);
        out.flush();
        out.close();
    }


    public static final int PORT = 8080;

    public static void launchServer()
            throws Exception {
        Server server = new Server(PORT);
        Context root = new Context(server, "/", Context.SESSIONS);
        ServletHolder holder = new ServletHolder(new RestianServer());
        root.addServlet(holder, "/rest");
        server.start();
    }

    public static void main(String[] args) throws Exception {
        launchServer();
    }
}
