package com.cabin.ter.constants.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaoye
 * @Date Created in 2024/11/04 11:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserOnlineNotifyDTO extends MQBaseMessage{
    private Long userId;
    private Long onlineTime;
}
