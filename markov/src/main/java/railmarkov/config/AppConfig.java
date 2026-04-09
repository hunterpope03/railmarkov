package railmarkov.config; 

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {

    public final String CASSANDRA_KEYSPACE;
    public final String CASSANDRA_KEYSPACE_REPLICATION;
    public final String CASSANDRA_TABLE;
    public final int DATA_GEN_RAILCARS;
    public final int DATA_GEN_TRANSITIONS_PER_RAILCAR;

    public AppConfig() {
        Dotenv dotenv = Dotenv.configure()
                .directory("../config")
                .filename("app.env")
                .load();

        this.CASSANDRA_KEYSPACE = dotenv.get("CASSANDRA_KEYSPACE");
        this.CASSANDRA_KEYSPACE_REPLICATION = dotenv.get("CASSANDRA_KEYSPACE_REPLICATION");
        this.CASSANDRA_TABLE = dotenv.get("CASSANDRA_TABLE");
        this.DATA_GEN_RAILCARS = Integer.parseInt(dotenv.get("DATA_GEN_RAILCARS"));
        this.DATA_GEN_TRANSITIONS_PER_RAILCAR = Integer.parseInt(dotenv.get("DATA_GEN_TRANSITIONS_PER_RAILCAR"));
    }
}