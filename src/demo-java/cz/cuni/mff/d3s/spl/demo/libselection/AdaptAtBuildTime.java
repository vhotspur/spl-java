/*
 * Copyright 2015 Charles University in Prague
 * Copyright 2015 Vojtech Horky
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
package cz.cuni.mff.d3s.spl.demo.libselection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.spl.buildadapt.BuildTimeAdaptationRunner;
import cz.cuni.mff.d3s.spl.buildadapt.Worker;
import cz.cuni.mff.d3s.spl.demo.imagequality.Pair;
import cz.cuni.mff.d3s.spl.utils.StringUtils;

/** Main server class.
 */
public class AdaptAtBuildTime {
	private static final int MEASUREMENT_TIME = 60;
	private static final String[] ALTERNATIVES = new String[] { "gral", "jfreechart", "xchart" };
	
	public static class NullOutputStream extends OutputStream {
		public static volatile int RESULT;
		@Override
		public void write(int b) throws IOException {
			RESULT = b;
		}
		
	}
	
	private static class MyWorker implements Worker {
		private Plotter plotter;
		private int width;
		private int height;
		private List<Pair<Long, Long>> data;
		private OutputStream devnull;
		
		public MyWorker(Plotter p, int w, int h, List<Pair<Long, Long>> d) {
			plotter = p;
			width = w;
			height = h;
			data = d;
			devnull = new NullOutputStream();
		}

		@Override
		public void run() {
			try {
				plotter.renderPlotToPng(data, width, height, devnull);
			} catch (IOException e) {
				/* Silently ignore. */
			}
		}

		@Override
		public void setup() {
		}

		@Override
		public void teardown() {
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		Map<String, Worker> workers = new HashMap<String, Worker>();
		
		for (String name : ALTERNATIVES) {
			workers.put(name, new MyWorker(PlotterFactory.getPlotter(name), 800, 600, DataToVisualize.get()));
		}
		
		System.out.println("Determining which library is fastest.");
		System.out.printf("Alternatives are %s.\n", StringUtils.join(ALTERNATIVES, ", "));
		System.out.printf("This will take about %d seconds.\n", MEASUREMENT_TIME);
		
		String best = BuildTimeAdaptationRunner.findBest(MEASUREMENT_TIME, 50, workers);
		
		System.out.printf("Looks like %s is the best.\n", best);
		
		String inifile = System.getProperty("demo.libselection.inifile");
		if (inifile != null) {
			writeResultToIniFile(inifile, best);
		}
	}
	
	private static void writeResultToIniFile(String filename, String result) throws FileNotFoundException {
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		PrintWriter writer = new PrintWriter(filename);
		
		writer.printf("# Generated at %s.\n", now);
		writer.println("# This was determined to be the fastest alternative:");
		writer.printf("demo.libselection.plotter=%s\n\n", result);
		
		writer.close();
	}
}
