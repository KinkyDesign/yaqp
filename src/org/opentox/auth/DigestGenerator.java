package org.opentox.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Factory for the generation of SHA and MD5 digests.
 * @author Sopasakis Pantelis
 */
public class DigestGenerator {

    /**
     * Set of Available Digest Algorithm.
     */
    public static class DigestAlgorithm {

        /**
         * Hash algorithms defined in the  FIPS PUB 180-2.
         * The output of this algorithm is a 160-bit digest.
         */
        public static String SHA_1 = "SHA-1";
        /**
         * Hash algorithms defined in the  FIPS PUB 180-2.
         * SHA-256 is a 256-bit hash function intended to provide 128 bits
         * of security against collision attacks
         */
        public static String SHA_256 = "SHA-256";
        /**
         * Hash algorithms defined in the  FIPS PUB 180-2.
         * A 384-bit hash may be obtained by truncating the SHA-512 output.
         */
        public static String SHA_384 = "SHA-384";
        /**
         * Hash algorithms defined in the  FIPS PUB 180-2.
         * SHA-512 is a 512-bit hash function intended to provide 256
         * bits of security.
         */
        public static String SHA_512 = "SHA-512";
        /**
         * The MD2 message digest algorithm as defined in  RFC 1319.
         * The output of this algorithm is a 128-bit (16 byte) digest.
         */
        public static String MD2 = "MD2";
        /**
         * The MD5 message digest algorithm as defined in  RFC 1321.
         * The output of this algorithm is a 128-bit (16 byte) digest.
         */
        public static String MD5 = "MD5";
    }

    /**
     * Gives the SHA or MD5 digest for a given string message. See
     * <a href="http://snippets.dzone.com/posts/show/3686">this snippet</a>
     * @param message Initial Message
     * @return The SHA or MD5 digest that correspongs to the given message.
     */
    public static String generateDigest(String message, String DigestAlgorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(DigestAlgorithm);
            md.update(message.getBytes(), 0, message.length());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
}
