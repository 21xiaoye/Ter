package com.cabin.ter.adapter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.chat.domain.FriendApplyDomain;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.chat.enums.HotFlagEnum;
import com.cabin.ter.chat.enums.RoomTypeEnum;
import com.cabin.ter.vo.request.FriendApplyReq;
import com.cabin.ter.vo.response.FriendApplyResp;

public class RoomAdapter {
    private static final Snowflake snowflake = new Snowflake();
    /**
     * 创建房间基本类型
     *
     * @param roomTypeEnum  房间类型
     * @param uId   房间创建uId
     * @return  房间实体对象
     */
    public static RoomDomain buildRoom(RoomTypeEnum roomTypeEnum, Long uId){
        return RoomDomain.builder()
                .type(roomTypeEnum.getType())
                .hotFlag(HotFlagEnum.NOT.getType())
                .id(snowflake.nextId())
                .uId(uId)
                .createTime(System.currentTimeMillis())
                .build();
    }
    /**
     * 创建群聊基本信息
     *
     * @param userDomain    创建者基本信息
     * @param roomId    房间Id
     * @return  群聊房间实体对象
     */
    public static GroupRoomDomain buildGroupRoom(UserDomain userDomain, Long roomId){
        return GroupRoomDomain.builder()
                .roomId(roomId)
                .RoomName(userDomain.getUserName()+"的房间")
                .RoomAvatar(userDomain.getUserAvatar())
                .createTime(System.currentTimeMillis())
                .build();
    }


    /**
     * 构建一个好友申请实体对象
     *
     * @param userId    申请者Id
     * @param friendApplyReq    申请请求实体
     * @return  好友申请持久化实体对象
     */
    public static FriendApplyDomain buildFriendApplyDomain(Long userId, FriendApplyReq friendApplyReq){
        return FriendApplyDomain.builder()
                .applyId(snowflake.nextId())
                .applyMessage(friendApplyReq.getApplyMessage())
                .createTime(System.currentTimeMillis())
                .userId(userId)
                .targetId(friendApplyReq.getTargetId())
                .build();
    }

    /**
     * 构建好友申请响应实体对象
     *
     * @param userInfoResp  好友申请被申请者信息
     * @param applyId       申请记录Id
     * @param applyStatus   好友申请状态
     * @param applyMessage  好友申请信息
     * @return  返回一个好友申请响应实体
     */
    public static FriendApplyResp buildFriendApplyResp(UserDomain userInfoResp, Long applyId, Integer applyStatus, String applyMessage, Integer applyType){
        FriendApplyResp friendApplyResp = new FriendApplyResp();
        BeanUtil.copyProperties(userInfoResp, friendApplyResp);
        friendApplyResp.setApplyStatus(applyStatus);
        friendApplyResp.setApplyId(applyId);
        friendApplyResp.setApplyMessage(applyMessage);
        friendApplyResp.setApplyType(applyType);
        return friendApplyResp;
    }

    /**
     * 构建好友关系持久化实体对象
     *
     * @param userId    申请者Id
     * @param targetId  被申请者Id
     * @return  返回好友关系持久化对象
     */
    public static FriendRoomDomain buildFriendRoomDomain(Long roomId,Long userId, Long targetId, String roomName){
        return FriendRoomDomain.builder()
                .roomId(roomId)
                .userId(userId)
                .targetId(targetId)
                .roomName(roomName)
                .build();
    }
}
