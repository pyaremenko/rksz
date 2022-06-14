package package1;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.SecureRandom;


public class PackageBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Key SECRET_KEY = getSecureRandomKey( 128);
    private static final Key SECRET_KEY2 = SECRET_KEY;
    private static final String CIPHER = "AES";
    private static Key getSecureRandomKey( int keySize) {
        byte[] secureRandomKeyBytes = new byte[keySize / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secureRandomKeyBytes);
        return new SecretKeySpec(secureRandomKeyBytes, CIPHER);
    }
    static byte[] encodeMessage(Message message){
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encodedText = cipher.doFinal(message.getMessage());
            return ByteBuffer.allocate(4 + 4 + encodedText.length)
                    .putInt(message.getcType())
                    .putInt(message.getbUserId())
                    .put(encodedText)

                    .array();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static byte[] encode(Message command) {
        try{
            byte[] message = encodeMessage(command);
            byte[] header = ByteBuffer.allocate( 1 + 1 + 8 + 4)
                    .put((byte) 0x13)
                    .put((byte) 15)
                    .putLong(123L)
                    .putInt(message.length)
                    .array();

            byte[] res = ByteBuffer.allocate(header.length + 2 + message.length + 2)
                    .put(header)
                    .putShort(crc16(header))
                    .put(message)
                    .putShort(crc16(message))
                    .array();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Message decode(Package pack){
        try {
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY2);

            ByteBuffer wrap = ByteBuffer.wrap(pack.getPackageBytes());
            byte clientId = wrap.get(1);
            long packetId = wrap.getLong(2);
            int messageLength = wrap.getInt(10);
            short first_crc16 = wrap.getShort(14);
            byte[] header = ByteBuffer.allocate(1 + 1 + 8 + 4).put((byte) 0x13).put(clientId).putLong(packetId).putInt(messageLength).array();
            if (crc16(header) != first_crc16){
                throw new RuntimeException("Invalid crc16");
            }
            byte[] messageBuffer = new byte[messageLength];
            System.arraycopy(pack.getPackageBytes(), 16, messageBuffer, 0,  messageLength);

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

//    public static void main(String[] args) {
//
//        String mess = "putin zdoh";
//        byte[] encryptedMessageBytes = mess.getBytes();
//        Message message = new Message(1, 1, encryptedMessageBytes);
//        System.out.println("new command : " + message);
//        Package p = new Package(encode(message));
//        System.out.println("encoded pack : " + p);
//        Message message1 = decode(p);
//        System.out.println("decoded message : " + message1);
//    }
    public static short crc16(byte[] bytes) {

        int[] table = {
                0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
                0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
                0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
                0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
                0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
                0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
                0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
                0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
                0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
                0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
                0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
                0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
                0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
                0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
                0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
                0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
                0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
                0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
                0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
                0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
                0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
                0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
                0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
                0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
                0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
                0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
                0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
                0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
                0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
                0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
                0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
                0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040,
        };


        int crc = 0x0000;
        for (byte b : bytes) {
            crc = (crc >>> 8) ^ table[(crc ^ b) & 0xff];
        }
        return (short) crc;
    }



}
