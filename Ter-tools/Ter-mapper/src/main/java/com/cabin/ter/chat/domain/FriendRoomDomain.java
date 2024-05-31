package com.cabin.ter.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoye
 * @date Created in 204-05-30 17:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRoomDomain {
    /**
     * 唯一标识
     */
    private Long id;
    /**
     * 申请者uid
     */
    private Long aUId;
    /**
     * 被申请者uid
     */
    private Long bUId;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 房间key
     */
    private String roomKey;
    /**
     * 好友状态
     */
    private Integer status;
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
