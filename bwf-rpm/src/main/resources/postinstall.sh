#!/bin/bash
# Ensure our service is enabled
COMMON_NAME="bwf";
USER_NAME=$COMMON_NAME;
GROUP_NAME=$COMMON_NAME;
SERVICE_NAME=$COMMON_NAME;

PROJECT_REL_FOLDER_NAME=$COMMON_NAME;
PROJECT_HOME_DIR="/opt/$PROJECT_REL_FOLDER_NAME";
PROJECT_HOME_CONFIG_DIR="$PROJECT_HOME_DIR/config";
PROJECT_HOME_CONFIG="$PROJECT_HOME_CONFIG_DIR/application.properties";
PROJECT_HOME_CONFIG_EXAMPLE="$PROJECT_HOME_CONFIG_DIR/application.properties.example";
PROJECT_USERNAME=$USER_NAME;


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

systemctl daemon-reload;

if [ $1 -eq 1 ]; then
  echo "Service haven't installed before. Enabling $SERVICE autostart after boot...";
  systemctl enable $SERVICE >/dev/null 2>&1 || :
  echo "Service autostart enabled";
else
  echo "Service installed before. Restarting $SERVICE ...";
  systemctl restart $SERVICE >/dev/null 2>&1 || :
  echo "Restart finished. If you update old version of $SERVICE you should check service status";
fi

echo "Service $SERVICE successfully installed!"
if [ $1 -eq 1 ]; then
  echo "This is first installation of service $SERVICE.";
  echo "You should configure service properties in this file $PROJECT_HOME_CONFIG.";
  echo "And then run start service command: sudo systemctl start $SERVICE";
fi


