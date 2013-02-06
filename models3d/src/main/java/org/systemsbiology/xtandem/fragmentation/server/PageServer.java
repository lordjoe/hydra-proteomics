package org.systemsbiology.xtandem.fragmentation.server;

import com.lordjoe.utilities.*;
import org.mortbay.jetty.*;
import org.mortbay.jetty.handler.*;
import org.mortbay.jetty.nio.*;
import org.mortbay.jetty.servlet.*;
import org.systemsbiology.jmol.*;
import org.systemsbiology.uniprot.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.fragmentation.ui.*;
import org.systemsbiology.xtandem.peptide.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * org.systemsbiology.xtandem.fragmentation.server.PageServer
 * code illustrating the use of a sample Jetty stand alone server
 * call  http://localhost:8080/rest?i1=3&i2=5
 * returns "8"
 *
 * @author Steve Lewis
 * @date Nov 28, 2007
 */
/*
Try this form
 <form id="5667777777461" ACTION="/svlt/handle"   method="get" >
 Uniprot Id: <input type="text" name="uniprot" value="A2RUC4" ></input><br>
 Fragments:<br>
 <textarea name="fragments" rows="20" cols="70">
 A[43.0]GQHLPVPR
 DAQYLYLK
 EEQFFSSVFR
 EQFMQHLYPQR
 EQFM[147.0]QHLYPQR
 FPEFFKEEQFFSSVFR
 FPEFFKEEQFFSSVFR
 GDIKFPEFFK
 GDIKFPEFFK
 GDIKFPEFFKEEQFFSSVFR
 YEC[160.0]SLEAGDVLFIPALWFH
 YEC[160.0]SLEAGDVLFIPALWFH
 </textarea><br>
 <input type="submit">
 </form>
 */
public class PageServer extends HttpServlet {
    public static PageServer[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = PageServer.class;

    public static final String SERVLET_URI = "/svlt";

    public static final String BUILD_PAGE_NAME = "Build Page";
    public static final String RESET_PAGE_NAME = "Reset Page";
    public static final String CHECK_PAGE_NAME = "CheckPage";
    public static final String INDEX_PAGE = "IndexGood.html";

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    public static final String PAGE_DIRECTORYX =  "pages" ;
    public static final File PAGE_DIRECTORY_URL = new File("pages");
    public static final String BASE_DIRECTORY = "Spaghetti";

    public static File getPageDirectory()
    {
        File f = ProteinDatabase.getHomeDirectory(PAGE_DIRECTORYX,BASE_DIRECTORY ) ;
        return f;
    }

    private boolean m_PagesLoaded;
    private ProteinCoveragePageTracker m_Tracker;
    private Map<String, String> m_PagesUnderConstruction = new ConcurrentHashMap<String, String>();

    public PageServer() {
        m_Tracker = new ProteinCoveragePageTracker(getPageDirectory());
        String actionUrl = "/spaghetti/svlt";

        // add some test data with a model
        SampleDataHolder sd = SampleDataHolder.getInstance();
        SampleDataHolder.SampleData sample = sd.getSample();
        String urlStr = sample.getId();
        String urlError = null;
        String fragments = sample.getFragmentStr();

        File page = new File(getPageDirectory(), urlStr + ".html");
        page.delete(); // force rebuild

        m_Tracker.buildIndexPage(INDEX_PAGE, actionUrl, urlStr, urlError, fragments);

    }

    protected void doGet(HttpServletRequest pHttpServletRequest, HttpServletResponse rsp)
            throws ServletException, IOException {
        doPost(pHttpServletRequest, rsp);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse rsp)
            throws ServletException, IOException {

        String requestURI = req.getRequestURI();
        String uniprot = req.getParameter("uniprot");
        if (uniprot == null && shouldForward(req, requestURI)) {
            if (requestURI.contains(SERVLET_URI)) {
                String newPage = requestURI.replace(SERVLET_URI, "");

                redirect(newPage, rsp);

            }
            else {
                serveFile(req, rsp);

            }
            return;
        }

        String ret = null;

        HttpSession session = req.getSession();
        String id = session.getId();

        if (uniprot == null || "".equals(uniprot)) {
//            // let someone else handle it
//            Request base_request = (req instanceof Request) ? (Request)req:HttpConnection.getCurrentConnection().getRequest();
//            base_request.setHandled(false);
//            String newPage = requestURI.replace(SERVLET_URI,"") ;
//            forward(newPage, req, rsp);
//           //   serveEmptyPage(rsp);
//            return;
        }

        String submitName = req.getParameter("Submit");
        if ("BUILDING".equals(submitName)) {
            String fileName = getPageDirectory() + "/" + uniprot + ".html?uniprot=BUILDING&Submit=BUILDING";
            String waitpage = m_PagesUnderConstruction.get(uniprot);
            if (waitpage != null) {
                returnWaitPage(waitpage, rsp);
                return;
            }
            else {
                String newPage =  "/" + getPageDirectory() + "/" + uniprot + ".html";
                redirect(newPage, rsp);

            }

        }
        if (BUILD_PAGE_NAME.equals(submitName)) {
            String fragments = req.getParameter("fragments");
            File page = new File(getPageDirectory(), uniprot + ".html");
            String path = page.getAbsolutePath();
            if (page.exists()) {
                String newPage = PAGE_DIRECTORY_URL + "/" + page.getName();
                redirect(newPage, rsp);
            }
            else {
                String fileName = PAGE_DIRECTORY_URL + "/" + uniprot + ".html?uniprot=" + uniprot + "&Submit=BUILDING";
                String waitpage = m_PagesUnderConstruction.get(uniprot);
                if (waitpage == null) {
                    waitpage = buildWaitPage(uniprot, SERVLET_URI + "/" + fileName, rsp);
                    returnWaitPage(waitpage, rsp);
                    m_PagesUnderConstruction.put(uniprot, waitpage);
                    buildPage(uniprot, fragments, rsp);
                    m_PagesUnderConstruction.remove(uniprot);
                }
                else {
                    returnWaitPage(waitpage, rsp);
                }

                //  String newPage = buildPage(uniprot, fragments, rsp);
                //  redirect(newPage, rsp);
            }

        }
        if (RESET_PAGE_NAME.equals(submitName)) {
            String actionUrl = "/svlt";

            // add some test data with a model
            SampleDataHolder sd = SampleDataHolder.getInstance();
            SampleDataHolder.SampleData sample = sd.getSample();
            String urlStr = sample.getId();
            String urlError = null;
            String fragments = sample.getFragmentStr();

            File page = new File(getPageDirectory(), urlStr + ".html");
            page.delete(); // force rebuild

            m_Tracker.buildIndexPage(INDEX_PAGE, actionUrl, urlStr, urlError, fragments);
            redirect(INDEX_PAGE, rsp);

        }

    }

    private String buildWaitPage(final String id, final String url, final HttpServletResponse rsp) {

        String page = WaitPageBuilder.buildWaitPage(id, url, 3);
        return page;
    }

    private void returnWaitPage(final String page, final HttpServletResponse rsp) {

        try {
            PrintWriter writer = rsp.getWriter();
            writer.println(page);
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }


    }



    public static FoundPeptide[] fragmentsToFoundPeptides(Protein protein, String fragmants) {
        if (fragmants == null)
            return FoundPeptide.EMPTY_ARRAY;
        List<FoundPeptide> holder = new ArrayList<FoundPeptide>();
        String[] frags = fragmants.split("\n");
        for (int i = 0; i < frags.length; i++) {
            String frag = frags[i].trim();
            IPolypeptide pp = Polypeptide.fromString(frag);
            FoundPeptide fp = new FoundPeptide(pp, protein.getId(), 0);
            holder.add(fp);
        }
        FoundPeptide[] ret = new FoundPeptide[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    private String buildPage(String uiprotId, String fragments, HttpServletResponse rsp) {
        try {
            String file = getPageString(uiprotId, fragments);
            return file;
        }
        catch (Exception e) {
            e.printStackTrace();
            buildBadUniprotPage(uiprotId, fragments, rsp);
            return null;
        }
    }

    public static String getPageString(String uiprotId, String fragments) {
        ProteinDatabase pd = ProteinDatabase.getInstance();
        Protein protein = pd.getProtein(uiprotId);
        if (protein == null) {
            throw new UnsupportedOperationException("Cannot Find Protein " + uiprotId);
        }
        String sequence = protein.getSequence();
        FoundPeptide[] peptides = fragmentsToFoundPeptides(protein, fragments);
        ProteinFragmentationDescription pfd = new ProteinFragmentationDescription(uiprotId, null, protein, peptides);
        Uniprot up = Uniprot.getUniprot(uiprotId);
        BioJavaModel bestModel = up.getBestModel();

        if (bestModel != null) {

            File file = bestModel.getFile();
            pfd.setModelFile(file);
        }


        return ProteinCoveragePageBuilder.buildFragmentDescriptionPage(uiprotId, null, null, pfd);
    }

    public ProteinCoveragePageTracker getTracker() {
        return m_Tracker;
    }

    public static final FilenameFilter HTML_FILTER = new FileUtilities.EndsWithFilter(".html");

    protected String buildIndexPage(File pageDir) {
        File[] files = pageDir.listFiles(HTML_FILTER);
        //  throw new UnsupportedOperationException("Fix This"); // ToDo
//        ProteinCollection proteins = getProteins();
//        HTMLPageBuillder pb = new HTMLPageBuillder("Protein Coverage ");
//        HTMLBodyBuillder body = pb.getBody();
//
//        List<ProteinFragmentationDescription> idWith3dModel = new ArrayList<ProteinFragmentationDescription>();
//        List<ProteinFragmentationDescription> idWithout3dModel = new ArrayList<ProteinFragmentationDescription>();
//        List<String> pageWith3dModel = new ArrayList<String>();
//        List<String> pageWithout3dModel = new ArrayList<String>();
//        List<ProteinFragmentationDescription> pfdWith3dModel = new ArrayList<ProteinFragmentationDescription>();
//
//        for (int i = 0; i < ids.length; i++) {
//            String id = ids[i];
//            String page = pages[i];
//            ProteinFragmentationDescription pd = proteins.getProteinFragmentationDescription(id);
//            if (pd == null)
//                throw new UnsupportedOperationException("Fix This"); // ToDo Is this expected
//            if (pd.getModel() != null) {
//                idWith3dModel.add(pd);
//                pageWith3dModel.add(page);
//            }
//            else {
//                idWithout3dModel.add(pd);
//                pageWithout3dModel.add(page);
//
//            }
//        }
//        String[] empty = {};
//        ProteinFragmentationDescription[] emptyPd = {};
//        if (!idWith3dModel.isEmpty()) {
//            body.addString("<h1>Proteins With 3D Models</h1>\n");
//            body.addString("<h3>UniprotID Coverage% Peptides Modeled/Total Peptides  [Number Chains]</h3>\n");
//            new ReferenceTableBuillder(body, idWith3dModel.toArray(emptyPd), pageWith3dModel.toArray(empty), INDEX_ROW_LENGTH);
//        }
//        if (!idWithout3dModel.isEmpty()) {
//            body.addString("<h1>Proteins Without 3D Models</h1>");
//            new ReferenceTableBuillder(body, idWithout3dModel.toArray(emptyPd), pageWithout3dModel.toArray(empty), INDEX_ROW_LENGTH);
//        }
//        String page = pb.buildPage();
//        String fileName = HOME_PAGE;
//        FileUtilities.writeFile(fileName, page);
//        Collections.sort(pfdWith3dModel, ProteinFragmentationDescription.INTERESTING_COMPARATOR);
//        for (Iterator<ProteinFragmentationDescription> iterator = pfdWith3dModel.iterator(); iterator.hasNext(); ) {
//            ProteinFragmentationDescription next = iterator.next();
//            System.out.println(next.getUniprotId() + " " + String.format("%5.3f", next.getFractionalCoverage()) + " " +
//                    next.getStatistics().getPartitionStatistics(2));
//        }
//        double[] averagecoverage = getAverageCoverage();
//        for (int i = 0; i < averagecoverage.length; i++) {
//            double v = averagecoverage[i];
//            System.out.print(Integer.toString(i) + " " + String.format("%5.3f", v) + ",");
//        }
//        System.out.println();
//        return fileName;
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }


    private void buildBadUniprotPage(String uiprotId, String fragments, HttpServletResponse rsp) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
    }


    public void postWaitPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();

        //     res.setHeader("Expires","Wed, 07 Aug 2012 1:00:00 GMT");
        res.setContentType("text/html");

        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
        out.println("<html>");

        out.println("<head>");
        out.println("  <title>Wait Page</title>");
        out.println("</head>");

        out.println("<body>");
        out.println("  <h1>running, please wait...</h1>");
        out.println("  <script language=\"JavaScript\">");
        out.println("  <!--");
        out.println("  location.replace(\"" + SERVLET_URI + "\" + location.search);");
        out.println("  //-->");
        out.println("  </script>");
        out.println("</body>");

        out.println("</html>");
    }

    private boolean shouldForward(HttpServletRequest req, String requestURI) {
        if (requestURI.contains((".html")))
            return true;
        String uniprot = req.getParameter("uniprot");
        if (uniprot == null || "".equals(uniprot)) {
            return true;
        }
        return false;
    }

    /**
     * @param newPage
     * @param aRequest
     * @param aResponse
     * @throws ServletException
     * @throws IOException
     */
    private void forward(
            String newPage, HttpServletRequest aRequest, HttpServletResponse aResponse
    ) throws ServletException, IOException {
        RequestDispatcher dispatcher = aRequest.getRequestDispatcher(newPage);
        dispatcher.forward(aRequest, aResponse);
    }


    private void redirect(String newPage, HttpServletResponse aResponse
    ) throws IOException {
        String urlWithSessionID = aResponse.encodeRedirectURL(newPage);
        aResponse.sendRedirect(urlWithSessionID);
    }


//    private boolean uriHandled(final String requestURI, final HttpServletRequest req, final HttpServletResponse rsp) {
//        try {
//            if("/".equals(requestURI))  {
//                serveEmptyPage(  rsp );
//                return true; // handled
//            }
//            if(requestURI.startsWith("/"))   {
//                serveFile(req, rsp);
//                 return true; // handled
//             }
//
//            if (true)
//                throw new UnsupportedOperationException("Fix This"); // ToDo
//
//            return false;
//        }
//        catch ( Exception e) {
//            throw new RuntimeException(e);
//
//        }
//    }

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
            }
        }
    }


    public static final int BUFSIZE = 4096;

    /**
     * Sends a file to the ServletResponse output stream.  Typically
     * you want the browser to receive a different name than the
     * name the file has been saved in your local database, since
     * your local names need to be unique.
     *
     * @param req               The request
     * @param resp              The response
     * @param filename          The name of the file you want to download.
     * @param original_filename The name the browser should receive.
     */
    private void doDownload(HttpServletRequest req, HttpServletResponse resp,
                            String filename, String original_filename)
            throws IOException {
        File f = new File(filename);
        int length = 0;
        ServletOutputStream op = resp.getOutputStream();
        ServletContext context = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(filename);

        //
        //  Set the response and go!
        //
        //
        resp.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
        resp.setContentLength((int) f.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + original_filename + "\"");

        //
        //  Stream to the requester.
        //
        byte[] bbuf = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(f));

        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
            op.write(bbuf, 0, length);
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
        //    System.err.println(requestedFile);
        super.service(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void servePage(HttpServletResponse rsp, File page) throws IOException {
        ServletOutputStream os = rsp.getOutputStream();
        FileInputStream is = new FileInputStream(page);
        FileUtilities.copyStream(is, os);

    }

    public void serveEmptyPage(HttpServletResponse rsp) throws IOException {
        File page = new File("IndexGood.html");
        ServletOutputStream os = rsp.getOutputStream();
        FileInputStream is = new FileInputStream(page);
        FileUtilities.copyStream(is, os);

    }


    public static final int PORT = 8080;

    public static Server launchServer()
            throws Exception {

        Server server = new Server(PORT);
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        server.addConnector(connector);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setWelcomeFiles(new String[]{"IndexGood.html"});  // was index.html

        resource_handler.setResourceBase(".");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, new DefaultHandler()});
        server.setHandler(handlers);

        ServletHolder holder = new ServletHolder(new PageServer());
        //   ServletHolder holder2 = new ServletHolder(new FileServlet());
        Context root = new Context(server, SERVLET_URI, Context.SESSIONS);
        root.setWelcomeFiles(new String[]{"IndexGood.html"});  // was index.html
        root.addServlet(holder, "/*");
        handlers.setHandlers(new Handler[]{root, resource_handler, new DefaultHandler()});


        //        HandlerList handlers = new HandlerList();
//             handlers.setHandlers(new Handler[]{ root, new DefaultHandler()});
//
//        server.setHandler(handlers);

//        root.addHandler(resource_handler);
        //     root.addServlet(holder, "/");
        server.start();
        return server;
    }

    /**
     * start in the directory above the pages directory
     * The JmolAppletSigned jars should be in a directory called CodeBase inside that directory
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Uniprot.setDownloadModels(true);
        ProteinDatabase pd = ProteinDatabase.getInstance();   // preload
        Server server = launchServer();
        server.join();
        while (server.isRunning()) ;
    }
}
