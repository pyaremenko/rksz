package package1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static package1.PackageBuilder.decode;
import static package1.PackageBuilder.encode;

public class PackageBuilderTest {
    @Test
    void dec_encr_test() {
        String mess = "putin zdoh";
        byte[] encryptedMessageBytes = mess.getBytes();
        Message message = new Message(1, 1, encryptedMessageBytes);
        System.out.println("new command : " + message);
        Package p = new Package(encode(message));
        System.out.println("encoded pack : " + p);
        Message message1 = decode(p);

        Assertions.assertEquals(message.getcType(), message1.getcType());
        Assertions.assertEquals(message.getbUserId(), message1.getbUserId());
        Assertions.assertTrue(Arrays.equals(message.getMessage(), message1.getMessage()));
    }
}
