package dev.ua.ikeepcalm.entities.holders.impls;

import dev.ua.ikeepcalm.entities.holders.Holder;
import dev.ua.ikeepcalm.entities.depositors.Depositor;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class DepositorMap implements Holder {

    private final Map<Integer, Depositor> depositorMap;
    
    public enum SortCriterion {
        NAME, ID, DEPOSITED, OBTAINED, BONUS
    }

    public DepositorMap() {
        this.depositorMap = new HashMap<>();
    }
    
    public DepositorMap(Map<Integer, Depositor> depositorMap) {
        this.depositorMap = new HashMap<>(depositorMap);
    }
    
    public void addDepositor(Depositor depositor) {
        depositorMap.put(depositor.getId(), depositor);
    }
    
    public Map<Integer, Depositor> getDepositorMap() {
        return Collections.unmodifiableMap(depositorMap);
    }

    public Map<Integer, Depositor> filterDepositors(Predicate<Depositor> predicate) {
        return depositorMap.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int removeDepositors(Predicate<Depositor> predicate) {
        List<Integer> keysToRemove = depositorMap.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        keysToRemove.forEach(key -> depositorMap.remove(key));

        return keysToRemove.size();
    }

    public int sumAttribute(ToIntFunction<Depositor> mapper) {
        return depositorMap.values().stream()
                .mapToInt(mapper)
                .sum();
    }

    public int getTotalDeposited() {
        return sumAttribute(Depositor::getDeposited);
    }

    public int getTotalObtained() {
        return sumAttribute(Depositor::getObtained);
    }

    public double averageAttribute(ToIntFunction<Depositor> mapper) {
        return depositorMap.values().stream()
                .mapToInt(mapper)
                .average()
                .orElse(0);
    }

    public List<Depositor> sortByMultipleCriteriaAnonymous(SortCriterion... criteria) {
        List<Depositor> sortedList = new ArrayList<>(depositorMap.values());
        
        sortedList.sort(new Comparator<Depositor>() {
            @Override
            public int compare(Depositor d1, Depositor d2) {
                for (SortCriterion criterion : criteria) {
                    int result = switch (criterion) {
                        case NAME -> d1.getName().compareTo(d2.getName());
                        case ID -> Integer.compare(d1.getId(), d2.getId());
                        case DEPOSITED -> Integer.compare(d1.getDeposited(), d2.getDeposited());
                        case OBTAINED -> Integer.compare(d1.getObtained(), d2.getObtained());
                        case BONUS -> Float.compare(d1.getBonus(), d2.getBonus());
                    };

                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        });
        
        return sortedList;
    }

    public List<Depositor> sortByMultipleCriteriaLambda(SortCriterion... criteria) {
        List<Depositor> sortedList = new ArrayList<>(depositorMap.values());
        
        sortedList.sort((d1, d2) -> {
            for (SortCriterion criterion : criteria) {
                int result = switch (criterion) {
                    case NAME -> d1.getName().compareTo(d2.getName());
                    case ID -> Integer.compare(d1.getId(), d2.getId());
                    case DEPOSITED -> Integer.compare(d1.getDeposited(), d2.getDeposited());
                    case OBTAINED -> Integer.compare(d1.getObtained(), d2.getObtained());
                    case BONUS -> Float.compare(d1.getBonus(), d2.getBonus());
                };

                if (result != 0) {
                    return result;
                }
            }
            return 0;
        });
        
        return sortedList;
    }

    public List<Depositor> sortByMultipleCriteriaMethodRef(SortCriterion... criteria) {
        List<Depositor> sortedList = new ArrayList<>(depositorMap.values());
        
        Comparator<Depositor> comparator = null;
        
        for (SortCriterion criterion : criteria) {
            Comparator<Depositor> nextComparator = switch (criterion) {
                case NAME -> Comparator.comparing(Depositor::getName);
                case ID -> Comparator.comparingInt(Depositor::getId);
                case DEPOSITED -> Comparator.comparingInt(Depositor::getDeposited);
                case OBTAINED -> Comparator.comparingInt(Depositor::getObtained);
                case BONUS -> Comparator.comparing(Depositor::getBonus);
            };

            if (comparator == null) {
                comparator = nextComparator;
            } else {
                comparator = comparator.thenComparing(nextComparator);
            }
        }
        
        if (comparator != null) {
            sortedList.sort(comparator);
        }
        
        return sortedList;
    }

}