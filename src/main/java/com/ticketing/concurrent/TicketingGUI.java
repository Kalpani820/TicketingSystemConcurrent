package com.ticketing.concurrent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import com.formdev.flatlaf.FlatLightLaf;

public class TicketingGUI {
    private JFrame frame;
    private JTextArea statusArea;
    private JComboBox<String> poolSelector;
    private JTextField capacityField;
    private TicketPool pool;
    private final List<Worker> workers = new ArrayList<>();
    private JButton addProducer, addConsumer, addReader, addWriter;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        SwingUtilities.invokeLater(() -> new TicketingGUI().createAndShowGUI());
    }

    public void createAndShowGUI() {
        frame = new JFrame("🎟 Ticketing System - Concurrent UI");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new FlowLayout());
        poolSelector = new JComboBox<>(new String[]{"Sync", "Lock", "BlockingQueue"});
        capacityField = new JTextField("10", 5);

        JButton initBtn = new JButton("Initialize Pool");
        initBtn.addActionListener(this::initializePool);

        controlPanel.add(new JLabel("Type:"));
        controlPanel.add(poolSelector);
        controlPanel.add(new JLabel("Capacity:"));
        controlPanel.add(capacityField);
        controlPanel.add(initBtn);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        addProducer = new JButton("Add Producer");
        addConsumer = new JButton("Add Consumer");
        addReader = new JButton("Add Reader");
        addWriter = new JButton("Add Writer");

        addProducer.setEnabled(false);
        addConsumer.setEnabled(false);
        addReader.setEnabled(false);
        addWriter.setEnabled(false);

        JButton stopAll = new JButton("Stop All");
        JButton refresh = new JButton("Show Status");

        addProducer.addActionListener(e -> startWorker(new Producer("Producer-" + workers.size(), pool)));
        addConsumer.addActionListener(e -> startWorker(new Consumer("Consumer-" + workers.size(), pool)));
        addReader.addActionListener(e -> startWorker(new Reader("Reader-" + workers.size(), pool)));
        addWriter.addActionListener(e -> startWorker(new Writer("Writer-" + workers.size(), pool)));
        stopAll.addActionListener(e -> workers.forEach(Worker::stopWorker));
        refresh.addActionListener(e -> updateStatus());

        buttonPanel.add(addProducer);
        buttonPanel.add(addConsumer);
        buttonPanel.add(addReader);
        buttonPanel.add(addWriter);
        buttonPanel.add(stopAll);
        buttonPanel.add(refresh);

        statusArea = new JTextArea(15, 60);
        statusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusArea);

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void initializePool(ActionEvent e) {
        String type = (String) poolSelector.getSelectedItem();
        int cap = Integer.parseInt(capacityField.getText());
        switch (type) {
            case "Sync" -> pool = new TicketPoolSync(cap);
            case "Lock" -> pool = new TicketPoolLock(cap);
            case "BlockingQueue" -> pool = new TicketPoolBlocking(cap);
        }
        log("Initialized pool: " + type + " with capacity: " + cap);

        addProducer.setEnabled(true);
        addConsumer.setEnabled(true);
        addReader.setEnabled(true);
        addWriter.setEnabled(true);
    }

    private void startWorker(Worker worker) {
        workers.add(worker);
        worker.start();
        log("Started: " + worker.getName());
    }

    private void updateStatus() {
        if (pool != null) {
            // Capture the output of printStatus()
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream oldOut = System.out;
            System.setOut(ps);

            pool.printStatus();

            System.out.flush();
            System.setOut(oldOut);

            statusArea.append(baos.toString());
        }
    }

    private void log(String msg) {
        statusArea.append(msg + "\n");
    }
}
