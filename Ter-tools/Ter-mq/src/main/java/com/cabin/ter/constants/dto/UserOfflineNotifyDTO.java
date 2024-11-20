package com.cabin.ter.constants.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaoye
 * @Date Created in 2024/11/04 11:15
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserOfflineNotifyDTO extends MQBaseMessage{
    private Long userId;
    private Long offlineTime;
}
