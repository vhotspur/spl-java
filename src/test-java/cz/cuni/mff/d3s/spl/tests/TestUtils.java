package cz.cuni.mff.d3s.spl.tests;

import org.junit.Ignore;
import static org.junit.Assert.*;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

@Ignore
public class TestUtils {
	public static void tryToSleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			/* Silently ignore. */
		}
	}
	
	public static void assertSampleCount(long expected, Data source) {
		assertNotNull(source);
		
		StatisticSnapshot stats = source.getStatisticSnapshot();
		assertNotNull(stats);
		
		assertEquals(expected, stats.getSampleCount());
	}
}
