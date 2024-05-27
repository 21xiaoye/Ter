package com.cabin.ter.service;


import com.cabin.ter.vo.response.FriendApplyReq;
import org.springframework.stereotype.Service;

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
}
