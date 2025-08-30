#!/bin/sh

set -u
set -e

./mvnw package -DskipTests -Dcheckstyle.skip=true

if [ ! -f docker/hsdis/hsdis-aarch64.so ]; then
    curl --fail -o docker/hsdis/hsdis-aarch64.so --get "https://builds.shipilev.net/hsdis-custombuilt/hsdis-aarch64.so"
fi

if [ ! -f docker/hsdis/hsdis-amd64.so ]; then
    curl --fail -o docker/hsdis/hsdis-amd64.so --get "https://builds.shipilev.net/hsdis-custombuilt/hsdis-amd64.so"
fi

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -Djarmode=tools \
  -jar target/*.jar extract \
  --destination target/extracted
