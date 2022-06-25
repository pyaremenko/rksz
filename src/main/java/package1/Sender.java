package package1;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static package1.Encryptor.encrypt;

public class Sender {
    public static void sendMessages(ArrayBlockingQueue<byte[]> messages, List<Goods> goods, Message stopMessage){
        for(int i = 0; i<goods.size(); i++) {
            Message m = new Message(i+1, 1, goods.get(i));
            messages.add(encrypt(m));
            System.out.println("Sent : " + m);
        }
        messages.add(encrypt(stopMessage));

    }
}
