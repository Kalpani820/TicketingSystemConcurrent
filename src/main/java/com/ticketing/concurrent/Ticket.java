package com.ticketing.concurrent;

public class Ticket {
    private static long counter = 0;
    private final long ticketNumber;
    private final String vendor;
    private final String event;

    public Ticket(String vendor, String event) {
        this.ticketNumber = ++counter;
        this.vendor = vendor;
        this.event = event;
    }

    @Override
    public String toString() {
        return "Ticket{" + "#" + ticketNumber + ", vendor='" + vendor + '\'' + ", event='" + event + '\'' + '}';
    }
}
