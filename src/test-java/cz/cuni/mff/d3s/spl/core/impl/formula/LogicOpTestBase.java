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
package cz.cuni.mff.d3s.spl.core.impl.formula;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.Formula;
import cz.cuni.mff.d3s.spl.core.MathematicalInterpretation;
import cz.cuni.mff.d3s.spl.core.Result;
import cz.cuni.mff.d3s.spl.core.impl.DataForTest;
import cz.cuni.mff.d3s.spl.core.impl.InterpretationForTests;
import cz.cuni.mff.d3s.spl.core.impl.formula.SplFormula.SplParseException;

@Ignore
public class LogicOpTestBase {
	protected MathematicalInterpretation interpretation;
	protected final Formula leftSubformula;
	protected final Formula rightSubformula;
	protected final Result expectedResult;
	protected Formula constructedFormula;

	public LogicOpTestBase(final Formula left, final Formula right, final Result result) {
		expectedResult = result;
		leftSubformula = left;
		rightSubformula = right;
	}
	
	@Before
	public void setupInterpretation() {
		interpretation = new InterpretationForTests();
	}
	
	@Test
	public void evaluationTest() {
		assertEquals(expectedResult, constructedFormula.evaluate());
	}
}
