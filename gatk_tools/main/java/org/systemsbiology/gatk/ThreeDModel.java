package org.systemsbiology.gatk;

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




    private final String m_AccessionId;
    private final Set<String> m_ModelIds = new HashSet<String>();

    public ThreeDModel(final String accessionId, String[] ids) {
        m_AccessionId = accessionId;
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            addModelId(  id);
        }
    }

    public ThreeDModel(final String xml) {
        String[] items = GeneUtilities.extractQuotedTextItems(xml, "");
        m_AccessionId = items[0];
        items = GeneUtilities.extractQuotedTextItems(  xml, "");
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            addModelId(item);
        }
    }

    public void addModelId(String id)   {
         m_ModelIds.add(id);

    }

    public String[] getModelIds()   {
         String[] ret = m_ModelIds.toArray(new String[0]);
        Arrays.sort(ret);
        return ret;

    }

    public String getAccessionId() {
        return m_AccessionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
         sb.append(getAccessionId());
         sb.append("\t");
         String[] ids = getModelIds();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
             if(i > 0)
                 sb.append(",");
            sb.append(id);
        }
        return sb.toString();
    }

    public static String[] readModelXml(File models) {
        String[] ids = GeneUtilities.readInLines(models);
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
                ThreeDModel model = new ThreeDModel(xml);
                holder.add(model);
            }

         }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
        ThreeDModel[] ret = new ThreeDModel[holder.size()];
           holder.toArray(ret);
           return ret;
      }

     public String getDescriptionString() {
         StringBuilder sb = new StringBuilder();
          sb.append(getAccessionId());
          sb.append("\t");
          String[] ids = getModelIds();
         for (int i = 0; i < ids.length; i++) {
             String id = ids[i];
              if(i > 0)
                  sb.append(",");
             sb.append(id);
         }
         return sb.toString();

    }

    private static String readPDBXML(final LineNumberReader rdr) {
        try {
            StringBuilder sb = new StringBuilder();
            String line = rdr.readLine();
            while (line != null) {
                if ("<EndModel/>".equals(line))  {
                    break;
                }
                else  {
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
            String urlStr = "http://www.pdb.org/pdb/rest/das/pdb_uniprot_mapping/alignment?query=" + item.trim();
            URL url = new URL(urlStr);
            String[] lines = GeneUtilities.readInLines(url);
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

    public static void readDesignated3dModels(String arg) throws IOException {
          File models = new File(arg);
          if (!models.exists())
              throw new IllegalArgumentException("model file " + arg + " does not exxist");
          PrintWriter out = new PrintWriter(new FileWriter("PdbModelList.txt"));
          ThreeDModel[] model3ds = readPDBList(models );
          for (int i = 0; i < model3ds.length; i++) {
              ThreeDModel model3d = model3ds[i];
              out.println(model3d.getDescriptionString());
          }
          out.close();
      }


    public static String[] interesting(File models) {
         String[] ids = GeneUtilities.readInLines(models);
        String lastRead = "";
         List<String> holder = new ArrayList<String>();
         for (int i = 1; i < ids.length; i++) {
             String id = ids[i];
             String[] items = id.split(",");
             String gene = items[3];
             String uniprot = items[4];
             if(lastRead.equals(uniprot))
                 continue;
             lastRead = uniprot;
             String xml = readPDBXML(uniprot);
             if (xml != null) {
                 holder.add(xml);
                 System.out.println("found " + (holder.size()) + " in " + i);
             }

         }

         String[] ret = new String[holder.size()];
         holder.toArray(ret);
         return ret;

     }

    public static void main(String[] args) throws Exception {
        File models = new File(args[0]);
        String[] strings = interesting(  models);
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            System.out.println(string);
        }
    //    readDesignated3dModels(args[0]);
  //      readModelsXML(models, out);
    }



}
