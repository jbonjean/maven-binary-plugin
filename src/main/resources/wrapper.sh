#!/bin/sh
target="/tmp/$(basename $0).jar"
trap "rm -f '$target'" INT
tail -n +8 "$0" > "$target"
java JAVA_OPTIONS -jar "$target" "$@"
rm -f "$target"
exit
