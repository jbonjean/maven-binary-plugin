#!/bin/sh
target="/tmp/$(basename $0).jar"
trap "rm -f '$target'" INT
tail -n +20 "$0" > "$target"
JVM_OPTIONS=${JVM_OPTIONS:-}
while [ $# -ne 0 ]; do
	case $1 in
		-D*|-X*)
			JVM_OPTIONS="$JVM_OPTIONS $1"
			shift
			;;
		*)
			break
			;;
	esac
done
java JAVA_OPTIONS $JVM_OPTIONS -jar "$target" "$@"
rm -f "$target"
exit
