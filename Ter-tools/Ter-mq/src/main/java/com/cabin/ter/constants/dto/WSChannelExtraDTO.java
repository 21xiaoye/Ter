package com.cabin.ter.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     用户 唯一标识
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-10 09:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSChannelExtraDTO {
    private Long uid;
}
