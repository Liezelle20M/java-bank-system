import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AccountManager {
    private List<Account> accounts;
    

    public AccountManager(List<Account> accounts) {
        this.accounts = new ArrayList<>(accounts);
        
    }

    // Load accounts from the database
    public static List<Account> loadAccountsFromDatabase(Connection con) {
        List<Account> accounts= new ArrayList<>();
        String query = "SELECT * FROM Bank"; 

        try (
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int accountNumber = rs.getInt("AccountNumber");
                double balance = rs.getDouble("Balance");
                String type = rs.getString("Type");

                // Create account object based on type
               
                if ("Savings Account".equalsIgnoreCase(type)) {
                    accounts.add(new SavingsAccount(accountNumber, balance));
                } else {
                    accounts.add(new CheckingAccount(accountNumber, balance)); 
                }
                
                
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

 
    public static Account getAccountByNumber(Connection con, int accountNumber) {
        Account account = null;
        String query = "SELECT * FROM Bank WHERE AccountNumber = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double balance = rs.getDouble("Balance");
                    String type = rs.getString("Type");

      
                    if ("Savings Account".equalsIgnoreCase(type)) {
                        account = new SavingsAccount(accountNumber, balance);
                    } else if ("Checking Account".equalsIgnoreCase(type)) {
                        account = new CheckingAccount(accountNumber, balance);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    // Method to remove an account from the list
    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    // Method to get all accounts
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }
}