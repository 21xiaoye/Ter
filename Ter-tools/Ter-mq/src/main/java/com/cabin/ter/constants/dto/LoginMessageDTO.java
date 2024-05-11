package com.cabin.ter.constants.dto;

import com.cabin.ter.constants.participant.msg.MessageParticipant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *     将扫码登录返回信息推送给所有横向扩展的服务
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-11 11:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginMessageDTO extends MQBaseMessage implements Serializable  {

    private static final long serialVersionUID = 1L;

    private Long uid;
    private Integer code;
}
