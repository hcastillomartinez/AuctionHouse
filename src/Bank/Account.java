package Bank;

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
    private boolean isAgent;

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

    /**
     * Constructor for Account.
     *
     * @param accountNumber the account number
     * @param balance the account balance
     * @param pendingBalance the pending account balance
     * @param isAgent if it is an agent
     */
    public Account(String name, int accountNumber, double balance, double pendingBalance, boolean isAgent){
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
        this.isAgent = isAgent;
    }


    /*****************************************/
    /**synchronized getters and setters here**/
    /*****************************************/

    /**
     * Gets the balance of the account
     * @return
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account
     * @param balance
     */
    public synchronized void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the pending balance of the account
     * @return
     */
    public double getPendingBalance() {
        return pendingBalance;
    }

    /**
     * Sets the pending balance of the account
     * @param pendingBalance
     */
    public synchronized void setPendingBalance(double pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    /**
     * Gets the account number
     * @return
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number
     * @param number
     */
    public synchronized void setAccountNumber(int number) {
        this.accountNumber = number;
    }

    /**
     * Gets the name of the account owner
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for isAgent
     */
    public boolean isAgent() {
        return isAgent;
    }

    /**
     * Setter for isAgent
     */
    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    /**
     * An overridden toString method for debugging and display purposes.
     */
    @Override
    public String toString() {
        return "accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", pendingBalance=" + pendingBalance;
    }
}
