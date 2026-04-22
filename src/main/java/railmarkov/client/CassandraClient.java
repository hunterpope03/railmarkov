package railmarkov.client; 

import java.net.InetSocketAddress;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;


public class CassandraClient {
    private final String host; 
    private final int port; 
    private final String keyspace; 
    private CqlSession session;

    public CassandraClient(String host, int port, String keyspace) {
        this.host = host; 
        this.port = port; 
        this.keyspace = keyspace; 
    }

    public void startup() {
        this.session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress(this.host, this.port))
            .withKeyspace(this.keyspace)
            .withLocalDatacenter("datacenter1")
            .build();
    }

    public ResultSet execute(SimpleStatement statement) {
        return session.execute(statement);
    }

    public void shutdown() {
        session.close();
    }
}