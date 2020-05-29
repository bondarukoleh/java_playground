package threads;

public class LostUpdate {
    public static void main(String[] args) {
        Balance balance = new Balance();
        UpdateBalance updateBalance = new UpdateBalance(balance);

        Thread updater1 = new Thread(updateBalance);
        Thread updater2 = new Thread(updateBalance);

        updater1.setName("updater1");
        updater2.setName("updater2");

        updater1.start();
        updater2.start();
    }
}

class Balance {
    private int balance;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}

class UpdateBalance implements Runnable {
    private final Balance balance;

    public UpdateBalance(Balance balance) {
        this.balance = balance;
    }

    public void updateBalance(int amount) {
        // do some stuff that doesn't need to be sync
        synchronized (this){ // without sync and you'll get balance 5 instead of 6 in the end
            Logger.log("Going to update the balance");
            int currentBalance = this.balance.getBalance();
            Logger.log("Current balance is - " + currentBalance);
            // Doing some checks
            currentBalance += amount;
            this.balance.setBalance(currentBalance);
            Logger.log("Updated balance is - " + this.balance.getBalance());
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            updateBalance(1);
        }
    }
}