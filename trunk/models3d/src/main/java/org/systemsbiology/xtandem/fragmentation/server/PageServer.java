package org.systemsbiology.xtandem.fragmentation.server;

import com.lordjoe.utilities.*;
import org.apache.jasper.servlet.*;
import org.mortbay.jetty.*;
import org.mortbay.jetty.handler.*;
import org.mortbay.jetty.nio.*;
import org.mortbay.jetty.servlet.*;
import org.mortbay.resource.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;

/**
 * com.lordjoe.server.RestianServer
 * code illustrating the use of a sample Jetty stand alone server
 *      call  http://localhost:8080/rest?i1=3&i2=5
 *      returns "8"
 *
 * @author Steve Lewis
 * @date Nov 28, 2007
 */
public class PageServer extends HttpServlet {
    public static PageServer[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = PageServer.class;

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    public static final File PAGE_DIRECTORY = new File("Pages");

    protected void doGet(HttpServletRequest pHttpServletRequest, HttpServletResponse rsp)
            throws ServletException, IOException {
        doPost(pHttpServletRequest, rsp);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
            throws ServletException, IOException {

        String requestURI = req.getRequestURI();
        if(uriHandled(requestURI,req,   rsp))
           return;
        String ret = null;
        String uniprot = req.getParameter("uniprot");



        String s2 = req.getParameter("fragments");
        File page = new File(PAGE_DIRECTORY,uniprot + ".html");
        if(page.exists())
            servePage(  rsp,   page);
        else
            serveEmptyPage(rsp);

    }

    private boolean uriHandled(final String requestURI, final HttpServletRequest req, final HttpServletResponse rsp) {
        try {
            if("/".equals(requestURI))  {
                serveEmptyPage(  rsp );
                return true; // handled
            }
            if(requestURI.startsWith("/"))   {
                serveFile(req, rsp);
                 return true; // handled
             }

            if (true)
                throw new UnsupportedOperationException("Fix This"); // ToDo

            return false;
        }
        catch ( Exception e) {
            throw new RuntimeException(e);

        }
    }

    protected void serveFile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get requested file by path info.
        String requestedFile = request.getPathInfo();
        requestedFile = request.getRequestURI().substring(1); // drop leading /

        // Check if file is actually supplied to the request URI.
        if (requestedFile == null) {
            // Do your thing if the file is not supplied to the request URI.
            // Throw an exception, or send 404, or show default/warning page, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        File base = FileUtilities.getUserDirectory();

        // Decode the file name (might contain spaces and on) and prepare file object.
        File file = new File(base, URLDecoder.decode(requestedFile, "UTF-8"));

        // Check if file actually exists in filesystem.
        if (!file.exists()) {
            // Do your thing if the file appears to be non-existing.
            // Throw an exception, or send 404, or show default/warning page, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(file.getName());

        // If content type is unknown, then set the default value.
        // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
        // To add new content types, add new mime-mapping entry in web.xml.
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Init servlet response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
//        response.setHeader("Content-Length", String.valueOf(file.length()));
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        // Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            // Open streams.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }
        finally {
            // Gently close streams.
            close(output);
            close(input);
        }
    }

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            }
            catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
                e.printStackTrace();
            }
        }
    }


    public static final int BUFSIZE = 4096;

    /**
       *  Sends a file to the ServletResponse output stream.  Typically
       *  you want the browser to receive a different name than the
       *  name the file has been saved in your local database, since
       *  your local names need to be unique.
       *
       *  @param req The request
       *  @param resp The response
       *  @param filename The name of the file you want to download.
       *  @param original_filename The name the browser should receive.
       */
      private void doDownload( HttpServletRequest req, HttpServletResponse resp,
                               String filename, String original_filename )
          throws IOException
      {
          File                f        = new File(filename);
          int                 length   = 0;
          ServletOutputStream op       = resp.getOutputStream();
          ServletContext      context  = getServletConfig().getServletContext();
          String              mimetype = context.getMimeType( filename );

          //
          //  Set the response and go!
          //
          //
          resp.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
          resp.setContentLength( (int)f.length() );
          resp.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );

          //
          //  Stream to the requester.
          //
          byte[] bbuf = new byte[BUFSIZE];
          DataInputStream in = new DataInputStream(new FileInputStream(f));

          while ((in != null) && ((length = in.read(bbuf)) != -1))
          {
              op.write(bbuf,0,length);
          }

          in.close();
          op.flush();
          op.close();
      }

    @Override
    public void service(final ServletRequest req, final ServletResponse res) throws ServletException, IOException {

        super.service(req, res);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        String requestedFile = req.getRequestURI();
        System.err.println(requestedFile);
        super.service(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void servePage(HttpServletResponse rsp, File page) throws IOException {
        ServletOutputStream os = rsp.getOutputStream();
        FileInputStream is = new FileInputStream(page);
        FileUtilities.copyStream(is,os);

    }

    public void serveEmptyPage(HttpServletResponse rsp ) throws IOException {
        File page = new File("Index.html");
         ServletOutputStream os = rsp.getOutputStream();
         FileInputStream is = new FileInputStream(page);
         FileUtilities.copyStream(is,os);

     }



    public static final int PORT = 8080;

    public static Server launchServer()
            throws Exception {

        Server server = new Server(PORT);
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
         resource_handler.setWelcomeFiles(new String[]{ "index.html" });

        resource_handler.setResourceBase(".");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);

        ServletHolder holder = new ServletHolder(new PageServer());
     //   ServletHolder holder2 = new ServletHolder(new FileServlet());
        Context root = new Context(server, "/svlt", Context.SESSIONS);
        root.addServlet(holder, "/");
        handlers.setHandlers(new Handler[] { resource_handler,root });


 //        HandlerList handlers = new HandlerList();
//             handlers.setHandlers(new Handler[]{ root, new DefaultHandler()});
//
//        server.setHandler(handlers);

//        root.addHandler(resource_handler);
   //     root.addServlet(holder, "/");
         server.start();
        return server;
    }

    public static void main(String[] args) throws Exception {
        Server server = launchServer();
        server.join();
        while(server.isRunning());
    }
}
