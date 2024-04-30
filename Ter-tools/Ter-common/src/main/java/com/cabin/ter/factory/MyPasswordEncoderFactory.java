package com.cabin.ter.factory;


import com.cabin.ter.constants.enums.EncryptionEnum;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 *     加密工厂
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-30 12:35
 */
public class MyPasswordEncoderFactory {
    private static final String DEFAULT_ENCRYPTION = "scrypt";

    private static final Map<String, PasswordEncoder>  idToPasswordEncoder = new HashMap<>();
    private EncryptionEnum encryptionEnum;
    public MyPasswordEncoderFactory() {
    }

    public  String encode(EncryptionEnum encryptionEnum,CharSequence charSequence) {
        this.encryptionEnum = encryptionEnum;
        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(this.encryptionEnum.getMessage(), idToPasswordEncoder);
        String encode = delegatingPasswordEncoder.encode(charSequence);
        return encode;
    }
    public static PasswordEncoder createDelegatingPasswordEncoder() {
        idToPasswordEncoder.put(EncryptionEnum.MD5.getMessage(), new MessageDigestPasswordEncoder(EncryptionEnum.MD5.getMessage()));
        idToPasswordEncoder.put(EncryptionEnum.SHA256.getMessage(), new MessageDigestPasswordEncoder(EncryptionEnum.SHA256.getMessage()));
        idToPasswordEncoder.put(EncryptionEnum.SCRYPT.getMessage(),SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());


        return new DelegatingPasswordEncoder(DEFAULT_ENCRYPTION, idToPasswordEncoder);
    }

    private static class CreateFactorySingleton{
        private static MyPasswordEncoderFactory factory = new MyPasswordEncoderFactory();
    }

    public static MyPasswordEncoderFactory getInstance(){
        return CreateFactorySingleton.factory;
    }
}
