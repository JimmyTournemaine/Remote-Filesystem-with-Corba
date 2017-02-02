#!/bin/sh

# Clean Build
echo "Compile..."
ant compile
if [ $? -ne 0 ]; then
    echo "Compilation failed. Aborted."
    exit 1
fi

# Run
echo "Running..."
jaco files.Serveur &
sleep 1
jaco files.console.Console
jaco admin.ShutdownClient

exit 0
