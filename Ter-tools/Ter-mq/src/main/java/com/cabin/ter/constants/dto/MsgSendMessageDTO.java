package com.cabin.ter.constants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 16:46
 */
@Data
public class MsgSendMessageDTO extends MQBaseMessage implements Serializable {
    private Long msgId;
}
