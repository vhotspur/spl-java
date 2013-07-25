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

import cz.cuni.mff.d3s.spl.core.impl.ConstLikeImplementations;
import cz.cuni.mff.d3s.spl.instrumentation.ClassLoaderFilter;
import cz.cuni.mff.d3s.spl.instrumentation.CommonExtraArgument;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgument;

public class MultimethodInstrumentationProbeControllerBuilder extends InstrumentationProbeControllerBuilderBase {
	private ClassLoaderFilter startMethodLoaderFilter = null;
	private String startMethodName;
	private ExtraArgument startMethodMatcher;
	
	private ClassLoaderFilter endMethodLoaderFilter = null;
	private String endMethodName;
	private ExtraArgument endMethodMatcher;
	
	public MultimethodInstrumentationProbeControllerBuilder(String startMethod, String endMethod) {
		super();
		startMethodName = startMethod;
		endMethodName = endMethod;
	}
	
	@Override
	protected ProbeController createController() {
		return new MultimethodInstrumentationProbeController(startMethodLoaderFilter, startMethodName, startMethodMatcher, invocationFilter, invocationFilterArgs, endMethodLoaderFilter, endMethodName, endMethodMatcher, dataConsumer, dataConsumerArgs);
	}
	
	@Override
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

	@Override
	protected void setDefaults() {
		super.setDefaults();
		
		if ((startMethodLoaderFilter == null) && (endMethodLoaderFilter == null)) {
			setClassLoaderFilter(ConstLikeImplementations.ANY_CLASS_LOADER);
		}
		if (startMethodMatcher == null) {
			startMethodMatcher = ExtraArgument.NULL;
		}
		if (endMethodMatcher == null) {
			endMethodMatcher = ExtraArgument.NULL;
		}
	}
}
