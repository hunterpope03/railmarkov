#!/usr/bin/env bash


cd "$(dirname "$0")/.."
echo
mvn test -q \
  2> >(grep -v '^WARNING:' >&2)
echo