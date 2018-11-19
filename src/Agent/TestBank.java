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

//    public static void main(String[] args) throws IOException {
//        portNumber = Integer.parseInt(args[0]);
//
//        TestBank bankOne = new TestBank();
//        ServerSocket server = new ServerSocket(portNumber);
//
//        while (true) {
//            Socket client = server.accept();
//            ServerThread bank = new ServerThread(client, bankOne);
//            (new Thread(bank)).start();
//        }
//    }
}
