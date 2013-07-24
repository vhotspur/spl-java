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
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgument;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArguments;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgumentsBuilder;

public class MultimethodInstrumentationProbeControllerBuilder {
	private ClassLoaderFilter startMethodLoaderFilter = null;
	private String startMethodName;
	private ExtraArgument startMethodMatcher;
	
	private ClassLoaderFilter endMethodLoaderFilter = null;
	private String endMethodName;
	private ExtraArgument endMethodMatcher;
	
	private InvocationFilter invocationFilter = null;
	private ExtraArguments invocationFilterArgs = null;
	
	private MeasurementConsumer dataConsumer = null;
	private ExtraArguments dataConsumerArgs = null;
	
	private boolean finalized = false;


	public MultimethodInstrumentationProbeControllerBuilder(String startMethod, String endMethod) {
		startMethodName = startMethod;
		endMethodName = endMethod;
	}
	
	public ProbeController get() {
		if (!finalized) {
			setDefaults();
		}
		
		finalized = true;
		return new MultimethodInstrumentationProbeController(startMethodLoaderFilter, startMethodName, startMethodMatcher, invocationFilter, invocationFilterArgs, endMethodLoaderFilter, endMethodName, endMethodMatcher, dataConsumer, dataConsumerArgs);
	}
	
	public void forwardSamplesToDataSource(Data source) {
		setConsumer(new ForwardingMeasurementConsumer(source));
	}

	public void setConsumer(MeasurementConsumer consumer) {
		checkNotFinalized();
		
		dataConsumer = consumer;
	}

	public void setConsumer(MeasurementConsumer consumer, ExtraArguments extraArguments) {
		checkNotFinalized();
		
		dataConsumer = consumer;
		dataConsumerArgs = extraArguments;
	}

	public void setInvocationFilter(InvocationFilter filter, CommonExtraArgument... parameters) {
		checkNotFinalized();
		
		invocationFilter = filter;
		
		ExtraArgumentsBuilder builder = ExtraArgumentsBuilder.createFromCommonArguments(parameters);
		invocationFilterArgs = builder.get();
	}

	public void setClassLoaderFilter(ClassLoaderFilter filter) {
		checkNotFinalized();
		
		startMethodLoaderFilter = filter;
		endMethodLoaderFilter = filter;
	}
	
	public void setStartMethodMatcher(CommonExtraArgument parameter) {
		checkNotFinalized();
		
		startMethodMatcher = ExtraArgument.createFromCommon(parameter);
	}
	
	public void setEndMethodMatcher(ExtraArgument parameter) {
		checkNotFinalized();
		
		endMethodMatcher = parameter;
	}

	private void setDefaults() {
		if ((startMethodLoaderFilter == null) && (endMethodLoaderFilter == null)) {
			setClassLoaderFilter(ConstLikeImplementations.ANY_CLASS_LOADER);
		}
		if (startMethodMatcher == null) {
			startMethodMatcher = ExtraArgument.NULL;
		}
		if (endMethodMatcher == null) {
			endMethodMatcher = ExtraArgument.NULL;
		}
		if (invocationFilter == null) {
			setInvocationFilter(ConstLikeImplementations.ALWAYS_MEASURE_FILTER);
		}
		if (invocationFilterArgs == null) {
			invocationFilterArgs = ExtraArguments.NO_ARGUMENTS;
		}
		if (dataConsumer == null) {
			Data source = new PlainBufferDataSource();
			SPL.registerDataSource(startMethodName + "->" + endMethodName, source);
			forwardSamplesToDataSource(source);
		}
		if (dataConsumerArgs == null) {
			dataConsumerArgs = ExtraArguments.NO_ARGUMENTS;
		}
	}
	
	private void checkNotFinalized() {
		if (finalized) {
			throw new IllegalStateException("MultimethodInstrumentationProbeControllerBuilder cannot be modified once get() was called.");
		}
	}
}
