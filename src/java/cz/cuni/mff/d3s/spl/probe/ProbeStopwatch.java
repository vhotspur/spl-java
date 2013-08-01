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

import cz.cuni.mff.d3s.spl.core.DurationStopwatch;
import cz.cuni.mff.d3s.spl.core.Probe;

public class ProbeStopwatch implements DurationStopwatch {

	public static DurationStopwatch start(Probe probe, Object... args) {
		if (!probe.isActive(args)) {
			return new ProbeStopwatch();
		}
		ProbeStopwatch stopwatch = new ProbeStopwatch(probe);
		stopwatch.startTime = System.nanoTime();
		return stopwatch;
	}
	
	private boolean active;
	private Probe probe;
	private long startTime;
	
	private ProbeStopwatch() {
		active = false;
	}
	
	private ProbeStopwatch(Probe probe) {
		active = true;
		this.probe = probe;
	}
	
	@Override
	public void done(Object... args) {
		if (!active) {
			return;
		}
		
		long now = System.nanoTime();
		probe.submit(startTime, now - startTime, args);
	}

	
}
