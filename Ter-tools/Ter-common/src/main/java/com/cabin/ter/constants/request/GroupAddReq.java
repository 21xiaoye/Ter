package com.cabin.ter.constants.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupAddReq {
    @NotNull
    @Size(min = 1, max = 50)
    @Schema(name = "memberInfoList",description = "邀请的群成员信息列表")
    private List<MemberInfo> memberInfoList;
    @Data
    public static class MemberInfo{
        /**
         * 群成员userId
         */
        @Schema(name = "userId", description = "邀请成员userId")
        private Long userId;
        /**
         * 群成员名称
         */
        @Schema(name = "userName", description = "邀请成员名称")
        private String userName;
    }
}
