package com.cabin.ter.chat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;


/**
 * @author xiaoye
 * @date Created in 2024-05-30 17:02
 */
@EqualsAndHashCode(callSuper = false)
@TableName("contact")
@Data
public class ContactDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableField("id")
    private Long id;

    /**
     * 用户uid
     */
    @TableField("uid")
    private Long uid;

    /**
     * 房间id
     */
    @TableField("roomId")
    private Long roomId;

    /**
     * 阅读到的时间
     */
    @TableField("readTime")
    private Long readTime;
    /**
     * 最新消息id
     */
    @TableField("lastMsgId")
    private Long lastMsgId;

    /**
     * 最细消息时间
     */
    @TableField("activeTime")
    private Long activeTime;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField("updateTime")
    private Long updateTime;
}
