package package1.Processors;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class Message {
    private int cType;
    private int bUserId;
    private byte[] message;
    public Goods goods;

    public Goods getGoods() {
        return goods;
    }

    public void doAction(){
        String[] command = goods.getCmd().split(" ");
        switch (command[0]){
            case "add" :
                goods.addGoods(Integer.parseInt(command[1]));
                break;
            case "delete" :
                goods.deleteGoods(Integer.parseInt(command[1]));
                break;
            case "setPrice":
                goods.setPrice(Integer.parseInt(command[1]));
                break;
        }
    }

    private String getCmd(){
        byte[] cmd = new byte[getMessage().length-8];
        System.arraycopy(message, 12, cmd, 0,  getMessage().length-12);
        return new String(cmd);
    }
    private ByteBuffer wrapper(){
        return ByteBuffer.wrap(getMessage());
    }

    public Message(int cType, int bUserId, byte[] message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public Message(int cType, int bUserId, Goods goods) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.goods = goods;
        this.message = goods.toBytes();

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

    public String messageToString(){
        ByteBuffer buff = wrapper();
        int amount = buff.getInt();
        int type = buff.getInt();
        int price = buff.getInt();
        byte[] name = new byte[getMessage().length-12];
        System.arraycopy(buff.array(), 12, name, 0,  getMessage().length-12);
        return "Goods{" +
                "amount=" + amount +
                ", type=" + type +
                ", price=" + price +
                ", cmd='" + new String(name) + '\'' +
                '}';
    }
    @Override
    public String toString() {
        return "package1.Processors.Message{" +
                "cType = " + cType +
                ", bUserId = " + bUserId +
                ", message = " + messageToString() +
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


