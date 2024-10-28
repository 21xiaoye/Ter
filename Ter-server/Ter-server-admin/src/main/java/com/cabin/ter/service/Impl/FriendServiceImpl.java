package com.cabin.ter.service.Impl;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.adapter.FriendAdapter;
import com.cabin.ter.admin.domain.FriendApplyDomain;
import com.cabin.ter.admin.mapper.FriendApplyDomainMapper;
import com.cabin.ter.constants.vo.response.ApiResponse;
import com.cabin.ter.listener.event.FriendApplyEvent;
import com.cabin.ter.service.FriendService;
import com.cabin.ter.vo.request.ApprovalFriendReq;
import com.cabin.ter.vo.request.FriendApplyReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private Snowflake snowflake;
    @Override
    public void apply(Long uId, FriendApplyReq request) {
        FriendApplyDomain friendApplyDomain = FriendAdapter.buildFriendApplyDomain(snowflake.nextId(), uId, request);
        friendApplyDomainMapper.saveFriendApplyRecord(friendApplyDomain);
        applicationEventPublisher.publishEvent(new FriendApplyEvent(this,uId, request.getTargetId()));
    }

    @Override
    public List<FriendApplyDomain> getFriendApplyRecord(Long userId) {
        return null;
    }

    @Override
    public ApiResponse approvalFriendApplyRecord(ApprovalFriendReq approvalFriendReq) {
        return null;
    }
}
