#!/usr/bin/env bash


set -euo pipefail


echo
echo 
echo -n "changing working directory to project root ... "
cd "$(dirname "$0")/.."
PROJECT_ROOT="$(pwd)"
echo "done" 
echo "at '$PROJECT_ROOT'"
echo


echo -n "loading application config ... "
CONFIG_FILE="$PROJECT_ROOT/src/main/resources/config.properties"
source "$CONFIG_FILE"
echo "done"
echo "config sourced from '$CONFIG_FILE'"
echo


echo -n "verifying 'Docker' requirement ... "
if ! docker -v >/dev/null 2>&1; then
    echo
    echo "ERROR: 'Docker' is not installed or is not accessible" >&2
    echo
    exit 1
else
  echo "done"
  echo "$(docker -v)"
  echo
fi 


echo -n "verifying 'Docker Compose' requirement ... "
if ! docker compose version >/dev/null 2>&1; then
    echo
    echo "ERROR: 'Docker Compose' is not installed or is not accessible" >&2
    echo
    exit 1
else
  echo "done"
  echo "$(docker compose version)"
  echo
fi 


echo -n "verifying 'Java' requirement ... "
if ! java -version >/dev/null 2>&1; then
    echo
    echo "ERROR: 'Java' is not installed or is not accessible" >&2
    echo
    exit 1
else
  echo "done"
  echo "$(java -version)"
fi 


echo -n "verifying 'Maven' requirement ... "
if ! mvn -v >/dev/null 2>&1; then
    echo
    echo "ERROR: 'Maven' is not installed or is not accessible" >&2
    echo
    exit 1
else
  echo "done"
  echo "$(mvn -v)\n"
  echo 
fi 


echo "starting Docker container 'cassandra' ... "
docker compose pull
docker compose up -d
docker compose ps
echo "done"
echo


echo -n "verifying Cassandra keyspace '$CASSANDRA_KEYSPACE' exists ... "
QUERY="CREATE KEYSPACE IF NOT EXISTS $CASSANDRA_KEYSPACE WITH REPLICATION = $CASSANDRA_KEYSPACE_REPLICATION;"
docker-compose exec -T cassandra cqlsh -e "$QUERY"
echo "done"
echo


echo -n "verifying Cassandra table '$CASSANDRA_KEYSPACE.$CASSANDRA_TABLE' exists ... "
QUERY="CREATE TABLE IF NOT EXISTS $CASSANDRA_KEYSPACE.$CASSANDRA_TABLE (
    railcar_id uuid, 
    state text, 
    timestamp timestamp, 
    PRIMARY KEY ((railcar_id), timestamp)
);"
docker-compose exec -T cassandra cqlsh -e "$QUERY"
echo "done"
echo


echo "application setup complete; run with './scripts/run.sh'"
echo 
echo