package railmarkov.markov.util;

import railmarkov.domain.State;
import railmarkov.markov.model.TransitionMatrix;


public class TransitionMatrixFormatter {
    public static String format(TransitionMatrix transitionMatrix) {
        StringBuilder stringBuilder = new StringBuilder();
        State[] states = transitionMatrix.getStates();
        double[][] matrix = transitionMatrix.getMatrix();
        
        int colWidth = 10;
        int rowLabelWidth = 10;

        stringBuilder.append(String.format("%-" + rowLabelWidth + "s", ""));
        for (State s : states) {
            stringBuilder.append(String.format("%" + colWidth + "s", s.getCode()));
        }
        stringBuilder.append("\n");
        stringBuilder.append("-".repeat(rowLabelWidth + colWidth * states.length)).append("\n");

        for (int i = 0; i < states.length; i++) {
            stringBuilder.append(String.format("%-" + rowLabelWidth + "s", states[i].getCode()));
            for (int j = 0; j < states.length; j++) {
                double prob = matrix[i][j];
                String val = prob == 0.0 ? "-" : String.format("%.2f", prob);
                stringBuilder.append(String.format("%" + colWidth + "s", val));
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}