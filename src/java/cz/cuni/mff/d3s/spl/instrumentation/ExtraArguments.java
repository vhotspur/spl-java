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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExtraArguments implements Iterable<ExtraArgument> {
	public static final ExtraArguments NO_ARGUMENTS = new ExtraArguments();
	
	private List<ExtraArgument> arguments = new ArrayList<>();
	
	private ExtraArguments() {
		arguments = Collections.emptyList();
	}
	
	ExtraArguments(List<ExtraArgument> args) {
		for (ExtraArgument a : args) {
			arguments.add(a);
		}
	}
	
	public boolean isEmpty() {
		return arguments.isEmpty();
	}
	
	public int size() {
		return arguments.size();
	}
	
	public ExtraArgument get(int index) {
		return arguments.get(index);
	}

	@Override
	public Iterator<ExtraArgument> iterator() {
		return new ArgumentIterator(arguments.iterator());
	}
	
	private class ArgumentIterator implements Iterator<ExtraArgument> {
		private Iterator<ExtraArgument> original;
		
		public ArgumentIterator(Iterator<ExtraArgument> original) {
			this.original = original;
		}
		
		@Override
		public boolean hasNext() {
			return original.hasNext();
		}

		@Override
		public ExtraArgument next() {
			return original.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Arguments cannot be removed.");
			
		}
		
	}

	@Override
	public int hashCode() {
		return arguments.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ExtraArguments)) {
			return false;
		}
		ExtraArguments other = (ExtraArguments) obj;
		return other.arguments.equals(arguments);
	}
}
