package cz.cuni.mff.d3s.spl.core.impl.formula;

import cz.cuni.mff.d3s.spl.core.Formula;
import cz.cuni.mff.d3s.spl.core.Result;

public class LogicAnd extends LogicOp {

	public LogicAnd(Formula left, Formula right) {
		super(left, right);
	}

	/*
	 * We are using Kleene three-value logic.
	 */
	@Override
	public Result evaluate() {
		Result leftResult = left.evaluate();
		
		/*
		 * If the left one is FALSE, we do not need
		 * to evaluate the right one.
		 */
		if (leftResult == Result.FALSE) {
			return Result.FALSE;
		}
		
		Result rightResult = right.evaluate();
		
		/*
		 * If left is TRUE, the result solely depends on
		 * the evaluation of the right one.
		 * For CANNOT_COMPUTE, it is a bit more complicated.
		 */
		if (leftResult == Result.TRUE) {
			return rightResult;
		} else {
			assert leftResult == Result.CANNOT_COMPUTE;
			if (rightResult == Result.FALSE) {
				return Result.FALSE;
			} else {
				return Result.CANNOT_COMPUTE;
			}
		}
	}

}
