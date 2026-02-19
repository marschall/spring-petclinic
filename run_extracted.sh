#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -jar target/extracted/*.jar

