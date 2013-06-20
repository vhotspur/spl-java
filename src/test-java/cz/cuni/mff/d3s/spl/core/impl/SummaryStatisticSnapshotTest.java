package cz.cuni.mff.d3s.spl.core.impl;

import static org.junit.Assert.*;
import org.junit.Test;

import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

public class SummaryStatisticSnapshotTest {
	private final static double EPSILON = 0.01;

	@Test
	public void emptySourceGivesZeros() {
		StatisticSnapshot stat = new SummaryStatisticSnapshot(new long[0]);
		assertEquals(0, stat.getSampleCount());
		assertEquals(0, stat.getArithmeticMean(), EPSILON);
	}
	
	@Test
	public void fewSamplesTest() {
		// 10 values, sum 101
		long[] samples = {9,10,11,9,12,9,10,10,11,10};
		
		StatisticSnapshot stat = new SummaryStatisticSnapshot(samples);
		assertEquals(10, stat.getSampleCount());
		assertEquals(10.1, stat.getArithmeticMean(), EPSILON);
	}
}
