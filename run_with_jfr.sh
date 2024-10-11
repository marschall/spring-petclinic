#!/bin/sh

set -e
set -u

java -Xms64m -Xmx512m \
  -XX:+UseSerialGC \
  -XX:NativeMemoryTracking=summary \
  -XX:+UnlockDiagnosticVMOptions \
  -XX:+DebugNonSafepoints \
  -XX:FlightRecorderOptions=stackdepth=256 \
  -XX:StartFlightRecording:maxsize=10m,filename=petclinic-profile.jfr,settings=profile \
  -Xlog:jfr+startup=error \
  -jar target/*.jar