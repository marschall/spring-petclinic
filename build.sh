#!/bin/sh

./mvnw package

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -Djarmode=tools \
  -jar target/*.jar extract \
  --destination target/extracted

