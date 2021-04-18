package client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Niloofar on 4/23/2020.
 */
public class MessageThread implements Runnable {
    Socket connectionSocket;
    BufferedReader reader ;


    public MessageThread(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    }

    @Override
    public void run() {
        //System.out.println("i'm running");
        while (true) {
            try {
               // System.out.println("i'm here");
                String line = reader.readLine();
                if(line == null || line.length() == 0)
                    break;

                Client.newMessages.add(new Message(line));

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            reader.close();
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}