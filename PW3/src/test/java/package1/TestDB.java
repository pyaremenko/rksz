package package1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import package1.Processors.Product;
import package1.Processors.Message;
import package1.UDP.StoreClientUDP;
import package1.UDP.StoreServerUDP;
import package1.db.DBProcessor;

import java.io.IOException;
import java.util.List;

public class TestDB {
    StoreServerUDP server;

//1 - create products (input Product in message)
//2 - show product by id (input id)
//3 - update product (input product for update in message)
//4 - delete product (input product for delete in message)
//5 - list product by category (input category in message)

    Product berries = new Product(1, 34, 1, 220, "Berries");
    Product chicken = new Product(2, 12, 2, 22, "Chicken");

    Message OKK = new Message(0, 0, "OK".getBytes());
    DBProcessor dbProcessor = new DBProcessor();
    @Before

    public void setup() throws IOException {
        dbProcessor.initialization();
        server = new StoreServerUDP(dbProcessor);
        server.start();
    }



    @Test
    public void create() throws IOException{
        System.out.print("create: ");
        StoreClientUDP client = new StoreClientUDP();
        Message createB = new Message(1, 1, berries);
        Message createC = new Message(1, 1, berries);

        Message response = client.sendMessage(createB);
        Message response1 = client.sendMessage(createC);
        Message resp2 = client.sendMessage(new Message(2,2,"1".getBytes()));
        {
            List<Object> objectInDB = dbProcessor.returnProductDataById(berries.getId());
            Assertions.assertEquals(berries.getId(), objectInDB.get(0));
            Assertions.assertEquals(berries.getName(), objectInDB.get(1));
            Assertions.assertEquals(berries.getType(), objectInDB.get(2));
            Assertions.assertEquals(berries.getAmount(), objectInDB.get(3));
            Assertions.assertEquals(berries.getPrice(), objectInDB.get(4));
        }
    }

    @Test
    public void showByID() throws IOException{
        System.out.print("show: ");
        StoreClientUDP client = new StoreClientUDP();
        Message show = new Message(2, 1, "1".getBytes());
        Message response = client.sendMessage(show);
    }

    @Test
    public void update() throws IOException{
        System.out.println("update: ");
        StoreClientUDP client = new StoreClientUDP();
        String id = String.valueOf(chicken.getId());

        Product updatedChicken = new Product(2, 15, 2, 223, "Chicken");
        Message update = new Message(3, 2, updatedChicken);

        System.out.print("before : ");
        Message before = new Message(2, 1, id.getBytes());
        Message show = client.sendMessage(before);

        Message response = client.sendMessage(update);

        System.out.print("After : ");
        Message showUpdated = new Message(2, 1, id.getBytes());
        Message shoW = client.sendMessage(showUpdated);

        {
            List<Object> objectInDB = dbProcessor.returnProductDataById(updatedChicken.getId());
            Assertions.assertEquals(updatedChicken.getId(), objectInDB.get(0));
            Assertions.assertEquals(updatedChicken.getName(), objectInDB.get(1));
            Assertions.assertEquals(updatedChicken.getType(), objectInDB.get(2));
            Assertions.assertEquals(updatedChicken.getAmount(), objectInDB.get(3));
            Assertions.assertEquals(updatedChicken.getPrice(), objectInDB.get(4));
        }
    }

    @Test
    public void delete() throws IOException{
        System.out.print("delete: ");
        StoreClientUDP client = new StoreClientUDP();
        Message delete = new Message(4, 2, berries);
        Message response = client.sendMessage(delete);
        System.out.println(response);
        {
            Assertions.assertNull(dbProcessor.returnProductDataById(1));
        }
    }

    @Test
    public void listByCriteria() throws IOException{
        System.out.print("criteria = category_type\nlist by criteria: ");
        StoreClientUDP client = new StoreClientUDP();
        Message list = new Message(5, 2, "2".getBytes());
        Message response = client.sendMessage(list);
        {
            Assertions.assertEquals(OKK, response);
        }
    }

    @After
    public void tearDown() throws InterruptedException {
        server.join();
    }


}
