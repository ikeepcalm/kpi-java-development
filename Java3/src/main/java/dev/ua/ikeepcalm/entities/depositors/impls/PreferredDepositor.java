package dev.ua.ikeepcalm.entities.depositors.impls;

import dev.ua.ikeepcalm.entities.depositors.Depositor;

public class PreferredDepositor extends Depositor {

    public PreferredDepositor(int id, String name) {
        super(id, name);
    }

    public String getPriority() {
        return "High";
    }

    @Override
    public float initBonus() {
        return 0.4f;
    }

}
