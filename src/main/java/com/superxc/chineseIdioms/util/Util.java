package com.superxc.chineseIdioms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    /**
     * MD5 encryption with salt
     * @param plainText sources plain text
     * @return 32 lowercase letter result
     */
    public static String MD5(String plainText) {
        String str_src = plainText + AppConfigure.getProperty("APP_KEY");
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str_src.getBytes());
            byte b[] = md.digest();

            StringBuilder buf = new StringBuilder();
            for (byte ele : b) {
                int i = ele;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
