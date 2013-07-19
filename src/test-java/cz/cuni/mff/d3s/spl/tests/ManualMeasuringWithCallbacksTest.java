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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.spl.agent.Facade;
import cz.cuni.mff.d3s.spl.agent.SPL;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.MeasurementSite;
import cz.cuni.mff.d3s.spl.measure.CallbackMethodMeasuring;
import static cz.cuni.mff.d3s.spl.tests.TestUtils.assertSampleCount;

public class ManualMeasuringWithCallbacksTest implements Runnable {
	private static final String SITE_ID = "user:test:SimpleMeasuring";
	private static final int LOOPS = 153;
	private static final int THREADS = 5;
	private CallbackMethodMeasuring siteMeter;
	private MeasurementSite site;
	private int idShift;
	
	public ManualMeasuringWithCallbacksTest() {
	}
	
	private ManualMeasuringWithCallbacksTest(CallbackMethodMeasuring meter) {
		siteMeter = meter;
	}
	
	@Before
	public void setUp() {
		SPL.__clear();
		site = Facade.createAndRegisterSite(SITE_ID);
		siteMeter = new CallbackMethodMeasuring(site);
	}
	
	@Test
	public void singleThreadedMeasuring() {
		this.run();
		
		assertSampleCount(LOOPS, getData());
	}
	
	@Test
	public void multiThreadedMeasuring() throws InterruptedException {
		Thread[] threads = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++) {
			ManualMeasuringWithCallbacksTest self = new ManualMeasuringWithCallbacksTest(siteMeter);
			self.idShift = i * LOOPS;
			threads[i] = new Thread(self);
		}
		
		for (int i = 0; i < THREADS; i++) {
			threads[i].start();
		}
		
		for (int i = 0; i < THREADS; i++) {
			threads[i].join();
		}
		
		assertSampleCount(THREADS * LOOPS, getData());
	}
	
	private Data getData() {
		return SPL.getDataSource(SITE_ID);
	}
	
	@Override
	public void run() {
		assertNotNull(siteMeter);
		for (int i = 0; i < LOOPS; i++) {
			hotspotStart(i + idShift);
		}
		for (int i = LOOPS - 1; i >= 0; i--) {
			hotspotEnd(i + idShift);
		}
	}
	
	private void hotspotStart(int id) {
		siteMeter.start(new Integer(id));
		
		/* Here would come the *something* we want to measure
		 * (start phase).
		 */
	}
	
	private void hotspotEnd(int id) {
		/* Here would come the *something* we want to measure
		 * (end phase).
		 */
		
		siteMeter.done(new Integer(id));
	}
}
