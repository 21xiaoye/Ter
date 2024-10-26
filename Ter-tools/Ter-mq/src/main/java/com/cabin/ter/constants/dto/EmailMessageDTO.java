package com.cabin.ter.constants.dto;

import com.cabin.ter.constants.enums.EmailTypeEnum;
import lombok.Data;

/**
 * <p>
 *     邮箱推送消息
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 16:40
 */
@Data
public class EmailMessageDTO extends MQBaseMessage {
    /**
     * 消息主题
     */
    private String subject;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 附件地址
     */
    private String filePath;
    /**
     * 消息接收者
     */
    private String toAddress;
    /**
     * 邮件类型
     */
    private EmailTypeEnum emailType;
}
