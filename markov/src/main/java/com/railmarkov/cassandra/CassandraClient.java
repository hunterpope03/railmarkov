package com.railmarkov.cassandra; 

import java.net.InetSocketAddress;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;


public class CassandraClient {
    private final CqlSession session;

    public CassandraClient(String host, int port, String keyspace) {
        this.session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress(host, port))
            .withKeyspace(keyspace)
            .withLocalDatacenter("datacenter1")
            .build();
    }

    public ResultSet executeQuery(SimpleStatement statement) {
        return session.execute(statement);
    }

    public void close() {
        session.close();
    }
}