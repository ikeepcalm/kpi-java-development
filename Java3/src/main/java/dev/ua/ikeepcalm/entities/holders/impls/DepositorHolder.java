package dev.ua.ikeepcalm.entities.holders.impls;

import dev.ua.ikeepcalm.entities.holders.Holder;
import dev.ua.ikeepcalm.entities.depositors.Depositor;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class DepositorHolder implements Holder {

    private final Collection<Depositor> depositors;

    public DepositorHolder() {
        this.depositors = new ArrayList<>();
    }

    public DepositorHolder(Collection<Depositor> depositors) {
        this.depositors = new ArrayList<>(depositors);
    }

    public void addDepositor(Depositor depositor) {
        depositors.add(depositor);
    }

    public Collection<Depositor> getDepositors() {
        return Collections.unmodifiableCollection(depositors);
    }

    public Depositor findDepositor(Predicate<Depositor> predicate) {
        return depositors.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    public Collection<Depositor> findDepositors(Predicate<Depositor> predicate) {
        return depositors.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public Set<Depositor> getUniqueDepositors(Comparator<Depositor> comparator) {
        Set<Depositor> uniqueDepositors = new TreeSet<>(comparator);
        uniqueDepositors.addAll(depositors);
        return uniqueDepositors;
    }

    public Set<Depositor> getUniqueDepositorsByID() {
        return new HashSet<>(depositors);
    }

    public enum SortCriterion {
        NAME, ID, DEPOSITED, OBTAINED, BONUS
    }

    public List<Depositor> sortWithAnonymousClass(SortCriterion criterion) {
        List<Depositor> sortedList = new ArrayList<>(depositors);

        sortedList.sort(new Comparator<Depositor>() {
            @Override
            public int compare(Depositor d1, Depositor d2) {
                return switch (criterion) {
                    case NAME -> d1.getName().compareTo(d2.getName());
                    case ID -> Integer.compare(d1.getId(), d2.getId());
                    case DEPOSITED -> Integer.compare(d1.getDeposited(), d2.getDeposited());
                    case OBTAINED -> Integer.compare(d1.getObtained(), d2.getObtained());
                    case BONUS -> Float.compare(d1.getBonus(), d2.getBonus());
                    default -> 0;
                };
            }
        });

        return sortedList;
    }

    public List<Depositor> sortWithLambda(SortCriterion criterion) {
        List<Depositor> sortedList = new ArrayList<>(depositors);

        switch (criterion) {
            case NAME -> sortedList.sort((d1, d2) -> d1.getName().compareTo(d2.getName()));
            case ID -> sortedList.sort((d1, d2) -> Integer.compare(d1.getId(), d2.getId()));
            case DEPOSITED -> sortedList.sort((d1, d2) -> Integer.compare(d1.getDeposited(), d2.getDeposited()));
            case OBTAINED -> sortedList.sort((d1, d2) -> Integer.compare(d1.getObtained(), d2.getObtained()));
            case BONUS -> sortedList.sort((d1, d2) -> Float.compare(d1.getBonus(), d2.getBonus()));
        }

        return sortedList;
    }

    public <T extends Comparable<T>> List<Depositor> sortWithMethodReference(
            java.util.function.Function<Depositor, T> keyExtractor) {
        List<Depositor> sortedList = new ArrayList<>(depositors);
        sortedList.sort(Comparator.comparing(keyExtractor));
        return sortedList;
    }

    public Collection<Depositor> filterDepositors(Predicate<Depositor> predicate) {
        return depositors.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public double calculateAverage(ToIntFunction<Depositor> mapper) {
        return depositors.stream()
                .mapToInt(mapper)
                .average()
                .orElse(0);
    }

    public double calculateAverageDeposited() {
        return calculateAverage(Depositor::getDeposited);
    }

    public double calculateAverageObtained() {
        return calculateAverage(Depositor::getObtained);
    }
}