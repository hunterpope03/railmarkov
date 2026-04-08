import datetime
import random
import uuid

from data_gen.src.cassandra import CassandraClient
from data_gen.src.config import Config
from data_gen.src.event import Event
from data_gen.src.railcar import Railcar
from data_gen.src.state import State


if __name__ == "__main__": 
    run_id = uuid.uuid4()

    config = Config()
    cassandra_client = CassandraClient(
        host = "localhost",
        port = 9042, 
        keyspace = config.app.CASSANDRA_KEYSPACE, 
        table = config.app.CASSANDRA_TABLE
    )

    events = []

    for _ in range(config.app.DATA_GEN_RAILCARS): 
        railcar_id = uuid.uuid5(run_id, str(uuid.uuid4()))

        railcar = Railcar(id = railcar_id)
        railcar.state = State.ACQUIRED
        railcar.timestamp = datetime.datetime.now()

        for _ in range(config.app.DATA_GEN_TRANSITIONS_PER_RAILCAR): 
            event = Event(
                railcar_id = railcar.id, 
                state = railcar.state, 
                timestamp = railcar.timestamp
            )
            events.append(event)

            transitionable_states = config.state_transition_probabilities.get(railcar.state)

            if transitionable_states is None: 
                break

            population = list(transitionable_states.keys())
            weights = list(transitionable_states.values())

            next_state = random.choices(
                population = population, 
                weights = weights, 
                k = 1
            )[0]

            railcar.state = State(next_state)
            railcar.timestamp += datetime.timedelta(minutes=random.randint(1, 60))

    for event in events:
        cassandra_client.insert_event(event)