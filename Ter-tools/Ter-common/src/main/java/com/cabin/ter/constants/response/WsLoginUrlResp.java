package com.cabin.ter.constants.response;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *     请求二维码、openId返回
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 20:54
 */
@Data
@Builder
public class WsLoginUrlResp {
    private String loginUrl;

}
