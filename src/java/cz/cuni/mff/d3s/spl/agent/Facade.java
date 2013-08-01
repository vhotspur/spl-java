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
import cz.cuni.mff.d3s.spl.core.ProbeController;
import cz.cuni.mff.d3s.spl.probe.InstrumentationProbeControllerBuilder;
import cz.cuni.mff.d3s.spl.stock.PlainBufferDataSource;

/** High-level access to SPL run-time framework. */
public final class Facade {
	public static Data instrument(String what) {
		String[] parts = what.split("#");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Illegal method specification.");
		}
		String klass = parts[0];
		String method = parts[1];
		String id = String.format("instrument:%s#%s", klass, method);
		
		Data data = new PlainBufferDataSource();
		SPL.registerDataSource(id, data);
		
		InstrumentationProbeControllerBuilder bld = new InstrumentationProbeControllerBuilder(what);
		bld.forwardSamplesToDataSource(data);
		
		ProbeController ctl = bld.get();
		ctl.activate();
		
		return data;
	}
}
