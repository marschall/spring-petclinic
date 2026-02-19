#!/bin/sh

set -e
set -u


#  -XX:AOTMode=on \
#  -XX:StartFlightRecording:filename=classloading-boot.jfr,dumponexit=true,settings=src/main/jfr/Classloading-only.jfc \

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -XX:AOTCache=spring-petclinic.aot \
  -jar target/extracted/*.jar

