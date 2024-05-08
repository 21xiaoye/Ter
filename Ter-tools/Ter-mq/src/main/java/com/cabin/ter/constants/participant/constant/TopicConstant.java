package com.cabin.ter.constants.participant.constant;

/**
 * <p>
 *     主题
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-05 16:00
 */
public interface TopicConstant {
    /**
     * redis 用户订阅主题
     */
    String REDIS_USER_MESSAGE_PUSH = "redis_user_message_push";

    /**
     *  广播推送主题
     */
    String ROCKETMQ_BROADCASTING_PUSH_MESSAGE_TOPIC = "rocketmq_broadcasting_push_message_topic";
    /**
     * 消息广播推送组
     */
    String SOURCE_BROADCASTING_GROUP = "rocketmq_broadcasting_message_group";
    /**
     * 广播推送，所有用户接受消息
     */
    String SOURCE_BROADCASTING_WIND_TAG = "rocketmq_broadcasting_wind_tag";




    /**
     * 单点推送主题
     */
    String ROCKET_SINGLE_PUSH_MESSAGE_TOPIC = "rocketmq_single_push_message_topic";
    /**
     * 单点推送组
     */
    String SINGLE_SINGLE_PUSH_MESSAGE_GROUP = "rocketmq_single_push_message_group";
    /**
     * 单点推送，指定用户接受消息
     */
    String SOURCE_SINGLE_PUSH_TAG = "rocket_single_push_message_tag";
}
