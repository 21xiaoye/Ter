package com.cabin.ter.constants.vo.response;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *     请求二维码返回
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 20:54
 */
@Data
@Builder
public class WsLoginUrl {
    private String loginUrl;
}
