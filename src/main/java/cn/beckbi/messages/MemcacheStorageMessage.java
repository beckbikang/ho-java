package cn.beckbi.messages;

import lombok.Data;
import lombok.ToString;

import java.util.Arrays;

@ToString
@Data
public class MemcacheStorageMessage {
    private final String key;
    private final int flags;
    private final int ttl;
    private final int numberOfBytes;
    private final byte[] data;

    public MemcacheStorageMessage(String key, int flags, int ttl, byte[] data) {
        this.key = key;
        this.flags = flags;
        this.ttl = ttl;
        this.numberOfBytes = data.length;
        this.data = data;
    }

    public MemcacheStorageMessage(MemcacheStorageMessage memcacheStorageMessage) {
        this(memcacheStorageMessage.getKey(), memcacheStorageMessage.getFlags(), memcacheStorageMessage.getTtl(), memcacheStorageMessage.getData());
    }

}
