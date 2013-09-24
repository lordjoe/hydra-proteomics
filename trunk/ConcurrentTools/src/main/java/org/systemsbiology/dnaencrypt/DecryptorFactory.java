package org.systemsbiology.dnaencrypt;

import java.util.*;

/**
 * org.systemsbiology.dnaencrypt.DecryptorFactory
 * User: steven
 * Date: Jun 1, 2010
 */
@SuppressWarnings("UnusedDeclaration")
public class DecryptorFactory {
    public static final DecryptorFactory[] EMPTY_ARRAY = {};

    private static Map<String, IDNAEncryptor> m_SpecialKeys = new HashMap<String, IDNAEncryptor>();


    public static void addDNAEncryptor(String key, IDNAEncryptor added) {
        m_SpecialKeys.put(key, added);
    }


    public static IDNAEncryptor getDNAEncryptor(String key) {
        return m_SpecialKeys.get(key);
    }


}
