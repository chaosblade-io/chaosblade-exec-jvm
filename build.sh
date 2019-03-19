#!/usr/bin/env bash
#set -x
path=$(cd `dirname $0`; pwd)
pluginsPath=${path}/plugins

if [ -e $pluginsPath ]; then
    rm -rf ${pluginsPath}
fi

mkdir ${pluginsPath}

mvn clean package -Dmaven.test.skip=true -U
cp ${path}/chaosblade-exec-plugin/chaosblade-exec-plugin-*/target/chaosblade-exec-plugin-*.jar ${pluginsPath}

mvn clean assembly:assembly -Dmaven.test.skip=true -U