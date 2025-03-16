package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.bank.impls.PrivatBank;
import dev.ua.ikeepcalm.bank.impls.UniversalBank;
import dev.ua.ikeepcalm.depositors.Depositor;
import dev.ua.ikeepcalm.depositors.generics.impls.GenericBankOfficer;
import dev.ua.ikeepcalm.depositors.impls.BankOfficer;
import dev.ua.ikeepcalm.depositors.impls.PlainDepositor;
import dev.ua.ikeepcalm.depositors.impls.PreferredDepositor;
import dev.ua.ikeepcalm.utils.AnalyzerUtil;
import dev.ua.ikeepcalm.utils.WildcardsUtil;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        genericMethod();
        genericClass();
        genericWildcards();
        libraryWildcards();
    }

    private static void genericMethod() {
        PlainDepositor[] plainDepositors = new PlainDepositor[]{
                new PlainDepositor(1, "John Smith", new PrivatBank()),
                new PlainDepositor(2, "Jane Doe", new UniversalBank())
        };

        for (Depositor depositor : plainDepositors) {
            depositor.imitateDeposit((int) (Math.random() * 2000 + 500), (int) (Math.random() * 12 + 1));
        }

        Depositor[] mixedDepositors = new Depositor[]{
                new PlainDepositor(1, "John Smith", new PrivatBank()),
                new PreferredDepositor(2, "Jane Doe", new UniversalBank())
        };

        for (Depositor depositor : mixedDepositors) {
            depositor.imitateDeposit((int) (Math.random() * 2000 + 500), (int) (Math.random() * 12 + 1));
        }

        // Старий метод, працює лише із суперкласом Depositor (або треба кастити)
        // PlainDepositor oldResult = AnalyzerUtil.oldFindMostProfitableDepositor(plainDepositors);
        Depositor oldResult = AnalyzerUtil.oldFindMostProfitableDepositor(plainDepositors);

        // Новий метод на дженериках, працює з будь-якими класами, що реалізують Depositor
        PlainDepositor bestPlain = AnalyzerUtil.findMostProfitableDepositor(plainDepositors);
        Depositor bestMixed = AnalyzerUtil.findMostProfitableDepositor(mixedDepositors);

        log("Most profitable mixed depositor: " + bestMixed.getName() + " with profit margin of " + AnalyzerUtil.calculateProfitMargin(bestMixed) + "%", Color.GREEN);
        log("Most profitable depositor overall: " + bestMixed.getName() + " with profit margin of " + AnalyzerUtil.calculateProfitMargin(bestMixed) + "%", Color.GREEN);

        log("Return type from plain depositors: " + bestPlain.getClass().getSimpleName(), Color.BLUE);
        log("Return type from mixed depositors: " + bestMixed.getClass().getSimpleName(), Color.BLUE);
        log("Return type from old depositors: " + oldResult.getClass().getSimpleName(), Color.RED);
    }

    private static void genericClass() {
        PrivatBank privatBank = new PrivatBank();
        UniversalBank universalBank = new UniversalBank();

        // Два однакових класа, із різними узагальненими типами
        GenericBankOfficer<PrivatBank> privatBankOfficer = new GenericBankOfficer<>(1, "John Smith", privatBank,
                new GenericBankOfficer.EmployeeRole("Bank Officer", "Banking", "Officer"));

        GenericBankOfficer<UniversalBank> universalBankOfficer = new GenericBankOfficer<>(2, "Jane Doe", universalBank,
                new GenericBankOfficer.EmployeeRole("Bank Manager", "Banking", "Manager"));

        // Виклик методів, що належать конкретному банку, хоча обидва об'єкти класу GenericBankOfficer
        privatBankOfficer.getBank().sayHello();
        universalBankOfficer.getBank().monoCat();
    }

    private static void genericWildcards() {
        List<Object> mixedList = new ArrayList<>();

        mixedList.add(new BankOfficer());
        mixedList.add(new PlainDepositor(1, "John Doe", new UniversalBank()));
        mixedList.add(new PreferredDepositor(2, "Jane Smith", new PrivatBank()));
        mixedList.add("Not a depositor");
        mixedList.add(123);

        List<BankOfficer> officerList = new ArrayList<>();
        officerList.add(new BankOfficer());
        officerList.add(new BankOfficer(3, "Bob Johnson", new PrivatBank(),
                new BankOfficer.EmployeeRole("Manager", "Loans", "Senior")));

        Map<String, Integer> mixedStats = WildcardsUtil.analyzeDepositors(mixedList);
        Map<String, Integer> officerStats = WildcardsUtil.analyzeDepositors(officerList);

        log("Mixed List Statistics:", Color.BLUE);
        WildcardsUtil.printStatistics(mixedStats);

        log("Officer List Statistics:", Color.BLUE);
        WildcardsUtil.printStatistics(officerStats);
    }

    public static void libraryWildcards() {
        // public static <T> void copy(List<? super T> dest, List<? extends T> src)
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Number> numbers = new ArrayList<>(Collections.nCopies(integers.size(), 0));
        Collections.copy(numbers, integers);
        for (Number number : numbers) {
            log("Number: " + number, Color.GREEN);
        }

        // public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll)
        String maxString = Collections.max(Arrays.asList("a", "b", "c"));
        log("Max string: " + maxString, Color.GREEN);

        // public static <T extends Comparable<? super T>> void sort(List<T> list)
        List<Integer> unsorted = new ArrayList<>(Arrays.asList(3, 1, 2));
        Collections.sort(unsorted);
        log(unsorted.toString(), Color.GREEN);

        // method: public int compare(T o1, T o2)
        Comparator<String> byLength = (s1, s2) -> s1.length() - s2.length();

        // public static <T> List<T> asList(T... a)
        List<Integer> intList = Arrays.asList(1, 2, 3);
        log(intList.toString(), Color.GREEN);
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