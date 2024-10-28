package com.cabin.ter.controller;

import com.alibaba.fastjson.JSON;
import com.cabin.ter.constants.participant.ws.SendChannelInfo;
import com.cabin.ter.constants.participant.ws.ServerInfo;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import com.cabin.ter.util.CacheUtil;
import com.cabin.ter.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/test")
@Slf4j
public class WebSocketController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;

    /**
     *  查 服务端 列表
     * @return
     */
    @RequestMapping("/queryNettyServerList")
    public Collection<ServerInfo> queryNettyServerList() {
        try {
            Collection<ServerInfo> serverInfos = CacheUtil.serverInfoMap.values();
            log.info("查询服务端列表。{}", JSON.toJSONString(serverInfos));
            return serverInfos;
        } catch (Exception e) {
            log.info("查询服务端列表失败。", e);
            return null;
        }
    }

    /**
     *  从 redis 查 用户管道
     * @return
     */
    @RequestMapping("/queryUserChannelInfoList")
    public List<SendChannelInfo> queryUserChannelInfoList() {
        try {
            log.info("查询用户列表信息开始");
            List<SendChannelInfo> userChannelInfoList = redisUtil.popList();
            log.info("查询用户列表信息完成。list：{}", JSON.toJSONString(userChannelInfoList));
            return userChannelInfoList;
        } catch (Exception e) {
            log.error("查询用户列表信息失败", e);
            return null;
        }
    }

}
