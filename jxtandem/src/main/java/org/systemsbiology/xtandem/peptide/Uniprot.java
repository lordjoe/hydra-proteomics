package org.systemsbiology.xtandem.peptide;

import com.lordjoe.utilities.*;
import org.systemsbiology.jmol.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * org.systemsbiology.xtandem.peptide.Uniprot
 * User: steven
 * Date: 8/16/12
 */
public class Uniprot
{
    public static final Uniprot[] EMPTY_ARRAY = {};


    private static final String UNIPROT_SERVER = "http://www.uniprot.org/";
    private static final Logger LOG = Logger.getAnonymousLogger();

    protected static String run(String tool, ParameterNameValue[] params)
            throws Exception
    {
        String location = buildLocation(tool, params);
        return retrieveData(location);
    }

    protected static String run(String tool, Collection<ParameterNameValue> params)
            throws Exception
    {
        String location = buildLocation(tool, params);
        return retrieveData(location);
    }

    protected static String retrieveDataX(String location) throws IOException, InterruptedException
    {
        StringBuilder builder = new StringBuilder();
        URL url = new URL(location);
        LOG.info("Submitting...");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        conn.setDoInput(true);
        conn.connect();

        int status = conn.getResponseCode();
        while (true) {
            int wait = 0;
            String header = conn.getHeaderField("Retry-After");
            if (header != null)
                wait = Integer.valueOf(header);
            if (wait == 0)
                break;
            LOG.info("Waiting (" + wait + ")...");
            conn.disconnect();
            Thread.sleep(wait * 1000);
            conn = (HttpURLConnection) new URL(location).openConnection();
            conn.setDoInput(true);
            conn.connect();
            status = conn.getResponseCode();
        }
        if (status == HttpURLConnection.HTTP_OK) {
            LOG.info("Got a OK reply");

            InputStream reader = conn.getInputStream();
            URLConnection.guessContentTypeFromStream(reader);
            int a = 0;
            while ((a = reader.read()) != -1) {
                builder.append((char) a);
            }
        }
        else
            LOG.severe("Failed, got " + conn.getResponseMessage() + " for "
                    + location);
        conn.disconnect();
        return builder.toString();
    }

    protected static String retrieveData(String location) throws IOException, InterruptedException
    {
        StringBuilder builder = new StringBuilder();
        URL url = new URL(location);
        LOG.info("Submitting...");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        conn.setDoInput(true);
        conn.connect();

        int status = conn.getResponseCode();
        while (true) {
            int wait = 0;
            String header = conn.getHeaderField("Retry-After");
            if (header != null)
                wait = Integer.valueOf(header);
            if (wait == 0)
                break;
            LOG.info("Waiting (" + wait + ")...");
            conn.disconnect();
            Thread.sleep(wait * 1000);
            conn = (HttpURLConnection) new URL(location).openConnection();
            conn.setDoInput(true);
            conn.connect();
            status = conn.getResponseCode();
        }
        if (status == HttpURLConnection.HTTP_OK) {
            LOG.info("Got a OK reply");

            InputStream reader = conn.getInputStream();
            LineNumberReader inp = new LineNumberReader(new InputStreamReader(reader));
            String line = inp.readLine();
            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = inp.readLine();
            }
        }
        else
            LOG.severe("Failed, got " + conn.getResponseMessage() + " for "
                    + location);
        conn.disconnect();
        return builder.toString();
    }

    private static String buildLocation(String tool, ParameterNameValue[] params)
    {
        StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + tool + "/?");
        boolean first = true;
        for (ParameterNameValue pv : params) {
            if (!first)
                locationBuilder.append('&');
            locationBuilder.append(pv.name).append('=').append(pv.value);
            first = false;
        }
        return locationBuilder.toString();
    }

    private static String buildLocation(String tool, Collection<ParameterNameValue> params)
    {
        StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER + tool + "/?");
        boolean first = true;
        for (ParameterNameValue pv : params) {
            if (!first)
                locationBuilder.append('&');
            locationBuilder.append(pv.name).append('=').append(pv.value);
            first = false;
        }
        return locationBuilder.toString();
    }

    private static class ParameterNameValue
    {
        private final String name;
        private final String value;

        public ParameterNameValue(String name, String value)
                throws UnsupportedEncodingException
        {
            this.name = name;
            this.value = value;
            //          this.name = URLEncoder.encode(name, "UTF-8");
            //           this.value = URLEncoder.encode(value, "UTF-8");
        }
    }


    public static Uniprot getUniprot(String accession)
    {
        try {
            String s = run("uniprot", new ParameterNameValue[]{
                    //           new ParameterNameValue("format", "tab"),
                    new ParameterNameValue("query",
                            "accession:" + accession + "&format=tab&columns=id,database(PDB),sequence"),
            });
            final String[] split = s.split("\n");
            if (split.length != 2)
                return null;
            if (!split[0].startsWith("Entry"))
                return null;
            final String query = split[1];
            final String[] split1 = query.split("\t");
            if( split1.length != 3)
                return null;
            return new Uniprot(split1);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private final Protein m_Protein;
    private final String[] m_Models;

    public Uniprot(String line)
     {
        this(line.split("\t"));
     }

    public Uniprot(String[] split1)
    {
        final String annotation = split1[0];
        final String sequence = split1[2].replace(" ", "");
        m_Protein = Protein.buildProtein(annotation, sequence, "");
        String pdbs = split1[1].trim();
        if (pdbs.length() == 0)
            m_Models = new String[0];
        else {
            m_Models = pdbs.split(";");
        }
    }

    protected String getDelimitedModels()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m_Models.length; i++) {
            if (i > 0)
                sb.append(";");
            sb.append(m_Models[i]);

        }
        return sb.toString();
    }

    public Protein getProtein()
    {
        return m_Protein;
    }

    public String[] getModels()
    {
        return m_Models;
    }

    @Override
    public String toString()
    {
        return
                m_Protein.getId() + "\t" +
                        getDelimitedModels() + "\t" +
                        m_Protein.getSequence();


    }


    public static Uniprot[] readUniprots()
    {
        String[] lines = FileUtilities.readInLines("Origene.tsv");
        List<Uniprot> holder = new ArrayList<Uniprot>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Uniprot up = new Uniprot(line);
            holder.add(up);
        }
        Uniprot[] ret = new Uniprot[holder.size()];
        holder.toArray(ret);
        return ret;

    }

    private static void downloadUniprots(String pArg) throws IOException
    {
        File inp = new File("GoodProteins.txt");
        if (!inp.exists())
            throw new IllegalArgumentException("input file " + pArg + " does not exist");


        File outf = new File("Origene.tsv");
        PrintWriter out = new PrintWriter(new FileWriter(outf));
        String[] proteins = FileUtilities.readInLines(inp);
        for (String id : proteins) {
            Uniprot up = getUniprot(id);
            if (up != null) {
                final String x = up.toString();
                out.println(x);

            }
            else {
                System.err.println("Cannot find " + id);
            }
        }
        out.close();
    }


    public static void main(String[] args) throws IOException
    {
       // downloadUniprots(args[0]);
        Uniprot[] pts = readUniprots();
        for (int i = 0; i < pts.length; i++) {
            Uniprot pt = pts[i];
            final String[] models = pt.getModels();
            for (int j = 0; j < models.length; j++) {
                String model = models[j];
                ThreeDModel.downLoad3DModel(model);

            }
         }
    }


}
