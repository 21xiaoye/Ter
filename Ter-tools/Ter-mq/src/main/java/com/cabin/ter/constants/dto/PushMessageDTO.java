package com.cabin.ter.constants.dto;

import com.cabin.ter.constants.vo.response.WSBaseResp;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 推送给用户的消息对象
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-08-12
 */
@Data
public class PushMessageDTO extends MQBaseMessage implements Serializable {
    /**
     * 推送的ws消息
     */
    private WSBaseResp<?> wsBaseMsg;
    /**
     * 推送的uid
     */
    private List<Long> uidList;

    private Integer pushType;
}
