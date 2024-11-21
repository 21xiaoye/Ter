package com.cabin.ter.constants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "表情包参数")
public class EmojisMsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name = "url",description = "下载地址")
    @NotBlank
    private String url;
}


