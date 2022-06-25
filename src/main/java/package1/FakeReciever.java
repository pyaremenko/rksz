package package1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static package1.Decryptor.decrypt;

public class FakeReciever implements Reciever{
    private static ArrayBlockingQueue<byte[]> resMessages;
    private static Message stopMessage;
    public FakeReciever(ArrayBlockingQueue<byte[]> resMessages, Message stopMessage) {
        this.resMessages = resMessages;
        this.stopMessage = stopMessage;
    }

    @Override
    public void receiveMessage() throws InterruptedException {
        while(true){
            byte[] m = resMessages.poll(10, TimeUnit.SECONDS);
            if(m == null)
                break;
            Message dec = decrypt(m);
            if(dec == stopMessage){
                break;
            }
            System.out.println("Recieved : " + dec);
        }
    }



}
