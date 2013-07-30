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

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import cz.cuni.mff.d3s.spl.instrumentation.InstrumentationSnippet;

/** Keeps track of instrumentation snippets currently in use and controls
 * the instrumentation.
 */
class InstrumentationController {
	private static Instrumentation instrumentationEngine;
	private static Set<ClassLoader> knownClassLoaders;
	private static Set<InstrumentationSnippet> snippets;
	private static boolean initialized = false;
	
	public static synchronized void initialize(Instrumentation instrumentation) {
		if (initialized) {
			return;
		}
		
		instrumentationEngine = instrumentation;
		
		Map<ClassLoader, Boolean> setBackend = new WeakHashMap<>();
		knownClassLoaders = Collections.newSetFromMap(setBackend);
		
		snippets = new HashSet<>();
		
		instrumentationEngine.addTransformer(new Transformer(), true);
		
		initialized = true;
	}
	
	private static class Transformer implements ClassFileTransformer {
		@Override
		public byte[] transform(ClassLoader loader, String className,
				Class<?> theClass, ProtectionDomain protection,
				byte[] bytecode)
				throws IllegalClassFormatException {
			
			registerClassLoader(loader);
			
			if (className.startsWith("java/")) {
				return null;
			}
			
			byte[] result = bytecode;
			synchronized (snippets) {
				for (InstrumentationSnippet snippet : snippets) {
					byte[] tmp = snippet.instrument(loader, theClass, className, result);
					if (tmp != null) {
						result = tmp;
					}
				}
			}
			
			return result;
		}
		
	}
	
	public static void registerClassLoader(ClassLoader loader) {
		synchronized (knownClassLoaders) {
			registerClassLoaderInternal(loader, true);
		}
	}
	
	public static void addSnippet(InstrumentationSnippet instrumentation) {
		if (instrumentation == null) {
			throw new IllegalArgumentException("Instrumentation cannot be null.");
		}
		synchronized (snippets) {
			snippets.add(instrumentation);
		}
	}
	
	public static void removeSnippet(InstrumentationSnippet instrumentation) {
		if (instrumentation == null) {
			return;
		}
		synchronized (snippets) {
			snippets.remove(instrumentation);
		}
	}
		
	public static void reinstrument(String klass) {
		/*
		 * Make a copy to prevent concurrent modification exception.
		 * 
		 * The cause is that class loading may trigger new class loader
		 * registration. As here we would be inside for-each loop, the
		 * collection would be modified. Creating a copy is the easiest
		 * way for dealing with this.
		 */
		Set<ClassLoader> classLoadersCopy = new HashSet<>();
		synchronized (knownClassLoaders) {
			classLoadersCopy.addAll(knownClassLoaders);
		}
		
		for (ClassLoader cl : classLoadersCopy) {
			reloadClass(klass, cl);
		}
	}
	
	private static void registerClassLoaderInternal(ClassLoader loader, boolean registerParent) {
		if (loader == null) {
			return;
		}

		boolean newLoader;
		synchronized (knownClassLoaders) {
			newLoader = knownClassLoaders.add(loader);
		}

		if (newLoader) {
			if (registerParent) {
				registerClassLoaderInternal(loader.getParent(), false);
			}
		}
	}


	private static boolean reloadClass(String className, ClassLoader loader) {
		Class<?> klass;
		try {
			klass = loader.loadClass(className);
		} catch (ClassNotFoundException e) {
			/*
			 * Not a problem, we expect that this could happen.
			 */
			return false;
		}

		assert (klass != null);

		
		try {
			instrumentationEngine.retransformClasses(klass);
		} catch (Exception e) {
			reportException(e, "retransformation of %s failed.", klass.getName());
		}
		
		return true;
	}

	private static void reportException(Throwable e, String msg, Object... args) {
		System.err.printf("Instrumentation: " + msg + "\n", args);
		e.printStackTrace();
	}
}
