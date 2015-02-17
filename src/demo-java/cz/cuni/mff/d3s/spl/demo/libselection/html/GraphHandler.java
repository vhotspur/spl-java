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
package cz.cuni.mff.d3s.spl.demo.libselection.html;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cz.cuni.mff.d3s.spl.demo.libselection.DataToVisualize;
import cz.cuni.mff.d3s.spl.demo.libselection.Plotter;

/** HTTP handler for drawing adaptive graph.
 */
public class GraphHandler implements HttpHandler {
	private Plotter plotter;
	
	public GraphHandler(Plotter plotter) {
		this.plotter = plotter;
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(200, 0);
				
		OutputStream outputStream = new BufferedOutputStream(exchange.getResponseBody());
		
		plotter.renderPlotToPng(DataToVisualize.get(), 800, 600, outputStream);
		
		exchange.close();
	}

}

