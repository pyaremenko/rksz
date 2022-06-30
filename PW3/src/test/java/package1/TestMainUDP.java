package package1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

public class TestMainUDP {
    StoreServerUDP server;
    int PORT = 1488;
    Goods berries = new Goods(1, 1,120, "add 100");
    Goods berries2 = new Goods(1, 1,120, "delete 1");
    Message message = new Message(4, 1, berries);
    Message message2 = new Message(4, 1, berries2);


    Goods OK = new Goods(1,1,1, "OK");
    Message OKK = new Message(message.getcType(), message.getbUserId(), OK);

    @Before
    public void setup() throws IOException {
        server = new StoreServerUDP();
        server.start();
    }

    @Test
    public void SingleClientSendsMessages() throws IOException, InterruptedException {
        System.out.println("Single Client: ");
        StoreClientUDP client = new StoreClientUDP();
        Message response = client.sendMessage(message);
        System.out.println("upd resp : " + response);
        Assertions.assertEquals(OKK, response);
    }

    @Test
    public void MultiThreadClientSendMessages() throws InterruptedException{
        System.out.println("MultiClient: ");
        Thread a = new Thread(() -> {
            try {
                StoreClientUDP client = new StoreClientUDP();
                Message response = client.sendMessage(message);
                System.out.println("upd resp : " + response);
                Assertions.assertEquals(OKK, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        a.start();

        Thread b = new Thread(() -> {
            try {
                StoreClientUDP client = new StoreClientUDP();
                Message response = client.sendMessage(message2);
                System.out.println("upd resp : " + response);
                Assertions.assertEquals(OKK, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        b.start();

        a.join();
        b.join();
    }

    @After
    public void tearDown() throws InterruptedException {
        server.join();
    }


}
