package ru.geekbrains.lesson5;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static final int CARS_COUNT = 20;
    public static CountDownLatch countDownLatchRaceStart = new CountDownLatch(CARS_COUNT);
    public static CountDownLatch countDownLatchRaceEnd = new CountDownLatch(CARS_COUNT);
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);
    public static Semaphore semaphore = new Semaphore(CARS_COUNT / 2);
    public static Lock winnerLock = new ReentrantLock();
    public static final int MIN_SPEED = 50;
    public static Random R = new Random();

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(200 + R.nextInt(500)), new Tunnel(50 + R.nextInt(150)), new Road(100 + R.nextInt(400)));
        Car[] cars = new Car[CARS_COUNT];
        Phaser phaser = new Phaser(CARS_COUNT);
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, MIN_SPEED + R.nextInt(100), phaser);
            new Thread(cars[i]).start(); //стартуем новый поток
        }

        System.err.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        phaser.awaitAdvance(0);
        phaser.awaitAdvance(1);
        System.err.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

    }
}