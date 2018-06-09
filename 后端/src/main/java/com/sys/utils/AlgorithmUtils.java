package com.sys.utils;

import java.security.MessageDigest;

/**
 * 算法工具类
 * @author sys
 */
public class AlgorithmUtils {

    /**
     * MD5加密
     * @param source
     * @return
     */
    public static String md5(String source) {
        String des = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(source.getBytes());
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                byte b = result[i];
                buf.append(String.format("%02X", b));
            }
            des = buf.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5 failure");
        }
        return des;
    }

    public static String pwdEncry(String pwd) {

        return md5(new StringBuffer(pwd).append("pwd").toString());
    }

  /*  public static void main(String[] args) {
        System.out.println(pwdEncry("admin"));
        0a14de5a76e5e14758b04c209f266726
    }*/
}
