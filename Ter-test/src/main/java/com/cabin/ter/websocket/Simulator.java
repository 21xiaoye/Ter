package com.cabin.ter.websocket;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class Simulator {
    public static void start() {

        String serverIp = "172.31.116.46";
        int serverPort = 9002;
        EventLoopGroup group = new NioEventLoopGroup();
        for (int i = 0; i < 100; i++) {
            WebSocketConnector client = new WebSocketConnector(serverIp, serverPort, group);
            client.doConnect();
        }
    }

    public static void main(String[] args) {
        start();
    }

}
