package com.cabin.ter.chat.domain;

import com.cabin.ter.chat.domain.msg.BaseFileDTO;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 会话表id
     */
    private Long roomId;

    /**
     * 消息发送者uid
     */
    private Long fromUid;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 回复的消息内容
     */
    private Long replyMsgId;

    /**
     * 消息状态 0正常 1删除
     *
     */
    private Integer status;

    /**
     * 与回复消息的间隔条数
     */
    private Integer gapCount;

    /**
     * 消息类型 1正常文本 2.撤回消息
     *
     * @see com.cabin.ter.constants.enums.MessageTypeEnum
     */
    private Integer type;

    /**
     * 消息扩展字段
     */
    @JsonSerialize(using = JsonSerializer.class)
    @JsonDeserialize(using = JsonDeserializer.class)
    private BaseFileDTO.MessageExtra extra;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}
