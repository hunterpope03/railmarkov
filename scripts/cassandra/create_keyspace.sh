#!/usr/bin/env bash


set -a
source "config/cassandra.env"
set +a

QUERY="CREATE KEYSPACE IF NOT EXISTS $KEYSPACE WITH REPLICATION = $REPLICATION;"
docker-compose exec -T cassandra cqlsh -e "$QUERY"