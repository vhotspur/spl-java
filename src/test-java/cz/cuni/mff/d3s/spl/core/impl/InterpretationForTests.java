package cz.cuni.mff.d3s.spl.core.impl;

import org.junit.Ignore;

import cz.cuni.mff.d3s.spl.core.MathematicalInterpretation;
import cz.cuni.mff.d3s.spl.core.Result;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;

@Ignore
public class InterpretationForTests implements MathematicalInterpretation {

	@Override
	public Result isGreaterThan(StatisticSnapshot left, StatisticSnapshot right) {
		return isGreaterThan(left.getArithmeticMean(), right.getArithmeticMean());
	}

	@Override
	public Result isSmallerThan(StatisticSnapshot variable, double constant) {
		return isGreaterThan(constant, variable.getArithmeticMean());
	}
	
	private Result isGreaterThan(double left, double right) {
		if (left - right > 0.0) {
			return Result.TRUE;
		} else {
			return Result.FALSE;
		}
	}

}
