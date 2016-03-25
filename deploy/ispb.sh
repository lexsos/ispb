#!/bin/sh

SERVICE_NAME=ISPB
PATH_TO_JAR=/opt/ispb/ispb.jar
PID_PATH_NAME=/tmp/ispb-pid
ADDITIONAL_PARAMETERS=""

start () {
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java $ADDITIONAL_PARAMETERS -jar $PATH_TO_JAR  /tmp 2>> /dev/null >> /dev/null & echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
}

stop () {
       if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
}

case $1 in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
    *)
        echo "Usage: ispb.sh (start|stop|restart)"
    ;;
esac 
