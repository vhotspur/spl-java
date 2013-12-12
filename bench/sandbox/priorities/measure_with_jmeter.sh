#!/bin/sh

#
# Copyright 2013 Charles University in Prague
# Copyright 2013 Vojtech Horky
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

SELF_HOME=`which -- "$0" 2>/dev/null`
# Maybe, we are running Bash
[ -z "$SELF_HOME" ] && SELF_HOME=`which -- "$BASH_SOURCE" 2>/dev/null`
SELF_HOME=`dirname -- "$SELF_HOME"`

JARS_PATH=$SELF_HOME/../../../out/jars
LIBS_PATH=$SELF_HOME/../../../libs

CONFIGURATIONS="none edf:10 edf:100 boost:10 boost:100"
JMETER_CONFIG="$SELF_HOME/graph.jmx"

run_server() {
	java \
		-javaagent:$JARS_PATH/spl-agent.jar=spl.class=cz.cuni.mff.d3s.spl.demo.sandbox.priorities.Monitor \
		-cp $LIBS_PATH/gral-core-0.9-SNAPSHOT.jar:$JARS_PATH/demos.jar \
		-XX:+UseThreadPriorities -XX:ThreadPriorityPolicy=42 \
		-Dadjust.strategy="$1" \
		-Dadjust.period="$2" \
		cz.cuni.mff.d3s.spl.demo.sandbox.priorities.Main &
}


run_and_measure() {
	echo; echo; echo
	echo "Running $1 ($2) with $3, results into $4."
	echo; echo;
	
	run_server "$1" "$2"
	java_pid=$!
	
	sleep 3
	
	temp_config=tmp_conf.$$.jmx
	sed \
		'/guiclass="RespTimeGraphVisualizer"/,/<\/ResultCollector>/s#<stringProp name="filename">\(.*\)</stringProp>#<stringProp name="filename">'$4'</stringProp>#' \
		<"$3" \
		>$temp_config
	
	jmeter -n -t "$temp_config"
	rm -f $temp_config
	
	echo "Killing the server."
	(	kill $java_pid
		kill $java_pid
		kill -9 $java_pid
		sleep 2
	) &>/dev/null
	
	echo; echo; echo; echo
}

for config in $CONFIGURATIONS; do
	strategy=`echo "$config" | cut '-d:' -f 1`
	period=`echo "$config" | cut '-d:' -f 2`
	if [ "$strategy" = "$period" ]; then
		period=0
	fi
	
	result_file="result_`basename $JMETER_CONFIG .jmx`_${strategy}_${period}ms.csv"
	
	if ! [ -e "$result_file" ]; then
		run_and_measure "$strategy" "$period" "$JMETER_CONFIG" "$result_file"
	fi
done
