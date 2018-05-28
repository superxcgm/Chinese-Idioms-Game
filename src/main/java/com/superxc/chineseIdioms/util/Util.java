package com.superxc.chineseIdioms.util;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    private static final String STAR_ICON = "✪";
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

    public static String fileReadToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
    public static String convertStarCountToString(int count) {
        switch (count) {
            case 0:
                return "";
            case 1:
                return STAR_ICON;
            case 2:
                return STAR_ICON + STAR_ICON;
            case 3:
                return STAR_ICON + STAR_ICON + STAR_ICON;
        }
        return null;
    }
}
