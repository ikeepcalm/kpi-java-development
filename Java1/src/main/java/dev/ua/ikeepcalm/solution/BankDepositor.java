package dev.ua.ikeepcalm.solution;

public class BankDepositor {

    private final int id;
    private final String name;
    private int depositedAmount;
    private int finalAmount;

    public BankDepositor() {
        this.id = 0;
        this.name = "John Doe";
        this.depositedAmount = 0;
        System.out.println("Hello! I am the empty Depositor, did you forget to set my name and id?");
    }

    public BankDepositor(int id, String name) {
        this.id = id;
        this.name = name;
        this.depositedAmount = 0;
    }

    public int imitateDeposit(int amount) {
        this.depositedAmount += amount;
        int obtainedAmount = BankImitation.imitateDeposit(amount, 12);
        this.finalAmount += obtainedAmount;
        return obtainedAmount;
    }

    public int imitateDeposit(int amount, int months) {
        this.depositedAmount += amount;
        int obtainedAmount = BankImitation.imitateDeposit(amount, months);
        this.finalAmount += obtainedAmount;
        return obtainedAmount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    public int getDepositedAmount() {
        return depositedAmount;
    }

    @Override
    public String toString() {
        return "BankDepositor [" +
               "id=" + id +
               ", name='" + name + '\'' +
               ']';
    }
}
