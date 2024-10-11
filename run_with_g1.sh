#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java -Xms64m -Xmx512m \
  -XX:+UseG1GC \
  -XX:+UseCompactObjectHeaders \
  -XX:G1PeriodicGCInterval=5000 \
  -XX:NativeMemoryTracking=summary \
  -jar target/*.jar
