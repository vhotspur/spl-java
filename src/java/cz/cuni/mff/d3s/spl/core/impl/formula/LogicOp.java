package cz.cuni.mff.d3s.spl.core.impl.formula;

import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.Formula;
import cz.cuni.mff.d3s.spl.core.MathematicalInterpretation;
import cz.cuni.mff.d3s.spl.core.Result;

abstract class LogicOp implements Formula {
	protected Formula left;
	protected Formula right;
	
	public LogicOp(Formula left, Formula right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public void setInterpreation(MathematicalInterpretation interpretation) {
		left.setInterpreation(interpretation);
		right.setInterpreation(interpretation);
	}

	@Override
	public void bind(String variable, Data data) {
		left.bind(variable, data);
		right.bind(variable, data);
	}

	@Override
	abstract public Result evaluate();
}
