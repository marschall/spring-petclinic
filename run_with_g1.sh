#!/bin/sh

set -e
set -u

java -Xms64m -Xmx512m \
  -XX:+UseG1GC \
  -XX:G1PeriodicGCInterval=5000 \
  -XX:NativeMemoryTracking=summary \
  -jar target/*.jar