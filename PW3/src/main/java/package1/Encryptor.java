package package1;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

import static package1.Main.*;
import static package1.crc16.*;

public class Encryptor {
    private static byte[] encryptMessage(Message message){
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            message.doAction();
            byte[] encodedText = cipher.doFinal(message.getMessage());
            byte[] res = ByteBuffer.allocate(4 + 4 + encodedText.length)
                    .putInt(message.getcType())
                    .putInt(message.getbUserId())
                    .put(encodedText)
                    .array();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(Message command){
        try{
            byte[] message = encryptMessage(command);
            byte[] header = ByteBuffer.allocate( 1 + 1 + 8 + 4)
                    .put((byte) 0x13)
                    .put((byte) 15)
                    .putLong(123L)
                    .putInt(message.length)
                    .array();

            byte[] res = ByteBuffer.allocate(header.length + 2 + message.length + 2)
                    .put(header)
                    .putShort(crc16.crc16(header))
                    .put(message)
                    .putShort(crc16.crc16(message))
                    .array();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
