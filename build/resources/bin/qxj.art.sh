#!/bin/bash
source /etc/profile
BIN_PATH=$(cd "$(dirname "$0")"; pwd)
WORK_HOME=$BIN_PATH/..
#项目名称
PRODUCT_NAME=ompQuickStart
#模块名称，若存在多个模块记得添加
MODULE1_NAME=helloWorld

get_pid()
{
    pid=`ps -ef | grep app.name=$PRODUCT_NAME-$1 | grep -v grep | awk '{print $2}'`
    if [ ! $pid ]
    then
        pid=0
    fi
}

print_state()
{
    get_pid $1
    if [ $pid -gt 0 ]
    then
        echo $1 running
    else
        echo $1 not running
        exit 1
    fi 
}

start_proc()
{
    get_pid $1
    if [ $pid -gt 0 ]
        then
        echo $1 already running
    else
        echo $1 start...
        $WORK_HOME/$1/bin/startup.sh --daemon
    fi 
}

stop_proc()
{
    get_pid $1
    if [ $pid -gt 0 ]
    then
        echo $1 kill...
        kill $pid
    else
        echo $1 not running
    fi
}

cmd=$1
if [ ! -n "$1" ]
then
    cmd='status'
fi

case $cmd in
status)
    print_state $MODULE1_NAME
    ;;
start)
    start_proc $MODULE1_NAME
    ;;
stop)
    stop_proc $MODULE1_NAME
    ;;
restart)
    stop_proc $MODULE1_NAME
    sleep 3
    start_proc $MODULE1_NAME
    ;;
esac
