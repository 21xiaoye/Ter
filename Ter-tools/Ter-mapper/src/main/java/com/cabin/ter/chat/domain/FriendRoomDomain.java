package com.cabin.ter.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoye
 * @date Created in 204-05-30 17:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRoomDomain {
    /**
     * 恢复好哟关系
     */
    public static final Integer FRIENDSHIP_RECOVER = 0;
    /**
     * 拉黑好友
     */
    public static final Integer FRIENDSHIP_BLOCK = 1;
    /**
     * 删除好友
     */
    public static final Integer FRIENDSHIP_DELETE = 2;
    /**
     * 置顶好友关系
     */
    public static final Integer IS_TOP = 0;
    /**
     * 不置顶好友关系
     */
    public static final Integer NO_TOP = 1;
    /**
     * 申请者uid
     */
    private Long userId;
    /**
     * 被申请者uid
     */
    private Long friendId;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 房间key
     */
    private String roomName;
    /**
     * 好友状态
     */
    private Integer roomStatus;
    /**
     * 是否置顶
     */
    private Integer isTop;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 修改时间
     */
    private Long updateTime;
}
