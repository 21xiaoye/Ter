package com.cabin.ter.constants;

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
     * redis 用户订阅主题 : 主要用于通知用户上下线通知
     */
    String REDIS_GLOBAL_USER_LINE_STATUS = "redis_global_user_line_status";

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

    String WEB_USER_MESSAGE_NOTIFICATION_group= "web_user_message_notification_group";
    /**
     * (授权完成后)登录信息mq
     */
    String LOGIN_MSG_TOPIC = "user_login_send_msg";
    String LOGIN_MSG_GROUP = "user_login_send_msg_group";

    /**
     * 扫码成功 信息发送mq
     */
    String SCAN_MSG_TOPIC = "user_scan_send_msg";
    String SCAN_MSG_GROUP = "user_scan_send_msg_group";
    /**
     * 用户上线通知
     */
    String GLOBAL_USER_ONLINE_TOPIC = "redis_global_user_online";
    /**
     * 用户下线通知
     */
    String GLOBAL_USER_OFFLINE_TOPIC = "redis_global_user_offline";
    /**
     * 邮箱绑定mq
     */
    String EMAIL_BINDING_TOPIC = "email_binding_msg";
    String EMAIL_BIDING_GROUP = "email_binding_send_msg_group";

    /**
     * 消息发送 mq
     */
    String SEND_MSG_TOPIC = "send_msg_topic";
    String SEND_MSG_GROUP = "chat_send_msg_group";
    /**
     * push用户
     */
    String PUSH_TOPIC = "websocket_push";
    String PUSH_GROUP = "websocket_push_group";

}
