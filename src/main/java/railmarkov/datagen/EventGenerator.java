package railmarkov.datagen; 

import java.time.Instant; 
import java.util.ArrayList;
import java.util.List; 

import railmarkov.domain.Event; 
import railmarkov.domain.Railcar; 
import railmarkov.domain.State; 
import railmarkov.datagen.util.StateTransitionUtil; 


public class EventGenerator {
    public List<Event> generate(int railcars, int eventsPerRailcar) {
        List<Event> events = new ArrayList<>(); 

        for (int i = 0; i < railcars; i++) {
            Railcar railcar = new Railcar(); 

            for (int j = 0; j < eventsPerRailcar; j++) {
                Event event = new Event(
                    railcar.getId(), 
                    railcar.getState(), 
                    Instant.now().plusSeconds(j * 300)
                );
                events.add(event);

                if (railcar.getState() == State.RETIRED || railcar.getState() == null) {
                    break; 
                } 

                State nextState = StateTransitionUtil.transition(railcar.getState());
                railcar.setState(nextState);
            }
        }

        return events;
    }
}