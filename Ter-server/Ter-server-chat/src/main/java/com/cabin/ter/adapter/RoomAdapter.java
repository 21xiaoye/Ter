package com.cabin.ter.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.cabin.ter.admin.domain.FriendApplyDomain;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import com.cabin.ter.vo.request.ChatMessageReq;
import com.cabin.ter.vo.request.FriendApplyReq;
import com.cabin.ter.vo.response.FriendApplyResp;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

public class RoomAdapter {
    /**
     * 构建一个好友申请实体对象
     *
     * @param applyId   申请记录Id
     * @param userId    申请者Id
     * @param friendApplyReq    申请请求实体
     * @return  好友申请持久化实体对象
     */
    public static FriendApplyDomain buildFriendApplyDomain(Long applyId, Long userId, FriendApplyReq friendApplyReq){
        FriendApplyDomain build = FriendApplyDomain.builder()
                .applyId(applyId)
                .applyMessage(friendApplyReq.getApplyMessage())
                .createTime(System.currentTimeMillis())
                .userId(userId)
                .targetId(friendApplyReq.getTargetId())
                .build();
        return build;
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
    public static FriendRoomDomain buildFriendRoomDomain(){
        return null;
    }
}
