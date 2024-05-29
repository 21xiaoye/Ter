package com.cabin.ter.chat.domain.msg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "图片消息参数")
public class ImgMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(name = "width",description = "宽度（像素）")
    @NotNull
    private Integer width;

    @Schema(name = "height", description = "高度（像素）")
    @NotNull
    private Integer height;

}


