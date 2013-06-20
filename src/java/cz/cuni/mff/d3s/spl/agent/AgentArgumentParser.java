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
package cz.cuni.mff.d3s.spl.agent;

import java.util.HashMap;
import java.util.Map;

/** Simple parser of arguments passed to Java agent. */
class AgentArgumentParser {
	private static final String OPTION_DELIMITER = ",";
	private static final String KEY_VALUE_DELIMITER = "=";
	private Map<String, String> options = new HashMap<>();
	
	/** Factory method for creating new parser.
	 * 
	 * @param arguments Arguments as passed to agent premain method.
	 * @return Argument parser.
	 */
	public static AgentArgumentParser create(String arguments) {
		AgentArgumentParser parser = new AgentArgumentParser();
		parser.parse(arguments);
		return parser;
	}
	
	/** Parse agent arguments.
	 * 
	 * The newly parsed arguments are added to already existing ones
	 * (i.e. calling this method several times on different input would
	 * merge all arguments, latest overriding the first ones).
	 * 
	 * @param arguments Arguments as passed to agent premain method.
	 */
	protected void parse(String arguments) {
		if ((arguments == null) || arguments.isEmpty()) {
			return;
		}
		
		String[] parts = arguments.split(OPTION_DELIMITER);
		for (String opt : parts) {
			String[] keyValuePair = opt.split(KEY_VALUE_DELIMITER, 2);
			switch (keyValuePair.length) {
			case 1:
				options.put(keyValuePair[0], null);
				break;
			case 2:
				options.put(keyValuePair[0], keyValuePair[1]);
				break;
			default:
				System.err.printf("WARN: ignoring invalid option %s.\n", opt);
			}
		}
	}
	
	public boolean hasOption(String key) {
		return options.containsKey(key);
	}
	
	/** Get string argument.
	 * 
	 * @param key Option (parameter) name.
	 * @param defaultValue Default value if option not present.
	 * @return String argument of given option.
	 */
	public String getValue(String key, String defaultValue) {
		String value = options.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/** Get integer argument.
	 * 
	 * If the option is present but it is not possible to convert the value
	 * to integer, default value is returned and the error is silently ignored.
	 * 
	 * @param ke Option (parameter) name.
	 * @param defaultValue Default value if option not present.
	 * @return Integer argument of given option.
	 */
	public int getValue(String key, int defaultValue) {
		String strValue = getValue(key, Integer.toString(defaultValue));
		try {
			return Integer.parseInt(strValue);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
