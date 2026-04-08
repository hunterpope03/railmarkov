#!/usr/bin/env bash


set -a
source "config/app.env"
set +a


QUERY="CREATE TABLE IF NOT EXISTS $CASSANDRA_KEYSPACE.$CASSANDRA_TABLE (
    railcar_id uuid, 
    state text, 
    timestamp timestamp, 
    PRIMARY KEY ((railcar_id), timestamp)
);"
docker-compose exec -T cassandra cqlsh -e "$QUERY"