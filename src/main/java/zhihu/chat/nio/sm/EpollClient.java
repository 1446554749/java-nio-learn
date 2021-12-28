package zhihu.chat.nio.sm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class EpollClient {
    public static void main(String[] args) {
        //100 -> output -> 50 ->output
        try {
            Selector selector = Selector.open();
            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            channel.configureBlocking(false);
            SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_WRITE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}