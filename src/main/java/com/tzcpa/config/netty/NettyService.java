package com.tzcpa.config.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 * @author Dream
 *  Netty的服务
 *
 */
@Slf4j
@Component
public class NettyService {

    @Autowired
    MyChannelInitializer myChannelInitializer;

    @PostConstruct
    public void  getNettyService() {
        new Thread(() -> {
            log.info("启动Netty!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(myChannelInitializer);
                Channel channel = serverBootstrap.bind(8089).sync().channel();

                channel.closeFuture().sync();
            } catch (Exception e) {
                log.error("启动异常",e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

}

