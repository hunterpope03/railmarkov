package com.railmarkov.cassandra; 

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import com.railmarkov.cassandra.entity.Event; 
import com.railmarkov.cassandra.entity.State; 


public class EventRepository {
    private final CassandraClient client;
    private final String table;

    public EventRepository(CassandraClient client, String table) {
        this.client = client;
        this.table = table; 
    }

    public List<Event> getEvents() {
        ResultSet resultSet = client.executeQuery(
            SimpleStatement.builder("SELECT railcar_id, state, timestamp FROM " + this.table)
                .setPageSize(1000)
                .build()
        );

        List<Event> events = new ArrayList<>();

        for (Row row : resultSet) {
            events.add(
                new Event(
                    row.getUuid("railcar_id"),
                    State.fromCode(row.getString("state")),
                    row.getInstant("timestamp")
                )
            );
        }

        return events;
    }
}