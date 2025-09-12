#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -XX:StartFlightRecording:filename=classloading-boot.jfr,dumponexit=true,settings=src/main/jfr/Classloading-only.jfc \
  -jar target/*.jar

#${JAVA_HOME}/bin/jfr view --verbose class-loaders classloading-extracted.jfr
