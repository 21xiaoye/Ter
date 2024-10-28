package com.cabin.ter.adapter;

import com.cabin.ter.admin.domain.FriendApplyDomain;
import com.cabin.ter.vo.request.FriendApplyReq;

public class FriendAdapter {
    public static FriendApplyDomain buildFriendApplyDomain(Long applyId, Long userId,FriendApplyReq friendApplyReq){
        FriendApplyDomain build = FriendApplyDomain.builder()
                .applyId(applyId)
                .applyMessage(friendApplyReq.getApplyMessage())
                .approvalTime(System.currentTimeMillis())
                .userId(userId)
                .targetId(friendApplyReq.getTargetId())
                .build();
        return build;
    }
}
