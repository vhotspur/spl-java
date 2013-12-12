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
package cz.cuni.mff.d3s.spl.demo.sandbox.priorities.adjust;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.TreeMap;

public class EarliestDeadlineFirst extends Adjust {
	private long sleepTimeMillis;
	
	public EarliestDeadlineFirst(long sleepTimeMillis) {
		this.sleepTimeMillis = sleepTimeMillis;
	}

	@Override
	public void run() {
		int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
		
		while (!terminate) {
			long timeNow = System.nanoTime();
			
			TreeMap<Long, Thread> deadlines = new TreeMap<>();
			synchronized (startTimes) {
				for (Map.Entry<Thread, Long> e : startTimes.entrySet()) {
					long value = e.getValue();
					if (value == 0) {
						value = timeNow;
					}
					long duration = value;
					while (deadlines.containsKey(duration)) {
						duration++;
					}
					deadlines.put(duration, e.getKey());
				}
			}
			
			int size = deadlines.size();
			if (size > 0) {
				int index = 0;
				for (Thread t : deadlines.values()) {
					if (index < cpuCount) {
						t.setPriority(Thread.MAX_PRIORITY);
					} else if (index < cpuCount * 2) {
						t.setPriority(Thread.NORM_PRIORITY);
					} else {
						t.setPriority(Thread.MIN_PRIORITY);
					}
					index++;
				}
			}
			
			sleep(sleepTimeMillis);
		}
	}
}
