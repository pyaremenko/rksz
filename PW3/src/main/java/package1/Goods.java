package package1;

import java.nio.ByteBuffer;

public class Goods {

    private int amount;
    private int type;
    private int price;

    public String getCmd() {
        return cmd;
    }

    private String cmd;

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Goods(int amount, int type, int price, String cmd) {
        this.amount = amount;
        this.type = type;
        this.price = price;
        this.cmd = cmd;
    }

    public int getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return cmd;
    }

    private void setAmount(int amount) {
        this.amount = amount;
    }

    public void addGoods(int am){
        setAmount(this.amount + am);
    }

    public void deleteGoods(int am){
        setAmount(this.amount - am);
    }

    @Override
    public String toString() {
        return "Goods{" +
                "amount=" + amount +
                ", type=" + type +
                ", price=" + price +
                ", cmd='" + cmd + '\'' +
                '}';
    }

    public byte[] toBytes(){
        return ByteBuffer.allocate(4 + 4 + 4 + cmd.getBytes().length)
                .putInt(amount)
                .putInt(type)
                .putInt(price)
                .put(cmd.getBytes())
                .array();
    }
}
