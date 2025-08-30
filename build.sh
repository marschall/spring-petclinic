#!/bin/sh

set -u
set -e

./mvnw package -DskipTests -Dcheckstyle.skip=true

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -Djarmode=tools \
  -jar target/*.jar extract \
  --destination target/extracted
