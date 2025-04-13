package dev.ua.ikeepcalm.app.bank;

import dev.ua.ikeepcalm.BankApp;

public abstract class Bank {

    protected final String name;
    protected final float interestRate;

    public Bank(String name) {
        BankApp.log(name + " bank created.");
        this.name = name;
        this.interestRate = initInterestRate();
    }

    public abstract int imitateDeposit(int amount, int months, float depositorBonus);

    public float initInterestRate() {
        return 0.02f;
    }

}
