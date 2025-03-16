package dev.ua.ikeepcalm.utils;

import dev.ua.ikeepcalm.entities.depositors.Depositor;
import dev.ua.ikeepcalm.entities.depositors.impls.BankOfficer;
import dev.ua.ikeepcalm.entities.depositors.impls.PlainDepositor;
import dev.ua.ikeepcalm.entities.depositors.impls.PreferredDepositor;

import java.util.ArrayList;
import java.util.List;

public class DepositorBuilder {

    public static List<Depositor> createRandomDepositors(int count) {
        List<Depositor> depositors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            depositors.add(createRandomDepositor(i + 1));
        }
        return depositors;
    }

    public static Depositor createRandomDepositor() {
        return createRandomDepositor(1);
    }

    private static Depositor createRandomDepositor(int id) {
        String name = getRandomName();
        int type = (int) (Math.random() * 3);
        return switch (type) {
            case 0 -> new BankOfficer(id, name, new BankOfficer.EmployeeRole("Bank Officer", "Banking", "Officer"));
            case 1 -> new PreferredDepositor(id, name);
            default -> new PlainDepositor(id, name);
        };
    }

    private static String getRandomName() {
        String[] names = {"John", "Jane", "Jack", "Jill", "Jim", "Jenny", "Joe", "Jill", "Jill", "Jill"};
        String[] surnames = {"Doe", "Smith", "Johnson", "Brown", "Taylor", "Anderson", "Harris", "Clark", "Allen", "Scott"};
        return names[(int) (Math.random() * names.length)] + " " + surnames[(int) (Math.random() * surnames.length)];
    }

}
