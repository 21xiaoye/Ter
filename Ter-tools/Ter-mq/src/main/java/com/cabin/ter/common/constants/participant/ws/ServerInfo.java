package com.cabin.ter.common.constants.participant.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 *    服务端信息
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 22:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {

    /**
     * 服务Ip
     */
    private String ip;

    /**
     * 服务端口
     */
    private int port;

    /**
     * 服务启动时间
     */
    private Date openDate;
}
