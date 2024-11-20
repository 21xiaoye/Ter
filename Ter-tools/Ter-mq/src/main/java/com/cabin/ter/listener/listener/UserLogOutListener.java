package com.cabin.ter.listener.listener;

import com.cabin.ter.adapter.WebSocketMessageBuilderAdapter;
import com.cabin.ter.listener.event.UserLogOutEvent;
import com.cabin.ter.service.WebSocketPublicService;
import com.cabin.ter.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserLogOutListener {
    @Autowired
    private WebSocketPublicService webSocketPublicService;
    @Autowired
    private JwtUtil jwtUtil;
    @Async(value = "terExecutor")
    @EventListener(classes = UserLogOutEvent.class)
    public void saveRedisAndPush(UserLogOutEvent event) {
        Long userId = event.getUserId();
        webSocketPublicService.sendToUid(WebSocketMessageBuilderAdapter.buildInvalidateTokenResp(),userId);
        jwtUtil.invalidateJWT(userId);
    }
}
