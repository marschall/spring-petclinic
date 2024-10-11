#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java -Xms64m -Xmx512m \
  -XX:+UseZGC \
  -XX:+UseCompactObjectHeaders \
  -XX:ZUncommitDelay=5 \
  -XX:NativeMemoryTracking=summary \
  -jar target/*.jar