package com.lwl.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * @author lwl
 * @date 2021/7/26 10:32
 * 密码工具类
 */
public class PasswordUtils {
    /**
     * 加密
     * @param slat
     * @param password
     * @return
     */
    public static String encrypt(String slat, String password) {
        //生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), slat.getBytes()).getEncoded();
        //加密
        AES aes = SecureUtil.aes(key);
        return aes.encryptBase64(password);
    }

    /**
     * 解密
     * @param slat
     * @param password
     * @return
     */
    public static String decrypt(String slat, String password) {
        //生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), slat.getBytes()).getEncoded();
        //解密
        AES aes = SecureUtil.aes(key);
        return aes.decryptStr(password);
    }

    public static void main(String[] args) {
        //必须16位
        String slat = RandomUtil.randomString(16);
        String encrypt = encrypt(slat, "lwl");
        String decrypt = decrypt(slat, encrypt);
        System.out.println(slat);
        System.out.println(encrypt);
        System.out.println(decrypt);
    }
}

