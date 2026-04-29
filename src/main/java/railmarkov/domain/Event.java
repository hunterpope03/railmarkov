package railmarkov.domain; 

import java.time.Instant;
import java.util.UUID;


public class Event {
    private final UUID railcarId;
    private final State state;
    private final Instant timestamp;

    public Event(UUID railcarId, State state, Instant timestamp) {
        this.railcarId = railcarId;
        this.state = state;
        this.timestamp = timestamp;
    }

    public UUID getRailcarId() { 
        return this.railcarId; 
    }

    public State getState() { 
        return this.state; 
    }

    public Instant getTimestamp() { 
        return this.timestamp; 
    }
}