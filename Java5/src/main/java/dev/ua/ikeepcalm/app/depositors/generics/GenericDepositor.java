package dev.ua.ikeepcalm.app.depositors.generics;

import dev.ua.ikeepcalm.app.bank.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GenericDepositor<B extends Bank> {

    private final int id;
    @Setter
    private String name;
    private final B bank;
    private final float bonus;
    private int deposited;
    private int obtained;

    public GenericDepositor() {
        this.id = 0;
        this.name = "John Doe";
        this.deposited = 0;
        this.bank = null;
        this.bonus = initBonus();
        System.out.println("Hello! I am the empty Depositor, did you forget to set my name and id?");
        System.out.println("I am using the Universal Bank by default.");
    }

    public GenericDepositor(int id, String name, B bank) {
        this.id = id;
        this.name = name;
        this.deposited = 0;
        this.bank = bank;
        this.bonus = initBonus();
    }

    public int imitateDeposit(int amount) {
        this.deposited += amount;
        int obtainedAmount = bank.imitateDeposit(amount, 12, bonus);
        this.obtained += obtainedAmount;
        return obtainedAmount;
    }

    public int imitateDeposit(int amount, int months) {
        this.deposited += amount;
        int obtainedAmount = bank.imitateDeposit(amount, months, bonus);
        this.obtained += obtainedAmount;
        return obtainedAmount;
    }

    public abstract float initBonus();
}