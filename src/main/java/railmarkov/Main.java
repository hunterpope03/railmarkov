package railmarkov; 

import railmarkov.config.Config; 
import railmarkov.client.CassandraClient; 
import railmarkov.repository.EventRepository; 
import railmarkov.service.DataGenService; 
import railmarkov.service.MarkovService; 


public class Main {
    public static void main(String[] args) {
        CassandraClient cassandraClient = new CassandraClient(
            Config.CASSANDRA_HOST, 
            Config.CASSANDRA_PORT, 
            Config.CASSANDRA_KEYSPACE
        ); 
        cassandraClient.startup();
        System.out.printf("connected to Cassandra cluster at '%s:%d' and keyspace '%s'\n\n", Config.CASSANDRA_HOST, Config.CASSANDRA_PORT, Config.CASSANDRA_KEYSPACE); 

        EventRepository eventRepository = new EventRepository(
            cassandraClient, 
            Config.CASSANDRA_TABLE
        );

        DataGenService dataGenService = new DataGenService(
            eventRepository, 
            Config.DATAGEN_RAILCARS, 
            Config.DATAGEN_EVENTS_PER_RAILCAR
        ); 
        dataGenService.generate(); 
        dataGenService.write(); 

        MarkovService markovService = new MarkovService(eventRepository); 
        markovService.read(); 
        markovService.train();
        markovService.build();
        markovService.save();

        cassandraClient.shutdown(); 
        System.out.printf("disconnected from Cassandra cluster\n"); 
    }
}