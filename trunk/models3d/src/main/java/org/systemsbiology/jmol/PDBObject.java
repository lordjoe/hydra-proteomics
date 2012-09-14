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
    private final SequenceChainMap[] m_Mappings;
    private int m_LastHandledLoc = -1;
    private ExperimentalType m_ExperimentalType = ExperimentalType.X_RAY_DIFFRACTION;
    //   private final List<AminoAcidAtLocation> m_DisplayedAminoAcids = new ArrayList<AminoAcidAtLocation>();
    //    private String m_Sequence;
    private final Map<ChainEnum, ProteinSubunit> m_Chains = new HashMap<ChainEnum, ProteinSubunit>();
    private final Map<String, List<ProteinSubunit>> m_SequenceToChains = new HashMap<String, List<ProteinSubunit>>();

    public PDBObject(File file, Protein p) {
        m_File = file;
        m_Protein = p;
        String sequence = p.getSequence();
        m_Mappings = new SequenceChainMap[sequence.length()];
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            FastaAminoAcid aa = FastaAminoAcid.fromChar(c);
            m_Mappings[i] = new SequenceChainMap(p, i, aa);
        }
        if (!m_File.exists())
            throw new IllegalStateException("bad file " + m_File);
        String[] lines = FileUtilities.readInLines(m_File);
//            LineNumberReader rdr = new LineNumberReader(new FileReader(m_File));
        readFromReader(lines);
        // save the models that are used
          FileUtilities.copyFile(file, new File("E:/tmp/Models3d/" + file.getName()));

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

    public ChainEnum[] getChains() {
        guaranteeChains();
        ChainEnum[] ret = m_Chains.keySet().toArray(ChainEnum.EMPTY_ARRAY);
        Arrays.sort(ret);
        return ret;
    }

    public SequenceChainMap[] getMappings() {
        return m_Mappings;
    }

    public SequenceChainMap getMapping(int i) {
        return m_Mappings[i];
    }

    protected void guaranteeChains() {
        if (m_Chains.isEmpty()) {
            throw new IllegalStateException("problem"); // ToDo change
//            ChainEnum[] chains = buildChains();
//            for (int i = 0; i < chains.length; i++) {
//                ChainEnum chain = chains[i];
//                ProteinSubunit su = buildSubunit(chain);
//                m_Chains.put(chain, su);
//            }
        }
    }

//    protected ChainEnum[] buildChains() {
//        Set<ChainEnum> holder = new HashSet<ChainEnum>();
//        for (AminoAcidAtLocation aa : m_DisplayedAminoAcids) {
//            holder.add(aa.getChain());
//        }
//        return holder.toArray(ChainEnum.EMPTY_ARRAY);
//    }
//
//    protected ProteinSubunit buildSubunit(final ChainEnum chain) {
//        List<AminoAcidAtLocation> hdx = new ArrayList<AminoAcidAtLocation>();
//        for (AminoAcidAtLocation aa : m_DisplayedAminoAcids) {
//            if (chain == aa.getChain()) {
//                hdx.add(aa);
//            }
//        }
//
//        AminoAcidAtLocation[] locs = new AminoAcidAtLocation[hdx.size()];
//        hdx.toArray(locs);
//        return new ProteinSubunit(chain, getProtein(), locs);
//    }

    public ProteinSubunit[] getSubUnits() {
        ProteinSubunit[] ret = m_Chains.values().toArray(ProteinSubunit.EMPTY_ARRAY);
        Arrays.sort(ret, ProteinSubunit.CHAIN_SORTER);
        return ret;
    }

    public ProteinSubunit getSubUnit(ChainEnum chain) {
        ProteinSubunit proteinSubunit = m_Chains.get(chain);
        if (proteinSubunit != null)
            return proteinSubunit;
        proteinSubunit = new ProteinSubunit(getProtein(), chain);
        m_Chains.put(chain, proteinSubunit);
        return proteinSubunit;
    }

//    public String getSequence() {
//        return m_Sequence;
//    }

    public void readFromReader(String[] lines) {
        m_LastHandledLoc = -1;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            handleConditions(line);
        }
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            handleAtoms(line);
        }
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            handleStructure(line);
        }
        List<IAminoAcidAtLocation> holder = new ArrayList<IAminoAcidAtLocation>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            handleResidues(line, holder);
        }
        IAminoAcidAtLocation[] ret = new IAminoAcidAtLocation[holder.size()];
        holder.toArray(ret);
        buildSequenceMappings();
    }

    protected void handleConditions(final String line) {
        if (line.startsWith("EXPDTA"))   {
            String trim = line.substring(10).trim();
            ExperimentalType.fromString(trim);
            setExperimentalType(ExperimentalType.fromString(trim));
            return;
        }
        String s = null;
    }


    protected void handleResidues(String line, List<IAminoAcidAtLocation> holder) {
        if (!line.startsWith("SEQRES"))
            return;
        String s = null;

        s = line.substring(8, 10).trim();
        int lineNumForChain = Integer.parseInt(s);


        s = line.substring(11, 12).trim();
        ChainEnum chainId = ChainEnum.fromString(s);
        ProteinSubunit chain = m_Chains.get(chainId);

        s = line.substring(13, 17).trim();
        int numRes = Integer.parseInt(s);
          for (int startRes = 19; startRes < line.length() - 3; startRes += 4) {
            s = line.substring(startRes, startRes + 4).trim();
            if(s.length() > 0) {
                FastaAminoAcid fa = FastaAminoAcid.fromAbbreviation(s);
                if(fa != null) {
                    IAminoAcidAtLocation aa = new ProteinAminoAcid(chainId,fa);
                     chain.addAminoAcidAtSeqres(aa);

                }

            }
        }


    }

    private void buildSequenceMappings() {
        for (ChainEnum ch : getChains()) {
            ProteinSubunit subUnit = getSubUnit(ch);
            buildSequenceMappings(subUnit, ch);
        }
    }

    private void buildSequenceMappings(ProteinSubunit subUnit, ChainEnum ch) {
        String chainSeq = subUnit.getSequence();
        AminoAcidAtLocation[] seqres = subUnit.getSeqres();
        for (int i = 0; i < seqres.length - 1; i++) {
            AminoAcidAtLocation seqre = seqres[i];
            ProteinAminoAcid pa = seqre.getProteinAminoAcid();
            FastaAminoAcid aa = seqre.getAminoAcid();

            if(aa == FastaAminoAcid.K || aa == FastaAminoAcid.R)  {
                FastaAminoAcid nextAA = seqres[i + 1].getAminoAcid();
                if(nextAA != FastaAminoAcid.P)  {
                    pa.setPotentialCleavage(true);
                }
            }
        }
         String seuresSeg = subUnit.getSeqresSequence();
        int seqresLength = seuresSeg.length();


        String seqence = getProtein().getSequence();
        int proteinLength = seqence.length();

        SmithWaterman sw = new SmithWaterman(seqence, seuresSeg);
         List<SimpleChainingMatch> matches = sw.getMatches();
        for (Iterator<SimpleChainingMatch> iterator = matches.iterator(); iterator.hasNext(); ) {
            SimpleChainingMatch best = iterator.next();
            int fromA = best.getFromA();
             int toA = best.getToA();
             int fromB = best.getFromB();
             int toB = best.getToB();
             int bindex = fromB;
             for (int i = fromA; i < toA; i++) {
                 int zeroBassed = i - 1;
                 SequenceChainMap mapping = getMapping(zeroBassed);
                 FastaAminoAcid seqaa = FastaAminoAcid.fromChar(seqence.charAt(zeroBassed));
                 AminoAcidAtLocation bx = seqres[bindex - 1];  // zero based

                 FastaAminoAcid baa = bx.getAminoAcid();
                 bx.setLocation(bindex);
                 if (seqaa == baa) {
                     mapping.addChainMapping(ch, bx);
                     bindex++;
                 }
                 else {
                     bindex++;
                 }
                 if (bindex >= seqres.length)
                     break;

                 // mapping.addChainMapping(ch, locations[i]);
             }
        }

        IAminoAcidAtLocation[] locations = subUnit.getLocations();
         //       if (seqence.contains("X"))
        //           return;
        //       if (chainSeq.contains("X"))
        //           return;
        int index = seqence.indexOf(chainSeq);
        if (index > -1) {
            for (int i = 0; i < chainSeq.length(); i++) {
                SequenceChainMap mapping = getMapping(index + i);
                mapping.addChainMapping(ch, locations[i]);
            }
        }
        else {
              sw = new SmithWaterman(seqence, chainSeq);
            matches = sw.getMatches();
            if (!matches.isEmpty()) {
                SimpleChainingMatch best = matches.get(0);
                int fromA = best.getFromA();
                int toA = best.getToA();
                int fromB = best.getFromB();
                int toB = best.getToB();
                int bindex = fromB;
                for (int i = fromA; i < toA; i++) {
                    int zeroBassed = i - 1;
                    SequenceChainMap mapping = getMapping(zeroBassed);
                    FastaAminoAcid seqaa = FastaAminoAcid.fromChar(seqence.charAt(zeroBassed));
                    IAminoAcidAtLocation bx = locations[bindex - 1];  // zero based
                    FastaAminoAcid baa = bx.getAminoAcid();
                    if (seqaa == baa) {
                        mapping.addChainMapping(ch, bx);
                        bindex++;
                    }
                    else {
                        bindex++;
                    }
                    if (bindex >= locations.length)
                        break;

                    // mapping.addChainMapping(ch, locations[i]);
                }

            }
            else {
                index = seqence.indexOf(chainSeq); // breal kere

            }
        }
    }

    private void handleAtoms(String line) {
        if (line.startsWith("SEQRES")) {
            return;
        }
        if (line.startsWith("ATOM")) {
            //           guaranteeChains();
            handleAtom(line);
            return;
        }
        if (line.startsWith("HETATM")) {
            //          guaranteeChains();
            handleAtom(line);
            return;
        }
        if (line.startsWith("HELIX")) {
            return;
        }
        if (line.startsWith("SHEET")) {
            return;
        }
        if (line.startsWith("SSBOND")) {
            return;
        }

    }

    private void handleStructure(String line) {
        if (line.startsWith("SEQRES")) {
            //   handleSeqRes(line);
            return;
        }
        if (line.startsWith("ATOM")) {
            return;
        }
        if (line.startsWith("HETATM")) {
            return;
        }
        if (line.startsWith("HELIX")) {
            guaranteeChains();
            handleHelix(line);
        }
        if (line.startsWith("SHEET")) {
            guaranteeChains();
            handleSheet(line);
        }
        if (line.startsWith("SSBOND")) {
            guaranteeChains();
            handleSSBond(line);
        }

    }

    public ExperimentalType getExperimentalType() {
        return m_ExperimentalType;
    }

    public void setExperimentalType(final ExperimentalType experimentalType) {
        m_ExperimentalType = experimentalType;
    }

    /*
SSBOND	1-6	"SSBOND"		character
8-10	Serial number	right	integer
12-14	Residue name ("CYS")	right	character
16	Filter.Chain identifier		character
18-21	Residue sequence number	right	integer
22	Code for insertions of residues		character
26-28	Residue name ("CYS")	right	character
30	Chain identifier		character
32-35	Residue sequence number	right	integer
36	Code for insertions of residues		character
60-65	Symmetry operator for first residue	right	integer
67-72	Symmetry operator for second residue	right	integer
74-78	Length of disulfide bond	right	real (5.2)
    */
    protected final void handleSSBond(String line) {

        String s;
        s = line.substring(7, 10).trim();
        int HelixNum = Integer.parseInt(s);
        s = line.substring(11, 14).trim();
        String Residue = s;
        if (!"CYS".equals(s))
            throw new IllegalStateException("should be CYS");
        s = line.substring(15, 18).trim();

        s = line.substring(15, 16).trim();
        ChainEnum chainId = ChainEnum.fromString(s);

        ProteinSubunit chain = m_Chains.get(chainId);
        if (chain == null)
            throw new IllegalStateException("bad chain " + chainId);

        s = line.substring(17, 21).trim();
        int ResNum = Integer.parseInt(s);

        s = line.substring(25, 28).trim();
        if (!"CYS".equals(s))
            throw new IllegalStateException("should be CYS");
        String ResidueEnd = s;

        s = line.substring(29, 30).trim();
        ChainEnum chainEnd = ChainEnum.fromString(s);
        ProteinSubunit chainLast = m_Chains.get(chainEnd);
        if (chain == null)
            throw new IllegalStateException("bad chain " + chainEnd);
        if (chain != chainLast)
            return;
        //  throw new IllegalStateException("bad last chain ");

        s = line.substring(31, 35).trim();
        int ResNumEnd = Integer.parseInt(s);


        s = line.substring(73, 78).trim();
        double length = Double.parseDouble(s);
        AminoAcidAtLocation[] locations = chain.getLocations();
        int start = -1;
        for (int i = 0; i < locations.length; i++) {
            AminoAcidAtLocation aa = locations[i];
            int loc = aa.getLocation();
            String res = aa.getAminoAcid().getAbbreviation();
            if ("CYS".equals(res)) {
                if (loc == ResNum) {
                    start = i;
                    aa.setDiSulphideBond(true);
                    //    aa.setStructure(SecondaryStructure.SHEET);   // mark as disulphide
                    break;
                }
            }
        }
        if (start == -1)
            throw new IllegalStateException("cannot find " + Residue + ResNum);
        for (int i = start; i < locations.length; i++) {
            AminoAcidAtLocation aa = locations[i];
            String res = aa.getAminoAcid().getAbbreviation();
            int loc = aa.getLocation();
            if ("CYS".equals(res)) {
                if (loc == ResNumEnd) {
                    aa.setDiSulphideBond(true);
                    //    aa.setStructure(SecondaryStructure.SHEET);  // mark as disulphide
                    return;
                }
            }
        }
        throw new IllegalStateException("end not found");
    }

    /*
HELIX	1-5	"HELIX"		character
8-10	Helix serial number	right	integer
12-14	Helix identifier	right	character
16-18 	Initial residue name	right	character
20	Chain identifier		character
22-25	Residue sequence number	right	integer
26	Code for insertions of residues		character
28-30 	Terminal residue name	right	character
32	Chain identifier		character
34-37	Residue sequence number	right	integer
38	Code for insertions of residues		character
39-40	Type of helix 	right	integer
41-70	Comment	left	character
72-76	Length of helix	right	integer
*/
    protected final void handleHelix(String line) {
        String s;
        s = line.substring(7, 10).trim();
        int HelixNum = Integer.parseInt(s);
        s = line.substring(11, 14).trim();
        String HelixId = s;
        s = line.substring(15, 18).trim();
        String Residue = s;
        s = line.substring(19, 20).trim();
        ChainEnum chainId = ChainEnum.fromString(s);
        ProteinSubunit chain = m_Chains.get(chainId);
        if (chain == null)
            throw new IllegalStateException("bad chain " + chainId);

        s = line.substring(21, 25).trim();
        int ResNum = Integer.parseInt(s);

        s = line.substring(27, 30).trim();
        String ResidueEnd = s;
        s = line.substring(31, 32).trim();
        ChainEnum chainEnd = ChainEnum.fromString(s);
        ProteinSubunit chainLast = m_Chains.get(chainEnd);
        if (chain == null)
            throw new IllegalStateException("bad chain " + chainEnd);
        if (chain != chainLast)
            throw new IllegalStateException("bad last chain ");

        s = line.substring(33, 37).trim();
        int ResNumEnd = Integer.parseInt(s);

        s = line.substring(38, 40).trim();
        int HelixType = Integer.parseInt(s);

        s = line.substring(71, 76).trim();
        int length = Integer.parseInt(s);
        AminoAcidAtLocation[] locations = chain.getLocations();
        int start = -1;
        for (int i = 0; i < locations.length; i++) {
            IAminoAcidAtLocation aa = locations[i];
            int loc = aa.getLocation();
            String res = aa.getAminoAcid().getAbbreviation();
            if (Residue.equals(res)) {
                if (loc == ResNum) {
                    start = i;
                    break;
                }


            }
        }
        if (start == -1) {
            return;
            // throw new IllegalStateException("cannot find " + Residue + ResNum);
        }
        for (int i = start; i < Math.min(locations.length, start + length); i++) {
            AminoAcidAtLocation aa = locations[i];
            aa.setStructure(UniprotFeatureType.HELIX);
        }

    }


    /*
    SHEET	1-5	"SHEET"		character
    8-10	Strand number (in current sheet)	right	integer
    12-14	Sheet identifier	right	character
    15-16	Number of strands (in current sheet)	right	integer
    18-20 	Initial residue name	right	character
    22	Filter.Chain identifier		character
    23-26	Residue sequence number	right	integer
    27	Code for insertions of residues		character
    29-31 	Terminal residue name	right	character
    33	Chain identifier		character
    34-37	Residue sequence number	right	integer
    38	Code for insertions of residues		character
    39-40	Strand sense with respect to previous 	right	integer
    The following fields identify two atoms involved in a hydrogen bond,
    the first in the current strand and the second in the previous strand.
    These fields should be blank for strand 1 (the first strand in a sheet).

    42-45	Atom name (as per ATOM record)	left	character
    46-48 	Residue name	right	character
    50	Chain identifier		character
    51-54	Residue sequence number	right	integer
    55	Code for insertions of residues		character
    57-60	Atom name (as per ATOM record)	left	character
    61-63 	Residue name	right	character
    65	Chain identifier		character
    66-69	Residue sequence number	right	integer
    70	Code for insertions of residues		character
    */
    protected final void handleSheet(String line) {
        String s;
        s = line.substring(7, 10).trim();
        int HelixNum = Integer.parseInt(s);
        s = line.substring(11, 14).trim();
        String HelixId = s;
        s = line.substring(17, 20).trim();
        String Residue = s;
        s = line.substring(21, 22).trim();
        ChainEnum chainId = ChainEnum.fromString(s);
        ProteinSubunit chain = m_Chains.get(chainId);
        if (chain == null)
            throw new IllegalStateException("bad chain " + chainId);

        s = line.substring(22, 26).trim();
        int ResNum = Integer.parseInt(s);

        s = line.substring(28, 31).trim();
        String ResidueEnd = s;
        s = line.substring(32, 33).trim();
        ChainEnum chainEnd = ChainEnum.fromString(s);
        ProteinSubunit chainLast = m_Chains.get(chainEnd);
        if (chain == null)
            throw new IllegalStateException("bad chain " + chainEnd);
        if (chain != chainLast)
            throw new IllegalStateException("bad last chain ");

        s = line.substring(33, 37).trim();
        int ResNumEnd = Integer.parseInt(s);

        s = line.substring(38, 40).trim();
        int HelixType = 0;
        if (s.length() > 0)
            HelixType = Integer.parseInt(s);

        AminoAcidAtLocation[] locations = chain.getLocations();
        int start = -1;
        for (int i = 0; i < locations.length; i++) {
            IAminoAcidAtLocation aa = locations[i];
            int loc = aa.getLocation();
            String res = aa.getAminoAcid().getAbbreviation();
            if (Residue.equals(res)) {
                if (loc == ResNum) {
                    start = i;
                    break;
                }
            }
        }
        if (start == -1)
            return;
        //        throw new IllegalStateException("cannot find " + Residue + ResNum);
        for (int k = start; k < locations.length; k++) {
            AminoAcidAtLocation aa = locations[k];
            aa.setStructure(UniprotFeatureType.STRAND);
            String res = aa.getAminoAcid().getAbbreviation();
            int loc = aa.getLocation();
            if (res.equals(ResidueEnd)) {
                if (loc == ResNumEnd) {
                    return;
                }
            }
        }
        if (true)
            return;
        throw new IllegalStateException("end not found");

    }


    private void handleSeqRes(String line) {
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
            int resNum = i + 1;
            AminoAcidAtLocation now = buildSubunit(chain, item, resNum);
            String id = AsaSubunit.buildSubUnitString(chain.toString(), item, resNum);
            addSubUnit(id, now);
            //  m_DisplayedAminoAcids.add(now);
            //          sb.append(aa.toString());

        }
    }

    /**
     * override builds  AminoAcidAtLocation
     */
    @Override
    protected AminoAcidAtLocation buildSubunit(ChainEnum chainId, String resType, final int pos) {
        FastaAminoAcid aa = FastaAminoAcid.fromAbbreviation(resType);
        if (aa == null)
            return null;
        final ProteinAminoAcid asaSubunit;
        asaSubunit = new ProteinAminoAcid(chainId, aa);
        asaSubunit.setLocation(pos);
        ProteinSubunit subUnit = getSubUnit(chainId);
        subUnit.addAminoAcidAtLocation(asaSubunit);
        //     AminoAcidAtLocation test = subUnit.getAminoAcidAtLocation(pos);
         return new AminoAcidAtLocation(chainId,asaSubunit);
    }


//    private void handleLine(String line) {
//        if (line.startsWith("ATOM")) {
//            handleAtom(line);
//        }
//    }
//
//    @Override
//    protected AsaAtom buildAtom(String line) {
//        AsaAtom ret = null;
//        try {
//            ret = super.buildAtom(line);
//        }
//        catch (UnknownAtomException e) {
//            return null;
//        }
//        catch (Exception e) {
//            return null;
//        }
//        if (ret == null)
//            return null;
//        String aaStr = line.substring(17, 20);
//        FastaAminoAcid aa = FastaAminoAcid.fromAbbreviation(aaStr);
//        if (aa == null)
//            return ret;
//        String chainStr = line.substring(21, 22);
//        ChainEnum chain = ChainEnum.fromString(chainStr);
//        String posStr = line.substring(22, 26).trim();
//        // sometimed we see lines like    ARG B  37A     24.919  22.314  18.745  0.08 30.67           N
//        String modifier = line.substring(26, 27);
//        if (!" ".equals(modifier))
//            return ret; // i cannot handle that
//        int pos = Integer.parseInt(posStr);
//        ProteinSubunit subUnit = getSubUnit(chain);
//        AminoAcidAtLocation test = subUnit.getAminoAcidAtLocation(pos);
//        if (test != null) {
//            if (test.getAminoAcid() != aa) {
//                String msg = "bad location  " + getFile() + " " + line;
//                throw new IllegalStateException(msg);
//            }
//            return ret;
//        }
//        AminoAcidAtLocation[] locations = subUnit.getLocations();
//        for (int i = Math.max(subUnit.getMinLoc(), pos); i < locations.length; i++) {
//            AminoAcidAtLocation loc = locations[i];
//            if (loc.getLocation() == -1 && loc.getAminoAcid() == aa) {
//                subUnit.setRealLocation(loc, pos);
//                return ret;
//            }
//
//        }
//        return ret;
//    }

    public IAminoAcidAtLocation[] getAminoAcidsForSequence(String foundSequence) {
        for (ProteinSubunit su : getSubUnits()) {
            IAminoAcidAtLocation[] ret = su.internalAminoAcidsForSequence(foundSequence);
            if (ret != null)
                return ret;

        }
        for (ProteinSubunit su : getSubUnits()) {
            IAminoAcidAtLocation[] ret = su.getAminoAcidsForCloseSequence(foundSequence);
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
        List<IAminoAcidAtLocation[]> holder = new ArrayList<IAminoAcidAtLocation[]>();

        for (int i = 0; i < SOUGHT_SEQUENCES.length; i++) {
            String foundSequence = SOUGHT_SEQUENCES[i];
            IAminoAcidAtLocation[] locs = obj.getAminoAcidsForSequence(foundSequence);
            holder.add(locs);
        }
        IAminoAcidAtLocation[][] ret = new IAminoAcidAtLocation[holder.size()][];
        holder.toArray(ret);
        ScriptWriter sw = new ScriptWriter();
        String script = sw.writeScript(obj, SOUGHT_SEQUENCES);
        System.out.println(script);
    }
}
