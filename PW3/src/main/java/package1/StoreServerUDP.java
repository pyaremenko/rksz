package package1;

import java.io.IOException;
import java.net.*;

import static package1.Decryptor.decrypt;
import static package1.Encryptor.encrypt;
import static package1.Processor.process;

public class StoreServerUDP extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public StoreServerUDP() throws SocketException {
        socket = new DatagramSocket(4445);
        socket.setSoTimeout(5000);
    }

    public void run() {
        try {
            running = true;
            while (running) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    Message dec = decrypt(packet.getData());
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    Message message = process(dec);
                    byte[] enc = encrypt(message);

                    packet = new DatagramPacket(enc, enc.length, address, port);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    socket.send(packet);
                }catch (SocketTimeoutException e){
                break;
            }

            }
            socket.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}