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

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgument;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArgumentVisitor;
import cz.cuni.mff.d3s.spl.instrumentation.ExtraArguments;
import cz.cuni.mff.d3s.spl.utils.StringUtils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class Utils {
	public static final String INSTRUMENTATION_IDENTIFIERS_PREFIX = "_______SPL_instrumentation_";
	
	public static CtClass getClassFromBytecode(ClassLoader loader,
			String classname, byte[] bytecode) throws NotFoundException {
		ClassPool pool = new ClassPool();
		pool.insertClassPath(new LoaderClassPath(loader));
		
		ByteArrayInputStream bytecodeAsStream = new ByteArrayInputStream(bytecode);
		
		try {
			/*
			 * We need to define the class explicitly.
			 * Adding ByteArrayClassPath to the pool classpath causes that
			 * following calls would work with the original class definition
			 * (that happens despite working with root class pool and
			 * with any combination of defrost/detach).
			 */
			CtClass cc = pool.makeClass(bytecodeAsStream);
			cc.defrost();
			return cc;
		} catch (Exception e) {
			/* This would hardly happen, but anyway. */
			throw new NotFoundException("Making class from byte[] bytecode failed.", e);
		}
	}
	
	public static String extraArgumentToCode(ExtraArgument argument, CtMethod method) {
		ExtraArgumentToCodeConverter converter = new ExtraArgumentToCodeConverter(method);
		argument.accept(converter);
		return converter.getCodePlain();
	}
	
	public static String extraArgumentsToCode(ExtraArguments arguments, CtMethod method) {
		ExtraArgumentToCodeConverter converter = new ExtraArgumentToCodeConverter(method);
		arguments.visit(converter);
		return converter.getCodeArray();
	}
	
	private static class ExtraArgumentToCodeConverter implements ExtraArgumentVisitor {
		private final List<String> codeParts;
		private final CtMethod method;
		
		public ExtraArgumentToCodeConverter(CtMethod instrumentedMethod) {
			method = instrumentedMethod;
			codeParts = new LinkedList<>();
		}
		
		public String getCodePlain() {
			return StringUtils.join(codeParts);
		}
		
		public String getCodeArray() {
			if (codeParts.isEmpty()) {
				return "new Object[0]";
			}
			return String.format("new Object[]{%s}", getCodePlain());
		}
		
		@Override
		public void visitNull() {
			codeParts.add("null");
		}

		@Override
		public void visitThis() {
			codeParts.add("$0");
		}

		@Override
		public void visitField(String name) {
			codeParts.add("$0." + name);
		}

		@Override
		public void visitParameter(int position) {
			CtClass type;
			try {
				type = method.getParameterTypes()[position - 1];
			} catch (NotFoundException e) {
				e.printStackTrace();
				codeParts.add("$0");
				return;
			}
			if (type.isPrimitive()) {
				if (type == CtClass.intType) {
					codeParts.add("new Integer($" + position + ")");
				} else {
					codeParts.add("$" + position);
				}
			} else {
				codeParts.add("$" + position);
			}
		}
	}
}
