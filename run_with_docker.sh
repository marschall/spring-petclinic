#!/bin/sh

set -u
set -e

docker container run \
  --name jvm-workshop-petclinic \
  --rm -it \
  --mount type=bind,src=./target/extracted,dst=/opt/spring-petclinic,readonly \
  --mount type=bind,src=./docker/logs,dst=/var/log/spring-petclinic \
  --mount type=bind,src=./docker/hsdis/hsdis-aarch64.so,dst=/usr/lib/jvm/java-25/lib/server/hsdis-aarch64.so,readonly \
  --mount type=bind,src=./docker/hsdis/hsdis-amd64.so,dst=/usr/lib/jvm/java-25/lib/server/hsdis-amd64.so,readonly \
  -w /opt/spring-petclinic \
  --cpus 2 \
  --memory 1GB \
  --memory-swap 1GB \
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
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/spring-petclinic \
  -Dspring.profiles.active=docker \
  -jar /opt/spring-petclinic/spring-petclinic-4.0.0-SNAPSHOT.jar
