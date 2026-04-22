package railmarkov.service; 

import java.time.Instant; 
import java.util.ArrayList;
import java.util.List; 

import railmarkov.entity.Event; 
import railmarkov.entity.Railcar; 
import railmarkov.model.State; 
import railmarkov.repository.EventRepository; 
import railmarkov.util.StateTransitionUtil; 


public class DataGenService {
    private final EventRepository eventRepository; 
    private final int railcars; 
    private final int eventsPerRailcar; 
    private final List<Event> events; 

    public DataGenService(EventRepository eventRepository, int railcars, int eventsPerRailcar) {
        this.eventRepository = eventRepository; 
        this.railcars = railcars; 
        this.eventsPerRailcar = eventsPerRailcar;
        this.events = new ArrayList<>(); 
    }

    public void generate() {
        for (int i = 0; i < this.railcars; i++) {
            Railcar railcar = new Railcar(); 

            for (int j = 0; j < this.eventsPerRailcar; j++) {
                Event event = new Event(
                    railcar.getId(), 
                    railcar.getState(), 
                    Instant.now().plusSeconds(j * 300)
                );
                this.events.add(event);

                State nextState = StateTransitionUtil.transition(railcar.getState());
                if (nextState == State.RETIRED || nextState == null) {
                    break; 
                } 
                railcar.setState(nextState);
            }

        }

        System.out.printf("%d railcar events generated\n", this.events.size()); 
    }

    public void write() {
        for (Event e : this.events) {
            this.eventRepository.insertEvent(e);
        }

        System.out.printf("%d railcar events written to Cassandra\n\n", this.events.size()); 
    }
}