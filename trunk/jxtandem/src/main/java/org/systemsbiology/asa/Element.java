package org.systemsbiology.asa;

/**
 * org.systemsbiology.asa.Element
 * User: Steve
 * Date: 7/12/12
 */
public enum Element {
    H(1.20),
    N(1.55),
    NA(2.27),
    CU(1.40),
    CL(1.75),
    C(1.70),
    O(1.52),
    I(1.98),
    P(1.80),
    B(1.85),
    BR(1.85),
    S(1.80),
    SE(1.90),
    F(1.47),
    FE(1.80),
    K(2.75),
    MN(1.73),
    MG(1.73),
    ZN(1.39),
    HG(1.8),
    XE(1.8),
    AU(1.8),
    LI(1.8),
    W(1.70),   // use this for all others
    ;
    // not used D,E,G,J,Q,R,T,U,V,W,Z
    public static final Element[] EMPTY_ARRAY = {};

    private final double m_Radius;

    private Element(double radius) {
        m_Radius = radius;
    }

    public double getRadius() {
        return m_Radius;
    }

    public static final Element UNKNOWN_ELEMENT = W;

    public static Element fromString(String s) {
        if ("UNK".equals(s))
            return UNKNOWN_ELEMENT;
        if (s.length() == 1) {
            try {
                return Element.valueOf(s);
            }
            catch (IllegalArgumentException e) {
                return UNKNOWN_ELEMENT;
            }
        }
        else {
            try {
                char c = s.charAt(0);

                switch (c) {
                    case 'D':
                    case 'E':
                    case 'G':
                    case 'J':
                    case 'Q':
                    case 'R':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                        return UNKNOWN_ELEMENT;

                    case 'C':   // CU CA
                    case 'M':  // MN MG
                    case 'Z':  // ZN
                    case 'B':   //
                    case 'S':  // SE
                    case 'N':
                    case 'L':   // LI
                        return Element.valueOf(s.substring(0, 2)); // elements have only one letter
                    default:
                        return Element.valueOf(s.substring(0, 1)); // elements have only one letter
                }
            }
            catch (IllegalArgumentException e) {
                try {
                    return Element.valueOf(s.substring(0, 1)); // elements have only one letter
                }
                catch (IllegalArgumentException e1) {
                    return UNKNOWN_ELEMENT;

                }

            }
          }
    }
}

