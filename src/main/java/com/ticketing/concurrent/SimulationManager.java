package com.ticketing.concurrent;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class SimulationManager {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Worker> workers = new ArrayList<>();
    private TicketPool pool;

    public void runCLI() {
        System.out.println("Choose implementation: 1) Sync 2) Lock 3) BlockingQueue");
        int choice = scanner.nextInt();
        System.out.print("Enter queue capacity: ");
        int cap = scanner.nextInt();

        pool = switch (choice) {
            case 1 -> new TicketPoolSync(cap);
            case 2 -> new TicketPoolLock(cap);
            case 3 -> new TicketPoolBlocking(cap);
            default -> throw new IllegalStateException("Invalid selection");
        };

        boolean running = true;
        while (running) {
            System.out.println("1) Add Producer 2) Add Consumer 3) Add Reader 4) Add Writer 5) Stop All 6) Exit");
            int input = scanner.nextInt();
            switch (input) {
                case 1 -> startWorker(new Producer("Producer-" + workers.size(), pool));
                case 2 -> startWorker(new Consumer("Consumer-" + workers.size(), pool));
                case 3 -> startWorker(new Reader("Reader-" + workers.size(), pool));
                case 4 -> startWorker(new Writer("Writer-" + workers.size(), pool));
                case 5 -> workers.forEach(Worker::stopWorker);
                case 6 -> {
                    workers.forEach(Worker::stopWorker);
                    running = false;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void startWorker(Worker worker) {
        workers.add(worker);
        worker.start();
    }
}
