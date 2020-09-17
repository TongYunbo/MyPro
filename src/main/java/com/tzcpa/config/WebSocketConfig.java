package com.tzcpa.config;

import com.tzcpa.config.netty.NettyService;
import com.tzcpa.config.netty.SocketUser;
import com.tzcpa.utils.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.annotation.PostConstruct;

/**
 * netty webSocket配置 -- 开启STOMP协议
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

@Autowired NettyService nettyService;
    @Override
    //注册STOMP协议节点 映射的url
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //广播 指定使用SockJS协议
        stompEndpointRegistry.addEndpoint("/broadcast").withSockJS();
    }

    @Override
    //配置消息代理
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //
        registry.enableSimpleBroker("/topic");
    }

    /**
     * 启动Netty 也可以不在App.class内实现CommandLineRunner 重写run方法
     * 在NettyService要重新开一个线程
     */
//    @Bean
//    public NettyService initNetty() throws Exception {
//        log.info("netty注入----------------------------------");
//        return nettyService;
//
//    }
}
