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

/** Probe: a place where measuring happens.
 * <P>
 * Probe is a generic representation of a place in code where
 * something needs to be measured.
 * <P>
 * Probe can be active or inactive.
 * When the probe is active, the data are collected and submitted
 * for processing.
 * Inactive probe shall not do any measuring etc. and shall have a
 * minimal (preferably none) overhead.
 */
public interface Probe {
	/** Tells whether this probe is currently active.
	 *
	 * Typically, the arguments are only passed to
	 * {@link InvocationFilter} and its result is
	 * returned.
	 *
	 * @param args Custom arguments for whatever use.
	 * @return Whether this probe is currently collecting data.
	 */
	public boolean isActive(Object... args);
	
	/** Submit new measured value.
	 *
	 * Typically, the values are only passed to
	 * {@link MeasurementConsumer}.
	 *
	 * @param when Wall-clock time of the event.
	 * @param duration Duration of the event.
	 * @param args Custom arguments for whatever use.
	 */
	public void submit(long when, long duration, Object... args);
}
