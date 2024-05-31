package com.cabin.ter.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cabin.ter.chat.domain.ContactDomain;
import com.cabin.ter.chat.mapper.ContactDomainMapper;
import com.cabin.ter.constants.vo.request.CursorPageBaseReq;
import com.cabin.ter.constants.vo.response.CursorPageBaseResp;
import com.cabin.ter.util.CursorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactDomainDao extends ServiceImpl<ContactDomainMapper, ContactDomain> {
    @Autowired
    private CursorUtil cursorUtil;

    /**
     * 获取用户会话列表
     */
    public CursorPageBaseResp<ContactDomain> getContactPage(Long uid, CursorPageBaseReq request) {
        return cursorUtil.getCursorPageByMysql(this, request, wrapper -> {
            wrapper.eq(ContactDomain::getUid, uid);
        }, ContactDomain::getActiveTime);
    }

    public List<ContactDomain> getByRoomIds(List<Long> roomIds, Long uid) {
        return lambdaQuery()
                .in(ContactDomain::getRoomId, roomIds)
                .eq(ContactDomain::getUid, uid)
                .list();
    }
}