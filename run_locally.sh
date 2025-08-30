#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java -Xms64m -Xmx512m \
  -XX:+UseSerialGC \
  -jar target/*.jar
