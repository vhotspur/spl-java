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

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;

import cz.cuni.mff.d3s.spl.demo.imagequality.Pair;

/** Plot rendering via GRAL library.
 */
public class XChartPlotter implements Plotter {
	public XChartPlotter() {
	}

	@Override
	public void renderPlotToPng(List<Pair<Long, Long>> dataPairs, int width,
			int height, OutputStream output) throws IOException {
		double[] x = new double[dataPairs.size()];
		double[] y = new double[dataPairs.size()];
		
		pairListToArrays(dataPairs, x, y);
		
		Chart chart = new Chart(width, height);
		chart.addSeries("1", x, y);
		
		byte png[] = BitmapEncoder.getPNGBytes(chart);
		output.write(png);
	}
	
	private static void pairListToArrays(List<Pair<Long, Long>> input, double[] x, double[] y) {
		int index = 0;		
		for (Pair<Long, Long> p : input) {
			x[index] = p.first;
			y[index] = p.second;
			index++;
		}
	}
}
