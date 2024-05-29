package com.cabin.ter.service.impl;

import com.cabin.ter.adapter.ChatAdapter;
import com.cabin.ter.chat.domain.GroupMemberDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.service.RoomInfoService;
import com.cabin.ter.service.RoomService;
import com.cabin.ter.vo.request.GroupAddReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:44
 */
@Service
@Slf4j
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    private RoomService roomService;
    @Autowired
    private ChatAdapter chatAdapter;

    /**
     * 新建群组
     *
     * @param uid       用户uid
     * @param request   群组如愿uid列表
     * @return
     */
    @Override
    public Long addGroup(Long uid, GroupAddReq request) {
        GroupRoomDomain group = roomService.createGroup(uid);
        List<GroupMemberDomain> groupMemberDomains = chatAdapter.buildGroupMemberBatch(request.getUidList(), group.getRoomId());

        return 1L;
    }
}
