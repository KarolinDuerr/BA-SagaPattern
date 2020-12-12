#!/bin/bash
# taken from https://github.com/Netflix/conductor/tree/master/docker/serverAndUI
# and from https://github.com/Netflix/conductor/tree/master/docker/server

echo "Starting Conductor server and UI"

# Start the UI
cd /app/ui/dist
if [ -z "$WF_SERVER" ];
  then
    export WF_SERVER=http://localhost:8080/api/
  else
    echo "using Conductor API server from '$WF_SERVER'"
fi

nohup node server.js 1>&2 > /app/logs/ui.log &

# Start the server
cd /app/libs
echo "Property file: $CONFIG_PROP"
echo $CONFIG_PROP
export config_file=

if [ -z "$CONFIG_PROP" ];
  then
    echo "Using an in-memory instance of conductor";
    export config_file=/app/config/config-local.properties
  else
    echo "Using '$CONFIG_PROP'";
    export config_file=/app/config/$CONFIG_PROP
fi

echo "Log4j file: $LOG4J_PROP"
echo $LOG4J_PROP
export log4j_file=

if [ -z "$LOG4J_PROP" ];
  then
    export log4j_file=/app/config/log4j.properties
  else
    echo "Using '$LOG4J_PROP'";
    export log4j_file=/app/config/$LOG4J_PROP
fi

nohup java -jar conductor-server-*-all.jar $config_file $log4j_file