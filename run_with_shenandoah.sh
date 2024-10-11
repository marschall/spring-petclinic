#!/bin/sh

set -e
set -u

java -Xms64m -Xmx512m \
  -XX:+UseShenandoahGC \
  -XX:ShenandoahGCHeuristics=compact \
  -XX:+UnlockExperimentalVMOptions \
  -XX:ShenandoahGuaranteedGCInterval=5000 \
  -XX:NativeMemoryTracking=summary \
  -jar target/*.jar