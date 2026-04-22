package railmarkov.repository;

import java.util.ArrayList; 
import java.util.List; 

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import railmarkov.client.CassandraClient; 
import railmarkov.entity.Event; 
import railmarkov.model.State; 


public class EventRepository {
    private final CassandraClient cassandraClient;
    private final String table;

    public EventRepository(CassandraClient client, String table) {
        this.cassandraClient = client;
        this.table = table; 
    }

    public List<Event> selectEvents() {
        List<Event> events = new ArrayList<>();

        SimpleStatement simpleStatement = SimpleStatement.builder("SELECT railcar_id, state, timestamp FROM " + this.table)
            .build(); 

        ResultSet resultSet = this.cassandraClient.execute(simpleStatement); 

        for (Row row: resultSet) {
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

    public void insertEvent(Event event) {
        SimpleStatement statement = SimpleStatement.builder("INSERT INTO " + this.table + " (railcar_id, state, timestamp) VALUES (?, ?, ?)")
            .addPositionalValue(event.getRailcarId())
            .addPositionalValue(event.getState().getCode())
            .addPositionalValue(event.getTimestamp())
            .build();

        this.cassandraClient.execute(statement);
    }
}