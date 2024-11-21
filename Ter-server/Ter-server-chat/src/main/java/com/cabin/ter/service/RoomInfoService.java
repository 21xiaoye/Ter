package com.cabin.ter.service;

import com.cabin.ter.constants.request.GroupAddReq;
import com.cabin.ter.constants.response.ChatRoomResp;
import com.cabin.ter.constants.response.CursorPageBaseResp;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:44
 */
public interface RoomInfoService {
    /**
     * 新建群组
     *
     * @param uid       创建者uid
     * @param request   要求如愿uid
     * @return
     */
    Long addGroup(Long uid, GroupAddReq request);


    /**
     * 获取会话列表
     * @param uid       请求用户Uid
     * @return
     */
    CursorPageBaseResp<ChatRoomResp> getUserContactPage(Long uid);
}
