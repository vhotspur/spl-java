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

import cz.cuni.mff.d3s.spl.core.InvocationFilter;
import cz.cuni.mff.d3s.spl.core.MeasurementConsumer;
import cz.cuni.mff.d3s.spl.core.Probe;
import cz.cuni.mff.d3s.spl.core.ProbeController;

public class ManualProbeController implements ProbeController {
	private ManualProbe probe;
	
	public ManualProbeController(InvocationFilter filter, MeasurementConsumer consumer) {
		probe = new ManualProbe(filter, consumer);
	}
	
	@Override
	public void activate() {
		// TODO Auto-generated method stub
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
	}

	@Override
	public Probe getProbe() {
		return probe;
	}
	
	private class ManualProbe implements Probe {
		private InvocationFilter filter;
		private MeasurementConsumer consumer;
		
		public ManualProbe(InvocationFilter filter,
				MeasurementConsumer consumer) {
			this.filter = filter;
			this.consumer = consumer;
		}

		@Override
		public boolean isActive(Object... args) {
			return filter.measureThisInvocation(args);
		}

		@Override
		public void submit(long when, long duration, Object... args) {
			consumer.submit(when, duration, args);
		}
		
	}
}
