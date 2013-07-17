package cz.cuni.mff.d3s.spl.core.impl.formula;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import cz.cuni.mff.d3s.spl.core.Result;
import cz.cuni.mff.d3s.spl.core.impl.DataForTest;
import cz.cuni.mff.d3s.spl.core.impl.InterpretationForTests;
import cz.cuni.mff.d3s.spl.core.impl.formula.Comparison.Operator;

public class ComparisonTest {

	private Comparison lessThan;
	
	@Before
	public void setUp() {
		lessThan = new Comparison("A", "B", Operator.LT);
		lessThan.setInterpreation(new InterpretationForTests());
	}
	
	@Test
	public void validValuesForLessThan() {
		lessThan.bind("A", new DataForTest(4.0, 100));
		lessThan.bind("B", new DataForTest(5.0, 100));
		assertTrue(lessThan.evaluate() == Result.TRUE);
	}
	
	@Test
	public void invalidValuesForLessThan() {
		lessThan.bind("A", new DataForTest(5.0, 100));
		lessThan.bind("B", new DataForTest(4.0, 100));
		assertTrue(lessThan.evaluate() == Result.FALSE);
	}
}
