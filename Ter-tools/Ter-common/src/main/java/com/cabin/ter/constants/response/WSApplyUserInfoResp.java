package com.cabin.ter.constants.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSApplyUserInfoResp {
    private Long userId;
    private String userName;
    private String userAvatar;
    private Integer sex;
    private Long applyId;
    private Long applyTime;
    private Integer applyStatus;
    private String applyMessage;
}
