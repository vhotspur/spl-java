package cz.cuni.mff.d3s.spl.demo.imagequality;

import java.util.ArrayList;
import java.util.List;

import de.erichseifert.gral.data.DataTable;

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
