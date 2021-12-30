import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestMain {
    static String generate() {
        Random random = new Random();
        final byte[] buffer = new byte[5];
        random.nextBytes(buffer);
        List<String> list = new ArrayList<>();
        for (byte b:buffer){
            list.add(Integer.toBinaryString(b & 0xFF));
        }
        System.out.println(list);
//        return BaseEncoding.base64Url().omitPadding().encode(buffer); // or base32()
        return BaseEncoding.base64Url().encode(buffer); // or base32()
    }

    public static void main(String[] args){
//        System.out.println(f(2));
        System.out.println(generate());
    }
}
