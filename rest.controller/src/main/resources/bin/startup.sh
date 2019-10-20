#! /bin/sh

app_name=rest.controller 	#待启动的jar全称(需要修改此项)
MAIN_CLASS=cn.info.StartApp    	#启动类（需要修改此项）
START_PORT=30000

sh_dir=$(cd "$(dirname "$0")" ; pwd)
work_dir=$sh_dir/..
app_start_path=$work_dir/$app_name
pid_name=$work_dir/bin/$app_name.pid


#数据校验
if [[ "$1" == "" ]] ; then
  echo "参数不能为空，支持start、stop、restart、status、help等参数！"
  exit 1
fi


check_proc_status(){
  CPS_PID=$1
  if [ "$CPS_PID" != "" ] ;then
      CPS_PIDLIST=`ps -ef|grep $CPS_PID|grep -v grep|awk -F" " '{print $2}'`
  else
      CPS_PIDLIST=`ps -ef|grep "$CPS_PNAME"|grep -v grep|awk -F" " '{print $2}'`
  fi

  for CPS_i in `echo $CPS_PIDLIST`
  do
      if [ "$CPS_PID" = "" ] ;then
          CPS_i1="$CPS_PID"
      else
          CPS_i1="$CPS_i"
      fi

      if [ "$CPS_i1" = "$CPS_PID" ] ;then
          #kill -s 0 $CPS_i
          kill -0 $CPS_i >/dev/null 2>&1
          if [ $? != 0 ] ;then
              echo "[`date`] MC-10500: Process $i have Dead"
              kill -9 $CPS_i >/dev/null 2>&1

              return 1
          else
              #echo "[`date`] MC-10501: Process is alive"
              return 0
          fi
      fi
  done
  echo "[`date`] MC-10502: Process $CPS_i is not exists"
  return 1
}
_start(){
  echo "开始启动程序$app_name..."
#  nohup java -jar $app_start_path > /dev/null 2>&1 & new_pid=$!
  java -DSTART_PORT=${START_PORT} -cp ${JAVA_HOME}/lib/*:${work_dir}/lib/* ${MAIN_CLASS} >> /dev/null 2>&1 &
  echo "$!" > $pid_name
  chmod 777 $pid_name
  _status
}

_stop(){
  if [ -f "${pid_name}" ] ; then
    pid=`cat $pid_name`
    if [ "$pid" != "" ] ; then
      kill -9 $pid
      rm -rf $pid_name
      echo "stop success"
    fi
  else
	  echo "$app_name is not running……"
  fi
}

_status(){
  if [[ -f "$pid_name" ]];then
    pid=`cat $pid_name`
    check_proc_status pid >/dev/null

    #判断check_proc_status方法的退出状态
    if [ $? != 0 ];then
      echo "$app_name is running, pid=$pid"
    else
      echo "$app_name is not running……"
    fi
  else
	  echo "$app_name is not running……"
  fi
}

_restart(){
    echo "stoping ... "
    _stop
    echo "staring ..."
    _start
}

case "$1" in
  "start")
    _start
    ;;
  "stop")
    _stop
    ;;
  "restart")
    _stop
    _start
    ;;
  "status")
    _status
    ;;
  "help")
    echo "支持start、stop、restart、status、help等参数！"
    exit 0
    ;;
  *)
    echo "参数 $1 输入有误,支持start、stop、restart、status、help等参数！"
    exit 1;
esac

exit 0