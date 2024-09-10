import java.sql.*;
import java.util.Scanner;

public class BankManagementSystem {
    private static Connection connect() {
        // MySQL connection string
        String url = "jdbc:mysql://localhost:3306/bank_db";
        String user = "root";  // replace with your MySQL username
        String password = "";  // replace with your MySQL password
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Connection Failed! " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection conn = connect();

        if (conn != null) {
            System.out.println("Bank Management System\n");
            while (true) {
                System.out.println("1. Open Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Mini Statement");
                System.out.println("5. Change PIN");
                System.out.println("6. Exit");
                System.out.print("Select an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        openAccount(conn, sc);
                        break;
                    case 2:
                        deposit(conn, sc);
                        break;
                    case 3:
                        withdraw(conn, sc);
                        break;
                    case 4:
                        miniStatement(conn, sc);
                        break;
                    case 5:
                        changePIN(conn, sc);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        }
    }

    private static void openAccount(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter your name: ");
            sc.nextLine();  // consume newline
            String name = sc.nextLine();
            System.out.print("Enter initial deposit: ");
            double balance = sc.nextDouble();
            System.out.print("Set a 4-digit PIN: ");
            int pin = sc.nextInt();

            String sql = "INSERT INTO account (name, balance, pin) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, balance);
            pstmt.setInt(3, pin);
            pstmt.executeUpdate();

            System.out.println("Account opened successfully!");
        } catch (SQLException e) {
            System.out.println("Error opening account: " + e.getMessage());
        }
    }

    private static void deposit(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter your account number: ");
            int accNum = sc.nextInt();
            System.out.print("Enter amount to deposit: ");
            double amount = sc.nextDouble();

            String sql = "UPDATE account SET balance = balance + ? WHERE account_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accNum);
            pstmt.executeUpdate();

            recordTransaction(conn, accNum, "Deposit", amount);

            System.out.println("Deposit successful!");
        } catch (SQLException e) {
            System.out.println("Error depositing money: " + e.getMessage());
        }
    }

    private static void withdraw(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter your account number: ");
            int accNum = sc.nextInt();
            System.out.print("Enter your 4-digit PIN: ");
            int pin = sc.nextInt();
            System.out.print("Enter amount to withdraw: ");
            double amount = sc.nextDouble();

            String pinCheck = "SELECT balance, pin FROM account WHERE account_number = ?";
            PreparedStatement pstmtCheck = conn.prepareStatement(pinCheck);
            pstmtCheck.setInt(1, accNum);
            ResultSet rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                if (rs.getInt("pin") == pin) {
                    if (rs.getDouble("balance") >= amount) {
                        String sql = "UPDATE account SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setDouble(1, amount);
                        pstmt.setInt(2, accNum);
                        pstmt.executeUpdate();

                        recordTransaction(conn, accNum, "Withdraw", amount);
                        System.out.println("Withdrawal successful!");
                    } else {
                        System.out.println("Insufficient balance.");
                    }
                } else {
                    System.out.println("Invalid PIN.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error withdrawing money: " + e.getMessage());
        }
    }

    private static void miniStatement(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter your account number: ");
            int accNum = sc.nextInt();

            String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY date DESC LIMIT 5";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accNum);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Last 5 transactions:");
            while (rs.next()) {
                System.out.println(rs.getString("date") + " | " + rs.getString("type") + " | " + rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving mini-statement: " + e.getMessage());
        }
    }

    private static void changePIN(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter your account number: ");
            int accNum = sc.nextInt();
            System.out.print("Enter your current 4-digit PIN: ");
            int oldPin = sc.nextInt();
            System.out.print("Enter your new 4-digit PIN: ");
            int newPin = sc.nextInt();

            String pinCheck = "SELECT pin FROM account WHERE account_number = ?";
            PreparedStatement pstmtCheck = conn.prepareStatement(pinCheck);
            pstmtCheck.setInt(1, accNum);
            ResultSet rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                if (rs.getInt("pin") == oldPin) {
                    String sql = "UPDATE account SET pin = ? WHERE account_number = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, newPin);
                    pstmt.setInt(2, accNum);
                    pstmt.executeUpdate();

                    System.out.println("PIN changed successfully!");
                } else {
                    System.out.println("Invalid current PIN.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error changing PIN: " + e.getMessage());
        }
    }

    private static void recordTransaction(Connection conn, int accNum, String type, double amount) {
        try {
            String sql = "INSERT INTO transactions (account_number, type, amount) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, accNum);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error recording transaction: " + e.getMessage());
        }
    }
}
