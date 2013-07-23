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
package cz.cuni.mff.d3s.spl.demo.imagequality.monitor;

import cz.cuni.mff.d3s.spl.annotations.AtExit;
import cz.cuni.mff.d3s.spl.annotations.AtStart;
import cz.cuni.mff.d3s.spl.core.Data;
import cz.cuni.mff.d3s.spl.core.StatisticSnapshot;
import cz.cuni.mff.d3s.spl.core.impl.PlainBufferDataSource;
import cz.cuni.mff.d3s.spl.instrumentation.CommonExtraArgument;
import cz.cuni.mff.d3s.spl.probe.InstrumentationProbeControllerBuilder;
import cz.cuni.mff.d3s.spl.probe.ProbeController;
import cz.cuni.mff.d3s.spl.utils.StringUtils;

public class Monitor implements Runnable {
	private static final int MILLIS_TO_NANOS = 1000 * 1000;
	private static final int MIN_SAMPLE_COUNT = 5;
	private static final String METHOD = "cz.cuni.mff.d3s.spl.demo.imagequality.html.GraphHandler#handle";
	
	private static ProbeController ctl;
	private static Data data;
	
	private static volatile boolean terminate = false;
	private static Thread monitoringThread;
	
	@AtStart
	public static void prepare() {
		print("Starting...\n");
		
		data = new PlainBufferDataSource(100);
		
		InstrumentationProbeControllerBuilder builder = new InstrumentationProbeControllerBuilder(METHOD);
		builder.setInvocationFilter(new FilterByMethod(), CommonExtraArgument.METHOD_PARAM_1);
		builder.forwardSamplesToDataSource(data);
		
		ctl = builder.get();
		ctl.activate();
		
		monitoringThread = new Thread(new Monitor());
		monitoringThread.setDaemon(true);
		monitoringThread.start();
	}
	
	@AtExit
	public static void finish() {
		terminate = true;
		monitoringThread.interrupt();
		
		StatisticSnapshot stats = data.getStatisticSnapshot();
		
		print("Mean execution time of last few invocations was %s.\n",
				StringUtils.formatTimeUnits(stats.getArithmeticMean()));
	}
	
	@Override
	public void run() {
		double lastAverage = 0.;
		while (!terminate) {
			StatisticSnapshot stats = data.getStatisticSnapshot();
			if (stats.getSampleCount() >= MIN_SAMPLE_COUNT) {
				double averageNow = stats.getArithmeticMean() / (double) MILLIS_TO_NANOS;
				if (bigEnoughDifference(lastAverage, averageNow)) {
					print("NOTE: performance difference recorded: new mean is %.0fms\n", averageNow);
					lastAverage = averageNow;
				}
			}
			try {
				Thread.sleep(100, 0);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private static void print(String fmt, Object... args) {
		System.out.printf("[Monitor]: " + fmt, args);
	}
	
	private static boolean bigEnoughDifference(double avg1, double avg2) {
		double smaller = Math.min(avg1, avg2);
		double bigger = Math.max(avg1, avg2);
		return smaller / bigger < 0.95;
	}
}
