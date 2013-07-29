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
package cz.cuni.mff.d3s.spl.tests.probes;

import org.junit.Ignore;

import cz.cuni.mff.d3s.spl.core.InvocationFilter;

@Ignore
public class AcceptOnlyOddSizes implements InvocationFilter {

	@Override
	public boolean measureThisInvocation(Object... args) {
		Integer size = (Integer) args[0];
		return (size % 2) == 1;
	}

}
