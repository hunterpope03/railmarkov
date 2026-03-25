#!/usr/bin/env bash


set -a
source "config/cassandra.env"
set +a

QUERY="CREATE TABLE IF NOT EXISTS $KEYSPACE.$TABLE (
    railcar_id uuid, 
    state text, 
    timestamp timestamp, 
    PRIMARY KEY ((railcar_id), timestamp)
);"

docker-compose exec -T cassandra cqlsh -e "$QUERY"