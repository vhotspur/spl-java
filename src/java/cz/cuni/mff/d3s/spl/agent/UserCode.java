package cz.cuni.mff.d3s.spl.agent;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.spl.annotations.AtExit;
import cz.cuni.mff.d3s.spl.annotations.AtStart;

public class UserCode {
	private static final Object[] NO_ARGS = {};
	
	public static void runFromClass(String classname) {
		try {
			runFromClassWithExceptions(classname);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
		
	protected static void runFromClassWithExceptions(String classname) throws ReflectiveOperationException {
		Class<?> klass = Class.forName(classname);
		Method[] methods = klass.getMethods();
		for (Method method : methods) {
			AtStart before = method.getAnnotation(AtStart.class);
			if (before != null) {
				method.invoke(null, NO_ARGS);
			}
			AtExit atexit = method.getAnnotation(AtExit.class);
			if (atexit != null) {
				if (shutdown == null) {
					shutdown = ShutdownCode.createAndRegister();
					shutdown.addMethod(method);
				}
			}
		}
	}
	
	private static ShutdownCode shutdown = null;
	
	private static class ShutdownCode implements Runnable {
		private List<Method> methodsToExecute = new LinkedList<>();
		
		private ShutdownCode() {
		}
		
		public static ShutdownCode createAndRegister() {
			ShutdownCode self = new ShutdownCode();
			Thread t = new Thread(self);
			Runtime.getRuntime().addShutdownHook(t);
			return self;
		}
		
		public void addMethod(Method m) {
			methodsToExecute.add(m);
		}
		
		@Override
		public void run() {
			for (Method m : methodsToExecute) {
				try {
					m.invoke(null, NO_ARGS);
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
