#!/bin/bash

if [ -z "$1" ]
then
    echo "need branch............."
    exit 1;
fi

# 获取执行文件的路径
workspace=$(cd "$(dirname "$0")"; pwd)
echo "workspace is "$workspace

cd $workspace
    git push -u origian $1
cd -
echo "done.............."%