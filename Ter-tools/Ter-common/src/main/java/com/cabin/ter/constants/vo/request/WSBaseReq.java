package com.cabin.ter.constants.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *     前端websocket请求体
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 15:39
 */

@Schema(name = "websocket请求体")
@Data
public class WSBaseReq {
    /**
    * 请求类型
    */
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}
