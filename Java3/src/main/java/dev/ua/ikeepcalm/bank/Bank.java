package dev.ua.ikeepcalm.bank;

import dev.ua.ikeepcalm.Main;
import dev.ua.ikeepcalm.entities.holders.Holder;
import dev.ua.ikeepcalm.entities.holders.impls.DepositorHolder;
import dev.ua.ikeepcalm.entities.depositors.Depositor;

public abstract class Bank {

    protected final String name;
    protected final float interestRate;
    protected Holder depositorHolder;

    public Bank(String name) {
        Main.log(name + " bank created.");
        this.name = name;
        this.interestRate = initInterestRate();
        this.depositorHolder = new DepositorHolder();
    }

    public Bank(String name, float interestRate) {
        Main.log(name + " bank created.");
        this.name = name;
        this.interestRate = interestRate;
        this.depositorHolder = new DepositorHolder();
    }

    public Bank(String name, float interestRate, Holder depositorHolder) {
        Main.log(name + " bank created.");
        this.name = name;
        this.interestRate = interestRate;
        this.depositorHolder = depositorHolder;
    }

    public abstract int imitateDeposit(Depositor depositor, int amount, int months);

    public float initInterestRate() {
        return 0.02f;
    }

}
