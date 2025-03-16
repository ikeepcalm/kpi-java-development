package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.bank.impls.PrivatBank;
import dev.ua.ikeepcalm.bank.impls.UniversalBank;
import dev.ua.ikeepcalm.depositors.Depositor;
import dev.ua.ikeepcalm.depositors.impls.BankOfficer;
import dev.ua.ikeepcalm.depositors.impls.PlainDepositor;
import dev.ua.ikeepcalm.depositors.impls.PreferredDepositor;

import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {
        Depositor[] depositors = getDepositors();

        for (Depositor depositor : depositors) {
            switch (depositor) {
                case BankOfficer bankOfficer -> {
                    log("Bank officer " + bankOfficer.getName() + " has role " + bankOfficer.getRole().role(), Color.GREEN);
                    log("He / She also works in the " + bankOfficer.getRole().department() + " department as a " + bankOfficer.getRole().position(), Color.BLUE);
                }
                case PlainDepositor plainDepositor -> {
                    log("Plain depositor " + depositor.getName() + " has also persistent " + plainDepositor.getPriority() + " priority", Color.GREEN);
                }
                case PreferredDepositor preferredDepositor -> {
                    log("Preferred depositor has bonus of " + preferredDepositor.getBonus(), Color.BLUE);
                    log("His priority is " + preferredDepositor.getPriority(), Color.GREEN);
                }
                default -> {
                }
            }
        }

        ((PlainDepositor) depositors[1]).startBrouhaha();

        for (Depositor depositor : depositors){
            int amount = (int) (Math.random() * 1000);
            int months = (int) (Math.random() * 12);
            log("Bank used by " + depositor.getName() + " is " + depositor.getBank().getClass().getSimpleName(), Color.GREEN);
            int obtained = depositor.imitateDeposit(amount, months);
            log(depositor.getName() + " deposited " + amount + " and obtained " + obtained, Color.BLUE);
        }

        try {
            BankOfficer laziestOfficer = findLaziestOfficer(depositors);
            log("Laziest officer is " + laziestOfficer.getName(), Color.GREEN);
        } catch (IllegalArgumentException e) {
            log("No officer found in current collection of depositors!", Color.RED);
        }

        Depositor[] onlyMonoUsers = new Depositor[depositors.length];
        for (Depositor depositor : depositors.clone()) {
            if (depositor.getBank() instanceof UniversalBank) {
                onlyMonoUsers[depositor.getId()] = depositor;
            }
        }

        for (Depositor depositor : onlyMonoUsers) {
            if (depositor != null) {
                log("Depositor " + depositor.getName() + " uses MonoBank", Color.GREEN);
                assert depositor.getBank() instanceof UniversalBank;
            }
        }

        Depositor mostRichDepositor = Arrays.stream(depositors).toList().stream().max(Comparator.comparingInt(Depositor::getObtained)).orElse(null);
        if (mostRichDepositor != null) {
            log("Most rich depositor is " + mostRichDepositor.getName() + " with " + mostRichDepositor.getObtained(), Color.GREEN);
        }

        Depositor[] sortedByDeposited = depositors.clone();
        Arrays.sort(sortedByDeposited, Comparator.comparingInt(Depositor::getDeposited).reversed());

        for (Depositor depositor : sortedByDeposited) {
            log(depositor.getName() + " deposited " + depositor.getDeposited(), Color.BLUE);
        }

    }

    private static Depositor[] getDepositors() {
        Bank monoBank = new UniversalBank();
        Bank privat24 = new PrivatBank();
        BankOfficer.EmployeeRole officerRole = new BankOfficer.EmployeeRole("Bank Officer", "Banking", "Officer");
        BankOfficer.EmployeeRole managerRole = new BankOfficer.EmployeeRole("Bank Manager", "Banking", "Manager");

        return new Depositor[] {
                new BankOfficer(1, getRandomName(), monoBank, officerRole),
                new PlainDepositor(2, getRandomName(), monoBank),
                new PreferredDepositor(3, getRandomName(), privat24),
                new BankOfficer(4, getRandomName(), privat24, managerRole),
                new PlainDepositor(5, getRandomName(), privat24),
                new PreferredDepositor(6, getRandomName(), monoBank),
                new BankOfficer(),
        };
    }

    public static void log(String message, Color color) {
        String colorCode = switch (color) {
            case RED -> "\u001B[31m";
            case GREEN -> "\u001B[32m";
            case BLUE -> "\u001B[34m";
            case GRAY -> "\u001B[37m";
        };
        System.out.println(colorCode + message + "\u001B[0m");
    }

    public static void log(String message) {
        log(message, Color.GRAY);
    }

    public enum Color {
        RED, GREEN, BLUE, GRAY
    }

    public static BankOfficer findLaziestOfficer(Depositor[] depositors) throws IllegalArgumentException {
        BankOfficer leastBusyOfficer = null;
        int minDepositors = Integer.MAX_VALUE;

        for (Depositor depositor : depositors) {
            if (depositor instanceof BankOfficer bankOfficer) {
                int depositorCount = getDepositorCount(bankOfficer, depositors);
                if (depositorCount < minDepositors) {
                    minDepositors = depositorCount;
                    leastBusyOfficer = bankOfficer;
                }
            }
        }

        if (leastBusyOfficer == null) {
            throw new IllegalArgumentException("No officers found");
        }

        return leastBusyOfficer;
    }

    private static int getDepositorCount(BankOfficer officer, Depositor[] depositors) {
        int count = 0;
        for (Depositor depositor : depositors) {
            if (depositor.getBank().equals(officer.getBank())) {
                count++;
            }
        }
        return count;
    }

    private static String getRandomName() {
        String[] names = {"John", "Jane", "Jack", "Jill", "Jim", "Jenny", "Joe", "Jill", "Jill", "Jill"};
        String[] surnames = {"Doe", "Smith", "Johnson", "Brown", "Taylor", "Anderson", "Harris", "Clark", "Allen", "Scott"};
        return names[(int) (Math.random() * names.length)] + " " + surnames[(int) (Math.random() * surnames.length)];
    }

}