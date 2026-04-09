package railmarkov.io;

import railmarkov.entity.State; 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class TransitionMatrix {
    private final int size = State.values().length;
    private final State[] states = State.values();
    private final int colWidth = 7;
    private final int labelWidth = 20;

    public void save(double[][] probs, String path) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(path))) {
            w.write(pad("", labelWidth));
            for (State s : states) {
                w.write(pad(s.getCode(), colWidth));
            }
            w.newLine();

            w.write("-".repeat(labelWidth + colWidth * size));
            w.newLine();

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