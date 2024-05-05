package com.cabin.ter.common.util;

import com.alibaba.fastjson.JSON;
import com.cabin.ter.common.constants.participant.msg.WebSocketSingleParticipant;

/**
 * <p>
 *     推送消息处理工具类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 01:12
 */

public class MsgUtil {

    public static WebSocketSingleParticipant buildMsg(String channelId, String content) {
        return new WebSocketSingleParticipant(channelId, content);
    }

    public static WebSocketSingleParticipant json2Obj(String objJsonStr) {
        return JSON.parseObject(objJsonStr, WebSocketSingleParticipant.class);
    }

    public static String obj2Json(WebSocketSingleParticipant msgAgreement) {
        return JSON.toJSONString(msgAgreement);
    }

}