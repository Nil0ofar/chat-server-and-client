package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Niloofar on 4/24/2020.
 */

public class Client {
    DatagramSocket clientSocket;
    ExecutorService pool;
    static ServerSocket serverSocket;
    static ArrayList<User> friends ;
    static ArrayList<Message> newMessages ;
    static String username ;

    public Client() throws IOException {
        clientSocket = new DatagramSocket();
        friends = new ArrayList<>();
        newMessages = new ArrayList<>();
        pool = Executors.newFixedThreadPool(2);
        serverSocket = new ServerSocket(0);

    }

    public void register() throws IOException {
        BufferedReader consolReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("please Enter a username to register : ");
        while (true) {
            String input = consolReader.readLine();
            String sendMessage = "register " + input + " " + Integer.toString(serverSocket.getLocalPort());
            System.out.println(sendMessage);

            DatagramPacket packet = new DatagramPacket(sendMessage.getBytes(), sendMessage.getBytes().length,
                    InetAddress.getByName("localhost"), 20000);

            clientSocket.send(packet);

            byte[] buffer = new byte[1024];
            DatagramPacket recivedPacket = new DatagramPacket(buffer, buffer.length);

            clientSocket.receive(recivedPacket);

            String recivedMessage = new String(recivedPacket.getData());

            if (recivedMessage.startsWith("Successfully")) {
                username = input;
                System.out.println(recivedMessage);
                break;
            } else {
                System.out.print("The username is already Taken!\nPlease try another username : ");
            }
        }
    }

    public void run() throws IOException {

        MainThread mainThread = new MainThread(0);
        pool.execute(mainThread);
        mainThread = new MainThread(1);
        pool.execute(mainThread);
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.register();
            client.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

