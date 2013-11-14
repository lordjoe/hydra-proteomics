package org.systemsbiology.dnaencrypt;

import java.lang.reflect.*;

/**
 * org.systemsbiology.dnaencrypt.AbstractDNAEncryptor
 * User: steven
 * Date: Jun 1, 2010
 */
public abstract class AbstractDNAEncryptor implements IDNAEncryptor {
    public static final AbstractDNAEncryptor[] EMPTY_ARRAY = {};


    // treat A = 0, C = 1, G = 10, T = 11 binary
    // xor 0
    private static final char[] DECRYPT0 = {'A', 'C', 'G', 'T'};
    // xor 1
    private static final char[] DECRYPT1 = {'C', 'A', 'T', 'G'};
    // xor 10
    private static final char[] DECRYPT2 = {'G', 'T', 'A', 'C'};
    // xor 11
    private static final char[] DECRYPT3 = {'T', 'G', 'C', 'A'};

    private static final char[][] DECRYPTORS = {DECRYPT0, DECRYPT1, DECRYPT2, DECRYPT3};

    private final byte[] m_Key;

    public AbstractDNAEncryptor(final byte[] pKey) {
        m_Key = new byte[pKey.length];
        System.arraycopy(pKey, 0, m_Key, 0, m_Key.length);

    }

    public byte[] getKey() {
        return m_Key;
    }

    /**
     * return 0..3 key item
     *
     * @param i
     * @return
     */
    protected int getKeyItem(int i) {
        final byte[] keys = getKey();
        return (keys[i % keys.length]) & 3;
    }

    /**
     * @param inp
     * @param key
     * @return
     */
    protected char encrypt(char inp, int key) {
        switch (inp) {
            case 'A':
                return DECRYPTORS[key][0];
            case 'C':
                return DECRYPTORS[key][1];
            case 'G':
                return DECRYPTORS[key][2];
            case 'T':
                return DECRYPTORS[key][3];

            // all non ACGT return the original character
            default:
                return inp;

        }
    }

    /**
     * map a string assumed to be largely ACGT into an encrypted version
     *
     * @param s !null input
     * @return !null output - this will be of the same length as the input
     */
    @Override
    public String encrypt(final String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            final int key = getKeyItem(i);
            sb.append(encrypt(c, key));
        }
        return sb.toString();
    }

    /**
     * map an encrypted string assumed to be largely ACGT into an decrypted version
     * NOTE for XOR versions of the algorithm this may repeat the operation
     *
     * @param s !null input
     * @return !null output - this will be of the same length as the input
     */
    @Override
    public String decrypt(final String s) {
        // XOR is reversable
        return encrypt(s);
    }
}
