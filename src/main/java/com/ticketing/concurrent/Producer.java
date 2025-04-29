package com.ticketing.concurrent;

public class Producer extends Worker{
    public Producer(String name, TicketPool pool) {
        super(name, pool);
    }

    public void run() {
        while (running) {
            try {
                pool.addTicket(new Ticket(getName(), "Concert"));
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
    }
}
