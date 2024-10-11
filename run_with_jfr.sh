#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java -Xms64m -Xmx512m \
  -XX:+UseSerialGC \
  -XX:+UseCompactObjectHeaders \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=./local/logs \
  -XX:FlightRecorderOptions=stackdepth=256 \
  -XX:StartFlightRecording:maxsize=10m,filename=./local/logs/petclinic-profile.jfr,settings=./profiling-plus-tlab.jfc,dumponexit=true \
  -Xlog:jfr+startup=error \
  -Xlog:gc*,safepoint:./local/logs/petclinic-gc.log::filecount=10,filesize=100M \
  -jar target/extracted/*.jar

