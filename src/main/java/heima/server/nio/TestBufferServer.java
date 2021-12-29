package heima.server.nio;

import heima.ByteBufferTest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static heima.ByteBufferUtil.debugAll;

@SuppressWarnings({"InfiniteLoopStatement", "DuplicatedCode"})
@Slf4j
public class TestBufferServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        //add channel to selector
        Selector selector = Selector.open();
        SelectionKey selectionKey = ssc.register(selector, 0);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

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
                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ);

                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    scKey.attach(buffer);

                }else if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int read = channel.read(buffer);
                    if(read != -1){
                        //调用两次flip，导致不能读也不能写 limit = 0 ,p = 0
//                        buffer.flip();
                        ByteBufferTest.split(buffer);
                        //如果buffer满了还不够一个消息的话，split之后为pos = limit，所以需要扩容
                        log.debug("buffer: position:{} limit:{}",buffer.position(),buffer.limit());
                        if(buffer.position() == buffer.limit()){
                            ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                            buffer.flip();
                            newBuffer.put(buffer);
                            key.attach(newBuffer);
                        }
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
