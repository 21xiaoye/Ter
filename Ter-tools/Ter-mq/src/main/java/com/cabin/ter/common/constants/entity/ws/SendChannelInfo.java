package com.cabin.ter.common.constants.entity.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 *     发送者管道信息，记录分配的服务器
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 21:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendChannelInfo {
    /**
     * 服务Ip
     */
    private String ip;

    /**
     * 服务端口
     */
    private int port;

    /**
     * 频道id
     */
    private String channelId;

    /**
     * 链接时间
     */
    private Date linkDate;
}
