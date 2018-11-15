package Proxies;

import Agent.Agent;
import AuctionHouse.AuctionHouse;
import Bank.Bank;
import Bank.Account;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Proxies.BankProxy.java is the class that is the mediary between the Bank and the
 * Agent. The class provides higher level functionality to interact with the
 * bank.
 */
public class BankProxy implements Runnable {

    private Bank bank;
    private Account accout;
    
    /**
     * Constructor for the bank proxy.
     * Builds a reference to the bank for bank functionality.
     */
    public BankProxy(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void run() {
        while(true){

        }
    }


    /**
     * Creates and assigns an account to an agent.
     */
    public Account openAccount(int balance, Agent agent){
        return bank.openAccount(balance, agent);
    }

    /**
     * Adds an auction house to the list of auction houses.
     */
    public void addAuctionHouse(AuctionHouse house){
        bank.getAuctionHouses().add(house);
    }

    /**
     * Gets list of agents for a auction house.
     */
    public ArrayList<Agent> getAgents() {
        return bank.getAgents();
    }

    /**
     * Gets list of auction houses for a agent.
     */
    public ArrayList<AuctionHouse> getAuctionHouses() {
        return bank.getAuctionHouses();
    }

    /**
     * Handles messages received from Houses and Agents.
     */
    
}
