#!/bin/sh

# Check idl
if ! type "idl" 2> /dev/null; then
  echo "idl command is missing, please run corba script before running this script."
  exit 1
fi

# Clean
echo "Clean workspace..."
make clean > /dev/null

# Build
echo "Compile..."
make test > /dev/null
if [ $? -ne 0 ]; then
    echo "Compilation failed. Aborted."
    exit 1
fi

# Run
echo "Running..."
jaco files.Serveur &
SERVER_PID=$!
sleep 3 # Wait until the server is ready
jaco files.test.Test
kill $SERVER_PID

exit 0
