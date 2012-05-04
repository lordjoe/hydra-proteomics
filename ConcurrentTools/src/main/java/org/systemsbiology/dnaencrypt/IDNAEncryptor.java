package org.systemsbiology.dnaencrypt;

/**
 * org.systemsbiology.dnaencrypt.IDNAEncryptor
 * User: steven
 * Date: Jun 1, 2010
 */
public interface IDNAEncryptor {
    public static final IDNAEncryptor[] EMPTY_ARRAY = {};

    /**
     * This is an Identity Encryptor
     */
    public static IDNAEncryptor NULL_DECRYPTOR = new NullDecryptor();

    /**
     * map a string assumed to be largely ACGT into an encrypted version
     * @param s  !null input
     * @return !null output - this will be of the same length as the input
     */
    public String encrypt(String s);

    /**
     * map an encrypted string assumed to be largely ACGT into an decrypted version
     * NOTE for XOR versions of the algorithm this may repeat the operation
     * @param s  !null input
     * @return !null output - this will be of the same length as the input
     */
    public String decrypt(String s);

    /**
     * Identity encryptor - this does nothing
     */
    public static class NullDecryptor implements IDNAEncryptor {

        private NullDecryptor() {}
        /**
         * map a string assumed to be largely ACGT into an encrypted version
         *
         * @param s !null input
         * @return !null output - this will be of the same length as the input
         */
        @Override
        public String encrypt(final String s) {
            return s;
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
            return s;
        }
    }

}
