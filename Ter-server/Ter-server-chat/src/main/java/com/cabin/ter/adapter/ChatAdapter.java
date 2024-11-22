package com.cabin.ter.adapter;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.chat.domain.*;
import com.cabin.ter.constants.enums.GroupRoleEnum;
import com.cabin.ter.constants.request.GroupAddReq;
import com.cabin.ter.constants.response.FriendResp;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:09
 */
public class ChatAdapter {
    private static final Snowflake snowflake = new Snowflake();

    /**
     * 创建群成员列表
     *
     * @param memberInfoList   成员信息列表
     * @param groupId   群组id
     * @param userId       创建者id
     * @return
     */
    public static List<GroupMemberDomain> buildGroupMemberBatch(List<GroupAddReq.MemberInfo> memberInfoList, Long groupId, Long userId) {
        return memberInfoList.stream()
                .distinct()
                .map(memberInfo -> {
                    GroupMemberDomain member = new GroupMemberDomain();
                    member.setId(snowflake.nextId());
                    // 如果是创建者则分配权限未群主
                    member.setRole(userId.equals(memberInfo.getUserId()) ? GroupRoleEnum.LEADER.getType() : GroupRoleEnum.MEMBER.getType());
                    member.setUserId(memberInfo.getUserId());
                    member.setGroupId(groupId);
                    member.setCreateTime(System.currentTimeMillis());
                    member.setGroupRemark(memberInfo.getUserName());
                    return member;
                }).collect(Collectors.toList());
    }

    public static FriendResp buildFriendResp(UserDomain userDomain, String roomName){
        return FriendResp.builder()
                        .userId(userDomain.getUserId())
                        .userAvatar(userDomain.getUserAvatar())
                        .userEmail(userDomain.getUserEmail())
                        .userName(roomName)
                        .sex(userDomain.getSex())
                        .build();
    }

    /**
     * 获取好友uId 集合
     *
     * @param values
     * @param uid
     * @return
     */
    public static Set<Long> getFriendUidSet(Collection<FriendRoomDomain> values, Long uid){
        return values.stream()
                .map(a-> getFriendUid(a, uid))
                .collect(Collectors.toSet());
    }

    /**
     * 获取好友uid
     */
    public static Long getFriendUid(FriendRoomDomain roomFriend, Long uid) {
        return Objects.equals(uid, roomFriend.getUserId()) ? roomFriend.getFriendId() : roomFriend.getUserId();
    }
}
