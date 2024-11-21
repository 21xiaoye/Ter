package com.cabin.ter.service;


import com.cabin.ter.constants.request.ApprovalFriendReq;
import com.cabin.ter.constants.request.FriendApplyReq;
import com.cabin.ter.constants.request.WhiteReq;
import com.cabin.ter.constants.response.FriendApplyRecordInfoResp;
import com.cabin.ter.constants.response.FriendApplyResp;
import com.cabin.ter.constants.response.FriendResp;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-27 10:15
 */
public interface FriendService {

    /**
     * 申请好友
     *
     * @param request 请求
     * @param userId  申请者userId
     */
    FriendApplyResp apply(Long userId, FriendApplyReq request);
    /**
     * 获取好友申请列表
     * @param userId    用户Id
     * @return  返回好哟申请列表
     */
    List<FriendApplyRecordInfoResp> getFriendApplyRecord(Long userId);
    /**
     * 同意好友申请
     * @param approvalFriendReq 审批好友请求参数
     * @param userId 申请者Id
     */
    void agreeFriendApply(ApprovalFriendReq approvalFriendReq, Long userId);
    /**
     * 更改好友状态
     *
     * @param userId    用户uId
     * @param targetId  好友uId
     * @param friendStatus 好友状态
     */
    void operateFriendStatus(Long userId, Long targetId, Integer friendStatus);
    /**
     * 获取用户好友列表
     *
     * @param userId    用户uId
     * @return  返回好友成员列表
     */
    List<FriendResp> getFriendPage(Long userId);

    /**
     * 获取用户所有黑名单
     *
     * @param userId    用户uId
     * @return  返回黑名单成员列表
     */
    List<FriendResp> getBlockFriendPage(Long userId);

    /**
     * 将黑名单拉回白名单
     * @param userId    用户uId
     * @param whiteReq  需要拉回白名单的黑名单成员id列表
     */
    void pullBackWhitePage(Long userId,WhiteReq whiteReq);
}
