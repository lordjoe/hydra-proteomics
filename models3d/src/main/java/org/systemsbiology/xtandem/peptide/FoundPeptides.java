package org.systemsbiology.xtandem.peptide;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.FoundPeptides
 * User: Steve
 * Date: 8/15/12
 */
public class FoundPeptides {
    public static final FoundPeptides[] EMPTY_ARRAY = {};

    public static FoundPeptides readFoundPeptides(File inp)  {
         try {
             FoundPeptides fps = new FoundPeptides();
             LineNumberReader rdr = new LineNumberReader(new InputStreamReader(new FileInputStream(inp)));
             String line = rdr.readLine();
             while (line != null) {
                 String[] items = line.split("\t");
                 IPolypeptide peptide1 = FoundPeptide.toPolyPeptide(items[0]);
                 int charge = Integer.parseInt(items[2]);
                 String[] proteins = items[1].split("/");
                 for (int i = 1; i < proteins.length; i++) {
                     String proteinId = proteins[i];
                     FoundPeptide fp = new FoundPeptide(peptide1, proteinId, charge);
                     fps.addPeptide(fp);
                 }
                 line = rdr.readLine();
             }
             return fps;
         }
         catch (IOException e) {
             throw new RuntimeException(e);

         }
         catch (NumberFormatException e) {
             throw new RuntimeException(e);

         }
     }


    private final Map<String, List<FoundPeptide>> m_ProteinToPeptide = new HashMap<String, List<FoundPeptide>>();

    public FoundPeptides() {
    }

    public FoundPeptide[] getPeptides(String protein) {
        List<FoundPeptide> foundPeptides = m_ProteinToPeptide.get(protein);
        return foundPeptides.toArray(FoundPeptide.EMPTY_ARRAY);
    }

    public String[] getProteins() {
        String[] ret = m_ProteinToPeptide.keySet().toArray(new String[0]);
        Arrays.sort(ret);
        return ret;
    }

    public String[] getMappedProteins() {
        String[] proteins = getProteins();
        List<String> holder = new ArrayList<String>();
        for (String id : proteins) {
            if(id.contains("DECOY"))
                continue;
            if(id.contains("UNMAPPED"))
                continue;
            if(id.startsWith("sp|"))
                continue;
            holder.add(id);
        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
      }

    public void addPeptide(FoundPeptide added) {
        String protein = added.getProteinId();
        List<FoundPeptide> foundPeptides = null;
        synchronized (m_ProteinToPeptide) {
            foundPeptides = m_ProteinToPeptide.get(protein);
            if (foundPeptides == null) {
                foundPeptides = new ArrayList<FoundPeptide>();
                m_ProteinToPeptide.put(protein, foundPeptides);
            }

        }
        synchronized (foundPeptides) {
            foundPeptides.add(added);
        }
    }

    public static void main(String[] args) throws IOException {
        File inp = new File(args[0]);
        if (!inp.exists())
            throw new IllegalArgumentException("input file " + args[0] + " does not exist");

        FoundPeptides fps = readFoundPeptides(inp);
        String[] proteins = fps.getMappedProteins();
        File fs = new File("GoodProteins.txt") ;
        PrintWriter out = new PrintWriter(new FileWriter(fs));
        for (String id : proteins) {
            System.out.println(id);
            out.println(id);
            FoundPeptide[] peptides = fps.getPeptides(id);
            for (int i = 0; i < peptides.length; i++) {
                FoundPeptide foundPeptide = peptides[i];
                foundPeptide = null;
            }
        }
        out.close();
        fs = new File("GoodPeptides.txt") ;
        out = new PrintWriter(new FileWriter(fs));
        for (String id : proteins) {
            System.out.println(id);
            out.println(id);
            FoundPeptide[] peptides = fps.getPeptides(id);
            for (int i = 0; i < peptides.length; i++) {
                FoundPeptide fp = peptides[i];
                out.println(fp.getPeptide().toString() + "\t" + fp.getProteinId() + "\t" + fp.getCharge());
            }
        }
        out.close();
     }

 }
