package com.cabin.ter.chat.domain.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "视频消息参数")
public class VideoMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name = "thumbWidth",description = "缩略图宽度（像素）")
    @NotNull
    private Integer thumbWidth;

    @Schema(name = "thumbHeight",description = "缩略图高度（像素）")
    @NotNull
    private Integer thumbHeight;

    @Schema(name = "thumbSize",description = "缩略图大小（字节）")
    @NotNull
    private Long thumbSize;

    @Schema(name = "thumbUrl",description = "缩略图下载地址")
    @NotBlank
    private String thumbUrl;

}
