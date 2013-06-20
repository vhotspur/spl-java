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
package cz.cuni.mff.d3s.spl.measure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cz.cuni.mff.d3s.spl.core.MeasurementSite;

public class CallbackMethodMeasuring {
	private Map<Object, DurationStopwatch> inProgress;
	private SingleMethodMeasuring meter;
	
	public CallbackMethodMeasuring(MeasurementSite site) {
		meter = new SingleMethodMeasuring(site);
		inProgress = new ConcurrentHashMap<>();
	}

	public void start(Object id, Object... args) {
		DurationStopwatch stopwatch = meter.start(args);
		inProgress.put(id, stopwatch);
	}
	
	public void done(Object id, Object... args) {
		DurationStopwatch stopwatch = inProgress.get(id);
		if (stopwatch == null) {
			return;
		}
		stopwatch.done(args);
	}
}
