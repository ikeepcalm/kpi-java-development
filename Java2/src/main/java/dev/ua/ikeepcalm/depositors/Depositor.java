package dev.ua.ikeepcalm.depositors;

import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.bank.impls.UniversalBank;
import lombok.Getter;

@Getter
public abstract class Depositor {

    private final int id;
    private final String name;
    private final Bank bank;
    private final float bonus;
    private int deposited;
    private int obtained;

    public Depositor() {
        this.id = 0;
        this.name = "John Doe";
        this.deposited = 0;
        this.bank = new UniversalBank();
        this.bonus = initBonus();
        System.out.println("Hello! I am the empty Depositor, did you forget to set my name and id?");
        System.out.println("I am using the Universal Bank by default.");
    }

    public Depositor(int id, String name, Bank bank) {
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

    @Override
    public String toString() {
        return "BankDepositor [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ']';
    }

}
