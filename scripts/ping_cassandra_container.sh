#!/usr/bin/env bash


echo ""
docker compose exec cassandra cqlsh -e "SELECT now() FROM system.local;" &> /dev/null
if [ $? -eq 0 ]; then
    echo "cassandra is running"
else
    echo "cassandra is down"
fi
docker compose ps
echo ""
