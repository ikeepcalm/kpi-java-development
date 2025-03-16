package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.solution.BankDepositor;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, world!");

        printNewLine();

        BankDepositor[] depositors = new BankDepositor[3];
        for (int i = 0; i < depositors.length; i++) {
            depositors[i] = new BankDepositor(i, getRandomName());
            System.out.println("Created depositor: " + depositors[i].toString());
        }

        printNewLine();

        System.out.println("This will print the line from the empty constructor:");
        BankDepositor blankDepositor = new BankDepositor();

        System.out.println("Our blank depositor " + blankDepositor + " will get " + blankDepositor.imitateDeposit(1000) + " after 12 months.");
        System.out.println("Our blank depositor " + blankDepositor + " will get " + blankDepositor.imitateDeposit(1000, 24) + " after 24 months.");
        System.out.println("Up until now the depositor has deposited: " + blankDepositor.getDepositedAmount() + " in total.");
        System.out.println("And the final amount he will receive is: " + blankDepositor.getFinalAmount());

        printNewLine();

        System.out.println("Now we will try with different approach:");

        int months = 6;
        for (BankDepositor depositor : depositors) {
            System.out.println(" - Depositor " + depositor.toString() + " will get " + depositor.imitateDeposit(1000, months) + " after " + months + " months.");
            months += 6;
        }

    }

    private static void printNewLine() {
        System.out.println("\n");
    }

    private static String getRandomName() {
        String[] names = {"John", "Jane", "Jack", "Jill", "Jim", "Jenny", "Joe", "Jill", "Jill", "Jill"};
        String[] surnames = {"Doe", "Smith", "Johnson", "Brown", "Taylor", "Anderson", "Harris", "Clark", "Allen", "Scott"};
        return names[(int) (Math.random() * names.length)] + " " + surnames[(int) (Math.random() * surnames.length)];
    }

}