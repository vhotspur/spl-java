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
package cz.cuni.mff.d3s.spl.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import cz.cuni.mff.d3s.spl.agent.Facade;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

public class SelfInstrumentationTest implements Runnable {
	/*
	 * Together, the test sleeps for LOOPS * SLEEP_LENGTH_MILLIS.
	 * Such big length of a single sleep is necessary to ensure that the
	 * specified length is bigger that the resolution of the underlying
	 * timer.
	 */
	private static final int LOOPS = 39;
	private static final int SLEEP_LENGTH_MILLIS = 14;
	private static final double EPSILON = 1;
	private static String hotspotMethodName;
	
	@BeforeClass
	public static void setUpClass() {
		String selfName = SelfInstrumentationTest.class.getCanonicalName();
		hotspotMethodName = String.format("%s#hotspot", selfName); 
	}
	
	@Test
	public void instrumentationWorks() {
		Data data = Facade.instrument(hotspotMethodName);
		run();
		StatisticSnapshot finalStats = data.getStatisticSnapshot();
		
		/*
		 * Intentionally more different asserts - putting them into separate
		 * methods would complicate the @Before phase way too much.
		 */
		assertTrue("Instrumentation should record at least one pass",
			finalStats.getSampleCount() > 0);
		TestUtils.assertSampleCount(LOOPS, data);
		assertEquals((double) SLEEP_LENGTH_MILLIS,
			finalStats.getArithmeticMean() / (1000 * 1000), EPSILON);
	}
	
	@Override
	public void run() {
		for (int i = 0; i < LOOPS; i++) {
			hotspot();
		}
	}
	
	protected void hotspot() {
		TestUtils.tryToSleep(SLEEP_LENGTH_MILLIS);
	}
}
