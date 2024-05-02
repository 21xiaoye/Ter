package com.cabin.ter.common.controller;

import com.alibaba.fastjson.JSON;
import com.cabin.ter.common.constants.entity.ws.SendChannelInfo;
import com.cabin.ter.common.constants.entity.ws.ServerInfo;
import com.cabin.ter.common.util.CacheUtil;
import com.cabin.ter.common.util.RedisUtil;
import com.cabin.ter.common.websocket.WebsocketServer;
import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.constants.vo.response.ApiResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/test")
@Slf4j
public class WebSocketController {
    @Autowired
    private RedisUtil redisUtil;

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
