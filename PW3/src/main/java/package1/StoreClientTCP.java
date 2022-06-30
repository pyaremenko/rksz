package package1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static package1.Decryptor.decrypt;
import static package1.Encryptor.encrypt;

public class StoreClientTCP {
    private Socket clientSocket;
    private DataOutputStream dout;
    private DataInputStream din;

    private int failed = 1;

    public void startConnection(int port) throws IOException {
        try {
            clientSocket = new Socket(InetAddress.getLocalHost().getHostAddress(), port);
            dout = new DataOutputStream(clientSocket.getOutputStream());
            din = new DataInputStream(clientSocket.getInputStream());
        } catch(ConnectException e){
            System.out.println("Failed tries to connect : " + failed);
            failed++;
            startConnection(port);
            if(failed == 5)
                throw new RuntimeException("Unable to connect");
        }
    }

    public Message sendMessage(Message message) throws IOException {
        byte[] enc = encrypt(message);
        dout.writeInt(enc.length);
        dout.write(enc);

        int MessageLength = din.readInt();
        byte[] m = new byte[MessageLength];
        din.readFully(m, 0, m.length);
        Message dec = decrypt(m);
        return dec;
    }

    public void stopConnection() {
        try {
            dout.writeInt(0);
            din.close();
            dout.close();
            clientSocket.close();

        }catch (IOException e){
            throw new RuntimeException(e);
    }

}



}