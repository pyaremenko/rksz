package package1.Processors;

import java.nio.ByteBuffer;

public class Product {
    private int id;
    private int amount;
    private int type;
    private int price;
    private String name;

    public Product(int id, int amount, int type, int price, String name) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.price = price;
        this.name = name;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getAmount() { return amount; }

    public void setAmount(int amount) { this.amount = amount; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", amount=" + amount +
                ", type=" + type +
                ", price=" + price +
                ", name='" + name + '\'' +
                '}';
    }

    public byte[] toBytes(){
        return ByteBuffer.allocate(4 + 4 + 4 + 4 + name.getBytes().length)
                .putInt(id)
                .putInt(amount)
                .putInt(type)
                .putInt(price)
                .put(name.getBytes())
                .array();
    }

    public static Product toProduct(byte[] product){
        ByteBuffer buff = ByteBuffer.wrap(product);
        int id = buff.getInt();
        int amount = buff.getInt();
        int type = buff.getInt();
        int price = buff.getInt();
        byte[] name = new byte[product.length - 16];
        System.arraycopy(buff.array(), 16, name, 0,  product.length-16);
        String NAME = new String(name);
        return new Product(id, amount, type, price, NAME);
    }
}
