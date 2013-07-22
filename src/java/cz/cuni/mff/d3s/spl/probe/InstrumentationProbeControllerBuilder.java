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
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.InvocationFilter;
import cz.cuni.mff.d3s.spl.core.MeasurementConsumer;
import cz.cuni.mff.d3s.spl.core.impl.ConstLikeImplementations;
import cz.cuni.mff.d3s.spl.core.impl.ForwardingMeasurementConsumer;
import cz.cuni.mff.d3s.spl.core.impl.PlainBufferDataSource;
import cz.cuni.mff.d3s.spl.instrumentation.ClassLoaderFilter;
import cz.cuni.mff.d3s.spl.instrumentation.CommonExtraArgument;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArguments;

public class InstrumentationProbeControllerBuilder {
	private ClassLoaderFilter loaderFilter = null;
	private InvocationFilter invocationFilter = null;
	private MeasurementConsumer dataConsumer = null;
	private String methodName;
	private boolean finalized = false;

	public InstrumentationProbeControllerBuilder(String methodName) {
		this.methodName = methodName;
	}

	public ProbeController get() {
		if (!finalized) {
			setDefaults();
		}
		
		return new SingleMethodInstrumentationProbeController(loaderFilter, invocationFilter, dataConsumer, methodName);
	}

	public void forwardSamplesToDataSource(Data source) {
		setConsumer(new ForwardingMeasurementConsumer(source));
	}

	private void setConsumer(MeasurementConsumer consumer) {
		checkNotFinalized();
		
		dataConsumer = consumer;
	}

	public void setConsumer(MeasurementConsumer consumer, ExtraArguments extraArguments) {
		checkNotFinalized();
		
		// TODO
	}

	public void setInvocationFilter(InvocationFilter filter, CommonExtraArgument... parameters) {
		checkNotFinalized();
		
		invocationFilter = filter;
		
		// TODO
	}

	public void setClassLoaderFilter(ClassLoaderFilter filter) {
		checkNotFinalized();
		
		loaderFilter = filter;
	}
	
	private void setDefaults() {
		if (loaderFilter == null) {
			setClassLoaderFilter(ConstLikeImplementations.ANY_CLASS_LOADER);
		}
		if (invocationFilter == null) {
			setInvocationFilter(ConstLikeImplementations.ALWAYS_MEASURE_FILTER);
		}
		if (dataConsumer == null) {
			Data source = new PlainBufferDataSource();
			SPL.registerDataSource(methodName, source);
			forwardSamplesToDataSource(source);
		}
	}
	
	private void checkNotFinalized() {
		if (finalized) {
			throw new IllegalStateException("ManualProbeControllerBuilder cannot be modified once get() was called.");
		}
	}
}
