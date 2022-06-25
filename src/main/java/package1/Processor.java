package package1;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static package1.Decryptor.decrypt;
import static package1.Encryptor.encrypt;

public class Processor extends Thread{
    private ArrayBlockingQueue<byte[]> messages;
    private ArrayBlockingQueue<byte[]> resMessages;
    private Message stopMessage;
    private int wLen;

    public Processor(ArrayBlockingQueue<byte[]> messages,
                     ArrayBlockingQueue<byte[]> resMessages,
                     Message stopMessage){
        this.messages = messages;
        this.stopMessage = stopMessage;
        this.resMessages = resMessages;
        this.wLen = messages.size();
        this.start();
    }
    @Override
    public void run(){
        super.run();
        for(int i = 0; i < 10; i++){
            synchronized (messages){
                try{
                    while(true) {
                        byte[] m = messages.poll(10, TimeUnit.SECONDS);
                        if(m == null)
                            break;
                        Message dec = decrypt(m);
                        System.out.println("Processed : " + dec);
                        if(dec == stopMessage ){
                            while(wLen != resMessages.size()-1)
                                messages.wait();
                            messages.put(m);
                            break;
                        }
                        Goods ok = new Goods(5, 1, 100, "OK");
                        Message okk = new Message(5, 1, ok);
                        System.out.println("OK");
                        resMessages.add(encrypt(okk));

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
