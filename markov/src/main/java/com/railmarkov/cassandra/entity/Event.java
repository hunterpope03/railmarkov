package com.railmarkov.cassandra.entity;

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

    public UUID getRailcarId() { return railcarId; }
    public State getState() { return state; }
    public Instant getTimestamp() { return timestamp; }
}