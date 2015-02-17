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

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import cz.cuni.mff.d3s.spl.demo.imagequality.Pair;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.io.plots.DrawableWriter;
import de.erichseifert.gral.io.plots.DrawableWriterFactory;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.util.Insets2D;

/** Plot rendering via GRAL library.
 */
public class GralPlotter implements Plotter {
	private DrawableWriter writer; 
	
	public GralPlotter() {
		DrawableWriterFactory factory = DrawableWriterFactory.getInstance();
		writer = factory.get("image/png");
	}

	@Override
	public void renderPlotToPng(List<Pair<Long, Long>> dataPairs, int width,
			int height, OutputStream output) throws IOException {
		DataTable data = getDataTableFromPairList(dataPairs);
		XYPlot plot = new XYPlot(data);
		LineRenderer lines = new DefaultLineRenderer2D();
		lines.setSetting(LineRenderer.COLOR, Color.BLUE);

		plot.setLineRenderer(data, lines);
		
		plot.getAxisRenderer(XYPlot.AXIS_Y).setSetting(
				AxisRenderer.TICKS_SPACING, 20.0);

		plot.setInsets(new Insets2D.Double(20, 60, 60, 40));
		
		writer.write(plot, output, width, height);
	}
	
	private static DataTable getDataTableFromPairList(List<Pair<Long, Long>> input) {
		@SuppressWarnings("unchecked") // varargs and Long.class don't mix well
		DataTable result = new DataTable(Long.class, Long.class);
		
		for (Pair<Long, Long> p : input) {
			result.add(p.first, p.second);
		}
		return result;
	}
}
