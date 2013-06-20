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
package cz.cuni.mff.d3s.spl.core.impl;

import cz.cuni.mff.d3s.spl.core.InvocationFilter;
import cz.cuni.mff.d3s.spl.core.MeasurementConsumer;

public class ConstLikeImplementations {
	public static final InvocationFilter ALWAYS_MEASURE_FILTER = new ConstAnswerInvocationFilter(true);
	public static final InvocationFilter NEVER_MEASURE_FILTER = new ConstAnswerInvocationFilter(false);
	
	public static final MeasurementConsumer NULL_MEASUREMENT_CONSUMER = new NullMeasurementConsumer();
	
	
	private static class ConstAnswerInvocationFilter implements InvocationFilter {
		private boolean answer;
		
		/* Prevent new instances. */
		private ConstAnswerInvocationFilter() {
			this(true);
		}
		
		private ConstAnswerInvocationFilter(boolean whatToReturn) {
			answer = whatToReturn;
		}
		
		@Override
		public boolean measureThisInvocation(Object... args) {
			return answer;
		}
	}
	
	
	private static class NullMeasurementConsumer implements MeasurementConsumer {
		@Override
		public void submit(long when, long duration, Object... args) {
			/* Do nothing. */
		}
		
	}
}
