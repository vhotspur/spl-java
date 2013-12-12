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

import java.util.ArrayList;
import java.util.List;

import de.erichseifert.gral.data.DataTable;

public class DataToVisualize {
	private static List<Pair<Long, Long>> data;
	
	static {
		data = new ArrayList<>();
		long counter = 0;
		for (long sample : RawData.samples) {
			Pair<Long, Long> pair = new Pair<>(counter, sample);
			
			data.add(pair);
			
			counter++;
		}
	}
	
	public static DataTable getData() {
		return getDataTableFromPairList(data, Long.MAX_VALUE);
	}
	
	public static DataTable getData(long size) {
		return getDataTableFromPairList(data, size);
	}
	
	private static DataTable getDataTableFromPairList(List<Pair<Long, Long>> input, long size) {
		@SuppressWarnings("unchecked") // varargs and Long.class don't mix well
		DataTable result = new DataTable(Long.class, Long.class);
		
		for (Pair<Long, Long> p : input) {
			result.add(p.first, p.second);
			size--;
			if (size < 0) {
				break;
			}
		}
		return result;
	}
}
