#!/bin/bash
# Add the "demo-application" group and user
PROJECT_HOME_DIR="/opt/bsc-eth-bridge"
PROJECT_HOME_CONFIG_DIR="$PROJECT_HOME_DIR/config"
PROJECT_HOME_LOGS_DIR="$PROJECT_HOME_DIR/logs"
PROJECT_USERNAME=bsc-eth-bridge

/usr/sbin/useradd -c $PROJECT_USERNAME -U -s /sbin/nologin -r -d $PROJECT_HOME_DIR $PROJECT_USERNAME 2> /dev/null || :

if [ ! -d $PROJECT_HOME_CONFIG_DIR ]; then
  mkdir -p $PROJECT_HOME_CONFIG_DIR;
  chown $PROJECT_USERNAME:$PROJECT_USERNAME -R $PROJECT_HOME_CONFIG_DIR;
fi

if [ ! -d $PROJECT_HOME_LOGS_DIR ]; then
  mkdir -p $PROJECT_HOME_LOGS_DIR;
  chown $PROJECT_USERNAME:$PROJECT_USERNAME -R $PROJECT_HOME_LOGS_DIR;
fi