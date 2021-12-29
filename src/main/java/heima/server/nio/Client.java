package heima.server.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import static heima.ByteBufferUtil.debugAll;

@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("localhost",8080));
        ByteBuffer buffer = ByteBuffer.allocate(32);
        sc.read(buffer);
        debugAll(buffer);
    }
}
