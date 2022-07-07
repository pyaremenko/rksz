package package1.UDP;

import package1.Processors.Message;

import java.io.IOException;
import java.net.*;

import static package1.Processors.Decryptor.decrypt;
import static package1.Processors.Encryptor.encrypt;

public class StoreClientUDP {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;

    public StoreClientUDP() throws SocketException, UnknownHostException {

        socket = new DatagramSocket();
        socket.setSoTimeout(5000);
        address = InetAddress.getByName("localhost");

    }

    public Message sendMessage(Message message) throws IOException {
        try {
            Message failed = null;
            buf = encrypt(message);

            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            Message received = decrypt(packet.getData());

            if(received == failed){
                System.out.println("Retry...");
                sendMessage(message);
            }

            return received;
        }catch(SocketTimeoutException e){
            throw new RuntimeException(e);
        }
    }
}
