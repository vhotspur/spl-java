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
package cz.cuni.mff.d3s.spl.instrumentation.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class MeasuringSnippets {
	public static void addMeasuringCodeForSingleMethodInvocation(
			CtMethod method, String siteId) {
		try {
			String initSite = addLocalVariableAndPrepareInitialization(
					method,
					"_site",
					"cz.cuni.mff.d3s.spl.core.MeasurementSite",
					"%1$s_site = cz.cuni.mff.d3s.spl.agent.SPL.getSite(\"%4$s\");",
					siteId);
			String initMeter = addLocalVariableAndPrepareInitialization(method,
					"_meter",
					"cz.cuni.mff.d3s.spl.measure.SingleMethodMeasuring",
					"%1$s_meter = new %3$s(%1$s_site);");
			String initStopwatch = addLocalVariableAndPrepareInitialization(
					method, "_stopwatch",
					"cz.cuni.mff.d3s.spl.measure.DurationStopwatch",
					"%1$s_stopwatch = %1$s_meter.start(new Object[0]);");

			String codeBefore = String.format("{%s %s %s}", initSite,
					initMeter, initStopwatch);

			String codeAfter = String.format("{ %s_stopwatch.done(new Object[0]); }",
					Utils.INSTRUMENTATION_IDENTIFIERS_PREFIX);

			method.insertBefore(codeBefore);
			method.insertAfter(codeAfter, false);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	private static String addLocalVariableAndPrepareInitialization(
			CtMethod method, String variableName, String variableClass,
			String initializationFormat, Object... args)
			throws CannotCompileException, NotFoundException {

		CtClass klass = method.getDeclaringClass();
		ClassPool pool = klass.getClassPool();

		String fullName = Utils.INSTRUMENTATION_IDENTIFIERS_PREFIX
				+ variableName;
		CtClass resolvedClass = pool.get(variableClass);

		method.addLocalVariable(fullName, resolvedClass);

		Object[] formatArgs = new Object[args.length + 3];
		formatArgs[0] = Utils.INSTRUMENTATION_IDENTIFIERS_PREFIX;
		formatArgs[1] = variableName;
		formatArgs[2] = variableClass;
		System.arraycopy(args, 0, formatArgs, 3, args.length);
		
		return String.format(initializationFormat, formatArgs);
	}
}
