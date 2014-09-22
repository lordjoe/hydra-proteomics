package com.lordjoe.server;

import org.mortbay.jetty.*;
import org.mortbay.jetty.webapp.*;

import java.io.*;
import java.util.*;

/**
 * com.lordjoe.server.AppServer
 *   code illustrating the use of a sample Jetty stand alone server
 *
 * @author Steve Lewis
 * @date Apr 19, 2007
 */
public class AppServer
{
    public static SiteRunner[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = AppServer.class;

    public static final int SERVER_PORT = 8091;
    public static final String SERVER_DIRECTORY = "/Pargrapher";


    public static final String[] DEFAULT_WELCOME_FILES = {"index.html", "index.htm"};

    public static final String[] JSP_WELCOME_FILES = {"index.jsp"};

    private final Server m_Server;

    public AppServer(String[] args)
    {
        Server server = new Server(SERVER_PORT);
        buildServer2(new File(SERVER_DIRECTORY), server);
        m_Server = server;
    }


    private void buildServer2(File pSiteDir, Server pServer)
    {
        WebAppContext hdlr = new WebAppContext();
        hdlr.setContextPath("/");
        hdlr.setResourceBase(pSiteDir.getAbsolutePath());
        hdlr.setWelcomeFiles(DEFAULT_WELCOME_FILES);
        pServer.setHandler(hdlr);
        //       ResourceBundle rb = ResourceBundle.getBundle("resources.resources");
        //        LocalizationContext ctx = new LocalizationContext(rb);
    }


    public void start() throws Exception
    {
        try {
            m_Server.start();
        }
        catch (Exception e) {
            e.printStackTrace(); // ignore
        }
    }


    public static void main(String[] args) throws Exception
    {
        AppServer siteRunner = new AppServer(args);
        siteRunner.start();
        while (true) ;
    }
}