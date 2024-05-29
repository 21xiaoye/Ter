package com.cabin.ter.service;

import com.cabin.ter.vo.request.GroupAddReq;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:44
 */
public interface RoomInfoService {
    Long addGroup(Long uid, GroupAddReq request);
}
