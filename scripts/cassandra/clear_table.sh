#!/usr/bin/env bash


set -a
source "config/app.env"
set +a


QUERY="TRUNCATE TABLE $CASSANDRA_KEYSPACE.$CASSANDRA_TABLE;"
docker-compose exec -T cassandra cqlsh -e "$QUERY"