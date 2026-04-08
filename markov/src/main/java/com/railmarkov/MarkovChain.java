package com.railmarkov; 

import java.util.List;

import com.railmarkov.cassandra.CassandraClient; 
import com.railmarkov.cassandra.EventRepository; 
import com.railmarkov.cassandra.entity.Event; 
import com.railmarkov.config.AppConfig; 


public class MarkovChain {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        CassandraClient cassandraClient = new CassandraClient("localhost", 9042, appConfig.CASSANDRA_KEYSPACE);
        EventRepository repository = new EventRepository(cassandraClient, appConfig.CASSANDRA_TABLE);

        List<Event> events = repository.getEvents();

        TransitionMatrix matrix = new TransitionMatrix();
        matrix.build(events);
        matrix.save("../data/transition_matrix.txt");

        cassandraClient.close();
    }
}