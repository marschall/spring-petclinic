#!/bin/sh

set -u
set -e

docker container run \
  --name jvm-workshop-petclinic \
  --rm -it \
  --mount type=bind,src=./target/spring-petclinic-4.0.0-SNAPSHOT.jar,dst=/opt/spring-petclinic.jar,readonly \
  --mount type=bind,src=./docker/logs,dst=/var/log/spring-petclinic \
  -w /opt \
  --cpus 2 \
  --memory 1GB \
  --health-cmd 'curl --fail --head http://127.0.0.1:8080/actuator/health/liveness' \
  --health-start-period 1s \
  --health-start-interval 1s \
  --health-interval 10s \
  -p 127.0.0.1:8080:8080/tcp \
  amazoncorretto:25.0.1-al2023 \
  java \
  -Xms64m -Xmx512m \
  -XX:+UseSerialGC \
  -XX:+UseCompactObjectHeaders \
  -XX:FlightRecorderOptions=stackdepth=256 \
  -XX:StartFlightRecording:maxsize=10m,filename=/var/log/spring-petclinic/petclinic-profile.jfr,settings=profile \
  -Xlog:jfr+startup=error \
  -Xlog:gc*:/var/log/spring-petclinic/petclinic-gc.log \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/spring-petclinic \
  -jar /opt/spring-petclinic.jar

