#!/bin/bash
# Ensure our service is enabled
PROJECT_HOME_DIR="/opt/bsc-eth-bridge"
PROJECT_HOME_CONFIG_DIR="$PROJECT_HOME_DIR/config"
PROJECT_HOME_CONFIG="$PROJECT_HOME_CONFIG_DIR/application.properties"
PROJECT_HOME_CONFIG_EXAMPLE="$PROJECT_HOME_CONFIG_DIR/application.properties.example"
PROJECT_USERNAME=bsc-eth-bridge
SERVICE="bsc-eth-bridge";

systemctl stop $SERVICE >/dev/null 2>&1 || :
systemctl disable $SERVICE >/dev/null 2>&1 || :


