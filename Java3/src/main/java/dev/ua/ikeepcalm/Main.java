package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.bank.impls.UniversalBank;
import dev.ua.ikeepcalm.entities.holders.impls.DepositorHolder;
import dev.ua.ikeepcalm.entities.holders.impls.DepositorMap;
import dev.ua.ikeepcalm.entities.depositors.Depositor;
import dev.ua.ikeepcalm.entities.depositors.impls.PreferredDepositor;
import dev.ua.ikeepcalm.utils.DepositorBuilder;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        fireHolder();
        fireMap();
    }

    private static void fireHolder() {
        DepositorHolder collection = new DepositorHolder();

        for (Depositor depositor : DepositorBuilder.createRandomDepositors(10)) {
            collection.addDepositor(depositor);
        }

        Depositor bob = new PreferredDepositor(11, "Bob Brown");
        collection.addDepositor(bob);

        Bank universalBank = new UniversalBank(collection);
        int amount = 1000;
        int months = 6;
        for (Depositor depositor : collection.getDepositors()) {
            amount += 1000;
            months -= 2;
            int obtained = universalBank.imitateDeposit(depositor, amount, months);
            log(depositor.getName() + " deposited " + amount + " and obtained " + obtained, Color.GREEN);
        }

        // Пошук по вимогам через лямбда вираз
        Depositor foundDepositor = collection.findDepositor(d -> d.getName().equals("Bob Brown"));
        log("\nFound depositor: " + foundDepositor, Color.BLUE);

        // Колекція унікальних депозиторів за ID
        log("\nUnique depositors by ID:", Color.BLUE);
        collection.getUniqueDepositorsByID().forEach(System.out::println);

        // Сортування депозиторів за отриманою сумою
        log("\nDepistors sorted by amount obtained:", Color.BLUE);
        collection.sortWithLambda(DepositorHolder.SortCriterion.OBTAINED).forEach(d -> System.out.println(d.getName() + ": " + d.getObtained()));

//         Method Reference
//        collection.sortWithMethodReference(Depositor::getObtained).forEach(d -> System.out.println(d.getName()));
//         Anonymous Class
//        collection.sortWithAnonymousClass(DepositorHolder.SortCriterion.ID).forEach(d -> System.out.println(d.getId()));

        // Порівняння депозиторів за критерієм (сума депозиту)
        log("\nDepositors with deposit > 1500:", Color.BLUE);
        collection.filterDepositors(d -> d.getDeposited() > 1500)
                .forEach(d -> System.out.println(d.getName() + ": " + d.getDeposited()));

        // Середнє значення депозитів та отриманих сум
        double avgDeposited = collection.calculateAverageDeposited();
        log("\nAverage deposited amount: " + avgDeposited, Color.BLUE);
        double avgObtained = collection.calculateAverageObtained();
        log("\nAverage obtained amount: " + avgObtained, Color.BLUE);
    }

    private static void fireMap() {
        DepositorMap depositorMap = new DepositorMap();
        for (Depositor depositor : DepositorBuilder.createRandomDepositors(10)) {
            depositorMap.addDepositor(depositor);
        }
        Depositor bob = new PreferredDepositor(11, "Bob Brown");
        depositorMap.addDepositor(bob);

        Bank universalBank = new UniversalBank(depositorMap);

        int amount = 1000;
        int months = 6;

        for (Depositor depositor : depositorMap.getDepositorMap().values()) {
            amount += 1000;
            months = months > 2 ? months - 2 : 1;
            log(depositor.getName() + " deposited " + amount + " and obtained " + universalBank.imitateDeposit(depositor, amount, months), Color.GREEN);
        }

        // Фільтр по обраній ознаці
        Depositor foundDepositor = depositorMap.filterDepositors(d -> d.getName().equals("Bob Brown"))
                .values().stream().findFirst().orElse(null);
        log("\nFound depositor: " + foundDepositor, Color.BLUE);

        // Видалення депозиторів за вимогами
        log("\nRemoving depositors with deposit < 3000...", Color.BLUE);

        log("\nRemoved " + depositorMap.removeDepositors(d -> d.getDeposited() < 3000) + " depositors", Color.BLUE);

        log("\nRemaining depositors:", Color.BLUE);
        depositorMap.getDepositorMap().values().forEach(System.out::println);

        // Визначення загальної суми депозитів та отриманих сум
        int totalDeposited = depositorMap.getTotalDeposited();
        log("\nTotal amount deposited across all depositors: " + totalDeposited, Color.BLUE);
        int totalObtained = depositorMap.getTotalObtained();
        log("Total amount obtained across all depositors: " + totalObtained, Color.BLUE);

        log("\nDepositors sorted by ID and amount obtained:", Color.BLUE);
        List<Depositor> sortedMap = depositorMap.sortByMultipleCriteriaAnonymous(DepositorMap.SortCriterion.ID, DepositorMap.SortCriterion.OBTAINED);
        sortedMap.forEach(d -> System.out.println(d.getId() + ": " + d.getObtained()));

        // Lambda Expression
        // List<Depositor> sortedList = depositorMap.sortByMultipleCriteriaLambda(DepositorMap.SortCriterion.ID, DepositorMap.SortCriterion.OBTAINED);
        // Method Reference
        // List<Depositor> sortedMethodReference = depositorMap.sortByMultipleCriteriaMethodRef(DepositorMap.SortCriterion.ID, DepositorMap.SortCriterion.OBTAINED);
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

}