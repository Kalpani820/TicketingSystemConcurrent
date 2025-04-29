package com.ticketing.concurrent;

public class Consumer extends Worker{
    public Consumer(String name, TicketPool pool) {
        super(name, pool);
    }

    public void run() {
        while (running) {
            try {
                Ticket ticket = pool.purchaseTicket();
                System.out.println(getName() + " bought: " + ticket);
                Thread.sleep(800);
            } catch (InterruptedException ignored) {}
        }
    }
}
