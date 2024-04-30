package com.cabin.ter.common.security;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class MyPasswordEncoder {
    private static final String SHA256 = "SHA-256";
    private static final String SHA512 = "SHA-512";


    /**
     * 生成盐值
     * @return String
     */
    public   String generateSalt(){
        return IdUtil.fastUUID();
    }
    /**
     * 哈希加密
     * @param strText
     * @param strType
     * @return
     */
    private String SHA(final String strText, final String strType){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(strType);

            messageDigest.update(strText.getBytes());
            byte[] digest = messageDigest.digest();

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xff & digest[i]);
                if(hex.length()==1){
                    stringBuilder.append('0');
                }
                stringBuilder.append(hex);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateStrText(String passwd, String salt){
        return passwd+salt;
    }
    public String passwdEncryption(String passwd,String salt){
        return SHA(generateStrText(passwd,salt),SHA256);
    }
}
