package com.cabin.ter.constants.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author xiaoye
 * @data Created in 2024-05-27 10:26
 */
@Data
@Builder
public class RequestInfoDTO {
    private Long uid;
    private String ip;
}
