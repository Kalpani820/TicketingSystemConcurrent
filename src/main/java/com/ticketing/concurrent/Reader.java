package com.ticketing.concurrent;

public class Reader extends Worker{
    public Reader(String name, TicketPool pool) {
        super(name, pool);
    }

    public void run() {
        while (running) {
            pool.printStatus();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
    }
}
