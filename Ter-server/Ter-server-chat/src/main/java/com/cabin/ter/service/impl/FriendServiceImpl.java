package com.cabin.ter.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.adapter.RoomAdapter;
import com.cabin.ter.admin.domain.FriendApplyDomain;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.admin.mapper.FriendApplyDomainMapper;
import com.cabin.ter.annotation.NullToEmpty;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.chat.mapper.RoomDomainMapper;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.listener.event.FriendApplyEvent;
import com.cabin.ter.service.FriendService;
import com.cabin.ter.vo.request.ApprovalFriendReq;
import com.cabin.ter.vo.request.FriendApplyReq;
import com.cabin.ter.vo.response.FriendApplyResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *     好友服务
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-27
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendApplyDomainMapper friendApplyDomainMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private RoomDomainMapper roomDomainMapper;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private Snowflake snowflake;
    @Override
    public void apply(Long uId, FriendApplyReq request) {
        FriendApplyDomain friendApplyDomain = RoomAdapter.buildFriendApplyDomain(snowflake.nextId(), uId, request);
        friendApplyDomainMapper.saveFriendApplyRecord(friendApplyDomain);
        applicationEventPublisher.publishEvent(new FriendApplyEvent(this,uId, request.getTargetId()));
    }

    @Override
    @NullToEmpty
    public List<FriendApplyResp> getFriendApplyRecord(Long userId) {
        List<FriendApplyDomain> friendApplyDomainList = friendApplyDomainMapper.getFriendApplyRecord(userId);
        return friendApplyDomainList.stream()
                .map(friendApply -> {
                    UserDomain userInfo = userInfoCache.getUserInfo(friendApply.getUserId());
                    return RoomAdapter.buildFriendApplyResp(
                            userInfo,
                            friendApply.getApplyId(),
                            friendApply.getApplyStatus(),
                            friendApply.getApplyMessage(),
                            Objects.equals(friendApply.getUserId(), userId) ? FriendApplyResp.USER_APPLY : FriendApplyResp.TARGET_APPLY
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse operateFriendApplyRecord(ApprovalFriendReq approvalFriendReq) {
        if(Objects.equals(approvalFriendReq.getApplyStatus(), FriendApplyDomain.DELETE_APPLY)){
            friendApplyDomainMapper.deleteFriendApplyRecord(approvalFriendReq.getApplyId(), approvalFriendReq.getApplyStatus(), System.currentTimeMillis());
        }
        friendApplyDomainMapper.approvalFriendApplyRecord(approvalFriendReq.getApplyId(), approvalFriendReq.getApplyStatus(), System.currentTimeMillis());
        if(Objects.equals(approvalFriendReq.getApplyStatus(), FriendApplyDomain.AGREE_APPLY)){
            // 创建好友房间

        }
        return ApiResponse.ofSuccess();
    }
}
