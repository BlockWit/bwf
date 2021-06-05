#!/bin/bash

COMMON_NAME="bwf";
USER_NAME=$COMMON_NAME;
GROUP_NAME=$COMMON_NAME;
SERVICE_NAME=$COMMON_NAME;

# Ensure our service is enabled
PROJECT_REL_FOLDER_NAME=$COMMON_NAME;
PROJECT_HOME_DIR="/opt/$PROJECT_REL_FOLDER_NAME";

PROJECT_HOME_CONFIG_DIR="$PROJECT_HOME_DIR/config";
PROJECT_HOME_CONFIG="$PROJECT_HOME_CONFIG_DIR/application.properties";
PROJECT_HOME_CONFIG_EXAMPLE="$PROJECT_HOME_CONFIG_DIR/application.properties.example";
PROJECT_USERNAME=$USER_NAME;
SERVICE=$SERVICE_NAME;

systemctl stop $SERVICE >/dev/null 2>&1 || :
systemctl disable $SERVICE >/dev/null 2>&1 || :


