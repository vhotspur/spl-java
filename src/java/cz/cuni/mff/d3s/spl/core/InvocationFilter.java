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
package cz.cuni.mff.d3s.spl.core;

/** Filter which method invocations shall be actually measured.
 * 
 * The filter is invoked at the beginning of measured method (or other code
 * snippet) and tells whether it shall be measured.
 * The reason is that measuring itself could be a costly operation and for
 * short methods it makes sense to measure only each n-th invocation to
 * lower the overhead.
 */
public interface InvocationFilter {
	/** Decides whether to measure this invocation.
	 * 
	 * The extra arguments can be used to, for example, measure only
	 * invocations on a single instance.
	 * 
	 * @param args Application-specific arguments.
	 * @return Whether to measure during this invocation.
	 */
	boolean measureThisInvocation(Object... args);
}
