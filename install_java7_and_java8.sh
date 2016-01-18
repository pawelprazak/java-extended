#!/usr/bin/env bash

echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections

sudo add-apt-repository -y ppa:webupd8team/java && \
sudo apt-get update && \
sudo apt-get install -y oracle-java7-installer && \
sudo apt-get install -y oracle-java8-installer