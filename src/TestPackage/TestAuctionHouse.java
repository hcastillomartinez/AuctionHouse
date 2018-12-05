package TestPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestAuctionHouse {

    private static String hostConnectionName, homeName;
    private static int portConnectionNumber, homePort;
    private BufferedReader in, stdIn;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        hostConnectionName = args[0];
        homePort = Integer.parseInt(args[1]);
        ServerSocket server = new ServerSocket(homePort);

        while (true) {
            Socket client = server.accept();

        }
    }

}
