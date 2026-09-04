import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ================= TRANSACTION CLASS =================
class Transaction {

    private String type;
    private double amount;
    private double balanceAfter;
    private String description;
    private LocalDateTime dateTime;

    public Transaction(String type, double amount,
                       double balanceAfter, String description) {

        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return dateTime.format(formatter)
                + " | " + type
                + " | Rs. " + amount
                + " | Balance: Rs. " + balanceAfter
                + " | " + description;
    }
}


// ================= ACCOUNT CLASS =================
class Account {

    private String accountId;
    private String userId;
    private String pin;
    private double balance;

    private ArrayList<Transaction> transactions;

    public Account(String accountId, String userId,
                   String pin, double balance) {

        this.accountId = accountId;
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;

        transactions = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean verifyPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    // Deposit
    public void deposit(double amount) {

        balance = balance + amount;

        transactions.add(
                new Transaction(
                        "Deposit",
                        amount,
                        balance,
                        "Cash deposited"
                )
        );
    }

    // Withdraw
    public boolean withdraw(double amount) {

        if (amount <= 0 || amount > balance) {
            return false;
        }

        balance = balance - amount;

        transactions.add(
                new Transaction(
                        "Withdraw",
                        amount,
                        balance,
                        "Cash withdrawn"
                )
        );

        return true;
    }

    // Transfer
    public boolean transferTo(Account receiver, double amount) {

        if (receiver == null ||
                receiver == this ||
                amount <= 0 ||
                amount > balance) {

            return false;
        }

        balance = balance - amount;
        receiver.balance = receiver.balance + amount;

        transactions.add(
                new Transaction(
                        "Transfer",
                        amount,
                        balance,
                        "Transferred to "
                                + receiver.accountId
                )
        );

        receiver.transactions.add(
                new Transaction(
                        "Transfer Received",
                        amount,
                        receiver.balance,
                        "Received from "
                                + accountId
                )
        );

        return true;
    }
}


// ================= BANK CLASS =================
class Bank {

    private HashMap<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {

        accounts.put(
                account.getUserId(),
                account
        );
    }

    // Login authentication
    public Account authenticate(String userId, String pin) {

        Account account = accounts.get(userId);

        if (account != null &&
                account.verifyPin(pin)) {

            return account;
        }

        return null;
    }

    // Find account using account ID
    public Account findAccount(String accountId) {

        for (Account account : accounts.values()) {

            if (account.getAccountId()
                    .equals(accountId)) {

                return account;
            }
        }

        return null;
    }
}


// ================= ATM CLASS =================
class ATM {

    private Bank bank;
    private Scanner scanner;

    public ATM(Bank bank) {

        this.bank = bank;
        scanner = new Scanner(System.in);
    }

    public void start() {

        System.out.println(
                "================================="
        );

        System.out.println(
                "       WELCOME TO JAVA ATM"
        );

        System.out.println(
                "================================="
        );

        Account account = login();

        if (account == null) {

            System.out.println(
                    "\nAccess denied."
            );

            System.out.println(
                    "Too many incorrect attempts."
            );

            System.out.println(
                    "Thank you for using Java ATM."
            );

            return;
        }

        System.out.println(
                "\nLogin successful!"
        );

        System.out.println(
                "Welcome " + account.getUserId()
        );

        showMenu(account);
    }


    // ================= LOGIN =================
    private Account login() {

        int maxAttempts = 3;

        for (int attempt = 1;
             attempt <= maxAttempts;
             attempt++) {

            System.out.print(
                    "\nEnter User ID: "
            );

            String userId =
                    scanner.nextLine();

            System.out.print(
                    "Enter PIN: "
            );

            String pin =
                    scanner.nextLine();

            Account account =
                    bank.authenticate(
                            userId,
                            pin
                    );

            if (account != null) {
                return account;
            }

            System.out.println(
                    "Invalid User ID or PIN."
            );

            int remaining =
                    maxAttempts - attempt;

            if (remaining > 0) {

                System.out.println(
                        "Attempts remaining: "
                                + remaining
                );
            }
        }

        return null;
    }


    // ================= MAIN MENU =================
    private void showMenu(Account account) {

        while (true) {

            System.out.println(
                    "\n================================="
            );

            System.out.println(
                    "           MAIN MENU"
            );

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "1. Transaction History"
            );

            System.out.println(
                    "2. Withdraw"
            );

            System.out.println(
                    "3. Deposit"
            );

            System.out.println(
                    "4. Transfer"
            );

            System.out.println(
                    "5. Quit"
            );

            System.out.println(
                    "================================="
            );

            int choice =
                    readInt("Enter your choice: ");


            switch (choice) {

                case 1:
                    showHistory(account);
                    break;

                case 2:
                    withdraw(account);
                    break;

                case 3:
                    deposit(account);
                    break;

                case 4:
                    transfer(account);
                    break;

                case 5:

                    System.out.println(
                            "\nThank you for using Java ATM."
                    );

                    System.out.println(
                            "Have a nice day!"
                    );

                    return;

                default:

                    System.out.println(
                            "Invalid choice."
                    );
            }
        }
    }


    // ================= TRANSACTION HISTORY =================
    private void showHistory(Account account) {

        System.out.println(
                "\n========== TRANSACTION HISTORY =========="
        );

        ArrayList<Transaction> history =
                account.getTransactions();

        if (history.isEmpty()) {

            System.out.println(
                    "No transactions yet."
            );

        } else {

            for (Transaction t : history) {

                System.out.println(t);
            }
        }

        System.out.printf(
                "Current Balance: Rs. %.2f%n",
                account.getBalance()
        );
    }


    // ================= WITHDRAW =================
    private void withdraw(Account account) {

        double amount =
                readAmount(
                        "Enter withdrawal amount: "
                );

        if (amount <= 0) {

            System.out.println(
                    "Amount must be greater than zero."
            );

            return;
        }

        // Balance check
        if (amount > account.getBalance()) {

            System.out.println(
                    "Insufficient Funds"
            );

            System.out.printf(
                    "Available Balance: Rs. %.2f%n",
                    account.getBalance()
            );

            return;
        }

        if (account.withdraw(amount)) {

            System.out.println(
                    "Withdrawal successful."
            );

            System.out.printf(
                    "Remaining Balance: Rs. %.2f%n",
                    account.getBalance()
            );
        }
    }


    // ================= DEPOSIT =================
    private void deposit(Account account) {

        double amount =
                readAmount(
                        "Enter deposit amount: "
                );

        if (amount <= 0) {

            System.out.println(
                    "Amount must be greater than zero."
            );

            return;
        }

        account.deposit(amount);

        System.out.println(
                "Deposit successful."
        );

        System.out.printf(
                "Updated Balance: Rs. %.2f%n",
                account.getBalance()
        );
    }


    // ================= TRANSFER =================
    private void transfer(Account sender) {

        System.out.print(
                "Enter recipient account ID: "
        );

        String receiverId =
                scanner.nextLine();

        Account receiver =
                bank.findAccount(receiverId);

        if (receiver == null) {

            System.out.println(
                    "Recipient account not found."
            );

            return;
        }

        if (receiver == sender) {

            System.out.println(
                    "Cannot transfer to your own account."
            );

            return;
        }

        double amount =
                readAmount(
                        "Enter transfer amount: "
                );

        if (amount <= 0) {

            System.out.println(
                    "Amount must be greater than zero."
            );

            return;
        }

        // Balance check
        if (amount > sender.getBalance()) {

            System.out.println(
                    "Insufficient Funds"
            );

            return;
        }

        if (sender.transferTo(
                receiver,
                amount)) {

            System.out.println(
                    "Transfer successful."
            );

            System.out.printf(
                    "Transferred: Rs. %.2f%n",
                    amount
            );

            System.out.printf(
                    "Updated Balance: Rs. %.2f%n",
                    sender.getBalance()
            );
        }
    }


    // ================= INTEGER INPUT =================
    private int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }


    // ================= AMOUNT INPUT =================
    private double readAmount(String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine();

            try {

                return Double.parseDouble(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid amount."
                );
            }
        }
    }
}


// ================= MAIN CLASS =================
public class Main {

    public static void main(String[] args) {

        Bank bank = new Bank();


        // Account 1
        Account account1 =
                new Account(
                        "ACC1001",
                        "varsha01",
                        "1234",
                        10000
                );


        // Account 2
        Account account2 =
                new Account(
                        "ACC1002",
                        "rahul01",
                        "5678",
                        8000
                );


        // Add accounts to bank
        bank.addAccount(account1);
        bank.addAccount(account2);


        // Start ATM
        ATM atm = new ATM(bank);

        atm.start();
    }
}