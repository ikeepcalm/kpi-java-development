package dev.ua.ikeepcalm.utils;

import dev.ua.ikeepcalm.depositors.Depositor;

public class AnalyzerUtil {

    public static Depositor oldFindMostProfitableDepositor(Depositor[] genericDepositors) {
        if (genericDepositors == null || genericDepositors.length == 0) {
            return null;
        }

        Depositor mostProfitable = genericDepositors[0];
        double highestProfitMargin = calculateProfitMargin(mostProfitable);

        for (Depositor genericDepositor : genericDepositors) {
            double profitMargin = calculateProfitMargin(genericDepositor);
            if (profitMargin > highestProfitMargin) {
                highestProfitMargin = profitMargin;
                mostProfitable = genericDepositor;
            }
        }

        return mostProfitable;
    }

    public static <T extends Depositor> T findMostProfitableDepositor(T[] depositors) {
        if (depositors == null || depositors.length == 0) {
            return null;
        }

        T mostProfitable = depositors[0];
        double highestProfitMargin = calculateProfitMargin(mostProfitable);

        for (T depositor : depositors) {
            double profitMargin = calculateProfitMargin(depositor);
            if (profitMargin > highestProfitMargin) {
                highestProfitMargin = profitMargin;
                mostProfitable = depositor;
            }
        }

        return mostProfitable;
    }

    public static double calculateProfitMargin(Depositor genericDepositor) {
        if (genericDepositor.getDeposited() == 0) {
            return 0;
        }
        return ((double) genericDepositor.getObtained() - genericDepositor.getDeposited()) / genericDepositor.getDeposited() * 100;
    }

}
