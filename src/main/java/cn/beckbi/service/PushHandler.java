package cn.beckbi.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.memcache.binary.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-09 22:43
 */
@Slf4j
@Component
public class PushHandler extends ChannelInboundHandlerAdapter {

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

        //多线程写入kafka

        //写入成功
        DefaultFullBinaryMemcacheResponse response =
                new DefaultFullBinaryMemcacheResponse(null, null,
                        Unpooled.wrappedBuffer("".getBytes("US-ASCII")));
        response.setStatus(BinaryMemcacheResponseStatus.SUCCESS);
        response.setOpaque(request.opaque());
        response.setOpcode(request.opcode());
        response.setCas(0);
        response.setTotalBodyLength(0);
        ctx.writeAndFlush(response);
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
