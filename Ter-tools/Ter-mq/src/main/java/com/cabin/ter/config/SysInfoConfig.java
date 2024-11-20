package com.cabin.ter.config;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.sys.domain.SysDomain;
import com.cabin.ter.sys.mapper.SysDomainMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * <p>
 *     服务信息
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-04 15:33
 */
@Slf4j
@Component
public class SysInfoConfig {
    @Component
    public static class ServerInfo {
        private static final Snowflake snowflake = new Snowflake();
        @Value("${server.port}")
        private int serverPort;
        @Value("${netty.port}")
        private int nettyPort;
        @Autowired
        private SysDomainMapper sysDomainMapper;
        @PostConstruct
        public void getServerIpAddress() {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                SysDomain http = SysDomain.builder()
                        .sysId(snowflake.nextId())
                        .sysPort(serverPort)
                        .sysHost(localhost.getHostAddress())
                        .sysType("http")
                        .build();
                SysDomain ws = SysDomain.builder()
                        .sysId(snowflake.nextId())
                        .sysPort(nettyPort)
                        .sysHost(localhost.getHostAddress())
                        .sysType("ws")
                        .build();
                sysDomainMapper.insertSysWsList(Arrays.asList(http,ws));
            } catch (UnknownHostException e) {
                log.info("记录系统运行信息错误{}",e.getMessage());
            }
        }
    }
}
