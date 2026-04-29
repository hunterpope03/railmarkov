package railmarkov.datagen.util; 

import railmarkov.domain.State; 


public class StateTransitionUtil {
    private static record TransitionProbability(State state, double probability) {}

    public static State transition(State state) {
        return switch (state) {
            case ACQUIRED -> pick(
                new TransitionProbability(State.TO_YARD, 0.9700),
                new TransitionProbability(State.UNDER_INSPECTION, 0.0300)
            );
            case TO_YARD -> pick(
                new TransitionProbability(State.AT_YARD, 0.9850),
                new TransitionProbability(State.SWITCHING, 0.0150)
            );
            case SWITCHING -> pick(
                new TransitionProbability(State.AT_YARD, 0.9700),
                new TransitionProbability(State.TO_STORAGE, 0.0300)
            );
            case AT_YARD -> pick(
                new TransitionProbability(State.UNDER_INSPECTION, 0.3500),
                new TransitionProbability(State.TO_INDUSTRY, 0.2300),
                new TransitionProbability(State.TO_STORAGE, 0.1800),
                new TransitionProbability(State.SWITCHING, 0.2200),
                new TransitionProbability(State.TO_YARD, 0.0750),
                new TransitionProbability(State.BAD_ORDER, 0.0100)
            );
            case TO_STORAGE -> pick(
                new TransitionProbability(State.IN_STORAGE, 0.9900),
                new TransitionProbability(State.AT_YARD, 0.0100)
            );
            case IN_STORAGE -> pick(
                new TransitionProbability(State.FROM_STORAGE, 1.0000)
            );
            case FROM_STORAGE -> pick(
                new TransitionProbability(State.AT_YARD, 0.7800),
                new TransitionProbability(State.SWITCHING, 0.1800),
                new TransitionProbability(State.TO_STORAGE, 0.0200)
            );
            case UNDER_INSPECTION -> pick(
                new TransitionProbability(State.TO_INDUSTRY, 0.4200),
                new TransitionProbability(State.TO_YARD, 0.4200),
                new TransitionProbability(State.AT_YARD, 0.1200),
                new TransitionProbability(State.BAD_ORDER, 0.0400)
            );
            case BAD_ORDER -> pick(
                new TransitionProbability(State.UNDER_REPAIR, 0.9200),
                new TransitionProbability(State.TO_STORAGE, 0.0500),
                new TransitionProbability(State.RETIRED, 0.0300)
            );
            case UNDER_REPAIR -> pick(
                new TransitionProbability(State.SWITCHING, 0.5500),
                new TransitionProbability(State.AT_YARD, 0.2500),
                new TransitionProbability(State.TO_STORAGE, 0.1800),
                new TransitionProbability(State.BAD_ORDER, 0.0200)
            );
            case TO_INDUSTRY -> pick(
                new TransitionProbability(State.AT_INDUSTRY, 1.0000)
            );
            case AT_INDUSTRY -> pick(
                new TransitionProbability(State.TO_SPUR, 0.8800),
                new TransitionProbability(State.TO_YARD, 0.0750),
                new TransitionProbability(State.TO_INDUSTRY, 0.0350),
                new TransitionProbability(State.BAD_ORDER, 0.0100)
            );
            case TO_SPUR -> pick(
                new TransitionProbability(State.AT_SPUR, 0.9800),
                new TransitionProbability(State.TO_INDUSTRY, 0.0100),
                new TransitionProbability(State.FROM_SPUR, 0.0100)
            );
            case AT_SPUR -> pick(
                new TransitionProbability(State.LOADING, 0.4800),
                new TransitionProbability(State.UNLOADING, 0.4800),
                new TransitionProbability(State.FROM_SPUR, 0.0350),
                new TransitionProbability(State.BAD_ORDER, 0.0050)
            );
            case FROM_SPUR -> pick(
                new TransitionProbability(State.TO_YARD, 0.7200),
                new TransitionProbability(State.TO_INDUSTRY, 0.1850),
                new TransitionProbability(State.AT_INDUSTRY, 0.0450),
                new TransitionProbability(State.TO_SPUR, 0.0500)
            );
            case LOADING -> pick(
                new TransitionProbability(State.FROM_SPUR, 0.8100),
                new TransitionProbability(State.AT_SPUR, 0.1700),
                new TransitionProbability(State.UNLOADING, 0.0200)
            );
            case UNLOADING -> pick(
                new TransitionProbability(State.FROM_SPUR, 0.8050),
                new TransitionProbability(State.AT_SPUR, 0.1200),
                new TransitionProbability(State.LOADING, 0.0750)
            );
            default -> null;
        };
    }
    
    private static State pick(TransitionProbability... transitionProbabilities) {
        double random = Math.random();
        double cumulative = 0.0;
        
        for (TransitionProbability tp : transitionProbabilities) {
            cumulative += tp.probability;
            if (random <= cumulative) {
                return tp.state;
            }
        }
        
        return transitionProbabilities[transitionProbabilities.length - 1].state;
    }
}