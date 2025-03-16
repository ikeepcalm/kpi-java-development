package dev.ua.ikeepcalm.utils;

import dev.ua.ikeepcalm.depositors.Depositor;
import dev.ua.ikeepcalm.depositors.impls.BankOfficer;
import dev.ua.ikeepcalm.depositors.impls.PlainDepositor;
import dev.ua.ikeepcalm.depositors.impls.PreferredDepositor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WildcardsUtil {

    public static Map<String, Integer> analyzeDepositors(List<?> items) {
        Map<String, Integer> statistics = new HashMap<>();
        int totalDepositors = 0;
        int bankOfficers = 0;
        int plainDepositors = 0;
        int preferredDepositors = 0;

        for (Object item : items) {
            if (item instanceof Depositor) {
                totalDepositors++;

                switch (item) {
                    case BankOfficer genericBankOfficer -> bankOfficers++;
                    case PlainDepositor plainDepositor -> plainDepositors++;
                    case PreferredDepositor preferredDepositor -> preferredDepositors++;
                    default -> {
                    }
                }
            }
        }

        statistics.put("Total Depositors", totalDepositors);
        statistics.put("Bank Officers", bankOfficers);
        statistics.put("Plain Depositors", plainDepositors);
        statistics.put("Preferred Depositors", preferredDepositors);

        return statistics;
    }

    public static void printStatistics(Map<String, Integer> statistics) {
        System.out.println("Depositor Statistics:");
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

}