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
package cz.cuni.mff.d3s.spl.formula;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.Formula;
import cz.cuni.mff.d3s.spl.formula.SimpleFormulas;
import cz.cuni.mff.d3s.spl.formula.SplFormula;
import cz.cuni.mff.d3s.spl.formula.SplFormula.SplParseException;
import cz.cuni.mff.d3s.spl.tests.DataForTest;

public class FormulaErrorHandlingTest {
	
	private Data source;
	
	@Before
	public void setupSource() {
		source = new DataForTest(5., 50);
	}
	
	@Test(expected=SplParseException.class)
	public void emptyFormulaThrows() throws SplParseException {
		@SuppressWarnings("unused")
		Formula formula = SplFormula.create("");
	}
	
	@Test(expected=SplParseException.class)
	public void invalidIdentifierThrows() throws SplParseException {
		@SuppressWarnings("unused")
		Formula formula = SplFormula.create("96xy < abc");
	}
	
	@Test(expected=SplParseException.class)
	public void lexerErrorDetected() throws SplParseException {
		@SuppressWarnings("unused")
		Formula formula = SplFormula.create("xy < abc &&& gh > xy");
	}
	
	@Test(expected=SplParseException.class)
	public void grammarErrorDetected() throws SplParseException {
		@SuppressWarnings("unused")
		Formula formula = SplFormula.create("xy < abc && gh > xy || baf");
	}
	
	@Test(expected=java.util.NoSuchElementException.class)
	public void bindingNonexistentSourceInSimpleFormulaThrows() {
		Formula formula = SimpleFormulas.createAsmallerThanB("abc", "def");
		formula.bind("xyz", source);
	}
	
	@Test(expected=java.util.NoSuchElementException.class)
	public void bindingNonexistentSourceInParsedFormulaThrows() throws SplParseException {
		Formula formula = SplFormula.create("abc < def");
		formula.bind("xyz", source);
	}
}
