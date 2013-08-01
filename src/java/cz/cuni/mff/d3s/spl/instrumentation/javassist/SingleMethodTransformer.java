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
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArguments;

public class SingleMethodTransformer implements Transformer {
	private String targetClass;
	private String targetMethod;
	private String probeId;
	private ExtraArguments filterArgs;
	private ExtraArguments consumerArgs;
	
	public SingleMethodTransformer(String fullMethodName, String probe, ExtraArguments filterArguments, ExtraArguments consumerArguments) {
		String[] parts = fullMethodName.split("#");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid method name.");
		}
		
		probeId = probe;
		targetClass = parts[0];
		targetMethod = parts[1];
		filterArgs = filterArguments;
		consumerArgs = consumerArguments;
	}
	
	@Override
	public String toString() {
		return String.format("SingleMethodTransformer @ %s#%s",
				targetClass.substring(targetClass.lastIndexOf('.') + 1), targetMethod);
	}
	
	@Override
	public int hashCode() {
		return targetClass.hashCode() * 31 + targetMethod.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SingleMethodTransformer)) {
			return false;
		}
		SingleMethodTransformer other = (SingleMethodTransformer) o;
		return other.targetClass.equals(targetClass) && other.targetMethod.equals(targetMethod);
	}
	
	public String getTargetClass() {
		return targetClass;
	}
	
	@Override
	public boolean applyToClass(String classname) {
		return classname.equals(targetClass);
	}

	@Override
	public void apply(CtMethod method) {
		if (!method.getName().equals(targetMethod)) {
			return;
		}
		
		addMeasuringCode(method);
	}
	
	protected void addMeasuringCode(CtMethod method) {
		try {
			String initProbe = addLocalVariableAndPrepareInitialization(
					method,
					"_probe",
					"cz.cuni.mff.d3s.spl.core.Probe",
					"%1$s_probe = cz.cuni.mff.d3s.spl.agent.SPL.getProbe(\"%4$s\");",
					probeId);
			String initStopwatch = addLocalVariableAndPrepareInitialization(
					method, "_stopwatch",
					"cz.cuni.mff.d3s.spl.core.DurationStopwatch",
					"%1$s_stopwatch = cz.cuni.mff.d3s.spl.probe.ProbeStopwatch.start(%1$s_probe, %4$s);",
					Utils.extraArgumentsToCode(filterArgs, method));

			String codeBefore = String.format("{%s %s}", initProbe, initStopwatch);

			
			
			String codeAfter = String.format("{ %s_stopwatch.done(%s); }",
					Utils.INSTRUMENTATION_IDENTIFIERS_PREFIX,
					Utils.extraArgumentsToCode(consumerArgs, method));
			
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
