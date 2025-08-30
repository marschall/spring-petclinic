#!/bin/sh

set -u
set -e

JEXTRACT_HOME=${HOME}/bin/java/jextract-22/

${JEXTRACT_HOME}/bin/jextract \
  --output src/main/java \
  --target-package org.springframework.samples.petclinic.ffi \
  --header-class-name Mman \
  --include-function mmap \
  --include-constant PROT_READ \
  --include-constant PROT_WRITE \
  --include-constant MAP_ANON \
  --include-constant MAP_SHARED \
  /usr/include/x86_64-linux-gnu/sys/mman.h

${JEXTRACT_HOME}/bin/jextract \
  --output src/main/java \
  --target-package org.springframework.samples.petclinic.ffi \
  --header-class-name Unistd \
  --include-function getpagesize \
  /usr/include/unistd.h
