package package1.Processors;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

import static package1.Processors.Product.toProduct;

public class Message {
    private int cType;
    private int bUserId;
    private byte[] message;

    public Message(int cType, int bUserId, byte[] message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public Message(int cType, int bUserId, Product product) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = product.toBytes();

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
        if (message[0] == "OK".getBytes()[0] && message[1] == "OK".getBytes()[1])
            return "Message{" +
                    "cType = " + cType +
                    ", bUserId = " + bUserId +
                    ", message = " + new String(message) +
                    '}';

        return "Message{" +
                "cType = " + cType +
                ", bUserId = " + bUserId +
                ", message = " + toProduct(message) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return cType == message1.cType && bUserId == message1.bUserId && Arrays.equals(message, message1.message);
    }
}


