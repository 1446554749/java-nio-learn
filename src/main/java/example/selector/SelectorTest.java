package example.selector;

import java.io.IOException;
import java.nio.channels.*;

public class SelectorTest {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        int interest = SelectionKey.OP_ACCEPT;
        SelectionKey key = channel.register(selector,interest,new Object());
        //get interestSet
        int interestSet = key.interestOps();
        boolean isInterestedInAccept = SelectionKey.OP_ACCEPT == (interestSet & SelectionKey.OP_ACCEPT);
        System.out.println(isInterestedInAccept);
        //get readySet
        int readySet = key.readyOps();
        boolean isReadyInConnect = SelectionKey.OP_CONNECT == (readySet & SelectionKey.OP_CONNECT);
        System.out.println(isReadyInConnect);
        //get channel and selector
        SelectableChannel channel1 = key.channel();
        Selector selector1 = key.selector();
        //attach objects
        key.attach(new Object());
        Object attachment = key.attachment();

    }
}
