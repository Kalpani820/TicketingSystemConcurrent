package com.ticketing.concurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TicketPoolLock implements TicketPool{
    private final Queue<Ticket> queue = new LinkedList<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock write = lock.writeLock();
    private final Lock read = lock.readLock();
    private final Condition notFull = write.newCondition(), notEmpty = write.newCondition();
    private final int capacity;
    private int sold = 0, added = 0;

    public TicketPoolLock(int capacity) {
        this.capacity = capacity;
    }

    public void addTicket(Ticket ticket) throws InterruptedException {
        write.lock();
        try {
            while (queue.size() == capacity) notFull.await();
            queue.offer(ticket);
            added++;
            notEmpty.signalAll();
        } finally {write.unlock();}
    }

    public Ticket purchaseTicket() throws InterruptedException {
        write.lock();
        try {
            while (queue.isEmpty()) notEmpty.await();
            sold++;
            notFull.signalAll();
            return queue.poll();
        } finally {
            write.unlock();
        }
    }

    public void printStatus() {
        read.lock();
        try {
            System.out.printf("[Lock] Tickets Added: %d, Sold: %d, Available: %d%n", added, sold, queue.size());
        } finally {
            read.unlock();
        }
    }
}
