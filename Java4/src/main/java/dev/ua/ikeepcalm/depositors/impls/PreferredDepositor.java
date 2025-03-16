package dev.ua.ikeepcalm.depositors.impls;

import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.depositors.Depositor;

public class PreferredDepositor extends Depositor {

    public PreferredDepositor(int id, String name, Bank bank) {
        super(id, name, bank);
    }

    public String getPriority() {
        return "High";
    }

    @Override
    public float initBonus() {
        return 0.4f;
    }

}
