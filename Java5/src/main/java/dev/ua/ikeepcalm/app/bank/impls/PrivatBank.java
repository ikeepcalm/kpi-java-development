package dev.ua.ikeepcalm.app.bank.impls;

import dev.ua.ikeepcalm.BankApp;
import dev.ua.ikeepcalm.app.bank.Bank;

public class PrivatBank extends Bank {

    public PrivatBank() {
        super("Privat24");
    }

    @Override
    public int imitateDeposit(int amount, int months, float depositorBonus) {
        BankApp.log("Imitating deposit for " + amount + " for " + months + " months.");
        BankApp.log("This method is implemented in PrivatBank, interest rate is " + interestRate);
        if (depositorBonus > 0) {
            return amount + (int) (amount * super.interestRate * months) + (int) (amount * depositorBonus * 3);
        } else {
            return amount + (int) (amount * super.interestRate * months);
        }
    }

    @Override
    public float initInterestRate() {
        return 0.06f;
    }

    public void sayHello() {
        BankApp.log("Hello from PrivatBank!", BankApp.LogColor.BLUE);
    }
}
