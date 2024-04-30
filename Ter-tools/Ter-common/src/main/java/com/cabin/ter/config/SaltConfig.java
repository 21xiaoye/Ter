package com.cabin.ter.config;



import cn.hutool.core.util.IdUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * <p>
 *     用户密码加密
 * </p>
 * @author xiaoye
 * @date Created in 2024-04-23 11:35
 */
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Builder
@Data
public class SaltConfig{
    private static final String SHA256 = "SHA-256";
    private static final String SHA512 = "SHA-512";

    private String passwd;
    private String salt;
    /**
     * 生成盐值
     * @return String
     */
    private  String generateSalt(){
        return IdUtil.fastUUID();
    }

    /**
     * 盐值和密码进行拼接
     */
    private String generateStrText(String passwd,String salt){
        return strText(passwd,salt);
    }

    private String strText(String passwd,String salt){
        return passwd+salt;
    }

    private  String SHA256(final String strText){
        return SHA(strText,SHA256);
    }
    private  String SHA512(final String strText){
        return SHA(strText,SHA512);
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


    public String passwdEncryption(String passwd){
        String salt = generateSalt();
        return SHA256(generateStrText(passwd,salt));
    }
    public SaltConfig passwdDecryption(String passwd, String salt){
        return new SaltConfig(SHA256(generateStrText(passwd,salt)), salt);
    }
}
