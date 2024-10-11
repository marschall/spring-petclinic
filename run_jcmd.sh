#!/bin/sh

set -e
set -u

docker container exec -it jvm-workshop-petclinic jcmd 1 "$@"
