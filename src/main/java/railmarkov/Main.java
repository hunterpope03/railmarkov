package railmarkov; 

import java.io.BufferedWriter; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.util.List; 

import railmarkov.config.Config; 
import railmarkov.datagen.EventGenerator;
import railmarkov.domain.Event; 
import railmarkov.infrastructure.client.CassandraClient; 
import railmarkov.infrastructure.repository.EventRepository; 
import railmarkov.markov.MarkovTrainer;
import railmarkov.markov.model.TransitionMatrix; 
import railmarkov.markov.util.TransitionMatrixFormatter; 


public class Main {
    public static void main(String[] args) {
        CassandraClient cassandraClient = new CassandraClient(Config.CASSANDRA_HOST, Config.CASSANDRA_PORT, Config.CASSANDRA_KEYSPACE); 

        try {
            cassandraClient.startup();
            EventRepository eventRepository = new EventRepository(cassandraClient, Config.CASSANDRA_TABLE);
            System.out.printf("connected to Cassandra cluster at '%s:%d' and keyspace '%s'\n\n", Config.CASSANDRA_HOST, Config.CASSANDRA_PORT, Config.CASSANDRA_KEYSPACE); 

            EventGenerator eventGenerator = new EventGenerator();
            List<Event> newEvents = eventGenerator.generate(Config.DATAGEN_RAILCARS, Config.DATAGEN_EVENTS_PER_RAILCAR);
            System.out.printf("generated %d new railcar events\n\n", newEvents.size()); 

            for (Event e: newEvents) {
                eventRepository.insertEvent(e);
            }
            System.out.printf("inserted %d new railcar events into Cassandra table '%s:%s'\n\n", newEvents.size(), Config.CASSANDRA_KEYSPACE, Config.CASSANDRA_TABLE); 

            List<Event> allEvents = eventRepository.selectEvents();
            System.out.printf("read %d railcar events from Cassandra table '%s:%s'\n\n", allEvents.size(), Config.CASSANDRA_KEYSPACE, Config.CASSANDRA_TABLE); 

            MarkovTrainer markovTrainer = new MarkovTrainer();
            TransitionMatrix transitionMatrix = markovTrainer.train(allEvents);
            System.out.printf("Markov model trained\n\n"); 

            transitionMatrix.normalize();
            System.out.printf("transition matrix normalized\n\n");

            String formatted = TransitionMatrixFormatter.format(transitionMatrix);
            try (BufferedWriter w = new BufferedWriter(new FileWriter(Config.MARKOV_TRANSITION_MATRIX_FILE))) {
                w.write(formatted);
            } catch (IOException e) {
                throw new RuntimeException("failed to write transition matrix", e);
            }
            System.out.printf("transition matrix saved to '%s'\n\n", Config.MARKOV_TRANSITION_MATRIX_FILE);

        } finally {
            cassandraClient.shutdown();
            System.out.printf("disconnected from Cassandra cluster\n\n");
        }
    }
}
