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
package cz.cuni.mff.d3s.spl.instrumentation;

public final class SnippetConfigurator {
	private ClassLoaderFilter classloaderFilter = null;
	private String method;
	private String klass;
	
	public SnippetConfigurator(String klass, String method) {
		this.method = method;
		this.klass = klass;
	}
	
	public SnippetConfigurator setClassLoaderFilter(ClassLoaderFilter filter) {
		classloaderFilter = filter;
		return this;
	}
	
	private void setDefaults() {
		if (classloaderFilter == null) {
			classloaderFilter = AnyClassLoaderFilter.INSTANCE;
		}
	}
	
	public InstrumentationSnippet getSnippetForSingleMethodMeasuring() {
		setDefaults();
		
		return new SingleMethodMeasuringInstrumentation(classloaderFilter,
				"instrument:" + klass + "#" + method,
				klass, method);
	}
	
	private static class AnyClassLoaderFilter implements ClassLoaderFilter {
		public static final AnyClassLoaderFilter INSTANCE = new AnyClassLoaderFilter();
		@Override
		public boolean instrumentClassesFromThisClassLoader(ClassLoader loader) {
			return true;
		}
		
	}
}
