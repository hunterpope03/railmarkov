import cassandra.cluster

from data_gen.src.event import Event


class CassandraClient: 
    def __init__(
        self, 
        host: str, 
        port: int, 
        keyspace: str, 
        table: str
    ): 
        self.host = host
        self.port = port
        self.keyspace = keyspace
        
        self.cluster = cassandra.cluster.Cluster([host], port = port)
        self.session = self.cluster.connect(keyspace)

        self.insert_query = self.session.prepare(
            f"""
                INSERT INTO {table} (railcar_id, state, timestamp)
                VALUES (?, ?, ?)
            """
        )

    def insert_event(self, event: Event):
        self.session.execute(
            query = self.insert_query, 
            parameters = (
                event.railcar_id,
                event.state.value,
                event.timestamp
            )
        )