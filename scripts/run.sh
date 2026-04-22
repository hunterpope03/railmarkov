#!/usr/bin/env bash


cd "$(dirname "$0")/.."
echo
mvn clean javafx:run -q \
  2> >(grep -v '^WARNING:' >&2)
echo
echo "transition matrix: "
cat src/main/resources/transition_matrix.txt
echo 
echo