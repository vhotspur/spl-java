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
package cz.cuni.mff.d3s.spl.probe.instrument;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.ProbeController;
import cz.cuni.mff.d3s.spl.stock.PlainBufferDataSource;
import cz.cuni.mff.d3s.spl.tests.TestUtils;

@Ignore
public class InstrumentationTestBase {
	
	protected void activateAndWait(ProbeController ctl) {
		ctl.activate();
		
		TestUtils.tryToSleep(21);
	}
	
	protected Data data;
	protected Data dataOdd;
	protected Data dataEven;
	protected MyClassLoader myLoader;
	protected List<ProbeController> probesToDeactivate;
	
	@Before
	public void setupData() {
		data = new PlainBufferDataSource(500);
		dataOdd = new PlainBufferDataSource(500);
		dataEven = new PlainBufferDataSource(500);
	}
	
	@Before
	public void setupClassLoader() {
		myLoader = new MyClassLoader(new URL[0], this.getClass().getClassLoader());
	}
	
	@Before
	public void setupProbesToDeactivate() {
		probesToDeactivate = new LinkedList<>();
	}
	
	@After
	public void deactivateRegisteredProbes() {
		for (ProbeController ctl : probesToDeactivate) {
			ctl.deactivate();
		}
		
		TestUtils.tryToSleep(15);
	}
}
