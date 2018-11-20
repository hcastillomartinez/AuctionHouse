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
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        // constructor
        public ServerThread(Socket client) {
            this.client = client;

            try {
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
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        @Override
        public void run() {
            String output, input = null;

            try {
                do {
                    try {
                        input = (String) inputStream.readObject();
                        System.out.println(input);
    
                        if (input.equalsIgnoreCase("hello")) {
                            outputStream.writeObject("server: hey");
                        } else if (input.equalsIgnoreCase("hows it going")) {
                            outputStream.writeObject("server: good");
                        } else {
                            outputStream.writeObject("server: I am bored");
                        }
                    } catch (EOFException eof) {
                        System.out.println("Client has been closed!");
                        break;
                    } catch (ClassNotFoundException cnf) {
                        cnf.printStackTrace();
                    }
                } while (!input.equalsIgnoreCase("bye"));
                closeClient();
            } catch (IOException io) {
                io.printStackTrace();
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
                                      accountSize);
        if (!accounts.contains(account)) {
            accounts.add(account);
        }
    }

    public static void main(String[] args) throws IOException {
        portNumber = Integer.parseInt(args[0]);

        ServerSocket server = new ServerSocket(portNumber);

        while (true) {
            Socket client = server.accept();
            ServerThread bank = new ServerThread(client);
            (new Thread(bank)).start();
        }
    }
}
