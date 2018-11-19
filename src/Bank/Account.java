package Bank;

import Agent.Agent;

/**
 * Bank Account Class
 * @author Daniel Miller
 */
public class Account {
    private int id, balance, pendingBalance;
    private Agent agent; //reference to the agent the account belongs to

    public Account(int id, int balance, int pendingBalance, Agent agent){
        this.id = id;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", pendingBalance=" + pendingBalance +
                ", agent=" + agent +
                '}';
    }

    //synchronized getters and setters here
}
