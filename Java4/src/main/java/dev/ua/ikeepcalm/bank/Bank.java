package dev.ua.ikeepcalm.bank;

import dev.ua.ikeepcalm.Main;

public abstract class Bank {

    protected final String name;
    protected final float interestRate;

    public Bank(String name) {
        Main.log(name + " bank created.");
        this.name = name;
        this.interestRate = initInterestRate();
    }

    public abstract int imitateDeposit(int amount, int months, float depositorBonus);

    public float initInterestRate() {
        return 0.02f;
    }

}
