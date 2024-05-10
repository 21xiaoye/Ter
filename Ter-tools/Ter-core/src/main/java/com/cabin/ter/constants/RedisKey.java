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

    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
