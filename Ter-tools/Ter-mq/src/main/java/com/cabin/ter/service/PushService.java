package com.cabin.ter.service;

import com.cabin.ter.constants.vo.response.WSBaseResp;


import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-30 13:09
 */
public interface PushService {
    void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList);
    void sendPushMsg(WSBaseResp<?> msg, Long uid);
    void sendPushMsg(WSBaseResp<?> msg);
}
