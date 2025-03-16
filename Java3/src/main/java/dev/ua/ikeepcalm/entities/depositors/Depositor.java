package dev.ua.ikeepcalm.entities.depositors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Depositor {

    private final int id;
    private final String name;
    private final float bonus;
    private int deposited;
    private int obtained;

    public Depositor() {
        this.id = 0;
        this.name = "John Doe";
        this.deposited = 0;
        this.bonus = initBonus();
        System.out.println("Hello! I am the empty Depositor, did you forget to set my name and id?");
        System.out.println("I am using the Universal Bank by default.");
    }

    public Depositor(int id, String name) {
        this.id = id;
        this.name = name;
        this.deposited = 0;
        this.bonus = initBonus();
    }

    public abstract float initBonus();

    @Override
    public String toString() {
        return "BankDepositor [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deposited=" + deposited +
                ']';
    }

}
