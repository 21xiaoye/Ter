package com.cabin.ter.constants;

/**
 * <p>
 *     redis key
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-10 10:07
 */
public class RedisKey {
    private static final String BASE_KEY = "Ter:";
    /**
     * 在线人数
     */
    public static final String ONLINE_UID_ZET = "online";
    /**
     * 离线人数
     */
    public static final String OFFLINE_UID_ZET = "offline";
    /**
     * 保存Open id
     */
    public static final String OPEN_ID_STRING = "openid:%s";
    /**
     * 保存用户上线信息
     */

    public static final String USER_ONLINE_INFO="userInfo:uid_%d";
    /**
     * 房间详情
     */
    public static final String ROOM_INFO_STRING = "roomInfo:roomId_%d";

    /**
     * 群组详情
     */
    public static final String GROUP_INFO_STRING = "groupInfo:roomId_%d";
    /**
     * 群组成员信息
     */
    public static final String GROUP_MEMBER_INFO_STRING = "groupMemberInfo:roomId_%d";
    /**
     * 公共房间
     */
    public static final String HOT_ROOM_STRING = "hot_room_string:roomId_%d";

    /**
     * 保存邮箱验证码
     */
    public static final String SAVE_EMAIL_CODE = "save_email_code:%s";

    /**
     * 微信扫码登录，绑定邮箱成功之后，等待用户授权之前保存用户信息
     */
    public static final String AUTHORIZE_WX = "authorize_wx:%s";
    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
