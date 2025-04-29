package com.ticketing.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TicketPoolBlocking implements TicketPool{
    private final BlockingQueue<Ticket> queue;
    private int sold = 0, added = 0;

    public TicketPoolBlocking(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        queue.put(ticket);
        added++;
    }

    public Ticket purchaseTicket() throws InterruptedException {
        sold++;
        return queue.take();
    }

    public void printStatus() {
        System.out.printf("[Blocking] Tickets Added: %d, Sold: %d, Available: %d%n", added, sold, queue.size());
    }
}
