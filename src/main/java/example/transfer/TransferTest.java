package example.transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;

public class TransferTest {
    public static void main(String[] args) throws IOException {
        URL fromFileURL = TransferTest.class.getResource("/fromFile.txt");
        assert fromFileURL!=null;
        RandomAccessFile fromFile = new RandomAccessFile(fromFileURL.getPath(), "rw");
        FileChannel fromChannel = fromFile.getChannel();

        URL toFileURL = TransferTest.class.getResource("/toFile.txt");
        assert toFileURL!=null;
        RandomAccessFile toFile = new RandomAccessFile(toFileURL.getPath(), "rw");
        FileChannel      toChannel = toFile.getChannel();

        long position = 0;
        long count    = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);
    }
}
