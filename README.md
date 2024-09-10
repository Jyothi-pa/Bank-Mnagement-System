# Bank Management System - ATM Simulator
## Project Description

The Bank Management System is an ATM Simulator built using Java and MySQL. It provides core functionalities such as account opening, deposit, withdrawal, mini-statement generation, and PIN change, simulating basic ATM operations in a console-based application.
## Features

    Account Management: Open new accounts with an initial deposit.
    Deposit: Add money to an existing account.
    Withdrawal: Withdraw money from an account by entering the correct PIN.
    Mini Statement: View the last 5 transactions of an account.
    PIN Change: Change the account's PIN securely.

## Technologies Used

    Programming Language: Java
    Database: MySQL
    JDBC: For connecting Java to the MySQL database.

## Prerequisites

Before running this project, ensure you have the following installed:

    Java Development Kit (JDK) (version 8 or above)
    MySQL (version 5.7 or above)
    MySQL Connector for Java (JDBC driver)

# Installation and Setup
## Step 1: Clone the Repository
 git clone https://github.com/your-username/BankManagementSystem.git
 cd BankManagementSystem

## Step 2: Set Up MySQL Database
Open MySQL and create a new database called bank_db
## CREATE DATABASE bank_db;
USE bank_db;
Create the required tables:
CREATE TABLE account (
    account_number INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    balance DECIMAL(10, 2),
    pin INT
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number INT,
    type VARCHAR(50),
    amount DECIMAL(10, 2),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES account(account_number)
);

## Step 3: Configure the Database Connection

In the BankManagementSystem.java file, update the MySQL connection credentials:

String url = "jdbc:mysql://localhost:3306/bank_db";
String user = "your-username";  // Replace with your MySQL username
String password = "your-password";  // Replace with your MySQL password


## Step 4: Compile and Run

    Add the MySQL Connector/J (JDBC driver) to your classpath. Download it from here if you haven't already.
    Compile and run the Java program:
javac BankManagementSystem.java
java BankManagementSystem


# Usage

Once the application is running, you will be presented with a menu offering various ATM operations:

  1.Open Account: Input your name, initial deposit, and a 4-digit PIN to create a new account.
  2.Deposit: Enter your account number and the amount to deposit money into the account.
  3.Withdraw: Input your account number, PIN, and the amount to withdraw funds.
  4.Mini Statement: View the last 5 transactions of your account.
  5.Change PIN: Update your account's PIN by entering the current and new PIN.

  # Contributing

If you would like to contribute to this project, feel free to create a pull request. Any enhancements or bug fixes are welcome!

# License

This project is open-source and available under the MIT License.


