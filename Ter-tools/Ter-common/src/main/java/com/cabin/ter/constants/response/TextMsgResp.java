package com.cabin.ter.constants.response;

import com.cabin.ter.constants.dto.UrlInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoye
 * @date Created in 2024-05-30 11:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextMsgResp {
    @Schema(name = "content",description = "消息内容")
    private String content;
    @Schema(name = "urlContentMap",description = "消息链接映射")
    private Map<String, UrlInfo> urlContentMap;
    @Schema(name = "atUidList",description = "艾特的uid")
    private List<Long> atUidList;
    @Schema(name = "reply",description = "父消息，如果没有父消息，返回的是null")
    private TextMsgResp.ReplyMsg reply;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReplyMsg {
        @Schema(name = "id",description = "消息id")
        private Long id;
        @Schema(name = "uid",description = "用户uid")
        private Long uid;
        @Schema(name = "username",description = "用户名称")
        private String username;
        @Schema(name = "type",description = "消息类型 1正常文本 2.撤回消息")
        private Integer type;
        @Schema(name = "body",description = "消息内容不同的消息类型，见父消息内容体")
        private Object body;
        @Schema(name = "canCallback",description = "是否可消息跳转 0否 1是")
        private Integer canCallback;
        @Schema(name = "gapCount",description = "跳转间隔的消息条数")
        private Integer gapCount;
    }
}
