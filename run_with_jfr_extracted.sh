#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -XX:StartFlightRecording:filename=classloading-extracted.jfr,dumponexit=true,settings=src/main/jfr/Classloading-only.jfc \
  -jar target/extracted/*.jar

