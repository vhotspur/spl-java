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
package cz.cuni.mff.d3s.spl.instrumentation;

import java.util.LinkedList;
import java.util.List;

public class ExtraArgumentsBuilder {
	private List<ExtraArgument> argsList = new LinkedList<>();
	private ExtraArguments args = null;

	public void addField(String name) {
		add(ExtraArgument.createField(name));
	}
	
	public void addParameter(int position) {
		add(ExtraArgument.createParameter(position));
	}
	
	public void addThis() {
		add(ExtraArgument.createThis());
	}

	public ExtraArguments get() {
		if (args == null) {
			args = new ExtraArguments(argsList);
			/* Not needed anymore. */
			argsList = null;
		}
		return args;
	}

	private void add(ExtraArgument argument) {
		if (args != null) {
			throw new IllegalStateException("ExtraArgumentsBuilder cannot be modified once get() was called.");
		}
		argsList.add(argument);
	}
	
	public static ExtraArgumentsBuilder createFromCommonArguments(CommonExtraArgument... parameters) {
		ExtraArgumentsBuilder builder = new ExtraArgumentsBuilder();
		for (CommonExtraArgument arg : parameters) {
			switch (arg) {
			case THIS:
				builder.addThis();
				break;
			case METHOD_PARAM_1:
				builder.addParameter(1);
				break;
			case METHOD_PARAM_2:
				builder.addParameter(1);
				break;
			case METHOD_PARAM_3:
				builder.addParameter(1);
				break;
			case METHOD_PARAM_4:
				builder.addParameter(1);
				break;
			}
		}
		return builder;
	}
}
