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
package cz.cuni.mff.d3s.spl.tests.probes.instrument;

import static cz.cuni.mff.d3s.spl.tests.TestUtils.assertSampleCount;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgument;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgumentsBuilder;
import cz.cuni.mff.d3s.spl.probe.MultimethodInstrumentationProbeControllerBuilder;
import cz.cuni.mff.d3s.spl.probe.ProbeController;
import cz.cuni.mff.d3s.spl.tests.TestUtils;
import cz.cuni.mff.d3s.spl.tests.probes.AcceptOnlyOddSizes;

public class CallbackInstrumentationTest extends InstrumentationTestBase {
	//!!! Must be even number otherwise the assertions won't work!
	private final static int LOOPS = 10;
	
	private MultimethodInstrumentationProbeControllerBuilder probeBuilder;
	
	@Before
	public void setupBuilder() {
		probeBuilder = new MultimethodInstrumentationProbeControllerBuilder("cz.cuni.mff.d3s.spl.tests.probes.instrument.Action#action",
				"cz.cuni.mff.d3s.spl.tests.probes.instrument.Action#action2");
	}
	
	@After
	public void removeInstrumentation() {
		probesToDeactivate.add(probeBuilder.get());
	}

	@Test
	public void deactivationWorks() {
		probeBuilder.forwardSamplesToDataSource(data);
		
		ProbeController probeCtl = probeBuilder.get();
		
		activateAndWait(probeCtl);
		
		for (int i = 0; i < LOOPS; i++) {
			Action a = new Action(456);
			a.action(i);
			a.action2(null, i);
		}
		
		assertSampleCount(LOOPS, data);
		
		probeCtl.deactivate();
		TestUtils.tryToSleep(21);
		
		for (int i = 0; i < LOOPS; i++) {
			Action a = new Action(456);
			a.action(i);
			a.action2(null, i);
		}
		
		assertSampleCount(LOOPS, data);
	}
	
	@Test
	public void allRunsCollected() {
		probeBuilder.forwardSamplesToDataSource(data);
		
		ProbeController probeCtl = probeBuilder.get();
		
		activateAndWait(probeCtl);
		
		for (int i = 0; i < LOOPS; i++) {
			Action a = new Action(456);
			a.action(i);
			a.action2(null, i);
		}
		
		assertSampleCount(LOOPS, data);
	}
	
	@Test
	public void invokeOnlyForOddIterations() {
		probeBuilder.forwardSamplesToDataSource(data);
		
		probeBuilder.setInvocationFilter(new AcceptOnlyOddSizes(), ExtraArgument.createParameter(1));
		
		ProbeController probeCtl = probeBuilder.get();
	
		activateAndWait(probeCtl);
		
		for (int i = 0; i < LOOPS; i++) {
			Action a = new Action(120 + i);
			a.action(i);
			a.action2(null, i);
		}
		
		assertSampleCount(LOOPS / 2, data);
	}
	
	@Test
	public void splittingConsumer() {
		ExtraArgumentsBuilder consumerArgs = new ExtraArgumentsBuilder();
		consumerArgs.addField("list");
		
		probeBuilder.setConsumer(new SplitOddEven(dataOdd, dataEven), consumerArgs.get());
		
		ProbeController probeCtl = probeBuilder.get();
		
		activateAndWait(probeCtl);
		
		for (int i = 0; i < LOOPS + 1; i++) {
			Action a = new Action(120 + i);
			a.action(i);
			a.action2(null, i);
		}
		
		assertSampleCount(LOOPS / 2, dataOdd);
		assertSampleCount(LOOPS - LOOPS / 2 + 1, dataEven);
	}
	
	@Test
	public void requestPairing() {
		probeBuilder.forwardSamplesToDataSource(data);
		probeBuilder.setStartMethodMatcher(ExtraArgument.createParameter(1));
		probeBuilder.setEndMethodMatcher(ExtraArgument.createParameter(2));
		
		ProbeController probeCtl = probeBuilder.get();
		
		activateAndWait(probeCtl);
		
		for (int i = 0; i < LOOPS; i++) {
			Action a = new Action(120 + i);
			a.action(i + LOOPS / 2);
			a.action2(null, i);
		}
		
		assertSampleCount(LOOPS / 2, data);
	}
}
