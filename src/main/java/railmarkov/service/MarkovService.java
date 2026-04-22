package railmarkov.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import railmarkov.entity.Event;
import railmarkov.model.State;
import railmarkov.repository.EventRepository;


public class MarkovService {
    private final EventRepository eventRepository;
    private List<Event> events;
    private double[][] transitionMatrix;
    private StringBuilder sb;

    public MarkovService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void read() {
        this.events = this.eventRepository.selectEvents();

        System.out.printf("%d railcar events read from Cassandra\n", this.events.size()); 
    }

    public void train() {
        State[] states = State.values();
        int stateCount = states.length;
        
        this.transitionMatrix = new double[stateCount][stateCount];
        int[][] counts = new int[stateCount][stateCount];
        
        for (int i = 1; i < this.events.size(); i++) {
            Event prev = this.events.get(i - 1);
            Event curr = this.events.get(i);
            
            if (prev.getRailcarId().equals(curr.getRailcarId())) {
                int fromIdx = prev.getState().ordinal();
                int toIdx = curr.getState().ordinal();
                counts[fromIdx][toIdx]++;
            }
        }
        
        for (int from = 0; from < stateCount; from++) {
            int total = 0;
            for (int to = 0; to < stateCount; to++) {
                total += counts[from][to];
            }
            
            if (total > 0) {
                for (int to = 0; to < stateCount; to++) {
                    this.transitionMatrix[from][to] = (double) counts[from][to] / total;
                }
            }
        }

        System.out.print("markov model trained\n"); 
    }

    public void build() {
        State[] states = State.values();
        this.sb = new StringBuilder();
        
        this.sb.append(pad("", 20));
        for (State s : states) {
            this.sb.append(pad(s.getCode(), 7));
        }
        this.sb.append("\n").append("-".repeat(20 + 7 * states.length)).append("\n");
        
        for (int from = 0; from < states.length; from++) {
            this.sb.append(pad(states[from].name(), 20));
            for (int to = 0; to < states.length; to++) {
                double prob = this.transitionMatrix[from][to];
                String val = prob == 0.0 ? "-" : String.format("%.2f", prob);
                this.sb.append(pad(val, 7));
            }
            this.sb.append("\n");
        }

        System.out.print("transition matrix built\n"); 
    }

    private String pad(String s, int width) {
        if (s.length() >= width) {
            return s.substring(0, width);
        }
        return s + " ".repeat(width - s.length());
    }

    public void save() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter("src/main/resources/transition_matrix.txt"))) {
            w.write(this.sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write transition matrix", e);
        }

        System.out.print("transition matrix saved to 'src/main/resources/transition_matrix.txt'\n\n"); 
    }
}