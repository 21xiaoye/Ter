package com.cabin.ter.common.websocket;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class Simulator {
    public static void start() {
        String serverIp = "127.0.0.1";
        int serverPort = 9001;
        EventLoopGroup group = new NioEventLoopGroup();
        for (int i = 0; i < 100000; i++) {
            WebSocketConnector client = new WebSocketConnector(serverIp, serverPort, group);
            client.doConnect();
        }
    }

    public static void main(String[] args) {
        start();
    }

}
