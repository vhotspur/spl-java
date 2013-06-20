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
package cz.cuni.mff.d3s.spl.agent;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.MeasurementConsumer;
import cz.cuni.mff.d3s.spl.core.MeasurementSite;
import cz.cuni.mff.d3s.spl.core.impl.ConstLikeImplementations;
import cz.cuni.mff.d3s.spl.core.impl.ForwardingMeasurementConsumer;
import cz.cuni.mff.d3s.spl.core.impl.PlainBufferDataSource;
import cz.cuni.mff.d3s.spl.instrumentation.InstrumentationSnippet;
import cz.cuni.mff.d3s.spl.instrumentation.SnippetConfigurator;

public final class Facade {
	public static MeasurementSite createAndRegisterSite(String id) {
		Data data = new PlainBufferDataSource();
		MeasurementConsumer consumer = new ForwardingMeasurementConsumer(data);
		MeasurementSite site = new MeasurementSite(ConstLikeImplementations.ALWAYS_MEASURE_FILTER, consumer);
		SPL.registerSite(id, site);
		try {
			SPL.registerDataSource(id, data);
		} catch (RuntimeException e) {
			SPL.unregisterSite(id);
			throw e;
		}
		return site;
	}
	
	public static Data instrument(String what) {
		String[] parts = what.split("#");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Illegal method specification.");
		}
		String klass = parts[0];
		String method = parts[1];
		String id = String.format("instrument:%s#%s", klass, method);
		
		Data data = new PlainBufferDataSource();
		MeasurementConsumer consumer = new ForwardingMeasurementConsumer(data);
		MeasurementSite site = new MeasurementSite(ConstLikeImplementations.ALWAYS_MEASURE_FILTER, consumer);
		SPL.registerSite(id, site);
		try {
			SPL.registerDataSource(id, data);
		} catch (RuntimeException e) {
			SPL.unregisterSite(id);
			throw e;
		}
		
		SnippetConfigurator instrumentationConfig = new SnippetConfigurator(klass, method);
		InstrumentationSnippet instrumentation = instrumentationConfig.getSnippetForSingleMethodMeasuring();
		SPL.registerInstrumentation(instrumentation);
		
		SPL.reloadClass(klass);
		
		return data;
	}
}
