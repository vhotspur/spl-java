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
package cz.cuni.mff.d3s.spl.manualtests;

import cz.cuni.mff.d3s.spl.annotations.AtExit;
import cz.cuni.mff.d3s.spl.annotations.AtStart;

public class ExecAnnotationWorks {
	
	public static void main(String args[]) {
		System.out.println("main()");
	}
	
	@AtStart
	public static void atStart() {
		System.out.println("@AtStart");
	}
	
	@AtExit
	public static void atExit() {
		System.out.println("@AtExit");
	}
}
