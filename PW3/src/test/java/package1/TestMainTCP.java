package package1;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;

public class TestMainTCP {
    StoreServerTCP server;
    int PORT = 1488;
    Goods berries = new Goods(1, 1,120, "add 100");
    Goods berries2 = new Goods(1, 1,120, "delete 1");
    Message message = new Message(4, 1, berries);
    Message message2 = new Message(4, 1, berries2);

    Goods OK = new Goods(1,1,1, "OK");
    Message OKK = new Message(message.getcType(), message.getbUserId(), OK);

    @Before
    public void setup() throws IOException {
        server = new StoreServerTCP();
        server.start();
    }

    @Test
    public void SingleClientSendsMessages() throws IOException, InterruptedException {
        System.out.println("Single Client: ");
        StoreClientTCP client = new StoreClientTCP();
        client.startConnection(PORT );
        Message response = client.sendMessage(message);
        System.out.println("tcp resp : " + response);
        Assertions.assertEquals(OKK, response);
        client.stopConnection();

    }


    @Test
    public void MultiThreadClientSendMessages() throws InterruptedException{
        System.out.println("MultiClient: ");
        Thread a = new Thread(() -> {
            try {
                StoreClientTCP client = new StoreClientTCP();
                client.startConnection(PORT) ;
                Message response = client.sendMessage(message);
                System.out.println("resp : " + response);
                Assertions.assertEquals(OKK, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        a.start();

        Thread b = new Thread(() -> {

            try {
                StoreClientTCP client = new StoreClientTCP();
                client.startConnection(PORT);
                Message response = client.sendMessage(message2);
                System.out.println("resp : " + response);
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
//    @Test
//    void dec_encr_test() throws InterruptedException, IOException {
//
//        Goods berries = new Goods(1, 1,120, "pass");
//        Message message = new Message(4, 1, berries);
//        System.out.println("mess : " + message);
//        StoreClientTCP client = new StoreClientTCP();
//        client.startConnection("127.0.0.1",8080);
//
//        Message resp = client.sendMessage(message);
//        System.out.println("resp : " + resp);
//
//
//
//    }
}
