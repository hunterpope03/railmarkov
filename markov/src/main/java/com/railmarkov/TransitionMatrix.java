package com.railmarkov;

import com.railmarkov.cassandra.entity.Event;
import com.railmarkov.cassandra.entity.State;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class TransitionMatrix {
    private final int size = State.values().length;
    private final State[] states = State.values();
    private final int[][] counts = new int[size][size];

    public void build(List<Event> events) {
        Map<UUID, List<Event>> byRailcar = new LinkedHashMap<>();
        for (Event e : events) {
            byRailcar.computeIfAbsent(e.getRailcarId(), k -> new ArrayList<>()).add(e);
        }

        for (List<Event> railcarEvents : byRailcar.values()) {
            railcarEvents.sort(Comparator.comparing(Event::getTimestamp));
            for (int i = 0; i < railcarEvents.size() - 1; i++) {
                State from = railcarEvents.get(i).getState();
                State to = railcarEvents.get(i + 1).getState();
                counts[from.ordinal()][to.ordinal()]++;
            }
        }
    }

    private double[][] probabilities() {
        double[][] probs = new double[size][size];
        for (int i = 0; i < size; i++) {
            int rowSum = 0;
            for (int j = 0; j < size; j++) rowSum += counts[i][j];
            for (int j = 0; j < size; j++) {
                probs[i][j] = rowSum == 0 ? 0.0 : (double) counts[i][j] / rowSum;
            }
        }
        return probs;
    }

    public void save(String path) {
        double[][] probs = probabilities();
        int colWidth = 7;
        int labelWidth = 20;

        try (BufferedWriter w = new BufferedWriter(new FileWriter(path))) {
            // header row
            w.write(pad("", labelWidth));
            for (State s : states) {
                w.write(pad(s.getCode(), colWidth));
            }
            w.newLine();

            // separator
            w.write("-".repeat(labelWidth + colWidth * size));
            w.newLine();

            // data rows
            for (int i = 0; i < size; i++) {
                w.write(pad(states[i].name(), labelWidth));
                for (int j = 0; j < size; j++) {
                    String cell = probs[i][j] == 0.0 ? "-" : String.format("%.2f", probs[i][j]);
                    w.write(pad(cell, colWidth));
                }
                w.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to write transition matrix to: " + path, e);
        }
    }

    private String pad(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        return s + " ".repeat(width - s.length());
    }
}