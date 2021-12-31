package heima.server.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NioClient {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(10_000_000);

        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);

        Selector selector = Selector.open();
        sc.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_READ);
        sc.connect(new InetSocketAddress("localhost", 8080));
        Set<SelectionKey> selectionKeys = null;
        while(true){
            int eventNums = selector.select();
            if(eventNums>0){
                 selectionKeys = selector.selectedKeys();
            }
            assert selectionKeys != null;
            Iterator<SelectionKey> iter = selectionKeys.iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                if(key.isConnectable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    log.debug("connect.......sc:{}",channel.toString());
                    channel.finishConnect();
//                    key.interestOps(key.interestOps()-SelectionKey.OP_CONNECT);
                }else if(key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    int read = channel.read(buffer);
                    if(read==-1){
                        key.cancel();
                        channel.close();
                    }
                    log.debug("read length {}",read);
                    buffer.clear();
                }
                iter.remove();
            }
        }
    }
}
