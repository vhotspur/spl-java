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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.MeasurementSite;
import cz.cuni.mff.d3s.spl.instrumentation.InstrumentationSnippet;

public final class SPL {
	private static Map<String, MeasurementSite> sites = new HashMap<>();
	private static Map<String, Data> datas = new HashMap<>();
	
	public static synchronized void __clear() {
		sites.clear();
		datas.clear();
		// TODO: reload all classes without any instrumentation
	}
	
	public static void registerSite(String id, MeasurementSite site) {
		if ((id == null) || id.isEmpty()) {
			throw new IllegalArgumentException("Site id cannot be empty.");
		}
		if (site == null) {
			throw new IllegalArgumentException("Site cannot be null.");
		}
		synchronized (sites) {
			if (sites.containsKey(id)) {
				// FIXME: better exception class
				throw new IllegalArgumentException("Site already exists.");
			}
			sites.put(id, site);
		}
	}
	
	public static void unregisterSite(String id) {
		synchronized (sites) {
			sites.remove(id);
		}
	}
	
	public static MeasurementSite getSite(String id) {
		synchronized (sites) {
			return sites.get(id);
		}
	}
		
	public static void registerDataSource(String id, Data source) {
		if ((id == null) || id.isEmpty()) {
			throw new IllegalArgumentException("Source id cannot be empty.");
		}
		if (source == null) {
			throw new IllegalArgumentException("Data source cannot be null.");
		}
		synchronized (datas) {
			if (datas.containsKey(id)) {
				// FIXME: better exception class
				throw new IllegalArgumentException("Source already exists.");
			}
			datas.put(id, source);
		}
	}
	
	public static void unregisterDataSource(String id) {
		synchronized (datas) {
			datas.remove(id);
		}
	}
	
	public static Data getDataSource(String id) {
		Data result = null;
		synchronized (datas) {
			result = datas.get(id);
		}
		if (result == null) {
			throw new NoSuchElementException("Data source with given id not found.");
		}
		return result;
	}
	
	public static void registerInstrumentation(InstrumentationSnippet instrumentation) {
		InstrumentationController.addSnippet(instrumentation);
	}
	
	public static void reloadClass(String name) {
		InstrumentationController.reinstrument(name);
	}
}
