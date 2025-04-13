package dev.ua.ikeepcalm.app.depositors.impls;

import dev.ua.ikeepcalm.BankApp;
import dev.ua.ikeepcalm.app.bank.Bank;
import dev.ua.ikeepcalm.app.depositors.Depositor;

public class PlainDepositor extends Depositor {

    public PlainDepositor(int id, String name, Bank bank) {
        super(id, name, bank);
    }

    public void startBrouhaha() {
        new Runnable() {
            @Override
            public void run() {
                try {
                    BankApp.log("Brouhaha started!", BankApp.LogColor.RED);
                    Thread.sleep(1000);
                    BankApp.log("I will now scream!", BankApp.LogColor.RED);
                    Thread.sleep(1000);
                    for (int i = 0; i < 10; i++) {
                        BankApp.log("Aaaaaaaaaaaaaa!", BankApp.LogColor.RED);
                        Thread.sleep(500);
                    }
                    BankApp.log("Brouhaha ended!", BankApp.LogColor.RED);
                } catch (InterruptedException e) {
                    BankApp.log("Brouhaha interrupted!", BankApp.LogColor.RED);
                }
            }
        }.run();
    }

    public String getPriority() {
        return "Medium";
    }

    @Override
    public float initBonus() {
        return 0;
    }
}
