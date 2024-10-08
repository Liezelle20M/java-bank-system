# Java Bank System

Welcome to the Java Bank System repository! 🏦 This project is a console-based banking application built with Java, integrating SQL for database management. It allows users to register, log in, create bank accounts, and perform various transactions like withdrawing funds, checking balances, making deposits, and transferring money between accounts. The project demonstrates the use of Object-Oriented Programming (OOP) principles alongside SQL for data management.

# Features

- User Registration: New users can register by providing their details, which are stored securely in the SQL database.
- User Login: Existing users can log in using their credentials to access their bank accounts, with authentication handled by SQL queries.
- Account Management:
  Create Account: Users can create a new bank account linked to their profile, with account details stored in the database.
  Check Balance: Users can view their current account balance, fetched directly from the database.
  Deposit Funds: Users can deposit money into their bank account, with the transaction recorded in the SQL database.
  Withdraw Funds: Users can withdraw money from their bank account, with the balance updated in the database.
  Transfer Funds: Users can transfer money between their own accounts or to other users' accounts, with both accounts' balances updated in the database.
- OOP Principles: The project is structured using OOP principles such as encapsulation, inheritance, and abstraction, while CRUD operations are managed through SQL.

# Set Up the Database:

- Create a SQL database and run the provided SQL scripts to set up the necessary tables (e.g., users, accounts, transactions).
- Configure your database connection settings in the src/main/resources/db.properties file.


# Project Structure

- Database: SQL database used for storing user data, account details, and transaction records.
- SavingsAccount and Checking: A class representing the bank account with methods to interact with the database for checking balance, depositing, withdrawing, and transferring funds.
- BankSystem (myProgram): The main class that handles user registration, login, and overall system management by interacting with the database.
- AccountManger: Lists all the accounts in the database
  
