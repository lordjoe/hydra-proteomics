package org.systemsbiology.xtandem.peptide;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * org.systemsbiology.xtandem.peptide.Uniprot
 * User: steven
 * Date: 8/16/12
 */
public class Uniprot {
    public static final Uniprot[] EMPTY_ARRAY = {};


    private static final String UNIPROT_SERVER = "http://www.uniprot.org/";
    private static final Logger LOG = Logger.getAnonymousLogger();

    protected static String run(String tool, ParameterNameValue[] params)
            throws Exception {
        String location = buildLocation(tool, params);
        return retrieveData(location);
    }

    protected static String run(String tool, Collection<ParameterNameValue> params)
            throws Exception {
        String location = buildLocation(tool, params);
        return retrieveData(location);
    }

    protected static String retrieveData(String location) throws IOException, InterruptedException {
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

    private static String buildLocation(String tool, ParameterNameValue[] params) {
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

    private static String buildLocation(String tool, Collection<ParameterNameValue> params) {
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

    private static class ParameterNameValue {
        private final String name;
        private final String value;

        public ParameterNameValue(String name, String value)
                throws UnsupportedEncodingException {
            this.name = URLEncoder.encode(name, "UTF-8");
            this.value = URLEncoder.encode(value, "UTF-8");
        }
    }


    public static void main(String[] args)
            throws Exception {
        String s = run("mapping", new ParameterNameValue[]{
                new ParameterNameValue("from", "ACC"),
                new ParameterNameValue("to", "P_REFSEQ_AC"),
                new ParameterNameValue("format", "tab"),
                new ParameterNameValue("query", "P13368 P20806 Q9UM73 P97793 Q17192"),
        });
        System.out.println(s);
    }

}
