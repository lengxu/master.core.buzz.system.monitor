#!/bin/bash
BIN_PATH=$(cd "$(dirname "$0")"; pwd)
#项目名称
PRODUCT_NAME=ompQuickStart
#模块名称，若存在多个模块记得添加
MODULE1_NAME=helloWorld
#MODULE2_NAME=modeule2
#配置文件名称
PROPERTIES_FILE=cbsm-qxj.art.properties
#运行目录
runngin_basepath=/opt/uyun/platform/$PRODUCT_NAME
#运行文件
SH_FILE=qxj.art.sh
#配置文件路径
PROPERTIES_PATH=$runngin_basepath/conf/$PROPERTIES_FILE
#安装前检测是否有该项目进程运行，若有则杀掉。
if [ -n "$(ps aux | grep app.name=$PRODUCT_NAME| grep -v grep)" ];then
    pkill -f app.name=$PRODUCT_NAME
fi
#去除037权限
umask  037
#删除并重建项目路径
rm -rf $runngin_basepath
mkdir -p $runngin_basepath

#引用uyun.soft:util-shell 的两个工具脚本
source ./getopt.sh
source ./util.sh
#下载公共配置文件 common.properties
load_common_item

#tar包解压后，拷贝资源文件至运行目录下
\cp -r $PRODUCT_NAME/$MODULE1_NAME bin conf  $runngin_basepath
\cp set-env.sh $runngin_basepath/bin
#更改目录所属用户、组 为OMP 传来的运行用户、组
chown -R $RUNNING_USER:$RUNNING_USER $runngin_basepath
#调用util.sh 的方法，更改目录权限
chmod_dir $runngin_basepath

# 上传配置文件
upload_file $PROPERTIES_PATH

#用运行用户身份运行
su $RUNNING_USER -c "sh $runngin_basepath/bin/$SH_FILE start"
#检测模块的端口是否打开，若存在多个模块记得添加
check_stat $PRODUCT_NAME-$MODULE1_NAME 17904
#check_stat $PRODUCT_NAME-$MODULE2_NAME 17905

echo "$PRODUCT_NAME Started!"
