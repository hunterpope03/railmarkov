package railmarkov;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import railmarkov.config.Config;
import railmarkov.domain.Event;
import railmarkov.domain.State; 
import railmarkov.infrastructure.client.CassandraClient;
import railmarkov.infrastructure.repository.EventRepository;
import railmarkov.markov.MarkovTrainer;
import railmarkov.markov.model.TransitionMatrix;


public class MainTest {
    @Test
    public void testNormalizedMatrixRowsSumToOne() {
        CassandraClient cassandraClient = new CassandraClient(Config.CASSANDRA_HOST, Config.CASSANDRA_PORT, Config.CASSANDRA_KEYSPACE);

        try {
            cassandraClient.startup();
            EventRepository eventRepository = new EventRepository(cassandraClient, Config.CASSANDRA_TABLE);

            List<Event> allEvents = eventRepository.selectEvents();
            
            MarkovTrainer markovTrainer = new MarkovTrainer();
            TransitionMatrix transitionMatrix = markovTrainer.train(allEvents);

            transitionMatrix.normalize();

            double[][] matrix = transitionMatrix.getMatrix();
            State[] states = transitionMatrix.getStates();

            for (int i = 0; i < states.length; i++) {
                double rowSum = 0.0;

                for (int j = 0; j < states.length; j++) {
                    rowSum += matrix[i][j];
                }

                if (rowSum > 0) {
                    assertEquals(1.0, rowSum, 1e-9,
                        String.format("Row %s sums to %f, expected 1.0", states[i], rowSum));
                }

                System.out.printf("'%s' sums to %f\n\n", states[i], rowSum);
            }

        } finally {
            cassandraClient.shutdown();
        }
    }
}