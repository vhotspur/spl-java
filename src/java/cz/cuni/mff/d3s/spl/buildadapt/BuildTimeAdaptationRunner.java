/*
 * Copyright 2015 Charles University in Prague
 * Copyright 2015 Vojtech Horky
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
package cz.cuni.mff.d3s.spl.buildadapt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Runner to find the best alternative.
 *
 */
public class BuildTimeAdaptationRunner {
	public static boolean VERBOSE = false;
	
	/** Find the best alternative.
	 * 
	 * @param testDurationSec How long shall the measurement run (in seconds).
	 * @param maxSamplesToCollect Maximum number of samples to keep.
	 * @param alternatives Alternatives to compare.
	 * @return Name of the best alternative.
	 */
	public static String findBest(long testDurationSec,
			int maxSamplesToCollect, Map<String, Worker> alternatives) {
		
		List<String> keys = new ArrayList<String>(alternatives.keySet());
		
		Map<String, long[]> samples = new HashMap<String, long[]>();
		for (String k : alternatives.keySet()) {
			samples.put(k, new long[maxSamplesToCollect]);
		}
		
		int sampleCount = 0;
		int sampleIndex = 0;
		
		long endTime = System.currentTimeMillis()/1000 + testDurationSec;
		while (System.currentTimeMillis()/1000 < endTime) {
			Collections.shuffle(keys);
			
			for (String key : keys) {
				Worker worker = alternatives.get(key);
				worker.setup();
				long start = System.nanoTime();
				worker.run();
				long end = System.nanoTime();
				worker.teardown();
				
				long[] times = samples.get(key);
				times[sampleIndex] = end - start;
			}
			
			sampleIndex = (sampleIndex + 1) % maxSamplesToCollect;
			if (sampleCount < maxSamplesToCollect - 1) {
				sampleCount++;
			}
		}
		
		if (VERBOSE) {
			for (String k : alternatives.keySet()) {
				System.out.printf("samples.%s <- c(", k);
				long[] times = samples.get(k);
				for (int i = 0; i < sampleCount; i++) {
					if (i > 0) {
						System.out.printf(", ");
					}
					System.out.printf("%d", times[i]);
				}
				System.out.println(")");
			}
		}
		
		double best = Double.MAX_VALUE;
		String bestKey = null;
		for (String k : alternatives.keySet()) {
			double stat = getStat(samples.get(k));
			if (stat < best) {
				best = stat;
				bestKey = k;
			}
		}
		
		return bestKey;
	}
	
	protected static double getStat(long[] samples) {
		long sum = 0;
		for (long l : samples) {
			sum += l;
		}
		return (double) sum / samples.length;
	}
}
