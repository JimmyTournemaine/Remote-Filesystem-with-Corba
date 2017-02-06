#!/bin/sh

# Clean Build
ant compile
if [ $? -ne 0 ]; then
    echo "Compilation failed. Aborted."
    exit 1
fi

# Run
echo "Running..."
jaco files.Serveur &
sleep 1
jaco files.ui.directory.ClientUI
jaco admin.ShutdownClient

exit 0

