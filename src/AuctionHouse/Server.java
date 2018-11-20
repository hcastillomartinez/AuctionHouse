package AuctionHouse;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private InetAddress serverName;
    //private List<AgentClient> agentClients;

    public Server(String port){
        try{
            //agentClients=new LinkedList<>();
            serverName=InetAddress.getLocalHost();
            serverSocket=new ServerSocket(Integer.parseInt(port));
            serverSocket.setSoTimeout(10000);
        }catch(IOException i){
            System.out.println(i);
        }
    }


    @Override
    public void run(){
        while(true){
            try {
                System.out.println("Waiting for Client");
                Socket client = serverSocket.accept();
            }catch(IOException i){
                System.out.println(i);
            }

        }
    }
}
