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
