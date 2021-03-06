package org.systemsbiology.jmol;

/**
 * org.systemsbiology.jmol.ExperimentalType
 * User: Steve
 * Date: 8/6/12
 */
public enum ExperimentalType {
    X_RAY_DIFFRACTION,
    FIBER_DIFFRACTION,
    NEUTRON_DIFFRACTION,
    ELECTRON_CRYSTALLOGRAPHY,
    ELECTRON_MICROSCOPY,
    SOLID_STATE_NMR,
    SOLUTION_NMR,
    THEORETICAL_MODEL,
    SOLUTION_SCATTERING;
     public static final ExperimentalType[] EMPTY_ARRAY = {};

    public static final String[] TYPES =
            {
                    "X-RAY DIFFRACTION",
                    "FIBER DIFFRACTION",
                    "NEUTRON DIFFRACTION",
                    "ELECTRON  CRYSTALLOGRAPHY",
                    "ELECTRON  MICROSCOPY",
                    "SOLID-STATE  NMR ",
                    "SOLUTION  NMR ",
                    "THEORETICAL MODEL",
                     "SOLUTION  SCATTERING"
            };

    public static ExperimentalType fromString(String s) {
        s = s.replace(" ", "_");
        s = s.replace("-", "_");
        try {
            return valueOf(s);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(e);

        }

    }

}
