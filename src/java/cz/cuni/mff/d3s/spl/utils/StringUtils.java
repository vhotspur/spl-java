package cz.cuni.mff.d3s.spl.utils;

import java.util.List;

public class StringUtils {
	
	public static String join(List<?> list) {
		if (list.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		boolean afterFirst = false;
		for (Object o : list) {
			if (afterFirst) {
				result.append(',');
			}
			result.append(o.toString());
			afterFirst = true;
		}
		return result.toString();
	}
	
	public static String formatTimeUnits(double nanos) {
		if (nanos < 1000) {
			return String.format("%.0fns", nanos);
		}
		double micros = nanos / 1000;
		if (micros < 1000) {
			return String.format("%.0fus", micros);
		}
		double millis = micros / 1000;
		if (millis < 1000) {
			return String.format("%.0fms", millis);
		}
		double sec = millis / 1000;
		millis = millis - sec * 1000;
		return String.format("%.0fs %.fms", sec, millis);
	}
}
