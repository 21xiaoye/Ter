package com.cabin.ter.constants.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     前端请求体
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 20:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "WsReqMsg",description = "前端请求websocket服务数据")
public class WsReqMsg {
    /**
     * 请求类型
     */
    @Schema(name = "type",description = "请求类型")
    private String type;
    /**
     * 请求数据
     */
    @Schema(name = "data",description = "请求数据")
    private String data;
}
