package heima.server.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static heima.ByteBufferUtil.debugAll;

@SuppressWarnings("InfiniteLoopStatement")
@Slf4j
public class SelectorServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        //add channel to selector
        Selector selector = Selector.open();
        SelectionKey selectionKey = ssc.register(selector, 0);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        ByteBuffer buffer = ByteBuffer.allocate(32);
        Set<SelectionKey> selectionKeys = null;
        while (true) {
            int eventNum = selector.select();

            if(eventNum>0){
                selectionKeys = selector.selectedKeys();
            }
            assert selectionKeys != null;

            Iterator<SelectionKey> iter = selectionKeys.iterator();
            while(iter.hasNext()){
                SelectionKey key = iter.next();
                //level trigger 与 edge trigger
                iter.remove();//因为java nio 采用水平触发，所以必须移除
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    int read = channel.read(buffer);
                    if(read != -1){
                        buffer.flip();
                        debugAll(buffer);
                        buffer.clear();
                    }else{
                        log.debug("client disconnect..close the channel");
                        key.cancel();
                        channel.close();
                    }
                }
            }
        }
    }
}
