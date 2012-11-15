package org.systemsbiology.xtandem.fragmentation;

import org.systemsbiology.jmol.*;
import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.peptide.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.fragmentation.ProteinSubunit
 * User: Steve
 * Date: 7/3/12
 */
public class ProteinSubunit {
    public static final ProteinSubunit[] EMPTY_ARRAY = {};

    public static final Comparator<ProteinSubunit> CHAIN_SORTER = new ChainSortComparator();

    /**
     * order by chain - we assume no two have same vhain
     */
    private static class ChainSortComparator implements Comparator<ProteinSubunit> {
        private ChainSortComparator() {
        }

        @Override
        public int compare(final ProteinSubunit o1, final ProteinSubunit o2) {
            return o1.getChain().compareTo(o2.getChain());
        }
    }

    private final ChainEnum m_Chain;
    private final Protein m_Protein;
    private final List<IAminoAcidAtLocation> m_Locations = new ArrayList<IAminoAcidAtLocation>();
    private final List<IAminoAcidAtLocation> m_Seqres = new ArrayList<IAminoAcidAtLocation>();
    private Map<Integer, IAminoAcidAtLocation> m_LocToAminoAcid = new HashMap<Integer, IAminoAcidAtLocation>();
    private   String m_Sequence;
 //   private final  ChainEnum[] m_ChainSet; // only non-null if multiple chains
    private int m_MinLoc = 0;

    public ProteinSubunit( Protein prot,final ChainEnum chain) {
        m_Chain = chain;
        m_Protein = prot;
  //      m_ChainSet = null;
    }

    public ProteinSubunit(final ChainEnum chain, Protein prot, final IAminoAcidAtLocation[] locations) {
       this( prot,chain);
        m_Locations.addAll(Arrays.asList(locations));
        m_Sequence = AminoAcidAtLocation.toSequence(locations);
//        ChainEnum[] chains = AminoAcidAtLocation.toChains(locations);
//        if (chains.length > 1)
//            m_ChainSet = chains;
//        else
//            m_ChainSet = null;
    }




    public void addAminoAcidAtLocation(IAminoAcidAtLocation added)   {
        if(!m_Locations.isEmpty())      {
            IAminoAcidAtLocation top = m_Locations.get(m_Locations.size() - 1);
            if(top.getAminoAcid() == added.getAminoAcid()) {
                if(top.getLocation() == added.getLocation())
                    return;
            }
        }
         m_Locations.add(added);
        m_Sequence = null;
    }


    public void addAminoAcidAtSeqres(IAminoAcidAtLocation added)   {
         m_Seqres.add(added);
      }

    public IAminoAcidAtLocation[] getSeqres()
    {
        return m_Seqres.toArray(IAminoAcidAtLocation.EMPTY_ARRAY);
    }

    public String getSeqresSequence()
    {
         IAminoAcidAtLocation[] aas = getSeqres();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < aas.length; i++) {
            IAminoAcidAtLocation aa = aas[i];
            sb.append(aa.getAminoAcid().toString());
        }
        return sb.toString();
    }

    public Protein getProtein() {
        return m_Protein;
    }

    public ChainEnum getChain() {
        return m_Chain;
    }

    public IAminoAcidAtLocation[] getLocations() {
        return m_Locations.toArray(IAminoAcidAtLocation.EMPTY_ARRAY);
    }

    public String getSequence() {
        if(m_Sequence == null)
            m_Sequence = AminoAcidAtLocation.toSequence(getLocations());
        return m_Sequence;
    }

    @Override
    public String toString() {
        return getSequence();
    }

    public void setRealLocation(AminoAcidAtLocation loc, int pos) {
        if(loc.getLocation() != -1)
            throw new IllegalStateException("reset location " + loc);
        loc.setLocation(pos);
        m_LocToAminoAcid.put(pos, loc);
        m_MinLoc = Math.max(m_MinLoc,pos);
    }

    public int getMinLoc() {
        return m_MinLoc;
    }

    public IAminoAcidAtLocation getAminoAcidAtLocation(int loc) {
        return m_LocToAminoAcid.get(loc);
    }


    public IAminoAcidAtLocation[] getAminoAcidsForSequence(String foundSequence) {
        IAminoAcidAtLocation[] ret = internalAminoAcidsForSequence(foundSequence);
        if (ret != null)
            return ret;
        return getAminoAcidsForCloseSequence(foundSequence);
    }

    public IAminoAcidAtLocation[] internalAminoAcidsForSequence(String foundSequence) {
        FastaAminoAcid[] aas = FastaAminoAcid.asAminoAcids(foundSequence);
        for (int i = 0; i < m_Locations.size() - aas.length; i++) {
            IAminoAcidAtLocation test = m_Locations.get(i);
            if (test.getLocation() != -1 && test.getAminoAcid() == aas[0]) {
                IAminoAcidAtLocation[] ret = maybeBuildSequence(aas, i);
                if (ret != null)
                    return ret;
            }

        }
        return null;

    }

    /**
     * @param foundSequence
     * @return
     */
    public IAminoAcidAtLocation[] getAminoAcidsForCloseSequence(String foundSequence) {
        String modelSequence = getSequence();
        SmithWaterman sw = new SmithWaterman(modelSequence, foundSequence);
        List<SimpleChainingMatch> matches = sw.getMatches();
        if (matches.size() == 0) {
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
        String sequenceInTarget = modelSequence.substring(best.getFromA(), best.getToA());
        return internalAminoAcidsForSequence(sequenceInTarget);


    }


    private IAminoAcidAtLocation[] maybeBuildSequence(FastaAminoAcid[] aas, int start) {
        List<IAminoAcidAtLocation> holder = new ArrayList<IAminoAcidAtLocation>();
        holder.add(m_Locations.get(start));
        for (int i = 1; i < aas.length; i++) {
            IAminoAcidAtLocation test = m_Locations.get(start + i);
            if (test.getLocation() != -1 && test.getAminoAcid() == aas[i]) {
                holder.add(test);
            }
            else {
                return null; // bad
            }
        }

        IAminoAcidAtLocation[] ret = new IAminoAcidAtLocation[holder.size()];
        holder.toArray(ret);
        return ret;
    }
}
