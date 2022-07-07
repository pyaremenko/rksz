package package1.Processors;

import package1.db.DBProcessor;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static package1.Processors.Product.toProduct;

public class Processor{
    private Message stopMessage;
    private int wLen;

    public static Message process(Message message, DBProcessor dbProcessor) {
        try {
            switch (message.getcType()){
                case 1 :
                    Product a = toProduct(message.getMessage());
                    dbProcessor.createProducts(a.getName(), a.getType(), a.getAmount(), a.getPrice());
                    break;
                case 2 :
                    int id = Integer.parseInt(new String(message.getMessage()));
                    dbProcessor.showProductDataById(id);
                    break;
                case 3 :
                    Product c = toProduct(message.getMessage());
                    dbProcessor.updateProduct(c.getId(), c.getName(), c.getType(), c.getAmount(), c.getPrice());
                    break;
                case 4 :
                    Product d = toProduct(message.getMessage());
                    dbProcessor.deleteProduct(d.getId());
                    break;
                case 5 :
                    int category = Integer.parseInt(new String(message.getMessage()));
                    dbProcessor.listProductByCategoryCriteria(category);
                    break;
                default:
                    break;
            }
            return new Message(0, 0, "OK".getBytes());
//            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
