class SavingsAccount extends Account {

    // Constructor
    public SavingsAccount(int accountNumber, double balance) {
        super(accountNumber, balance);
    }

    // Implementing interface methods
    //@Override
    public double checkBalance() {
        return balance;
    } 

    @Override
    public void deposit(double amount) {
        balance += amount - (amount * transactionFee);
    }

    public int getAccountNumber() {
        return accountNumber;
    }


    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (balance >= amount) {
            balance -= amount + (amount * transactionFee);
           
        } else {
            throw new InsufficientFundsException("Insufficient funds to withdraw.");
        }
    }

     @Override
    public void transferFunds(Account recipient, double amount) throws InsufficientFundsException {
        if (balance >= amount) {
            balance -= amount + (amount * transactionFee);
            recipient.depositForTransfer(amount); // Use depositForTransfer to avoid applying transaction fee again
        } else {
            throw new InsufficientFundsException("Insufficient funds to transfer.");
        }
    }
}