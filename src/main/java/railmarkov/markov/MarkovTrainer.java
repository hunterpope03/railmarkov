package railmarkov.markov;

import java.util.List;

import railmarkov.domain.Event;
import railmarkov.markov.model.TransitionMatrix; 


public class MarkovTrainer {
    public TransitionMatrix train(List<Event> events) {
        TransitionMatrix transitionMatrix = new TransitionMatrix();

        Event previous = null;

        for (Event current : events) {
            if (previous != null && previous.getRailcarId().equals(current.getRailcarId())) {
                transitionMatrix.incrementCount(previous.getState(), current.getState());
            }
            previous = current;
        }

        return transitionMatrix;
    }
}