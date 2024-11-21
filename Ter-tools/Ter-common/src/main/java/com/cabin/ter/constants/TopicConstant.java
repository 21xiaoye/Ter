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
     * 邮箱发送mq
     */
    String SYSTEM_EMAIL_SEND_TOPIC = "system_email_send_topic";
    String SYSTEM_EMAIL_SEND_GROUP ="system_email_send_group";
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
    String GLOBAL_USER_ONLINE_GROUP = "redis_global_user_online_group";
    /**
     * 用户下线通知
     */
    String GLOBAL_USER_OFFLINE_TOPIC = "redis_global_user_offline";
    String GLOBAL_USER_OFFLINE_GROUP = "redis_global_user_offline_group";
    /**
     * 聊天消息发送 mq
     */
    String CHAT_MESSAGE_SEND_TOPIC = "chat_message_send_topic";
    String CHAT_MESSAGE_SEND_GROUP = "chat_message_send_group";
    /**
     * push用户
     */
    String PUSH_TOPIC = "websocket_push";
    String PUSH_GROUP = "websocket_push_group";

}
