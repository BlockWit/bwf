#!/bin/bash
# Ensure our service is enabled
PROJECT_HOME_DIR="/opt/bsc-eth-bridge"
PROJECT_HOME_CONFIG_DIR="$PROJECT_HOME_DIR/config"
PROJECT_HOME_CONFIG="$PROJECT_HOME_CONFIG_DIR/application.properties"
PROJECT_HOME_CONFIG_EXAMPLE="$PROJECT_HOME_CONFIG_DIR/application.properties.example"
PROJECT_USERNAME=bsc-eth-bridge
SERVICE="bsc-eth-bridge";

if [ -f "$PROJECT_HOME_CONFIG" ]; then
  echo "$SERVICE config file - $PROJECT_HOME_CONFIG - already exists. Please, update file corresponding with $PROJECT_HOME_CONFIG_EXAMPLE";
else
  echo "$SERVICE config file - $PROJECT_HOME_CONFIG not created before";
  cp $PROJECT_HOME_CONFIG_EXAMPLE $PROJECT_HOME_CONFIG;
  chown $PROJECT_USERNAME:$PROJECT_USERNAME $PROJECT_HOME_CONFIG;
  echo "Copy $PROJECT_HOME_CONFIG_EXAMPLE to $PROJECT_HOME_CONFIG, please configure it and start service";
fi

if [ ! -d $PROJECT_HOME_CONFIG_DIR ]; then
  mkdir -p $PROJECT_HOME_CONFIG_DIR;
  chown $PROJECT_USERNAME:$PROJECT_USERNAME -R $PROJECT_HOME_CONFIG_DIR;
fi

systemctl daemon-reload

if [ $1 -eq 1 ]; then
  echo "Service haven't installed before. Enable $SERVICE autostart after boot.";
  systemctl enable $SERVICE >/dev/null 2>&1 || :
  echo "Service autostart enabled";
else
  echo "Service installed before. Restarting $SERVICE ...";
  systemctl restart $SERVICE >/dev/null 2>&1 || :
  echo "Restart finished. If you update old version of $SERVICE you should check service status";
fi

echo "BSC <=> ETH bridge installed"
if [ $1 -eq 1 ]; then
  echo "Because service not installed before you should configure it and then start it"
fi


