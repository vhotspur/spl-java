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

import cz.cuni.mff.d3s.spl.core.InvocationFilter;
import cz.cuni.mff.d3s.spl.core.MeasurementConsumer;
import cz.cuni.mff.d3s.spl.core.impl.ConstLikeImplementations;
import cz.cuni.mff.d3s.spl.instrumentation.ClassLoaderFilter;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgument;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArguments;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgumentsBuilder;

abstract class InstrumentationProbeControllerBuilderBase extends ProbeControllerBuilderBase {
	protected ClassLoaderFilter loaderFilter = null;
	protected ExtraArguments invocationFilterArgs = null;
	protected ExtraArguments dataConsumerArgs = null;
	
	public InstrumentationProbeControllerBuilderBase() {
		super();
	}

	final public void setConsumer(MeasurementConsumer consumer, ExtraArguments extraArguments) {
		checkNotFinalized();
		
		dataConsumer = consumer;
		dataConsumerArgs = extraArguments;
	}

	final public void setInvocationFilter(InvocationFilter filter, ExtraArgument... arguments) {
		checkNotFinalized();
		
		invocationFilter = filter;
		
		ExtraArgumentsBuilder builder = ExtraArgumentsBuilder.create(arguments);
		invocationFilterArgs = builder.get();
	}

	public void setClassLoaderFilter(ClassLoaderFilter filter) {
		checkNotFinalized();
		
		loaderFilter = filter;
	}
	
	@Override
	protected void setDefaults() {
		super.setDefaults();
		
		if (loaderFilter == null) {
			setClassLoaderFilter(ConstLikeImplementations.ANY_CLASS_LOADER);
		}
		if (invocationFilterArgs == null) {
			invocationFilterArgs = ExtraArguments.NO_ARGUMENTS;
		}
		if (dataConsumerArgs == null) {
			dataConsumerArgs = ExtraArguments.NO_ARGUMENTS;
		}
	}
}
