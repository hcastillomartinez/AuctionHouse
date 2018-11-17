package Agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class TestAuctionHouse {

    private static String hostConnectionName, homeName;
    private static int portConnectionNumber, homePort;
    private BufferedReader in, stdIn;
    private PrintWriter out;

    public static void main(String[] args) {
        hostConnectionName = args[0];
        homePort = Integer.parseInt(args[1]);

    }

}
