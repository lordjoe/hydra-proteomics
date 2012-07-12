package org.systemsbiology.jmol;

import com.lordjoe.utilities.*;
import org.systemsbiology.asa.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.fragmentation.*;
import org.systemsbiology.xtandem.peptide.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.jmol.PDBObject
 * User: steven
 * Date: 5/15/12
 */
public class PDBObject extends AsaMolecule {
    public static final PDBObject[] EMPTY_ARRAY = {};

    private File m_File;
    private final Protein m_Protein;
    private int m_LastHandledLoc = -1;
    private final List<AminoAcidAtLocation> m_DisplayedAminoAcids = new ArrayList<AminoAcidAtLocation>();
    private String m_Sequence;
    private final Map<ChainEnum, ProteinSubunit> m_Chains = new HashMap<ChainEnum, ProteinSubunit>();

    public PDBObject(File file, Protein p) {
        m_File = file;
        m_Protein = p;
        try {
            LineNumberReader rdr = new LineNumberReader(new FileReader(m_File));
            readFromReader(rdr);
            // save the models that are used
            //  FileUtilities.copyFile(file, new File("E:/tmp/Models3d/" + file.getName()));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }

    }

    public Protein getProtein() {
        return m_Protein;
    }

    public File getFile() {
        return m_File;
    }

    public void setFile(File file) {
        m_File = file;
    }

    public AminoAcidAtLocation getAminoAcidAtLocation(int loc) {
        if(loc < m_DisplayedAminoAcids.size())  {
            return m_DisplayedAminoAcids.get(loc);
        }
        return null;
    }

    public ChainEnum[] getChains() {
        guaranteeChains();
        ChainEnum[] ret = m_Chains.keySet().toArray(ChainEnum.EMPTY_ARRAY);
        Arrays.sort(ret);
        return ret;
    }

    protected void guaranteeChains() {
        if (m_Chains.isEmpty()) {
            ChainEnum[] chains = buildChains();
            for (int i = 0; i < chains.length; i++) {
                ChainEnum chain = chains[i];
                ProteinSubunit su = buildSubunit(chain);
                m_Chains.put(chain, su);
            }
        }
    }

    protected ChainEnum[] buildChains() {
        Set<ChainEnum> holder = new HashSet<ChainEnum>();
        for (AminoAcidAtLocation aa : m_DisplayedAminoAcids) {
            holder.add(aa.getChain());
        }
        return holder.toArray(ChainEnum.EMPTY_ARRAY);
    }

    protected ProteinSubunit buildSubunit(final ChainEnum chain) {
        List<AminoAcidAtLocation> hdx = new ArrayList<AminoAcidAtLocation>();
        for (AminoAcidAtLocation aa : m_DisplayedAminoAcids) {
            if (chain == aa.getChain()) {
                hdx.add(aa);
            }
        }

        AminoAcidAtLocation[] locs = new AminoAcidAtLocation[hdx.size()];
        hdx.toArray(locs);
        return new ProteinSubunit(chain, getProtein(), locs);
    }

    public ProteinSubunit[] getSubUnits() {
        ProteinSubunit[] ret = m_Chains.values().toArray(ProteinSubunit.EMPTY_ARRAY);
        Arrays.sort(ret, ProteinSubunit.CHAIN_SORTER);
        return ret;
    }

    public ProteinSubunit getSubUnit(ChainEnum chain) {
        return m_Chains.get(chain);
    }

    public String getSequence() {
        return m_Sequence;
    }

    public void readFromReader(LineNumberReader rdr) {
        try {
            m_LastHandledLoc = -1;
            AminoAcidAtLocation here = null;
            StringBuilder sb = new StringBuilder();
            String line = rdr.readLine();
            while (line != null) {
                handleLine(line, sb);
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

    private void handleLine(String line, StringBuilder sb) {
        if (line.startsWith("SEQRES")) {
            handleSeqRes(line, sb);
            return;
        }
        if(line.startsWith("ATOM"))   {
            guaranteeChains();
             handleAtom(line);
        }
        if(line.startsWith("HETATM"))   {
            guaranteeChains();
             handleAtom(line);
        }
    }


    private void handleSeqRes(String line, StringBuilder sb) {
        String substring = line.substring(8, 10).trim();
        int num = Integer.parseInt(substring);
        String substring1 = line.substring(11, 12);
        ChainEnum chain = ChainEnum.A;
        try {
              chain = ChainEnum.fromString(substring1);
        }
        catch (IllegalArgumentException e) {
            ChainEnum.valueOf(substring1);
            throw new RuntimeException(e);

        }
        String substring2 = line.substring(13, 18).trim();
        int residues = Integer.parseInt(substring2);

        String rest = line.substring(19);
        String[] items = rest.split(" ");
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if (item == null || item.length() == 0)
                continue;
            FastaAminoAcid aa = FastaAminoAcid.fromAbbreviation(item);
            if (aa == null)
                continue;
            AminoAcidAtLocation now = new AminoAcidAtLocation( chain,aa);
            m_DisplayedAminoAcids.add(now);
            sb.append(aa.toString());

        }
    }

    private void handleLine(String line ) {
        if (line.startsWith("ATOM")) {
             handleAtom(line );
        }
     }

   @Override
    protected AsaAtom buildAtom(String line ) {
       AsaAtom ret = null;
       try {
           ret = super.buildAtom(line);
       }
       catch (UnknownAtomException e) {
           return null;
       }
        catch (Exception e) {
           return null;
       }
         if(ret == null)
            return null;
        String aaStr = line.substring(17, 20);
        FastaAminoAcid aa = FastaAminoAcid.fromAbbreviation(aaStr);
        if (aa == null)
             return ret;
        String chainStr = line.substring(21, 22);
        ChainEnum chain = ChainEnum.fromString(chainStr);
        String posStr = line.substring(22, 26).trim();
        // sometimed we see lines like    ARG B  37A     24.919  22.314  18.745  0.08 30.67           N
        String modifier = line.substring(26,27);
        if(!" ".equals(modifier))
            return ret; // i cannot handle that
        int pos = Integer.parseInt(posStr);
        ProteinSubunit subUnit = getSubUnit(chain);
        AminoAcidAtLocation test = subUnit.getAminoAcidAtLocation(pos);
        if(test != null )   {
            if(test.getAminoAcid() != aa) {
                String msg = "bad location  " + getFile() + " " + line;
                throw new IllegalStateException(msg);
            }
            return ret;
        }
         AminoAcidAtLocation[] locations = subUnit.getLocations();
        for (int i = Math.max(subUnit.getMinLoc(),pos); i < locations.length; i++) {
            AminoAcidAtLocation loc = locations[i];
            if(loc.getLocation() == -1 && loc.getAminoAcid() == aa) {
                subUnit.setRealLocation(loc,pos);
                return ret;
            }

        }
        return ret;
      }

    public AminoAcidAtLocation[] getAminoAcidsForSequence(String foundSequence) {
        for(ProteinSubunit su : getSubUnits()) {
            AminoAcidAtLocation[] ret = su.internalAminoAcidsForSequence(foundSequence);
            if (ret != null)
                 return ret;

        }
        for(ProteinSubunit su : getSubUnits()) {
            AminoAcidAtLocation[] ret = su.getAminoAcidsForCloseSequence(foundSequence);
            if (ret != null)
                 return ret;

        }
         return null;
    }
//
//    protected AminoAcidAtLocation[] internalAminoAcidsForSequence(String foundSequence) {
//        FastaAminoAcid[] aas = FastaAminoAcid.asAminoAcids(foundSequence);
//        for (int i = 0; i < m_DisplayedAminoAcids.size() - aas.length; i++) {
//            AminoAcidAtLocation aminoAcidAtLocation = m_DisplayedAminoAcids.get(i);
//            if (aminoAcidAtLocation.getAminoAcid() == aas[0]) {
//                AminoAcidAtLocation[] ret = maybeBuildSequence(aas, i);
//                if (ret != null)
//                    return ret;
//            }
//
//        }
//        return null;
//
//    }
//
//    /**
//     * @param foundSequence
//     * @return
//     */
//    protected AminoAcidAtLocation[] getAminoAcidsForCloseSequence(String foundSequence) {
//        String modelSequence = getSequence();
//        SmithWaterman sw = new SmithWaterman(modelSequence, foundSequence);
//        List<SimpleChainingMatch> matches = sw.getMatches();
//        if (matches.size() == 0) {
//            // try again
////            sw = new SmithWaterman(modelSequence, foundSequence,SmithWaterman.DEFAULT_SCORE_THRESHOLD / 3);
////            matches = sw.getMatches();
////            for (Iterator<SimpleChainingMatch> iterator = matches.iterator(); iterator.hasNext(); ) {
////                SimpleChainingMatch best =  iterator.next();
////                String sequenceInTarget = modelSequence.substring(best.getFromA(),best.getToA());
////                System.out.println(sequenceInTarget);
////            }
////            System.out.println(foundSequence + " sought");
////            System.out.println(modelSequence);
////            System.out.println();
//            return null;
//        }
//        SimpleChainingMatch best = matches.get(0);
//        String sequenceInTarget = modelSequence.substring(best.getFromA(), best.getToA());
//        return internalAminoAcidsForSequence(sequenceInTarget);
//
//
//    }
//
//
//    private AminoAcidAtLocation[] maybeBuildSequence(FastaAminoAcid[] aas, int start) {
//        List<AminoAcidAtLocation> holder = new ArrayList<AminoAcidAtLocation>();
//        holder.add(m_DisplayedAminoAcids.get(start));
//        for (int i = 1; i < aas.length; i++) {
//            AminoAcidAtLocation aminoAcidAtLocation = m_DisplayedAminoAcids.get(start + i);
//            if (aminoAcidAtLocation.getAminoAcid() == aas[i]) {
//                holder.add(aminoAcidAtLocation);
//            }
//            else {
//                return null; // bad
//            }
//        }
//
//        AminoAcidAtLocation[] ret = new AminoAcidAtLocation[holder.size()];
//        holder.toArray(ret);
//        return ret;
//    }

    public static final String[] SOUGHT_SEQUENCES = {
            "HMDLCLTVVDR",
            "HVGSNLCLDSR",
            "HMDLCLTVVDRAPGSLIK",
            "VLTFLDSHCECNEHWLEPLLER",
            "HMDLCLTVVDRAPGSLIK",
    };

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        PDBObject obj = new PDBObject(f, null);
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
