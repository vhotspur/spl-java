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
package cz.cuni.mff.d3s.spl.core;

/** Consumer for measured samples - execution durations.
 * 
 * Typical role of this class is to forward the samples to appropriate
 * Data source.
 * However, custom consumers can split the incoming samples into more data
 * sources, for example based on instance or by size of (some) argument.
 */
public interface MeasurementConsumer {
	/** Callback when new sample is collected.
	 * 
	 * The extra arguments can be used to, for example, use different targets
	 * based on instance the method was executed on.
	 * 
	 * @param when Wall-clock time when the sample was collected.
	 * @param duration Execution duration in nanoseconds.
	 * @param args Application-specific arguments.
	 */
	void submit(long when, long duration, Object... args);
}
