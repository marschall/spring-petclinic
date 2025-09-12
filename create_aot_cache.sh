#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -XX:AOTCacheOutput=spring-petclinic.aot \
  -jar target/extracted/*.jar

