package Agent;

import Bank.Bank;

/**
 * BankProxy.java is the class that is the mediary between the Bank and the
 * Agent. The class provides higher level functionality to interact with the
 * bank.
 */
public class BankProxy {

    private Bank bank;
    
    /**
     * Constructor for the bank proxy.
     * Builds a reference to the bank for bank functionality.
     */
    public BankProxy() {
        this.bank = new Bank();
    }
    
}
