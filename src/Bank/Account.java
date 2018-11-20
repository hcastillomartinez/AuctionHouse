package Bank;

import Agent.Agent;

/**
 * Bank Account Class
 * @author Daniel Miller
 */
public class Account {
    private int id;
    private double balance, pendingBalance;

    /**
     * Constructor for Account.
     *
     * @param id the account id
     * @param balance the account balance
     * @param pendingBalance the pending account balance
     */
    public Account(int id, double balance, double pendingBalance){
        this.id = id;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
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
                '}';
    }


    /*****************************************/
    /**synchronized getters and setters here**/
    /*****************************************/
    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void setBalance(double balance) {
        this.balance = balance;
    }

    public synchronized double getPendingBalance() {
        return pendingBalance;
    }

    public synchronized void setPendingBalance(double pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public int getId() {
        return id;
    }
}
