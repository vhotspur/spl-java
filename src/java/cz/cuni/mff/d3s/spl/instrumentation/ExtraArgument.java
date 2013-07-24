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

public class ExtraArgument {
	public enum Kind {
		NULL,
		THIS,
		FIELD,
		PARAMETER,
	};
	public final Kind kind;
	public final String name;
	public final int index;
	
	private static final ExtraArgument THIS = new ExtraArgument(Kind.THIS, null, -1);
	public static final ExtraArgument NULL = new ExtraArgument(Kind.NULL, null, -1);
	
	private ExtraArgument(Kind kind, String name, int index) {
		this.kind = kind;
		this.name = name;
		this.index = index;
	}
	
	public static ExtraArgument createThis() {
		return THIS;
	}
	
	public static ExtraArgument createField(String name) {
		return new ExtraArgument(Kind.FIELD, name, -1);
	}
	
	public static ExtraArgument createParameter(int position) {
		return new ExtraArgument(Kind.PARAMETER, null, position);
	}
}
