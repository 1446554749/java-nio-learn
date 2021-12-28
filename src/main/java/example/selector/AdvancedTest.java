package example.selector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class AdvancedTest {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        int interest = SelectionKey.OP_ACCEPT;
        SelectionKey selectionKey = channel.register(selector, interest);

//        selector.select();
//        selector.select(5000);
//        selector.selectNow();

        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while (keyIterator.hasNext()){
            SelectionKey key = keyIterator.next();
            if(key.isAcceptable()){
                System.out.println();
            }else if(key.isConnectable()){
                System.out.println();
            }
            keyIterator.remove();
        }

    }
}
