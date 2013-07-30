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

import cz.cuni.mff.d3s.spl.agent.SPL;
import cz.cuni.mff.d3s.spl.core.InvocationFilter;
import cz.cuni.mff.d3s.spl.core.MeasurementConsumer;
import cz.cuni.mff.d3s.spl.instrumentation.ClassLoaderFilter;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArguments;
import cz.cuni.mff.d3s.spl.instrumentation.javassist.JavassistSnippet;
import cz.cuni.mff.d3s.spl.instrumentation.javassist.SingleMethodTransformer;

public class SingleMethodInstrumentationProbeController implements ProbeController {
	private InstrumentationProbe probe;
	private JavassistSnippet snippet;
	private SingleMethodTransformer transformer;
	private String probeId;
	
	public SingleMethodInstrumentationProbeController(
			ClassLoaderFilter loaderFilter,
			InvocationFilter filter, ExtraArguments filterArgs,
			MeasurementConsumer consumer, ExtraArguments consumerArguments,
			String methodName) {
		probe = new InstrumentationProbe(filter, consumer);
		probeId = "probe:" + methodName;
		transformer = new SingleMethodTransformer(methodName, probeId, filterArgs, consumerArguments);
		snippet = new JavassistSnippet(loaderFilter, transformer);
	}
	
	@Override
	public void activate() {
		SPL.registerInstrumentation(snippet);
		SPL.reloadClass(transformer.getTargetClass());
		SPL.registerProbe(probeId, probe);
	}

	@Override
	public void deactivate() {
		SPL.unregisterInstrumentation(snippet);
		SPL.reloadClass(transformer.getTargetClass());
		SPL.unregisterProbe(probeId);
	}

	@Override
	public Probe getProbe() {
		return probe;
	}
	
	private class InstrumentationProbe implements Probe {
		private InvocationFilter filter;
		private MeasurementConsumer consumer;
		
		public InstrumentationProbe(InvocationFilter filter,
				MeasurementConsumer consumer) {
			this.filter = filter;
			this.consumer = consumer;
		}

		@Override
		public boolean isActive(Object... args) {
			return filter.measureThisInvocation(args);
		}

		@Override
		public void submit(long when, long duration, Object... args) {
			consumer.submit(when, duration, args);
		}
		
	}
}
