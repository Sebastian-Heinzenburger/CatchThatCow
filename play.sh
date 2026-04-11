#!/bin/bash

# Catch That Cow - Start Script

echo "Building Catch That Cow..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo ""
    echo "Starting game..."
    echo ""

    # Build classpath
    CP="4-CatchThatCow-abstraction/target/classes"
    CP="$CP:3-CatchThatCow-domain/target/classes"
    CP="$CP:2-CatchThatCow-application/target/classes"
    CP="$CP:1-CatchThatCow-adapters/target/classes"
    CP="$CP:0-CatchThatCow-plugin/target/classes"
    CP="$CP:0-CatchThatCow-main/target/classes"

    java -cp "$CP" de.heinzenburger.Main
else
    echo "Build failed!"
    exit 1
fi
