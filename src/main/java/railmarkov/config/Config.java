package railmarkov.config; 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


public class Config {
    public static final String CASSANDRA_HOST;
    public static final int CASSANDRA_PORT;
    public static final String CASSANDRA_KEYSPACE;
    public static final String CASSANDRA_REPLICATION;
    public static final String CASSANDRA_TABLE;
    public static final int DATAGEN_RAILCARS;
    public static final int DATAGEN_EVENTS_PER_RAILCAR;

    static {
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(Paths.get("src/main/resources/config.properties")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        CASSANDRA_HOST = props.getProperty("cassandra.host");
        CASSANDRA_PORT = Integer.parseInt(props.getProperty("cassandra.port"));
        CASSANDRA_KEYSPACE = props.getProperty("cassandra.keyspace");
        CASSANDRA_REPLICATION = props.getProperty("cassandra.replication");
        CASSANDRA_TABLE = props.getProperty("cassandra.table");
        DATAGEN_RAILCARS = Integer.parseInt(props.getProperty("datagen.railcars"));
        DATAGEN_EVENTS_PER_RAILCAR = Integer.parseInt(props.getProperty("datagen.events_per_railcar"));
    }
}