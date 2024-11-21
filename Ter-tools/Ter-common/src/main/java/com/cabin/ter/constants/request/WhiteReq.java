package com.cabin.ter.constants.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class WhiteReq {
    @NotNull
    @Size(min = 1, max = 50)
    @Schema(name = "friendList",description = "拉回白名单的成员id列表")
    private List<Long> friendList;
}
