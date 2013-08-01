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
import cz.cuni.mff.d3s.spl.core.Probe;
import cz.cuni.mff.d3s.spl.instrumentation.InstrumentationSnippet;

/** Low-level access to SPL run-time framework. */
public final class SPL {
	private static Map<String, Data> datas = new HashMap<>();
	private static Map<String, Probe> probes = new HashMap<>();
	
	public static synchronized void __clear() {
		datas.clear();
		// TODO: reload all classes without any instrumentation
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

	
	public static void registerProbe(String id, Probe probe) {
		if ((id == null) || id.isEmpty()) {
			throw new IllegalArgumentException("Probe id cannot be empty.");
		}
		if (probe == null) {
			throw new IllegalArgumentException("Probe cannot be null.");
		}
		synchronized (probes) {
			if (probes.containsKey(id)) {
				// FIXME: better exception class
				throw new IllegalArgumentException("Probe with given id already exists.");
			}
			probes.put(id, probe);
		}
	}
	
	public static void unregisterProbe(String id) {
		synchronized (probes) {
			probes.remove(id);
		}
	}
	
	public static Probe getProbe(String id) {
		Probe result = null;
		synchronized (probes) {
			result = probes.get(id);
		}
		if (result == null) {
			throw new NoSuchElementException("Data source with given id not found.");
		}
		return result;
	}

	
	public static void registerInstrumentation(InstrumentationSnippet instrumentation) {
		InstrumentationController.addSnippet(instrumentation);
	}
	
	public static void unregisterInstrumentation(InstrumentationSnippet instrumentation) {
		InstrumentationController.removeSnippet(instrumentation);
	}
	
	public static void reloadClass(String name) {
		InstrumentationController.reinstrument(name);
	}
}
