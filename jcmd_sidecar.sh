#!/bin/sh

set -u
set -e

docker container run \
  --name jcmd-sidecar \
  --rm -it \
  --pid=container:jvm-workshop-petclinic \
  --cpus 1 \
  --memory 100MB \
  --memory-swap 100MB \
  amazoncorretto:25.0.1-al2023 \
  /bin/sh
