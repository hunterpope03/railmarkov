package railmarkov.domain; 

import java.util.UUID;


public class Railcar {
    private final UUID id;
    private State state;

    public Railcar() {
        this.id = UUID.randomUUID(); 
        this.state = State.ACQUIRED; 
    }

    public UUID getId() {
        return this.id; 
    }

    public State getState() {
        return this.state; 
    }

    public void setState(State state) {
        this.state = state; 
    }
}