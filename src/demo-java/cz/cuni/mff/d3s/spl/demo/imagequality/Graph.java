package cz.cuni.mff.d3s.spl.demo.imagequality;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.io.plots.DrawableWriter;
import de.erichseifert.gral.io.plots.DrawableWriterFactory;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.util.Insets2D;

public class Graph {
	private DataTable data;
	private XYPlot plot = null;
	private DrawableWriter writer; 
	
	public Graph(DataTable input) {
		data = input;
		
		DrawableWriterFactory factory = DrawableWriterFactory.getInstance();
		writer = factory.get("image/png");
	}
	
	public void render() {
		plot = new XYPlot(data);
		LineRenderer lines = new DefaultLineRenderer2D();
		lines.setSetting(DefaultLineRenderer2D.COLOR, Color.BLUE);

		plot.setLineRenderer(data, lines);
		
		plot.getAxisRenderer(XYPlot.AXIS_Y).setSetting(
				AxisRenderer.TICKS_SPACING, 20.0);

		plot.setInsets(new Insets2D.Double(20, 60, 60, 40));
	}
	
	public void writeAsPng(OutputStream output, int width, int height) throws IOException {
		writer.write(plot, output, width, height);
	}
}
