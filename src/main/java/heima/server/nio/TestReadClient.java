package heima.server.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

@Slf4j
public class TestReadClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("localhost",8080));
        ByteBuffer buffer = ByteBuffer.allocate(1_0000_000);
        sc.write(Charset.defaultCharset().encode("hello,world"));
        sc.read(buffer);
        log.debug("buffer position {}",buffer.position());
//        debugAll(buffer);
    }
}
