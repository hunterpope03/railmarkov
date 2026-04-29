package railmarkov.config; 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


public class Config {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    public static final String CASSANDRA_HOST;
    public static final int CASSANDRA_PORT;
    public static final String CASSANDRA_KEYSPACE;
    public static final String CASSANDRA_REPLICATION;
    public static final String CASSANDRA_TABLE;
    public static final int DATAGEN_RAILCARS;
    public static final int DATAGEN_EVENTS_PER_RAILCAR;
    public static final String MARKOV_TRANSITION_MATRIX_FILE;

    static {
        Properties props = new Properties();

        try {
            props.load(Files.newInputStream(Paths.get(CONFIG_FILE)));
        } catch (IOException e) {
            throw new RuntimeException("failed to load 'config.properties'", e);
        }

        CASSANDRA_HOST = props.getProperty("CASSANDRA_HOST");
        CASSANDRA_PORT = Integer.parseInt(props.getProperty("CASSANDRA_PORT"));
        CASSANDRA_KEYSPACE = props.getProperty("CASSANDRA_KEYSPACE");
        CASSANDRA_REPLICATION = props.getProperty("CASSANDRA_REPLICATION");
        CASSANDRA_TABLE = props.getProperty("CASSANDRA_TABLE");
        DATAGEN_RAILCARS = Integer.parseInt(props.getProperty("DATAGEN_RAILCARS"));
        DATAGEN_EVENTS_PER_RAILCAR = Integer.parseInt(props.getProperty("DATAGEN_EVENTS_PER_RAILCAR"));
        MARKOV_TRANSITION_MATRIX_FILE = props.getProperty("MARKOV_TRANSITION_MATRIX_FILE");
    }
}