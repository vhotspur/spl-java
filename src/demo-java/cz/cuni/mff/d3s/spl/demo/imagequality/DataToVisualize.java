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
package cz.cuni.mff.d3s.spl.demo.imagequality;

import java.util.ArrayList;
import java.util.List;

import de.erichseifert.gral.data.DataTable;

/** Source data for visualization.
 */
public class DataToVisualize {
	private static List<Pair<Long, Long>> hourly;
	private static List<Pair<Long, Long>> daily;
	private static List<Pair<Long, Long>> weekly;
	
	static {
		hourly = new ArrayList<>();
		daily = new ArrayList<>();
		weekly = new ArrayList<>();
		int counter = 0;
		for (long sample : RawData.samples) {
			/*sample = sample / 1000;
			
			if ((sample/100 < 320) | (sample/100 >= 325)) {
				continue;
			}*/
			
			Pair<Long, Long> pair = new Pair<>((long)counter, sample);
			
			hourly.add(pair);
			if ((counter % 10) == 0) {
				daily.add(pair);
			}
			if ((counter % 20) == 0) {
				weekly.add(pair);
			}
			
			counter++;
		}
	}
	
	public static DataTable getHourly() {
		return getDataTableFromPairList(hourly);
	}
	
	public static DataTable getDaily() {
		return getDataTableFromPairList(daily);
	}
	
	public static DataTable getWeekly() {
		return getDataTableFromPairList(weekly);
	}
	
	private static DataTable getDataTableFromPairList(List<Pair<Long, Long>> input) {
		@SuppressWarnings("unchecked") // varargs and Long.class don't mix well
		DataTable result = new DataTable(Long.class, Long.class);
		
		for (Pair<Long, Long> p : input) {
			result.add(p.first, p.second);
		}
		return result;
	}
}
