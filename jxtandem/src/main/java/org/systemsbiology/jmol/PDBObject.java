package org.systemsbiology.jmol;

import com.lordjoe.utilities.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.jmol.PDBObject
 * User: steven
 * Date: 5/15/12
 */
public class PDBObject {
    public static final PDBObject[] EMPTY_ARRAY = {};

    private File m_File;
    private final List<AminoAcidAtLocation> m_DisplayedAminoAcids = new ArrayList<AminoAcidAtLocation>();
    private String m_Sequence;

    public PDBObject(File file) {
        m_File = file;
        try {
            LineNumberReader rdr = new LineNumberReader(new FileReader(m_File));
            readFromReader(rdr);
         // save the models that are used
         //   FileUtilities.copyFile(file,new File("E:/tmp/Models3d/" + file.getName()));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }

    }

    public File getFile() {
        return m_File;
    }

    public void setFile(File file) {
        m_File = file;
    }

    public String getSequence() {
        return m_Sequence;
    }

    public void readFromReader(LineNumberReader rdr) {
        try {
            AminoAcidAtLocation here = null;
            StringBuilder sb = new StringBuilder();
            String line = rdr.readLine();
            while (line != null) {
                handleLine(line,sb );
 //               AminoAcidAtLocation now = handleLine(line,sb, here);
//                if (now != null && !now.equals(here)) {
//                    m_DisplayedAminoAcids.add(now);
//                    sb.append(now.getAminoAcid());
//                }
//                here = now;
                line = rdr.readLine();
            }
            m_Sequence = sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private void handleLine(String line, StringBuilder sb ) {
        if (line.startsWith("SEQRES")) {
             handleSeqRes(line,sb );
        }
      }



    private void handleSeqRes(String line, StringBuilder sb ) {
        String substring = line.substring(8, 10).trim();
        int num = Integer.parseInt(substring);
        String substring1 = line.substring(11, 12);

        try {
            ChainEnum chain = ChainEnum.fromString(substring1);
        }
        catch (IllegalArgumentException e) {
             ChainEnum.valueOf(substring1);
            throw new RuntimeException(e);

        }
        String substring2 = line.substring(13, 18).trim();;
        int residues = Integer.parseInt(substring2);

        String rest = line.substring(19);
        String[] items = rest.split(" ");
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if(item == null || item.length() == 0)
                 continue;
            FastaAminoAcid aa = FastaAminoAcid.fromAbbreviation(item);
            if(aa == null)
                continue;
             AminoAcidAtLocation now = new AminoAcidAtLocation(aa, m_DisplayedAminoAcids.size() + 1);
            m_DisplayedAminoAcids.add(now);
            sb.append(aa.toString());

        }
    }

    private AminoAcidAtLocation handleLine(String line, AminoAcidAtLocation here) {
        if (line.startsWith("ATOM")) {
            return handleAtom(line, here);
        }
        return here;
    }

    private AminoAcidAtLocation handleAtom(String line, AminoAcidAtLocation here) {
        while (line.contains("  "))
            line = line.replace("  ", " ");
        line = line.replace(" ", "\t");
        String[] items = line.split("\t");
        FastaAminoAcid aa = FastaAminoAcid.fromAbbreviation(items[3]);
        if (aa == null)
            return here;
        String chain = items[4];
        int position = Integer.parseInt(items[5]);
        AminoAcidAtLocation now = new AminoAcidAtLocation(aa, position);
        if (now.equals(here))
            return here;
        return now;
    }

    public AminoAcidAtLocation[] getAminoAcidsForSequence(String foundSequence) {
        AminoAcidAtLocation[] ret = internalAminoAcidsForSequence(foundSequence);
        if(ret != null)
            return ret;
        return getAminoAcidsForCloseSequence (  foundSequence);
    }

    protected AminoAcidAtLocation[] internalAminoAcidsForSequence(String foundSequence) {
        FastaAminoAcid[] aas = FastaAminoAcid.asAminoAcids(foundSequence);
        for (int i = 0; i < m_DisplayedAminoAcids.size() - aas.length; i++) {
            AminoAcidAtLocation aminoAcidAtLocation = m_DisplayedAminoAcids.get(i);
            if (aminoAcidAtLocation.getAminoAcid() == aas[0]) {
                AminoAcidAtLocation[] ret = maybeBuildSequence(aas, i);
                if (ret != null)
                    return ret;
            }

        }
           return null;

    }

    /**
     *
     * @param foundSequence
     * @return
     */
    protected AminoAcidAtLocation[] getAminoAcidsForCloseSequence(String foundSequence) {
        String modelSequence = getSequence();
        SmithWaterman sw = new SmithWaterman(modelSequence, foundSequence);
        List<SimpleChainingMatch> matches = sw.getMatches();
        if(matches.size() == 0)  {
            // try again
//            sw = new SmithWaterman(modelSequence, foundSequence,SmithWaterman.DEFAULT_SCORE_THRESHOLD / 3);
//            matches = sw.getMatches();
//            for (Iterator<SimpleChainingMatch> iterator = matches.iterator(); iterator.hasNext(); ) {
//                SimpleChainingMatch best =  iterator.next();
//                String sequenceInTarget = modelSequence.substring(best.getFromA(),best.getToA());
//                System.out.println(sequenceInTarget);
//            }
//            System.out.println(foundSequence + " sought");
//            System.out.println(modelSequence);
//            System.out.println();
              return null;
        }
        SimpleChainingMatch best = matches.get(0);
        String sequenceInTarget = modelSequence.substring(best.getFromA(),best.getToA());
        return  internalAminoAcidsForSequence(sequenceInTarget);


    }


    private AminoAcidAtLocation[] maybeBuildSequence(FastaAminoAcid[] aas, int start) {
        List<AminoAcidAtLocation> holder = new ArrayList<AminoAcidAtLocation>();
        holder.add(m_DisplayedAminoAcids.get(start));
        for (int i = 1; i < aas.length; i++) {
            AminoAcidAtLocation aminoAcidAtLocation = m_DisplayedAminoAcids.get(start + i);
            if (aminoAcidAtLocation.getAminoAcid() == aas[i]) {
                holder.add(aminoAcidAtLocation);
            }
            else {
                return null; // bad
            }
        }

        AminoAcidAtLocation[] ret = new AminoAcidAtLocation[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static final String[] SOUGHT_SEQUENCES = {
            "HMDLCLTVVDR",
            "HVGSNLCLDSR",
            "HMDLCLTVVDRAPGSLIK",
            "VLTFLDSHCECNEHWLEPLLER",
            "HMDLCLTVVDRAPGSLIK",
    };

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        PDBObject obj = new PDBObject(f);
        List<AminoAcidAtLocation[]> holder = new ArrayList<AminoAcidAtLocation[]>();

        for (int i = 0; i < SOUGHT_SEQUENCES.length; i++) {
            String foundSequence = SOUGHT_SEQUENCES[i];
            AminoAcidAtLocation[] locs = obj.getAminoAcidsForSequence(foundSequence);
            holder.add(locs);
        }
        AminoAcidAtLocation[][] ret = new AminoAcidAtLocation[holder.size()][];
        holder.toArray(ret);
        ScriptWriter sw = new ScriptWriter();
        String script = sw.writeScript(obj, SOUGHT_SEQUENCES);
        System.out.println(script);
    }
}
