#!/usr/bin/env bash


set -e


# 01. change working directory to project root
echo -en "\nchanging working directory to project root ... "
cd "$(dirname "${BASH_SOURCE[0]}")/../"
PROJECT_ROOT=$(pwd)
echo "done"
echo -e "at '$PROJECT_ROOT'\n"


# 02. ensure docker daemon is running
echo -n "ensuring docker daemon is running ... "
if ! docker info > /dev/null 2>&1; then
    echo -e "\n[ERROR] the docker daemon is not running; start the docker desktop application and re-run this script.\n"
    exit 1
fi
echo -e "done\n" 


# 03. start docker container
echo -n "starting docker container ... "
if ! docker compose ps --services --filter "status=running" | grep -q cassandra; then
    docker compose up -d
    echo -e "done\n"
else
    echo "done"
    echo -e "container already running\n"
fi


# 04. create Cassandra keyspace
echo -n "creating Cassandra keyspace ... "
./scripts/cassandra/create_keyspace.sh
echo -e "done\n"


# 05. create Cassandra table
echo -n "creating Cassandra table ... "
./scripts/cassandra/create_table.sh
echo -e "done\n"


echo -e "setup complete\n"