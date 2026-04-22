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
CONFIG="$PROJECT_ROOT/src/main/resources/config.properties"
eval "$(sed "s/\./_/g; s/^/export /; s/=\(.*\)$/=\"\1\"/" "$CONFIG")"
echo "done"
echo "config sourced from '$CONFIG'"
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


echo -n "verifying Cassandra keyspace '$cassandra_keyspace' exists ... "
QUERY="CREATE KEYSPACE IF NOT EXISTS $cassandra_keyspace WITH REPLICATION = $cassandra_replication;"
docker-compose exec -T cassandra cqlsh -e "$QUERY"
echo "done"
echo


echo -n "verifying Cassandra table '$cassandra_table' exists ... "
QUERY="CREATE TABLE IF NOT EXISTS $cassandra_keyspace.$cassandra_table (
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