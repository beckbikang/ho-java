package cn.beckbi.service;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.memcache.binary.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-09 22:43
 */
@Slf4j
@Component
public class PushHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {


        if (msg instanceof DefaultFullBinaryMemcacheRequest) {

            try {
                DefaultFullBinaryMemcacheRequest request = (DefaultFullBinaryMemcacheRequest) msg;
                switch (request.opcode()) {
                    case BinaryMemcacheOpcodes.SET:
                        this.dealWithMessage(ctx, request);
                        break;
                    default:
                        ctx.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                ctx.close();
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
    }

    //处理消息
    private void dealWithMessage(ChannelHandlerContext ctx, DefaultFullBinaryMemcacheRequest request) throws UnsupportedEncodingException {

        threadPoolExecutor.submit(()->{
            //多线程写入kafka
            String key = request.key().toString(CharsetUtil.UTF_8);
            String content = request.content().toString(CharsetUtil.UTF_8);


            //todo write kafka


            ByteBuf byteBuf = null;
            try {
                byteBuf = Unpooled.wrappedBuffer("".getBytes("US-ASCII"));
            }catch (UnsupportedEncodingException ex) {
                log.error("UnsupportedEncodingException", ex);
            }

            //写入成功
            assert byteBuf != null;
            DefaultFullBinaryMemcacheResponse response =
                    new DefaultFullBinaryMemcacheResponse(null, null, byteBuf);
            response.setStatus(BinaryMemcacheResponseStatus.SUCCESS);
            response.setOpaque(request.opaque());
            response.setOpcode(request.opcode());
            response.setCas(0);
            response.setTotalBodyLength(0);
            ctx.writeAndFlush(response);

        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("listen error", cause);
        ctx.close();
    }
}
