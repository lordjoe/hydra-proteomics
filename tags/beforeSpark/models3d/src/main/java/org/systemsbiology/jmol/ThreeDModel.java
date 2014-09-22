package org.systemsbiology.jmol;


import com.lordjoe.utilities.*;
import org.systemsbiology.xtandem.hadoop.*;
import org.systemsbiology.xtandem.sax.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * org.systemsbiology.gatk.ThreeDModels
 * User: steven
 * Date: 6/20/12
 */
public class ThreeDModel {
    public static final ThreeDModel[] EMPTY_ARRAY = {};

    public static final String PDB_DOWNLOAD_URL_START = "http://www.pdb.org/pdb/download/downloadFile.do?fileFormat=pdb&compression=NO&structureId=";


    public static ThreeDModel fromXML(String xml) {
        xml = xml.trim();
        try {
            return XTandemHadoopUtilities.parseXMLString(xml, new ThreeDModelHandler());
        }
        catch (Exception e) {
            return null;

        }

    }

    public static String[] readModelXml(File models) {
        String[] ids = FileUtilities.readInLines(models);
        List<String> holder = new ArrayList<String>();
        for (int i = 1; i < ids.length; i++) {
            String id = ids[i];
            String[] items = id.split("\t");
            String xml = readPDBXML(items[1]);
            if (xml != null) {
                holder.add(xml);
                System.out.println("found " + (holder.size()) + " in " + i);
            }

        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;

    }

    private static void readModelsXML(final File models, PrintWriter out) {
        String[] xmls = readModelXml(models);
        for (int i = 0; i < xmls.length; i++) {
            String xml = xmls[i];
            out.println(xml);
            out.println("<EndModel/>\n");

        }
        out.close();
    }


    private static ThreeDModel[] readPDBList(final File models) {
        List<ThreeDModel> holder = new ArrayList<ThreeDModel>();
        try {
            LineNumberReader rdr = new LineNumberReader(new FileReader(models));

            String xml = readPDBXML(rdr);
            while (xml != null) {
                xml = xml.trim();
                if (xml.length() > 0) {
                    ThreeDModel model = ThreeDModel.fromXML(xml);
                    if (model != null) {
                        holder.add(model);
                    }
                    else {
                        System.out.println(xml);
                    }
                }
                xml = readPDBXML(rdr);
            }

        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
        ThreeDModel[] ret = new ThreeDModel[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    private static String readPDBXML(final LineNumberReader rdr) {
        try {
            StringBuilder sb = new StringBuilder();
            String line = rdr.readLine();
            while (line != null) {
                if ("<EndModel/>".equals(line)) {
                    break;
                }
                else {
                    sb.append(line);
                    sb.append("\n");
                }
                line = rdr.readLine();
            }
            if (sb.length() == 0)
                return null;
            return sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


    private static String readPDBXML(String item) {
        try {
            String urlStr = "http://www.pdb.org/pdb/rest/das/pdb_uniprot_mapping/alignment?query=" + item;
            URL url = new URL(urlStr);
            String[] lines = FileUtilities.readInLines(url);
            if (lines.length == 0)
                return null;
            if (lines[0].contains("Bad reference (403)"))
                return null;
            if (!lines[0].startsWith("<?xml"))
                return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                sb.append(line + "\n");
            }
            String s = sb.toString();
            return s;
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);

        }
    }

    public static void write3DModelList(final File models) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("PdbModelList.txt"));
        ThreeDModel[] model3ds = readPDBList(models);
        for (int i = 0; i < model3ds.length; i++) {
            ThreeDModel model3d = model3ds[i];
            addModel(model3d);
         }
        String[] proteinIds = getProteinIds();
        for (int i = 0; i < proteinIds.length; i++) {
              ThreeDModel model3d = getModel(proteinIds[i]);
            String descriptionString = model3d.getDescriptionString();
            out.println(descriptionString);
          }
        out.close();
    }

    public static File downLoad3DModel(File parent,String id)   {
        try {
            String urlStr =  DOWNLOAD_MODEL_STRING + id;
            File fileName = new File(parent,id + ".pdb");
            if(fileName.exists() && fileName.length() > 1000)
                return null;
            URL url = new URL(urlStr);
            String[] strings = FileUtilities.readInLines(url);
            FileUtilities.writeFileLines(fileName,strings);
            return fileName;
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);

        }
    }


    private static final Map<String, ThreeDModel> gProteinToModel = new HashMap<String, ThreeDModel>();

    public static String[] getProteinIds( )  {
        String[] strings = gProteinToModel.keySet().toArray(new String[0]);
        Arrays.sort(strings);
        return strings;
    }


    public static ThreeDModel getModel(String proteinId)  {
         return gProteinToModel.get(proteinId);
    }

    public static void addModel(ThreeDModel model)  {
          gProteinToModel.put(model.getProteinId(), model);
    }



    private String m_ProteinId;
    private final Map<String, ThreeDModelStructure> m_ModelIds = new HashMap<String, ThreeDModelStructure>();

    public ThreeDModel(final String accessionId) {
        m_ProteinId = accessionId;
    }

    public ThreeDModel() {
    }


    public void addStructure(ThreeDModelStructure id) {
        m_ModelIds.put(id.getAccessionId(), id);

    }

    public String[] getModelIds() {
        String[] ret = m_ModelIds.keySet().toArray(new String[0]);
        Arrays.sort(ret);
        return ret;

    }

    public String getProteinId() {
        return m_ProteinId;
    }

    public void setProteinId(final String proteinId) {
        m_ProteinId = proteinId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getProteinId());
        sb.append("\t");
        String[] ids = getModelIds();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            if (i > 0)
                sb.append(",");
            sb.append(id);
        }
        return sb.toString();
    }

    public String getDescriptionString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getProteinId());
        sb.append("\t");
        String[] ids = getModelIds();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            if (i > 0)
                sb.append(",");
            sb.append(id);
        }
        return sb.toString();

    }

     public static final String DOWNLOAD_MODEL_STRING = "http://www.pdb.org/pdb/download/downloadFile.do?fileFormat=pdb&compression=NO&structureId=";

    public static final String SAMPLE = "java ThreeDModel" + "  PdbModelsList.txt";


    public static void main(String[] args) throws Exception {

        if(args.length < 1) {
                 UsageGenerator.showUsage(SAMPLE,
                         "models <tab delimited uniptotid\tcommas delimited pdb model is list>",
                         "O00198\t2L58,2L5B"

                 );
                return;
        }

        File models = new File(args[0]);
        if (!models.exists())
            throw new IllegalArgumentException("model file " + args[0] + " does not exist");
        String[] lines = FileUtilities.readInLines(models);
        File base = new File(System.getProperty("user.dir"));
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] items = line.split("\t");
            String protein = items[0];
            String[] ids = items[1].split(",");

            String id = ids[0];

            downLoad3DModel(base,id);
            System.out.println("Downloaded "  + id );
        }
       // write3DModelList(models);
        //      readModelsXML(models, out);
    }



}
