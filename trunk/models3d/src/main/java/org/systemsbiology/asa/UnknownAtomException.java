package org.systemsbiology.asa;

/**
 * org.systemsbiology.asa.UnknownAtomException
 * User: steven
 * Date: 7/12/12
 */
public class UnknownAtomException extends RuntimeException {
    public static final UnknownAtomException[] EMPTY_ARRAY = {};

    public UnknownAtomException() {
        super("Unnknown Element");
    }

    public UnknownAtomException(String s) {
        super("Unnknown Element " + s);
    }
}