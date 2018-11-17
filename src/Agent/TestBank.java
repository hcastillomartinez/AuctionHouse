package Agent;

import AuctionHouse.AuctionHouse;
import Bank.Account;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TestBank {

    private ArrayList<Account> userAccounts;
    private ArrayList<AuctionHouse> auctionHouses;
    private int accountNumber = 0;

    private static int portNumber;

    public TestBank() {
        userAccounts = new ArrayList<>();
        auctionHouses = new ArrayList<>();
    }

    // private sub class
    private static class ServerThread implements Runnable {

        private Socket client;
        private BufferedReader in, stdIn;
        private PrintWriter out;
        private TestBank bank;

        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        // constructor
        public ServerThread(Socket client, TestBank bank) {
            this.client = client;
            this.bank = bank;

            try {
//                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                out = new PrintWriter(client.getOutputStream(), true);
                stdIn = new BufferedReader(new InputStreamReader(System.in));
                outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(client.getInputStream());
                outputStream.writeObject("You have been connected!");
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        /**
         * Function to close the client from the server.
         */
        private void closeClient() {
            try {
                outputStream.writeObject("Server " + client.getLocalAddress() + " has closed.");
                inputStream.close();
                outputStream.close();
                stdIn.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        @Override
        public void run() {
            String output, input = null;

            try {
                do {
                    input = (String) inputStream.readObject();
                    System.out.println(input);

                    output = stdIn.readLine();
                    if (output != "") {
                        outputStream.writeObject("server: " + output);
                    }
                } while (input != "bye");
                closeClient();
            } catch (IOException io) {
                io.printStackTrace();
            } catch (ClassNotFoundException cnf) {
                cnf.printStackTrace();
            }
        }
    }

    // getting the user accounts
    private ArrayList<Account> getUserAccounts() { return userAccounts; }

    // testing making a new account for the user
    private void makeAccount(int accountSize, Agent agent, TestBank bank) {
        ArrayList<Account> accounts = bank.getUserAccounts();

        Account account = new Account(accountNumber,
                                      accountSize,
                                      accountSize,
                                      agent);
        if (!accounts.contains(account)) {
            accounts.add(account);
        }
    }

    public static void main(String[] args) throws IOException {
        portNumber = Integer.parseInt(args[0]);

        TestBank bankOne = new TestBank();
        ServerSocket server = new ServerSocket(portNumber);

        while (true) {
            Socket client = server.accept();
            ServerThread bank = new ServerThread(client, bankOne);
            (new Thread(bank)).start();
        }
    }
}
