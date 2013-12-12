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

import java.util.Map;

public class BoostAfterDeadline extends Adjust {
	private long sleepTimeMillis;
	
	public BoostAfterDeadline(long sleepTimeMillis) {
		this.sleepTimeMillis = sleepTimeMillis;
	}

	@Override
	public void run() {
		while (!terminate) {
			long timeNow = System.nanoTime();
			long limit = timeNow - 100 * 1000 * 1000;
			
			synchronized (startTimes) {
				for (Map.Entry<Thread, Long> e : startTimes.entrySet()) {
					long value = e.getValue();
					if (value == 0) {
						continue;
					}
					Thread t = e.getKey();
					int priority = t.getPriority();
					if (value < limit) {
						priority += 2;
					} else {
						priority -= 4;
					}
					if (priority < Thread.MIN_PRIORITY) {
						priority = Thread.MIN_PRIORITY;
					} else if (priority > Thread.MAX_PRIORITY) {
						priority = Thread.MAX_PRIORITY;
					}
					t.setPriority(priority);
				}
			}
			
			sleep(sleepTimeMillis);
		}
	}
}
