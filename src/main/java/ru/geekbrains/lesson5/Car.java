package ru.geekbrains.lesson5;

import javax.swing.*;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private static int CARS_COUNT = 1;
    private static boolean isWinner;
    private Phaser phaser;
    private static final AtomicInteger RacePosition = new AtomicInteger();
    private final Race race;
    private final int speed;
    private final String name;

    public Car(Race race, int speed, Phaser phaser) {
        this.race = race;
        this.speed = speed;
        this.name = "Участник #" + CARS_COUNT;
        this.phaser = phaser;
        CARS_COUNT++;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            phaser.arriveAndAwaitAdvance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        Main.winnerLock.lock();
        if (!isWinner) {
            System.err.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> " + this.name + " - ПОБЕДИТЕЛЬ!!!");
            JOptionPane.showConfirmDialog(null, this.name + " - ПОБЕДИТЕЛЬ!!!", "Уведомление", JOptionPane.CLOSED_OPTION);
            isWinner = true;
        }
        Main.winnerLock.unlock();
        System.err.println(">>> " + this.name + " занял " + RacePosition.incrementAndGet() + " место.");
        phaser.arrive();
    }
}
