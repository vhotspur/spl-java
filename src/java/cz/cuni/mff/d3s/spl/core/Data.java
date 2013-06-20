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
package cz.cuni.mff.d3s.spl.core;

/** Provides access to serie of performance data.
 * 
 * It is expected that the data are continuously changing (new samples
 * coming in), user gets a consistent snapshot of the data by calling
 * getStatisticSnapshot. The returned snapshots reflects data at the
 * time of the call.
 */
public interface Data {
	/** Get consistent view on this data source.
	 * 
	 * @return Statistics summary of this data source.
	 */
	StatisticSnapshot getStatisticSnapshot();
	
	/** Add a new sample to the data source.
	 * 
	 * @param when Wall-clock time when the sample was collected.
	 * @param value Value of the sample (such as execution duration).
	 */
	void addValue(long when, long value);
	
	/** Remove all data.
	 * 
	 * This operation is intended mainly for testing, thus the
	 * double-underscore naming.
	 * Typically, you would not need to use this operation at all.
	 */
	void __clear();
}
