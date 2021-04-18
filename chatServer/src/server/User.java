package server;

import java.net.InetAddress;

/**
 * Created by Niloofar on 4/24/2020.
 */
public class User{
    public String username ;
    public InetAddress address;
    public int portNumber;


    public User(String username, InetAddress address, int portNumber) {
        this.username = username;
        this.address = address;
        this.portNumber = portNumber;
    }

    public String toString(){//overriding the toString() method
        return (address.getHostName().trim() + " " + Integer.toString(portNumber).trim());
    }
}
