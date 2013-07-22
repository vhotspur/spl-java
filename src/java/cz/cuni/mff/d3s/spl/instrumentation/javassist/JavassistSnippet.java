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

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import cz.cuni.mff.d3s.spl.instrumentation.ClassLoaderFilter;
import cz.cuni.mff.d3s.spl.instrumentation.InstrumentationSnippet;

public class JavassistSnippet implements InstrumentationSnippet {
	private ClassLoaderFilter classloaderFilter;
	private Transformer transformer;
	
	public JavassistSnippet(ClassLoaderFilter loaderFilter, Transformer transformer) {
		classloaderFilter = loaderFilter;
		this.transformer = transformer;
	}

	@Override
	public byte[] instrument(ClassLoader loader, Class<?> theClass,
			String classname, byte[] bytecode) {
		if (!classloaderFilter.instrumentClassesFromThisClassLoader(loader)) {
			return null;
		}
		
		/* Javassist (and normal people) use dot-separated names. */
		String dotClassname = classname.replace('/', '.');

		if (!transformer.applyToClass(dotClassname)) {
			return null;
		}

		/* Now, do the actual instrumentation. */
		
		byte[] modifiedBytecode;
		try {
			modifiedBytecode = doInstrument(loader, theClass, dotClassname,
					bytecode);
		} catch (Exception e) {
			// TODO - log the exception?
			e.printStackTrace(System.err);
			return null;
		}

		return modifiedBytecode;
	}

	private byte[] doInstrument(ClassLoader loader, Class<?> theClass,
			String classname, byte[] bytecode) throws NotFoundException, IOException, CannotCompileException {
		/* Load the class and defrost it for transformation. */
		CtClass cc = Utils.getClassFromBytecode(loader, classname, bytecode);

		/* Instrument individual methods. */
		CtMethod[] methods = cc.getMethods();
		for (CtMethod m : methods) {
			/* Only methods declared here. */
			if (!m.getLongName().startsWith(classname)) {
				continue;
			}

			transformer.apply(m);
		}

		byte[] transformedBytecode = cc.toBytecode();

		/*
		 * Without detaching, subsequent calls to the default class pool would
		 * return the already modified class. This ensures that the class is
		 * created in a "fresh" copy.
		 */
		cc.detach();

		return transformedBytecode;
	}

}
