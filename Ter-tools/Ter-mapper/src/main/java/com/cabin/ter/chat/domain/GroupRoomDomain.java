package com.cabin.ter.chat.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GroupRoomDomain implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 群名称
     */
    private String RoomName;

    /**
     * 群头像
     */
    private String RoomAvatar;


    /**
     * 逻辑删除(0-正常,1-删除)
     */
    private Integer deleteStatus;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}
