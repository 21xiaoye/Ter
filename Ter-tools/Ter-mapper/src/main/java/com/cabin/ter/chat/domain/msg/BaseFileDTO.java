package com.cabin.ter.chat.domain.msg;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:22
 */


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(name = "size",description = "大小（字节）")
    @NotNull
    private Long size;

    @Schema(name = "url",description = "下载地址")
    @NotBlank
    private String url;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MessageExtra implements Serializable {
        private static final long serialVersionUID = 1L;
        //url跳转链接
        private Map<String, UrlInfo> urlContentMap;
        //消息撤回详情
        private MsgRecall recall;
        //艾特的uid
        private List<Long> atUidList;
        //文件消息
        private FileMsgDTO fileMsg;
        //图片消息
        private ImgMsgDTO imgMsgDTO;
        //语音消息
        private SoundMsgDTO soundMsgDTO;
        //文件消息
        private VideoMsgDTO videoMsgDTO;

        /**
         * 表情图片信息
         */
        private EmojisMsgDTO emojisMsgDTO;
    }
}
