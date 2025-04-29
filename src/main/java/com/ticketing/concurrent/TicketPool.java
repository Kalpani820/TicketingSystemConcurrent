package com.ticketing.concurrent;

public interface TicketPool {
    void addTicket(Ticket ticket) throws InterruptedException;
    Ticket purchaseTicket() throws InterruptedException;
    void printStatus();
}
