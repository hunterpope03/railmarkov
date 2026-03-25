#!/usr/bin/env bash


set -a
source "config/cassandra.env"
set +a

QUERY="CREATE TABLE IF NOT EXISTS $KEYSPACE.railcar_states (
    railcar_id uuid, 
    state text, 
    is_loaded boolean, 
    cycle int, 
    timestamp timestamp, 
    PRIMARY KEY ((railcar_id), timestamp)
);"

docker-compose exec -T cassandra cqlsh -e "$QUERY"