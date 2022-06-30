package package1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static package1.Decryptor.decrypt;
import static package1.Encryptor.encrypt;
import static package1.Processor.process;

public class StoreServerTCP extends Thread{
    private ServerSocket serverSocket;

    public void startServer(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(5000);

        Socket client_socket;
        while (true) {
            try {
                client_socket = serverSocket.accept();
            } catch (SocketTimeoutException e) {
                serverSocket.close();
                break;
            }
            new StoreClientTcpHandler(client_socket).start();
        }
    }

    @Override
    public void run() {
        StoreServerTCP server = new StoreServerTCP();
        try {
            server.startServer(1488);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class StoreClientTcpHandler extends Thread {
        private Socket clientSocket;
        private DataOutputStream dout;
        private DataInputStream din;

        public StoreClientTcpHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                dout = new DataOutputStream(clientSocket.getOutputStream());
                din = new DataInputStream(clientSocket.getInputStream());

                while (true) {
                    int MessageLength = din.readInt();
                    if (MessageLength == 0)
                        break;
                    byte[] message = new byte[MessageLength];
                    din.readFully(message, 0, message.length);
                    Message dec = decrypt(message);
                    Message processed = process(dec);
                    byte[] enc = encrypt(processed);
                    dout.writeInt(enc.length);
                    dout.write(enc);
                }
                din.close();
                dout.close();
                clientSocket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

