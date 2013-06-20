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

/** Wrapper class combining invocation filter with a measurement consumer.
 * 
 * The site is a point-cut-like place where execution is measured.
 * 
 */
public final class MeasurementSite {
	private InvocationFilter filter;
	private MeasurementConsumer consumer;
	
	public MeasurementSite(InvocationFilter filter, MeasurementConsumer consumer) {
		this.filter = filter;
		this.consumer = consumer;
	}
	
	/** Tells whether execution shall be measured during this invocation.
	 * 
	 * @param args Application-specific arguments, passed to InvocationFilter.
	 * @return Whether to measure this invocatino.
	 */
	public boolean isActive(Object... args) {
		return filter.measureThisInvocation(args);
	}
	
	/** Called when duration is measured.
	 * 
	 * @param when Wall-clock time when the sample was collected.
	 * @param duration Execution duration in nanoseconds.
	 * @param args Application-specific arguments, passed to
	 * MeasurementConsumer.
	 */
	public void submit(long when, long duration, Object... args) {
		consumer.submit(when, duration, args);
	}
}
