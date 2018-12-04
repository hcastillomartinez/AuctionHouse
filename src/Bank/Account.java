package Bank;

import Agent.Agent;

import java.io.Serializable;

/**
 * Bank Account Class
 * @author Daniel Miller
 */
public class Account implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private Integer accountNumber;
    private double balance, pendingBalance;
    private String name;

    /**
     * Constructor for Account.
     *
     * @param accountNumber the account number
     * @param balance the account balance
     * @param pendingBalance the pending account balance
     */
    public Account(String name, int accountNumber, double balance, double pendingBalance){
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
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

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public synchronized void setAccountNumber(int number) {
        this.accountNumber = number;
    }


    public String getName() {
        return name;
    }

    /**
     * An overridden toString method for debugging purposes.
     *
     * @return
     */
    @Override
    public String toString() {
        return "accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", pendingBalance=" + pendingBalance;
    }
}
