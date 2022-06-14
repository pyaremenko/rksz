package package1;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class Message {
    private int cType;
    private int bUserId;
    private byte[] message;


    public Message(int cType, int bUserId, byte[] message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public Message(ByteBuffer byteBuffer, int wLen)
    {
        this.cType = byteBuffer.getInt();
        this.bUserId = byteBuffer.getInt();
        this.message = new byte [wLen - Integer.BYTES * 2];
        byteBuffer.get(message , 0, wLen - Integer.BYTES * 2 );
    }

    public Message(){

    }
    public int getcType() {
        return cType;
    }

    public int getbUserId() {
        return bUserId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setcType(int cType) {
        this.cType = cType;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "package1.Message{" +
                "cType = " + cType +
                ", bUserId = " + bUserId +
                ", message = " + new String(message) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return cType == message1.cType && bUserId == message1.bUserId && Arrays.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(cType, bUserId);
        result = 31 * result + Arrays.hashCode(message);
        return result;
    }
}


