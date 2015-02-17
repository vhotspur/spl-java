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
package cz.cuni.mff.d3s.spl.demo.libselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.cuni.mff.d3s.spl.demo.imagequality.Pair;
import cz.cuni.mff.d3s.spl.demo.imagequality.RawData;

/** Source data for visualization.
 */
public class DataToVisualize {
	private static List<Pair<Long, Long>> allData;
	
	static {
		List<Pair<Long, Long>> data = new ArrayList<>();
		long counter = 0;
		for (long sample : RawData.samples) {
			data.add(new Pair<>(counter, sample));
			counter++;
		}
		allData = Collections.unmodifiableList(data);
	}
	
	public static List<Pair<Long, Long>> get() {
		return allData;
	}
}
