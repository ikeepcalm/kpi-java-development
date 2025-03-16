package dev.ua.ikeepcalm.bank.impls;

import dev.ua.ikeepcalm.Main;
import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.entities.holders.Holder;
import dev.ua.ikeepcalm.entities.depositors.Depositor;

public class UniversalBank extends Bank {

    public UniversalBank(Holder depositorHolder) {
        super("Universal Bank", 0.04f, depositorHolder);
    }

    @Override
    public int imitateDeposit(Depositor depositor, int amount, int months) {
        Main.log("Imitating deposit for " + amount + " for " + months + " months.");
        Main.log("This method is implemented in UniversalBank, interest rate is " + interestRate);
        if (depositor.getBonus() > 0) {
            int bonus = (int) (amount * depositor.getBonus() * 2);
            int obtainedAmount = amount + (int) (amount * super.interestRate * months * 1.2) + bonus;
            depositor.setDeposited(depositor.getDeposited() + amount);
            depositor.setObtained(depositor.getObtained() + obtainedAmount);
            return obtainedAmount;
        } else {
            int obtainedAmount = amount + (int) (amount * super.interestRate * months);
            depositor.setDeposited(depositor.getDeposited() + amount);
            depositor.setObtained(depositor.getObtained() + obtainedAmount);
            return obtainedAmount;
        }
    }

}
