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
	
	public static ExtraArgument createFromCommon(CommonExtraArgument arg) {
		switch (arg) {
		case METHOD_PARAM_1:
			return createParameter(1);
		case METHOD_PARAM_2:
			return createParameter(2);
		case METHOD_PARAM_3:
			return createParameter(3);
		case METHOD_PARAM_4:
			return createParameter(4);
		case THIS:
			return createThis();
		default:
			assert false : "Unreachable case.";
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ExtraArgument)) {
			return false;
		}
		ExtraArgument other = (ExtraArgument) obj;
		if (index != other.index) {
			return false;
		}
		if (kind != other.kind) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
