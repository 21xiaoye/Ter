package com.cabin.ter.constants.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdResp {
    @Schema(name = "id",description = "群聊id")
    private long id;

    public static IdResp id(Long id) {
        IdResp idRespVO = new IdResp();
        idRespVO.setId(id);
        return idRespVO;
    }
}