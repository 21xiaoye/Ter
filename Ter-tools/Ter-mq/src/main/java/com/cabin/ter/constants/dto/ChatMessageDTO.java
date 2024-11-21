package com.cabin.ter.constants.dto;

import com.cabin.ter.constants.response.WSBaseResp;
import lombok.Data;

import java.util.List;

@Data
public class ChatMessageDTO extends MQBaseMessage{
    private WSBaseResp<?> wsBaseResp;
    private List<Long> userIdList;
}
