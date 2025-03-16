package dev.ua.ikeepcalm.bank.impls;

import dev.ua.ikeepcalm.Main;
import dev.ua.ikeepcalm.bank.Bank;

public class UniversalBank extends Bank {

    public UniversalBank() {
        super("Universal Bank");
    }

    public int imitateDeposit(int amount, int months, float depositorBonus) {
        Main.log("Imitating deposit for " + amount + " for " + months + " months.");
        Main.log("This method is implemented in UniversalBank, interest rate is " + interestRate);
        if (depositorBonus > 0) {
            return amount + (int) (amount * interestRate * months) + (int) (amount * depositorBonus * 2);
        } else {
            return amount + (int) (amount * interestRate * months);
        }
    }

    public void monoCat() {
        Main.log("Meow from UniversalBank!", Main.Color.BLUE);
    }

}
