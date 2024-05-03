package com.cabin.ter.common.constants.vo.response;

import lombok.Data;

/**
 * <p>
 *     响应给前端的数据
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03
 */
@Data
public class WSBaseResp<T> {
    /**
     * ws推送给前端的消息
     *
     * @see com.cabin.ter.common.constants.enums.WSRespTypeEnum
     */
    private Integer type;
    private T data;
}
