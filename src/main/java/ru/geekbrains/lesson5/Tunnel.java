package ru.geekbrains.lesson5;

public class Tunnel extends Stage {

    public Tunnel(int length) {
        this.length = length;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            if(!Main.semaphore.tryAcquire()) {
                System.out.println(c.getName() + " (ждет пока освободиться) " + description);
                Main.semaphore.acquire();
            }
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(c.getName() + " закончил этап: " + description);
            Main.semaphore.release();
        }
    }
}