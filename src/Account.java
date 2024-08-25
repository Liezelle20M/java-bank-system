

abstract class Account implements BankingOperations {
    protected int accountNumber;
    protected double balance;


    protected static double transactionFee = 0.01; // 1% transaction fee
    protected static double interestRate = 0.05; // 5% interest rate pa

    public static void setTransactionFee(double fee) {
        transactionFee = fee;
    }

   
    public static void setInterestRate(double rate) {
        interestRate = rate;
    }

    // Constructor
    public Account(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }


     public int getAccountNumber() {
        return accountNumber;
    }
   
    public double getBalance() {
        return balance;
    }

   
    public void applyInterest() {
        balance *= (1 + interestRate);
    }

    //@Override
    public void deposit(double amount) {
        balance += amount;
}


    //@Override
    public abstract void withdraw(double amount) throws InsufficientFundsException;

   // @Override
    public abstract void transferFunds(Account recipient, double amount) throws InsufficientFundsException;

// Interface for banking operations
interface BankingOperations {
    double checkBalance();
    void deposit(double amount);
    void withdraw(double amount) throws InsufficientFundsException, IllegalArgumentException;
    void transferFunds(Account recipient, double amount) throws InsufficientFundsException;
}

//For insufficient funds
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

public void depositForTransfer(double amount) {
    balance += amount;

}

}