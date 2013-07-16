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
package cz.cuni.mff.d3s.spl.sources;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

/** Data source representing current system load.
 * 
 */
public class SystemLoad implements Data {

	public static final SystemLoad INSTANCE = new SystemLoad();
	
	private OperatingSystemMXBean osMxBean;
	
	private SystemLoad() {
		osMxBean = ManagementFactory.getOperatingSystemMXBean();
	}
	
	@Override
	public StatisticSnapshot getStatisticSnapshot() {
		double load = osMxBean.getSystemLoadAverage();
		if (load < 0) {
			load = 0.1; // FIXME: what to do with this
		}
		return new LoadStatistics(load);
	}

	@Override
	public void addValue(long when, long value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void __clear() {
		/* Do nothing. */
	}
	
	private class LoadStatistics implements StatisticSnapshot {
		private double mean;
		
		public LoadStatistics(double load) {
			mean = load;
		}

		@Override
		public double getArithmeticMean() {
			return mean;
		}

		@Override
		public long getSampleCount() {
			return 1000; // FIXME
		}

		@Override
		public long[] getSamples() {
			throw new UnsupportedOperationException("Original samples not available.");
		}
		
	}
}
