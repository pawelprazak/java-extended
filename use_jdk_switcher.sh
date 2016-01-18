#!/usr/bin/env bash

wget -O jdk_switcher.sh https://raw.githubusercontent.com/michaelklishin/jdk_switcher/master/jdk_switcher.sh

source ./jdk_switcher.sh

export JAVA7_HOME=$(jdk_switcher home oraclejdk7)
export JAVA8_HOME=$(jdk_switcher home oraclejdk8)

echo
echo Avaliable JDK versions
update-java-alternatives -l
echo
echo Exported variables:
echo JAVA7_HOME=$JAVA7_HOME
echo JAVA8_HOME=$JAVA8_HOME
echo

rm jdk_switcher.sh