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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.Formula;
import cz.cuni.mff.d3s.spl.core.MathematicalInterpretation;
import cz.cuni.mff.d3s.spl.core.Result;
import cz.cuni.mff.d3s.spl.core.impl.DataForTest;
import cz.cuni.mff.d3s.spl.core.impl.InterpretationForTests;
import cz.cuni.mff.d3s.spl.core.impl.formula.Comparison;
import cz.cuni.mff.d3s.spl.core.impl.formula.Comparison.Operator;

public class FormulaParserTest {
	protected static final int SAMPLE_COUNT_GOOD = InterpretationForTests.MINIMUM_SAMPLES_REQUIRED * 2;
	protected static final int SAMPLE_COUNT_BAD = InterpretationForTests.MINIMUM_SAMPLES_REQUIRED / 2;

	/* We assume that formula uses variables d1, d2, d3 ... */
	protected void bindAndAssert(Result expectedResult, Formula formula, Data... datas) {
		assertNotNull(formula);
		
		/* First, set the proper interpretation. */
		formula.setInterpreation(interpretation);
		
		/* Bind the provided data sources. */
		for (int i = 0; i < datas.length; i++) {
			String varname = String.format("d%d", i + 1);
			formula.bind(varname, datas[i]);
		}
		
		/* And evaluate. */
		assertEquals(expectedResult, formula.evaluate());
	}
	
	protected MathematicalInterpretation interpretation;
	protected Data low;
	protected Data medium;
	protected Data high;
	protected Data empty;
	
	@Before
	public void setupInterpretation() {
		interpretation = new InterpretationForTests();
	}
	
	@Before
	public void setupData() {
		low = new DataForTest(10, SAMPLE_COUNT_GOOD);
		medium = new DataForTest(20, SAMPLE_COUNT_GOOD);
		high = new DataForTest(30, SAMPLE_COUNT_GOOD);
		empty = new DataForTest(0, SAMPLE_COUNT_BAD);
	}
	
	@Test
	public void simpleFormula() {
		Formula formula = SplFormula.create("d1 < d2");
		bindAndAssert(Result.TRUE, formula, low, high);
	}

}
