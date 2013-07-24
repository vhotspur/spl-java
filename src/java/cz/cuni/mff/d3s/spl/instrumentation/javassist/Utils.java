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

import javassist.ClassPool;
import javassist.CtClass;
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
	
}
