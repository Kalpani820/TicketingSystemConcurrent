package com.ticketing.concurrent;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TicketCLI {
    public static void main(String[] args) {
        SimulationManager simulation = new SimulationManager();
        simulation.runCLI();
    }
}
