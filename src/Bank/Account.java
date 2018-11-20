package Bank;

import Agent.Agent;

/**
 * Bank Account Class
 * @author Daniel Miller
 */
public class Account {
    private int id, balance, pendingBalance;
    private Agent agent;

    /**
     * Constructor for Account.
     *
     * @param id the account id
     * @param balance the account balance
     * @param pendingBalance the pending account balance
     * @param agent a reference to the agent that owns the account
     */
    public Account(int id, int balance, int pendingBalance, Agent agent){
        this.id = id;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
        this.agent = agent;
    }

    /**
     * An overridden toString method for debugging purposes.
     * @return
     */
    @Override
    public synchronized String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", pendingBalance=" + pendingBalance +
                ", agent=" + agent +
                '}';
    }


    /*****************************************/
    /**synchronized getters and setters here**/
    /*****************************************/
    public synchronized int getBalance() {
        return balance;
    }

    public synchronized void setBalance(int balance) {
        this.balance = balance;
    }

    public synchronized int getPendingBalance() {
        return pendingBalance;
    }

    public synchronized void setPendingBalance(int pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public int getId() {
        return id;
    }

    public Agent getAgent() {
        return agent;
    }
}
