/*
 * Copyright 2013 Charles University in Prague
 * Copyright 2013 Vojtech Horky
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.cuni.mff.d3s.spl.probe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class NonLocalStopwatch {
	private static Map<Probe, Map<Object, MyStopwatch>> inProgress = new HashMap<>();
	/* Separate map for nulls as concurrent hash maps does not allow to store null. */
	private static Map<Probe, MyStopwatch> inProgressNullIds = new ConcurrentHashMap<>();
	
	public static void start(Probe probe, Object id, Object... args) {
		if (!probe.isActive(args)) {
			return;
		}
		
		MyStopwatch stopwatch = new MyStopwatch();
		
		if (id != null) {
			getProbeStopwatches(probe).put(id, stopwatch);
		} else {
			inProgressNullIds.put(probe, stopwatch);
		}
		
		stopwatch.start();
	}
	
	public static void stop(Probe probe, Object id, Object... args) {
		MyStopwatch stopwatch;
		if (id != null) {
			stopwatch = getProbeStopwatches(probe).remove(id);
		} else {
			stopwatch = inProgressNullIds.remove(probe);
		}
		
		if (stopwatch == null) {
			return;
		}
		long diff = stopwatch.stop();
		probe.submit(stopwatch.startTime, diff, args);
	}
	
	private static Map<Object, MyStopwatch> getProbeStopwatches(Probe probe) {
		synchronized (inProgress) {
			Map<Object, MyStopwatch> stopwatches = inProgress.get(probe);
			if (stopwatches == null) {
				stopwatches = new ConcurrentHashMap<>();
				inProgress.put(probe, stopwatches);
			}
			return stopwatches;
		}
	}
	
	private static class MyStopwatch {
		public long startTime;
		public MyStopwatch() {
		}
		
		public void start() {
			startTime = System.nanoTime();
		}
		
		public long stop() {
			return System.nanoTime() - startTime;
		}
	}
}
