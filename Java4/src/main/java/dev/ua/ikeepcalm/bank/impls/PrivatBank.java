package dev.ua.ikeepcalm.bank.impls;

import dev.ua.ikeepcalm.Main;
import dev.ua.ikeepcalm.bank.Bank;

public class PrivatBank extends Bank {

    public PrivatBank() {
        super("Privat24");
    }

    @Override
    public int imitateDeposit(int amount, int months, float depositorBonus) {
        Main.log("Imitating deposit for " + amount + " for " + months + " months.");
        Main.log("This method is implemented in PrivatBank, interest rate is " + interestRate);
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
        Main.log("Hello from PrivatBank!", Main.Color.BLUE);
    }
}
