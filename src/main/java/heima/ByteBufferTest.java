package heima;

import java.nio.ByteBuffer;

import static heima.ByteBufferUtil.debugAll;

public class ByteBufferTest {

    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        //                     11            24
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);

        source.put("w are you?\nhaha!\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        //read
        int oldLimit = source.limit();
        for (int i = 0; i < source.limit(); i++) {
            if(source.get(i)=='\n'){
                ByteBuffer target = ByteBuffer.allocate(i - source.position() + 1);
                source.limit(i+1);
                target.put(source);//read source from position to limit
                debugAll(target);
                source.limit(oldLimit);
            }
        }
        source.compact();
    }
}