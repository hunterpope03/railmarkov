# railmarkov


### Overview

`railmarkov` is a Java application submitted as an Honors Program contract in the Spring 2026 semester. The application extends on the coursework of [CSCI 2830: Object-Oriented Software Engineering Fundamentals](https://catalog.unomaha.edu/search/?P=CSCI%202830) by applying OOP principles in Java to simulate aspects of a real-world transportation management system. 


### Summary 

`railmarkov` implements first-order [Markov chain](https://en.wikipedia.org/wiki/Markov_chain) modeling for anomaly detection to identify anomalous sequences of railcar state transitions. The process for such modeling can be defined as follows: 

1. **Define a finite set of railcar operational states**

    > _a railcar R can only be in one operational state S at any moment in time_
    
    See [railcar-operational-state-definitions.md](/docs/railcar-operational-state-definitions.md).

2. **Define allowed state transitions and mock transition probabilities for synthetic data generation**

    > _if a railcar R moves from one operational state S1 to another operational state S2, a state transition occurs_

    In real-world transportation management systems, operational state transitions would be streamed in real-time from external inputs. For simplification, this application internally generates sequences of operational state transitions, thus requiring allowed state transitions and mock transition probabilities to be defined before generation. Such restrictions on data generation make for a realistic simulation of transportation operations. 
    
    See [StateTransitionUtil.java](/src/main/java/railmarkov/datagen/util/StateTransitionUtil.java).

3. **Generate sequences of state transitions**

    Using the above restrictions, initialize a railcar and repeatedly transition states. Each railcar produces an ordered set of state transitions. Repeat across a number of railcars. 

    See [EventGenerator.java](/src/main/java/railmarkov/datagen/EventGenerator.java).

4. **Train a first-order Markov model**

    Aggregate all generated event sequences and count observed transitions between states. These counts represent empirical evidence of how frequently one state follows another in the simulated system.

    See [MarkovTrainer.java](/src/main/java/railmarkov/markov/MarkovTrainer.java).

5. **Normalize state transition counts to probabilities**

    Convert raw transition counts into probabilities by normalizing each row of the transition count matrix. Each row then represents the probability distribution of next states given a current state.

    See [TransitionMatrix.java](/src/main/java/railmarkov/markov/model/TransitionMatrix.java).

5. **Identify low-probability transitions as anomolous**

    Compare observed transitions against the learned transition probability matrix. Transitions with probabilities below some threshold can be reasonably defined as anomalous. 


### Requirements 

> _tested on Mac & Windows_

- Docker
- Docker Compose
- Java (openJDK)
- Maven


### Configuration Properties

> _see [config.properties](/src/main/resources/config.properties)_

- **CASSANDRA_HOST**: the Cassandra cluster's host
    - default is `localhost`
- **CASSANDRA_PORT**: the Cassandra cluster's port
    - default is `9042`
- **CASSANDRA_KEYSPACE**: the name of the Cassandra keyspace to create for this application
    - default is `railmarkov`
- **CASSANDRA_KEYSPACE_REPLICATION**: the replication pattern of the Cassandra keyspace
    - default is `{'class': 'SimpleStrategy', 'replication_factor': 1}` for simple replication
- **CASSANDRA_TABLE**: the name of the Cassandra table to create in the above keyspace for this application
    - default is `events`
- **DATAGEN_RAILCARS**: the number of railcars to generate events for in a single application run
    - default is `100`
- **DATAGEN_EVENTS_PER_RAILCAR**: the number of events to generate for each railcar in a single application run
    - default is `100`
- **MARKOV_TRANSITION_MATRIX_FILE**: the path of the transition matrix output file
    - default is `src/main/resources/transition_matrix.txt`

### Setup & Execution
To clone the repo: 
```
git clone https://github.com/hunterpope03/railmarkov
```

To setup the application: 
```
./scripts/setup.sh
```

To run the application: 
```
./scripts/run.sh
```