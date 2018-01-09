package com.wjc.parttime.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import android.util.Base64;




/**
 * Created by huyh on 2017/6/7 0007.
 */
public class AESCoder {

    private final static String CHARSET = "UTF-8";

    private final static String algorithm = "AES";//AES加密

    private final static String seed = "parttime";//密钥种子

    private final static String ECB = "AES/ECB/PKCS5Padding";

    private final static String SEED_ECB = "parttimeparttime";

    /**根据密钥种子进行128位加密
     * @param content 要加密的内容
     * @param seed 密钥种子
     * @return
     */
    public static byte[] encrypt(String content, String seed) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(algorithm);// 创建AES的Key生产者
            if(seed == null) seed = AESCoder.seed;
            kgen.init(128, new SecureRandom(seed.getBytes()));// 利用用户密码作为随机数初始化出
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, algorithm);// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器
            byte[] byteContent = content.getBytes(CHARSET);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**使用默认的密钥种子进行128位加密
     * @param content 要加密的内容
     * @return
     */
    public static byte[] encrypt(String content){
        return encrypt(content, seed);
    }

    /**根据密钥种子进行128位解密
     * @param content 加密过的内容
     * @param seed 密钥种子
     * @return
     */
    public static byte[] decrypt(byte[] content, String seed) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(algorithm);// 创建AES的Key生产者
            if(seed == null) seed = AESCoder.seed;
            kgen.init(128, new SecureRandom(seed.getBytes()));// 利用用户密码作为随机数初始化出
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, algorithm);// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance(algorithm);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return result; // 明文
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**使用默认的密钥种子进行128位解密
     * @param content 加密过的内容
     * @return
     */
    public static byte[] decrypt(byte[] content) {
        return decrypt(content, seed);
    }

    /**二进制转为16进制
     * @param src
     * @return
     */
    public static String bytesToHexStr(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            String hv = Integer.toHexString(src[i] & 0xFF);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] hexStrToBytes(String hexStr) {
        if (hexStr == null || "".equals(hexStr)) {
            return null;
        }
        hexStr = hexStr.toUpperCase();
        int length = hexStr.length() / 2;
        char[] hexChars = hexStr.toCharArray();
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            result[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]) & 0xff);
        }
        return result;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**使用默认的ECB 的key 进行加密
     * @param content
     * @return
     */
    public static String encryptAES_ECB(String content){
        return encryptAES_ECB(content, SEED_ECB);
    }

    /**使用key 进行加密
     * @param content
     * @param key
     * @return
     */
    public static String encryptAES_ECB(String content, String key){

        byte[] encryptedBytes = new byte[0];
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
            kgen.init(128);
            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, algorithm);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance(ECB);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedBytes = cipher.doFinal(content.getBytes(CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 同样对加密后数据进行 base64 编码
       // String encodedString = Base64.encodeToString("whoislcj".getBytes(), Base64.DEFAULT);
        //return (new sun.misc.BASE64Encoder()).encode( s.getBytes() );
     String encoder= Base64.encodeToString(encryptedBytes,Base64.NO_WRAP);
        return encoder;
       /* Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(encryptedBytes);*/
    }

    /**使用默认ECB的key 进行解密
     * @param content
     * @return
     */
    /*public static String decryptAES_ECB(String content) {
        return decryptAES_ECB(content, SEED_ECB);
    }*/

    /**使用key 进行解密
     * @param content
     * @param key
     * @return
     */
  /*  public static String decryptAES_ECB(String content, String key) {
        byte[] result = new byte[0];
        String str = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
            kgen.init(128);
            Base64.Decoder decoder = Base64.getDecoder();//base64 解码
            byte[] encryptedBytes = decoder.decode(content);
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, algorithm);
            Cipher cipher = Cipher.getInstance(ECB);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            result = cipher.doFinal(encryptedBytes);
            str = new String(result, CHARSET);
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println("解密失败");
        } finally {
            return str;
        }
    }*/
}
