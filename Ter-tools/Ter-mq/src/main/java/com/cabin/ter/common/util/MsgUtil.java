package com.cabin.ter.common.util;

import com.alibaba.fastjson.JSON;
import com.cabin.ter.common.constants.entity.msg.MessageParticipant;
import com.cabin.ter.common.constants.entity.msg.WebSocketParticipant;

/**
 * <p>
 *     推送消息处理工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 01:12
 */

public class MsgUtil {

    public static WebSocketParticipant buildMsg(String channelId, String content) {
        return new WebSocketParticipant(channelId, content);
    }

    public static WebSocketParticipant json2Obj(String objJsonStr) {
        return JSON.parseObject(objJsonStr, WebSocketParticipant.class);
    }

    public static String obj2Json(WebSocketParticipant msgAgreement) {
        return JSON.toJSONString(msgAgreement);
    }

}