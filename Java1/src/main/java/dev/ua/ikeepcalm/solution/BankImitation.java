package dev.ua.ikeepcalm.solution;

public class BankImitation {

    private final static float INTEREST_RATE = 0.05f;

    public static int imitateDeposit(int amount, int months) {
        System.out.println("Imitating deposit for " + amount + " for " + months + " months.");
        return amount + (int) (amount * INTEREST_RATE * months);
    }

}
