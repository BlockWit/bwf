## INFO

### Installing

1. sudo rpm -ivh --nodeps /path/to/rpm/bsc-eth-bridge-package-0.0.0.6-SNAPSHOT20210509135823.noarch.rpm
2. create database   
3. update config (database options) - /opt/bsc-eth-bridge/config/application.properties
4. start service: sudo systemctl start bsc-eth-bridge
5. check: sudo systemctl status bsc-eth-bridge

### Removing:

sudo rpm -e bsc-eth-bridge-package-0.0.0.6-SNAPSHOT20210509135823.noarch

Then you 
