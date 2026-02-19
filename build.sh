#!/bin/sh

./mvnw package -Dcheckstyle.skip=true

# unzip -p target/extracted/spring-petclinic-4.0.0-SNAPSHOT.jar META-INF/MANIFEST.MF

${JAVA_HOME}/bin/java \
  -Xmx512m \
  -XX:+UseSerialGC \
  -Djarmode=tools \
  -jar target/*.jar extract \
  --destination target/extracted

