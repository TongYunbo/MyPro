package com.tzcpa.config.netty;

import com.tzcpa.model.treatment.UserInfo;
import com.tzcpa.utils.UserSessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dream
 *
 *  组装handler
 */
@Slf4j
@Component
public class MyChannelInitializer extends ChannelInitializer {
    @Autowired
    MyMatchingHandler myMatchingHandler;
    @Override
    protected void initChannel(Channel channel) throws Exception {
        try {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast("http-codec", new HttpServerCodec()); // Http消息编码解码
            pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // Http消息组装
            pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // WebSocket通信支持
            //pipeline.addLast("handler", new MyHandler());//指定房间
            pipeline.addLast("handler",myMatchingHandler);//每两个匹配房间
        }catch (Exception e){
            log.error("netty初始化异常",e);
        }
    }
}
