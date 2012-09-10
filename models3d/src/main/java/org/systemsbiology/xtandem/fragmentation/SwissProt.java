package org.systemsbiology.xtandem.fragmentation;

import com.lordjoe.utilities.*;
import org.biojava.bio.*;
import org.biojava.bio.seq.*;
import org.biojava.bio.seq.io.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.SwissProt
 * User: steven
 * Date: 9/7/12
 */
public class SwissProt {
    public static final SwissProt[] EMPTY_ARRAY = {};

    public static Map<String, Sequence> copyInterestingStructures(Reader inpr) {
        BufferedReader inp = new BufferedReader(inpr);
        //read the SwissProt File
        SequenceIterator sequences = SeqIOTools.readSwissprot(inp);
        int count = 0;
        Map<String, Sequence> holder = new HashMap<String, Sequence>();

        //iterate through the sequences
        while (sequences.hasNext()) {
            try {

                Sequence seq = sequences.nextSequence();
                String id = seq.getURN();
                String name = seq.getName();
                count++;
                holder.put(name, seq);

            }
            catch (BioException ex) {
                //not in SwissProt format
                ex.printStackTrace();
            }
            catch (NoSuchElementException ex) {
                //request for more sequence when there isn't any
                ex.printStackTrace();
            }
        }
          return holder;

    }

    private static void accumulateInterestingFeatureTypes(Sequence seq) {
        Set<String> seen_features = new HashSet<String>();
        Iterator<Feature> features = seq.features();
        while (features.hasNext()) {
            Feature f = features.next();
            String type = f.getType();
            try {
                UniprotFeatureType uf = UniprotFeatureType.valueOf(type);
            }
            catch (Exception ex) {
                if (!seen_features.contains(type)) {
                    seen_features.add(type);
                    System.out.println(type + ",");

                }
            }
        }
    }


    public static void copyInterestingStructuresX(Collection<String> ids, Reader inpr, Appendable out) {
        LineNumberReader inp = new LineNumberReader(inpr);
        //read the SwissProt File

        String[] lines = readTilSlashSlash(inp);
        try {
            while (lines != null) {
                if (containsGoodId(lines, ids)) {
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i];
                        out.append(line);
                        out.append("\n");
                    }
                }
                lines = readTilSlashSlash(inp);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }

    }

    public static Reader buildLinesReader(String[] lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            sb.append(line);
            sb.append("\n");
        }
        return new StringReader(sb.toString());
    }

    private static boolean containsGoodId(String[] lines, Collection<String> ids) {
        Reader rdr = buildLinesReader(lines);
        Sequence seq = readOneSequence(rdr);
        String name = seq.getName();
        boolean contains = ids.contains(name);
        if (contains)
            return true; // break here
        else
            return false;
    }

    public static Sequence readOneSequence(Reader inpr) {
        BufferedReader inp = new BufferedReader(inpr);
        //read the SwissProt File
        SequenceIterator sequences = SeqIOTools.readSwissprot(inp);
        Set<String> seen_features = new HashSet<String>();

        //iterate through the sequences
        while (sequences.hasNext()) {
            try {

                Sequence seq = sequences.nextSequence();
                return seq;

            }
            catch (BioException ex) {
                //not in SwissProt format
                ex.printStackTrace();
            }
            catch (NoSuchElementException ex) {
                //request for more sequence when there isn't any
                ex.printStackTrace();
            }
        }
        return null;

    }


    public static String[] readTilSlashSlash(LineNumberReader inp) {

        try {
            String line = inp.readLine();
            List<String> holder = new ArrayList<String>();

            while (line != null) {
                holder.add(line);
                if (line.startsWith("//")) {
                    String[] ret = new String[holder.size()];
                    holder.toArray(ret);
                    return ret;

                }
                line = inp.readLine();
            }
            return null; // at end
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static final String[] INTERESESTING_IDS =
            {
                    "A0AVT1", "A0PJX2", "A1L0T0", "A2A3K4", "A2RUC4",
                    "A2RUH7", "A3RDR7", "A4D198", "A6NKF1", "A9UHW6",
                    "B2RDZ2", "B3KQE9", "B4DEH1", "B4DI63",
            };


    public static Feature[] getFeatures(Sequence seq) {
        List<Feature> holder = new ArrayList<Feature>();

        Iterator<Feature> features = seq.features();
        while (features.hasNext()) {
            Feature f = features.next();
            String type = f.getType();
            holder.add(f);
        }

        Feature[] ret = new Feature[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static final UniprotFeatureType[] INTERESTING_FEATURES =
            {
                    //           UniprotFeatureType.CHAIN,
                    //           UniprotFeatureType.DISULFID,

                    UniprotFeatureType.STRAND,
                    UniprotFeatureType.HELIX,
                    UniprotFeatureType.TURN,

            };

    public static final Set<UniprotFeatureType> INTERESTING_FEATURE_SET =
            new HashSet<UniprotFeatureType>(Arrays.asList(INTERESTING_FEATURES));

    public static Feature[] getInterestingFeatures(Sequence seq) {
        List<Feature> holder = new ArrayList<Feature>();
        Feature[] features = getFeatures(seq);
        for (int i = 0; i < features.length; i++) {
            Feature feature = features[i];
            UniprotFeatureType ft = getFeatureType(feature);
            if (INTERESTING_FEATURE_SET.contains(ft))
                holder.add(feature);
        }

        Feature[] ret = new Feature[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static final UniprotFeatureType[] ANALYZED_FEATURES =
              {
                         UniprotFeatureType.TRANSMEM,
                      UniprotFeatureType.STRAND,
                       UniprotFeatureType.HELIX,
                      UniprotFeatureType.TURN,

              };

      public static final Set<UniprotFeatureType> ANALYZED_FEATURE_SET =
              new HashSet<UniprotFeatureType>(Arrays.asList(ANALYZED_FEATURES));

      public static Feature[] getAnalyzedFeatures(Sequence seq) {
          List<Feature> holder = new ArrayList<Feature>();
          Feature[] features = getFeatures(seq);
          for (int i = 0; i < features.length; i++) {
              Feature feature = features[i];
              UniprotFeatureType ft = getFeatureType(feature);
              if (ANALYZED_FEATURE_SET.contains(ft))
                  holder.add(feature);
          }

          Feature[] ret = new Feature[holder.size()];
          holder.toArray(ret);
          return ret;
      }

    public static UniprotFeatureType getFeatureType(Feature feature) {
        String type = feature.getType();
        return UniprotFeatureType.valueOf(type);
    }


    public static Sequence[] readInterestingSequences(File f)  {
        try {
            Reader br = null;
            br = new FileReader(f);
//        extractInterestingSequences(args, br);
            Map<String, Sequence> stringSequenceMap = copyInterestingStructures(br);
            Sequence[] sequences = stringSequenceMap.values().toArray(new Sequence[0]);
            List<Sequence> holder = new ArrayList<Sequence>();

            for (int i = 0; i < sequences.length; i++) {
                Sequence sequence = sequences[i];
                Feature[] fts = getInterestingFeatures(sequence);
                if (fts.length > 0)
                    holder.add(sequence);
            }

            Sequence[] ret = new Sequence[holder.size()];
            holder.toArray(ret);
            holder.clear();
            return ret;
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }
    }

    private static void extractInterestingSequences(String[] args, Reader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        File inp = new File(args[1]);
        FileUtilities.guaranteeExistingFile(inp);
        Map<String, Uniprot> idToUniprot = Uniprot.readUniprots(inp);
         Uniprot[] ups = idToUniprot.values().toArray(Uniprot.EMPTY_ARRAY);
         Set<String> ids = new HashSet<String>();
        for (int i = 0; i < ups.length; i++) {
            Uniprot up = ups[i];
            ids.add(up.getProtein().getId());
        }
        try {
            //create a buffered reader to read the sequence file specified by args[0]
            br = new FileReader(args[0]);
        }
        catch (FileNotFoundException ex) {
            //can't find the file specified by args[0]
            ex.printStackTrace();
            System.exit(-1);
        }
        File outf = new File(args[2]);
        PrintWriter out = new PrintWriter(new FileWriter(outf));
        copyInterestingStructuresX(ids, br, out);
        out.close();
    }


    public static void main(String[] args) throws IOException {
        File f = new File(args[0]);
        FileUtilities.guaranteeExistingFile(f);
        Sequence[] ret = readInterestingSequences(f);
        for (int i = 0; i < ret.length; i++) {
            Sequence sequence = ret[i];
            Feature[] fts = getInterestingFeatures(sequence);
            }

    }

}


