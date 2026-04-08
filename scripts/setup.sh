#!/usr/bin/env bash


set -euo pipefail


echo ""
echo -n "changing working directory to project root ... "
cd "$(dirname "$0")/.."
PROJECT_ROOT="$(pwd)"
mkdir -p "$PROJECT_ROOT/data"
echo "done"
echo -e "at '$PROJECT_ROOT'\n"


echo -n "sourcing environment ... "
set -a
source "$PROJECT_ROOT/config/setup.env"
set +a
echo "done"
echo -e "environment sourced from '$PROJECT_ROOT/config/setup.env'\n"


echo -n "checking Docker ... "
if ! docker -v >/dev/null 2>&1; then
    echo "ERROR: Docker is not installed or is not accessible" >&2
    exit 1
else
  echo "done"
  echo -e "$(docker -v)\n"
fi 


echo -n "checking Docker compose ... "
if ! docker compose version >/dev/null 2>&1; then
    echo "ERROR: Docker compose is not installed or is not accessible" >&2
    exit 1
else
  echo "done"
  echo -e "$(docker compose version)\n"
fi 


echo "starting Docker container ... "
docker compose pull
docker compose up -d
docker compose ps
echo -e "done\n"


echo -n "creating Cassandra keyspace ... "
source "./scripts/cassandra/create_keyspace.sh"
echo "done"
echo -e "Cassandra keyspace '$CASSANDRA_KEYSPACE' created\n"


echo -n "creating Cassandra table ... "
source "./scripts/cassandra/create_table.sh"
echo "done"
echo -e "Cassandra table '$CASSANDRA_KEYSPACE.$CASSANDRA_TABLE' created\n"


echo -n "checking Java ... "
if ! java -version >/dev/null 2>&1; then
    echo "ERROR: Java is not installed or is not accessible" >&2
    exit 1
else
  echo "done"
  echo -e "$(java -version)"
fi 


echo -n "checking Maven ... "
if ! mvn -v >/dev/null 2>&1; then
    echo "ERROR: Maven is not installed or is not accessible" >&2
    exit 1
else
  echo "done"
  echo -e "$(mvn -v)\n"
fi 


echo -n "initailizing Maven build ... "
cd markov/
mvn install -q
mvn clean compile -q
cd ../
echo -e "done\n"


if [ -n "$PYTHON_DIR" ]; then
    PYTHON_PATH="$PYTHON_DIR/$PYTHON"
else
    PYTHON_PATH="$PYTHON"
fi 

echo -n "checking Python ... "
if ! "$PYTHON_PATH" --version >/dev/null 2>&1; then
    echo "ERROR: Maven is not installed or is not accessible" >&2
    exit 1
else
  echo "done"
  echo -e "$("$PYTHON_PATH" --version)\n"
fi 


echo -n "checking Python venv ... "
export PIP_DISABLE_PIP_VERSION_CHECK=1
if [ ! -d "$PROJECT_ROOT/$VENV_DIR" ]; then
    "$PYTHON" -m venv "$PROJECT_ROOT/$VENV_DIR"
    echo "done"
    echo -e "Python venv created at '$PROJECT_ROOT/$VENV_DIR'\n"
else 
    echo "done"
    echo -e "Python venv already exists at '$PROJECT_ROOT/$VENV_DIR'\n"
fi


if [ -d "$PROJECT_ROOT/$VENV_DIR/bin" ]; then
    VENV_BIN="bin"
elif [ -d "$PROJECT_ROOT/$VENV_DIR/Scripts" ]; then     
    VENV_BIN="Scripts"
else
    echo "ERROR: unknown Python venv structure; expected 'bin' or 'Scripts' directory in '$PROJECT_ROOT/$VENV_DIR'"
    exit 1
fi


echo -n "upgrading pip ... "
"$PROJECT_ROOT/$VENV_DIR/$VENV_BIN/$PYTHON" -m pip install --upgrade pip -q
echo "done"
echo -e "$("$PROJECT_ROOT/$VENV_DIR/$VENV_BIN/$PYTHON" -m pip --version)\n"


echo -n "installing Python dependencies ... "
"$PROJECT_ROOT/$VENV_DIR/$VENV_BIN/$PYTHON" -m pip install -r requirements.txt -q
echo -e "done\n"


echo -e "setup complete\n"