package com.cabin.ter.constants.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoye
 * @date Created in 2024-05-22 20:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OssReq {
    @Schema(description = "文件存储路径")
    private String filePath;

    @NotBlank(message = "文件名不能为空")
    @Schema(description = "文件名")
    private String fileName;

    @NotBlank(message = "用户uid不能为空")
    @Schema(description = "请求的uid")
    private Long uid;

    @Schema(description = "自动生成地址")
    @Builder.Default
    private boolean autoPath = false;
}
