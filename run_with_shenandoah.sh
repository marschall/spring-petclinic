#!/bin/sh

set -e
set -u

${JAVA_HOME}/bin/java -Xms64m -Xmx512m \
  -XX:+UseShenandoahGC \
  -XX:+UseCompactObjectHeaders \
  -XX:ShenandoahGCHeuristics=compact \
  -XX:+UnlockExperimentalVMOptions \
  -XX:ShenandoahGuaranteedGCInterval=5000 \
  -XX:NativeMemoryTracking=summary \
  -jar target/*.jar