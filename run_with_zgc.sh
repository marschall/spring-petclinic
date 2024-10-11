#!/bin/sh

set -e
set -u

java -Xms64m -Xmx512m \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -XX:ZUncommitDelay=5 \
  -XX:NativeMemoryTracking=summary \
  -jar target/*.jar