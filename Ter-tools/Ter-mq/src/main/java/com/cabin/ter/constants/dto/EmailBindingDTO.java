package com.cabin.ter.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *     通知用户绑定邮箱
 *
 *     只有微信扫码用户且在此之前未进行注册操作，则需要绑定邮箱完成注册
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-20 10:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailBindingDTO extends MQBaseMessage implements Serializable {
    /**
     * 推送的uid
     */
    private Integer code;
    /**
     * 用户 openId
     */
    private String openId;
}
