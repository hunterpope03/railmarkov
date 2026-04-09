package railmarkov; 

import java.util.List;

import railmarkov.cassandra.CassandraClient; 
import railmarkov.cassandra.EventRepository; 
import railmarkov.entity.Event; 
import railmarkov.config.AppConfig; 
import railmarkov.io.TransitionMatrix; 


public class MarkovChain {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        CassandraClient cassandraClient = new CassandraClient("localhost", 9042, appConfig.CASSANDRA_KEYSPACE);
        EventRepository repository = new EventRepository(cassandraClient, appConfig.CASSANDRA_TABLE);

        List<Event> events = repository.getEvents();

        TransitionMatrix matrix = new TransitionMatrix();
        matrix.save(build(events), "../data/transition_matrix.txt");

        cassandraClient.close();
    }

    private static double[][] build(List<Event> events) {
        int size = railmarkov.entity.State.values().length;
        railmarkov.entity.State[] states = railmarkov.entity.State.values();
        int[][] counts = new int[size][size];

        java.util.Map<java.util.UUID, List<Event>> byRailcar = new java.util.LinkedHashMap<>();
        for (Event e : events) {
            byRailcar.computeIfAbsent(e.getRailcarId(), k -> new java.util.ArrayList<>()).add(e);
        }

        for (List<Event> railcarEvents : byRailcar.values()) {
            railcarEvents.sort(java.util.Comparator.comparing(Event::getTimestamp));
            for (int i = 0; i < railcarEvents.size() - 1; i++) {
                railmarkov.entity.State from = railcarEvents.get(i).getState();
                railmarkov.entity.State to = railcarEvents.get(i + 1).getState();
                counts[from.ordinal()][to.ordinal()]++;
            }
        }

        double[][] probs = new double[size][size];
        for (int i = 0; i < size; i++) {
            int rowSum = 0;
            for (int j = 0; j < size; j++) rowSum += counts[i][j];
            for (int j = 0; j < size; j++) {
                probs[i][j] = rowSum == 0 ? 0.0 : (double) counts[i][j] / rowSum;
            }
        }
        return probs;
    }
}