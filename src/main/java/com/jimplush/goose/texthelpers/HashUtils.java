package com.jimplush.goose.texthelpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils
{
    /**
     * Return a string of 40 lower case hex characters.
     * 
     * @param input
     * @return a string of 40 hex characters
     */
    public static String sha1(String input)
    {
        String hexHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(input.getBytes());
            byte[] output = md.digest();
            hexHash = bytesToLowerCaseHex(output);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
        return hexHash;
    }

    /**
     * Return a string of 32 lower case hex characters.
     * 
     * @param input
     * @return a string of 32 hex characters
     */
    public static String md5(String input)
    {
        String hexHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] output = md.digest();
            hexHash = bytesToLowerCaseHex(output);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
        return hexHash;
    }

    @SuppressWarnings("unused")
    private static String bytesToUpperCaseHex(byte[] b)
    {
        char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }

    private static String bytesToLowerCaseHex(byte[] data)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

}