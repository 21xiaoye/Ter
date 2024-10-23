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
@Data
public class ContactDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private Long id;
    /**
     * 用户uid
     */
    private Long uid;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 阅读到的时间
     */
    private Long readTime;
    /**
     * 最新消息id
     */
    private Long lastMsgId;
    /**
     * 会话状态
     */
    private int status;
}
