package org.systemsbiology.xtandem.peptide;

import org.apache.openjpa.persistence.criteria.*;
import org.systemsbiology.xtandem.*;

import java.util.*;

/**
 * Backs up a simpler peptide
 * org.systemsbiology.xtandem.peptide.ModifiedPolypeptideBackup
 * User: steven
 * Date: 6/30/11
 */
public class ModifiedPolypeptide extends Polypeptide implements IModifiedPeptide {
    public static final ModifiedPolypeptide[] EMPTY_ARRAY = {};

    /**
     * take a string like AK[]GHTE
     *
     * @param s !null string
     * @return !null peptide
     */
    public static IPolypeptide fromModifiedString(String s) {
        return fromModifiedString(s, 0);
    }

    /**
     * take a string like AK[81.456]GHTE
     *
     * @param s !null string
     * @return !null peptide
     */
    public static IPolypeptide fromModifiedString(String s, int missed_cleavages) {
        String unmods = buildUnmodifiedSequence(s);
        PeptideModification[] mods = new PeptideModification[unmods.length()];
        int charNumber = -1;
        String lastChar = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                int index = s.indexOf("]", i);
                String modText = s.substring(i + 1, index);
                double offest = Double.parseDouble(modText);
                PeptideModification mod = PeptideModification.fromString(modText + "@" +  lastChar,PeptideModificationRestriction.Global,false ) ;
                if (charNumber >= mods.length)
                    XTandemUtilities.breakHere();
                mods[charNumber] = mod;
                i = index;
            }
            else {
                lastChar = Character.toString(c);
                charNumber++;
            }

        }
        ModifiedPolypeptide ret = new ModifiedPolypeptide(unmods, missed_cleavages, mods);
        ret.setMissedCleavages(PeptideBondDigester.getDefaultDigester().probableNumberMissedCleavages(ret));

        return ret;
    }

    /**
     * drop modifications
     *
     * @param s
     * @return
     */
    public static String buildUnmodifiedSequence(String s) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '[':
                case ']':
                case '-':
                case '.':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * add any modifications at the C and N terminals
     *
     * @param peptide
     * @return
     */
    public static PeptideModification[] buildTerminalModifications(IPolypeptide peptide) {
        String sequence = peptide.getSequence().toUpperCase();
        FastaAminoAcid NTerminal = FastaAminoAcid.valueOf(sequence.substring(0, 1));
        FastaAminoAcid CTerminal = FastaAminoAcid.valueOf(
                sequence.substring(sequence.length() - 1, sequence.length()));
        PeptideModification[] nmods = PeptideModification.getNTerminalModifications(NTerminal);
        PeptideModification[] cmods = PeptideModification.getCTerminalModifications(CTerminal);
        if (nmods.length == 0 && cmods.length == 0)
            return PeptideModification.EMPTY_ARRAY;
        List<PeptideModification> holder = new ArrayList<PeptideModification>();
        holder.addAll(Arrays.asList(nmods));
        holder.addAll(Arrays.asList(cmods));
        PeptideModification[] ret = new PeptideModification[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static IModifiedPeptide[] buildModifications(IPolypeptide peptide,
                                                        PeptideModification[] mod) {

        PeptideModification[] tmods = ModifiedPolypeptide.buildTerminalModifications(peptide);
        if (mod.length == 0 && tmods.length == 0)
            return IModifiedPeptide.EMPTY_ARRAY;
        List<IModifiedPeptide> holder = new ArrayList<IModifiedPeptide>();
        String sequence = peptide.getSequence();
        for (int i = 0; i < mod.length; i++) {
            applyModification(peptide, mod[i], holder, sequence);
        }
        for (int i = 0; i < tmods.length; i++) {
            applyTerminalModification(peptide, tmods[i], holder);
        }
        IModifiedPeptide[] ret = new IModifiedPeptide[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    /**
     * this version will allow multiple modifications
     *
     * @param peptide input peptide
     * @param mod     !null array of modifications
     * @return
     */
    public static IModifiedPeptide[] buildAllModifications(IPolypeptide peptide,
                                                           PeptideModification[] mod) {

        PeptideModification[] tmods = ModifiedPolypeptide.buildTerminalModifications(peptide);
        if (mod.length == 0 && tmods.length == 0)
            return IModifiedPeptide.EMPTY_ARRAY;
        List<IModifiedPeptide> holder = new ArrayList<IModifiedPeptide>();
        String sequence = peptide.getSequence();

        for (int i = 0; i < mod.length; i++) {
            applyModification(peptide, mod[i], holder, sequence);
        }
        for (int i = 0; i < tmods.length; i++) {
            applyTerminalModification(peptide, tmods[i], holder);
        }

        // add more modifications
        for (int i = 0; i < mod.length; i++) {
            // modify modifies spectra
            IModifiedPeptide[] items = holder.toArray(IModifiedPeptide.EMPTY_ARRAY);
            for (int j = 0; j < items.length; j++) {
                IModifiedPeptide item = items[j];
                final String sequence1 = item.getSequence();
                applyModification(item, mod[i], holder, sequence1);
            }
        }
        for (int i = 0; i < tmods.length; i++) {
            // modify modifies spectra
            IModifiedPeptide[] items = holder.toArray(IModifiedPeptide.EMPTY_ARRAY);
            for (int j = 0; j < items.length; j++) {
                IModifiedPeptide item = items[j];
                applyModification(item, tmods[i], holder, item.getSequence());
            }
        }
        IModifiedPeptide[] ret = new IModifiedPeptide[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    protected static void applyTerminalModification(final IPolypeptide peptide,
                                                    final PeptideModification pPeptideModification,
                                                    final List<IModifiedPeptide> pHolder) {
        PeptideModification pm = pPeptideModification;
        String test = pm.applyTo();
        int start = 0;
        IModifiedPeptide e = null;
        switch (pm.getRestriction()) {
            case CTerminal:
                e = buildModification(peptide, pm, peptide.getSequence().length() - 1);
                break;
            case NTerminal:
                e = buildModification(peptide, pm, 0);
                break;
            default:
                throw new IllegalStateException("should only call for terminal modifications");
        }
        double mass = e.getMass(); // look here
        pHolder.add(e);
    }

    protected static void applyModification(final IPolypeptide peptide,
                                            final PeptideModification pPeptideModification,
                                            final List<IModifiedPeptide> pHolder,
                                            final String pSequence) {
        PeptideModification pm = pPeptideModification;
        String test = pm.applyTo();
        int start = 0;
        int index = pSequence.indexOf(test, start);
        while (index > -1) {
            IModifiedPeptide e = buildModification(peptide, pm, index);
            if (!pHolder.contains(e))
                pHolder.add(e);
            if (index > pSequence.length() - 1)
                break;
            index = pSequence.indexOf(test, index + 1);
        }
    }

    public static IModifiedPeptide buildModification(final IPolypeptide peptide,
                                                     final PeptideModification pm, final int index) {

        String sequence = peptide.getSequence();
        PeptideModification[] mods = null;
        if (peptide instanceof IModifiedPeptide) {
            IModifiedPeptide eptide = (IModifiedPeptide) peptide;
            mods = eptide.getModifications();
        }
        else {
            mods = new PeptideModification[sequence.length()];
        }
        if (mods[index] == pm)
            return (IModifiedPeptide) peptide;
        mods[index] = pm;
        ModifiedPolypeptide modifiedPolypeptide = new ModifiedPolypeptide(sequence, peptide.getMissedCleavages(), mods);
        modifiedPolypeptide.setContainedInProteins(peptide.getProteinPositions());
        return modifiedPolypeptide;

    }


    /**
     * routine to determine whether to return a PolyPeptide or a Modified Moplpeptide
     *
     * @param pSequence
     * @param missedCleavages
     * @param mods
     * @return
     */
    protected static IPolypeptide buildPolypeptide(final String pSequence,
                                                   final int missedCleavages,
                                                   PeptideModification[] mods) {
        if (pSequence.contains("."))
            XTandemUtilities.breakHere();

        for (int i = 0; i < mods.length; i++) {
            PeptideModification mod = mods[i];
            if (mod != null)
                return new ModifiedPolypeptide(pSequence, missedCleavages, mods);
        }
        return new Polypeptide(pSequence, missedCleavages);
    }


    private final PeptideModification[] m_SequenceModifications;

    public ModifiedPolypeptide(final String pSequence, final int missedCleavages,
                               PeptideModification[] mods) {
        super(pSequence, missedCleavages);
        if (mods.length != pSequence.length())
            throw new IllegalArgumentException("bad modifications array");
        PeptideModification[] modifications = new PeptideModification[mods.length];
        System.arraycopy(mods, 0, modifications, 0, mods.length);
        m_SequenceModifications = modifications;
    }

    /**
     * deibbrately hide the manner a peptide is cleaved to
     * support the possibility of the sequence pointing to the protein as
     * Java substring does
     *
     * @param bond non-negative bond
     * @return !null array of polypeptides
     * @throws IndexOutOfBoundsException on bad bond
     */
    @Override
    public IPolypeptide[] cleave(final int bond) throws IndexOutOfBoundsException {

        IPolypeptide[] ret = new Polypeptide[2];
        String sequence = getSequence();
        int assumeNoMissedCleavages = 0;

        PeptideModification[] mods = getModificationsInInterval(0, bond + 1);
        ret[0] = buildPolypeptide(sequence.substring(0, bond + 1), assumeNoMissedCleavages, mods);
        mods = getModificationsInInterval(bond + 1);
        ret[1] = buildPolypeptide(sequence.substring(bond + 1), assumeNoMissedCleavages, mods);

        IProteinPosition[] proteinPositions = this.getProteinPositions();

        if(proteinPositions == null)
            XTandemUtilities.breakHere();

        // add proteins and before and after AAN
        ((Polypeptide)ret[0]).setContainedInProteins(ProteinPosition.buildPeptidePositions(ret[0],0,proteinPositions));
        ((Polypeptide)ret[1]).setContainedInProteins(ProteinPosition.buildPeptidePositions(ret[1],bond + 1,proteinPositions));
        return ret;
    }


    protected PeptideModification[] getModificationsInInterval(int start) {
        return getModificationsInInterval(start, getSequence().length());
    }

    protected PeptideModification[] getModificationsInInterval(int start, int end) {
        int length = end - start;
        PeptideModification[] ret = new PeptideModification[length];
        System.arraycopy(m_SequenceModifications, start, ret, 0, length);
        return ret;
    }

    /**
     * deibbrately hide the manner a peptide is cleaved to
     * support the possibility of the sequence pointing to the protein as
     * Java substring does - this is usually used to convert a polupeptide id to poplpeptide
     *
     * @param start  start value
     * @param length sequence length
     * @return !null polypeptide
     * @throws IndexOutOfBoundsException
     */
    @Override
    public IPolypeptide fragment(final int start, final int length) throws IndexOutOfBoundsException {
        PeptideModification[] mods = getModificationsInInterval(start, start + length);
        return buildPolypeptide(getSequence().substring(start, start + length), 0, mods);
    }

    /**
     * build a polypeptide by putting the two peptides together
     *
     * @param added !null added sequence
     * @return !null merged peptide
     */
    @Override
    public IPolypeptide concat(final IPolypeptide added) {
        throw new UnsupportedOperationException("This may never be used");
    }

    @Override
    public String toString() {
        return getModifiedSequence();
    }

    /**
     * deibbrately hide the manner a peptide is cleaved to
     * support the possibility of the sequence pointing to the protein as
     * Java substring does
     *
     * @param bond non-negative bond
     * @return !null array of polypeptides
     * @throws IndexOutOfBoundsException on bad bond
     */
    @Override
    public IPolypeptide subsequence(final int start, final int end) throws IndexOutOfBoundsException {
        PeptideModification[] mods = getModificationsInInterval(start, end);
        return buildPolypeptide(getSequence().substring(start, end), 0, mods);
    }

    @Override
    public double getMass() {
        double massModification = getMassModification();
        double mass = super.getMass();
        return massModification + mass;
    }

    public double getMassModification() {
        double modifiedMass = 0;
        for (int i = 0; i < m_SequenceModifications.length; i++) {
            PeptideModification mod = m_SequenceModifications[i];
            if (mod != null)
                modifiedMass += mod.getMassChange();
        }
        return modifiedMass;
    }


    public String getModifiedSequence() {
        StringBuilder sb = new StringBuilder();
        String sequence = getSequence();
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            sb.append(c);
            PeptideModification mod = m_SequenceModifications[i];
            if (mod != null)
                sb.append("[" + mod.getMassStepChange() + "]");


        }
        return sb.toString();
    }


    /**
     * true if there is at least one modification
     *
     * @return
     */
    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public String getModificationString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m_SequenceModifications.length; i++) {
            PeptideModification mod = m_SequenceModifications[i];
            if (mod != null) {
                if (sb.length() > 0)
                    sb.append(",");
                sb.append(Integer.toString(i) + ":" + mod.toString());
            }
        }
        return sb.toString();
    }

    /**
     * return applied modifications
     *
     * @return
     */
    @Override
    public PeptideModification[] getModifications() {
        PeptideModification[] ret = new PeptideModification[m_SequenceModifications.length];
        System.arraycopy(m_SequenceModifications, 0, ret, 0, m_SequenceModifications.length);
        return ret;
    }

    @Override
    public int compareTo(IPolypeptide o) {
        final int i = super.compareTo(o);
        if (i != 0)
            return i;
        if (o instanceof IModifiedPeptide) {
            IModifiedPeptide o1 = (IModifiedPeptide) o;
            return getModifiedSequence().compareTo(((IModifiedPeptide) o).getModificationString());
        }
        return -1;

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        final ModifiedPolypeptide that = (ModifiedPolypeptide) o;

        if (!Arrays.equals(m_SequenceModifications, that.m_SequenceModifications)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (m_SequenceModifications != null ? Arrays.hashCode(m_SequenceModifications) : 0);
        return result;
    }
}
