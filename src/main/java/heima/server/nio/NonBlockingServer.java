package heima.server.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Optional;

import static heima.ByteBufferUtil.debugAll;

@SuppressWarnings("InfiniteLoopStatement")
@Slf4j
public class NonBlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));

        ArrayList<SocketChannel> channels = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        while (true) {
            SocketChannel socketChannel = ssc.accept();
            Optional.ofNullable(socketChannel).ifPresent((sc) -> {
                try {
                    sc.configureBlocking(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.debug("connected....{}", sc);
                channels.add(sc);
            });
            channels.forEach((channel) -> {
                try {
                    int read = channel.read(buffer);
                    if(read>0){
                        buffer.flip();
                        debugAll(buffer);
                        buffer.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
