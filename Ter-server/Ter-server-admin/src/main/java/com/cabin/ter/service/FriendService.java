package com.cabin.ter.service;


import com.cabin.ter.admin.domain.FriendApplyDomain;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.vo.request.ApprovalFriendReq;
import com.cabin.ter.vo.request.FriendApplyReq;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-27 10:15
 */
public interface FriendService {

    /**
     * 应用
     * 申请好友
     *
     * @param request 请求
     * @param uid     uid
     */
    void apply(Long uid, FriendApplyReq request);

    /**
     * 获取好友申请列表
     * @param userId    用户Id
     * @return  返回好哟申请列表
     */
    List<FriendApplyDomain> getFriendApplyRecord(Long userId);

    /**
     * 审批好友申请
     * @param approvalFriendReq 审批好友请求参数
     */
    ApiResponse approvalFriendApplyRecord(ApprovalFriendReq approvalFriendReq);
}
