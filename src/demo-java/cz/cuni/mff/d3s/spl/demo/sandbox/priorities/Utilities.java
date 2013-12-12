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

import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

class Utilities {
	public static Map<String, String> parseQuery(URI uri) {
		Map<String, String> result = new HashMap<>();
		String raw = uri.getRawQuery();
		if (raw == null) {
			return result;
		}
		
		String pairs[] = raw.split("[&]");
		
		for (String keyval : pairs) {
			String tmp[] = keyval.split("[=]", 2);
			
			String key = URLDecoder.decode(tmp[0]);
			String value = null;
			
			if (tmp.length > 1) {
				value = URLDecoder.decode(tmp[1]);
			}
			
			result.put(key,  value);
		}
		
		return result;
	}
}
