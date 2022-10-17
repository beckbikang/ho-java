package cn.beckbi.service;


import cn.beckbi.messages.MemcacheSetMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.memcache.binary.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-09 22:43
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class PushHandler  extends SimpleChannelInboundHandler<MemcacheSetMessage> {

    @Autowired
    @Qualifier("testKafkaTemplate")
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.test.topic}")
    private String topic1;


    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MemcacheSetMessage msg) {

        log.info("msg={}", msg);
        try {
            this.dealWithMessage(ctx, msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ctx.close();
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    private void writeToKafka(String key, String content) {
        if (key.equals(topic1)) {
            kafkaTemplate.send(topic1, content);
        }
    }

    /**
     * 处理消息
     * @param ctx
     * @param request
     * @throws UnsupportedEncodingException
     */
    private void dealWithMessage(ChannelHandlerContext ctx, MemcacheSetMessage request) throws UnsupportedEncodingException {

        threadPoolExecutor.submit(()->{
            //多线程写入kafka
            String key = request.getKey();
            String content = new String(request.getData(), StandardCharsets.UTF_8);

            log.info("key={},content={}",key, content);

            this.writeToKafka(key, content);

            //写入成功
            ByteBufAllocator alloc = ctx.alloc();
            ByteBuf buffer = alloc.buffer();
            buffer.writeBytes("STORED\r\n".getBytes());
            ctx.writeAndFlush(buffer);

        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("listen error", cause);
        ctx.close();
    }
}
