package com.cabin.ter.service.impl;

import com.cabin.ter.adapter.MessageAdapter;
import com.cabin.ter.adapter.RoomAdapter;
import com.cabin.ter.chat.domain.FriendApplyDomain;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.chat.mapper.FriendApplyDomainMapper;
import com.cabin.ter.annotation.NullToEmpty;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.chat.mapper.FriendRoomDomainMapper;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.listener.event.FriendApplyEvent;
import com.cabin.ter.service.ChatService;
import com.cabin.ter.service.FriendService;
import com.cabin.ter.service.RoomService;
import com.cabin.ter.util.AsserUtil;
import com.cabin.ter.vo.request.ApprovalFriendReq;
import com.cabin.ter.vo.request.FriendApplyReq;
import com.cabin.ter.vo.response.FriendApplyResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private FriendRoomDomainMapper friendRoomDomainMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private ChatService chatService;
    @Override
    public void apply(Long uId, FriendApplyReq request) {
        FriendApplyDomain friendApplyDomain = RoomAdapter.buildFriendApplyDomain(uId, request);
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
    @Transactional(rollbackFor = Exception.class)
    public void operateFriendApplyRecord(ApprovalFriendReq approvalFriendReq,Long userId) {
        // 删除好友申请记录
        if(Objects.equals(approvalFriendReq.getApplyStatus(), FriendApplyDomain.DELETE_APPLY)){
            friendApplyDomainMapper.deleteFriendApplyRecord(approvalFriendReq.getApplyId(), approvalFriendReq.getApplyStatus(), System.currentTimeMillis());
            return;
        }
        // 审批好友申请拒绝或者同意
        friendApplyDomainMapper.approvalFriendApplyRecord(approvalFriendReq.getApplyId(), approvalFriendReq.getApplyStatus(), System.currentTimeMillis());
        // 同意好友创建房间
        if(Objects.equals(approvalFriendReq.getApplyStatus(), FriendApplyDomain.AGREE_APPLY)){
            // 已经添加过好友，但是拉黑或者删除直接恢复好友关系
            FriendRoomDomain friendDomain = friendRoomDomainMapper.getFriendship(userId, approvalFriendReq.getTargetId());
            if(Objects.nonNull(friendDomain) && !friendDomain.getStatus().equals(FriendRoomDomain.FRIENDSHIP_RECOVER)){
                friendRoomDomainMapper.operateFriendship(friendDomain.getRoomId(), FriendRoomDomain.FRIENDSHIP_RECOVER);
            }
            // 创建好友房间
            friendDomain = roomService.createFriend(userId, approvalFriendReq.getTargetId());
            AsserUtil.isEmpty(friendDomain, "房间创建失败");
            chatService.sendMsg(MessageAdapter.buildAgreeMsg(friendDomain.getRoomId()),userId);
        }
    }
}
