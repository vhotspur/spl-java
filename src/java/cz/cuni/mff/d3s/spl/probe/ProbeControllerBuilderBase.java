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

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.InvocationFilter;
import cz.cuni.mff.d3s.spl.core.MeasurementConsumer;
import cz.cuni.mff.d3s.spl.core.ProbeController;
import cz.cuni.mff.d3s.spl.stock.ConstLikeImplementations;
import cz.cuni.mff.d3s.spl.stock.ForwardingMeasurementConsumer;
import cz.cuni.mff.d3s.spl.stock.PlainBufferDataSource;

abstract class ProbeControllerBuilderBase {
	protected MeasurementConsumer dataConsumer = null;
	protected InvocationFilter invocationFilter = null;
	protected boolean finalized = false;
	
	public ProbeControllerBuilderBase() {
		/* No need to do anything. */
	}

	final public ProbeController get() {
		if (!finalized) {
			setDefaults();
		}
		
		finalized = true;
		return createController();
	}
	
	abstract protected ProbeController createController();

	final public void forwardSamplesToDataSource(Data data) {
		setConsumer(new ForwardingMeasurementConsumer(data));
	}
	
	final public void setConsumer(MeasurementConsumer consumer) {
		checkNotFinalized();
		
		dataConsumer = consumer;
	}
	
	final public void setInvocationFilter(InvocationFilter filter) {
		checkNotFinalized();
		
		invocationFilter = filter;
	}

	protected void setDefaults() {
		if (invocationFilter == null) {
			setInvocationFilter(ConstLikeImplementations.ALWAYS_MEASURE_FILTER);
		}
		if (dataConsumer == null) {
			Data source = new PlainBufferDataSource();
			forwardSamplesToDataSource(source);
		}
	}
	
	final protected void checkNotFinalized() {
		if (finalized) {
			throw new IllegalStateException("ProbeController builder cannot be modified once get() was called.");
		}
	}
}
