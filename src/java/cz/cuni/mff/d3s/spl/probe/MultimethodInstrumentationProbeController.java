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
import cz.cuni.mff.d3s.spl.core.Probe;
import cz.cuni.mff.d3s.spl.core.ProbeController;
import cz.cuni.mff.d3s.spl.instrumentation.ClassLoaderFilter;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgument;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArguments;
import cz.cuni.mff.d3s.spl.instrumentation.javassist.EndMethodTransformer;
import cz.cuni.mff.d3s.spl.instrumentation.javassist.JavassistSnippet;
import cz.cuni.mff.d3s.spl.instrumentation.javassist.StartMethodTransformer;

public class MultimethodInstrumentationProbeController implements ProbeController {
	private InstrumentationProbe probe;
	private String probeId;
	private JavassistSnippet startSnippet;
	private StartMethodTransformer startTransformer;
	private JavassistSnippet endSnippet;
	private EndMethodTransformer endTransformer;
	
	public MultimethodInstrumentationProbeController(
			ClassLoaderFilter startMethodLoaderFilter,
			String startMethodName,
			ExtraArgument startMethodMatcher,
			InvocationFilter filter, ExtraArguments filterArgs,
			
			ClassLoaderFilter endMethodLoaderFilter,
			String endMethodName,
			ExtraArgument endMethodMatcher,
			
			MeasurementConsumer consumer, ExtraArguments consumerArgs) {
		probe = new InstrumentationProbe(filter, consumer);
		probeId = "probe:" + startMethodName + "->" + endMethodName;
		
		startTransformer = new StartMethodTransformer(startMethodName, probeId, startMethodMatcher, filterArgs);
		startSnippet = new JavassistSnippet(startMethodLoaderFilter, startTransformer);
		
		endTransformer = new EndMethodTransformer(endMethodName, probeId, endMethodMatcher, consumerArgs);
		endSnippet = new JavassistSnippet(endMethodLoaderFilter, endTransformer);
	}
	
	@Override
	public void activate() {
		SPL.registerInstrumentation(startSnippet);
		SPL.registerInstrumentation(endSnippet);
		SPL.reloadClass(endTransformer.getTargetClass());
		SPL.reloadClass(startTransformer.getTargetClass());
		SPL.registerProbe(probeId, probe);
	}

	@Override
	public void deactivate() {
		SPL.unregisterInstrumentation(startSnippet);
		SPL.unregisterInstrumentation(endSnippet);
		SPL.reloadClass(startTransformer.getTargetClass());
		SPL.reloadClass(endTransformer.getTargetClass());
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
