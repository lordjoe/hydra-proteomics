package org.systemsbiology.xtandem.peptide;

import org.systemsbiology.xtandem.*;
import org.systemsbiology.xtandem.sax.*;

import java.util.*;

/**
 * org.systemsbiology.xtandem.peptide.PeptideModification
 * information about a potential peptide modification
 * User: steven
 * Date: 6/30/11
 */
public class PeptideModification implements Comparable<PeptideModification> {
    public static final PeptideModification[] EMPTY_ARRAY = {};

    private static final Map<String, PeptideModification> gAsString = new HashMap<String, PeptideModification>();

    public static PeptideModification[] fromListString(String mods, PeptideModificationRestriction restrict, boolean fixed) {
        List<PeptideModification> holder = new ArrayList<PeptideModification>();
        String[] items = mods.split(",");
        for (int i = 0; i < items.length; i++) {
            holder.add(fromString(items[i], restrict, fixed));
        }
        PeptideModification[] ret = new PeptideModification[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    public static PeptideModification fromString(String s, PeptideModificationRestriction restrict, boolean fixed) {
        PeptideModification ret = gAsString.get(s);
        if (ret == null) {
            ret = new PeptideModification(s, restrict, fixed);
            gAsString.put(s, ret);
        }
        return ret;
    }

    public static PeptideModification[] fromModificationString(String sequence, String mods) {
        PeptideModification[] ret = new PeptideModification[sequence.length()];
        String[] items = mods.split(",");
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            String[] elements = item.split(":");
            int index = Integer.parseInt(elements[0]);
            PeptideModification pm = PeptideModification.fromString(elements[1], PeptideModificationRestriction.Global, false);
            ret[index] = pm;
        }

        return ret;
    }


    /**
     * XTandem codes these potential modifications of the N Terminal
     */
    private static final Map<FastaAminoAcid, PeptideModification[]> gNTermalModifications =
            new HashMap<FastaAminoAcid, PeptideModification[]>();
    /**
     * XTandem cods these potential modifications of the N Terminal
     */
    private static final Map<FastaAminoAcid, PeptideModification[]> gCTermalModifications =
            new HashMap<FastaAminoAcid, PeptideModification[]>();

    public static PeptideModification[] getNTerminalModifications(FastaAminoAcid aa) {
        guaranteeHardCodedModifications();
        PeptideModification[] pm = gNTermalModifications.get(aa);
        if (pm == null)  {
             pm = gNTermalModifications.get(FastaAminoAcid.X); // any
            if (pm == null)
                return PeptideModification.EMPTY_ARRAY;
        }
        return pm;
    }

    public static PeptideModification[] getCTerminalModifications(FastaAminoAcid aa) {
        guaranteeHardCodedModifications();
        PeptideModification[] pm = gCTermalModifications.get(aa);
        if (pm == null)  {
             pm = gCTermalModifications.get(FastaAminoAcid.X); // any
            if (pm == null)
                return PeptideModification.EMPTY_ARRAY;
        }
        return pm;
    }

    public static final double CYSTEIN_MODIFICATION_MASS = -57.02146;

    public static final PeptideModification CYSTEIN_MODIFICATION = new PeptideModification(FastaAminoAcid.C, CYSTEIN_MODIFICATION_MASS,
            PeptideModificationRestriction.Global, true);

    public static void guaranteeHardCodedModifications() {
        // new PeptideModification(FastaAminoAcid.C, CYSTEIN_MODIFICATION_MASS,
        //        PeptideModificationRestriction.Global, true);
        if (gNTermalModifications.isEmpty()) {
            new PeptideModification(FastaAminoAcid.Q, -MassCalculator.getDefaultCalculator().calcMass("NH3"),
                    PeptideModificationRestriction.NTerminal, false);
            new PeptideModification(FastaAminoAcid.C, -MassCalculator.getDefaultCalculator().calcMass("NH3"),
                    PeptideModificationRestriction.NTerminal, false);
            new PeptideModification(FastaAminoAcid.E, -MassCalculator.getDefaultCalculator().calcMass("H2O"),
                    PeptideModificationRestriction.NTerminal, false);
        }
    }


    /**
     * turm a modification string into modifications
     * 15.994915@M,8.014199@K,10.008269@R
     *
     * @param value !null string
     * @return !null array
     */
    public static PeptideModification[] parsePeptideModifications(String value) {
        return parsePeptideModifications(value, PeptideModificationRestriction.Global, false);
    }

    /**
     * turm a modification string into modifications
     * 15.994915@M,8.014199@K,10.008269@R
     *
     * @param value !null string
     * @return !null array
     */
    public static PeptideModification[] parsePeptideModifications(String value, PeptideModificationRestriction restriction, boolean fixed) {
        value = value.trim();
        if (value.length() == 0)
            return EMPTY_ARRAY;
        String[] items = value.split(",");
        PeptideModification[] ret = new PeptideModification[items.length];
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            ret[i] = new PeptideModification(items[i], restriction, fixed);
        }
        Arrays.sort(ret);
        return ret;
    }


    private final FastaAminoAcid m_AminoAcid;
    private final double m_MassChange;
    private final boolean m_Fixed;
    private final PeptideModificationRestriction m_Restriction;

    private PeptideModification(String value) {
        this(value, PeptideModificationRestriction.Global, false);
    }

    private PeptideModification(String value, PeptideModificationRestriction restriction, boolean fixed) {
        if (value.endsWith("[")) {
            restriction = PeptideModificationRestriction.NTerminal;
            value = value.substring(0, value.length() - 1);
        }
        if (value.endsWith("]")) {
            restriction = PeptideModificationRestriction.CTerminal;
            value = value.substring(0, value.length() - 1);
        }
        String[] items = value.split("@");
        if (items.length > 1) {
            m_AminoAcid = FastaAminoAcid.valueOf(items[1]);

        }
        else {
            if (restriction != PeptideModificationRestriction.NTerminal ||
                    restriction != PeptideModificationRestriction.CTerminal)
                throw new IllegalStateException("Any Animo Acid can only apply to terminal modifications ");
            m_AminoAcid = null;
        }
        m_MassChange = Double.parseDouble(items[0]);
        m_Restriction = restriction;
        m_Fixed = fixed;
    }

    private PeptideModification(final FastaAminoAcid pAminoAcid, final double pMassChange) {
        this(pAminoAcid, pMassChange, PeptideModificationRestriction.Global, false);
    }

    private PeptideModification(final FastaAminoAcid pAminoAcid, final double pMassChange,
                                PeptideModificationRestriction restriction, boolean fixed) {
        m_AminoAcid = pAminoAcid;
        m_MassChange = pMassChange;
        m_Restriction = restriction;
        switch (m_Restriction) {
            case CTerminal:
                if (pAminoAcid == null)
                    XTandemUtilities.insertIntoArrayMap(gCTermalModifications, FastaAminoAcid.X, this);   // any
                else
                    XTandemUtilities.insertIntoArrayMap(gCTermalModifications, pAminoAcid, this);
                break;
            case NTerminal:
                  if (pAminoAcid == null)
                     XTandemUtilities.insertIntoArrayMap(gNTermalModifications, FastaAminoAcid.X, this);   // any
                 else
                     XTandemUtilities.insertIntoArrayMap(gNTermalModifications, pAminoAcid, this);
                break;
            default:
                break;
        }
        m_Fixed = fixed;
    }

    /**
     * if true this is always applied
     *
     * @return
     */
    public boolean isFixed() {
        return m_Fixed;
    }

    public String applyTo() {
        return getAminoAcid().toString();
    }

    /**
     * null says any
     *
     * @return
     */
    public FastaAminoAcid getAminoAcid() {
        return m_AminoAcid;
    }

    public double getMassChange() {
        return m_MassChange;
    }

    public double getPepideMass() {
        return getMassChange() + MassCalculator.getDefaultCalculator().getAminoAcidMass(getAminoAcid().getAbbreviation().charAt(0)) ;
    }

    public PeptideModificationRestriction getRestriction() {
        return m_Restriction;
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        FastaAminoAcid aminoAcid = getAminoAcid();
        String value = "";
        if (aminoAcid != null)
            value = aminoAcid.toString();
        return Double.toString(getMassStepChange()) + "@" + value + getRestriction().getRestrictionString();
    }

    public double getMassStepChange() {
        int value = (int) (1000 * (getMassChange() + 0.0005));
        return value / 1000.0;
    }

    @Override
    public int compareTo(final PeptideModification o) {
        if (this == o)
            return 0;
        if (getAminoAcid() != null) {
            int ret = getAminoAcid().compareTo(o.getAminoAcid());
            if (ret != 0)
                return ret;

        }
        else {
            if (o.getAminoAcid() != null)
                return 1;
        }
        int ms1 = getRoundedMassChange();
        int ms2 = o.getRoundedMassChange();
        if (ms1 == ms2)
            return 0;
        return ms1 < ms2 ? -1 : 1;
    }

    protected int getRoundedMassChange()
    {
        return (int)( 500 * (getMassChange() + 0.5));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PeptideModification that = (PeptideModification) o;
         return compareTo(that)== 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = m_AminoAcid != null ? m_AminoAcid.hashCode() : 0;
        temp = (long)getRoundedMassChange();
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public void serializeAsString(IXMLAppender ap) {
        ap.openTag("modification");
        ap.appendAttribute("aminoacid", getAminoAcid());
        ap.appendAttribute("massChange", String.format("%10.4f", getMassChange()).trim());
        ap.appendAttribute("restriction", getRestriction());
        ap.closeTag("modification");
    }


}
