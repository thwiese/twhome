#!/usr/bin/env bash
/sbin/init 3
export ENABLE_GLUON_COMMERCIAL_EXTENSIONS=true
java \
  -Degl.displayid=/dev/dri/card0 \
  -Dmonocle.egl.lib=/opt/javafx-sdk-17/lib/libgluon_drm-1.1.6.so \
  -Djava.library.path=/opt/javafx-sdk-17/lib \
  -Dmonocle.platform.traceConfig=false \
  -Dprism.verbose=false \
  -Djavafx.verbose=false \
  -Dmonocle.platform=EGL \
  --module-path .:/opt/javafx-sdk-17/lib \
  --add-modules javafx.controls \
  -jar ../target/twhome-fx.jar --configLocation=/home/pi/twhome.properties $@
/sbin/init 5
