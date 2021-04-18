package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;


/**
 * Created by Niloofar on 4/24/2020.
 */
public class Server {
    DatagramSocket serverSocket;
    ArrayList<User> user;

    public Server() throws SocketException {
        serverSocket = new DatagramSocket(20000);
        user = new ArrayList<>();
    }

    public void run() throws IOException {
        while_lable :
        while(true) {
            byte[] buffer = new byte[524288];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(packet);
            String[] tokens = new String(packet.getData()).split(" ");
            String response = null;

            //System.out.println(new String(packet.getData()));

            if(tokens[0].equals("register")) {
                for(User u : user){
                    if(u.username.equals(tokens[1])){
                        response = "taken";
                        break;
                    }
                }
                if(response != "taken") {
                    response = "Successfully registered :D ";
                    //System.out.println(tokens[1].trim() + ' ' +tokens[2].trim());
                    user.add(new User(tokens[1] , packet.getAddress() , Integer.parseInt(tokens[2].trim())));
                }

                DatagramPacket sendPacket = new DatagramPacket(response.getBytes(), response.getBytes().length,
                        packet.getAddress(), packet.getPort());
                serverSocket.send(sendPacket);
            }

            else if(tokens[0].equals("connect")){
                boolean found = false;
                for(User u : user){
                    if(tokens[1].trim().equals(u.username.trim())){
                        DatagramPacket sendPacket = new DatagramPacket(u.toString().getBytes(), u.toString().getBytes().length ,
                                packet.getAddress(), packet.getPort());
                        serverSocket.send(sendPacket);
                        found = true;
                        break ;
                    }
                }
                if(!found){
                    DatagramPacket sendPacket = new DatagramPacket("NotFound".getBytes(), "NotFound".toString().getBytes().length,
                            packet.getAddress(), packet.getPort());
                    serverSocket.send(sendPacket);
                }
            }

        }
    }

    public static void main(String[] args){
        try {
            Server server = new Server();
            server.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
