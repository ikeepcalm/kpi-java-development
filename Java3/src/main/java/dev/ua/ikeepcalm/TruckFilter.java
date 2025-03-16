package dev.ua.ikeepcalm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
class Truck {
    private String color;
    private double capacity;
    private String model;
    private int year;

    public Truck(String model, String color, double capacity, int year) {
        this.model = model;
        this.color = color;
        this.capacity = capacity;
        this.year = year;
    }
}

public class TruckFilter {

    public static void main(String[] args) {
        List<Truck> trucks = new ArrayList<>();

        trucks.add(new Truck("Volvo FH16", "blue", 4.5, 2020));
        trucks.add(new Truck("Scania R730", "yellow", 7.2, 2019));
        trucks.add(new Truck("MAN TGX", "red", 6.8, 2021));
        trucks.add(new Truck("Mercedes-Benz Actros", "yellow", 8.0, 2022));
        trucks.add(new Truck("Renault T-High", "yellow", 4.8, 2020));
        trucks.add(new Truck("DAF XF", "white", 7.5, 2018));
        trucks.add(new Truck("Iveco S-Way", "yellow", 6.2, 2021));

        List<Truck> plainFilteredTrucks = new ArrayList<>();
        for (Truck truck : trucks) {
            if ("yellow".equals(truck.getColor()) && truck.getCapacity() > 5.0) {
                plainFilteredTrucks.add(truck);
            }
        }

        List<Truck> streamFilteredTrucks = trucks.stream()
                .filter(truck -> "yellow".equals(truck.getColor()))
                .filter(truck -> truck.getCapacity() > 5.0)
                .toList();

        plainFilteredTrucks.forEach(System.out::println);
        streamFilteredTrucks.forEach(System.out::println);
    }
}