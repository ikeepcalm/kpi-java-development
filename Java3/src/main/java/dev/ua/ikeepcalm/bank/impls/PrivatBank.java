package dev.ua.ikeepcalm.bank.impls;

import dev.ua.ikeepcalm.Main;
import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.entities.holders.Holder;
import dev.ua.ikeepcalm.entities.depositors.Depositor;

public class PrivatBank extends Bank {

    public PrivatBank(Holder depositorHolder) {
        super("Privat24", 0.06f, depositorHolder);
    }

    @Override
    public int imitateDeposit(Depositor depositor, int amount, int months) {
        Main.log("Imitating deposit for " + amount + " for " + months + " months.");
        Main.log("This method is implemented in PrivatBank, interest rate is " + interestRate);
        if (depositor.getBonus() > 0) {
            int bonus = (int) (amount * depositor.getBonus() * 3);
            int obtainedAmount = amount + (int) (amount * super.interestRate * months) + bonus;
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

    @Override
    public float initInterestRate() {
        return 0.06f;
    }
}
