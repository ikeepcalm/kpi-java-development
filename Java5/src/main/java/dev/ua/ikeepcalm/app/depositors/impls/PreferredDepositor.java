package dev.ua.ikeepcalm.app.depositors.impls;

import dev.ua.ikeepcalm.app.bank.Bank;
import dev.ua.ikeepcalm.app.depositors.Depositor;

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
