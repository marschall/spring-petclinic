#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -jar target/*.jar

#${JAVA_HOME}/bin/jfr view --verbose class-loaders classloading-extracted.jfr
