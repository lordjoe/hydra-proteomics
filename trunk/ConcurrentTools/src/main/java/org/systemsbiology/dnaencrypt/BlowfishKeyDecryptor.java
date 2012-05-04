package org.systemsbiology.dnaencrypt;

import com.lordjoe.utilities.*;

import java.util.*;

/**
 * org.systemsbiology.dnaencrypt.BlowfishKeyDecryptor
 * A version of the decryptor where the key is presented as a String encrypted with
 * Blowfish and then binhexed
 * User: steven
 * Date: Jun 1, 2010
 */
public class BlowfishKeyDecryptor extends AbstractDNAEncryptor
{
    public static final BlowfishKeyDecryptor[] EMPTY_ARRAY = {};

    private static String BLOWSFISH_KEY = "The secret of life";
    public static final Random RND = new Random();
    /**
     * make a random key
     * @return
     */
    public static String makeKey()
    {
        byte[] values = new byte[Encrypt.CYPHER_LENGTH]; // 2 bytes per char
        for (int i = 0; i < values.length; i++) {
             RND.nextBytes(values);

        }
        return Base64.encode(values);
    }

    private static byte[] decryptDeclaredKey(String theKey) {
       byte[] bytes =  Base64.decode(theKey);
       byte[] ret = Encrypt.decryptBytes(bytes, BLOWSFISH_KEY);
        return ret;
    }

    public BlowfishKeyDecryptor(String pKey) {
        super(decryptDeclaredKey(pKey));
    }
}
