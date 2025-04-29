package com.ticketing.concurrent;

public class Writer extends Worker{
    public Writer(String name, TicketPool pool) {
        super(name, pool);
    }

    public void run() {
        while (running) {
            try {
                pool.addTicket(new Ticket(getName(), "Update"));
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {}
        }
    }
}
