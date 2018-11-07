package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author yifan
 * @date 2018/7/19 下午6:00
 */
public class MD5Util {

    /**
     * 明文字符串md5
     * @param src
     * @return
     */
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass (String inputPass) {   // 第一次明文密码md5
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass (String formPass,String salt) {   // salt 随机的，第二次md5
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass (String formPass,String saltDB) {
        return formPassToDBPass(inputPassToFormPass(formPass),saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));  // d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(inputPassToDbPass("123456","abcdef"));
    }
}
