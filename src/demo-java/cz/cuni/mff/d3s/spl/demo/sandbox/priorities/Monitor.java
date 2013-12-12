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
package cz.cuni.mff.d3s.spl.demo.sandbox.priorities;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import cz.cuni.mff.d3s.spl.annotations.AtExit;
import cz.cuni.mff.d3s.spl.annotations.AtStart;
import cz.cuni.mff.d3s.spl.demo.sandbox.priorities.adjust.Adjust;
import cz.cuni.mff.d3s.spl.demo.sandbox.priorities.adjust.BoostAfterDeadline;
import cz.cuni.mff.d3s.spl.demo.sandbox.priorities.adjust.EarliestDeadlineFirst;

public class Monitor {
	private static final int MINIMAL_ADJUST_PERIOD_MS = 10;
	private static Thread monitoringThread;
	private static Adjust adjustment;
	private static boolean changePriorities;
	
	private static Map<Thread, Long> startTimes = Collections.synchronizedMap(new WeakHashMap<Thread, Long>());
	
	
	@AtStart
	public static void prepare() {
		String propAdjustPeriod = System.getProperty("adjust.period", "0");
		int adjustPeriod = 0;
		try {
			adjustPeriod = Integer.parseInt(propAdjustPeriod);
		} catch (NumberFormatException e) {
		}
		if (adjustPeriod < MINIMAL_ADJUST_PERIOD_MS) {
			adjustPeriod = MINIMAL_ADJUST_PERIOD_MS;
		}
		
		adjustment = null;
		String propPriority = System.getProperty("adjust.strategy", "none");
		if (propPriority.equals("edf")) {
			adjustment = new EarliestDeadlineFirst(adjustPeriod);
		} else if (propPriority.equals("boost")) {
			adjustment = new BoostAfterDeadline(adjustPeriod);
		}
		
		if (adjustment != null) {
			adjustment.init(startTimes);
			monitoringThread = new Thread(adjustment);
			monitoringThread.setDaemon(true);
			monitoringThread.start();
		}
	}
	
	@AtExit
	public static void finish() {
		if (adjustment != null) {
			adjustment.shutdown();
			monitoringThread.interrupt();
		}
	}
	
	public static void announceHandleStart() {
		startTimes.put(Thread.currentThread(), System.nanoTime());
	}
	
	public static void announceHandleStop() {
		Thread t = Thread.currentThread();
		startTimes.put(t, Long.valueOf(0));
		if (changePriorities) {
			t.setPriority(Thread.MIN_PRIORITY);
		}
	}
}
