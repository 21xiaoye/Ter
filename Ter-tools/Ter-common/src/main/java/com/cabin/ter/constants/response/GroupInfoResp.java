package com.cabin.ter.constants.response;

import com.cabin.ter.constants.request.GroupAddReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfoResp {
    /**
     * 群Id
     */
    private Long roomId;
    /**
     * 群名称
     */
    private String roomName;
    /**
     * 群头像
     */
    private String roomAvatar;

    /**
     * 群成员列表
     */
    private List<GroupAddReq.MemberInfo> memberInfoList;
    @Data
    public static class MemberInfo{
        /**
         * 群成员userId
         */
        private Long userId;
        /**
         * 群成员名称
         */
        private String userName;
        /**
         * 群成员头像
         */
        private String userAvatar;
        /**
         * 群权限Id
         */
        private Integer roleId;
    }
}
