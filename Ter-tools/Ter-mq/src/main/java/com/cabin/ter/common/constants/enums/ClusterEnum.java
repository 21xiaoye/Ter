package com.cabin.ter.common.constants.enums;

import com.cabin.ter.constants.enums.IStatus;
import lombok.Getter;

/**
 * <p>
 *     集群消息订阅发布主体配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 15:23
 */
@Getter
public enum ClusterEnum implements IStatus {
    REDIS_USER_MESSAGE_PUSH(8001,"redis-user-message-push");

    private Integer code;
    private String message;

    ClusterEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
