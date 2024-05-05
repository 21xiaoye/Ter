package com.cabin.ter.common.controller;

import com.alibaba.fastjson.JSON;
import com.cabin.ter.common.constants.enums.SourceEnum;
import com.cabin.ter.common.constants.participant.TopicConstant;
import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.common.constants.participant.ws.SendChannelInfo;
import com.cabin.ter.common.constants.participant.ws.ServerInfo;
import com.cabin.ter.common.template.RocketMQEnhanceTemplate;
import com.cabin.ter.common.util.CacheUtil;
import com.cabin.ter.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/mq")
    public SendResult mqTest(){
        WebSocketSingleParticipant webSocketSingleParticipant = new WebSocketSingleParticipant();
        webSocketSingleParticipant.setKey(UUID.randomUUID().toString());
        webSocketSingleParticipant.setSource(SourceEnum.TEST_SOURCE.getSource());
        webSocketSingleParticipant.setContent("这里是消息的主要内容");
        webSocketSingleParticipant.setSendTime(LocalDate.now());
        return rocketMQEnhanceTemplate.send(TopicConstant.SOURCE_BROADCASTING_GROUP, webSocketSingleParticipant);
    }
}
