package org.systemsbiology.xtandem;

import java.util.*;

/**
 * org.systemsbiology.xtandem.FastaAminoAcid
 *
 * @author Steve Lewis
 * @date Jan 7, 2011
 */
public enum FastaAminoAcid {
    A("alanine"),
    B("aspartate or asparagine"),
    C("cystine"),
    D("aspartate"),
    E("glutamate"),
    F("phenylalanine"),
    G("glycine"),
    H("histidine"),
    I("isoleucine"),
    K("lysine"),
    L("leucine"),
    M("methionine"),
    N("asparagine"),
    P("proline"),
    Q("glutamine"),
    R("arginine"),
    S("serine"),
    T("threonine"),
    V("valine"),
    W("tryptophan"),
    Y("tyrosine"),
    Z("glutamate or glutamine"),
    X("any"),
    UNKNOWN("unknown")

    ;


    /**
     * convert the first character of a string to an anminoacid - bad values are null
     *
     * @param in !null value
     * @return 0.. n - 1
     */
    public static FastaAminoAcid asAminoAcidOrNull(String s) {
        if (s == null)
            return null;
        if (s.trim().length() != 1)
            return null;
        char in = s.charAt(0);
        return asAminoAcidOrNull(in);
    }

    /**
     * convert the first character of a string to an anminoacid - bad values are null
     *
     * @param in !null value
     * @return 0.. n - 1
     */
    public static FastaAminoAcid[] asAminoAcids(String s) {
        List<FastaAminoAcid> holder = new ArrayList<FastaAminoAcid>();
        for(int i = 0; i < s.length(); i++) {
             holder.add(FastaAminoAcid.fromChar(s.charAt(i)));
        }
        FastaAminoAcid[] ret = new FastaAminoAcid[holder.size()];
        holder.toArray(ret);
        return ret;
    }

    /**
     * convert the first character of a string to an anminoacid - bad values are null
     *
     * @param in a character
     * @return possibly null animoacid
     */
    public static FastaAminoAcid asAminoAcidOrNull(char in) {
        switch (Character.toUpperCase(in)) {
            case 'A':
                return A;
            case 'C':
                return C;
            case 'D': //("aspartate"),
                return D;
            case 'E':  //"glutamate"),
                return E;
            case 'F':  //"phenylalanine"),
                return F;
            case 'G':  //"glycine"),
                return G;
            case 'H':  //"histidine"),
                return H;
            case 'I':  //"isoleucine"),
                return I;
            case 'K':  //"lysine"),
                return K;
            case 'L':  //"leucine"),
                return L;
            case 'M':  //"methionine"),
                return M;
            case 'N':  //"asparagine"),
                return N;
            case 'P':  //"proline"),
                return P;
            case 'Q':  //"glutamine"),
                return Q;
            case 'R':  //"arginine"),
                return R;
            case 'S':  //"serine"),
                return S;
            case 'T':  //"threonine"),
                return T;
            case 'V':  //"valine"),
                return V;
            case 'W':  //"tryptophan"),
                return W;
            case 'Y':  //"tyrosine"),
                return Y;
            case 'Z':  //"glutamate or glutamine"),
                return Z;
            case 'B':  // "aspartate or asparagine"
                return B;
            default:
                return null;
        }

    }

    /**
     * convert to an index
     *
     * @param in !null value
     * @return 0.. n - 1
     */
    public static FastaAminoAcid fromAbbreviation(String in) {
        char c = in.charAt(0);
        switch (c) {
            case 'A':
                if ("ALA".equalsIgnoreCase(in))
                    return A;
                if ("ARG".equalsIgnoreCase(in))
                    return R;
                if ("ASP".equalsIgnoreCase(in))
                     return D;
                if ("ASN".equalsIgnoreCase(in))
                     return N;
                return UNKNOWN;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
            case 'C':
                if ("CYS".equalsIgnoreCase(in))
                    return C;
                return UNKNOWN;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
             case 'G':  //"glycine"),
                if ("GLY".equalsIgnoreCase(in))
                    return G;
                if ("GLU".equalsIgnoreCase(in))
                    return E;
                if ("GLN".equalsIgnoreCase(in))
                    return Q;
                 return UNKNOWN;
               //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
             case 'H':  //"histidine"),
                if ("HIS".equalsIgnoreCase(in))
                    return H;
                return null;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
             case 'I':  //"isoleucine"),
                if ("ILE".equalsIgnoreCase(in))
                    return I;
                throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
            case 'L':  //"leucine"),
                if ("LYS".equalsIgnoreCase(in))
                    return K;
                if ("LEU".equalsIgnoreCase(in))
                    return L;
                return UNKNOWN;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
            case 'M':  //"methionine"),
                if ("MET".equalsIgnoreCase(in))
                    return M;
                return UNKNOWN;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
             case 'P':  //"proline"),
                if ("PRO".equalsIgnoreCase(in))
                    return P;
                if ("PHE".equalsIgnoreCase(in))
                    return F;
                return UNKNOWN;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
            case 'S':  //"serine"),
                if ("SER".equalsIgnoreCase(in))
                    return S;
                return null;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
            case 'T':  //"threonine"),
                if ("THR".equalsIgnoreCase(in))
                    return T;
                if ("TRP".equalsIgnoreCase(in))
                    return W;
                if ("TYR".equalsIgnoreCase(in))
                    return Y;
                return null;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
            case 'V':  //"valine"),
                if ("VAL".equalsIgnoreCase(in))
                    return V;
                return UNKNOWN;
              //  throw new IllegalArgumentException("Bad amino acid abbreviation " + in);
            default:
                return UNKNOWN;

        }

    //    throw new IllegalStateException("No Amino acid for string " + in);
    }

    /**
     * convert to an index
     *
     * @param in !null value
     * @return 0.. n - 1
     */
    public static FastaAminoAcid fromChar(char in) {
        switch (in) {
            case 'A':
                return A;
            case 'C':
                return C;
            case 'D': //("aspartate"),
                return D;
            case 'E':  //"glutamate"),
                return E;
            case 'F':  //"phenylalanine"),
                return F;
            case 'G':  //"glycine"),
                return G;
            case 'H':  //"histidine"),
                return H;
            case 'I':  //"isoleucine"),
                return I;
            case 'K':  //"lysine"),
                return K;
            case 'L':  //"leucine"),
                return L;
            case 'M':  //"methionine"),
                return M;
            case 'N':  //"asparagine"),
                return N;
            case 'P':  //"proline"),
                return P;
            case 'Q':  //"glutamine"),
                return Q;
            case 'R':  //"arginine"),
                return R;
            case 'S':  //"serine"),
                return S;
            case 'T':  //"threonine"),
                return T;
            case 'V':  //"valine"),
                return V;
            case 'W':  //"tryptophan"),
                return W;
            case 'Y':  //"tyrosine"),
                return Y;
            case 'Z':  //"glutamate or glutamine"),
                return Z;
            case 'B':  // "aspartate or asparagine"
                return B;
            case 'X':  // "aspartate or asparagine"
                return X;
        }
        if (!Character.isLetter(in))
            throw new IllegalStateException("No Amino acid for char " + in);

        if (!Character.isUpperCase(in))
            return fromChar(Character.toUpperCase(in));

        throw new IllegalStateException("No Amino acid for char " + in);
    }


    /**
     * if true in represents a unique amino acid
     *
     * @param in as above
     * @return as above
     */
    public static boolean representsUnigueAminoacid(char in) {
        switch (Character.toUpperCase(in)) {
            case 'A':
            case 'C':
            case 'D': //("aspartate"),
            case 'E':  //"glutamate"),
            case 'F':  //"phenylalanine"),
            case 'G':  //"glycine"),
            case 'H':  //"histidine"),
            case 'I':  //"isoleucine"),
            case 'K':  //"lysine"),
            case 'L':  //"leucine"),
            case 'M':  //"methionine"),
            case 'N':  //"asparagine"),
            case 'P':  //"proline"),
            case 'Q':  //"glutamine"),
            case 'R':  //"arginine"),
            case 'S':  //"serine"),
            case 'T':  //"threonine"),
            case 'V':  //"valine"),
            case 'W':  //"tryptophan"),
            case 'Y':  //"tyrosine"),
                return true;
            default:
                return false;
        }
    }


    /**
     * convert to an index
     *
     * @param in !null value
     * @return 0.. n - 1
     */
    public static int asIndex(FastaAminoAcid in) {
        switch (in) {
            case A:
                return 0;
            case C:
                return 1;
            case D: //("aspartate"),
                return 2;
            case E:  //"glutamate"),
                return 3;
            case F:  //"phenylalanine"),
                return 4;
            case G:  //"glycine"),
                return 5;
            case H:  //"histidine"),
                return 6;
            case I:  //"isoleucine"),
                return 7;
            case K:  //"lysine"),
                return 8;
            case L:  //"leucine"),
                return 9;
            case M:  //"methionine"),
                return 10;
            case N:  //"asparagine"),
                return 11;
            case P:  //"proline"),
                return 12;
            case Q:  //"glutamine"),
                return 13;
            case R:  //"arginine"),
                return 14;
            case S:  //"serine"),
                return 15;
            case T:  //"threonine"),
                return 16;
            case V:  //"valine"),
                return 17;
            case W:  //"tryptophan"),
                return 18;
            case Y:  //"tyrosine"),
                return 19;
            case Z:  //"glutamate or glutamine"),
                return 20;
            case B:  // "aspartate or asparagine"
                return 22;
            case X:  // "aspartate or asparagine"
                return -1;   // todo id this right
        }
        throw new IllegalStateException("Never get here");
    }


    public static FastaAminoAcid[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = FastaAminoAcid.class;

    private final String m_Name;

    FastaAminoAcid(String pName) {
        m_Name = pName;
    }

    public String getName() {
        return m_Name;
    }



    /**
     * three letter abbreviation
     *
     * @return
     */
    public String getAbbreviation() {
        switch (this) {
            case I:
                return "ILE";
            case N:
                return "ASN";
            case Q:
                return "GLN";
            case W:
                return "TRP";
            case B:
                return "ASX";
            case Z:
                return "GLX";

        }
        // all others are first three letters
        return getName().substring(0, 3).toUpperCase();
    }

    @Override
    public String toString() {
        if(UNKNOWN == this)
            return "X";
        return super.toString();
    }
}
