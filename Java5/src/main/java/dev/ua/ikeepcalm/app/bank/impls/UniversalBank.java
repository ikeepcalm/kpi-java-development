package dev.ua.ikeepcalm.app.bank.impls;

import dev.ua.ikeepcalm.BankApp;
import dev.ua.ikeepcalm.app.bank.Bank;

public class UniversalBank extends Bank {

    public UniversalBank() {
        super("Universal Bank");
    }

    public int imitateDeposit(int amount, int months, float depositorBonus) {
        BankApp.log("Imitating deposit for " + amount + " for " + months + " months.");
        BankApp.log("This method is implemented in UniversalBank, interest rate is " + interestRate);
        if (depositorBonus > 0) {
            return amount + (int) (amount * interestRate * months) + (int) (amount * depositorBonus * 2);
        } else {
            return amount + (int) (amount * interestRate * months);
        }
    }

    public void monoCat() {
        BankApp.log("Meow from UniversalBank!", BankApp.LogColor.BLUE);
    }

}
