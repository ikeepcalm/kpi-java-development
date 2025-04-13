package dev.ua.ikeepcalm.app.simulator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class DrawableClient {
    String name;
    int age;
    String job;
    String clientType;
    String requestType;
    int amount;
    int creditScore;
    String position;
    Point currentPosition;
    Point targetPosition;
    boolean movingToDesk = false;

    public DrawableClient(String name, int age, String job, String clientType, String requestType, int amount, int creditScore, String position) {
        this.name = name;
        this.age = age;
        this.job = job;
        this.clientType = clientType;
        this.requestType = requestType;
        this.amount = amount;
        this.creditScore = creditScore;
        this.position = position;
    }
}