package cn.beckbi.protocol;

import cn.beckbi.messages.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;


import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class MemcacheDecoder extends ReplayingDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int bytesToTake = in.bytesBefore((byte) ' ');
        String command = in.readBytes(bytesToTake).toString(Charset.defaultCharset()).trim();
        in.readByte();

        switch (command) {
            case "set": {
                handleSetMessage(in, out);
                break;
            }
            default: {
                throw new RuntimeException("Unexpected command: |" + command + "|");
            }
        }

    }



    private void handleSetMessage(ByteBuf in, List<Object> out) {
        MemcacheStorageMessage memcacheStorageMessage = readStorageCommand(in);
        out.add(new MemcacheSetMessage(memcacheStorageMessage));
    }

    private MemcacheStorageMessage readStorageCommand(ByteBuf in) {
        int bytesToTake;
        bytesToTake = in.bytesBefore((byte) ' ');
        String key = in.readBytes(bytesToTake).toString(Charset.defaultCharset());
        in.readByte();

        bytesToTake = in.bytesBefore((byte) ' ');
        Integer flags = Integer.parseInt(in.readBytes(bytesToTake).toString(Charset.defaultCharset()));
        in.readByte();

        bytesToTake = in.bytesBefore((byte) ' ');
        Integer ttl = Integer.parseInt(in.readBytes(bytesToTake).toString(Charset.defaultCharset()));
        in.readByte();

        bytesToTake = in.bytesBefore((byte) '\r');
        Integer number = Integer.parseInt(in.readBytes(bytesToTake).toString(Charset.defaultCharset()).trim());
        in.readByte(); // the \r
        in.readByte(); // the \n

        byte[] data = new byte[number];
        in.readBytes(data);
        in.readByte(); // the \r
        in.readByte(); // the \n

        return new MemcacheStorageMessage(key, flags, ttl, data);
    }
}
