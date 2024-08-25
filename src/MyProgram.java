import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Base64;
import java.util.Random;

public class MyProgram {

    static String username = "root";
    static String passsword = "lieMMA20@";
    static String db = "jdbc:mysql://localhost:3306/Banking_system";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("WELCOME TO MZANZI BANKING SYSTEM");
        System.out.println("1. Register");
        System.out.println("2. Login ");
        System.out.println("3. Exit");
        System.out.println("Choose an option : ");
        int option = sc.nextInt();

        switch (option) {
            case 1:
                insert();
                break;
            case 2:
                try {
                    Display();
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                System.out.println("You have exited the program");
                break;
            default:
                System.out.println("Wrong input");
        }
    }

    

    public static void insert() throws NoSuchAlgorithmException {
        try {
            sc.nextLine(); 
            String lname, fname, password;
            System.out.println("Enter name: ");
            fname = sc.nextLine();
            System.out.println("Enter Surname: ");
            lname = sc.nextLine();
            System.out.println("Enter password: ");
            password = sc.nextLine();
            String hashedPassword = hashPassword(password);
            System.out.println(password);
            System.out.println(hashedPassword);

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(db, username, passsword)) {
                String myquery = "INSERT INTO Bank (FirstName, LastName, Password) VALUES(?,?,?)";
                try (PreparedStatement st = con.prepareStatement(myquery)) {
                    st.setString(1, fname);
                    st.setString(2, lname);
                    st.setString(3, hashedPassword);
                    st.executeUpdate();
                    System.out.println("Registered successfully.");
                }
            }
        } catch (SQLException | ClassNotFoundException r) {
            r.printStackTrace();
        }
    }

    public static void Display() throws InsufficientFundsException, NoSuchAlgorithmException {
        try {
            sc.nextLine(); 
            String lname, fname, password;
            System.out.println("Enter name: ");
            fname = sc.nextLine();
            System.out.println("Enter Surname: ");
            lname = sc.nextLine();
            System.out.println("Enter password: ");
            password = sc.nextLine();
            String hashedPassword = hashPassword(password);

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(db, username, passsword)) {
                String myquery = "SELECT * FROM bank WHERE FirstName=? AND LastName=? AND Password=?";
                try (PreparedStatement st = con.prepareStatement(myquery)) {
                    st.setString(1, fname);
                    st.setString(2, lname);
                    st.setString(3, hashedPassword);
                    try (ResultSet resultSet = st.executeQuery()) {
                        if (resultSet.next()) {
                            System.out.println("-- Main Menu --");
                            System.out.println("1. Create a new account");
                            System.out.println("2. View existing accounts");
                            System.out.println("3. Perform transactions");
                            System.out.println("4. Logout");
                            System.out.println("Choose an option : ");
                            int choice = sc.nextInt();

                            switch (choice) {
                                case 1:
                                    createNewAccount(con, fname,lname,hashedPassword);
                                    break;
                                case 2:
                                    viewExistingAccounts(con, fname,hashedPassword);
                                    break;
                                case 3:
                                    performTransactions(con, fname);
                                    break;
                                case 4:
                                    System.out.println("You have logged out");
                                    break;
                                default:
                                    System.out.println("Invalid Choice");
                            }
                        } else {
                            System.out.println("User does not exist in the database.");
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createNewAccount(Connection con, String fname, String lname, String hashedPassword) {
        try {
            sc.nextLine();
            Random random = new Random();
            int accountNo = random.nextInt(900000) + 100000;
            int bal = random.nextInt(9000) + 1000;
            System.out.println("What type of account is it?");
            String type = sc.nextLine();

            String deleteQuery = "DELETE FROM Bank WHERE AccountNumber = 0 AND Balance = 0";
            try (PreparedStatement deleteSt = con.prepareStatement(deleteQuery)) {
                deleteSt.executeUpdate();

            }
            String query = "INSERT INTO bank (AccountNumber, Balance, Type, FirstName, LastName, Password) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement st = con.prepareStatement(query)) {
                st.setLong(1, accountNo);
                st.setLong(2, bal);
                st.setString(3, type);
                st.setString(4, fname);
                st.setString(5, lname );
                st.setString(6, hashedPassword);
                st.executeUpdate();
                System.out.println("New account created successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewExistingAccounts(Connection con, String fname, String hashedPassword) {
        try {
            String query = "SELECT * FROM bank WHERE FirstName = ? AND Password=? ";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, fname);
                preparedStatement.setString(2, hashedPassword);
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while (result.next()) {
                        String resultName = result.getString("FirstName");
                        String resultSurname = result.getString("LastName");
                        int resultAccountNo = result.getInt("AccountNumber");
                        String resultType = result.getString("Type");
                        int resultBalance = result.getInt("Balance");
                        System.out.println("Name: " + resultName + ", Surname: " + resultSurname + ", Account number: " + resultAccountNo + ", has a " + resultType + ", with R " + resultBalance);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void performTransactions(Connection con, String fname) throws InsufficientFundsException {
        try {
            sc.nextLine(); 
            System.out.println("Choose a transaction:");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Funds");

            int ch = sc.nextInt();
            sc.nextLine(); 

            SavingsAccount sAccount = getAccountDetails(fname);
            CheckingAccount cAccount = getAccountDetailsCheckingAccount(fname);

            System.out.println("Verify account type: Savings Account or Checking Account");
            String typeAc = sc.nextLine();

           
           
            if ("Savings Account".equals(typeAc)) {
                switch (ch) {
                    case 1:
                        System.out.println("Checking Balance...");
                        System.out.println("Balance: " + sAccount.getBalance());
                        break;
                    case 2:
                        System.out.println("Depositing Money...");
                        System.out.println("Enter amount to deposit:");
                        double depositAmount = sc.nextDouble();
                        sAccount.deposit(depositAmount);
                        System.out.println("Amount deposited successfully. New balance: " + sAccount.getBalance());
                        double newBalanceSaving = sAccount.balance;
                        updateBalanceInDatabase(sAccount.getAccountNumber(), newBalanceSaving);
                        break;
                    case 3:
                         System.out.println("Withdrawing Money...");
                        System.out.println("Enter amount to withdraw:");
                        double withdrawAmount = sc.nextDouble();
                        sAccount.withdraw(withdrawAmount);
                        System.out.println("Amount withdrawn successfully. New balance: " + sAccount.getBalance());
                        double newBalanceSavingW = sAccount.balance;
                        updateBalanceInDatabase(sAccount.getAccountNumber(), newBalanceSavingW);
                        break;
                    case 4:
                        System.out.println("Transferring Funds...");
                        System.out.println("Enter amount to transfer:");
                        double transferAmount = sc.nextDouble();
                        System.out.println("Enter account number to transfer to:");
                        int transferacc = sc.nextInt();
                       
                        try {
                            // Retrieve the recipient account from AccountManager
                            
                            Account recipientAccount = AccountManager.getAccountByNumber(con,transferacc);
                            
                            if (recipientAccount == null) {
                                System.out.println("Recipient account not found.");
                                break;
                            }
                    
                            // Perform the transfer
                            sAccount.transferFunds(recipientAccount, transferAmount);
                    
                            
                            System.out.println("Amount transferred successfully. New balance: " + sAccount.getBalance());
                    
                            // Update balance in the database for both accounts
                            updateBalanceInDatabase(sAccount.getAccountNumber(), sAccount.getBalance());
                            updateBalanceInDatabase(recipientAccount.getAccountNumber(), recipientAccount.getBalance());
                    
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } else if ("Checking Account".equals(typeAc)) {
                switch (ch) {
                    case 1:
                        System.out.println("Checking Balance...");
                        System.out.println("Balance: " + cAccount.getBalance());
                        break;
                    case 2:
                        System.out.println("Depositing Money...");
                        System.out.println("Enter amount to deposit:");
                        double depositAmount = sc.nextDouble();
                        cAccount.deposit(depositAmount);
                        System.out.println("Amount deposited successfully. New balance: " + cAccount.getBalance());
                        double newBalancee = cAccount.balance;
                        updateBalanceInDatabase(cAccount.getAccountNumber(), newBalancee);

                        break;

                    case 3:
                        System.out.println("Withdrawing Money...");
                       System.out.println("Enter amount to withdraw:");
                       double withdrawAmount = sc.nextDouble();
                       sAccount.withdraw(withdrawAmount);
                       System.out.println("Amount withdrawn successfully. New balance: " + cAccount.getBalance());
                       double newBalanceSavingW = cAccount.balance;
                       updateBalanceInDatabase(cAccount.getAccountNumber(), newBalanceSavingW);
                       break;
                   case 4:
                       System.out.println("Transferring Funds...");
                       System.out.println("Enter amount to transfer:");
                       double transferAmount = sc.nextDouble();
                       System.out.println("Enter account number to transfer to:");
                       int transferacc = sc.nextInt();
                      
                       try {
                           // Retrieve the recipient account from AccountManager
                           
                           Account recipientAccount = AccountManager.getAccountByNumber(con,transferacc);
                           
                           if (recipientAccount == null) {
                               System.out.println("Recipient account not found.");
                               break;
                           }
                   
                           // Perform the transfer
                           sAccount.transferFunds(recipientAccount, transferAmount);
                   
                          
                           System.out.println("Amount transferred successfully. New balance: " + sAccount.getBalance());
                   
                           // Update balance in the database for both accounts
                           updateBalanceInDatabase(cAccount.getAccountNumber(), cAccount.getBalance());
                           updateBalanceInDatabase(recipientAccount.getAccountNumber(), recipientAccount.getBalance());
                   
                       } catch (IllegalArgumentException e) {
                           System.out.println("Error: " + e.getMessage());
                       }
                       break;
                    
                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Invalid account type.");
            }
        
            sc.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateBalanceInDatabase(int accountNumber, double balance) {
        try (Connection conn = DriverManager.getConnection(db, username, passsword)) {
            String updateQuery = "UPDATE bank SET Balance = ? WHERE AccountNumber = ?";
            try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                updateStatement.setDouble(1, balance);  
                updateStatement.setInt(2, accountNumber);
                updateStatement.executeUpdate();  
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
    public static SavingsAccount getAccountDetails(String fname ) {
		SavingsAccount sAccount = null;
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(db, username, passsword);
			String sql = "SELECT AccountNumber, Balance FROM Bank WHERE FirstName=?";
			
			try (PreparedStatement pt = conn.prepareStatement(sql)) {
				pt.setString(1, fname);
				ResultSet result = pt.executeQuery();
	
				if (result.next()) {
					// Retrieve by column name
					int accountNumber = result.getInt("AccountNumber");
					double balance = result.getDouble("Balance");
	
					// Create SavingsAccount object with data from database
					sAccount = new SavingsAccount(accountNumber, balance);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		return sAccount;
	}

	public static CheckingAccount getAccountDetailsCheckingAccount(String fname) {
		CheckingAccount cAccount = null; 
	
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(db, username, passsword);
			String sql = "SELECT AccountNumber, Balance FROM Bank WHERE FirstName=?";
			
			try (PreparedStatement pt = conn.prepareStatement(sql)) {
				pt.setString(1, fname);
				ResultSet result = pt.executeQuery();
	
				if (result.next()) {
					// Retrieve by column name
					int accountNumber = result.getInt("AccountNumber");
					double balance = result.getDouble("Balance");
	
					// Create SavingsAccount object with data from database
					cAccount = new CheckingAccount(accountNumber, balance);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		return cAccount;
	}

    static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
	
}




class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}



