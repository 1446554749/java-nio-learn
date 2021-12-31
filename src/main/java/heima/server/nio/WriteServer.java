package heima.server.nio;

import com.google.common.io.BaseEncoding;
import heima.ByteBufferTest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

@Slf4j
public class WriteServer {
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
                iter.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);

                    byte[] bytes = new byte[1_000_000];
                    new Random().nextBytes(bytes);
                    String randomString = BaseEncoding.base64().omitPadding().encode(bytes);
                    ByteBuffer buffer = Charset.defaultCharset().encode(randomString);

                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ);

                    int write = sc.write(buffer);
                    log.debug("write byte {}",write);
                    if(buffer.hasRemaining()){
                        scKey.interestOpsOr(SelectionKey.OP_WRITE);
                        scKey.attach(buffer);
                    }
                }else if (key.isWritable()){
                    final SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    try {
                        int write = sc.write(buffer);
                        log.debug("writable... byte {}",write);
                        if(!buffer.hasRemaining()){
                            key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                            key.attach(null);
                        }
                    }catch (IOException e){
                        key.cancel();
                        sc.close();
                    }
                }else if(key.isReadable()){
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    int read = sc.read(buffer);
                    if(read == -1){
                        log.debug("close channel {}",sc);
                        key.cancel();
                        sc.close();
                    }
                }
            }
        }
    }
}
