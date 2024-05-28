package com.cabin.ter.config;

import com.cabin.ter.config.IdConfig;
import com.cabin.ter.sys.domain.SysDomain;
import com.cabin.ter.sys.mapper.SysDomainMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.ref.PhantomReference;
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
    public class ServerInfo {

        @Value("${server.port}")
        private int serverPort;
        @Value("${netty.port}")
        private int nettyPort;
        @Autowired
        private IdConfig idConfig;
        @Autowired
        private SysDomainMapper sysDomainMapper;
        @PostConstruct
        public void getServerIpAddress() {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                SysDomain http = SysDomain.builder()
                        .sysId(idConfig.snowflake().nextId())
                        .sysPort(serverPort)
                        .sysHost(localhost.getHostAddress())
                        .sysType("http")
                        .build();
                SysDomain ws = SysDomain.builder()
                        .sysId(idConfig.snowflake().nextId())
                        .sysPort(nettyPort)
                        .sysHost(localhost.getHostAddress())
                        .sysType("ws")
                        .build();
                sysDomainMapper.insertSysWsList(Arrays.asList(http,ws));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
