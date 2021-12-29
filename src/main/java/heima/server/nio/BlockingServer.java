package heima.server.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static heima.ByteBufferUtil.debugAll;

@SuppressWarnings("InfiniteLoopStatement")
@Slf4j
public class BlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ArrayList<SocketChannel> channels = new ArrayList<>();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        while(true){
            log.debug("connecting....");
            SocketChannel sc = ssc.accept();
            log.debug("connected....{}",sc);
            channels.add(sc);
            channels.forEach((socketChannel)->{
                try {
                    log.debug("before read.....");
                    socketChannel.read(buffer);
                    buffer.flip();
                    debugAll(buffer);
                    buffer.clear();
                    log.debug("after read.....");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
