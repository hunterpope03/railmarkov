#!/usr/bin/env bash


set -euo pipefail


echo ""
echo -n "changing working directory to project root ... "
cd "$(dirname "$0")/.."
PROJECT_ROOT="$(pwd)"
echo "done"
echo -e "at '$PROJECT_ROOT'\n"


echo -n "sourcing environment ... "
set -a
source "$PROJECT_ROOT/config/setup.env"
set +a
echo "done"
echo -e "environment sourced from '$PROJECT_ROOT/config/setup.env'\n"


if [ -d "$PROJECT_ROOT/$VENV_DIR/bin" ]; then
    VENV_BIN="bin"
elif [ -d "$PROJECT_ROOT/$VENV_DIR/Scripts" ]; then     
    VENV_BIN="Scripts"
else
    echo "ERROR: unknown Python venv structure; expected 'bin' or 'Scripts' directory in '$PROJECT_ROOT/$VENV_DIR'"
    exit 1
fi


echo -n "generating railcar events ... "
"$PROJECT_ROOT/$VENV_DIR/$VENV_BIN/$PYTHON" -m  data_gen.scripts.generate
echo -e "done\n"


echo "running markov model ... "
cd markov/
mvn clean compile -q
mvn exec:java -q
cd ../
echo -e "done\n"


echo -n "checking transition matrix ... "
if [ -f "$PROJECT_ROOT/data/transition_matrix.txt" ]; then
    echo "done" 
    echo -e "transition matrix exists at '$PROJECT_ROOT/data/transition_matrix.txt'\n"
else 
    echo "ERROR: transition matrix not found at '$PROJECT_ROOT/data/transition_matrix.txt'"
    exit 1
fi


cat "$PROJECT_ROOT/data/transition_matrix.txt"
echo -e "\n"