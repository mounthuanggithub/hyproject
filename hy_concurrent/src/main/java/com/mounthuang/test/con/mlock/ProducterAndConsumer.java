package com.mounthuang.test.con.mlock;

/**
 * Author: mounthuang
 * Data: 2017/6/29.
 */

public class ProducterAndConsumer {
    public static void main(String[] args) {
        Container container = new Container();
        Producter producter = new Producter(container);
        Consumer consumer = new Consumer(container);
        Thread p1 = new Thread(producter, "p1");
        Thread p2 = new Thread(producter, "p2");
        Thread c1 = new Thread(consumer, "c1");
        Thread c2 = new Thread(consumer, "c2");
        p1.start();
        p2.start();
        c1.start();
        c2.start();
    }
}

class Resource {
    private int id;

    public Resource(int id) {
        this.id = id;
    }
}

class Container {
    private int index = 0;
    Resource[] resources = new Resource[10];

    public synchronized void increase() {
        while (index == resources.length) {
            try {
                System.out.println("生产队列已满，生产线程进入进入等待：" + Thread.currentThread().getName());
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        index++;
        notify();
    }

    public synchronized void decrease() {
        while (index == 0) {
            try {
                System.out.println("消费队列已空，消费线程进入进入等待：" + Thread.currentThread().getName());
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        index--;
        notify();
    }
}

class Producter implements Runnable {
    private Container container;

    public Producter(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(100);
                container.increase();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private Container container;

    public Consumer(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(10);
                container.decrease();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


