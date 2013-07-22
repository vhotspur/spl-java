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
}
