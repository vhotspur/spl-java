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
/** Annotations for executing user specific code at start-up/termination.
 * 
 * Below is an example how one can start the instrumentation at program
 * start-up and collect the results at program termination.
 * Notice that this is extremely simplified example as no formula
 * is actually created and we report only the average of the execution times.
 * 
 *
<pre class="syntax java">
public class CollectExecutionTimes {
	private static final String METHOD = "org.pkg.class#Method";
	
	private static Data data;
	
	// Executed before application is started.
	&#x40;AtStart
	public static void prepare() {
		// Create storage for the measured data.
		data = new PlainBufferDataSource(100);
		
		// We will instrument the METHOD method...
		InstrumentationProbeControllerBuilder builder = new InstrumentationProbeControllerBuilder(METHOD);
		// ...and store the execution times into our buffer.
		builder.forwardSamplesToDataSource(data);
		
		// Now, get the probe controller and turn-on the instrumentation.
		builder.get().activate();
	}
	
	// Executed just before the application terminates.
	&#x40;AtExit
	public static void finish() {
		// Retrieve the statistics...
		StatisticSnapshot stats = data.getStatisticSnapshot();
		double mean = stats.getArithmeticMean();
		
		// ... and print them.
		System.out.printf(
			"Mean execution time of last few invocations of %s was %s.\n",
			METHOD, StringUtils.formatTimeUnits(mean));
	}
}
	
</pre>
 * 
 * 
 */
package cz.cuni.mff.d3s.spl.annotations;
