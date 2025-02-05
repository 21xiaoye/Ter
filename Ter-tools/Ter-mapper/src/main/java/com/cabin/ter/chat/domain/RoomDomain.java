package com.cabin.ter.chat.domain;

import com.cabin.ter.constants.enums.HotFlagEnum;
import com.cabin.ter.constants.enums.RoomTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDomain {
    /**
     * id
     */
    private Long roomId;
    /**
     * 创建者uid
     */
    private Long userId;
    /**
     * 房间类型 1群聊 2单聊
     */
    private Integer roomType;

    /**
     * 是否全员展示 0否 1是
     */
    private Integer hotFlag;

    /**
     * 群最后消息的更新时间（热点群不需要写扩散，更新这里就行）
     */
    private Long activeTime;

    /**
     * 最后一条消息id
     */
    private Long lastMsgId;

    /**
     * 额外信息（根据不同类型房间有不同存储的东西）
     */
    private String extJson;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;


    @JsonIgnore
    public boolean isHotRoom() {
        return HotFlagEnum.of(this.hotFlag) == HotFlagEnum.YES;
    }

    @JsonIgnore
    public boolean isRoomFriend() {
        return RoomTypeEnum.of(this.roomType) == RoomTypeEnum.FRIEND;
    }

    @JsonIgnore
    public boolean isRoomGroup() {
        return RoomTypeEnum.of(this.roomType) == RoomTypeEnum.GROUP;
    }
}
