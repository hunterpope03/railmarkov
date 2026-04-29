package railmarkov.markov.model;

import railmarkov.domain.State;

public class TransitionMatrix {
    private final State[] states;
    private final int[][] counts;
    private final double[][] matrix;

    public TransitionMatrix() {
        this.states = State.values();
        this.counts = new int[states.length][states.length];
        this.matrix = new double[states.length][states.length];
    }

    public void incrementCount(State from, State to) {
        this.counts[from.ordinal()][to.ordinal()]++;
    }

    public void normalize() {
        for (int from = 0; from < states.length; from++) {
            int total = 0;

            for (int to = 0; to < states.length; to++) {
                total += this.counts[from][to];
            }
            
            if (total > 0) {
                for (int to = 0; to < states.length; to++) {
                    this.matrix[from][to] = (double) this.counts[from][to] / total;
                }
            }
        }
    }

    public int[][] getCounts() {
        return this.counts;
    }

    public double[][] getMatrix() {
        return this.matrix;
    }

    public State[] getStates() {
        return this.states;
    }
}