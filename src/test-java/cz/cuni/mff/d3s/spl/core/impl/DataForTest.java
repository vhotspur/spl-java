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

import org.junit.Ignore;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

@Ignore
public class DataForTest implements Data {

	@Ignore
	private class SnapshotForTests implements StatisticSnapshot {
		private double mean;
		private long sampleCount;
		public SnapshotForTests(double mean, long sampleCount) {
			this.mean = mean;
			this.sampleCount = sampleCount;
		}

		@Override
		public double getArithmeticMean() {
			return mean;
		}

		@Override
		public long getSampleCount() {
			return sampleCount;
		}

		@Override
		public long[] getSamples() {
			return null;
		}
	}
	
	private SnapshotForTests stats;
	
	public DataForTest(double mean, long sampleCount) {
		stats = new SnapshotForTests(mean, sampleCount);
	}
	
	@Override
	public StatisticSnapshot getStatisticSnapshot() {
		return stats;
	}

	@Override
	public void addValue(long when, long value) {
		/* Do nothing. */
	}

	@Override
	public void __clear() {
		/* Do nothing. */
	}

}
