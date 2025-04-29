package com.ticketing.concurrent;

abstract class Worker extends Thread{
    protected final TicketPool pool;
    protected boolean running = true;

    public Worker(String name, TicketPool pool) {
        super(name);
        this.pool = pool;
    }

    public void stopWorker() {
        running = false;
        this.interrupt();
    }
}
