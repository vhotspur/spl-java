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
