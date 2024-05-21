package com.cabin.ter.constants.enums;

import lombok.Getter;

/**
 * <p>
 *      加密枚举
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-30 12:52
 */
@Getter
public enum EncryptionEnum implements IStatus {

    MD5(10001,"MD5"),
    SHA256(10002,"SHA-256"),
    SCRYPT(10003,"scrypt");
    /**
     * 状态码
     */
    private Integer status;
    /**
     * 返回信息
     */
    private String message;

    EncryptionEnum(Integer status, String message){
        this.status=status;
        this.message=message;
    }
}
