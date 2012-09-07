package org.systemsbiology.xtandem.fragmentation;

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

    public static Sequence[] copyInterestingStructures(Collection<String> ids, Reader inpr, Appendable out) {
        BufferedReader inp = new BufferedReader(inpr);
        //read the SwissProt File
        SequenceIterator sequences = SeqIOTools.readSwissprot(inp);
        Set<String> seen_features = new HashSet<String>();
        int count = 0;
        List<Sequence> holder = new ArrayList<Sequence>();

        //iterate through the sequences
        while (sequences.hasNext()) {
            try {

                Sequence seq = sequences.nextSequence();
                String id = seq.getURN();
                String name = seq.getName();
                count++;
//
//                Iterator<Feature> features = seq.features();
//                while (features.hasNext()) {
//                    Feature f = features.next();
//                    String type = f.getType();
//                    try {
//                        UniprotFeatureType uf = UniprotFeatureType.valueOf(type);
//                    }
//                    catch (Exception ex) {
//                        if (!seen_features.contains(type)) {
//                            seen_features.add(type);
//                            System.out.println(type + ",");
//
//                        }
//                    }
//                }
                if (ids.contains(name)) {
                    holder.add(seq);

                }
                //      System.out.print(".");
                //      if(++count  % 50 == 0)
                //         System.out.println();

                //do stuff with the sequence

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
        Sequence[] ret = new Sequence[holder.size()];
        holder.toArray(ret);
        return ret;

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

    } ;

    public static final Set<UniprotFeatureType>  INTERESTING_FEATURE_SET =
            new HashSet<UniprotFeatureType>(Arrays.asList(INTERESTING_FEATURES));

    public static Feature[] getInterestingFeatures(Sequence seq) {
        List<Feature> holder = new ArrayList<Feature>();
        Feature[] features = getFeatures(  seq);
        for (int i = 0; i < features.length; i++) {
            Feature feature = features[i];
            String type = feature.getType();
            UniprotFeatureType ft = UniprotFeatureType.valueOf(type);
            if(INTERESTING_FEATURE_SET.contains(ft))
                holder.add(feature);
        }

        Feature[] ret = new Feature[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static void main(String[] args) {
        Reader br = null;
        StringBuilder sb = new StringBuilder();
        Uniprot[] ups = Uniprot.readUniprots(new File(args[1]));
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
        Sequence[] sequences = copyInterestingStructures(ids, br, sb);
        List<Sequence> holder = new ArrayList<Sequence>();

         for (int i = 0; i < sequences.length; i++) {
            Sequence sequence = sequences[i];
            Feature[] fts = getInterestingFeatures(sequence);
            if(fts.length > 0)
                holder.add(sequence);
        }

        Sequence[] ret = new Sequence[holder.size()];
         holder.toArray(ret);
        holder.clear();
        for (int i = 0; i < ret.length; i++) {
            Sequence sequence = ret[i];
            Feature[] fts = getInterestingFeatures(sequence);
            if(fts.length > 0)
                holder.add(sequence);

        }

    }
}


