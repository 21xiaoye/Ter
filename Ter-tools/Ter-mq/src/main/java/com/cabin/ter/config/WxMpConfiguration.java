package com.cabin.ter.config;

import com.cabin.ter.constants.participant.WxMpProperties;
import com.cabin.ter.handler.LogHandler;
import com.cabin.ter.handler.ScanHandler;
import com.cabin.ter.handler.SubscribeHandler;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.common.api.WxConsts.EventType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 21024-05-09
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {
    private final WxMpProperties properties;
    private final LogHandler logHandler;
    private final ScanHandler scanHandler;
    private final SubscribeHandler subscribeHandler;

    @Bean
    public WxMpService wxMpService(){
        final List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();

        WxMpServiceImpl service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs.
                stream().map(a ->{
                    WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
                    configStorage.setAppId(a.getAppId());
                    configStorage.setSecret(a.getSecret());
                    configStorage.setToken(a.getToken());
                    configStorage.setAesKey(a.getAesKey());
                    return configStorage;
                }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n)->o)));
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

        // 默认
//        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }
}
