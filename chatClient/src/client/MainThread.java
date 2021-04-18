package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Niloofar on 4/23/2020.
 */
public class MainThread implements Runnable {

    int mode;

    static ExecutorService pool = Executors.newFixedThreadPool(5);


    public MainThread(int mode) throws IOException {
        this.mode = mode;
    }

    @Override
    public void run() {

        if(mode == 0){
            try {
                DatagramSocket clientSocket = new DatagramSocket();

                while (true) {
                    System.out.println("\nchoose a number : ");
                    System.out.println("1. make a new connection");
                    //System.out.println("2. disconnect a connection");
                    System.out.println("2. send a message to a friend");
                    System.out.println("3. show new messages");

                    BufferedReader consolReader = new BufferedReader(new InputStreamReader(System.in));
                    String input = null;

                    input = consolReader.readLine();
                    if (input.equals("1")) {
                        while_lable :
                        while (true) {
                            System.out.println("to make a new connection enter a username ,and in order to go back type 'exit' : ");
                            String name = consolReader.readLine();

                            if (name.equals("exit"))
                                break;

                            for (User f : Client.friends) {
                                if (f.username.equals(name)) {
                                    System.out.println("you are already connected !");
                                    break while_lable;
                                }
                            }

                            String message = "connect " + name;

                            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length,
                                    InetAddress.getByName("localhost"), 20000);

                            clientSocket.send(packet);

                            byte[] buffer = new byte[524288];
                            DatagramPacket recivedPacket = new DatagramPacket(buffer, buffer.length);
                            clientSocket.receive(recivedPacket);
                            //System.out.println("recived " + new String(recivedPacket.getData()));

                            if (new String(recivedPacket.getData()).trim().equals("NotFound")) {
                                System.out.println("username '" + name + "' does not exist");
                                continue;
                            }

                            String tokens[] = new String(recivedPacket.getData()).trim().split(" ");

                            User new_friend = new User(name, InetAddress.getByName(tokens[0]) ,Integer.parseInt(tokens[1]) );
                            Client.friends.add(new_friend);
                            System.out.println("Connection with " + new_friend.username + " is made , now you can send him/her a message");
                            break;
                        }
                    } else if (input.equals("2")) {
                        if (Client.friends.size() == 0) {
                            System.out.println("You have no connection , make a connection first");
                        } else {
                            while (true) {
                                System.out.println("choose a number : ");
                                for (int i = 0; i < Client.friends.size(); i++)
                                    System.out.println((i + 1) + ". send a message to " + Client.friends.get(i).username);
                                System.out.println((Client.friends.size() + 1) + ".exit");

                                String choice = consolReader.readLine().trim();

                                if (choice.equals(Integer.toString(Client.friends.size() + 1))) {
                                    break;
                                }
                                boolean found = false;
                                for (int i = 1; i <= Client.friends.size(); i++) {
                                    if (choice.equals(Integer.toString(i).trim())) {
                                        found = true;
                                        Socket socket = new Socket(Client.friends.get(i - 1).address , Client.friends.get(i - 1).portNumber);
                                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                        System.out.println("Enter your message for " + Client.friends.get(i - 1).username);
                                       // System.out.println(socket.getPort());
                                        String message = Client.username + " " + consolReader.readLine();
                                        writer.write(message);
                                        writer.flush();
                                        writer.close();
                                        System.out.println("the message has been send successfully.");
                                        break;
                                    }
                                }
                                if (!found) {
                                    System.out.println("Invalid input !");
                                }
                                else
                                    break;
                            }
                        }
                    }else  if(input.equals("3")){

                        if(Client.newMessages.size() != 0) {
                            System.out.println("You have " + Integer.toString(Client.newMessages.size()) + " messages :\n");
                            for (Message m : Client.newMessages) {
                                m.show();
                            }
                        }
                        else
                            System.out.println("You have no new messages.");
                        Client.newMessages = new ArrayList<>();
                    }
                    else{
                        System.out.println("invalid input");
                    }
                }
            }catch (IOException e) {
                    e.printStackTrace();
            }
        }
        else {
             try{
                 while(true) {
                    Socket connectionSocket = Client.serverSocket.accept();
                    MessageThread messageThread = new MessageThread(connectionSocket);
                    pool.execute(messageThread);
                }
            }catch (IOException e) {
                 e.printStackTrace();
             }
        }
    }
}