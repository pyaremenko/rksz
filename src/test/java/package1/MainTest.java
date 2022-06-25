package package1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static package1.Sender.sendMessages;


public class MainTest {
    @Test
    void dec_encr_test() throws InterruptedException {

        Goods berries = new Goods(1, 1,120, "pass");
        Goods meat = new Goods(5, 2,160, "pass");
        Goods vegetables = new Goods(10, 3,30, "pass");
        Goods stop = new Goods(10, 3,30, "pass");
        Message stopMessages = new Message(4, 1, stop);
        List<Goods> goods = new ArrayList<>();
        goods.add(berries);
        goods.add(meat);
        goods.add(vegetables);
        ArrayBlockingQueue<byte[]> messages = new ArrayBlockingQueue<>(goods.size()  + 1);
        ArrayBlockingQueue<byte[]> resMessages = new ArrayBlockingQueue<>(goods.size()  + 1);

        for(int i =0; i < 5; i++){
            new Processor(messages, resMessages, stopMessages);
        }

        FakeReciever fr = new FakeReciever(resMessages, stopMessages);
        sendMessages(messages, goods, stopMessages);
        fr.receiveMessage();

    }
}
