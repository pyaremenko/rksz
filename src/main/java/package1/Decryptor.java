package package1;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

import static package1.Main.*;
import static package1.crc16.*;
public class Decryptor {
    public static Message decrypt(byte[] packet){
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            ByteBuffer wrap = ByteBuffer.wrap(packet);
            byte clientId = wrap.get(1);
            long packetId = wrap.getLong(2);
            int messageLength = wrap.getInt(10);
            short first_crc16 = wrap.getShort(14);

            byte[] header = ByteBuffer.allocate(1 + 1 + 8 + 4).put((byte) 0x13).put(clientId).putLong(packetId).putInt(messageLength).array();
            if (crc16(header) != first_crc16){
                throw new RuntimeException("Invalid crc16");
            }
            byte[] messageBuffer = new byte[messageLength];
            System.arraycopy(packet, 16, messageBuffer, 0,  messageLength);

            if (crc16(messageBuffer) != wrap.getShort(16+messageLength)){
                throw new RuntimeException("Invalid crc16");
            }
            ByteBuffer buffer = ByteBuffer.wrap(messageBuffer);
            int cType = buffer.getInt();
            int bUserId = buffer.getInt();
            byte[] decrypted_message = new byte[messageLength-8];
            buffer.get(8, decrypted_message);
            byte[] resMessage = cipher.doFinal(decrypted_message);
            return new Message(cType, bUserId, resMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
