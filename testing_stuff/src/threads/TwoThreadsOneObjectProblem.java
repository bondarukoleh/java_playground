package src.threads;

import threads.Logger;

public class TwoThreadsOneObjectProblem {
    public static void main(String[] args) {
        BankAccount joinBankAccount = new BankAccount();

        Shopping shopping = new Shopping(joinBankAccount);

        Thread johnDay = new Thread(shopping);
        Thread marianDay = new Thread(shopping);

        johnDay.setName("John day");
        marianDay.setName("Maria day");

        johnDay.start();
        marianDay.start();
    }
}


class BankAccount {
    public int money = 100;
    private static int withdrawCount;

    public int getBalance() {
        return money;
    }

    public int withdraw(int amount) {
        BankAccount.withdrawCount++;
        if (money - amount < 0) {
            throw new Error("Withdraw Count - " + BankAccount.withdrawCount + ". Account has " + money + ". You cannot take more!!! Overdrawn!!!");
        }
        money -= amount;
        Logger.log("Withdraw Count - " + BankAccount.withdrawCount + ". Now amount of money - " + money);
        return money;
    }
}

class Shopping implements Runnable {
    private BankAccount bankAccount;

    public Shopping(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            withdraw(30);
        }
    }

    public synchronized void withdraw(int amount) { // remove synchronized, and you'll get the error.
        Logger.log("I'm about to make a withdraw");
        if (this.bankAccount.money >= amount) {
            Logger.log("I think that I can take a money.");
            try {
                Logger.log("I'm about to sleep");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.log("I'm awake and continue to make the withdraw");
            try {
                this.bankAccount.withdraw(amount);
                Logger.log("I have my money!");
            } catch (Error e) {
                Logger.log(e.getMessage());
            }
        } else {
            Logger.log("I cannot take money no more :(");
        }
    }
}