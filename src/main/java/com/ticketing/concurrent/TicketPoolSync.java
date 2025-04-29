package com.ticketing.concurrent;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPoolSync implements TicketPool{
    private final Queue<Ticket> queue = new LinkedList<>();
    private final int capacity;
    private int sold = 0, added = 0;

    public TicketPoolSync(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void addTicket(Ticket ticket) throws InterruptedException {
        while (queue.size() == capacity) wait();
        queue.offer(ticket);
        added++;
        notifyAll();
    }

    public synchronized Ticket purchaseTicket() throws InterruptedException {
        while (queue.isEmpty()) wait();
        sold++;
        notifyAll();
        return queue.poll();
    }

    public synchronized void printStatus() {
        System.out.printf("[Sync] Tickets Added: %d, Sold: %d, Available: %d%n", added, sold, queue.size());
    }
}
