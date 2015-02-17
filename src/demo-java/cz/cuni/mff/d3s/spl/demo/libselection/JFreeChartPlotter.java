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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import cz.cuni.mff.d3s.spl.demo.imagequality.Pair;


/** Plot rendering via GRAL library.
 */
public class JFreeChartPlotter implements Plotter {
	@Override
	public void renderPlotToPng(List<Pair<Long, Long>> dataPairs, int width,
			int height, OutputStream output) throws IOException {
		
		XYDataset dataset = getDatasetFromPairList(dataPairs);
		JFreeChart chart = ChartFactory.createXYLineChart("Line Chart Demo",
				"X", "Y", dataset);
		
	 	ChartUtilities.writeChartAsPNG(output, chart, width, height);
	}
	
	private static XYDataset getDatasetFromPairList(List<Pair<Long, Long>> input) {
		XYSeries result = new XYSeries("");
		
		for (Pair<Long, Long> p : input) {
			result.add(p.first, p.second);
		}
		
		return new XYSeriesCollection(result);
	}
}
