package AuctionHouse;

import java.io.IOException;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port){
        try{
            serverSocket=new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
        }catch(IOException i){
            System.out.println(i);
        }
    }

    public void waitForClient(){
        try{
            Socket client=serverSocket.accept();
            System.out.println("Connection Established with: "+client.getRemoteSocketAddress());
        }catch(IOException i){
            System.out.println(i);
        }
    }


}
