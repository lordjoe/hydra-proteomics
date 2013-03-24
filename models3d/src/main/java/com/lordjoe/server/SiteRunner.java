package com.lordjoe.server;

import org.mortbay.jetty.*;
import org.mortbay.jetty.webapp.*;

//import javax.servlet.jsp.jstl.core.*;
//import javax.servlet.jsp.jstl.fmt.*;
import java.io.*;
import java.util.*;

/**
 * com.lordjoe.server.SiteRunner
 *
 * @author Steve Lewis
 * @date Apr 19, 2007
 */
public class SiteRunner
{
    public static SiteRunner[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = SiteRunner.class;

    public static final String[] DEFAULT_WELCOME_FILES = { "index.html","index.htm" };

    public static final String[] JSP_WELCOME_FILES = { "index.jsp" };

    private final List<Server> m_Servers;

    public SiteRunner(String[] args)
    {
        m_Servers = new ArrayList<Server>();
        int index = 0;
        while(index < args.length)
            index = handleArgs(index,args);
    }

    public static void usage()
    {
        System.out.println( "usage  >java com.lordjoe.server.SiteRunner <port1> <basedir1> <port2> <basedir2> ...");
    }

    protected int handleArgs(int start,String[] args)
    {

        int index = start;
        int port = Integer.parseInt(args[index++]);
        File siteDir = new File(args[index++]);
        Server server = new Server(port);
         addBlog(siteDir, server);
    //    buildServer2(siteDir, server);
        m_Servers.add(server);
        for(; index < args.length; )  {
             port = Integer.parseInt(args[index++]);
             siteDir = new File(args[index++]);
             server = new Server(port);
            buildServer2(siteDir, server);
            m_Servers.add(server);
        }

        return index;
    }

    private void addBlog(File pSiteDir, Server pServer)
    {
        WebAppContext hdlr = new WebAppContext();
        hdlr.setContextPath("/blog");
    //    hdlr.setResourceAlias();
        hdlr.setResourceBase(pSiteDir.getAbsolutePath() + "/blog");
        hdlr.setWelcomeFiles(JSP_WELCOME_FILES);
        pServer.setHandler(hdlr);
        hdlr = new WebAppContext();
        hdlr.setContextPath("/");
        hdlr.setResourceBase(pSiteDir.getAbsolutePath());
        hdlr.setWelcomeFiles(DEFAULT_WELCOME_FILES);
        pServer.addHandler(hdlr);
    }

    private void buildServer2(File pSiteDir, Server pServer)
    {
        WebAppContext hdlr = new WebAppContext();
        hdlr.setContextPath("/");
        hdlr.setResourceBase(pSiteDir.getAbsolutePath());
        hdlr.setWelcomeFiles(DEFAULT_WELCOME_FILES);
        pServer.setHandler(hdlr);
 //       ResourceBundle rb = ResourceBundle.getBundle("resources");
  //       LocalizationContext ctx = new LocalizationContext(rb);
        }


    public void start() throws Exception
    {
        for (Iterator<Server> iterator = m_Servers.iterator(); iterator.hasNext();) {
            Server server =  iterator.next();
            try {
                server.start();
          }
            catch (Exception e) {
                e.printStackTrace(); // ignore
            }
        }
    }


    public static void main(String[] args)  throws Exception
    {
        if(args.length == 0 || args.length % 2 == 1)   {
            usage();
            return;
        }
        SiteRunner siteRunner = new SiteRunner(args);
        siteRunner.start();
        while(true);
    }
}
